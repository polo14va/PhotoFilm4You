import { Component } from '@angular/core';
import { Product } from 'src/app/models/product';
import { Router } from '@angular/router';
import { ProductService } from 'src/app/services/product/product.service';

@Component({
  selector: 'app-update-product',
  templateUrl: './update-product.component.html'
})
export class UpdateProductComponent {
  public products: Product[] = [];

  constructor(private productService: ProductService, private router: Router) {}

  ngOnInit() {
    this.getAllProducts();
  }

  getAllProducts() {
    this.productService.getProducts().subscribe(
      (data: Product[]) => {
        this.products = data;
      },
      (error) => {
        console.error('Error fetching products:', error);
      }
    );
  }

  update(product: Product) {
    this.productService.registerProduct(product).subscribe(
      (response) => {
        // Create OK
        this.productService.removeProduct(product.id.toString()).subscribe(
          (response) => {
            alert('Producto actualizado satisfactoriamente de la base de datos');
            this.reloadComponent();
          },
          (error) => {
            console.error('Error al actualizar el producto:', error);
          }
        );
      },
      (error) => {
        console.error('Error al actualizar el producto:', error);
      }
    );
  }

  delete(product: Product) {
    this.productService.removeProduct(product.id.toString()).subscribe(
      (response) => {
        // Registro OK
        alert('Producto borrado satisfactoriamente de la base de datos');
        this.reloadComponent();
      },
      (error) => {
        console.error('Error al borrar el producto:', error);
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
