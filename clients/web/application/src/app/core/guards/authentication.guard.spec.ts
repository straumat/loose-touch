import {inject, TestBed} from '@angular/core/testing';

import {AuthenticationGuard} from './authentication.guard';
import {LoginComponent} from '../../features/login/login.component';
import {DashboardComponent} from '../../features/dashboard/dashboard.component';
import {CoreComponent} from '../core.component';
import {ErrorComponent} from '../../features/error/error.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {JwtHelperService, JwtModule} from '@auth0/angular-jwt';
import {RouterTestingModule} from '@angular/router/testing';
import {routes} from '../core-routing.module';
import {AccountService} from '../services/account.service';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {Router} from '@angular/router';
import {ApiModule} from 'angular-loose-touch-api';
import {apiConfigFactory, provideSocialLoginConfiguration} from '../../app.module';
import {AuthServiceConfig, SocialLoginModule} from 'angularx-social-login';

describe('AuthenticationGuard', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CoreComponent, DashboardComponent, LoginComponent, ErrorComponent],
      imports: [
        HttpClientTestingModule,
        ApiModule.forRoot(apiConfigFactory),
        JwtModule.forRoot({
          config: {
            tokenGetter: () => {
              return localStorage.getItem('token');
            }
          }
        }),
        SocialLoginModule,
        RouterTestingModule.withRoutes(routes)
      ],
      providers: [{provide: AuthServiceConfig, useFactory: provideSocialLoginConfiguration}, JwtHelperService, AccountService],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    });
    localStorage.clear();
  });

  it('should create', inject([AuthenticationGuard], (guard: AuthenticationGuard) => {
    expect(guard).toBeTruthy();
  }));

  it('should not allow access to pages without token or with expired token', () => {
    const router: Router = TestBed.get(Router);

    // Tokens used for tests ( valid token : 17/8/2840 à 14:33:01	/ expired token : 7/6/2019 à 15:16:09).
    // tslint:disable-next-line:max-line-length
    const validToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjMjUyOWI1OC1hYzVhLTRiYTMtODA2Yy02NjlmYzkwZmQ4YTMiLCJpYXQiOjE1NTQzODgzNzksImV4cCI6Mjc0NzQzODgzODF9.z_pCx8hxTEc5J1PIOAzW-NEUKZE6LD6AE-pG_v17QVo';
    // tslint:disable-next-line:max-line-length
    const expiredToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2OGQwY2FiOC00YzVhLTRlNDctOGZlZS04ZWNmZjEyMjIzYTciLCJpYXQiOjE1NTQzOTI2NDMsImV4cCI6MTU1NDM5MjY0M30.gyj3mErABtm8jyaoROPzCiaFW8tQ2HCkhSP0vb8BVkw';

    router
    // Access to pages without token.
      .navigate(['login'])
      .then(() => {
        expect(router.url).toBe('/login');
      })
      .then(() => router.navigate(['/']))
      .then(() => {
        expect(router.url).toBe('/login');
      })
      .then(() => router.navigate(['dashboard']))
      .then(() => {
        expect(router.url).toBe('/login');
      })
      // Access to pages with expired token.
      .then(() => localStorage.setItem('token', expiredToken))
      .then(() => router.navigate(['login']))
      .then(() => {
        expect(router.url).toBe('/login');
      })
      .then(() => router.navigate(['/']))
      .then(() => {
        expect(router.url).toBe('/login');
      })
      .then(() => router.navigate(['dashboard']))
      .then(() => {
        expect(router.url).toBe('/login');
      })
      // Access to pages with valid token.
      .then(() => localStorage.setItem('token', validToken))
      .then(() => router.navigate(['login']))
      .then(() => {
        expect(router.url).toBe('/login');
      })
      .then(() => router.navigate(['/']))
      .then(() => {
        expect(router.url).toBe('/dashboard');
      })
      .then(() => router.navigate(['dashboard']))
      .then(() => {
        expect(router.url).toBe('/dashboard');
      });
  });

});
