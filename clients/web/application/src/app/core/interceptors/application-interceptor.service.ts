import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {INTERNAL_SERVER_ERROR, UNAUTHORIZED} from 'http-status-codes';
import {Router} from '@angular/router';

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
              switch (error.status) {

                // -----------------------------------------------------------------------------------------------------
                // 401 - UNAUTHORIZED.
                case UNAUTHORIZED: {
                  this.router.navigate(['/login'], {queryParams: {looseTouchError: JSON.stringify(error.error)}});
                  break;
                }

                // -----------------------------------------------------------------------------------------------------
                // 500 - INTERNAL_SERVER_ERROR.
                case INTERNAL_SERVER_ERROR: {
                  this.router.navigate(['/error'], {queryParams: {looseTouchError: JSON.stringify(error.error)}});
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
