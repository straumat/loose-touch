import {inject, TestBed} from '@angular/core/testing';

import {AuthenticationService} from './authentication.service';
import {JwtHelperService, JwtModule} from '@auth0/angular-jwt';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';

describe('AuthenticationService', () => {
  beforeEach(() => TestBed.configureTestingModule(({
    declarations: [],
    imports: [
      JwtModule.forRoot({
        config: {
          tokenGetter: () => {
            return localStorage.getItem('token');
          }
        }
      })
    ],
    providers: [JwtHelperService],
  })));

  it('should be created', () => {
    const service: AuthenticationService = TestBed.get(AuthenticationService);
    expect(service).toBeTruthy();
  });

  it('should return false if not token or token expired', () => {
    const service: AuthenticationService = TestBed.get(AuthenticationService);
    localStorage.clear();

    // Tokens used for tests.
    const validToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjMjUyOWI1OC1hYzVhLTRiYTMtODA2Yy02NjlmYzkwZmQ4YTMiLCJpYXQiOjE1NTQzODgzNzksImV4cCI6Mjc0NzQzODgzODF9.z_pCx8hxTEc5J1PIOAzW-NEUKZE6LD6AE-pG_v17QVo';
    const expiredToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2OGQwY2FiOC00YzVhLTRlNDctOGZlZS04ZWNmZjEyMjIzYTciLCJpYXQiOjE1NTQzOTI2NDMsImV4cCI6MTU1NDM5MjY0M30.gyj3mErABtm8jyaoROPzCiaFW8tQ2HCkhSP0vb8BVkw';

    // Should return false if the token is not present.
    expect(service.isAuthenticated()).toBe(false);

    // Should return false if the token is present and expired.
    localStorage.setItem('token', expiredToken);
    expect(service.isAuthenticated()).toBe(false);

    // Should return true if the token is present and valid.
    localStorage.setItem('token', validToken);
    expect(service.isAuthenticated()).toBe(true);
  });

});
