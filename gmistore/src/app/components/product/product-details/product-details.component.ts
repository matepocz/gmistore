import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProductService} from "../../../service/product-service";
import {ProductModel} from "../../../models/product-model";
import {ActivatedRoute, Router} from "@angular/router";
import {RatingModel} from "../../../models/rating-model";
import {CartService} from "../../../service/cart-service";
import {AuthService} from "../../../service/auth-service";
import {LocalStorageService} from "ngx-webstorage";
import {Title} from "@angular/platform-browser";
import {Subscription} from "rxjs";
import {RatingService} from "../../../service/rating.service";
import {SideNavComponent} from "../../side-nav/side-nav.component";
import {HttpErrorResponse} from "@angular/common/http";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "../../loading-spinner/loading-spinner.component";
import {SpinnerService} from "../../../service/spinner-service.service";
import {ConfirmDialog} from "../../confirm-delete-dialog/confirm-dialog";
import {UserService} from "../../../service/user.service";
import {MessageDialogComponent} from "../../message-dialog/message-dialog.component";
import {MessageService} from "../../../service/message.service";
import {PopupSnackbar} from "../../../utils/popup-snackbar";

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit, OnDestroy {

  spinner: MatDialogRef<LoadingSpinnerComponent> = this.spinnerService.start();

  slug: string;
  product: ProductModel;
  defaultPicture: string;

  ratings: Array<RatingModel>;
  ratingByCurrentUser = 0;
  ratedByCurrentUser = false;

  authenticatedUser = this.authService.isAuthenticated();
  currentUsername: string;
  isAdmin: boolean = false;
  isSeller: boolean = false;

  fiveStars = 0;
  fourStars = 0;
  threeStars = 0;
  twoStars = 0;
  oneStar = 0;

  fiveStarPercentage = 0;
  fourStarPercentage = 0;
  threeStarPercentage = 0;
  twoStarPercentage = 0;
  oneStarPercentage = 0;

  subscriptions: Subscription = new Subscription();

  constructor(private route: ActivatedRoute, private router: Router, private productService: ProductService,
              private cartService: CartService, private authService: AuthService,
              private localStorageService: LocalStorageService, private snackBar: PopupSnackbar,
              private titleService: Title, private ratingService: RatingService,
              private sideNavComponent: SideNavComponent, private spinnerService: SpinnerService,
              private dialog: MatDialog, private userService: UserService,
              private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(this.authService.isAdmin.subscribe(
      (response) => {
        this.isAdmin = response;
      }, (error) => {
        this.spinnerService.stop(this.spinner);
        console.log(error);
      }
    ));
    this.authService.usernameSubject.subscribe(
      (username) => {
        this.currentUsername = username;
      }, error => console.log(error)
    )

    this.subscriptions.add(this.authService.isSeller.subscribe(
      (response) => {
        this.isSeller = response;
      }, (error) => {
        this.spinnerService.stop(this.spinner);
        console.log(error);
      }
    ));

    this.slug = this.route.snapshot.params['slug'];

    this.subscriptions.add(this.productService.getProductBySlug(this.slug).subscribe(
      (data: ProductModel) => {
        this.product = data;
        this.defaultPicture = data.pictureUrl;
      }, (error: HttpErrorResponse) => {
        this.spinnerService.stop(this.spinner);
        if (error.error.details === 'Product not found') {
          this.router.navigate(['/not-found'])
        }
      },
      () => {
        this.titleService.setTitle(this.product.name + " - GMI Store")
      }
    ));

    this.subscriptions.add(this.ratingService.getRatingsByProductSlug(this.slug)
      .subscribe(
        (data: Array<RatingModel>) => {
          this.ratings = data;
        }, (error) => {
          this.spinnerService.stop(this.spinner);
          console.log(error);
        },
        () => {
          this.isReviewedByUserAlready();
          this.countRatings();
          this.fiveStarPercentage = this.calculateRatingPercentage(this.fiveStars);
          this.fourStarPercentage = this.calculateRatingPercentage(this.fourStars);
          this.threeStarPercentage = this.calculateRatingPercentage(this.threeStars);
          this.twoStarPercentage = this.calculateRatingPercentage(this.twoStars);
          this.oneStarPercentage = this.calculateRatingPercentage(this.oneStar);
          this.spinnerService.stop(this.spinner);
        }
      ));
  }

  changeDefaultImg(picture: string): void {
    this.defaultPicture = this.product.pictures.find(x => x === picture);
  }

  calculateDiscountedPrice(): number {
    return (this.product.price / 100) * (100 - this.product.discount);
  }

  addToCart(id: number) {
    this.spinner = this.spinnerService.start();
    this.subscriptions.add(this.cartService.addProduct(id).subscribe(
      (response: boolean) => {
        if (response) {
          this.snackBar.popUp("A termék a kosárba került!");
          this.sideNavComponent.updateItemsInCart(0);
        } else {
          this.snackBar.popUp("A kért mennyiség nincs készleten!");
        }
        this.spinnerService.stop(this.spinner);
      }, (error) => {
        console.log(error);
        this.spinnerService.stop(this.spinner);
        this.snackBar.popUp("Valami hiba történt!");
      }
    ));
  }

  isReviewedByUserAlready() {
    this.ratings.forEach((rating) => {
      if (rating.username === this.currentUsername) {
        this.ratingByCurrentUser = rating.actualRating;
        this.ratedByCurrentUser = true;
      }
    })
  }

  countRatings() {
    this.ratings.forEach(
      (rating) => {
        switch (rating.actualRating) {
          case 5:
            this.fiveStars++;
            break;
          case 4:
            this.fourStars++;
            break;
          case 3:
            this.threeStars++;
            break;
          case 2:
            this.twoStars++;
            break;
          case 1:
            this.oneStar++;
            break;
        }
      }
    )
  }

  calculateRatingPercentage(stars: number): number {
    let width = stars / this.ratings?.length;
    return width * 100;
  }

  navigateToRateProduct() {
    this.router.navigate(['/add-review', this.slug])
  }

  removeRating(id: number) {
    this.spinner = this.spinnerService.start();
    this.subscriptions.add(this.ratingService.removeRating(id).subscribe(
      (response) => {
        if (response === true) {
          this.snackBar.popUp("Értékelés törölve!");
        } else {
          this.snackBar.popUp("Nincs jogosultságod a művelethez!");
        }
        this.spinnerService.stop(this.spinner);
      }, (error) => {
        this.spinnerService.stop(this.spinner);
        console.log(error);
      }
    ));
  }

  voteRating(id: number) {
    let actualRating: RatingModel = new RatingModel();
    let username = this.authService.currentUsername;
    this.ratings.find((rating) => {
        if (rating.id === id) {
          actualRating = rating;
        }
      }
    );
    if (!actualRating.voters.includes(username)) {
      this.upVoteRating(id, actualRating);
    } else if (actualRating.voters.includes(username)) {
      this.removeUpVoteRating(id, actualRating);
    }
  }

  private upVoteRating(id: number, rating: RatingModel) {
    this.spinner = this.spinnerService.start();
    this.subscriptions.add(this.ratingService.upVoteRating(id).subscribe(
      () => {
        rating.upVotes++;
        rating.voters.push(this.authService.currentUsername);
        this.spinnerService.stop(this.spinner);
      }, (error) => {
        console.log(error);
        this.spinnerService.stop(this.spinner);
      }
    ));
  }

  private removeUpVoteRating(id: number, rating: RatingModel) {
    this.spinner = this.spinnerService.start();
    this.subscriptions.add(this.ratingService.removeUpVoteRating(id).subscribe(
      () => {
        let username = this.authService.currentUsername;
        let indexOfUsername = 0;
        rating.voters.forEach((name: string) => {
          if (name === username) {
            rating.upVotes--;
            rating.voters.splice(indexOfUsername, 1);
          }
          indexOfUsername++;
        });
        this.spinnerService.stop(this.spinner);
      }, (error) => {
        console.log(error);
        this.spinnerService.stop(this.spinner);
      }
    ));
  }

  reportRating(id: number) {
    this.spinner = this.spinnerService.start();
    this.subscriptions.add(this.ratingService.reportRating(id).subscribe(
      (response) => {
        if (response) {
          this.snackBar.popUp("Jelentés sikeres!");
        }
        this.spinnerService.stop(this.spinner);
      }, (error) => {
        console.log(error);
        this.spinnerService.stop(this.spinner);
        this.snackBar.popUp("Valami hiba történt!");
      }
    ));
  }

  openDeleteProductDialog(productId: number, productName?: string) {
    const dialogRef = this.dialog.open(ConfirmDialog, {
      width: '250px',
      data: {
        message: 'Biztosan törölni szeretnéd?',
        name: productName
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleteProduct(productId);
      }
    });
  }

  deleteProduct(id: number) {
    this.subscriptions.add(this.productService.deleteProduct(id).subscribe(
      (response: boolean) => {
        if (response) {
          this.snackBar.popUp("Termék törölve!");
        } else {
          this.snackBar.popUp("Nincs jogosultságod!");
        }
      }, (error) => {
        this.snackBar.popUp("Valami hiba történt");
        console.log(error);
      }
    ));
  }

  addProductToFavorites(id: number) {
    this.spinner = this.spinnerService.start();
    this.subscriptions.add(this.userService.addProductToFavorites(id).subscribe(
      (response: boolean) => {
        if (response) {
          this.snackBar.popUp("Termék hozzáadva a kedvencekhez.");
        } else {
          this.snackBar.popUp("Valami hiba történt!");
        }
        this.sideNavComponent.updateFavoriteItems(0);
        this.spinnerService.stop(this.spinner);
      }, () => {
        this.snackBar.popUp("Valami hiba történt!");
        this.spinnerService.stop(this.spinner);
      }
    ));
  }

  openNewMessageDialog(receiver: string) {
    const dialogRef = this.dialog.open(MessageDialogComponent, {
      width: '90%',
      data: {
        receiver: receiver,
      }
    });

    this.subscriptions.add(
      dialogRef.afterClosed().subscribe(message => {
        if (message) {
          this.spinner = this.spinnerService.start();
          this.subscriptions.add(
            this.messageService.createMessage(message).subscribe(
              (response) => {
                if (response) {
                  this.snackBar.popUp("Üzenet sikeresen elküldve!");
                }
                this.spinnerService.stop(this.spinner);
              }, (error) => {
                console.log(error);
                this.spinnerService.stop(this.spinner);
              }
            )
          );
        }
      })
    );
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
