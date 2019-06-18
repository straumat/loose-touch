import {BrowserModule, Title} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {CoreModule} from './core/core.module';
import {FeaturesModule} from './features/features.module';
import {SharedModule} from './shared/shared.module';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {JwtModule} from '@auth0/angular-jwt';
import {environment} from '../environments/environment';
import {AuthServiceConfig, GoogleLoginProvider, SocialLoginModule} from 'angularx-social-login';
import {AuthenticationGuard} from './core/guards/authentication.guard';
import {ApiModule, Configuration, ConfigurationParameters} from 'angular-loose-touch-api';
import {JwtInterceptor} from './core/interceptors/jwt-interceptor.service';
import {ErrorInterceptor} from './core/interceptors/error-interceptor.service';

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

/**
 * Loose touch API configuration.
 */
export function apiConfigFactory(): Configuration {
  const params: ConfigurationParameters = {
    basePath: environment.apiURL
  };
  return new Configuration(params);
}


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    CoreModule,
    FeaturesModule,
    SharedModule,
    ApiModule.forRoot(apiConfigFactory),
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
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: AuthServiceConfig, useFactory: provideSocialLoginConfiguration},
    AuthenticationGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
