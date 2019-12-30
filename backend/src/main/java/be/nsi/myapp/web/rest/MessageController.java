package be.nsi.myapp.web.rest;

import be.nsi.myapp.domain.Message;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.stream.Stream;

@RestController
@RequestMapping("messages")
public class MessageController {

    @GetMapping("/test")
    public Message test() {
        return new Message("Test");
    }

    @CrossOrigin
    @GetMapping("/mono")
    public Mono<Message> lastMessage() {
        return Mono.just(new Message("Fake mono"));
    }

    @CrossOrigin
    @GetMapping(value = "/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> allMessage() {
        Flux<Message> eventFlux = Flux.fromStream(
            Stream.generate(()-> {
                String content = "Message " + System.currentTimeMillis();
                System.out.println(content);
                return new Message(content);
            })
        );

        Flux<Long> durationFlux = Flux.interval(Duration.ofSeconds(1));

        return Flux.zip(eventFlux, durationFlux).map(Tuple2::getT1);

//        return Flux.just(new Message("Fake flux 1"), new Message("Fake flux 2"));
    }
}
