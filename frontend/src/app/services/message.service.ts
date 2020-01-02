import { Injectable, NgZone } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject, Subscriber } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class MessageService {
  private serviceUrl = '//localhost:8080/messages';

  private _eventSource: EventSource;
  private _events: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(private http: HttpClient) {}

  getLastMessage(): Observable<any> {
    return this.http.get(this.serviceUrl + '/mono').pipe(tap(r => console.log('Got a result from backend', r)));
  }

  getAllMessages(): Observable<any> {
    return new Observable(subscriber => {
      // Create websoquet from EventSource
      const eventSource = new EventSource(this.serviceUrl + '/flux');

      // Trigger behavioural subject on message received
      eventSource.onmessage = sse => {
        const event: any = JSON.parse(sse.data);
        // Requiered zone to trigger angular view change
        // this._zone.run(() => this._events.next(event));
        subscriber.next(event);
      };

      // Trigger error on error
      eventSource.onerror = err => subscriber.error(err);

      // Override subscribe method to close the event source too
      const originalUsub = subscriber.unsubscribe;
      subscriber.unsubscribe = () => {
        eventSource.close();
        originalUsub.call(subscriber);
      };
    });
  }
}
