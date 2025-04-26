import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminStoreComponent } from './admin-store.component';

describe('AdminStoreComponent', () => {
  let component: AdminStoreComponent;
  let fixture: ComponentFixture<AdminStoreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminStoreComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminStoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
