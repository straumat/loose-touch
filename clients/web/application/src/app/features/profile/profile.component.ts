import {Component, OnInit} from '@angular/core';
import {AccountService} from '../../core/services/account.service';
import {AccountDTO} from '../../core/models/api';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  profile: AccountDTO;

  constructor(private accountService: AccountService, private titleService: Title) {
    this.titleService.setTitle('Account profile');
  }

  ngOnInit() {
    this.accountService.getProfile().subscribe(data => {
      this.profile = data;
    });
  }

}
