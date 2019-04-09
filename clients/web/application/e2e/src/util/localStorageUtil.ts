'use strict';

import {browser} from 'protractor';

/**
 * Class used to manipulate the token local storage.
 */
export default class LocalTokenStorage {

  static validToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkM2I1MDRlMS1mMWJmLTRmNGItYWM5My1hMDZiYjgzNGY2MGYiLCJpYXQiOjE1NTQ4NDI0MzgsImV4cCI6MTU1NzQzNDQzOH0.54vBBxZLKh8ONAIHkA-KL8hI2y0gXidoV3waMDX34G0';
  static invalidToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2OWU3MGQzYi05YTQwLTRlMmMtYWNkZS04NWQyNWYxODc3ZjAiLCJpYXQiOjE1NTQ4NDI2MjQsImV4cCI6MTU1NzQzNDYyNH0.mqbbpGYDTuXiHmmXhBdAN737mqztq-esJSAC-ixdt2E';
  static expiredToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2OGQwY2FiOC00YzVhLTRlNDctOGZlZS04ZWNmZjEyMjIzYTciLCJpYXQiOjE1NTQzOTI2NDMsImV4cCI6MTU1NDM5MjY0M30.gyj3mErABtm8jyaoROPzCiaFW8tQ2HCkhSP0vb8BVkw';

  static getToken() {
    browser.executeScript('return window.localStorage.getItem(\'token\');').then(function(token) {
      this.console.log('Token : ' + token);
      return token;
    });
  }

  static setToken(token: string) {
    browser.executeScript('window.localStorage.setItem(\'token\', \'' + token + '\');');
  }

  static setValidToken() {
    this.setToken(this.validToken);
  }

  static setInvalidToken() {
    this.setToken(this.invalidToken);
  }

  static setExpiredToken() {
    this.setToken(this.expiredToken);
  }

  static clear() {
    browser.executeScript('return window.localStorage.clear();');
  }

}
