import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from 'src/app/models/product';
import { Category } from 'src/app/models/category';
import { ProductService } from '../../services/product/product.service';
import { CategoryService } from 'src/app/services/category/category.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { Alert } from 'src/app/models/Alert';
import { tap, catchError } from 'rxjs/operators';
import { CookieService } from 'ngx-cookie-service';
import { UserService } from 'src/app/services/user/user.service';
import { PageEvent } from '@angular/material/paginator'; //Deal with pagination
import { Catalog } from 'src/app/models/catalog';
import { FilterClass } from 'src/app/models/filter';


@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrl: './catalog.component.css',
  styles: []
})
export class CatalogComponent implements OnInit {
  public products: Product[]  = [];
  public produtAlert: Alert[] = [];
  user: any;
  totalElements: number = 0;
  public allproducts: Product[] = [];
  public filter: FilterClass = new FilterClass('Todas las marcas', 0, 0, 0)
  public maxPrice: number = 0;
  public brands: string[] = [];
  public idCategories: number[] = [];
  public categories: Category[] = [{
    "id": 0,
    "name": "Todas las categorias",
    "description": "Todas las categorias",
    "parentId": null
  }];
  public pageIndex: number = 0;
  public pageSize: number = 6;


  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private authService: AuthService,
    private router: Router,
    private cookieService: CookieService,
    private userService: UserService
    ) { }

  ngOnInit() {
    this.leerSesion();
    this.getPageProducts(0, 100);
    this.getAllCategories();
  }

  getAllCategories() {
    this.categoryService.getCategories().subscribe(
      (data: Category[]) => {
        this.categories.push(...data);
        this.getAllProducts();
      },
      (error) => {
        console.error('Error fetching categories:', error);
      }
    );
  }

  getAllProducts() {
    this.productService.getProducts().subscribe(
      (data: Product[]) => {
        this.allproducts = data;
        //Set image setImageName
        this.allproducts.forEach(product => this.setImageName(product));
        this.getBrandsCategoriesMaxPrice();
      },
      (error) => {
        console.error('Error fetching products:', error);
      }
    );
  }  

  getBrandsCategoriesMaxPrice(){
    // Extract unique brands, categories, and find the maximum price
    let uniqueBrands = new Set<string>();
    let uniqueCategories = new Set<number>();

    uniqueBrands.add(this.filter.brand);

    for (const p of this.allproducts) {
      uniqueBrands.add(p.brand);
      uniqueCategories.add(p.categoryId);
      this.maxPrice = Math.max(this.maxPrice, p.dailyPrice);
    }
    this.filter.priceMax = this.maxPrice;
    //casting
    this.brands = Array.from(uniqueBrands);
    this.idCategories = Array.from(uniqueCategories);
  }

  applyFilters(){
    this.products = this.allproducts.filter((product) => {
      const brandFilter = this.filter.brand === 'Todas las marcas' || product.brand === this.filter.brand;
      const categoryFilter = this.filter.categoryId == 0 || this.checkCategoryHierarchy(product.categoryId, this.filter.categoryId);
      const priceFilter = product.dailyPrice >= this.filter.priceMin && product.dailyPrice <= this.filter.priceMax;
  
      return brandFilter && categoryFilter && priceFilter;
    });
    this.totalElements = this.products.length;
  }

  // Helper method to check if the product's category or its parent category matches the selected category
  checkCategoryHierarchy(productCategoryId: number, selectedCategoryId: number): boolean {
    const productCategory = this.categories.find(category => category.id === productCategoryId);
    if (productCategory) {
      return productCategory.id == selectedCategoryId || (!!productCategory.parentId && productCategory.parentId == selectedCategoryId);
    }
    return false;
  }

  getPageProducts(index: number, size: number) {
    this.productService.getCatalog(index, size).subscribe(
      (data: Catalog[]) => {
        this.totalElements = (data as any)['totalElements'];
        this.allproducts = (data as any)['content'];
        //Set image setImageName
        this.allproducts.forEach(product => this.setImageName(product));
        this.products = this.allproducts;
      },
      (error) => {
        console.error('Error fetching products:', error);
      }
    );
  }

  nextPage(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.getPageProducts(this.pageIndex/100, 100);
}

  setImageName(product: Product){
    if (product.name && product.imageName === undefined) {
      // Modify name, replace " " for "_" add .png at the end
      product.imageName = product.name.replace(/\s+/g, '_') + '.png';
    }
  }

  leerSesion() {
   const userFromSessionStorage = sessionStorage.getItem('USER');

    if (userFromSessionStorage) {
      this.user = JSON.parse(userFromSessionStorage);
      this.authService.getAlertsByUserId(this.user.id).subscribe(
        (response) => {
          this.produtAlert = response;
        },
        (error) => {
          console.error('Error al obtener alerta:', error);
        }
      );
    }
  }

  // Método para agregar el producto a las alertas
  addAlert(product: Product) {
    this.authService.addAlert(product, this.user.id)
      .pipe(
        tap(response => {
          alert("alerta creada satisfactoriamente")
          this.reloadComponent();
        }),
        catchError(error => {
          console.error('Error al agregar alerta:', error);
          throw error; // Re-throw the error to propagate it to the next error handler
        })
      )
      .subscribe();
  }

    // Method to reload the component
  private reloadComponent() {
    const currentRoute = this.router.url;
    this.router.navigateByUrl('./', { skipLocationChange: true }).then(() => {
      this.router.navigate([currentRoute]);
    });
  }

  //Adds a product to users cart
  addToCart(product:Product) {

    this.userService.addToCart(product.id, this.user.id)
    .pipe(
      tap(response => {
        this.reloadComponent();
      }),
      catchError(error => {
        console.error('Error al agregar alerta:', error);
        throw error; // Re-throw the error to propagate it to the next error handler
      })
    )
    .subscribe();
    }
}
