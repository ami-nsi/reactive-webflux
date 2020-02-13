package be.nsi.myapp.web.rest;

import be.nsi.myapp.domain.Message;
import be.nsi.myapp.service.MessageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("messages")
public class MessageController {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MessageService messageService;

    @GetMapping("/test")
    public Message test() {
        return new Message("Test");
    }

    @CrossOrigin
    @GetMapping("/mono")
    public Mono<Message> lastMessage() {
        return Mono.just(new Message("Fake mono"));
    }

    @PostMapping()
    public Message create(@RequestBody Message message) {
        return this.messageService.create(message);
    }

    @CrossOrigin
    @ResponseBody
    @GetMapping(value = "/flux-obj", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> getLastMessage$() {
        this.log.info("Request to get last message flux");
        return this.messageService.getLastMessage$();

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
//        return Flux.zip(eventFlux, durationFlux).map(Tuple2::getT1);
    }

    @CrossOrigin
    @GetMapping(value = "/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<Message>> getLastMessageEntity$() {
        this.log.info("Request to get last message flux");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "text/event-stream");
        httpHeaders.add("Cache-Control", "no-cache");
        httpHeaders.add("Connection", "keep-alive");
        return new ResponseEntity<>(this.messageService.getLastMessage$(), httpHeaders, HttpStatus.OK);

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
//        return Flux.zip(eventFlux, durationFlux).map(Tuple2::getT1);
    }
}
