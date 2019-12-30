import {NgModule} from '@angular/core';

import {SharedModule} from '@app/shared/shared.module';
import {LayoutBreadcrumbComponent} from './layout.breadcrumb.component';
import {LayoutComponent} from './layout.component';
import {LayoutFooterComponent} from './layout.footer.component';
import {LayoutMenuComponent, LayoutSubMenuComponent} from './layout.menu.component';
import {LayoutInlineProfileComponent} from './layout.profile.component';
import {LayoutRightpanelComponent} from './layout.rightpanel.component';
import {LayoutTopbarComponent} from './layout.topbar.component';

@NgModule({
  declarations: [
    LayoutComponent,
    LayoutTopbarComponent,
    LayoutFooterComponent,
    LayoutInlineProfileComponent,
    LayoutMenuComponent,
    LayoutSubMenuComponent,
    LayoutRightpanelComponent,
    LayoutBreadcrumbComponent
  ],
  imports: [SharedModule],
  exports: [LayoutComponent]
})
export class MainLayoutModule {}
