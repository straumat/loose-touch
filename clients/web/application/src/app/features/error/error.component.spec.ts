import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {ErrorComponent} from './error.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {CoreComponent} from '../../core/core.component';
import {LoginComponent} from '../login/login.component';
import {DashboardComponent} from '../dashboard/dashboard.component';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {routes} from '../../core/core-routing.module';
import {JwtModule} from '@auth0/angular-jwt';
import {environment} from '../../../environments/environment';
import {SocialLoginModule} from 'angularx-social-login';
import {LooseTouchError, LooseTouchErrorType} from '../../core/models/looseToucheError';
import {ActivatedRoute, Router} from '@angular/router';
import {HTTP_INTERCEPTORS, HttpClient} from '@angular/common/http';
import {ApplicationInterceptor} from '../../core/interceptors/application-interceptor.service';
import {AccountService} from '../../core/services/account.service';
import {INTERNAL_SERVER_ERROR} from 'http-status-codes';

describe('ErrorComponent', () => {

  let component: ErrorComponent;
  let fixture: ComponentFixture<ErrorComponent>;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CoreComponent, LoginComponent, DashboardComponent, ErrorComponent],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule.withRoutes(routes),
        JwtModule.forRoot({
          config: {
            tokenGetter: () => {
              return localStorage.getItem(environment.tokenNameInLocalStorage);
            }
          }
        }),
        SocialLoginModule
      ],
      providers: [
        {provide: HTTP_INTERCEPTORS, useClass: ApplicationInterceptor, multi: true},
        AccountService
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    }).compileComponents();
    // Inject the http service and test controller for each test
    httpClient = TestBed.get(HttpClient);
    httpTestingController = TestBed.get(HttpTestingController);
    localStorage.clear();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // ===================================================================================================================
  it('should display the page (without error parameter)', fakeAsync(() => {
    // TODO Test page title.
    const compiled = fixture.debugElement.nativeElement;
    tick();
    fixture.detectChanges();
    // Check component data.
    expect(component.error).toBeUndefined();

    // Check page components.
    expect(compiled.querySelector('mat-card-title').textContent).toContain('An error occurred !');
    expect(compiled.querySelector('mat-card-subtitle')).toBeNull();
    expect(compiled.querySelector('button')).not.toBeNull();
    expect(compiled.querySelector('button').textContent).toContain('Go back to dashboard');
  }));

  // ===================================================================================================================
  it('should display the page (with error parameter)', fakeAsync(() => {
    const compiled = fixture.debugElement.nativeElement;
    const router: Router = TestBed.get(Router);
    router.initialNavigation();

    // When routing with an error.
    const expectedError: LooseTouchError = {
      type: LooseTouchErrorType.api_error,
      message: 'Internal server error !',
      errors: []
    };
    router.navigate(['/error'], {queryParams: {looseTouchError: JSON.stringify(expectedError)}});
    tick();
    fixture.detectChanges();

    // Check component data.
    expect(component.error).not.toBeUndefined();
    expect(component.error).toEqual(expectedError);

    // Check page components.
    expect(compiled.querySelector('mat-card-title').textContent).toContain('An error occurred !');
    expect(compiled.querySelector('mat-card-subtitle').textContent).toContain(expectedError.message);
    expect(compiled.querySelector('button')).not.toBeNull();
    expect(compiled.querySelector('button').textContent).toContain('Go back to dashboard');

    // Check the link is working.
    spyOn(component, 'goToDashboard');
    fixture.debugElement.nativeElement.querySelector('button').click();
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      expect(component.goToDashboard).toHaveBeenCalled();
    });
  }));

  // ===================================================================================================================
  it('link to dashboard button should work', fakeAsync(() => {
    const router: Router = TestBed.get(Router);
    router.initialNavigation();

    fixture.debugElement.nativeElement.querySelector('button').click();
    tick();
    expect(router.url).toContain('/dashboard');
  }));

  // ===================================================================================================================
  it('should display if there is 500 error', fakeAsync(() => {
    const router: Router = TestBed.get(Router);
    const service: AccountService = TestBed.get(AccountService);
    router.initialNavigation();

    // Go to login page.
    router.navigate(['/dashboard']);
    tick();
    expect(router.url).toContain('/dashboard');

    // Raise an exception.
    // Calling the service and testing that an error occurred.
    service.googleLogin('test1', 'test1').subscribe(() => {
      fail('No error was found');
    });

    // Raise an exception 500.
    const req = httpTestingController.expectOne({
      method: 'GET',
      url: 'http://localhost:8080/v1/login/google?googleIdToken=' + 'test1' + '&googleAccessToken=' + 'test1'
    });
    req.flush({
      type: 'authentication_error',
      message: 'Internal server error',
      errors: []
    }, {status: INTERNAL_SERVER_ERROR, statusText: 'Internal server error'});

    // Check if we are on error page
    tick();
    const compiled = fixture.debugElement.nativeElement;
    fixture.detectChanges();
    expect(router.url).toContain('/error');
    expect(compiled.querySelector('mat-card-subtitle').textContent).toContain('Internal server error');
  }));

  // ===================================================================================================================
  it('should not display if there is 401 error', fakeAsync(() => {
    const router: Router = TestBed.get(Router);
    const service: AccountService = TestBed.get(AccountService);
    router.initialNavigation();

    // Go to login page.
    router.navigate(['/dashboard']);
    tick();
    expect(router.url).toContain('/dashboard');

    // Raise an exception.
    // Calling the service and testing that an error occurred.
    service.googleLogin('test2', 'test2').subscribe(data => {
      fail('No error was found');
    });

    // Raise an exception 400.
    const req = httpTestingController.expectOne({
      method: 'GET',
      url: 'http://localhost:8080/v1/login/google?googleIdToken=' + 'test2' + '&googleAccessToken=' + 'test2'
    });
    req.flush({
      type: 'authentication_error',
      message: 'Invalid Google Id token : invalid',
      errors: []
    }, {status: 401, statusText: 'Bad Request'});

    // Check if we are on error page
    tick();
    expect(router.url).not.toContain('/error');
    expect(router.url).toContain('/login');
  }));

  // ===================================================================================================================
  it('should display if there is 404 error', fakeAsync(() => {
    const router: Router = TestBed.get(Router);
    const route: ActivatedRoute = TestBed.get(ActivatedRoute);
    router.initialNavigation();

    // Go to an non existing page - check we go back to error.
    router.navigate(['/toto']);
    tick();
    expect(router.url).toContain('/toto');

    const compiled = fixture.debugElement.nativeElement;
    fixture.detectChanges();
    expect(compiled.querySelector('mat-card-title').textContent).toContain('An error occurred !');
    // expect(compiled.querySelector('mat-card-subtitle')).not.toBeNull();
    // expect(compiled.querySelector('mat-card-subtitle').textContent).toContain('Pas not found');
  }));

});
