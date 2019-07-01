import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Title} from '@angular/platform-browser';
import {ContactAPIService, ContactDTO} from 'angular-loose-touch-api';
import {Router} from '@angular/router';

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

  /**
   * Error messages.
   */
  contactValidationMessages = {
    email: [
      {type: 'required', message: 'An email is required'},
      {type: 'email', message: 'A valid email is required'},
    ],
    contactRecurrenceValue: [
      {type: 'required', message: 'This field is required'},
      {type: 'pattern', message: 'This field must be a number between 1 and 100'},
      {type: 'min', message: 'This field must be a number between 1 and 100'},
      {type: 'max', message: 'This field must be a number between 1 and 100'}
    ]
  };

  /**
   * Recurrence type for the form.
   */
  contactRecurrenceTypes: ContactRecurrenceType[] = [
    {value: 'DAY', viewValue: 'Days'},
    {value: 'MONTH', viewValue: 'Months'},
    {value: 'YEAR', viewValue: 'Years'}
  ];

  /**
   * Contact form.
   */
  contactForm: FormGroup;

  constructor(private titleService: Title,
              public router: Router,
              private formBuilder: FormBuilder,
              private contactAPIService: ContactAPIService) {
    this.titleService.setTitle('Add a new contact');
    this.contactForm = this.createFormGroupWithBuilder(formBuilder);
  }

  ngOnInit() {
  }

  /**
   * On save.
   */
  onSubmit() {
    // Make sure to create a deep copy of the form-model
    const contactDTO: ContactDTO = Object.assign({}, this.contactForm.value);
    // Send the data to the server.
    this.contactAPIService.createContact(contactDTO).subscribe(result => {
        this.router.navigate(['/dashboard']);
      },
      error => {
        // TODO Treat server errors.
      });
  }

  /**
   * Cancel form.
   */
  cancel() {
    this.router.navigate(['/dashboard']);
  }

  /**
   * Creates the form.
   * @param formBuilder form builder.
   */
  createFormGroupWithBuilder(formBuilder: FormBuilder) {
    return formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      firstName: '',
      lastName: '',
      notes: '',
      contactRecurrenceValue: [3, [Validators.required, Validators.pattern('^[0-9]*$'), Validators.min(1), Validators.max(1000)]],
      contactRecurrenceType: 'MONTH',
    });
  }

}
