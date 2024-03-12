import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { RegisterModalComponent } from './register-modal.component';
import { RegisterComponent } from '../register/register.component';
import { ReactiveFormsModule } from '@angular/forms';

describe('RegisterModalComponent', () => {
  let component: RegisterModalComponent;
  let fixture: ComponentFixture<RegisterModalComponent>;

  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientModule, ReactiveFormsModule],
    declarations: [RegisterModalComponent, RegisterComponent]
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the NewCategory component', () => {
    expect(component).toBeTruthy();
  });
});
