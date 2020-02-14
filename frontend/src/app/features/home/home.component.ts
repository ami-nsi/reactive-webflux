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

  constructor(private messageService: MessageService, private zone: NgZone) {}

  ngOnInit() {
    this.allMessages = [];
    this.listenLastMessage();
  }

  listenLastMessage() {
    this.sub = this.messageService.getLastMessage().subscribe(
      v => {
        // Use zone to update the value
        // Otherwise the detection is not triggered
        this.zone.run(() => {
          this.lastMessage = v;
          this.allMessages.push(v);
        });
      },
      e => (this.lastMessage = e),
      () => console.log('On complete')
    );
  }

  unsub() {
    this.sub && this.sub.unsubscribe();
  }

  create(content: string) {
    const message = { content };
    this.messageService.create(message).subscribe(m => console.log('Created', m));
  }
}
