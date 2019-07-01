import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CoreComponent} from './core.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {ApiModule} from 'angular-loose-touch-api';
import {apiConfigFactory} from '../app.module';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {AccountComponent} from '../features/account/account.component';
import {JwtModule} from '@auth0/angular-jwt';
import {environment} from '../../environments/environment';
import {SocialLoginModule} from 'angularx-social-login';

describe('CoreComponent', () => {
  let component: CoreComponent;
  let fixture: ComponentFixture<CoreComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CoreComponent],
      imports: [
        HttpClientTestingModule,
        [RouterTestingModule.withRoutes([])],
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
