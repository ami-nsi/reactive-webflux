import { Injectable, NgZone } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject, Subscriber, Subject, Observer } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class MessageService {
  private serviceUrl = '//localhost:8080/messages';

  constructor(private http: HttpClient) {}

  create(message): Observable<any> {
    return this.http.post(this.serviceUrl, message);
  }

  private getFluxObervable<T>(url: string): Observable<T> {
    return new Observable(subscriber => {
      // Create websoquet from EventSource
      const eventSource = new EventSource(url, { withCredentials: false });

      // Trigger behavioural subject on message received
      eventSource.onmessage = sse => {
        const event: any = JSON.parse(sse.data);
        // Requiered zone to trigger angular view change
        // this._zone.run(() => this._events.next(event));
        subscriber.next(event);
      };

      // Trigger error on error
      eventSource.onerror = err => {
        subscriber.error(err);
      };

      // Override subscribe method to close the event source too
      const originalUsub = subscriber.unsubscribe;
      subscriber.unsubscribe = () => {
        eventSource.close();
        originalUsub.call(subscriber);
      };
    });
  }

  private createWs<T>(url: string): Subject<T> {
    const ws = new WebSocket(url);

    const observable = Observable.create((obs: Observer<T>) => {
      ws.onmessage = obs.next.bind(obs);
      ws.onerror = obs.error.bind(obs);
      ws.onclose = obs.complete.bind(obs);

      return ws.close.bind(ws);
    });

    const observer = {
      next: (data: object) => {
        if (ws.readyState === WebSocket.OPEN) {
          ws.send(JSON.stringify(data));
        }
      },
    };

    return Subject.create(observer, observable);
  }

  getLastMessage(): Observable<any> {
    // return this.createWs(this.serviceUrl + '/flux');
    return this.getFluxObervable(this.serviceUrl + '/flux');
  }
}
