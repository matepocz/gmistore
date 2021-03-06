import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProductService} from "../../../service/product-service";
import {ProductModel} from "../../../models/product-model";
import {FormBuilder, Validators} from "@angular/forms";
import {errorHandler} from "../../../utils/error-handler";
import {LocalStorageService} from "ngx-webstorage";
import {Subscription} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {CdkDragDrop, moveItemInArray} from "@angular/cdk/drag-drop";
import {ProductCategoryModel} from "../../../models/product-category.model";
import {MatSelectChange} from "@angular/material/select";
import {COMMA, ENTER} from "@angular/cdk/keycodes";
import {MatChipInputEvent} from "@angular/material/chips";
import {MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "../../loading-spinner/loading-spinner.component";
import {SpinnerService} from "../../../service/spinner-service.service";
import {ImageService} from "../../../service/image.service";
import {AuthService} from "../../../service/auth-service";

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.css']
})
export class ProductFormComponent implements OnInit, OnDestroy {

  spinner: MatDialogRef<LoadingSpinnerComponent> = this.spinnerService.start();

  selectableDetails: boolean = true;
  removableDetails: boolean = true;
  addOnBlur: boolean = true;
  readonly separatorKeysCodes: number[] = [ENTER, COMMA];
  productFeatures: Array<string> = new Array<string>();

  mainProductCategories: Array<ProductCategoryModel>;
  subProductCategories: Array<ProductCategoryModel> = new Array<ProductCategoryModel>();

  product: ProductModel;
  productName: string;
  productCode: string;
  currentMainCategory: ProductCategoryModel;
  currentSubCategory: ProductCategoryModel;

  selectedFiles: FileList;
  productPictures: string[];
  loading: boolean = false;
  slug: string;

  currentUsername: string;

  productForm = this.formBuilder.group({
    id: [null],
    name: [null, Validators.compose(
      [Validators.required, Validators.minLength(3), Validators.maxLength(200)])],
    productCode: [null, Validators.compose(
      [Validators.required, Validators.minLength(3), Validators.maxLength(30)])],
    description: [null, Validators.compose(
      [Validators.required, Validators.minLength(10)])],
    mainCategory: [null, Validators.required],
    subCategory: [null, Validators.required],
    pictureUrl: [null],
    pictures: [null],
    price: [null, Validators.compose([Validators.required, Validators.min(0)])],
    discount: [null, Validators.compose(
      [Validators.required, Validators.min(0), Validators.max(100)])],
    warrantyMonths: [null, Validators.compose(
      [Validators.required, Validators.min(0), Validators.max(360)])],
    quantityAvailable: [null, Validators.compose([Validators.required, Validators.min(0)])],
    active: [false],
  });

  subscriptions: Subscription = new Subscription();

  constructor(private formBuilder: FormBuilder, private productService: ProductService,
              private localStorageService: LocalStorageService, private activatedRoute: ActivatedRoute,
              private titleService: Title, private router: Router, private spinnerService: SpinnerService,
              private imageService: ImageService, private authService: AuthService) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.subscriptions.add(
      this.authService.usernameSubject.subscribe(
        (response: string) => {
          this.currentUsername = response;
        }, (error) => {
          console.log(error);
        }
      )
    );
    this.titleService.setTitle("Új termék");
    this.fetchMainCategories();

    this.productPictures = new Array<string>();

    this.subscriptions.add(this.activatedRoute.paramMap.subscribe(
      (data) => {
        this.slug = data.get('slug');
        this.spinnerService.stop(this.spinner);
        if (this.slug) {
          this.spinner = this.spinnerService.start();
          this.loading = true;
          this.productForm.get('name').disable();
          this.productForm.get('productCode').disable();
          this.titleService.setTitle("Termék szerkesztése");

          this.subscriptions.add(this.productService.getProductBySlug(this.slug).subscribe(
            (data: ProductModel) => {
              this.product = data;
              this.productName = this.product.name;
              this.productCode = this.product.productCode;
              this.currentMainCategory = data.mainCategory;
              this.currentSubCategory = data.subCategory;
              this.productFeatures = data.features;
              this.fetchCurrentSubCategories();
            }, (error) => {
              console.log(error);
              this.spinnerService.stop(this.spinner);
            }, () => {
              this.productPictures = this.product.pictures;
              this.pathValueProductForm();
              this.loading = false;
              this.spinnerService.stop(this.spinner);
            }));
        }
      }, (error) => {
        console.log(error);
        this.spinnerService.stop(this.spinner);
      }
    ));
  }

  fetchMainCategories() {
    this.subscriptions.add(this.productService.getMainProductCategories().subscribe(
      (response: Array<ProductCategoryModel>) => {
        this.mainProductCategories = response;
        this.loading = false;
      }, (error) => {
        console.log(error);
        this.loading = false;
      }
    ));
  }

  pathValueProductForm() {
    this.productForm.patchValue({
      id: this.product.id,
      name: this.product.name,
      slug: this.product.slug,
      productCode: this.product.productCode,
      description: this.product.description,
      mainCategory: this.currentMainCategory.id,
      subCategory: this.currentSubCategory.key,
      pictureUrl: this.product.pictureUrl,
      pictures: this.product.pictures,
      price: this.product.price,
      discount: this.product.discount,
      warrantyMonths: this.product.warrantyMonths,
      quantityAvailable: this.product.quantityAvailable,
      active: this.product.active,
      addedBy: this.product.addedBy
    });
  }

  fetchCurrentSubCategories() {
    this.subscriptions.add(this.productService.getSubProductCategories(this.currentMainCategory.id).subscribe(
      (response: Array<ProductCategoryModel>) => {
        this.subProductCategories = response;
      }, (error) => {
        console.log(error);
      }
    ));
  }

  fetchSubCategories($event: MatSelectChange) {
    if ($event.value !== undefined) {
      this.spinner = this.spinnerService.start();
      this.loading = true;
      this.setCurrentMainCategory($event);
      this.subscriptions.add(this.productService.getSubProductCategories($event.value).subscribe(
        (response: Array<ProductCategoryModel>) => {
          this.subProductCategories = response;
          this.loading = false;
          this.spinnerService.stop(this.spinner);
        }, (error) => {
          console.log(error);
          this.loading = false;
          this.spinnerService.stop(this.spinner);
        }
      ));
    }
  }

  setCurrentMainCategory($event: MatSelectChange) {
    this.mainProductCategories.forEach(
      (category) => {
        if (category.id === $event.value) {
          this.currentMainCategory = category;
        }
      }
    );
  }

  setCurrentSubCategory($event: MatSelectChange) {
    this.subProductCategories.forEach(
      (category) => {
        if (category.key === $event.value) {
          this.currentSubCategory = category;
        }
      }
    );
  }

  addFeature($event: MatChipInputEvent) {
    const input = $event.input;
    const value = $event.value;

    if ((value || '').trim() && this.productFeatures.length < 5) {
      this.productFeatures.push(value.trim());
    }
    if (input) {
      input.value = '';
    }
  }

  removeFeature(feature: string): void {
    const index = this.productFeatures.indexOf(feature);

    if (index >= 0) {
      this.productFeatures.splice(index, 1);
    }
  }

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.productPictures, event.previousIndex, event.currentIndex);
  }

  onSubmit() {
    this.loading = true;
    this.product = this.productForm.value;
    if (this.productPictures.length > 0) {
      this.product.pictureUrl = this.productPictures[0];
    }
    this.product.mainCategory = this.currentMainCategory;
    this.product.subCategory = this.currentSubCategory;
    this.product.pictures = this.productPictures;
    this.product.features = this.productFeatures;

    if (this.slug) {
      this.processProductUpdate();
    } else {
      this.processNewProduct();
    }
    this.loading = false;
  }

  processNewProduct() {
    this.spinner = this.spinnerService.start();
    this.product.addedBy = this.currentUsername;
    this.subscriptions.add(this.productService.addProduct(this.product).subscribe(
      (response) => {
        console.log(response);
        this.spinnerService.stop(this.spinner);
      }, (error) => {
        errorHandler(error, this.productForm);
        this.spinnerService.stop(this.spinner);
      }, () => {
        this.spinnerService.stop(this.spinner);
        this.router.navigate(['product-list']);
      }
    ));
  }

  processProductUpdate() {
    this.spinner = this.spinnerService.start();
    this.product.name = this.productName;
    this.product.productCode = this.productCode;
    this.subscriptions.add(this.productService.updateProduct(this.product, this.slug).subscribe(
      () => {
      },
      (error) => {
        errorHandler(error, this.productForm);
        this.spinnerService.stop(this.spinner);
      },
      () => {
        this.spinnerService.stop(this.spinner);
        this.navigateBack();
      }
    ))
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
      this.productPictures.push(data[1]);
      this.loading = false;
    }, (error) => {
      this.loading = false;
      console.log(error);
    });
  }

  removeImage(picture: string) {
    this.spinner = this.spinnerService.start();
    let indexOfImage = this.productPictures.indexOf(picture);
    this.subscriptions.add(
      this.imageService.destroyImage(picture).subscribe(
        (response) => {
          this.productPictures.splice(indexOfImage, 1);
          this.spinnerService.stop(this.spinner);
        }, (error) => {
          this.spinnerService.stop(this.spinner);
          console.log(error);
        }
      )
    );
  }

  navigateBack() {
    if (this.slug) {
      this.router.navigate(['/product', this.slug]);
    } else {
      this.router.navigate(['/']);
    }
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
