import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../utils/product-service";
import {Product} from "../product";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent implements OnInit {

  pictureUrl: string;
  categories: Array<String>;

  productForm: FormGroup;
  product: Product;
  selectedValue: string;

  constructor(private formBuilder: FormBuilder, private productService: ProductService) {
    this.productForm = new FormGroup({
      id: new FormControl(''),
      name: new FormControl(''),
      description: new FormControl(''),
      category: new FormControl(''),
      pictureUrl: new FormControl(''),
      pictures: new FormControl(null),
      price: new FormControl(0),
      discount: new FormControl(0),
      warrantyMonths: new FormControl(0),
      quantityAvailable: new FormControl(0),
      quantitySold: new FormControl(0),
      ratings: new FormControl(null),
      averageRating: new FormControl(0),
      active: new FormControl(false),
      addedBy: new FormControl('')
    });
    this.product = {
      id: 0,
      name: '',
      description: '',
      category: '',
      pictureUrl: '',
      pictures: null,
      price: 0,
      discount: 0,
      warrantyMonths: 0,
      quantityAvailable: 0,
      quantitySold: 0,
      ratings: null,
      averageRating: 0,
      active: false,
      addedBy: ''
    }
  }

  ngOnInit(): void {
    this.categories = new Array<String>();
    this.productService.getProductCategories().subscribe(
      data => {
        this.categories = data;
      }, error => console.log(error)
    );


  }

  onSubmit() {
    this.product.name = this.productForm.get('name').value;
    this.product.description = this.productForm.get('description').value;
    this.product.category = this.productForm.get('category').value;
    this.product.pictureUrl = this.productForm.get('pictureUrl').value;
    this.product.price = this.productForm.get('price').value;
    this.product.discount = this.productForm.get('discount').value;
    this.product.warrantyMonths = this.productForm.get('warrantyMonths').value;
    this.product.quantityAvailable = this.productForm.get('quantityAvailable').value;

    this.productService.addProduct(this.product).subscribe(data => {
      console.log('success')
    }, error => {
      console.log('failed')
      console.log(this.product)
    });
  }

  processFile($event: Event) {

  }
}
