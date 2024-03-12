import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';  // Importa ReactiveFormsModule
import { HttpClientModule } from '@angular/common/http';
import { UpdateProductStockComponent } from './update-product-stock.component';

describe('UpdateProductStockComponent', () => {
  let component: UpdateProductStockComponent;
  let fixture: ComponentFixture<UpdateProductStockComponent>;

  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientModule, ReactiveFormsModule],  // Agrega ReactiveFormsModule
    declarations: [UpdateProductStockComponent]
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateProductStockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the updateproductstock component', () => {
    expect(component).toBeTruthy();
  });
});
