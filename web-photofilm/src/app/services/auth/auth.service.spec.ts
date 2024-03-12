import { TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { first } from 'rxjs';
import { AuthService } from './auth.service';
import { Product } from 'src/app/models/product';

describe('AuthService', () => {
  let authService: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule, HttpClientTestingModule],
      providers: [AuthService],
    });

    // Get the service and the HTTP testing controller from the TestBed
    authService = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Ensure that there are no outstanding HTTP requests
    httpMock.verify();
  });

  it('should be created', () => {
    expect(authService).toBeTruthy();
  });

  describe('addAlert', () => {
    it('should return a number', () => {
      // Arrange
      const productMock: Product = {
        id: 1,
        name: 'Sample Product',
        description: 'Sample Description',
        dailyPrice: 10,
        brand: 'Sample Brand',
        model: 'Sample Model',
        categoryId: 1,
        imageName: 'sample.png',
      };
      const userIdMock: string = '1';
      const expectedNumber: number = 1;

      // Act
      authService.addAlert(productMock, userIdMock)
        .pipe(first())
        .subscribe((data) => {
          // Assert
          expect(data).toBeInstanceOf(Number);
          expect(data).toEqual(expectedNumber as any);
        });

      // Assert
      const req = httpMock.expectOne('http://localhost:18082/alerts');
      expect(req.request.method).toBe('POST');

      // Act
      req.flush(expectedNumber); // Simulate server response
    });
  });
});
