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
    console.error('Is it null ? ' + (token == null) + ' /  Is it expired ? ' + this.jwtHelper.isTokenExpired(token));
    return !(token == null || this.jwtHelper.isTokenExpired(token));
  }

}
