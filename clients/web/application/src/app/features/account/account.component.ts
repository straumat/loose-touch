import { Component, OnInit } from '@angular/core';
import {AccountService} from '../../core/services/account.service';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {

  constructor(public accountService: AccountService) { }

  ngOnInit() {

  }

}
