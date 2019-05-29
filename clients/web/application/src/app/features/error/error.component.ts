import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {LooseTouchError} from '../../core/models/looseToucheError';
import {filter} from 'rxjs/operators';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {

  readonly looseTouchErrorParameter = 'looseTouchError';

  /**
   * Error found.
   */
  error: LooseTouchError = undefined;

  constructor(private titleService: Title, public router: Router, private route: ActivatedRoute) {
    this.titleService.setTitle('An error occurred !');
  }

  ngOnInit() {
    // We check if somme errors were passed to the component thanks to queryParams.
    // TODO Use extra data instead if query params.
    this.route.queryParams.pipe(
      filter(params => params[this.looseTouchErrorParameter] != null)
    ).subscribe(params =>
      this.error = JSON.parse(params[this.looseTouchErrorParameter])
    );
    // If not, we check that there is no message in context data.
    if (this.error == null) {
      this.route.data.pipe(
        filter(value => value != null)
      ).subscribe(value => {
        this.error = value.looseTouchError;
      });
    }
  }

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

}
