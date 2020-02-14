package be.nsi.myapp.service;

import be.nsi.myapp.domain.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private Log log = LogFactory.getLog(this.getClass());

    private EmitterProcessor<Message> lastMessageEmitter;

    private List<Message> allMessages = new ArrayList<>();

    private int cnt = 0;

    public MessageService() {
        this.lastMessageEmitter = EmitterProcessor.create();

        // By creating a subscription (and never unsubscribe from it) the flux will never close.
//        this.lastMessageEmitter.subscribe(m -> this.log.info("Java sub : " + m.getContent()));
    }

    /**
     * Infinite loop to create message from java.
     */
    @Scheduled(fixedRate = 1000)
    public void infiniteLoop() {
        if (this.cnt == Integer.MAX_VALUE) {
            this.cnt = 0;
        }
        Message m = new Message("Infinite loop " + this.cnt++);
        this.lastMessageEmitter.onNext(m);
    }

    public Flux<Message> getLastMessage$() {
        // Log flux information
        this.log.info("Cancelled : " + this.lastMessageEmitter.isCancelled());
        this.log.info("Terminated : " + this.lastMessageEmitter.isTerminated());
        this.log.info("Disposed : " + this.lastMessageEmitter.isDisposed());
        this.log.info("Has error : " + this.lastMessageEmitter.hasError());

        // Init the copy to the original flux
        Flux<Message> copy = this.lastMessageEmitter;

        /*
         * Returning the emitter directly works.
         * If the client close the connection the emmiter will be "cancelled".
         * In this stage, if no other subscription exists the Flux will be closed and no other value will emit from it.
         */
        copy = this.lastMessageEmitter;

        /*
         * Map the value does not change the flux and does not return a new Flux either.
         * Closing the connection will still close the Flux.
         * Other subscription are not affected by this mapping.
         */
        copy = this.lastMessageEmitter.map(m -> {
            this.log.info("New message mapped");
            m.setContent("Mapped : " + m.getContent());
            return m;
        });
        this.log.info("Does flux.map create a new Flux? " + (copy != this.lastMessageEmitter));

        /*
         * Flux.from does not create a new flux
         */
        copy = Flux.from(this.lastMessageEmitter);
        this.log.info("Does Flux.from create a new Flux? " + (copy != this.lastMessageEmitter));


        /*
         * Try to zip the Flux with itself. It does create a new Flux entity
         * But the closing also close the original flux
         */
        copy = Flux.zip(this.lastMessageEmitter, this.lastMessageEmitter).map(Tuple2::getT1);
        this.log.info("Does Flux.zip create a new Flux? " + (copy != this.lastMessageEmitter));

        /*
         * Create a transfer flux. But (currently) the new flux in rever unsubscribed tho don't do that.
         */
        copy = this.copy(this.lastMessageEmitter);
        this.log.info("Does the copy method create a new Flux? " + (copy != this.lastMessageEmitter));

        return copy;


//        return this.lastMessageFlux;
//        // Create the message flux
//        Flux<Message> eventFlux = Flux.fromStream(
//            // Generate a new message containing the current timestamp
//            Stream.generate(()-> new Message("Message " + System.currentTimeMillis()))
//        );
//
//        // Create a 1sec interval
//        Flux<Long> durationFlux = Flux.interval(Duration.ofSeconds(1));
//
//        // Synchronise the interval and the message generator
//        return Flux.zip(lastMessageFlux, durationFlux).map(Tuple2::getT1);
    }

    private <T> Flux<T> copy(Flux<T> original) {
        return Flux.create(c -> {
            original.subscribe(v -> {
                this.log.info("Transfert a new value");
                c.next(v);
            }, e -> {
                this.log.info("Transfert an error");
                c.error(e);
            }, () -> {
                this.log.info("Transfert the complete");
                c.complete();
            });
        });
    }

    public Message create(Message message) {
        this.allMessages.add(message);
        this.lastMessageEmitter.onNext(message);
        return message;
    }
}
