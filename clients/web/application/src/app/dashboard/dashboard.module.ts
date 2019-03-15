import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DashboardRoutingModule} from "./dashboard-routing.module";
import {MatchHeightModule} from "../shared/directives/match-height.directive";
import {DashboardComponent} from "./dashboard.component";

@NgModule({
  imports: [
    CommonModule,
    DashboardRoutingModule,
    MatchHeightModule
  ],
  exports: [],
  declarations: [DashboardComponent],
  providers: [],
})
export class DashboardModule {
}
