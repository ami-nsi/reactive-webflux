package be.nsi.myapp.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import reactor.core.publisher.EmitterProcessor;

public class FakeMessageBuilder extends Thread {

    private Log log = LogFactory.getLog(this.getClass());

    private EmitterProcessor<Message> emitter;

    public FakeMessageBuilder(EmitterProcessor<Message> emitter) {
        this.emitter = emitter;
    }

    @Override
    public void run() {
        int count = 0;
        while (true) {
            this.log.info("Creating new message: "+count);
            this.emitter.onNext(new Message("Loop message " + count++));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.emitter.onError(e);
            }
        }

    }
}
