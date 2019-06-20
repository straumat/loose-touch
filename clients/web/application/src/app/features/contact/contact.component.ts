import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {ContactDTO} from 'angular-loose-touch-api';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit {

  contactRecurrenceTypes = ['Day', 'Month', 'Year'];

  contactForm: FormGroup;

  constructor(private titleService: Title,
              private formBuilder: FormBuilder) {
    this.titleService.setTitle('Add a new contact');
    this.contactForm = this.createFormGroupWithBuilder(formBuilder);
  }

  ngOnInit() {
  }

  onSubmit() {
    // Make sure to create a deep copy of the form-model
    const result: ContactDTO = Object.assign({}, this.contactForm.value);
    switch (result.contactRecurrenceType) {
      case 'Day': {
        result.contactRecurrenceType = 'DAY';
        break;
      }
      case 'Month': {
        result.contactRecurrenceType = 'MONTH';
        break;
      }
      case 'Year': {
        result.contactRecurrenceType = 'YEAR';
        break;
      }
      default: {
        result.contactRecurrenceType = 'MONTH';
        break;
      }
    }

    // Do useful stuff with the gathered data
    console.log(result);
  }

  createFormGroupWithBuilder(formBuilder: FormBuilder) {
    return formBuilder.group({
      email: '',
      firstName: '',
      lastName: '',
      notes: '',
      contactRecurrenceValue: 3,
      contactRecurrenceType: 'Month',
    });
  }

}
