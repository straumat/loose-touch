import {async, ComponentFixture, fakeAsync, TestBed} from '@angular/core/testing';

import {ErrorComponent} from './error.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterTestingModule} from '@angular/router/testing';
import {DashboardComponent} from '../dashboard/dashboard.component';
import {Title} from '@angular/platform-browser';
import {ActivatedRoute, Data} from '@angular/router';
import {LoginComponent} from '../login/login.component';
import {LooseTouchError} from 'angular-loose-touch-api';

/**
 * Error component.
 */
describe('ErrorComponent', () => {

  let component: ErrorComponent;
  let fixture: ComponentFixture<ErrorComponent>;

  // ===================================================================================================================
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DashboardComponent, ErrorComponent],
      providers: [
        {provide: Title, useClass: Title},
        {
          provide: ActivatedRoute,
          useValue: {
            data: {
              subscribe: (fn: (value: Data) => void) => fn({
                looseTouchError: {
                  type: LooseTouchError.TypeEnum.ApiConnectionError,
                  message: 'Unit test error message',
                  errors: []
                }
              }),
            }
          }
        }
      ],
      imports: [RouterTestingModule.withRoutes([
        {path: 'error', component: ErrorComponent},
        {path: 'dashboard', component: DashboardComponent}
      ])],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    });
  }));

  // ===================================================================================================================
  it('should create', () => {
    fixture = TestBed.createComponent(ErrorComponent);
    component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });

  // ===================================================================================================================
  it('should display the page (without error)', () => {
    // Override the activated route to remove data passed to the error page.
    TestBed.overrideProvider(ActivatedRoute, {
      useValue: {
        data: {
          subscribe: (fn: (value: Data) => void) => fn({}),
        }
      }
    });
    TestBed.compileComponents();
    fixture = TestBed.createComponent(ErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;

    // Check component data.
    expect(component.error).toBeUndefined();

    // Check page data.
    expect(TestBed.get(Title).getTitle()).toEqual('An error occurred');
    expect(compiled.querySelector('mat-card-title').textContent).toContain('An error occurred !');
    expect(compiled.querySelector('mat-card-subtitle')).toBeNull();
    expect(compiled.querySelector('button')).not.toBeNull();
    expect(compiled.querySelector('button').textContent).toContain('Go back to dashboard');

    // Check page links.
    spyOn(component, 'goToDashboard');
    fixture.debugElement.nativeElement.querySelector('button').click();
    fixture.whenStable().then(() => {
      expect(component.goToDashboard).toHaveBeenCalled();
    });
  });

  // ===================================================================================================================
  it('should display the page (with error)', () => {
    fixture = TestBed.createComponent(ErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;

    // Check component data.
    expect(component.error).not.toBeUndefined();
    expect(component.error).toEqual({
      type: LooseTouchError.TypeEnum.ApiConnectionError,
      message: 'Unit test error message',
      errors: []
    });

    // Check page data.
    expect(TestBed.get(Title).getTitle()).toEqual('An error occurred');
    expect(compiled.querySelector('mat-card-title').textContent).toContain('An error occurred !');
    expect(compiled.querySelector('mat-card-subtitle')).not.toBeNull();
    expect(compiled.querySelector('mat-card-subtitle').textContent).toEqual('Unit test error message');
    expect(compiled.querySelector('button')).not.toBeNull();
    expect(compiled.querySelector('button').textContent).toEqual('Go back to dashboard');

    // Check page links.
    spyOn(component, 'goToDashboard');
    document.getElementById('button-go-to-dashboard').click();
    fixture.whenStable().then(() => {
      expect(component.goToDashboard).toHaveBeenCalled();
    });
  });

  // ===================================================================================================================
  it('should display the error image', (done) => {
    fixture = TestBed.createComponent(ErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    const googleSignIn = window.getComputedStyle(document.getElementById('error-image')).backgroundImage;
    imageExists(googleSignIn, function(exists) {
      expect(exists).toBe(true);
      done();
    });
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
