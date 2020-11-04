import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GetMyPComponent } from './get-my-p.component';

describe('GetMyPComponent', () => {
  let component: GetMyPComponent;
  let fixture: ComponentFixture<GetMyPComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GetMyPComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GetMyPComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
