import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FatherManager } from './father-manager';

describe('FatherManager', () => {
  let component: FatherManager;
  let fixture: ComponentFixture<FatherManager>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FatherManager],
    }).compileComponents();

    fixture = TestBed.createComponent(FatherManager);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
