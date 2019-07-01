import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {DashboardComponent} from './dashboard.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {Router} from '@angular/router';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {ApiModule} from 'angular-loose-touch-api';
import {apiConfigFactory} from '../../app.module';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DashboardComponent],
      imports: [
        ApiModule.forRoot(apiConfigFactory),
        HttpClientTestingModule,
        [RouterTestingModule.withRoutes([
          {path: 'contacts/new', component: DashboardComponent},
          {path: 'dashboard', component: DashboardComponent}
        ])]],
      providers: [],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
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
  it('add contact link should send you to the new contact form', fakeAsync(() => {
    const router: Router = TestBed.get(Router);
    document.getElementById('button-add-new-contact').click();
    tick();
    expect(router.url).toBe('/contacts/new');
  }));

});
