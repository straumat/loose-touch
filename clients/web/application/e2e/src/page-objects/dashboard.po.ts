import {browser} from 'protractor';

/**
 * Dashboard page.
 */
export class DashboardPage {

  navigateTo() {
    return browser.get('/dashboard');
  }

  getTitle() {
    return browser.getTitle();
  }

}
