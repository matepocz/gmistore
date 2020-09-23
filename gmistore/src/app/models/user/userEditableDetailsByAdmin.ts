import {AddressModel} from "../address-model";

export interface UserEditableDetailsByAdmin {
  id: number;
  username: string;
  lastName: string;
  firstName: string;
  shippingAddress: AddressModel;
  billingAddress?: AddressModel;
  phoneNumber: string;
  roles: Array<string>;
}
