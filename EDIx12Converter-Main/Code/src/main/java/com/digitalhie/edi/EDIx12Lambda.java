package com.digitalhie.edi;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;

/**
 *
 * This class implements the RequestHandler interface to handle Lambda function invocations.
 * It processes incoming API Gateway proxy requests containing JSON payloads, performs EDI conversion,
 * and generates API Gateway proxy responses with JSON payloads.
 * 
 * The incoming API Gateway proxy requests a JSON body containing
 * a "title" and "value" field. The "value" field is expected to contain a base64-encoded EDI message.
 * 
 * Upon receiving a request, the handleRequest method decodes the base64-encoded EDI message, performs
 * EDI conversion using an instance of EdiSegmentProcessor, and encodes the resulting EDI response as
 * base64. The response is then wrapped in a JSON object and returned in the API Gateway proxy response.
 * 
 * If any error occurs during request processing, an error response with the appropriate status code and
 * error message is generated and returned.
 * 
 * @author namrathag
 * @version 1.0
 */

public class EDIx12Lambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    
	// EdiSegmentProcessor Instance for each EDI Conversion
    private final EdiSegmentProcessor segmentProcessor = new EdiSegmentProcessor();

    /**
     * Handles incoming requests and generates responses.
     * 
     * @param event The APIGatewayProxyRequestEvent containing the request payload.
     * @param context The Lambda Context object.
     * @return An APIGatewayProxyResponseEvent containing the response payload.
     */
    
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> requestBody = objectMapper.readValue(event.getBody(), new TypeReference<Map<String,Object>>(){});

            String title = (String) requestBody.get("title");
            String value = (String) requestBody.get("value");
            String decodedString = new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);

            String ediResponse = segmentProcessor.convertJsonToEdi(title, decodedString);

            String encodedResponse = Base64.getEncoder().encodeToString(ediResponse.getBytes(StandardCharsets.UTF_8));
            
            // Create a JSON object with Key-Value pair
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put(title, encodedResponse);

            // Encode the JSON object as a string
            String jsonResponse = objectMapper.writeValueAsString(responseBody);

            // Set the response properties
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            response.setStatusCode(200);
            response.setHeaders(headers);
            response.setBody(jsonResponse);
        } catch (Exception e) {
            context.getLogger().log("Error processing request: {}" + e.getMessage());

            return createErrorResponse(response, 500, "Error processing request");
        }
        return response;
    }
    
    /**
     * Error Handling
     * 
     * @param response The APIGatewayProxyResponseEvent object to populate with error details.
     * @param statusCode: The HTTP status code of the error response.
     * @param errorMessage: The error message to include in the response body.
     * @return An APIGatewayProxyResponseEvent containing the details of the error response.
     */

    private APIGatewayProxyResponseEvent createErrorResponse(APIGatewayProxyResponseEvent response, int statusCode, String errorMessage) {
        response.setStatusCode(statusCode);
        response.setBody(errorMessage);
        return response;
    }
}
