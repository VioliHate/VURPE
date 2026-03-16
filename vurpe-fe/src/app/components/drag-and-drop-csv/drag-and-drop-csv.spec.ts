import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DragAndDropCsv } from './drag-and-drop-csv';

describe('DragAndDropCsv', () => {
  let component: DragAndDropCsv;
  let fixture: ComponentFixture<DragAndDropCsv>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DragAndDropCsv]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DragAndDropCsv);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
