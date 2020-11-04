import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PWaitingComponent } from './p-waiting.component';

describe('PWaitingComponent', () => {
  let component: PWaitingComponent;
  let fixture: ComponentFixture<PWaitingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PWaitingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PWaitingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
