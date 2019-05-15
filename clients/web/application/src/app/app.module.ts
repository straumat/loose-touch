import {BrowserModule, Title} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {CoreModule} from './core/core.module';
import {FeaturesModule} from './features/features.module';
import {SharedModule} from './shared/shared.module';
import {ApplicationInterceptor} from './core/interceptors/application-interceptor.service';
import {HTTP_INTERCEPTORS} from '@angular/common/http';

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
    SharedModule
  ],
  providers: [
    Title,
    {provide: HTTP_INTERCEPTORS, useClass: ApplicationInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
