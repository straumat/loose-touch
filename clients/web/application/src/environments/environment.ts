// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

const API_URL = 'http://localhost:8080/v1';

export const environment = {
  production: false,

  /**
   * API.
   */
  // Authentication URL.
  GOOGLE_LOGIN_URL: API_URL + '/login/google',
  // Account URL.
  ACCOUNT_INFORMATION_URL: API_URL + '/account',
  // Delete account URL
  ACCOUNT_DELETE_URL: API_URL + '/account/delete',

  /**
   * Authentication.
   */
  googleClientID: '408314219149-60s8l2ltrbal82hnuj35u81opt27ghsa.apps.googleusercontent.com',
  tokenNameInLocalStorage: 'token'

};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
