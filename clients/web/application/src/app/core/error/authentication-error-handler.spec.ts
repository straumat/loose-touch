import {TestBed} from '@angular/core/testing';
import {AuthenticationErrorHandler} from './authentication-error-handler';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';

describe('Authentication error handler', () => {

  let mock: HttpTestingController;

  beforeEach(() => TestBed.configureTestingModule(({
    declarations: [],
    imports: [HttpClientTestingModule],
    providers: [AuthenticationErrorHandler],
  })));

  beforeEach(() => {
    mock = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    mock.verify();
  });

  it('should be created', () => {
    const component: AuthenticationErrorHandler = TestBed.get(AuthenticationErrorHandler);
    expect(component).toBeTruthy();
  });

  it('Error handler should not be called if everything is ok', () => {
    const component: AuthenticationErrorHandler = TestBed.get(AuthenticationErrorHandler);
    spyOn(component, 'handleError');
    // mock.expectOne('/test').flush(null, {status: 200, statusText: 'Request OK'});
    // expect(component.handleError).toHaveBeenCalled();
  });

});
