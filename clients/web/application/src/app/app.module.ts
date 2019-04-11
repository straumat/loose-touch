import {BrowserModule, Title} from '@angular/platform-browser';
import {ErrorHandler, NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {CoreModule} from './core/core.module';
import {FeaturesModule} from './features/features.module';
import {CustomMaterialModule} from './core/material.module';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {AuthenticationErrorHandler} from './core/error/authentication-error-handler';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CustomMaterialModule,
    FontAwesomeModule,
    CoreModule,
    FeaturesModule,
  ],
  providers: [Title,
    {
      provide: ErrorHandler,
      useClass: AuthenticationErrorHandler
    }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
