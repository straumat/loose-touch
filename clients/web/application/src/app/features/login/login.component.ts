import {Component, OnInit} from '@angular/core';
import {faSignInAlt} from '@fortawesome/free-solid-svg-icons';
import {library} from '@fortawesome/fontawesome-svg-core';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  constructor(private titleService: Title) {
    library.add(faSignInAlt);
    this.titleService.setTitle('Loose touch login');
  }

}
