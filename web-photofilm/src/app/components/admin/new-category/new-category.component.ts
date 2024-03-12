import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { Category } from 'src/app/models/category';
import { Router } from '@angular/router';
import { CategoryService } from '../../../services/category/category.service';


@Component({
  selector: 'app-new-category',
  templateUrl: './new-category.component.html'
})
export class NewCategoryComponent {
  newCategoryForm: FormGroup;

  constructor(
    private categoryService: CategoryService,
    private router: Router,
    private fb: FormBuilder) {
      this.newCategoryForm = this.fb.group({
        name: ['', Validators.required],
        description: ['', Validators.required],
        parentId: [0, [Validators.required, this.parentIdValidator ]]
      });
  }


  parentIdValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const id = control.value;
    const regex = /^\d+$/;
    const valid = regex.test(id);
    return valid ? null : { 'pattern': true };
  }

  onSubmit() {
    if (this.newCategoryForm.valid) {
      const category: Category = this.newCategoryForm.value;
      this.registerCategory(category);
    }
  }

  registerCategory(category: Category) {
    this.categoryService.registerCategory(category).subscribe(
      (response) => {
        // Registro OK
        this.reloadComponent();
        alert("Categoria añadida satisfactoriamente en la base de datos")
      },
      (error) => {
        console.error('Error al registrar la categoria:', error);
      }
    );
  }

  // Method to reload the component
  private reloadComponent() {
    const currentRoute = this.router.url;
    this.router.navigateByUrl('./', { skipLocationChange: true }).then(() => {
      this.router.navigate([currentRoute]);
    });
  }
}
