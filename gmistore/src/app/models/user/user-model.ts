import {OrderModel} from "../order/order-model";
import {AddressModel} from "../address-model";

export interface UserModel {
  id: number;
  username: string;
  lastName: string;
  firstName: string;
  shippingAddress: AddressModel;
  billingAddress?: AddressModel;
  email: string;
  phoneNumber: string;
  roles?: Array<string>;
  registered: Date;
  orderList?: Array<OrderModel>;

}

