import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GetFreePComponent } from './get-free-p.component';

describe('GetFreePComponent', () => {
  let component: GetFreePComponent;
  let fixture: ComponentFixture<GetFreePComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GetFreePComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GetFreePComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
