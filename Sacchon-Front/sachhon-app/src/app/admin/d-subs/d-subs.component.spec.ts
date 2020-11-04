import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DSubsComponent } from './d-subs.component';

describe('DSubsComponent', () => {
  let component: DSubsComponent;
  let fixture: ComponentFixture<DSubsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DSubsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DSubsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
