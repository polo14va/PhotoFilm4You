import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Item } from 'src/app/models/item';

@Injectable({
  providedIn: 'root'
})
export class ItemService {
  private baseUrl: string = 'http://localhost:18081';

  constructor(private http: HttpClient) { }

  getItems(): Observable<Item[]> {
    return this.http.get<Item[]>(`${this.baseUrl}/items`);
  }

  // Register a new item
  registerItem(item: Item): Observable<any> {
    const url = `${this.baseUrl}/items`;
    return this.http.post(url, item);
  }

  // Set item as operational/non_operational
  setOperational(serialNumber: string, operational: boolean): Observable<any> {
    const url = `${this.baseUrl}/items/${serialNumber}`;
    return this.http.patch(url, operational);
  }
}
