import {browser} from 'protractor';

// tslint:disable-next-line:max-line-length
const validToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjMjUyOWI1OC1hYzVhLTRiYTMtODA2Yy02NjlmYzkwZmQ4YTMiLCJpYXQiOjE1NTQzODgzNzksImV4cCI6Mjc0NzQzODgzODF9.z_pCx8hxTEc5J1PIOAzW-NEUKZE6LD6AE-pG_v17QVo';
// tslint:disable-next-line:max-line-length
const invalidToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2OWU3MGQzYi05YTQwLTRlMmMtYWNkZS04NWQyNWYxODc3ZjAiLCJpYXQiOjE1NTQ4NDI2MjQsImV4cCI6MTU1NzQzNDYyNH0.mqbbpGYDTuXiHmmXhBdAN737mqztq-esJSAC-ixdt2E';
// tslint:disable-next-line:max-line-length
const expiredToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2OGQwY2FiOC00YzVhLTRlNDctOGZlZS04ZWNmZjEyMjIzYTciLCJpYXQiOjE1NTQzOTI2NDMsImV4cCI6MTU1NDM5MjY0M30.gyj3mErABtm8jyaoROPzCiaFW8tQ2HCkhSP0vb8BVkw';

function setToken(token: string) {
  browser.executeScript('window.localStorage.setItem(\'token\', \'' + token + '\');');
}

export function clearStorage() {
  browser.executeScript('window.localStorage.clear();');
}

export function setValidToken() {
  setToken(validToken);
}

export function setInvalidToken() {
  setToken(invalidToken);
}

export function setExpiredToken() {
  setToken(expiredToken);
}
