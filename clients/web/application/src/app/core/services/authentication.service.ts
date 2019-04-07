import {Injectable} from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(public jwtHelper: JwtHelperService) {
  }

  /**
   * Returns true if the token is present and not expired.
   */
  public isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    return !(token == null || this.jwtHelper.isTokenExpired(token));
  }

}
