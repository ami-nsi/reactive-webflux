import { Component, OnInit } from '@angular/core';

declare const APP_MANIFEST: any;

@Component({
  selector: 'app-footer',
  template: `
    <div class="footer">
      <div class="card clearfix">
        <span class="footer-text-left">
          Version : {{ manifest.version }} - Build date : {{ manifest.buildDate | date: 'dd-MM-y' }}
        </span>
        <span class="footer-text-right">
          <span class="material-icons ui-icon-copyright"></span>
          <span>All Rights Reserved</span>
        </span>
      </div>
    </div>
  `,
})
export class LayoutFooterComponent {
  public manifest: any = APP_MANIFEST;
}
