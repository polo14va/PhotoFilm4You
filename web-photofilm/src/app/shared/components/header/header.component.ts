import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { ProductService } from 'src/app/services/product/product.service';
import { Product } from '../../../models/product';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  searchTerm: string = '';
  userActual?: User;
  showResults: boolean = false;
  searchResults: Product[] = [];
  searchResultsFiltered: Product[] = [];
  private searchTerms = new Subject<string>();
  showDropdown = false;
  isAdmin: boolean = false;

  constructor(
    private productService: ProductService,
    private router: Router,
    private cookieService: CookieService
  ) {}

  ngOnInit() {
    this.retrieveSessionUser();
    this.initializeSearch();
  }

  // Initialize the product search functionality
  private initializeSearch(): void {
    this.searchTerms.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap((term: string) => this.productService.search(term))
    ).subscribe(results => {
      this.searchResults = results;
      this.showResults = results.length > 0;
    });
  }

  // Retrieve the user from session and check for admin role
  private retrieveSessionUser(): void {
    const userSession = sessionStorage.getItem('USER');
    if (userSession) {
      this.userActual = JSON.parse(userSession);
      this.isAdmin = this.userActual?.role === 'ADMIN';
    }
  }

  // Toggle the dropdown menu
  toggleDropdown(show: boolean): void {
    this.showDropdown = show;
  }

  // Handle user logout
  logout(): void {
    this.cookieService.delete('Token');
    sessionStorage.removeItem('USER');
    this.userActual = undefined;
    this.showDropdown = false;
  }

  // Search products based on the term
  search(term: string, event?: Event): void {
    if (event) {
      event.preventDefault(); // Prevent page reload
    }

    if (!term.trim()) {
      this.showResults = false;
      return;
    }
    this.searchResultsFiltered = this.filterResults(this.searchTerm);
    this.showResults = true;
    this.searchTerms.next(term);
  }

  filterResults(term: string): any[] {
    return this.searchResults.filter(product => product.name.toLowerCase().includes(term.toLowerCase()));
  }

  // Navigate to a specific product
  public goToProduct(productId: number): void {
    this.resetSearch();
    this.router.navigate(['/product/', productId.toString()]);
  }

  // Reset the search state
  private resetSearch(): void {
    this.showResults = false;
    this.searchTerm = '';
    this.searchResults = [];
  }

  // Hide the search results
  hideSearchResults(): void {
    setTimeout(() => {
      this.showResults = false;
    }, 200);
  }

  // Navigate to the login page
  navigateToLogin(): void {
    this.router.navigate(['/login']);
  }
}
