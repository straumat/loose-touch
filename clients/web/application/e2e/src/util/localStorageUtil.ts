import {browser} from 'protractor';

// tslint:disable-next-line:max-line-length
const invalidToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2OWU3MGQzYi05YTQwLTRlMmMtYWNkZS04NWQyNWYxODc3ZjAiLCJpYXQiOjE1NTQ4NDI2MjQsImV4cCI6MTU1NzQzNDYyNH0.mqbbpGYDTuXiHmmXhBdAN737mqztq-esJSAC-ixdt2E';
// tslint:disable-next-line:max-line-length
const expiredToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2OGQwY2FiOC00YzVhLTRlNDctOGZlZS04ZWNmZjEyMjIzYTciLCJpYXQiOjE1NTQzOTI2NDMsImV4cCI6MTU1NDM5MjY0M30.gyj3mErABtm8jyaoROPzCiaFW8tQ2HCkhSP0vb8BVkw';

export function getToken() {
  browser.executeScript('window.localStorage.getItem(\'token\');');
}

export function setToken(token: string) {
  browser.executeScript('window.localStorage.setItem(\'token\', \'' + token + '\');');
}

export function clearStorage() {
  browser.executeScript('window.localStorage.clear();');
}

export function setInvalidToken() {
  setToken(invalidToken);
}

export function setExpiredToken() {
  setToken(expiredToken);
}
