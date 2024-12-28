import { KeycloakService } from "keycloak-angular";

export const appConfig = (keycloak: KeycloakService) => async () => {
  await keycloak.init({
    config: {
      url: 'http://localhost:8081',
      realm: 'HappyPaws',
      clientId: 'frontend',
    },
    initOptions: {
      onLoad: 'check-sso', 
      checkLoginIframe: false,
      silentCheckSsoRedirectUri: window.location.origin + '/assets/silent-check-sso.html',
      flow: 'standard',
      pkceMethod: 'S256',
    },
    enableBearerInterceptor: true, // Automatically allows adding the token to HTTP requests
    bearerExcludedUrls: ['/assets'], 
    loadUserProfileAtStartUp: true, // Load the user profile
  });
};
