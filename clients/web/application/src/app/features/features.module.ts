import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DashboardComponent} from './dashboard/dashboard.component';
import {DummyComponent} from './dummy/dummy.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {LoginComponent} from './login/login.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {CustomMaterialModule} from '../core/material.module';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';

@NgModule({
  declarations: [DashboardComponent, DummyComponent, LoginComponent, PageNotFoundComponent],
  imports: [
    CommonModule,
    NgbModule,
    CustomMaterialModule,
    FontAwesomeModule
  ]
})
export class FeaturesModule {
}
