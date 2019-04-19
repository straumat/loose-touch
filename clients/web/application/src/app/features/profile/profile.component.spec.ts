import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProfileComponent} from './profile.component';
import {CustomMaterialModule} from '../../core/material.module';
import {AccountService} from '../../core/services/account.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {AccountDTO} from '../../core/models/api';
import {Observable} from 'rxjs';
import {of} from 'rxjs';

describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let accountServiceStub: Partial<AccountService>;

  // Mocked service.
  accountServiceStub = {

    getProfile(): Observable<AccountDTO> {
      const expectedData: AccountDTO = {
        idToken: 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyYjI5ZTY3ZS0zM2U0LTRiMGItYmY5YS1jOTIxNDJlZmQ5MWEiLCJpYXQiOjE1NTQ5ODMzMTYsImV4cCI6MTU1NzU3NTMxNn0._Jj8TLKQJAjGnzq4om2ujbLTgNCf_ZhJdhlNYiuk3xE',
        firstName: 'loose 1',
        lastName: 'touch 1',
        email: 'loose.touch.test.1@gmail.com',
        pictureUrl: 'https://lh5.googleusercontent.com/-vTIMhyL9ePM/AAAAAAAAAAI/AAAAAAAAAAA/ACHi3rfUfyX7ftUhaB_oui4jnuvnQMBhww/s96-c/photo.jpg',
        newAccount: true
      };
      return of(expectedData);
    },

    disconnect() {
      console.log('accountServiceStub - Disconnect');
    },

    delete() {
      console.log('accountServiceStub - Delete');
    }

  };

  beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [CustomMaterialModule, HttpClientTestingModule],
        declarations: [ProfileComponent],
        providers: [{provide: AccountService, useValue: accountServiceStub}]
      })
        .compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Should display user profile', () => {
    fixture = TestBed.createComponent(ProfileComponent);
    const compiled = fixture.debugElement.nativeElement;
    fixture.detectChanges();
    expect(compiled.querySelector('div.profile>img').src).toContain('https://lh5.googleusercontent.com/-vTIMhyL9ePM/AAAAAAAAAAI/AAAAAAAAAAA/ACHi3rfUfyX7ftUhaB_oui4jnuvnQMBhww/s96-c/photo.jpg');
    expect(compiled.querySelector('mat-card-title').textContent).toContain('loose 1 touch 1');
  });

});
