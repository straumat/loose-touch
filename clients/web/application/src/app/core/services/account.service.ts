import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import {JwtHelperService} from '@auth0/angular-jwt';
import {AccountDTO} from '../models/account';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  /**
   * Authentication URL.
   */
  GOOGLE_LOGIN_URL: string = environment.apiURL + '/login/google';

  /**
   * Account URL.
   */
  ACCOUNT_URL: string = environment.apiURL + '/account';

  /**
   * Delete URL.
   */
  DELETE_URL: string = environment.apiURL + '/account/delete';

  /**
   * Connected account.
   */
  account: AccountDTO = null;

  /**
   * Constructor.
   * @param http enable rest connection
   * @param jwtHelper enable jwt management
   */
  constructor(private http: HttpClient,
              public jwtHelper: JwtHelperService) {
  }

  /**
   * Login via Google.
   */
  public googleLogin(googleIdToken: string, googleAccessToken: string): Observable<AccountDTO> {
    return this.http.get<AccountDTO>(this.GOOGLE_LOGIN_URL, {
      params: new HttpParams()
        .set('googleIdToken', googleIdToken)
        .set('googleAccessToken', googleAccessToken)
    }).pipe(
      map(account => {
        this.account = account;
        localStorage.setItem(environment.tokenNameInLocalStorage, account.idToken);
        return account;
      })
    );
  }

}
