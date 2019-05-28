/* tslint:disable:no-string-literal */
import {Component, OnInit} from '@angular/core';
import {Title} from '@angular/platform-browser';
import {ActivatedRoute, Router} from '@angular/router';
import {AccountService} from '../../core/services/account.service';
import {filter} from 'rxjs/operators';
import {LooseTouchError, LooseTouchErrorType} from '../../core/models/looseToucheError';
import {AuthService, GoogleLoginProvider} from 'angularx-social-login';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  /**
   * If there is an authentication error.
   */
  error: LooseTouchError = null;

  constructor(private titleService: Title,
              public router: Router,
              private route: ActivatedRoute,
              private socialLoginService: AuthService,
              public accountService: AccountService) {
    this.titleService.setTitle('Loose touch connexion');
  }

  ngOnInit() {
    // We check if somme errors were passed to the component.
    // TODO Use extra data instead if query params.
    this.route.queryParams.pipe(
      filter(params => params['looseTouchError'] != null)
    ).subscribe(params =>
      this.error = JSON.parse(params['looseTouchError'])
    );

    // Google login.
    this.socialLoginService.authState.subscribe((user) => {
      // Login success ==> Connecting to account service.
      this.accountService.googleLogin(user.idToken, user.authToken).subscribe(() => {
        // Account service connection successful.
        this.router.navigate(['/dashboard']);
      }, () => {
        // Account service connection failed.
        this.error = {
          type: LooseTouchErrorType.authentication_error,
          message: 'Impossible to connect to the authentication.',
          errors: []
        };
      });
      // Google login failed.
    }, () => {
      this.error = {
        type: LooseTouchErrorType.authentication_error,
        message: 'Unknown error.',
        errors: []
      };
    });
  }

  /**
   * Sign in with google.
   */
  signInWithGoogle(): void {
    this.socialLoginService.signIn(GoogleLoginProvider.PROVIDER_ID)
    // If the user cancels the login.
      .catch(reason => {
        this.error = {
          type: LooseTouchErrorType.authentication_error,
          message: reason,
          errors: []
        };
      });
  }

}
