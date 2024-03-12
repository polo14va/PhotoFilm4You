import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../../models/product';
import {Catalog} from '../../models/catalog';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private baseUrl: string = 'http://localhost:18081';

  constructor(private http: HttpClient) { }

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/products/`);
  }

  getCatalog(page: number, size: number) {
    return this.http.get<Catalog[]>(`${this.baseUrl}/products/catalog?page=${page}&size=${size}`);
  }

  getProductById(productId: string): Observable<Product | undefined> {
    const url = `${this.baseUrl}/products/${productId}`;
    return this.http.get<Product | undefined>(url);
  }

  search(term: string): Observable<Product[]> {
    const url = `${this.baseUrl}/products/?name=${term}`;
    return this.http.get<Product[]>(url);
  }

  registerProduct(product: Product): Observable<any> {
    const url = `${this.baseUrl}/products`;
    return this.http.post(url, product);
  }

  removeProduct(productId: string): Observable<any> {
    const url = `${this.baseUrl}/products/${productId}`;
    return this.http.delete(url);
  }

}
