import {Component, OnDestroy, OnInit} from '@angular/core';
import {ClickEvent} from "angular-star-rating";
import {RatingService} from "../../service/rating.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {RatingInitDataModel} from "../../models/rating-init-data-model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AddRatingRequestModel} from "../../models/add-rating-request-model";
import {HttpErrorResponse} from "@angular/common/http";
import {SpinnerService} from "../../service/spinner-service.service";
import {PopupSnackbar} from "../../utils/popup-snackbar";
import {MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "../loading-spinner/loading-spinner.component";
import {CdkDragDrop, moveItemInArray} from "@angular/cdk/drag-drop";
import {ImageService} from "../../service/image.service";
import {errorHandler} from "../../utils/error-handler";

@Component({
  selector: 'app-add-product-review',
  templateUrl: './add-product-review.component.html',
  styleUrls: ['./add-product-review.component.css']
})
export class AddProductReviewComponent implements OnInit, OnDestroy {

  loading: boolean = false;
  product: RatingInitDataModel;
  slug: string;

  selectedFiles: FileList;
  pictures: Array<string> = new Array<string>();
  ratingInputValue: number;
  ratingData: AddRatingRequestModel;

  ratingForm: FormGroup = this.formBuilder.group({
    product: [null],
    actualRating: [null],
    title: [null, Validators.maxLength(100)],
    positiveComment: [null, Validators.compose(
      [Validators.required, Validators.minLength(3), Validators.maxLength(1000)])],
    negativeComment: [null, Validators.compose(
      [Validators.required, Validators.minLength(3), Validators.maxLength(1000)])],
    pictures: [null]
  })

  spinner: MatDialogRef<LoadingSpinnerComponent> = this.spinnerService.start();

  subscriptions: Subscription = new Subscription();

  constructor(private ratingService: RatingService, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private snackbar: PopupSnackbar,
              private router: Router, private spinnerService: SpinnerService,
              private imageService: ImageService) {
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
          this.snackbar.popUp("Köszönjük az értékelést!");
          this.spinnerService.stop(this.spinner);
        },
        (error: HttpErrorResponse) => {
          errorHandler(error, this.ratingForm);
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
    return await this.imageService.uploadImage(file).then((data) => {
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
    this.spinner = this.spinnerService.start();
    let indexOfImage = this.pictures.indexOf(picture);
    this.subscriptions.add(
      this.imageService.destroyImage(picture).subscribe(
        (response) => {
          if (response) {
            this.snackbar.popUp("Sikeres törlés!");
            this.pictures.splice(indexOfImage, 1);
          } else {
            this.snackbar.popUp("Valami hiba történt!");
          }
          this.spinnerService.stop(this.spinner);
        }, (error) => {
          this.spinnerService.stop(this.spinner);
          console.log(error);
        }
      )
    );
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
