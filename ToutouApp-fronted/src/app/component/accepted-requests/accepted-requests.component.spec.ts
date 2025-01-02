import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AcceptedRequestsComponent } from './accepted-requests.component';

describe('AcceptedRequestsComponent', () => {
  let component: AcceptedRequestsComponent;
  let fixture: ComponentFixture<AcceptedRequestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AcceptedRequestsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AcceptedRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
