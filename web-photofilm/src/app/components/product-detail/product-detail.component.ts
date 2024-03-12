import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Product } from 'src/app/models/product';
import { ProductService } from 'src/app/services/product/product.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {
  product: Product | undefined; // Define una propiedad para almacenar los detalles del producto

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService,// Inyecta tu servicio
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const productId = params.get('id');

      // Verifica si productId no está vacío antes de hacer la solicitud
      if (productId) {
        this.productService.getProductById(productId).subscribe(
          (product) => {
            if (product) {
              this.product = product;
              this.setImageName();
            } else {
              this.router.navigate(['/not-found']);
            }
          },
          (error) => {
            console.error('Error cargando los detalles del producto:', error);
            // Puedes manejar el error de alguna manera apropiada, como mostrar un mensaje de error.
          }
        );
      } else {
        // Si productId está vacío, puedes redirigir a una página de error o realizar alguna otra acción.
        this.router.navigate(['/not-found']);
      }
    });
  }

  setImageName(){
    if (this.product! && this.product.name && this.product.imageName === undefined) {
      // Modify name, replace " " for "_" add .png at the end
      this.product.imageName = this.product.name.replace(/\s+/g, '_') + '.png';
    }
  }
}
