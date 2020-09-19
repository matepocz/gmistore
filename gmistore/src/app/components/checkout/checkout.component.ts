import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CartService} from "../../service/cart-service";
import {AuthService} from "../../service/auth-service";
import {OrderService} from "../../service/order.service";
import {Subscription} from "rxjs";
import {CustomerDetailsModel} from "../../models/customer-details.model";
import {CartModel} from "../../models/cart-model";
import {Title} from "@angular/platform-browser";
import {SideNavComponent} from "../side-nav/side-nav.component";
import {OrderRequestModel} from "../../models/order-request.model";
import {Router} from "@angular/router";
import {PaymentMethodDetailsModel} from "../../models/payment-method-details.model";
import {errorHandler} from "../../utils/error-handler";
import {AddressModel} from "../../models/address-model";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "../loading-spinner/loading-spinner.component";
import {SpinnerService} from "../../service/spinner-service.service";

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit, OnDestroy {

  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'bottom';

  spinner: MatDialogRef<LoadingSpinnerComponent> = this.spinnerService.start();
  authenticatedUser: boolean;

  cartDetails: CartModel;
  customerDetails: CustomerDetailsModel;
  orderRequest: OrderRequestModel;
  paymentMethods: Array<PaymentMethodDetailsModel>;
  chosenPaymentMethod: PaymentMethodDetailsModel;

  detailsForm: FormGroup;
  shippingAddressForm: FormGroup;
  billingAddressForm: FormGroup;

  customerDetailsSub: Subscription;
  cartSub: Subscription;
  createOrderSub: Subscription;
  paymentMethodsSub: Subscription;

  constructor(private formBuilder: FormBuilder, private cartService: CartService,
              private authService: AuthService, private orderService: OrderService,
              private titleService: Title, private sideNav: SideNavComponent, private router: Router,
              private snackBar: MatSnackBar, private spinnerService: SpinnerService) {
  }

  ngOnInit(): void {
    this.titleService.setTitle("Kosár véglegesítése - GMI Store")
    this.authenticatedUser = this.authService.isAuthenticated();

    this.cartSub = this.cartService.getCart().subscribe(
      (response) => {
        this.cartDetails = response;
        if (this.cartDetails.cartItems.length <= 0) {
          this.spinnerService.stop(this.spinner);
          this.router.navigate(['/cart']);
        }
      }, (error) => {
        console.log(error);
        this.spinnerService.stop(this.spinner);
      },
      () => {
        this.spinnerService.stop(this.spinner);
      }
    );

    this.detailsForm = this.formBuilder.group({
      firstName: [null, Validators.compose(
        [Validators.required, Validators.minLength(3), Validators.maxLength(100)])],
      lastName: [null, Validators.compose(
        [Validators.required, Validators.minLength(3), Validators.maxLength(100)])],
      email: [null, Validators.compose([Validators.required, Validators.email])],
      phoneNumber: [null, Validators.required]
    });

    this.shippingAddressForm = this.makeNewAddressForm();
    this.billingAddressForm = this.makeNewAddressForm();

    this.paymentMethodsSub = this.orderService.getPaymentMethods().subscribe(
      (response) => {
        this.paymentMethods = response;
        if (this.paymentMethods.length > 0) {
          this.chosenPaymentMethod = this.paymentMethods[0];
        }
      }, (error) => {
        console.log(error);
      }
    )

    if (this.authenticatedUser) {
      this.customerDetailsSub = this.orderService.getCustomerDetails().subscribe(
        (response) => {
          console.log(response);
          this.customerDetails = response;
          this.pathDetailsForm();
          CheckoutComponent.patchAddressForm(this.shippingAddressForm, this.customerDetails.shippingAddress);
          CheckoutComponent.patchAddressForm(this.billingAddressForm, this.customerDetails.billingAddress);
        }, (error) => {
          console.log(error);
        },
        () => {
        }
      )
    }
  }

  private makeNewAddressForm() {
    return this.formBuilder.group({
      id: [null],
      city: [null,
        Validators.compose([Validators.required, Validators.minLength(3)])],
      street: [null,
        Validators.compose([Validators.required, Validators.minLength(2)])],
      number: [null,
        Validators.compose([Validators.required, Validators.min(1)])],
      floor: [null],
      door: [null],
      country: [null,
        Validators.compose([Validators.required, Validators.minLength(3)])],
      postcode: [null,
        Validators.compose([Validators.required, Validators.minLength(3)])]
    });
  }

  private pathDetailsForm() {
    this.detailsForm.patchValue({
      firstName: this.customerDetails.firstName,
      lastName: this.customerDetails.lastName,
      email: this.customerDetails.email,
      phoneNumber: this.customerDetails.phoneNumber
    });
  }

  private static patchAddressForm(form: FormGroup, address: AddressModel) {
    if (address !== null) {
      form.patchValue({
        id: address.id,
        city: address.city,
        street: address.street,
        number: address.number,
        floor: address.floor,
        door: address.door,
        country: address.country,
        postcode: address.postcode
      });
    }
  }

  copyShippingDetails() {
    this.spinner = this.spinnerService.start();
    this.billingAddressForm.patchValue({
      id: this.shippingAddressForm.get('id').value,
      city: this.shippingAddressForm.get('city').value,
      street: this.shippingAddressForm.get('street').value,
      number: this.shippingAddressForm.get('number').value,
      floor: this.shippingAddressForm.get('floor').value,
      door: this.shippingAddressForm.get('door').value,
      country: this.shippingAddressForm.get('country').value,
      postcode: this.shippingAddressForm.get('postcode').value,
    });
    this.spinnerService.stop(this.spinner);
  }

  onSubmit() {
    this.spinner = this.spinnerService.start();
    this.orderRequest = this.detailsForm.value;
    this.orderRequest.shippingAddress = this.shippingAddressForm.value;
    this.orderRequest.billingAddress = this.billingAddressForm.value;
    this.orderRequest.paymentMethod = this.chosenPaymentMethod.paymentMethod;

    this.createOrderSub = this.orderService.createOrder(this.orderRequest).subscribe(
      (response) => {
        console.log(response);
        this.openSnackBar("Sikeres vásárlás!");
      }, (error) => {
        console.log(error);
        errorHandler(error, this.detailsForm);
        this.spinnerService.stop(this.spinner);
      },
      () => {
        this.sideNav.updateItemsInCart(0);
        this.spinnerService.stop(this.spinner);
      }
    )
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'OK', {
      duration: 2000,
      horizontalPosition: this.horizontalPosition,
      verticalPosition: this.verticalPosition,
    });
  }

  ngOnDestroy() {
    this.cartSub.unsubscribe();
    this.paymentMethodsSub.unsubscribe();
    if (this.customerDetailsSub) {
      this.customerDetailsSub.unsubscribe();
    }
    if (this.createOrderSub) {
      this.createOrderSub.unsubscribe();
    }
  }
}
