import {browser, by, element} from 'protractor';

/**
 * Login page.
 */
export class LoginPage {

  navigateTo() {
    return browser.get('/login');
  }

}
