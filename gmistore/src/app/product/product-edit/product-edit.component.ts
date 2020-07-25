import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../service/product-service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Product} from "../../models/product";
import {ActivatedRoute, Router} from "@angular/router";
import {errorHandler} from "../../utils/errorHandler";

@Component({
  selector: 'app-product-edit',
  templateUrl: './product-edit.component.html',
  styleUrls: ['./product-edit.component.css']
})
export class ProductEditComponent implements OnInit {

  product: Product;
  loading = false;
  selectedFiles: FileList;
  productPictures: string[];
  categories: Map<String, String>;
  productForm: FormGroup;

  productId: number;
  productSlug: string;
  productAddedBy: string;

  constructor(private productService: ProductService, private formBuilder: FormBuilder,
              private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.categories = new Map<String, String>();
    this.productService.getProductCategories().subscribe(
      (data) => {
        for (let value in data) {
          this.categories.set(value, data[value]);
        }
      }, (error) => {
        console.warn(error);
      }
    );
    let slug = this.route.snapshot.params['slug'];
    this.productService.getProductBySlug(slug).subscribe((data) => {
      this.product = data;
    }, (error) => {
      console.log(error);
    }, () => {
      this.productId = this.product.id;
      this.productSlug = this.product.slug;
      this.productPictures = this.product.pictures;
      this.productAddedBy = this.product.addedBy;
      this.productForm = this.formBuilder.group({
        'name': [this.product.name],
        'productCode': [this.product.productCode],
        'description': [this.product.description, Validators.compose(
          [Validators.required, Validators.minLength(10)])],
        'category': [this.product.category, Validators.required],
        'pictureUrl': [this.product.pictureUrl],
        'pictures': [this.product.pictures],
        'price': [this.product.price, Validators.compose([Validators.required, Validators.min(0)])],
        'discount': [this.product.discount, Validators.compose(
          [Validators.required, Validators.min(0), Validators.max(100)])],
        'warrantyMonths': [this.product.warrantyMonths, Validators.compose(
          [Validators.required, Validators.min(0), Validators.max(360)])],
        'quantityAvailable': [this.product.quantityAvailable, Validators.compose([Validators.required, Validators.min(0)])],
        'active': [this.product.active],
      });
      this.loading = false;
    });
  }

  onSubmit() {
    this.loading = true;
    this.product = this.productForm.value;
    this.product.id = this.productId;
    this.product.slug = this.productSlug;
    this.product.addedBy = this.productAddedBy;
    this.productService.updateProduct(this.product, this.product.slug).subscribe(
      (data) => {
      }, (error) => {
        errorHandler(error, this.productForm);
      }, () => {
        this.router.navigate(['/product', this.product.slug]);
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
}
