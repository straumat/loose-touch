import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DummyComponent } from './dummy.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';

describe('DummyComponent', () => {
  let component: DummyComponent;
  let fixture: ComponentFixture<DummyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DummyComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DummyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
