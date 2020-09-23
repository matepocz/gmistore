import {OrderItemModel} from "./orderItemModel";
import {AddressModel} from "../address-model";
import {OrderUserDetailsModel} from "./orderUserDetailsModel";
import {ShippingMethodModel} from "../shipping-method-model";
import {OrderStatusOptionsModel} from "./orderStatusOptionsModel";

export interface OrderDetails {
  generatedUniqueId: string;
  status: OrderStatusOptionsModel[];
  items: OrderItemModel[];
  shippingMethod?: ShippingMethodModel;
  paymentMethod: string;
  deliveryAddress: AddressModel;
  invoiceAddress: AddressModel;
  deliveryCost: number;
  totalPrice: number;
  orderedAt: Date;
  expectedDeliveryDate: Date;
  deliveredAt: Date;
  user: OrderUserDetailsModel;
}
