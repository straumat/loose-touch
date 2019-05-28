import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DashboardComponent} from './dashboard/dashboard.component';
import {LoginComponent} from './login/login.component';
import {CustomMaterialModule} from '../core/material.module';
import { ErrorComponent } from './error/error.component';

@NgModule({
  declarations: [DashboardComponent, LoginComponent, ErrorComponent],
  imports: [
    CommonModule,
    CustomMaterialModule
  ]
})
export class FeaturesModule {
}
