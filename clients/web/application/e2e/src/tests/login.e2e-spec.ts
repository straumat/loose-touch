import {LoginPage} from '../page-objects/login.po';
import {browser, by, element} from 'protractor';
import {DashboardPage} from '../page-objects/dashboard.po';
import {clearStorage, setExpiredToken, setInvalidToken, setValidToken} from '../util/localStorageUtil';
import {HttpClient} from 'protractor-http-client/dist/http-client';
import {ResponsePromise} from 'protractor-http-client/dist/promisewrappers';

/**
 * E2E tests for the login page.
 */
describe('E2E tests for the login page', () => {

  let loginPage: LoginPage;
  let dashboardPage: DashboardPage;

  /**
   * Authentication.
   */
  let user1GoogleToken: string;
  let user1GoogleAccessToken: string;
  let user2GoogleToken: string;
  let user2GoogleAccessToken: string;
  let user1LooseTouchToken: string;
  let user2LooseTouchToken: string;

  beforeAll(() => {
    /* tslint:disable:no-string-literal */
    const httpForGoogle = new HttpClient();
    const httpForLooseTouch = new HttpClient();

    // Retrieve user 1 token.
    httpForGoogle.post('https://www.googleapis.com/oauth2/v4/token', {
      client_id: '408314219149-60s8l2ltrbal82hnuj35u81opt27ghsa.apps.googleusercontent.com',
      client_secret: 'NsR7_eU8KCLZ85BEzske6v_C',
      refresh_token: '1/ST2ikxLlVoH3K352DN60nUQn8KW4NBpf2er2Q7QReF7qxNrXGNr4jqeLRBlPdwyc',
      grant_type: 'refresh_token'
    }).then((response: Response) => {
      user1GoogleToken = response['body']['id_token'];
      user1GoogleAccessToken = response['body']['access_token'];
      console.log('Id token ', user1GoogleToken);
      console.log('Access token ', user1GoogleAccessToken);


      let responseBody: ResponsePromise = httpForLooseTouch.get('http://localhost:8080/v1/login/google?googleIdToken=' + user1GoogleToken + '&googleAccessToken=' + user1GoogleAccessToken, {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      });

      console.log('Mon token ');
      responseBody.jsonBody.then(value => {
        console.log(value.idToken);
      });

    });

    // Retrieve user 2 token.
    httpForGoogle.post('https://www.googleapis.com/oauth2/v4/token', {
      client_id: '408314219149-60s8l2ltrbal82hnuj35u81opt27ghsa.apps.googleusercontent.com',
      client_secret: 'NsR7_eU8KCLZ85BEzske6v_C',
      refresh_token: '1/H50lw_FYGKr0xKJMDFC_FJ6jmEiTc8-WjEPRr8SWgLY',
      grant_type: 'refresh_token'
    }).then((res: Response) => {
      user2GoogleToken = res['body']['id_token'];
      user2GoogleAccessToken = res['body']['access_token'];
    });
    /* tslint:enable:no-string-literal */
  });

  beforeEach(() => {
    loginPage = new LoginPage();
    dashboardPage = new DashboardPage();
    clearStorage();
  });

  // ===================================================================================================================
  it('Accessing dashboard without token', () => {
    loginPage.navigateTo();
    expect(browser.getTitle()).toEqual('Loose touch connexion');
    dashboardPage.navigateTo();
    expect(browser.getTitle()).toEqual('Loose touch connexion');
  });

  // ===================================================================================================================
  it('Accessing dashboard with invalid token', () => {
    setInvalidToken();
    loginPage.navigateTo();
    expect(browser.getTitle()).toEqual('Loose touch connexion');
    dashboardPage.navigateTo();
    expect(browser.getTitle()).toEqual('Loose touch connexion');
  });

  // ===================================================================================================================
  it('Accessing dashboard with expired token', () => {
    setExpiredToken();
    loginPage.navigateTo();
    expect(browser.getTitle()).toEqual('Loose touch connexion');
    dashboardPage.navigateTo();
    expect(browser.getTitle()).toEqual('Loose touch connexion');
  });

  // ===================================================================================================================
  it('Accessing dashboard with valid token', () => {
    setValidToken();
    loginPage.navigateTo();
    expect(browser.getTitle()).toEqual('Loose touch connexion');
    dashboardPage.navigateTo();
    expect(browser.getTitle()).toEqual('Dashboard');
    browser.sleep(10*1000)
    element(by.id(".alert")).getText().then(function(text) {
      console.log("===========>" + text);
    });
  });

});
