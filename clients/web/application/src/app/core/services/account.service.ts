import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {JwtHelperService} from '@auth0/angular-jwt';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {AccountAPIService, AccountDTO} from 'angular-loose-touch-api';
import {AuthService} from 'angularx-social-login';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  /**
   * Connected account.
   */
  account: AccountDTO = undefined;

  constructor(private http: HttpClient,
              private socialLoginService: AuthService,
              private accountAPIService: AccountAPIService,
              public jwtHelper: JwtHelperService) {
    if (this.account === undefined) {
      accountAPIService.getProfile('body').subscribe(value => {
        this.account = value;
        localStorage.setItem(environment.tokenNameInLocalStorage, this.account.idToken);
      });
    }
  }

  /**
   * Login via Google.
   */
  public googleLogin(googleIdToken: string, googleAccessToken: string): Observable<AccountDTO> {
    return this.accountAPIService.googleLogin(googleIdToken, googleAccessToken).pipe(map((value => {
      this.account = value;
      localStorage.setItem(environment.tokenNameInLocalStorage, this.account.idToken);
      return value;
    })));
  }

  /**
   * Returns true if the token is present and not expired and  profile is set.
   */
  public isTokenValid(): boolean {
    // If the token is expired we remove it.
    if (this.jwtHelper.isTokenExpired(localStorage.getItem(environment.tokenNameInLocalStorage))) {
      localStorage.removeItem(environment.tokenNameInLocalStorage);
    }
    return (localStorage.getItem(environment.tokenNameInLocalStorage) != null);
  }

  signOut() {
    this.socialLoginService.signOut(false);
    this.account = undefined;
    localStorage.removeItem(environment.tokenNameInLocalStorage);
  }
}
