import { TestBed } from '@angular/core/testing';

import { RulesSerivce } from './rules-serivce';

describe('RulesSerivce', () => {
  let service: RulesSerivce;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RulesSerivce);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
