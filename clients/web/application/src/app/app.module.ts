import {BrowserModule, Title} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {CoreModule} from './core/core.module';
import {FeaturesModule} from './features/features.module';
import {SharedModule} from './shared/shared.module';
import {ApplicationInterceptor} from './core/interceptors/application-interceptor.service';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {JwtModule} from '@auth0/angular-jwt';
import {environment} from '../environments/environment';
import {AuthServiceConfig, GoogleLoginProvider, SocialLoginModule} from 'angularx-social-login';
import {AuthenticationGuard} from './core/guards/authentication.guard';

/**
 * Social login configuration.
 */
const socialLoginConfiguration = new AuthServiceConfig([
  {
    id: GoogleLoginProvider.PROVIDER_ID,
    provider: new GoogleLoginProvider(environment.googleClientID)
  }
]);

export function provideSocialLoginConfiguration() {
  return socialLoginConfiguration;
}

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    CoreModule,
    FeaturesModule,
    SharedModule,
    HttpClientModule,
    SocialLoginModule,
    JwtModule.forRoot({
      config: {
        tokenGetter: () => {
          return localStorage.getItem(environment.tokenNameInLocalStorage);
        }
      }
    })
  ],
  providers: [
    Title,
    {provide: HTTP_INTERCEPTORS, useClass: ApplicationInterceptor, multi: true},
    {provide: AuthServiceConfig, useFactory: provideSocialLoginConfiguration},
    AuthenticationGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
