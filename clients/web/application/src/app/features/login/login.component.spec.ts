import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {LoginComponent} from './login.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {JwtModule} from '@auth0/angular-jwt';
import {environment} from '../../../environments/environment';
import {DashboardComponent} from '../dashboard/dashboard.component';
import {ActivatedRoute, Data, Router} from '@angular/router';
import {LooseTouchError, LooseTouchErrorType} from '../../core/models/looseToucheError';
import {AuthServiceConfig, SocialLoginModule} from 'angularx-social-login';
import {apiConfigFactory, provideSocialLoginConfiguration} from '../../app.module';
import {ErrorComponent} from '../error/error.component';
import {Title} from '@angular/platform-browser';
import {AccountAPIService, ApiModule} from 'angular-loose-touch-api';

describe('LoginComponent', () => {

  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  // ===================================================================================================================
  beforeEach(async(async(() => {
    TestBed.configureTestingModule({
      declarations: [LoginComponent, DashboardComponent, ErrorComponent],
      imports: [
        HttpClientTestingModule,
        [RouterTestingModule.withRoutes([
          {path: 'login', component: LoginComponent},
          {path: 'error', component: ErrorComponent},
          {path: 'dashboard', component: DashboardComponent}
        ])],
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
        {provide: AuthServiceConfig, useFactory: provideSocialLoginConfiguration},
        {
          provide: ActivatedRoute,
          useValue: {
            data: {
              subscribe: (fn: (value: Data) => void) => fn({}),
            }
          }
        },
        {provide: AccountAPIService, useFactory: apiConfigFactory}
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    });
    localStorage.clear();
  })));

  // ===================================================================================================================
  it('should create', () => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });

  // ===================================================================================================================
  it('should display the login page', () => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;

    // CHeck page data.
    expect(TestBed.get(Title).getTitle()).toEqual('Loose touch connexion');
    expect(compiled.querySelector('mat-card-title').textContent).toContain('Loose touch connexion');
    expect(compiled.querySelector('mat-card-subtitle').textContent).toContain('Use your google account to sign up and sign in');
    expect(component.error).toBeUndefined();
    expect(compiled.querySelector('.alert-warning')).toBeNull();
  });

  // ===================================================================================================================
  it('should display the user image', (done) => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    const avatarImage = window.getComputedStyle(document.getElementById('avatar')).backgroundImage;
    imageExists(avatarImage, function(exists) {
      expect(exists).toBe(true);
      done();
    });
  });

  // ===================================================================================================================
  it('should display the google sign-in image', (done) => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    const googleSignIn = window.getComputedStyle(document.getElementById('google-sign-in')).backgroundImage;
    imageExists(googleSignIn, function(exists) {
      expect(exists).toBe(true);
      done();
    });
  });

  // ===================================================================================================================
  it('should call the google sign in method of the component', () => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    spyOn(component, 'signInWithGoogle');

    const button = fixture.debugElement.nativeElement.querySelector('.google-sign-in');
    button.click();

    fixture.detectChanges();
    fixture.whenStable().then(() => {
      expect(component.signInWithGoogle).toHaveBeenCalled();
    });
    expect(component.error).toBeUndefined();
  });

  // ===================================================================================================================
  it('should display an error message if login fails', fakeAsync(() => {
    const expectedError: LooseTouchError = {
      type: LooseTouchErrorType.authentication_error,
      message: 'Invalid Google Id token : FakeToken',
      errors: []
    };

    TestBed.overrideProvider(ActivatedRoute, {
      useValue: {
        data: {
          subscribe: (fn: (value: Data) => void) => fn({
            looseTouchError: expectedError
          }),
        }
      }
    });
    TestBed.compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    const router: Router = TestBed.get(Router);
    router.initialNavigation();

    // Move to login with an error message
    router.navigate(['/login']);

    // Check if we have an error.
    tick();
    fixture.detectChanges();
    expect(component.error).toEqual(expectedError);
    expect(compiled.querySelector('.alert-warning')).not.toBeNull();
  }));

  // ===================================================================================================================
  // TODO This method doesn't work yet. Don't know why. A question is asked on stackoverflow.
  // https://stackoverflow.com/questions/56079889/why-my-css-hover-background-image-is-not-changed-during-test-with-mouseover-or-m
  it('should display the google sign-in image on hover', () => {
    /*    const compiled = fixture.debugElement.nativeElement;
        fixture.detectChanges();

        let googleSignInTest = fixture.debugElement.query(By.css('.google-sign-in'));
        googleSignInTest.triggerEventHandler('mouseover', null);
        fixture.detectChanges();

        const h1 = fixture.debugElement.query(By.css('.google-sign-in'));
        const mouseenter = new MouseEvent('mouseover');
        h1.nativeElement.dispatchEvent(mouseenter);
        fixture.detectChanges();

        //expect(googleSignInTest.nativeElement.style.backgroundImage).toBe('red');
        let backgroundImage = window.getComputedStyle(document.getElementById('google-sign-in')).backgroundImage;
        console.log(backgroundImage);

        imageExists(backgroundImage, function(exists) {
          expect(exists).toBe(true);
          done();
        });*/
  });

  /**
   * Returns true is an image exists.
   * @param url image url
   * @param callback method
   * @return true if exists
   */
  function imageExists(url, callback) {
    const img = new Image();
    img.onload = function() {
      callback(true);
    };
    img.onerror = function() {
      callback(false);
    };
    img.src = url.replace('url("', '').replace('")', '');
  }

});
