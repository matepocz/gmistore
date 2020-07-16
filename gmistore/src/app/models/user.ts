import {OrderModel} from "./orderModel";

export interface User {
  id: number;
  username:string;
  lastName: string;
  firstName: string;
  address: string;
  email: string;
  phoneNumber: string;
  roles:Array<string>;
  registered: any;
  active: boolean;
  orderList:Array<OrderModel>;

}

