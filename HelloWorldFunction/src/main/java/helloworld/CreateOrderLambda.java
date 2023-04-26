package helloworld;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateOrderLambda {
    public APIGatewayProxyResponseEvent createOrder(APIGatewayProxyRequestEvent requestEvent) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Order order = objectMapper.readValue(requestEvent.getBody(), Order.class);

        final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
        Table ordersTable = dynamoDB.getTable(System.getenv("ORDERS_TABLE"));

        PutItemOutcome putItemOutcome = ordersTable.putItem(new Item()
                .withPrimaryKey("id", order.id)
                .withString("itemName", order.itemName)
                .withInt("quantity", order.quantity));

        return new APIGatewayProxyResponseEvent().withBody(String.format("ordersapi.Order ID: %s", order.getId()));
    }

}