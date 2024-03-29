package helloworld;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandler {
    public void handler(SNSEvent event) {
        final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);
        event.getRecords()
                .forEach(record -> logger.info(String.format("Dead Letter Queue Event %s", record.toString())));
    }
}
