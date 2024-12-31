import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentHelperComponent } from './comment-helper.component';

describe('CommentHelperComponent', () => {
  let component: CommentHelperComponent;
  let fixture: ComponentFixture<CommentHelperComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommentHelperComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CommentHelperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
