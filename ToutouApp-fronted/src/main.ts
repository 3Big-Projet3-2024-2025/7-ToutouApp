import { enableProdMode } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { APP_INITIALIZER } from '@angular/core';

import { KeycloakService } from 'keycloak-angular';
import { appConfig } from './app/app.config';
import { routes } from './app/app.routes';
import { AppComponent } from './app/app.component';
import { environment } from './environement';
import { authInterceptor } from './auth.interceptor';
import { provideHttpClient, withInterceptors } from '@angular/common/http';


if (environment.production) {
  enableProdMode();
}

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(withInterceptors([authInterceptor])),
    KeycloakService, // Provides the Keycloak service
    provideRouter(routes),
    {
      provide: APP_INITIALIZER,
      useFactory: appConfig,
      deps: [KeycloakService],
      multi: true,
    },
  ],
}).catch((err) => console.error(err));
