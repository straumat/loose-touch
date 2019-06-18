import {Component, OnInit} from '@angular/core';
import {AccountService} from '../../core/services/account.service';
import {AuthService, GoogleLoginProvider} from 'angularx-social-login';
import {LooseTouchError} from 'angular-loose-touch-api';
import {environment} from '../../../environments/environment';
import {Router} from '@angular/router';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {

  constructor(public router: Router,
              private socialLoginService: AuthService,
              public accountService: AccountService) {
  }

  ngOnInit() {

  }

  signOut(): void {
    this.socialLoginService.signOut(false)
      .then(value => {
        this.accountService.signOut();
        this.router.navigate(['/login']);
      })
      .catch(reason => {
        // TODO If we fail to disconnect
        /*        this.error = {
                  type: LooseTouchError.TypeEnum.AuthenticationError,
                  message: reason,
                  errors: []
                };*/
      });
  }


}
