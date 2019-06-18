import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DashboardComponent} from './dashboard.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {AuthServiceConfig} from 'angularx-social-login';
import {apiConfigFactory, provideSocialLoginConfiguration} from '../../app.module';
import {ActivatedRoute, Data} from '@angular/router';
import {AccountAPIService} from 'angular-loose-touch-api';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DashboardComponent],
      imports: [
        HttpClientTestingModule,
        [RouterTestingModule.withRoutes([
          {path: 'dashboard', component: DashboardComponent}
        ])]],
      providers: [
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // ===================================================================================================================
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // ===================================================================================================================

});
