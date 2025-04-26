import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminStoreDetailComponent } from './admin-store-detail.component';

describe('AdminStoreDetailComponent', () => {
  let component: AdminStoreDetailComponent;
  let fixture: ComponentFixture<AdminStoreDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminStoreDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminStoreDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
