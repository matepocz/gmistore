import {Component, OnDestroy, OnInit} from '@angular/core';
import {ClickEvent} from "angular-star-rating";
import {RatingService} from "../../service/rating.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {RatingInitDataModel} from "../../models/rating-init-data-model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {AddRatingRequestModel} from "../../models/add-rating-request-model";
import {HttpErrorResponse} from "@angular/common/http";
import {SpinnerService} from "../../service/spinner-service.service";
import {PopupSnackbar} from "../../utils/popup-snackbar";
import {MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "../loading-spinner/loading-spinner.component";
import {ProductService} from "../../service/product-service";
import {CdkDragDrop, moveItemInArray} from "@angular/cdk/drag-drop";

@Component({
  selector: 'app-add-product-review',
  templateUrl: './add-product-review.component.html',
  styleUrls: ['./add-product-review.component.css']
})
export class AddProductReviewComponent implements OnInit, OnDestroy {

  loading: boolean = false;
  ratingForm: FormGroup;
  product: RatingInitDataModel;
  slug: string;

  selectedFiles: FileList;
  pictures: Array<string> = new Array<string>();
  ratingInputValue: number;
  ratingData: AddRatingRequestModel;

  spinner: MatDialogRef<LoadingSpinnerComponent> = this.spinnerService.start();

  subscriptions: Subscription = new Subscription();

  constructor(private ratingService: RatingService, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private snackbar: PopupSnackbar,
              private router: Router, private spinnerService: SpinnerService,
              private productService: ProductService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(this.activatedRoute.paramMap.subscribe(
      (params: ParamMap) => {
        this.spinnerService.stop(this.spinner);
        this.slug = params.get('slug');
        this.subscriptions.add(this.ratingService.getInitData(this.slug).subscribe(
          (response: RatingInitDataModel) => {
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
        ));
      }, (error) => {
        console.log(error);
      }
    ));
  }

  getRatingInputValue = ($event: ClickEvent) => {
    this.ratingInputValue = $event.rating;
  }

  onSubmit() {
    this.spinner = this.spinnerService.start();
    this.ratingData = this.ratingForm.value;
    this.ratingData.product = this.slug;
    this.ratingData.actualRating = this.ratingInputValue;
    this.ratingData.pictures = this.pictures;
    this.subscriptions.add(
      this.ratingService.addNewRating(this.ratingData).subscribe(
        () => {
          this.spinnerService.stop(this.spinner);
        },
        (error: HttpErrorResponse) => {
          this.spinnerService.stop(this.spinner);
          this.snackbar.popUp('Valami hiba történt! Mindent kitöltöttél?');
        },
        () => {
          this.router.navigate(['/product', this.slug]);
        }
      )
    );
  }

  onFileChange(event) {
    if (event.target.files.length > 0) {
      this.selectedFiles = event.target.files;
      this.uploadFiles();
    }
  }

  uploadFiles() {
    this.spinner = this.spinnerService.start();
    for (let i = 0; i < this.selectedFiles.length; i++) {
      if (this.selectedFiles[i].type.startsWith('image')) {
        this.upload(this.selectedFiles[i]).then(
          () => this.spinnerService.stop(this.spinner),
          () => this.spinnerService.stop(this.spinner)
        );
      } else {
        this.spinnerService.stop(this.spinner);
      }
    }
  }

  async upload(file) {
    this.loading = true;
    return await this.productService.uploadImage(file).then((data) => {
      this.pictures.push(data[1]);
      this.loading = false;
    }, (error) => {
      this.loading = false;
      console.log(error);
    });
  }

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.pictures, event.previousIndex, event.currentIndex);
  }

  removeImage(picture: string) {
    let indexOfImage = this.pictures.indexOf(picture);
    this.pictures.splice(indexOfImage, 1);
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
