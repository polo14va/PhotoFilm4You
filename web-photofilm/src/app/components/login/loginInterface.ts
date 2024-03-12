export interface LoginInterface {
  email: string;
  password: string;
}


export interface LoginResponse {
  token?: string;
  error?: string;
}
