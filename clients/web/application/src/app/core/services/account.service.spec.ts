/* tslint:disable */
import {fakeAsync, TestBed, tick} from '@angular/core/testing';

import {AccountService} from './account.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HTTP_INTERCEPTORS, HttpClient} from '@angular/common/http';
import {JwtModule} from '@auth0/angular-jwt';
import {environment} from '../../../environments/environment';
import {RouterTestingModule} from '@angular/router/testing';
import {routes} from '../core-routing.module';
import {LoginComponent} from '../../features/login/login.component';
import {CoreComponent} from '../core.component';
import {DashboardComponent} from '../../features/dashboard/dashboard.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {Router} from '@angular/router';
import {ErrorComponent} from '../../features/error/error.component';
import {ErrorInterceptor} from '../interceptors/error-interceptor.service';
import {AccountDTO} from 'angular-loose-touch-api';

describe('AccountService', () => {

  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  // Test data.
  const googleUser1IdToken = 'eyJhbGciOiJSUzI1NiIsImtpZCI6IjI4ZjU4MTNlMzI3YWQxNGNhYWYxYmYyYTEyMzY4NTg3ZTg4MmI2MDQiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MDgzMTQyMTkxNDktNjBzOGwybHRyYmFsODJobnVqMzV1ODFvcHQyN2doc2EuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MDgzMTQyMTkxNDktNjBzOGwybHRyYmFsODJobnVqMzV1ODFvcHQyN2doc2EuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDk5NzE4MTY5MTIxNjI4ODcwNDkiLCJlbWFpbCI6Imxvb3NlLnRvdWNoLnRlc3QuMUBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6IkZpMWgtZlgzSC1HT2l4dTh1R3ctTVEiLCJuYW1lIjoibG9vc2UgMSB0b3VjaCAxIiwicGljdHVyZSI6Imh0dHBzOi8vbGg1Lmdvb2dsZXVzZXJjb250ZW50LmNvbS8tdlRJTWh5TDllUE0vQUFBQUFBQUFBQUkvQUFBQUFBQUFBQUEvQUNIaTNyZlVmeVg3ZnRVaGFCX291aTRqbnV2blFNQmh3dy9zOTYtYy9waG90by5qcGciLCJnaXZlbl9uYW1lIjoibG9vc2UgMSIsImZhbWlseV9uYW1lIjoidG91Y2ggMSIsImxvY2FsZSI6ImZyIiwiaWF0IjoxNTU3NjA3NDI0LCJleHAiOjE1NTc2MTEwMjR9.eZkeAabrhA1Y_YAbX3F8uVP_TSolLhu_vvGqISqsm2kUPWnqboHUuW4MVrw_-LNjy0A75lBbrkXwZiH3NQAvwPigkRQ-d7j881dTjGvuuPX3d448hQlRUYu01SL2Fms0oCILc1RkeKXQfmEByEFt-xTannQNhCZjAC7uv7StfmA5KGkPRllQqWAlqkhjeTJeiF-d4-_gyUkUpbNwqgKa1Ctx8p9cWB3XhnfON3rC99QQ5TdpgYKm1BIBvlerZKgw7Rw2WKqmGoh1MbI6geOgOHzQmNM-Xjf7Bwp251WK48XNoOyuskRMKlQxqViG8edV2tKjF9zN67y5qn5RDKyyPw';
  const googleUser1AccessToken = 'ya29.Gl0GB8nUQyjnRM4QmgFFinHGn2l8RH0_WSzw0fNaEwgZvdKQMf9kTI1e9_po4n4XXr0MwyRxsNtcBSoDgXteZ8l4ivewkdZ_1OuTKaS-HVgCzJ-9yt8_SCcT79xWUk0';

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CoreComponent, LoginComponent, DashboardComponent, ErrorComponent],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule.withRoutes(routes), // TODO Use local routes.
        JwtModule.forRoot({
          config: {
            tokenGetter: () => {
              return localStorage.getItem(environment.tokenNameInLocalStorage);
            }
          }
        })
      ],
      providers: [{provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true}],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    });
    // Inject the http service and test controller for each test
    httpClient = TestBed.get(HttpClient);
    httpTestingController = TestBed.get(HttpTestingController);
    localStorage.clear();
  });

  it('should be created', () => {
    const service: AccountService = TestBed.get(AccountService);
    expect(service).toBeTruthy();
  });

  it('should connect with correct google credentials', () => {
    const service: AccountService = TestBed.get(AccountService);

    // Test account data.
    const expectedData: AccountDTO = {
      idToken: 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyYjI5ZTY3ZS0zM2U0LTRiMGItYmY5YS1jOTIxNDJlZmQ5MWEiLCJpYXQiOjE1NTQ5ODMzMTYsImV4cCI6MTU1NzU3NTMxNn0._Jj8TLKQJAjGnzq4om2ujbLTgNCf_ZhJdhlNYiuk3xE',
      firstName: 'loose 1',
      lastName: 'touch 1',
      email: 'loose.touch.test.1@gmail.com',
      pictureUrl: 'https://lh5.googleusercontent.com/-vTIMhyL9ePM/AAAAAAAAAAI/AAAAAAAAAAA/ACHi3rfUfyX7ftUhaB_oui4jnuvnQMBhww/s96-c/photo.jpg',
      newAccount: true
    };

    // Verify default data.
    expect(service.account).toBeUndefined();
    expect(localStorage.getItem(environment.tokenNameInLocalStorage)).toBeNull();

    // Calling the service and testing results.
    service.googleLogin(googleUser1IdToken, googleUser1AccessToken).subscribe(data => {
      // Test returned value.
      expect(data).toEqual(expectedData);
      // Test stored data in service.
      expect(data).toEqual(service.account);
      // Test stored token.
      expect(localStorage.getItem(environment.tokenNameInLocalStorage)).toEqual(data.idToken);
    });

    // Then we set the the url expected and the fake data returned.
    const req = httpTestingController.expectOne({
      method: 'GET',
      url: 'http://localhost:8080/v1/login/google?googleIdToken=' + googleUser1IdToken + '&googleAccessToken=' + googleUser1AccessToken
    });
    req.flush({
      idToken: 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyYjI5ZTY3ZS0zM2U0LTRiMGItYmY5YS1jOTIxNDJlZmQ5MWEiLCJpYXQiOjE1NTQ5ODMzMTYsImV4cCI6MTU1NzU3NTMxNn0._Jj8TLKQJAjGnzq4om2ujbLTgNCf_ZhJdhlNYiuk3xE',
      firstName: 'loose 1',
      lastName: 'touch 1',
      email: 'loose.touch.test.1@gmail.com',
      pictureUrl: 'https://lh5.googleusercontent.com/-vTIMhyL9ePM/AAAAAAAAAAI/AAAAAAAAAAA/ACHi3rfUfyX7ftUhaB_oui4jnuvnQMBhww/s96-c/photo.jpg',
      newAccount: true
    });
  });

  it('should not connect with incorrect google credentials', fakeAsync(() => {
    const service: AccountService = TestBed.get(AccountService);
    const router: Router = TestBed.get(Router);

    // Verify data in the service.
    expect(service.account).toBeUndefined();
    expect(localStorage.getItem(environment.tokenNameInLocalStorage)).toBeNull();

    // Calling the service and testing that an error occurred.
    service.googleLogin(googleUser1IdToken, googleUser1AccessToken).subscribe(data => {
      fail('No error was found');
    });

    // Then we set the the url expected and the fake data returned.
    const req = httpTestingController.expectOne({
      method: 'GET',
      url: 'http://localhost:8080/v1/login/google?googleIdToken=' + googleUser1IdToken + '&googleAccessToken=' + googleUser1AccessToken
    });
    req.flush({
      'type': 'authentication_error',
      'message': 'Invalid Google Id token : invalid',
      'errors': []
    }, {status: 401, statusText: 'Bad Request'});

    // Test for the url and the data in the service.
    tick();
    expect(router.url).toContain('/login');
    expect(service.account).toBeUndefined();
    expect(localStorage.getItem(environment.tokenNameInLocalStorage)).toBeNull();
  }));

});
