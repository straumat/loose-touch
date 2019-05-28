import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {LoginComponent} from './login.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {JwtModule} from '@auth0/angular-jwt';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {routes} from '../../core/core-routing.module';
import {CoreComponent} from '../../core/core.component';
import {DashboardComponent} from '../dashboard/dashboard.component';
import {Router} from '@angular/router';
import {LooseTouchError, LooseTouchErrorType} from '../../core/models/looseToucheError';
import {AuthServiceConfig, SocialLoginModule} from 'angularx-social-login';
import {provideSocialLoginConfiguration} from '../../app.module';
import {ErrorComponent} from '../error/error.component';

describe('LoginComponent', () => {

  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
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
        {provide: AuthServiceConfig, useFactory: provideSocialLoginConfiguration}
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    });
    // Inject the http service and test controller for each test
    httpClient = TestBed.get(HttpClient);
    httpTestingController = TestBed.get(HttpTestingController);
    localStorage.clear();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display the card information text', () => {
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('mat-card-title').textContent).toContain('Loose touch connexion');
    expect(compiled.querySelector('mat-card-subtitle').textContent).toContain('Use your google account to sign up and sign in');

    expect(component.error).toBeNull();
    expect(compiled.querySelector('.alert-warning')).toBeNull();
  });

  it('should display the user image', (done) => {
    const avatarImage = window.getComputedStyle(document.getElementById('avatar')).backgroundImage;
    imageExists(avatarImage, function(exists) {
      expect(exists).toBe(true);
      done();
    });
  });

  it('should display the google sign-in image', (done) => {
    const googleSignIn = window.getComputedStyle(document.getElementById('google-sign-in')).backgroundImage;
    imageExists(googleSignIn, function(exists) {
      expect(exists).toBe(true);
      done();
    });
  });

  it('should call the google sign in method of the component', () => {
    spyOn(component, 'signInWithGoogle');

    const button = fixture.debugElement.nativeElement.querySelector('.google-sign-in');
    button.click();

    fixture.detectChanges();
    fixture.whenStable().then(() => {
      expect(component.signInWithGoogle).toHaveBeenCalled();
    });
    expect(component.error).toBeNull();
  });

  it('should display an error message if login fails', fakeAsync(() => {
    const compiled = fixture.debugElement.nativeElement;
    const router: Router = TestBed.get(Router);
    router.initialNavigation();

    // Simulate an error.
    const expectedError: LooseTouchError = {
      type: LooseTouchErrorType.authentication_error,
      message: 'Invalid Google Id token : FakeToken',
      errors: []
    };
    router.navigate(['/login'], {queryParams: {looseTouchError: JSON.stringify(expectedError)}});

    // Check if we have an error.
    tick();
    fixture.detectChanges();
    expect(component.error).toEqual(expectedError);
    expect(compiled.querySelector('.alert-warning')).not.toBeNull();
  }));

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
