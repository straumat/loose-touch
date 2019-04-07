import {Component, OnInit} from '@angular/core';
import {faSignInAlt} from '@fortawesome/free-solid-svg-icons';
import {library} from '@fortawesome/fontawesome-svg-core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor() {
    library.add(faSignInAlt);
  }

  ngOnInit() {
  }

}
