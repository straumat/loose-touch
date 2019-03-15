import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard/dashboard.component';
import { DummyComponent } from './dummy/dummy.component';

@NgModule({
  declarations: [DashboardComponent, DummyComponent],
  imports: [
    CommonModule
  ]
})
export class FeaturesModule { }
