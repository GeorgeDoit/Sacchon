import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PSubsComponent } from './p-subs.component';

describe('PSubsComponent', () => {
  let component: PSubsComponent;
  let fixture: ComponentFixture<PSubsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PSubsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PSubsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
