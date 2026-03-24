import { TestBed } from '@angular/core/testing';

import { DataRecord } from './data-record';

describe('DataRecord', () => {
  let service: DataRecord;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DataRecord);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
