import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReadOrdersLambda {
    final ObjectMapper objectMapper = new ObjectMapper();

    public APIGatewayProxyResponseEvent getOrders(APIGatewayProxyRequestEvent requestEvent) throws JsonProcessingException {
        Order ssaddasd = new Order(123, "ssaddasd", 100);
        return new APIGatewayProxyResponseEvent().withBody(objectMapper.writeValueAsString(ssaddasd));
    }

}