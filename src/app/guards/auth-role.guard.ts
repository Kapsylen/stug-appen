import { Injectable, inject, PLATFORM_ID } from '@angular/core';
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot} from '@angular/router';
import { KeycloakAuthGuard, KeycloakService } from 'keycloak-angular';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard extends KeycloakAuthGuard {
  private platformId = inject(PLATFORM_ID);

  constructor(
    protected readonly keycloak: KeycloakService,
    protected override readonly router: Router
  ) {
    super(router, keycloak);
  }

  public async isAccessAllowed(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ) {
    if (!isPlatformBrowser(this.platformId)) {
      return true; // Allow access during SSR
    }

    // Only execute authentication logic in browser environment
    if (!this.authenticated) {
      // Create redirect URL using state URL but avoid using window object directly
      const baseUrl = isPlatformBrowser(this.platformId) ? window.location.origin : '';
      await this.keycloak.login({
        redirectUri: `${baseUrl}${state.url}`,
      });
    }

    const requiredRoles = route.data['roles'];

    if (!(requiredRoles instanceof Array) || requiredRoles.length === 0) {
      return true;
    }

    return requiredRoles.every((role) => this.roles.includes(role));
  }
}
