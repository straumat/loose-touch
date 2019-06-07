import {browser} from 'protractor';

/**
 * Error page.
 */
export class ErrorPage {

  navigateTo() {
    return browser.get('/error');
  }

  getTitle() {
    return browser.getTitle();
  }

}
