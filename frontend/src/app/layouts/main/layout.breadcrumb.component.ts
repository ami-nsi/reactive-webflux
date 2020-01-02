import { Component, OnDestroy } from '@angular/core';
import { BreadcrumbService } from '@app/services/breadcrumb.service';
import { MenuItem } from 'primeng/components/common/menuitem';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-breadcrumb',
  templateUrl: './layout.breadcrumb.component.html',
})
export class LayoutBreadcrumbComponent implements OnDestroy {
  subscription: Subscription;

  items: MenuItem[];

  constructor(public breadcrumbService: BreadcrumbService) {
    this.subscription = breadcrumbService.itemsHandler.subscribe(response => {
      this.items = response;
    });
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
