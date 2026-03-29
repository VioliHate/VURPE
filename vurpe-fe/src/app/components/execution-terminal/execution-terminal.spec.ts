import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExecutionTerminal } from './execution-terminal';

describe('ExecutionTerminal', () => {
  let component: ExecutionTerminal;
  let fixture: ComponentFixture<ExecutionTerminal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExecutionTerminal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExecutionTerminal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
