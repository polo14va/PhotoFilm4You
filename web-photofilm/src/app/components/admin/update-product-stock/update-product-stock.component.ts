import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Product } from 'src/app/models/product';
import { Item } from 'src/app/models/item';
import { Router } from '@angular/router';
import { ProductService } from 'src/app/services/product/product.service';
import { ItemService } from 'src/app/services/item/item.service';

@Component({
  selector: 'app-update-product-stock',
  templateUrl: './update-product-stock.component.html'
})
export class UpdateProductStockComponent {
  newProductToStockForm: FormGroup;
  public products: Product[] = [];
  public items: Item[] = [];
  groupedItems: any = {};

  constructor(
    private productService: ProductService,
    private itemService: ItemService,
    private router: Router,
    private fb: FormBuilder) {
      this.newProductToStockForm = this.fb.group({
        productId: [1, Validators.required],
        serialNumber: ['', Validators.required]
      });
    }

  ngOnInit() {
    this.getAllProducts();
  }


  onSubmit() {
    if (this.newProductToStockForm.valid) {
      const item: Item = this.newProductToStockForm.value;
      item.productId = Number(item.productId)
      this.registerItem(item);
    }
  }

  registerItem(item: Item) {
    this.itemService.registerItem(item).subscribe(
      (response) => {
        // Registro OK
        alert("Producto con referencia añadido satisfactoriamente en la base de datos")
        this.reloadComponent();
      },
      (error) => {
        console.error('Error al registrar el producto con referencia:', error);
      }
    );
  }

  getAllProducts() {
    this.productService.getProducts().subscribe(
      (data: Product[]) => {
        this.products = data;
        this.getAllItems();
      },
      (error) => {
        console.error('Error fetching products:', error);
      }
    );
  }

  getAllItems() {
    this.itemService.getItems().subscribe(
      (data: Item[]) => {
        this.items = data;
        this.groupItemsByProductId();
      },
      (error) => {
        console.error('Error fetching items:', error);
      }
    );
  }

  groupItemsByProductId() {
    this.groupedItems = {};
    this.items.forEach(item => {
      if (!this.groupedItems[item.productId]) {
        this.groupedItems[item.productId] = [];
      }
      this.groupedItems[item.productId].push(item);
    });
  }

  setOperational(itemGroup: any){
    const status: boolean = itemGroup.status === 'OPERATIONAL' ? false : true;
    this.itemService.setOperational(itemGroup.serialNumber, status).subscribe(
      (response) => {
        // Registro OK
        alert('Producto cambiado de estado satisfactoriamente de la base de datos');
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
