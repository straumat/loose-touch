import {Component, OnInit} from '@angular/core';
import {Title} from '@angular/platform-browser';
import {Router} from '@angular/router';
import {AccountService} from '../../core/services/account.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private titleService: Title, public router: Router, public accountService: AccountService) {
    this.titleService.setTitle('Loose touch connexion');
  }

  ngOnInit() {
  }

  /**
   * Sign in with google.
   */
  signInWithGoogle(): void {

    this.router.navigate(['dashboard']);
  }

}
