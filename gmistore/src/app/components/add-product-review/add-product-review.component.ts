import {Component, OnDestroy, OnInit} from '@angular/core';
import {ClickEvent} from "angular-star-rating";
import {RatingService} from "../../service/rating.service";
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {RatingInitDataModel} from "../../models/rating-init-data-model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {AddRatingRequestModel} from "../../models/add-rating-request-model";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-add-product-review',
  templateUrl: './add-product-review.component.html',
  styleUrls: ['./add-product-review.component.css']
})
export class AddProductReviewComponent implements OnInit, OnDestroy {

  ratingForm: FormGroup;
  product: RatingInitDataModel;
  slug: string;

  pictures: Array<string> = new Array<string>();
  ratingInputValue: number;
  ratingData: AddRatingRequestModel;

  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'bottom';
  activatedRouteSubscription: Subscription;
  initDataSubscription: Subscription;
  newRatingSubscription: Subscription;

  constructor(private ratingService: RatingService, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.activatedRouteSubscription = this.activatedRoute.paramMap.subscribe(
      (params) => {
        this.slug = params.get('slug');
        console.log(this.slug);

        this.initDataSubscription = this.ratingService.getInitData(this.slug).subscribe(
          (response: RatingInitDataModel) => {
            console.log(response);
            this.product = response;
          }, (error) => {
            console.log(error);
          },
          () => {
            this.ratingForm = this.formBuilder.group({
              product: [null],
              actualRating: [null],
              title: [null],
              positiveComment: [null],
              negativeComment: [null],
              pictures: [null]
            })
          }
        );
      }, (error) => {
        console.log(error);
      }, () => {
      }
    )
  }

  getRatingInputValue = ($event: ClickEvent) => {
    this.ratingInputValue = $event.rating;
  }

  onSubmit() {
    this.ratingData = this.ratingForm.value;
    this.ratingData.product = this.slug;
    this.ratingData.actualRating = this.ratingInputValue;
    this.ratingData.pictures = this.pictures;
    this.newRatingSubscription = this.ratingService.addNewRating(this.ratingData).subscribe(
      () => {},
      (error: HttpErrorResponse) => {
        this.openSnackBar('Valami hiba történt!');
      },
      () => {
      }
    );
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'OK', {
      duration: 2000,
      horizontalPosition: this.horizontalPosition,
      verticalPosition: this.verticalPosition,
    });
  }

  ngOnDestroy() {
    this.activatedRouteSubscription.unsubscribe();
    this.initDataSubscription.unsubscribe();
    if (this.newRatingSubscription) {
      this.newRatingSubscription.unsubscribe();
    }
  }
}
