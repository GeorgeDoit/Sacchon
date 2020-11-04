import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PWoActivityComponent } from './p-wo-activity.component';

describe('PWoActivityComponent', () => {
  let component: PWoActivityComponent;
  let fixture: ComponentFixture<PWoActivityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PWoActivityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PWoActivityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
