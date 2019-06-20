import {Component, OnInit} from '@angular/core';
import {AccountService} from '../../core/services/account.service';
import {AuthService} from 'angularx-social-login';
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
    this.accountService.signOut();
    // TODO Add a message saying "you have been disconnected".
    this.router.navigate(['/login']);
    this.socialLoginService.signOut(false);
  }


}
