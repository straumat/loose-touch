import {TestBed} from '@angular/core/testing';

import {AuthenticationService} from './authentication.service';
import {JwtHelperService, JwtModule} from '@auth0/angular-jwt';

describe('AuthenticationService', () => {
  beforeEach(() => TestBed.configureTestingModule(({
    declarations: [],
    imports: [
      JwtModule.forRoot({
        config: {
          tokenGetter: () => {
            return localStorage.getItem('token');
          }
        }
      })
    ],
    providers: [JwtHelperService],  // all all services upon which AuthService depends
  })));

  it('should be created', () => {
    const service: AuthenticationService = TestBed.get(AuthenticationService);
    expect(service).toBeTruthy();
  });
});
