import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {ContactDTO} from 'angular-loose-touch-api';
import {Title} from '@angular/platform-browser';

class ContactRecurrenceType {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit {

  contactRecurrenceTypes: ContactRecurrenceType[] = [
    {value: 'DAY', viewValue: 'Day'},
    {value: 'MONTH', viewValue: 'Month'},
    {value: 'YEAR', viewValue: 'Year'}
  ];

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
      contactRecurrenceType: 'MONTH',
    });
  }

}
