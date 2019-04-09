import {DashboardPage} from '../page-objects/dashboard.po';
import {LoginPage} from '../page-objects/login.po';
import {browser} from 'protractor';
import LocalTokenStorage from '../util/localStorageUtil';

/**
 * E2E tests for the account management.
 */
describe('Account management test', () => {

  /**
   * Application pages.
   */
  let loginPage: LoginPage;
  let dashboardPage: DashboardPage;

  beforeEach(() => {
    loginPage = new LoginPage();
    dashboardPage = new DashboardPage();
  });

  // ===================================================================================================================
  it('Accessing application without token', () => {
    // Going to "/".
    browser.get('/');
    browser.getTitle().then(function(title) {
      this.expect(title).toEqual('Loose touch login');
    });

    // Going to "dashboard".
    dashboardPage.navigateTo();
    browser.getTitle().then(function(title) {
      this.expect(title).toEqual('Loose touch login');
    });

    // Going to "login".
    loginPage.navigateTo();
    browser.getTitle().then(function(title) {
      this.expect(title).toEqual('Loose touch login');
    });
  });

  // ===================================================================================================================
  it('Accessing application with expired token', () => {
    browser.get('/');
    LocalTokenStorage.setExpiredToken();

    // Going to "/".
    browser.get('/');
    browser.getTitle().then(function(title) {
      this.expect(title).toEqual('Loose touch login');
    });

    // Going to "dashboard".
    dashboardPage.navigateTo();
    browser.getTitle().then(function(title) {
      this.expect(title).toEqual('Loose touch login');
    });

    // Going to "login".
    loginPage.navigateTo();
    browser.getTitle().then(function(title) {
      this.expect(title).toEqual('Loose touch login');
    });
  });

  // ===================================================================================================================
  it('Accessing application with valid token', () => {
    browser.get('/');
    LocalTokenStorage.setValidToken();

    // Going to "/".
    browser.get('/');
    browser.getTitle().then(function(title) {
      this.expect(title).toEqual('Dashboard');
    });

    // Going to "dashboard".
    dashboardPage.navigateTo();
    browser.getTitle().then(function(title) {
      this.expect(title).toEqual('Dashboard');
    });

    // Going to "login".
    loginPage.navigateTo();
    browser.getTitle().then(function(title) {
      this.expect(title).toEqual('Loose touch login');
    });
  });

  // ===================================================================================================================

});
