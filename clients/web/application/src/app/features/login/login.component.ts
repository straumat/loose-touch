/* tslint:disable:no-string-literal */
import {Component, OnInit} from '@angular/core';
import {Title} from '@angular/platform-browser';
import {ActivatedRoute, Router} from '@angular/router';
import {AccountService} from '../../core/services/account.service';
import {AuthService, GoogleLoginProvider} from 'angularx-social-login';
import {LooseTouchError} from 'angular-loose-touch-api';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  /**
   * If there is an authentication error.
   */
  error: LooseTouchError = undefined;

  constructor(private titleService: Title,
              public router: Router,
              private route: ActivatedRoute,
              private socialLoginService: AuthService,
              public accountService: AccountService) {
    this.titleService.setTitle('Loose touch connexion');
  }

  ngOnInit() {
    // We retrieve any error passed throw data.
    this.route.data.subscribe(data => {
      this.error = data.looseTouchError;
    });

    this.socialLoginService.authState.subscribe((user) => {
      // Login success ==> Connecting to account service.
      this.accountService.googleLogin(user.idToken, user.authToken).subscribe(() => {
        // Account service connection successful.
        this.router.navigate(['/dashboard']);
      }, () => {
        // Account service connection failed.
        this.error = {
          type: LooseTouchError.TypeEnum.AuthenticationError,
          message: 'Impossible to connect to the authentication',
          errors: []
        };
      });
    }, () => {
      // Google login failed.
      this.error = {
        type: LooseTouchError.TypeEnum.AuthenticationError,
        message: 'Unknown error',
        errors: []
      };
    });
  }

  /**
   * Sign in with google.
   */
  signInWithGoogle(): void {
    this.socialLoginService.signIn(GoogleLoginProvider.PROVIDER_ID)
      .catch(reason => {
        // If the user cancels the login.
        this.error = {
          type: LooseTouchError.TypeEnum.AuthenticationError,
          message: reason,
          errors: []
        };
      });
  }

}
