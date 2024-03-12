import { Product } from './product';

export interface Shopping {
  userId: number;
  product: Product;
  quantity: number;
  price: number;
  tax: number;
  rentId:number; //Id of the cart/order line
  orderId:number; //Id of the cart/order
}
