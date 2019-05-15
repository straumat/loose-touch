import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {UNAUTHORIZED} from 'http-status-codes';
import {Router} from '@angular/router';
import {state} from '@angular/animations';

@Injectable()
export class ApplicationInterceptor implements HttpInterceptor {

  constructor(public router: Router) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    return next.handle(request).pipe(
      catchError(
        error =>
          new Observable<HttpEvent<any>>(observer => {

            // If we get an HTTP error.
            if (error instanceof HttpErrorResponse) {
              // Switch on error.
              const httpErrorResponse: HttpErrorResponse = error as HttpErrorResponse;
              switch (httpErrorResponse.status) {

                // -----------------------------------------------------------------------------------------------------
                // 401 - UNAUTHORIZED.
                case UNAUTHORIZED: {
                  this.router.navigate(['/login'], {state: httpErrorResponse.error});
                  break;
                }

                // -----------------------------------------------------------------------------------------------------
                // Others errors that should be transferred.
                default: {
                  observer.error(error);
                  observer.complete();
                  break;
                }

              }
            }
          })
      )
    );
  }

}
