import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProductService} from "../../service/product-service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Product} from "../../models/product";
import {ActivatedRoute, Router} from "@angular/router";
import {errorHandler} from "../../utils/errorHandler";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-product-edit',
  templateUrl: './product-edit.component.html',
  styleUrls: ['./product-edit.component.css']
})
export class ProductEditComponent implements OnInit, OnDestroy {

  product: Product;
  loading = false;
  selectedFiles: FileList;
  productPictures: string[];
  categories: Map<string, string>;
  productForm: FormGroup;
  currentCategory: string;

  categorySubscription: Subscription;
  productSubscription: Subscription;
  updateProductSubscription: Subscription;

  constructor(private productService: ProductService, private formBuilder: FormBuilder,
              private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.categories = new Map<string, string>();
    let slug = this.route.snapshot.params['slug'];

    this.productForm = this.formBuilder.group({
      id: [''],
      name: [''],
      slug: [''],
      productCode: [''],
      description: ['', Validators.compose([Validators.required, Validators.minLength(10)])],
      category: ['', Validators.required],
      pictureUrl: [''],
      pictures: [''],
      price: ['', Validators.compose([Validators.required, Validators.min(0)])],
      discount: ['', Validators.compose(
        [Validators.required, Validators.min(0), Validators.max(100)])],
      warrantyMonths: ['', Validators.compose(
        [Validators.required, Validators.min(0), Validators.max(360)])],
      quantityAvailable: ['', Validators.compose([Validators.required, Validators.min(0)])],
      active: [''],
      addedBy: ['']
    });

    this.categorySubscription = this.productService.getProductCategories().subscribe(
      (data) => {
        for (let value in data) {
          this.categories.set(value, data[value].toString());
        }
      }, (error) => {
        console.warn(error)
      });

    this.productSubscription = this.productService.getProductBySlug(slug).subscribe(
      (data) => {
        this.product = data;
      }, (error) => {
        console.log(error);
      }, () => {

        this.productPictures = this.product.pictures;
        this.categories.forEach((value, key) => {
          if (value === this.product.category) {
            this.currentCategory = key;
          }
        })
        this.productForm.patchValue({

          id: this.product.id,
          name: this.product.name,
          slug: this.product.slug,
          productCode: this.product.productCode,
          description: this.product.description,
          category: this.currentCategory,
          pictureUrl: '',
          pictures: this.product.pictures,
          price: this.product.price,
          discount: this.product.discount,
          warrantyMonths: this.product.warrantyMonths,
          quantityAvailable: this.product.quantityAvailable,
          active: this.product.active,
          addedBy: this.product.addedBy
        });
        console.log(this.productForm.get('pictureUrl'));
        console.log(this.product.pictureUrl);
      });
    this.loading = false;
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
    return await this.productService.uploadImage(file)
      .then((data) => {
        this.productPictures.push(data[1]);
        this.loading = false;
      }, (error) => {
        console.log(error);
      });
  }

  removeImage(picture: string) {
    let indexOfImage = this.productPictures.indexOf(picture);
    this.productPictures.splice(indexOfImage, 1);
  }

  onSubmit() {
    this.loading = true;
    this.product = {...this.productForm.value};

    this.updateProductSubscription =
      this.productService.updateProduct(this.product, this.product.slug).subscribe(
        (data) => {
        },
        (error) => {
          errorHandler(error, this.productForm)
        },
        () => {
          this.router.navigate(['/product', this.product.slug]);
        });
    this.loading = false;
  }

  ngOnDestroy() {
    this.categorySubscription.unsubscribe();
    this.productSubscription.unsubscribe();
    if (this.updateProductSubscription) {
      this.updateProductSubscription.unsubscribe();
    }
  }
}
