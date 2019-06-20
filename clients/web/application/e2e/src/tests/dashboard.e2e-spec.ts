import {LoginPage} from '../page-objects/login.po';
import {browser, by, element, protractor} from 'protractor';
import {DashboardPage} from '../page-objects/dashboard.po';
import {clearStorage, getToken, setExpiredToken, setInvalidToken, setToken} from '../util/localStorageUtil';
import {HttpClient} from 'protractor-http-client/dist/http-client';
import {ResponsePromise} from 'protractor-http-client/dist/promisewrappers';

/**
 * E2E tests for the dashboard page.
 */
describe('E2E tests for the dashboard page', () => {

  let dashboardPage: DashboardPage;

  /**
   * Authentication.
   */
  let user1LooseTouchToken: string;

  beforeAll(() => {
    /* tslint:disable:no-string-literal */
    const httpForGoogle = new HttpClient();
    const httpForLooseTouch = new HttpClient();

    // Retrieve user 1 token.
    httpForGoogle.post('https://www.googleapis.com/oauth2/v4/token', {
      client_id: '408314219149-60s8l2ltrbal82hnuj35u81opt27ghsa.apps.googleusercontent.com',
      client_secret: 'NsR7_eU8KCLZ85BEzske6v_C',
      refresh_token: '1/k3ZDbF3kyC5FJACcHq-ByiIIlgZPPZ7M3hiv0PyWW4E',
      grant_type: 'refresh_token'
    }).then((response: Response) => {
      const user1GoogleToken = response['body']['id_token'];
      const user1GoogleAccessToken = response['body']['access_token'];
      const responseBody: ResponsePromise = httpForLooseTouch
        .get('http://localhost:8080/v1/login/google?googleIdToken=' + user1GoogleToken + '&googleAccessToken=' + user1GoogleAccessToken,
          {
            'Content-Type': 'application/json',
            Accept: 'application/json'
          });
      responseBody.jsonBody.then(value => {
        user1LooseTouchToken = value.idToken;
      });
    });
  });

  beforeEach(() => {
    browser.ignoreSynchronization = true;
    dashboardPage = new DashboardPage();
    dashboardPage.navigateTo();
  });

  // ===================================================================================================================
  it('Accessing empty dashboard with valid token', () => {
    setToken(user1LooseTouchToken);
    dashboardPage.navigateTo();
    expect(dashboardPage.getTitle()).toEqual('Dashboard');
    expect(dashboardPage.getAccountName()).toEqual('loose 1 touch 1');
    expect(dashboardPage.getAccountPicture()).not.toBeNull();
  });

  // ===================================================================================================================
  it('Disconnecting', () => {
    setToken(user1LooseTouchToken);
    dashboardPage.navigateTo();
    expect(dashboardPage.getTitle()).toEqual('Dashboard');

    // Disconnecting.
    dashboardPage.disconnect();
    expect(browser.getCurrentUrl()).toContain('login');
    expect(browser.getTitle()).toEqual('Loose touch connexion');
  });


});
