import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {filter} from 'rxjs/operators';
import {Title} from '@angular/platform-browser';
import {LooseTouchError} from 'angular-loose-touch-api';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {

  /**
   * Error retrieved.
   */
  error: LooseTouchError = undefined;

  constructor(private titleService: Title,
              public router: Router,
              private route: ActivatedRoute) {
    this.titleService.setTitle('An error occurred');
  }

  ngOnInit() {
    // We retrieve any error passed throw data.
    this.route.data.subscribe(data => {
      this.error = data.looseTouchError;
    });
  }

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

}
