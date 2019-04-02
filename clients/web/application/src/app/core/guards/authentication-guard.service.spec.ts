import {TestBed} from '@angular/core/testing';

import {AuthenticationGuardService} from './authentication-guard.service';
import {RouterModule} from '@angular/router';
import {JwtHelperService, JwtModule} from '@auth0/angular-jwt';

describe('AuthenticationGuardService', () => {

  beforeEach(() => TestBed.configureTestingModule(({
    declarations: [],
    imports: [
      RouterModule.forRoot([])
    ],
    providers: [JwtHelperService],  // all all services upon which AuthService depends
  })));

  it('should be created', () => {
    const service: AuthenticationGuardService = TestBed.get(AuthenticationGuardService);
    expect(service).toBeTruthy();
  });

});
