import {browser, by, element, protractor} from 'protractor';

/**
 * Contact form page.
 */
export class ContactFormPage {

  navigateTo() {
    return browser.get('/contacts/new');
  }

  getTitle() {
    return browser.getTitle();
  }

  isSubmitButtonActive() {
    return element(by.id('button-save')).isEnabled();
  }

  setEmail(value: string) {
    this.setInputValue('input-email', value);
  }

/*  setContactRecurrenceValue(value : string) {
    this.setInputValue('input-contact-recurrence-value', value);
  }*/

  submit() {
    element(by.id('button-save')).click();
  }

  async setInputValue(field: string, value: string) {
    const inputElement = element(by.id(field));
    await inputElement.clear();
    inputElement.sendKeys(value);

  }

}
