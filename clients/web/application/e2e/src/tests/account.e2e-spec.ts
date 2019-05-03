import {DashboardPage} from '../page-objects/dashboard.po';
import {LoginPage} from '../page-objects/login.po';
import {browser} from 'protractor';
import LocalTokenStorage from '../util/localStorageUtil';
import {HttpClient} from 'protractor-http-client';

/**
 * E2E tests for the account management.
 */
describe('Account management test', () => {

  /**
   * Application pages.
   */
  let loginPage: LoginPage;
  let dashboardPage: DashboardPage;

  /**
   * Authentication.
   */
  let user1GoogleToken: string;
  let user2GoogleToken: string;

  beforeAll(() => {
    const http = new HttpClient();

    // Retrieve user 1 token.
    http.post('https://www.googleapis.com/oauth2/v4/token', {
      client_id: '408314219149-60s8l2ltrbal82hnuj35u81opt27ghsa.apps.googleusercontent.com',
      client_secret: 'NsR7_eU8KCLZ85BEzske6v_C',
      refresh_token: '1/ST2ikxLlVoH3K352DN60nUQn8KW4NBpf2er2Q7QReF7qxNrXGNr4jqeLRBlPdwyc',
      grant_type: 'refresh_token'
    }).then((res: Response) => {
      // user1GoogleToken = res['body']['id_token'];
      user1GoogleToken = '';
    });

    // Retrieve user 2 token.
    http.post('https://www.googleapis.com/oauth2/v4/token', {
      client_id: '408314219149-60s8l2ltrbal82hnuj35u81opt27ghsa.apps.googleusercontent.com',
      client_secret: 'NsR7_eU8KCLZ85BEzske6v_C',
      refresh_token: '1/H50lw_FYGKr0xKJMDFC_FJ6jmEiTc8-WjEPRr8SWgLY',
      grant_type: 'refresh_token'
    }).then((res: Response) => {
      // user2GoogleToken = res['body']['id_token'];
      user2GoogleToken = '';
    });
  });

  beforeEach(() => {
    loginPage = new LoginPage();
    dashboardPage = new DashboardPage();
  });

  // ===================================================================================================================
  it('Accessing application without token', () => {
    // Going to "/".
    browser.get('/');
    browser.getTitle().then(function(title) {
      expect(title).toEqual('Loose touch login');
    });

    // Going to "dashboard".
    dashboardPage.navigateTo();
    browser.getTitle().then(function(title) {
      expect(title).toEqual('Loose touch login');
    });

    // Going to "login".
    loginPage.navigateTo();
    browser.getTitle().then(function(title) {
      expect(title).toEqual('Loose touch login');
    });
  });

  // ===================================================================================================================
  it('Accessing application with expired token', () => {
    browser.get('/');
    LocalTokenStorage.setExpiredToken();

    // Going to "/".
    browser.get('/');
    browser.getTitle().then(function(title) {
      expect(title).toEqual('Loose touch login');
    });

    // Going to "dashboard".
    dashboardPage.navigateTo();
    browser.getTitle().then(function(title) {
      expect(title).toEqual('Loose touch login');
    });

    // Going to "login".
    loginPage.navigateTo();
    browser.getTitle().then(function(title) {
      expect(title).toEqual('Loose touch login');
    });
  });

  // ===================================================================================================================
  it('Accessing application with valid token', () => {
    browser.get('/');
    LocalTokenStorage.setValidToken();

    // Going to "/".
    browser.get('/');
    browser.getTitle().then(function(title) {
      expect(title).toEqual('Dashboard');
    });

    // Going to "dashboard".
    dashboardPage.navigateTo();
    browser.getTitle().then(function(title) {
      expect(title).toEqual('Dashboard');
    });

    // Going to "login".
    loginPage.navigateTo();
    browser.getTitle().then(function(title) {
      expect(title).toEqual('Loose touch login');
    });
  });

  // ===================================================================================================================
  it('Accessing application with valid token', () => {
    console.log(user1GoogleToken);
    console.log('===========');
    console.log(user2GoogleToken);
  });

});
