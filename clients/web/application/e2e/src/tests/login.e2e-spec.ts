/**
 * E2E tests for the login page.
 */
import {LoginPage} from '../page-objects/login.po';
import {browser} from 'protractor';
import {DashboardPage} from '../page-objects/dashboard.po';
import {clearStorage, setExpiredToken, setInvalidToken, setValidToken} from '../util/localStorageUtil';

describe('E2E tests for the login page', () => {

  let loginPage: LoginPage;
  let dashboardPage: DashboardPage;

  // ===================================================================================================================
  beforeEach(() => {
    loginPage = new LoginPage();
    dashboardPage = new DashboardPage();
    clearStorage();
  });

  // ===================================================================================================================
  it('Accessing dashboard without token', () => {
    dashboardPage.navigateTo();
    expect(browser.getTitle()).toEqual('Loose touch connexion');
  });

  // ===================================================================================================================
  it('Accessing dashboard with invalid token', () => {
    setInvalidToken();
    dashboardPage.navigateTo();
    expect(browser.getTitle()).toEqual('Loose touch connexion');
  });

  // ===================================================================================================================
  it('Accessing dashboard with expired token', () => {
    setExpiredToken();
    dashboardPage.navigateTo();
    expect(browser.getTitle()).toEqual('Loose touch connexion');
  });

  // ===================================================================================================================
  it('Accessing dashboard with valid token', () => {
    setValidToken();
    dashboardPage.navigateTo();
    expect(browser.getTitle()).toEqual('Dashboard');
  });

});
