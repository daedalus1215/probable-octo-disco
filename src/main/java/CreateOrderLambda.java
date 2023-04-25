import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateOrderLambda {
    public APIGatewayProxyResponseEvent createOrder(APIGatewayProxyRequestEvent requestEvent) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Order order = objectMapper.readValue(requestEvent.getBody(), Order.class);
        return new APIGatewayProxyResponseEvent().withBody(String.format("Order ID: " + order.getId()));
    }

}