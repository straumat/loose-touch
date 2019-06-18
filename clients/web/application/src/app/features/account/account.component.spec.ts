import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AccountComponent} from './account.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {ApiModule} from 'angular-loose-touch-api';
import {apiConfigFactory, provideSocialLoginConfiguration} from '../../app.module';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {AuthServiceConfig, SocialLoginModule} from 'angularx-social-login';
import {ActivatedRoute, Data} from '@angular/router';
import {JwtModule} from '@auth0/angular-jwt';
import {environment} from '../../../environments/environment';

describe('AccountComponent', () => {
  let component: AccountComponent;
  let fixture: ComponentFixture<AccountComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AccountComponent],
      imports: [
        ApiModule.forRoot(apiConfigFactory),
        HttpClientTestingModule,
        [RouterTestingModule.withRoutes([
          {path: 'account', component: AccountComponent}
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
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});
