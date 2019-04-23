import {Injectable} from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(public jwtHelper: JwtHelperService) {
  }

  /**
   * Login via Google.
   */
  public googleLogin(): string {
    return 'toto';
  }

  /**
   * Returns true if the token is present and not expired.
   */
  public isAuthenticated(): boolean {
    // If the token is expired we remove it.
    if (this.jwtHelper.isTokenExpired(localStorage.getItem('token'))) {
      localStorage.clear();
    }

    return (localStorage.getItem('token') != null);
  }

}
