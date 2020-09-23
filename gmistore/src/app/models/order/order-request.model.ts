import {AddressModel} from "../address-model";

export interface OrderRequestModel {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  shippingAddress: AddressModel;
  billingAddress: AddressModel;
  paymentMethod: string;
}
