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

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.css']
})
export class ProductFormComponent implements OnInit, OnDestroy {

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

  mainCategorySub: Subscription;
  subCategorySub: Subscription;
  productSubscription: Subscription;
  submitProductSub: Subscription;
  paramMapSub: Subscription;

  constructor(private formBuilder: FormBuilder, private productService: ProductService,
              private localStorageService: LocalStorageService, private activatedRoute: ActivatedRoute,
              private titleService: Title, private router: Router) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.titleService.setTitle("Új termék");
    this.fetchMainCategories();

    this.productPictures = new Array<string>();

    this.paramMapSub = this.activatedRoute.paramMap.subscribe(
      (data) => {
        this.slug = data.get('slug');
        if (this.slug) {
          this.loading = true;
          this.productForm.get('name').disable();
          this.productForm.get('productCode').disable();
          this.titleService.setTitle("Termék szerkesztése");

          this.productSubscription = this.productService.getProductBySlug(this.slug).subscribe(
            (data) => {
              this.product = data;
              this.productName = this.product.name;
              this.productCode = this.product.productCode;
              this.currentMainCategory = data.mainCategory;
              this.currentSubCategory = data.subCategory;
              this.fetchCurrentSubCategories();
            }, (error) => {
              console.log(error);
            }, () => {
              this.productPictures = this.product.pictures;
              this.pathValueProductForm();
              this.loading = false;
            });
        }
      }
    )
  }

  fetchMainCategories() {
    this.mainCategorySub = this.productService.getMainProductCategories().subscribe(
      (response: Array<ProductCategoryModel>) => {
        this.mainProductCategories = response;
        this.loading = false;
      }, (error) => {
        console.log(error);
        this.loading = false;
      }
    );
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
    this.subCategorySub = this.productService.getSubProductCategories(this.currentMainCategory.id).subscribe(
      (response: Array<ProductCategoryModel>) => {
        this.subProductCategories = response;
      }, (error) => {
        console.log(error);
      }
    );
  }

  fetchSubCategories($event: MatSelectChange) {
    if ($event.value !== undefined) {
      this.loading = true;
      this.setCurrentMainCategory($event);
      this.subCategorySub = this.productService.getSubProductCategories($event.value).subscribe(
        (response: Array<ProductCategoryModel>) => {
          this.subProductCategories = response;
          this.loading = false;
        }, (error) => {
          console.log(error);
          this.loading = false;
        }
      );
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

    if (this.slug) {
      this.processProductUpdate();
    } else {
      this.processNewProduct();
    }
    this.loading = false;
  }

  processNewProduct() {
    this.product.addedBy = this.localStorageService.retrieve("username");
    this.submitProductSub = this.productService.addProduct(this.product).subscribe(
      () => {
      }, (error) => {
        errorHandler(error, this.productForm);
      }, () => {
        this.router.navigate(['product-list']);
      }
    );
  }

  processProductUpdate() {
    this.product.name = this.productName;
    this.product.productCode = this.productCode;
    this.submitProductSub = this.productService.updateProduct(this.product, this.slug).subscribe(
      () => {
      },
      error => errorHandler(error, this.productForm),
      () => {
        this.router.navigate(['product', this.slug]);
      }
    )
  }

  onFileChange(event) {
    if (event.target.files.length > 0) {
      this.selectedFiles = event.target.files;
      this.uploadFiles();
    }
  }

  uploadFiles() {
    for (let i = 0; i < this.selectedFiles.length; i++) {
      this.upload(this.selectedFiles[i]);
    }
  }

  async upload(file) {
    this.loading = true;
    return await this.productService.uploadImage(file).then((data) => {
      this.productPictures.push(data[1]);
      console.log(this.productPictures);
      this.loading = false;
    }, (error) => {
      console.log(error);
    });
  }

  removeImage(picture: string) {
    let indexOfImage = this.productPictures.indexOf(picture);
    this.productPictures.splice(indexOfImage, 1);
  }

  ngOnDestroy() {
    this.paramMapSub.unsubscribe();
    this.mainCategorySub.unsubscribe();
    if (this.subCategorySub) {
      this.subCategorySub.unsubscribe();
    }
    if (this.productSubscription) {
      this.productSubscription.unsubscribe();
    }
    if (this.submitProductSub) {
      this.submitProductSub.unsubscribe();
    }
  }
}
