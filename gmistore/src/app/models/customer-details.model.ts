import {AddressModel} from "./address-model";

export interface CustomerDetailsModel {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  shippingAddress: AddressModel;
  billingAddress: AddressModel;
}
