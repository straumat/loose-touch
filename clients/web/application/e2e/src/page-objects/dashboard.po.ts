import {browser, by, element} from 'protractor';

/**
 * Dashboard page.
 */
export class DashboardPage {

  navigateTo() {
    return browser.get('/dashboard');
  }

}
