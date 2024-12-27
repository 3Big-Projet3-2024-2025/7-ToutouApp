import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HubForRequestsComponent } from './hub-for-requests.component';

describe('HubForRequestsComponent', () => {
  let component: HubForRequestsComponent;
  let fixture: ComponentFixture<HubForRequestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HubForRequestsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HubForRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
