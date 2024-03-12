import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { jwtDecode } from "jwt-decode";
import { AuthService } from 'src/app/services/auth/auth.service';
import { Observable, of } from 'rxjs';
import { switchMap, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(
    private cookieService: CookieService,
    private router: Router,
    private authService: AuthService
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> {
    const jwtToken = this.cookieService.get('Token');

    if (jwtToken) {
      try {
        const decodedToken: any = jwtDecode(jwtToken);

        // Obtiene la fecha actual en segundos (timestamp)
        const currentTimestamp = Math.floor(Date.now() / 1000);
        if (decodedToken.exp && decodedToken.exp > currentTimestamp) {
          // Token válido y no ha caducado

          const jwtId = decodedToken.id;

          return this.authService.getAllAdminUserIDs().pipe(
            switchMap((adminIDs: number[]) => {
              if (adminIDs.includes(parseInt(jwtId))) {
                // El usuario es un administrador
                if (state.url.startsWith('/admin')) {
                  // Si el usuario es admin y está accediendo a una ruta que comienza con /admin, permite el acceso
                  return of(true);
                } else {
                  // El usuario es admin, pero no está accediendo a una ruta de /admin, redirige a alguna página de acceso no autorizado
                  //return of(this.router.createUrlTree(['/unauthorized']));
                  return of(true);
                }
              } else {
                // El usuario no es un administrador
                if (state.url.startsWith('/admin')) {
                  // Si el usuario no es admin pero está accediendo a una ruta que comienza con /admin, redirige a alguna página de acceso no autorizado
                  return of(this.router.createUrlTree(['/unauthorized']));
                } else {
                  // El usuario no es un administrador y está accediendo a una ruta protegida por AuthGuard, permite el acceso
                  return of(true);
                }
              }
            }),
            catchError((error: any) => {
              console.error('Error al obtener IDs de administradores:', error);
              // Manejo de errores, por ejemplo, redirigir a una página de error
              return of(this.router.createUrlTree(['/error']));
            })
          );
        } else {
          // Token caducado, redirige al usuario a la página de inicio de sesión
          return of(this.router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } }));
        }
      } catch (error) {
        console.error('Error al decodificar el JWT:', error);
      }
    }

    // Si no hay un token válido, guarda la URL actual y redirige al usuario a la página de inicio de sesión
    console.error('No hay un token válido, redirige al usuario a la página de inicio de sesión');
    return of(this.router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } }));
  }
}
