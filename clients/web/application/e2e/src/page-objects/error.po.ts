import {browser, by, element} from 'protractor';

/**
 * Error page.
 */
export class ErrorPage {

  navigateTo() {
    return browser.get('/error');
  }

}
