import {TestBed} from '@angular/core/testing';

import {AccountService} from './account.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient} from '@angular/common/http';
import {AccountDTO} from '../models/api';

describe('AccountService', () => {

  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    // Inject the http service and test controller for each test
    httpClient = TestBed.get(HttpClient);
    httpTestingController = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    const service: AccountService = TestBed.get(AccountService);
    expect(service).toBeTruthy();
  });

  it('We should receive profile data', () => {
    const service: AccountService = TestBed.get(AccountService);

    // Test account data.
    const expectedData: AccountDTO = {
      idToken: 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyYjI5ZTY3ZS0zM2U0LTRiMGItYmY5YS1jOTIxNDJlZmQ5MWEiLCJpYXQiOjE1NTQ5ODMzMTYsImV4cCI6MTU1NzU3NTMxNn0._Jj8TLKQJAjGnzq4om2ujbLTgNCf_ZhJdhlNYiuk3xE',
      firstName: 'loose 1',
      lastName: 'touch 1',
      email: 'loose.touch.test.1@gmail.com',
      pictureUrl: 'https://lh5.googleusercontent.com/-vTIMhyL9ePM/AAAAAAAAAAI/AAAAAAAAAAA/ACHi3rfUfyX7ftUhaB_oui4jnuvnQMBhww/s96-c/photo.jpg',
      newAccount: true
    };

    // Calling the service & testing the result.
    service.getProfile().subscribe(data => {
      expect(data.idToken).toBe(expectedData.idToken);
      expect(data.firstName).toBe(expectedData.firstName);
      expect(data.lastName).toBe(expectedData.lastName);
      expect(data.email).toBe(expectedData.email);
      expect(data.pictureUrl).toBe(expectedData.pictureUrl);
      expect(data.newAccount).toBe(expectedData.newAccount);
    });

    // We set the expectations for the HttpClient mock
    const req = httpTestingController.expectOne('http://localhost:8080/v1/account/profile');
    expect(req.request.method).toEqual('GET');

    // Then we set the fake data to be returned by the mock
    req.flush({
      idToken: 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyYjI5ZTY3ZS0zM2U0LTRiMGItYmY5YS1jOTIxNDJlZmQ5MWEiLCJpYXQiOjE1NTQ5ODMzMTYsImV4cCI6MTU1NzU3NTMxNn0._Jj8TLKQJAjGnzq4om2ujbLTgNCf_ZhJdhlNYiuk3xE',
      firstName: 'loose 1',
      lastName: 'touch 1',
      email: 'loose.touch.test.1@gmail.com',
      pictureUrl: 'https://lh5.googleusercontent.com/-vTIMhyL9ePM/AAAAAAAAAAI/AAAAAAAAAAA/ACHi3rfUfyX7ftUhaB_oui4jnuvnQMBhww/s96-c/photo.jpg',
      newAccount: true
    });

  });

  it('Calling disconnect should disconnect', () => {
    const service: AccountService = TestBed.get(AccountService);
    localStorage.setItem('token', 'test');
    expect(localStorage.getItem('token')).toEqual('test');
    service.disconnect();
    expect(localStorage.getItem('token')).toBeNull();
  });

  it('Calling delete should disconnect', () => {
    const service: AccountService = TestBed.get(AccountService);
    localStorage.setItem('token', 'test');
    expect(localStorage.getItem('token')).toEqual('test');
    service.delete();
    expect(localStorage.getItem('token')).toBeNull();
  });

});
