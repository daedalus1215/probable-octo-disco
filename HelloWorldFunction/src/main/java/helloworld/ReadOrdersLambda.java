package helloworld;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ReadOrdersLambda {
    final ObjectMapper objectMapper = new ObjectMapper();

    public APIGatewayProxyResponseEvent getOrders(APIGatewayProxyRequestEvent requestEvent) throws JsonProcessingException {
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        ScanResult scanResult = amazonDynamoDB.scan(new ScanRequest().withTableName(System.getenv("ORDERS_TABLE")));
        List<Order> orders = scanResult.getItems().stream()
                .map(item -> new Order(
                        Integer.parseInt(item.get("id").getN()),
                        item.get("itemName").getS(),
                        Integer.parseInt(item.get("quantity").getN())))
                .collect(Collectors.toList());

        return new APIGatewayProxyResponseEvent().withBody(objectMapper.writeValueAsString(orders));
    }

}