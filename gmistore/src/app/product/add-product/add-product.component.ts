import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../service/product-service";
import {Product} from "../../models/product";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent implements OnInit {

  categories: Map<String, String>;
  productForm: FormGroup;
  product: Product;

  selectedFiles: FileList;
  uploadedPictures: string[];

  constructor(private formBuilder: FormBuilder, private productService: ProductService) {
    this.productForm = new FormGroup({
      id: new FormControl(''),
      name: new FormControl(''),
      productCode: new FormControl(''),
      description: new FormControl(''),
      category: new FormControl(''),
      pictureUrl: new FormControl(null),
      pictures: new FormControl(null),
      price: new FormControl(),
      discount: new FormControl(),
      warrantyMonths: new FormControl(),
      quantityAvailable: new FormControl(),
      quantitySold: new FormControl(),
      ratings: new FormControl(null),
      averageRating: new FormControl(),
      active: new FormControl(false),
      addedBy: new FormControl('')
    });
  }

  ngOnInit(): void {
    this.categories = new Map<String, String>();
    this.uploadedPictures = new Array<string>();
    this.productService.getProductCategories().subscribe(
      (data) => {
        for (let value in data) {
          this.categories.set(value, data[value]);
        }
      }, (error) => console.log(error)
    );
  }

  onSubmit() {
    this.product = this.productForm.value;
    if (this.uploadedPictures.length > 0) {
      this.product.pictureUrl = this.uploadedPictures[0];
    }
    this.product.pictures = this.uploadedPictures;

    this.productService.addProduct(this.product).subscribe((data) => {
      console.log(data);
    }, (error) => {
      console.log(error)
    });
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
    return await this.productService.uploadImage(file).then((data) => {
      this.uploadedPictures.push(data[1]);
      console.log(this.uploadedPictures);
    }, (error) => {
      console.log(error);
    });
  }
}
