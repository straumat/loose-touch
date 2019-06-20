import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DashboardComponent} from './dashboard/dashboard.component';
import {LoginComponent} from './login/login.component';
import {CustomMaterialModule} from '../core/material.module';
import {ErrorComponent} from './error/error.component';
import {AccountComponent} from './account/account.component';
import {ContactComponent} from './contact/contact.component';
import {ReactiveFormsModule} from '@angular/forms';

@NgModule({
  declarations: [DashboardComponent, LoginComponent, ErrorComponent, AccountComponent, ContactComponent],
  imports: [
    CommonModule,
    CustomMaterialModule,
    ReactiveFormsModule
  ]
})
export class FeaturesModule {
}
