import {OrderModel} from "./order-model";
import {AddressModel} from "./address-model";

export interface UserModel {
  id: number;
  username: string;
  lastName: string;
  firstName: string;
  shippingAddress: AddressModel;
  billingAddress?: AddressModel;
  email: string;
  phoneNumber: string;
  roles: Array<string>;
  registered: Date;
  active: boolean;
  orderList: Array<OrderModel>;

}

