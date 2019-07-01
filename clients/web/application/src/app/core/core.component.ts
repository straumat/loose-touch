import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-core',
  templateUrl: './core.component.html',
  styleUrls: ['./core.component.css']
})
export class CoreComponent implements OnInit {

  constructor(public router: Router,
              private route: ActivatedRoute) {
    // We retrieve any error passed throw data.
    // console.log('===> ' + this.router.getCurrentNavigation().extras.state);
    // // this.route.
    // this.route.data.subscribe(data => {
    // });
  }

  ngOnInit() {

  }

}
