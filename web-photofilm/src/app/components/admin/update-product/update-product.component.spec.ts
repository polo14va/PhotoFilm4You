import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UpdateProductComponent } from './update-product.component';
import { HttpClientModule } from '@angular/common/http';

describe('UpdateProductComponent', () => {
  let component: UpdateProductComponent;
  let fixture: ComponentFixture<UpdateProductComponent>;

  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientModule],
    declarations: [UpdateProductComponent]
  }));
    
  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the updateproduct component', () => {
    expect(component).toBeTruthy();
  });
});
