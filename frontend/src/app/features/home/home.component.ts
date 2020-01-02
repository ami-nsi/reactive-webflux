import { Component, OnInit, NgZone } from '@angular/core';
import { MessageService } from '@app/services/message.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  public lastMessage: any;
  public allMessages: any;

  public sub: Subscription;

  constructor(private messageService: MessageService, private _zone: NgZone) {}

  ngOnInit() {}

  callMono() {
    this.messageService.getLastMessage().subscribe(
      v => {
        this._zone.run(() => (this.lastMessage = v));
      },
      e => console.error('Error', e)
    );
  }

  callFlux() {
    this.sub = this.messageService.getAllMessages().subscribe(
      v => {
        // Use zone to update the value
        // Otherwise the detection is not triggered
        this._zone.run(() => (this.allMessages = v));
      },
      e => console.error('Error', e)
    );
  }

  unsub() {
    this.sub && this.sub.unsubscribe();
  }
}
