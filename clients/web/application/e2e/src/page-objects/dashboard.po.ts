import {browser, by, element} from 'protractor';

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

  getAccountPicture() {
    return element(by.id('account-picture')).getAttribute('src');
  }

  getAccountName() {
    return element(by.id('account-name')).getText();
  }

  disconnect() {
    element(by.id('button-disconnect')).click();
  }

}
