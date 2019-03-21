import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DashboardComponent} from './dashboard/dashboard.component';
import {DummyComponent} from './dummy/dummy.component';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";

@NgModule({
  declarations: [DashboardComponent, DummyComponent],
  imports: [
    CommonModule,
    NgbModule
  ]
})
export class FeaturesModule {
}
