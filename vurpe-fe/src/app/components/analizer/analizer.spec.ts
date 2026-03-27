import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Analizer } from './analizer';

describe('Analizer', () => {
  let component: Analizer;
  let fixture: ComponentFixture<Analizer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Analizer]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Analizer);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
