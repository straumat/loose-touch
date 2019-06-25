import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ContactComponent} from './contact.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {CustomMaterialModule} from '../../core/material.module';
import {ApiModule, ContactAPIService} from 'angular-loose-touch-api';
import {apiConfigFactory} from '../../app.module';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('ContactComponent', () => {
  let component: ContactComponent;
  let fixture: ComponentFixture<ContactComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ContactComponent],
      imports: [
        ApiModule.forRoot(apiConfigFactory),
        HttpClientTestingModule,
        CustomMaterialModule,
        ReactiveFormsModule,
        BrowserAnimationsModule,
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContactComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should manage form validation on client side', () => {
    // Form fields.
    fixture.detectChanges();
    const email = component.contactForm.controls.email;
    const contactRecurrenceValue = component.contactForm.controls.contactRecurrenceValue;

    // The form should not be valid.
    component.contactForm.updateValueAndValidity();
    expect(component.contactForm.valid).toBeFalsy();
    expect(email.hasError('required')).toBeTruthy();
    expect(email.hasError('email')).toBeFalsy();

    // Setting a wrong email.
    email.setValue('test');
    component.contactForm.updateValueAndValidity();
    expect(component.contactForm.valid).toBeFalsy();
    expect(email.hasError('required')).toBeFalsy();
    expect(email.hasError('email')).toBeTruthy();

    // Setting the right email - form should work.
    email.setValue('test@test.com');
    component.contactForm.updateValueAndValidity();
    expect(component.contactForm.valid).toBeTruthy();
    expect(email.hasError('required')).toBeFalsy();
    expect(email.hasError('email')).toBeFalsy();

    // Setting wrong values for contactRecurrenceValue.
    // Required value.
    contactRecurrenceValue.setValue('');
    component.contactForm.updateValueAndValidity();
    expect(component.contactForm.valid).toBeFalsy();
    expect(contactRecurrenceValue.hasError('required')).toBeTruthy();
    expect(contactRecurrenceValue.hasError('pattern')).toBeFalsy();
    expect(contactRecurrenceValue.hasError('min')).toBeFalsy();
    expect(contactRecurrenceValue.hasError('max')).toBeFalsy();
    // Pattern.
    contactRecurrenceValue.setValue('zzz');
    component.contactForm.updateValueAndValidity();
    expect(component.contactForm.valid).toBeFalsy();
    expect(contactRecurrenceValue.hasError('required')).toBeFalsy();
    expect(contactRecurrenceValue.hasError('pattern')).toBeTruthy();
    expect(contactRecurrenceValue.hasError('min')).toBeFalsy();
    expect(contactRecurrenceValue.hasError('max')).toBeFalsy();
    // < 1.
    contactRecurrenceValue.setValue(0);
    component.contactForm.updateValueAndValidity();
    expect(component.contactForm.valid).toBeFalsy();
    expect(contactRecurrenceValue.hasError('required')).toBeFalsy();
    expect(contactRecurrenceValue.hasError('pattern')).toBeFalsy();
    expect(contactRecurrenceValue.hasError('min')).toBeTruthy();
    expect(contactRecurrenceValue.hasError('max')).toBeFalsy();
    // > 1000.
    contactRecurrenceValue.setValue(1001);
    component.contactForm.updateValueAndValidity();
    expect(component.contactForm.valid).toBeFalsy();
    expect(contactRecurrenceValue.hasError('required')).toBeFalsy();
    expect(contactRecurrenceValue.hasError('pattern')).toBeFalsy();
    expect(contactRecurrenceValue.hasError('min')).toBeFalsy();
    expect(contactRecurrenceValue.hasError('max')).toBeTruthy();
  });

  it('should make a rest call with good data', () => {
    // Form fields.
    fixture.detectChanges();
    const email = component.contactForm.controls.email;
    const firstName = component.contactForm.controls.firstName;
    const lastName = component.contactForm.controls.lastName;
    const notes = component.contactForm.controls.notes;
    const contactRecurrenceValue = component.contactForm.controls.contactRecurrenceValue;
    const contactRecurrenceType = component.contactForm.controls.contactRecurrenceType;

    // Set data.
    email.setValue('test@test.fr');
    firstName.setValue('Yves');
    lastName.setValue('Dupont');
    notes.setValue('My notes');
    contactRecurrenceType.setValue('DAY');
    contactRecurrenceValue.setValue(9);

    // Check that the form is ok.
    component.contactForm.updateValueAndValidity();
    expect(component.contactForm.valid).toBeTruthy();

    //
  });

});
