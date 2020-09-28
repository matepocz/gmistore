import { TestBed } from '@angular/core/testing';

import { LiveDataSubjectService } from './live-data-subject.service';

describe('LiveDataSubjectService', () => {
  let service: LiveDataSubjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LiveDataSubjectService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
