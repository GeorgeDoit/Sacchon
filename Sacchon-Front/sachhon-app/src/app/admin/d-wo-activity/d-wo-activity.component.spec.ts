import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DWoActivityComponent } from './d-wo-activity.component';

describe('DWoActivityComponent', () => {
  let component: DWoActivityComponent;
  let fixture: ComponentFixture<DWoActivityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DWoActivityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DWoActivityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
