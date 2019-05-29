import {browser, by, element} from 'protractor';

/**
 * E2E tests for the 404 error page.
 */
describe('E2E tests for the 404 error page', () => {

  // ===================================================================================================================
  it('Accessing a non existing page', () => {
    // Accessing to a non existing page.
    browser.get('/nonExistingPage');

    // Check that the page displayed is the good one and the error message is the good one.
    expect(browser.getTitle()).toEqual('An error occurred !');
    expect(element(by.tagName('mat-card-subtitle')).getText()).toEqual('Page not found');
  });

});
