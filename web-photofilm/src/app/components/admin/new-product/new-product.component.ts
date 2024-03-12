import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { Product } from 'src/app/models/product';
import { Router } from '@angular/router';
import { ProductService } from '../../../services/product/product.service';

@Component({
  selector: 'app-new-product',
  templateUrl: './new-product.component.html',
})
export class NewProductComponent {
  newProductForm: FormGroup;

  constructor(
    private productService: ProductService,
    private router: Router,
    private fb: FormBuilder) {
      this.newProductForm = this.fb.group({
        name: ['', Validators.required],
        description: ['', Validators.required],
        categoryId: [0, Validators.required],
        dailyPrice: [0, [Validators.required, this.priceValidator]],
        brand: ['', Validators.required],
        model: ['', Validators.required],
      });
  }

  priceValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const id = control.value;
    const regex = /^\d{1,3}(\.\d{1,2})?$/;
    const valid = regex.test(id);
    return valid ? null : { 'pattern': true };
  }

  onSubmit() {
    if (this.newProductForm.valid) {
      const product: Product = this.newProductForm.value;
      this.registerProduct(product);
    }
  }

  registerProduct(product: Product) {
    this.productService.registerProduct(product).subscribe(
      (response) => {
        // Registro OK
        alert("Producto añadido satisfactoriamente en la base de datos")
        this.reloadComponent();
      },
      (error) => {
        console.error('Error al registrar el producto:', error);
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
