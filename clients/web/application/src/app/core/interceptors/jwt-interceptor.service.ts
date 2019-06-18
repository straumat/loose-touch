import {Injectable} from '@angular/core';
import {HttpRequest, HttpHandler, HttpEvent, HttpInterceptor} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor() {
  }

  /**
   * This interceptor adds the jwt token to every call.
   * @param request request
   * @param next modified request
   */
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add authorization header with jwt token if available
    request = request.clone({
      setHeaders: {
        Authorization: `Bearer ${localStorage.getItem(environment.tokenNameInLocalStorage)}`
      }
    });
    return next.handle(request);
  }

}
