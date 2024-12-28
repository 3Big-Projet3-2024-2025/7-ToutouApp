import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { from, Observable } from 'rxjs';
import { switchMap, of } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloakService = inject(KeycloakService);

  // Check if the user is logged in
  const isAuthenticated = keycloakService.isLoggedIn();

  if (isAuthenticated) {
    // If the user is logged in, get the token
    const tokenPromise = keycloakService.getToken();

    return from(tokenPromise).pipe(
      switchMap((token: string) => {
        // Clone the request to add the token in the headers
        const clonedRequest = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
          },
        });
        return next(clonedRequest); // Pass the cloned query
      })
    );
  }

  // If the user is not authenticated, pass the original request
  return next(req);
};
