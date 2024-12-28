import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostFormRequestComponent } from './post-form-request.component';

describe('PostFormRequestComponent', () => {
  let component: PostFormRequestComponent;
  let fixture: ComponentFixture<PostFormRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PostFormRequestComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PostFormRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
