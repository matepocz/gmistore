import {OrderModel} from "./orderModel";
import {AddressModel} from "./addressModel";

export interface User {
  id: number;
  username: string;
  lastName: string;
  firstName: string;
  address: AddressModel;
  email: string;
  phoneNumber: string;
  roles: Array<string>;
  registered: Date;
  active: boolean;
  orderList: Array<OrderModel>;

}

