import {TestBed} from '@angular/core/testing';

import {AuthenticationGuardService} from './authentication-guard.service';
import {JwtHelperService, JwtModule} from '@auth0/angular-jwt';
import {RouterTestingModule} from '@angular/router/testing';
import {Router, Routes} from '@angular/router';
import {LoginComponent} from '../../features/login/login.component';
import {CoreComponent} from '../core.component';
import {DashboardComponent} from '../../features/dashboard/dashboard.component';
import {PageNotFoundComponent} from '../../features/page-not-found/page-not-found.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {ProfileComponent} from '../../features/profile/profile.component';
import {AccountService} from '../services/account.service';
import {HttpClient} from '@angular/common/http';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';

const coreRoutes: Routes = [
  {
    // Login.
    path: 'login',
    component: LoginComponent
  },
  {
    // Dashboard.
    path: '',
    redirectTo: '/dashboard',
    pathMatch: 'full',
    canActivate: [AuthenticationGuardService]
  },
  {
    path: '',
    component: CoreComponent,
    children: [
      {
        path: '',
        component: DashboardComponent,
        canActivate: [AuthenticationGuardService]
      },
      {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [AuthenticationGuardService]
      },
      {
        path: 'profile',
        component: ProfileComponent,
        canActivate: [AuthenticationGuardService]
      }
    ]
  },
  {
    path: '**',
    component: PageNotFoundComponent
  }
];

describe('AuthenticationGuardService', () => {

  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CoreComponent, DashboardComponent, LoginComponent, ProfileComponent, PageNotFoundComponent],
      imports: [
        HttpClientTestingModule,
        JwtModule.forRoot({
          config: {
            tokenGetter: () => {
              return localStorage.getItem('token');
            }
          }
        }),
        RouterTestingModule.withRoutes(coreRoutes)
      ],
      providers: [JwtHelperService, AccountService],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    });
    // Inject the http service and test controller for each test
    httpClient = TestBed.get(HttpClient);
    httpTestingController = TestBed.get(HttpTestingController);
  });

  it('should be created', () => {
    const service: AuthenticationGuardService = TestBed.get(AuthenticationGuardService);
    expect(service).toBeTruthy();
  });

  it('should not allow access to pages without token or with expired token', () => {
    const router: Router = TestBed.get(Router);
    localStorage.clear();

    // Tokens used for tests.
    const validToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjMjUyOWI1OC1hYzVhLTRiYTMtODA2Yy02NjlmYzkwZmQ4YTMiLCJpYXQiOjE1NTQzODgzNzksImV4cCI6Mjc0NzQzODgzODF9.z_pCx8hxTEc5J1PIOAzW-NEUKZE6LD6AE-pG_v17QVo';
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
      .then(() => router.navigate(['profile']))
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
      .then(() => router.navigate(['profile']))
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
      })
      .then(() => router.navigate(['profile']))
      .then(() => {
        expect(router.url).toBe('/profile');
      });
  });

})
;
