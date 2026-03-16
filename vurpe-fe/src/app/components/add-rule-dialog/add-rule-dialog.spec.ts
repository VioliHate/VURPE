import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRuleDialog } from './add-rule-dialog';

describe('AddRuleDialog', () => {
  let component: AddRuleDialog;
  let fixture: ComponentFixture<AddRuleDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddRuleDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddRuleDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
