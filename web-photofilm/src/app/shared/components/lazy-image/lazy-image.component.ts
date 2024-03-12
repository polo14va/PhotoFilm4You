import { Component, Input, ViewChild, ElementRef, AfterViewInit,ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-lazy-image',
  templateUrl: './lazy-image.component.html'
})
export class LazyImageComponent implements AfterViewInit {
  @Input() imageName!: string;
  @ViewChild('imageElement') imageElement!: ElementRef;

  notFoundImageSrc = 'assets/notfound.svg'; // URL de la imagen predeterminada
  isLoading = true; // Inicialmente, el spinner estará visible


  constructor(private cdr: ChangeDetectorRef) {}

  ngAfterViewInit(): void {
    if (this.imageName && this.imageName.trim() !== '') {
      this.loadImage();
    } else {
      this.isLoading = false;
      this.cdr.detectChanges();
    }
  }

  loadImage() {
    if (!this.imageName || this.imageName.trim() === '') {
      this.isLoading = false;
      return; // Sale si imageName no es válido
    }

    const img = new Image();
    img.src = this.getSafeImageUrl(this.imageName);
    img.onload = () => {
      this.imageElement.nativeElement.src = img.src;
      this.isLoading = false;
      this.cdr.detectChanges();
    };

    img.onerror = () => {
      this.imageElement.nativeElement.src = this.notFoundImageSrc;
      this.isLoading = false;
      this.cdr.detectChanges();
    };
  }


  getSafeImageUrl(imageName: string): string {
    return `assets/images/${imageName}`;
  }
}
