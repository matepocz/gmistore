import {AddressModel} from "../address-model";

export interface UserEditableDetailsByUser {
  username: string;
  lastName: string;
  firstName: string;
  shippingAddress: AddressModel;
  billingAddress: AddressModel;
  phoneNumber: string;
}
