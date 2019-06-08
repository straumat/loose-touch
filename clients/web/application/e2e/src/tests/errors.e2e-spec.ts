import {browser, by, element} from 'protractor';
import {ErrorPage} from '../page-objects/error.po';

/**
 * E2E tests for the error page.
 */
describe('E2E tests for the error page', () => {

  let errorPage: ErrorPage;

  // ===================================================================================================================
  beforeEach(() => {
    errorPage = new ErrorPage();
  });

  // ===================================================================================================================
  it('Accessing error page', () => {
    errorPage.navigateTo();
    // Check that the page displayed is the good one and no error message.
    expect(errorPage.getTitle()).toEqual('An error occurred');
  });

  // ===================================================================================================================
  it('Unauthorized access (401)', () => {
    // TODO Implement this test.
  });

  // ===================================================================================================================
  it('Accessing a non existing page (404)', () => {
    // Accessing to a non existing page.
    browser.get('/nonExistingPage.html');

    // Check that the page displayed is the good one and the error message is the good one.
    expect(browser.getTitle()).toEqual('An error occurred');
    expect(element(by.tagName('mat-card-subtitle')).getText()).toEqual('Page not found');
  });

  // ===================================================================================================================
  it('Internal server error (500)', () => {
    // TODO Implement this test.
  });

});
