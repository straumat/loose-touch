import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {LoginComponent} from './login.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterTestingModule} from '@angular/router/testing';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [RouterTestingModule],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    }).compileComponents();
  }));

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
  });


  // TODO This method doesn't work yet. Don't know why. A question is asked on stackoverflow.
  // https://stackoverflow.com/questions/56079889/why-my-css-hover-background-image-is-not-changed-during-test-with-mouseover-or-m
  it('should display the google sign-in image on hover', () => {
    /*    const compiled = fixture.debugElement.nativeElement;
        fixture.detectChanges();

        let googleSignIntest = fixture.debugElement.query(By.css('.google-sign-in'));
        googleSignIntest.triggerEventHandler('mouseover', null);
        fixture.detectChanges();

        const h1 = fixture.debugElement.query(By.css('.google-sign-in'));
        const mouseenter = new MouseEvent('mouseover');
        h1.nativeElement.dispatchEvent(mouseenter);
        fixture.detectChanges();

        //expect(googleSignIntest.nativeElement.style.backgroundImage).toBe('red');
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
