import {AddressModel} from "../address-model";

export interface OrderUserDetailsModel {
  id: number;
  lastName: string;
  firstName: string;
  shippingAddress: AddressModel;
  billingAddress?: AddressModel;
  email: string;
  phoneNumber: string;
}

