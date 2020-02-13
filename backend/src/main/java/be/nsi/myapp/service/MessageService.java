package be.nsi.myapp.service;

import be.nsi.myapp.domain.FakeMessageBuilder;
import be.nsi.myapp.domain.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private Log log = LogFactory.getLog(this.getClass());

    private EmitterProcessor<Message> lastMessageEmitter;

    private List<Message> allMessages = new ArrayList<>();

    private Thread loopThread;
    private int cnt = 0;

    public MessageService() {
        this.lastMessageEmitter = EmitterProcessor.create();
        // Create a loop to fake create a message every second
        this.loopThread = new FakeMessageBuilder(this.lastMessageEmitter);

        this.lastMessageEmitter.subscribe(m -> {
            this.log.info("Received : " + m.getContent());
        });
//        this.loopThread.start();
    }

    @Scheduled(fixedRate = 1000)
    public void infinitLoop() {
        Message m = new Message("Infinite loop " + this.cnt++);
        this.log.info(m.getContent());
        this.lastMessageEmitter.onNext(m);
    }

    @Async
    public Flux<Message> getLastMessage$() {
        return this.lastMessageEmitter;

//        return this.lastMessageEmitter.map(m -> {
//            this.log.info("New message");
//            return m;
//        });


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

    public Message create(Message message) {
        this.allMessages.add(message);
        this.lastMessageEmitter.onNext(message);
        return message;
    }
}
