import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DashboardComponent} from './dashboard/dashboard.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {LoginComponent} from './login/login.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {CustomMaterialModule} from '../core/material.module';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {ProfileComponent} from './profile/profile.component';
import {ContactComponent} from './contact/contact.component';

@NgModule({
  declarations: [DashboardComponent, LoginComponent, PageNotFoundComponent, ProfileComponent, ContactComponent],
  imports: [
    CommonModule,
    NgbModule,
    CustomMaterialModule,
    FontAwesomeModule
  ]
})
export class FeaturesModule {
}
