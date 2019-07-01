import {Component, OnInit} from '@angular/core';
import {Title} from '@angular/platform-browser';
import {ActivatedRoute, Router} from '@angular/router';
import {AccountDTO} from 'angular-loose-touch-api/model/accountDTO';
import {ContactAPIService} from 'angular-loose-touch-api';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  account: AccountDTO = undefined;

  constructor(private titleService: Title,
              public router: Router,
              private route: ActivatedRoute,
              private contactAPIService: ContactAPIService) {
    this.titleService.setTitle('Dashboard');
  }

  ngOnInit() {
    this.contactAPIService.getContactsToReach('body').subscribe(results => {
      console.log(results.length + ' Results');
    });
  }

}
