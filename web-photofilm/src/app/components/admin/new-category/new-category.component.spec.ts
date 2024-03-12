import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { NewCategoryComponent } from './new-category.component';
import { ReactiveFormsModule } from '@angular/forms';
import { CategoryDetailsComponent } from '../category-details/category-details.component';

describe('NewCategoryComponent', () => {
  let component: NewCategoryComponent;
  let fixture: ComponentFixture<NewCategoryComponent>;

  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientModule, ReactiveFormsModule],  // Agrega ReactiveFormsModule
    declarations: [NewCategoryComponent, CategoryDetailsComponent]
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the NewCategory component', () => {
    expect(component).toBeTruthy();
  });
});
