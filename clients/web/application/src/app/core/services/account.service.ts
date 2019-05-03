import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {AccountDTO} from '../models/api';
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  /**
   * Authentication URL.
   */
  GOOGLE_LOGIN_URL: string = environment.apiURL + '/login/google';

  /**
   * Profile URL.
   */
  PROFILE_URL: string = environment.apiURL + '/account/profile';

  /**
   * Delete URL.
   */
  DELETE_URL: string = environment.apiURL + '/account/delete';

  constructor(private http: HttpClient, public jwtHelper: JwtHelperService) {
  }

  /**
   * Returns true if the token is present and not expired.
   */
  public isTokenPresent(): boolean {
    // If the token is expired we remove it.
    if (this.jwtHelper.isTokenExpired(localStorage.getItem('token'))) {
      localStorage.removeItem('token');
    }
    return (localStorage.getItem('token') != null);
  }

  /**
   * Login via Google.
   */
  public googleLogin(googleIdToken: string, googleAccessToken: string): string {
    return 'toto';
  }

  public getProfile() {
    return this.http.get<AccountDTO>(this.PROFILE_URL);
  }

  public disconnect() {
    localStorage.clear();
  }

  public delete() {
    this.http.delete(this.DELETE_URL);
    this.disconnect();
  }

}
