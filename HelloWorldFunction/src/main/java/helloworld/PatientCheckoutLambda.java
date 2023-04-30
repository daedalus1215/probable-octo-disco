package helloworld;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

public class PatientCheckoutLambda {
    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    private final AmazonSNS sns = AmazonSNSClientBuilder.defaultClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handler(S3Event event, Context context) {
//        final LambdaLogger logger = context.getLogger();
        final Logger logger = LoggerFactory.getLogger(PatientCheckoutLambda.class);
        event.getRecords().forEach(record -> {
            S3ObjectInputStream s3InputStream = s3.getObject(record
                                    .getS3()
                                    .getBucket()
                                    .getName(),
                            record
                                    .getS3()
                                    .getObject()
                                    .getKey())
                    .getObjectContent();

            try {
                logger.info("Reading data from S3");
                List<PatientCheckoutEvent> patientCheckoutEvents = asList(objectMapper.readValue(s3InputStream, PatientCheckoutEvent.class));
                s3InputStream.close();
                logger.info(patientCheckoutEvents.toString());
                publishMessageToSNS(patientCheckoutEvents, logger);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void publishMessageToSNS(List<PatientCheckoutEvent> patientCheckoutEvents, Logger logger) {
        patientCheckoutEvents.forEach(patientCheckoutEvent -> {
            try {
                sns.publish(System.getenv("PATIENT_CHECKOUT_TOPIC"), objectMapper.writeValueAsString(patientCheckoutEvent));
            } catch (JsonProcessingException e) {
                final StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter));
                logger.error(stringWriter.toString());
            }
        });
    }
}
