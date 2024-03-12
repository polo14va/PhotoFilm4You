import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category } from '../../models/category';


@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private baseUrl: string = 'http://localhost:18081';

  constructor(private http: HttpClient) { }

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.baseUrl}/categories`);
  }

  // Register a new category
  registerCategory(category: Category): Observable<any> {
    if (category.parentId === 0) category.parentId = null;
    const url = `${this.baseUrl}/categories`;
    return this.http.post(url, category);
  }
}
