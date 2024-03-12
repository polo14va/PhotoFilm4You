export interface User {
  id?:          number;
  fullName:    string;
  email:       string;
  role:        string;
  password?:    string;
  password2?:    string;
  phoneNumber: string;
  address? : string;
  city? : string;
  token?:      string;
  sendEmail?:  boolean;
}
