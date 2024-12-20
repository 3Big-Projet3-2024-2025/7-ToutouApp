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
    enableBearerInterceptor: true, // Autorise automatiquement l'ajout du token aux requêtes HTTP
    bearerExcludedUrls: ['/assets'], 
    loadUserProfileAtStartUp: true, // Charge le profil utilisateur
  });
};
