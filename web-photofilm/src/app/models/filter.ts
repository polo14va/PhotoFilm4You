export class FilterClass {
    brand: string;
    categoryId: number;
    priceMin: number;
    priceMax: number;
  
    constructor(brand: string, categoryId: number, priceMin: number, priceMax: number) {
      this.brand = brand;
      this.categoryId = categoryId;
      this.priceMin = priceMin;
      this.priceMax = priceMax;
    }
  }