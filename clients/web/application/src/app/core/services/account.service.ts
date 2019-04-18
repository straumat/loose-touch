import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {AccountDTO} from '../models/api';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  /**
   * Profile URL.
   */
  PROFILE_URL: string = environment.apiURL + '/account/profile';

  /**
   * Delete URL.
   */
  DELETE_URL: string = environment.apiURL + '/account/delete';

  constructor(private http: HttpClient) {
  }

  public getProfile() {
    return this.http.get<AccountDTO>(this.PROFILE_URL);
  }

  public disconnect() {
    localStorage.clear();
  }

  public delete() {
    this.http.delete(this.DELETE_URL);
    this.disconnect();
  }

}
