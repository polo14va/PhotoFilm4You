import { Component } from '@angular/core';
import { Category } from 'src/app/models/category';
import { CategoryService } from '../../../services/category/category.service';

@Component({
  selector: 'app-category-details',
  templateUrl: './category-details.component.html'
})
export class CategoryDetailsComponent {
  public categories: Category[]  = [];

  constructor(
    private categoryService: CategoryService
    ) { }

  ngOnInit() {
    this.getAllCategories();
  }

  getAllCategories() {
    this.categoryService.getCategories().subscribe(
      (data: Category[]) => {
        this.categories = data;
      },
      (error) => {
        console.error('Error fetching categories:', error);
      }
    );
  }

}
