import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { from, Observable } from 'rxjs';
import { switchMap, of } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloakService = inject(KeycloakService);

  // Vérifiez si l'utilisateur est connecté
  const isAuthenticated = keycloakService.isLoggedIn(); // Retourne un boolean directement

  if (isAuthenticated) {
    // Si l'utilisateur est connecté, obtenez le token
    const tokenPromise = keycloakService.getToken();

    return from(tokenPromise).pipe(
      switchMap((token: string) => {
        // Clonez la requête pour ajouter le token dans les en-têtes
        const clonedRequest = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
          },
        });
        return next(clonedRequest); // Passez la requête clonée
      })
    );
  }

  // Si l'utilisateur n'est pas authentifié, laissez passer la requête d'origine
  return next(req);
};
