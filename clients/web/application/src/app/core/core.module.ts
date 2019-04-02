import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CoreComponent} from './core.component';
import {HeaderComponent} from './header/header.component';
import {AppRoutingModule} from '../app-routing.module';
import {CoreRoutingModule} from './core-routing.module';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {CustomMaterialModule} from './material.module';
import {JwtHelperService, JwtModule} from '@auth0/angular-jwt';
import {RouterModule} from "@angular/router";

@NgModule({
  declarations: [CoreComponent, HeaderComponent],
  imports: [
    AppRoutingModule,
    CoreRoutingModule,
    CommonModule,
    NgbModule,
    RouterModule.forRoot([]),
    JwtModule.forRoot({
      config: {
        tokenGetter: () => {
          return localStorage.getItem('token');
        },
        whitelistedDomains: ['*'],
        blacklistedRoutes: [] // TOODO Fix routes !
      }
    }),
    CustomMaterialModule
  ],
  providers: [
    JwtHelperService
  ]
})
export class CoreModule {
}
