import {Component, OnInit} from '@angular/core';
import {Title} from '@angular/platform-browser';
import {ActivatedRoute, Router} from '@angular/router';
import {AccountDTO} from 'angular-loose-touch-api/model/accountDTO';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  account: AccountDTO = undefined;

  constructor(private titleService: Title,
              public router: Router,
              private route: ActivatedRoute) {
    this.titleService.setTitle('Dashboard');
  }

  ngOnInit() {
    // this.accountAPIService.getProfile('body').subscribe(value => {
    //   this.account = value;
    // }, error => console.log('An error occured : ', error));
  }

}
