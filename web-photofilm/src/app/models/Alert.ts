import { User } from "./user";

export interface Alert {
  id:        string;
  from:      string;
  to:        string;
  productId: string;
  user:      User;
}
