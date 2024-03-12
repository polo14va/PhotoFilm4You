import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { HeaderComponent } from './shared/components/header/header.component';

describe('AppComponent', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [RouterTestingModule, HttpClientModule, FormsModule],
    declarations: [AppComponent, HeaderComponent]
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'WebPhotofilm'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('WebPhotofilm');
  });


  it('should contain HeaderComponent', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    // Verify if HeaderComponent is present in the render
    const headerComponent = fixture.debugElement.nativeElement.querySelector('app-header');
    expect(headerComponent).toBeTruthy();
  });

  it('should contain RouterOutlet', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    // Verify if RouterOutletComponent is present in the render
    const RouterOutletComponent = fixture.debugElement.nativeElement.querySelector('router-outlet');
    expect(RouterOutletComponent).toBeTruthy();
  });
});
