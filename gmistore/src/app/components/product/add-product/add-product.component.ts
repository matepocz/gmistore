import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../../service/product-service";
import {ProductModel} from "../../../models/product-model";
import {FormBuilder, Validators} from "@angular/forms";
import {errorHandler} from "../../../utils/error-handler";
import {LocalStorageService} from "ngx-webstorage";

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent implements OnInit {

  categories: Map<String, String>;
  product: ProductModel;
  selectedFiles: FileList;
  uploadedPictures: string[];
  loading = false;

  productForm = this.formBuilder.group({
    'id': [null],
    'name': [null, Validators.compose(
      [Validators.required, Validators.minLength(3), Validators.maxLength(200)])],
    'productCode': [null, Validators.compose(
      [Validators.required, Validators.minLength(3), Validators.maxLength(30)])],
    'description': [null, Validators.compose(
      [Validators.required, Validators.minLength(10)])],
    'category': [null, Validators.required],
    'pictureUrl': [null],
    'pictures': [null],
    'price': [null, Validators.compose([Validators.required, Validators.min(0)])],
    'discount': [null, Validators.compose(
      [Validators.required, Validators.min(0), Validators.max(100)])],
    'warrantyMonths': [null, Validators.compose(
      [Validators.required, Validators.min(0), Validators.max(360)])],
    'quantityAvailable': [null, Validators.compose([Validators.required, Validators.min(0)])],
    'active': [false],
  });

  constructor(private formBuilder: FormBuilder, private productService: ProductService,
              private localStorageService: LocalStorageService) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.categories = new Map<String, String>();
    this.uploadedPictures = new Array<string>();
    this.productService.getProductCategories().subscribe(
      (data) => {
        for (let value in data) {
          this.categories.set(value, data[value]);
        }
        this.loading = false;
      }, (error) => {
        console.warn(error);
      }
    );
  }

  onSubmit() {
    this.loading = true;
    this.product = this.productForm.value;
    if (this.uploadedPictures.length > 0) {
      this.product.pictureUrl = this.uploadedPictures[0];
    }
    this.product.pictures = this.uploadedPictures;
    this.product.addedBy = this.localStorageService.retrieve("username");

    this.productService.addProduct(this.product).subscribe(
      () => {
      }, (error) => {
        errorHandler(error, this.productForm);
      }
    )
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
    return await this.productService.uploadImage(file).then((data) => {
      this.uploadedPictures.push(data[1]);
      console.log(this.uploadedPictures);
      this.loading = false;
    }, (error) => {
      console.log(error);
    });
  }

  removeImage(picture: string) {
    let indexOfImage = this.uploadedPictures.indexOf(picture);
    this.uploadedPictures.splice(indexOfImage, 1);
  }
}
