import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FatherCharts } from './father-charts';

describe('FatherCharts', () => {
  let component: FatherCharts;
  let fixture: ComponentFixture<FatherCharts>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FatherCharts]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FatherCharts);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
