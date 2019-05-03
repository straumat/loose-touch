import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {AccountService} from '../services/account.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationGuardService implements CanActivate {

  constructor(public router: Router, public accountService: AccountService) {
  }

  /**
   * Can activate only if a non expired token is present.
   * If not, we forward the user to the login page.
   * @param route route
   * @param state state
   */
  canActivate(route: ActivatedRouteSnapshot,
              state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.accountService.isTokenPresent()) {
      return true;
    } else {
      this.router.navigate(['login']);
      return false;
    }
  }

}
