import { KeycloakService } from "keycloak-angular";

export const appConfig = (keycloak: KeycloakService) => async () => {
  await keycloak.init({
    config: {
      url: 'http://localhost:8081',
      realm: 'HappyPaws',
      clientId: 'frontend',
    },
    initOptions: {
      onLoad: 'login-required', // Redirection automatique vers la connexion si non connecté
      checkLoginIframe: false,
      silentCheckSsoRedirectUri: window.location.origin + '/assets/silent-check-sso.html',
      flow: 'standard', // Utilisation du flux standard (Authorization Code Flow)
      pkceMethod: 'S256', // Utilisation de PKCE (Proof Key for Code Exchange)
    },
    enableBearerInterceptor: true, // Autorise automatiquement l'ajout du token aux requêtes HTTP
    bearerExcludedUrls: ['/assets'], // Exclut certaines URL du besoin de token
    loadUserProfileAtStartUp: true, // Charge le profil utilisateur
  });
};
