package com.digitalhie.edi;


//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author namrathag
 */

public class EDIx12Lambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    //private static final Logger logger = LoggerFactory.getLogger(EDIx12Lambda.class);
    private static final String SEGMENT_DELIMITER = "~";
    private static final String ELEMENT_DELIMITER = "*";
    
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
           APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();  
           
           try {
               
               // Extract title and value from input JSON
        	   ObjectMapper objectMapper = new ObjectMapper();
               Map<String, Object> requestBody = objectMapper.readValue(event.getBody(), new TypeReference<Map<String,Object>>(){});

               String title = (String) requestBody.get("title");
               String value = (String) requestBody.get("value");
               String decodedString = new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);

               String ediResponse;
                if ("270".equals(title)) {
            	    ediResponse = convertJsonToEdi270(decodedString);
            	} else if ("837P".equals(title)) {
            	    ediResponse = convertJsonToEdi837P(decodedString);
            	} else if ("837I".equals(title)) {
            	    ediResponse = convertJsonToEdi837I(decodedString);
            	} else {
            	    throw new IllegalArgumentException("Invalid title");
            	}
            // Encode the EDIx12 response
               String encodedResponse = Base64.getEncoder().encodeToString(ediResponse.getBytes(StandardCharsets.UTF_8));

               // Create a JSON object with Key-Value pair
               Map<String, String> responseBody = new HashMap<>();
               responseBody.put(title, encodedResponse);

               // Encode the JSON object as a string
               //ObjectMapper objectMapper = new ObjectMapper();
               String jsonResponse = objectMapper.writeValueAsString(responseBody);

               // Set the response properties
               Map<String, String> headers = new HashMap<>();
               headers.put("Content-Type", "application/json");
               response.setStatusCode(200);
               response.setHeaders(headers);
               response.setBody(jsonResponse);
        } catch (Exception e) {
        	context.getLogger().log("Error processing request: {}" + e.getMessage());
//            response.setStatusCode(500);
//            response.setBody("Error processing request");
//            return response;
        	return createErrorResponse(response, 500, "Error processing request");
        }
        finally {
            // Reset the globalEdiBuilder after processing the request
            globalEdiBuilder.setLength(0);
        }
        return response;
    }
         
    private APIGatewayProxyResponseEvent createErrorResponse(APIGatewayProxyResponseEvent response, int statusCode, String errorMessage) {
        response.setStatusCode(statusCode);
        response.setBody(errorMessage);
        return response;
    }
    
    private StringBuilder globalEdiBuilder = new StringBuilder();
    //private static StringBuilder ediBuild = new StringBuilder();
    //StringBuilder globalEdiBuild = new StringBuilder();
    
    private void appendToGlobalVariable(String output) {
        // For SV5 segment
        globalEdiBuilder.append(output).append("\n");
    }
    
    private String convertJsonToEdi837P(String value) {
        // EDI 837P conversion logic using EdiConversionService
    	 StringBuilder ediBuilder = new StringBuilder();
         try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode parentNode = objectMapper.readTree(value);

            // Implement your 837P conversion logic here   
            
            // Process ISA, GS, ST, BHT, Loop1000A, Loop2000A, Loop2010AA, Loop2000B, Loop2010BA, Loop2010BB, Loop2000C, Loop2010CA, Loop2300, Loop2310A, Loop2310B, Loop2320, Loop2330B, Loop2400, SE, GE, IEA segments...
            //Process ISA Segment
            JsonNode isaNode = parentNode.path("ISA");
            if (!isaNode.isMissingNode()) {
                processEdiSegment("ISA", isaNode);
            }
            // Process GS Segment
            JsonNode gsNode = parentNode.path("GS");
            if (!gsNode.isMissingNode()) {
                processEdiSegment("GS", gsNode);
            }
            // Process ST Segment
            JsonNode stNode = parentNode.path("ST");
            if (!stNode.isMissingNode()) {
                processEdiSegment("ST", stNode);
            }
            // Process BHT Segment
            JsonNode bhtNode = parentNode.path("BHT");
            if (!bhtNode.isMissingNode()) {
                processEdiSegment("BHT", bhtNode);
            }
            // Process LOOP1000A Segment
            JsonNode loop1000ANode = parentNode.path("Loop1000A");
            if (!loop1000ANode.isMissingNode()) { // Ensure the Loop1000A node exists
                JsonNode nm1Node = loop1000ANode.path("NM1");
                if (!nm1Node.isMissingNode()) { // Check if the HL segment exists within LOOP2000B
                    processEdiSegment("NM1", nm1Node);
                    if (!loop1000ANode.isMissingNode()) { // Ensure the LOOP2000C node exists
                        JsonNode perNode = loop1000ANode.path("PER");
                        if (!perNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000B
                            processEdiSegment("PER", perNode);
                        }
                    }
                }
            }
            // Process LOOP1000B Segment
            JsonNode loop1000BNode = parentNode.path("Loop1000B");
            if (!loop1000BNode.isMissingNode()) { // Ensure the Loop1000A node exists
                JsonNode nm1Node = loop1000BNode.path("NM1");
                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2000B
                    processEdiSegment("NM1", nm1Node);
                }
            }
            // Process Loop2000A Segment
            JsonNode loop2000ANode = parentNode.path("Loop2000A");
            if (!loop2000ANode.isMissingNode()) { // Ensure the Loop2000A node exists
                // Process HL Segment within Loop2000A
                JsonNode hlNode = loop2000ANode.path("HL");
                if (!hlNode.isMissingNode()) { // Check if the HL segment exists within Loop2000A
                    processEdiSegment("HL", hlNode);

                    // Process PRV Segment within Loop2000A
                    JsonNode prvNode = loop2000ANode.path("PRV");
                    if (!prvNode.isMissingNode()) { // Check if the PRV segment exists within Loop2000A
                        processEdiSegment("PRV", prvNode);
                    }
                }
            }
            // Process Loop2010AA Segment within Loop2000A
            JsonNode loop2010AANode = parentNode.path("Loop2010AA");
            if (!loop2010AANode.isMissingNode()) { // Ensure the Loop2010AA node exists within Loop2000A
                // Process NM1 Segment within Loop2010AA
                JsonNode nm1Node = loop2010AANode.path("NM1");
                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within Loop2010AA
                    processEdiSegment("NM1", nm1Node);
                }

                // Process N3 Segment within Loop2010AA
                JsonNode n3Node = loop2010AANode.path("N3");
                if (!n3Node.isMissingNode()) { // Check if the N3 segment exists within Loop2010AA
                    processEdiSegment("N3", n3Node);
                }

                // Process N4 Segment within Loop2010AA
                JsonNode n4Node = loop2010AANode.path("N4");
                if (!n4Node.isMissingNode()) { // Check if the N4 segment exists within Loop2010AA
                    processEdiSegment("N4", n4Node);
                }

                // Process REF Segment within Loop2010AA
                JsonNode refNode = loop2010AANode.path("REF");
                if (!refNode.isMissingNode()) { // Check if the REF segment exists within Loop2010AA
                    processEdiSegment("REF", refNode);
                }
            }

            // Process LOOP2000B Segment
            JsonNode loop2000BNode = parentNode.path("Loop2000B");
            if (!loop2000BNode.isMissingNode()) { // Ensure the Loop2000B node exists
                // Process HL Segment within Loop2000B
                JsonNode hlNode = loop2000BNode.path("HL");
                if (!hlNode.isMissingNode()) { // Check if the HL segment exists within Loop2000B
                    processEdiSegment("HL", hlNode);
                }

                // Process SBR Segment within Loop2000B
                JsonNode sbrNode = loop2000BNode.path("SBR");
                if (!sbrNode.isMissingNode()) { // Check if the SBR segment exists within Loop2000B
                    processEdiSegment("SBR", sbrNode);
                }
            }
            // Process LOOP2010BA Segment
            JsonNode loop2010BANode = parentNode.path("Loop2010BA");
            if (!loop2010BANode.isMissingNode()) { // Ensure the Loop2010BA node exists
                // Process NM1 Segment within Loop2010BA
                JsonNode nm1Node = loop2010BANode.path("NM1");
                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within Loop2010BA
                    processEdiSegment("NM1", nm1Node);
                }

                // Process DMG Segment within Loop2010BA
                JsonNode dmgNode = loop2010BANode.path("DMG");
                if (!dmgNode.isMissingNode()) { // Check if the DMG segment exists within Loop2010BA
                    processEdiSegment("DMG", dmgNode);
                }
            }

            // Process LOOP2010BB Segment
            JsonNode loop2010BBNode = parentNode.path("Loop2010BB");
            if (!loop2010BBNode.isMissingNode()) { // Ensure the Loop2010BB node exists
                // Process NM1 Segment within Loop2010BB
                JsonNode nm1Node = loop2010BBNode.path("NM1");
                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within Loop2010BB
                    processEdiSegment("NM1", nm1Node);
                }
                // Process N4 Segment within Loop2010BB
                JsonNode n4Node = loop2010BBNode.path("N4");
                if (!n4Node.isMissingNode()) { // Check if the N4 segment exists within Loop2010BB
                    processEdiSegment("N4", n4Node);
                }
                // Process REF Segment within Loop2010BB
                JsonNode refNode = loop2010BBNode.path("REF");
                if (!refNode.isMissingNode()) { // Check if the REF segment exists within Loop2010BB
                    processEdiSegment("REF", refNode);
                }
            }

            // Process LOOP2000C Segment
            JsonNode loop2000CNode = parentNode.path("Loop2000C");
            if (!loop2000CNode.isMissingNode()) { // Ensure the Loop2000B node exists
                // Process HL Segment within Loop2000C
                JsonNode hlNode = loop2000CNode.path("HL");
                if (!hlNode.isMissingNode()) { // Check if the HL segment exists within Loop2000C
                    processEdiSegment("HL", hlNode);
                }

                // Process PAT Segment within Loop2000C
                JsonNode patNode = loop2000CNode.path("PAT");
                if (!patNode.isMissingNode()) { // Check if the SBR segment exists within Loop2000C
                    processEdiSegment("PAT", patNode);
                }
            }
            // Process LOOP2010CA Segment
            JsonNode loop2010CANode = parentNode.path("Loop2010CA");
            if (!loop2010CANode.isMissingNode()) { // Ensure the Loop2010CA node exists
                // Process NM1 Segment within Loop2010CA
                JsonNode nm1Node = loop2010CANode.path("NM1");
                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within Loop2010CA
                    processEdiSegment("NM1", nm1Node);
                }
                // Process N3 Segment within Loop2010CA
                JsonNode n3Node = loop2010CANode.path("N3");
                if (!n3Node.isMissingNode()) { // Check if the N3 segment exists within Loop2010CA
                    processEdiSegment("N3", n3Node);
                }

                // Process N4 Segment within Loop2010CA
                JsonNode n4Node = loop2010CANode.path("N4");
                if (!n4Node.isMissingNode()) { // Check if the N4 segment exists within Loop2010CA
                    processEdiSegment("N4", n4Node);
                }
                // Process DMG Segment within Loop2010CA
                JsonNode dmgNode = loop2010CANode.path("DMG");
                if (!dmgNode.isMissingNode()) { // Check if the DMG segment exists within Loop2010CA
                    processEdiSegment("DMG", dmgNode);
                }
            }
            // Process Loop2300 Segment
            JsonNode loop2300Node = parentNode.path("Loop2300");
            if (!loop2300Node.isMissingNode()) { // Ensure the Loop2300 node exists

                // Process CLM Segment
                JsonNode clmNode = loop2300Node.path("CLM");
                if (!clmNode.isMissingNode()) { // Check if the CLM segment exists within Loop2300
                    processEdiSegment("CLM", clmNode);
                }
                //REF
                JsonNode refNode = loop2300Node.path("REF");
                if (!refNode.isMissingNode()) { // Check if the REF segment exists within Loop2300
                    processEdiSegment("REF", refNode);
                }
                //HI
                JsonNode hiNode = loop2300Node.path("HI");
                if (!hiNode.isMissingNode()) { // Check if the HI segment exists within Loop2300
                    String hiOutput = String.format("HI*%s:%s~", hiNode.path("HI01-1").asText(), hiNode.path("HI01-2").asText());
                    //processEdiSegment("HI", hiNode);
                    appendToGlobalVariable(hiOutput);
                }

            }
            // Process LOOP2310A Segment
            JsonNode loop2310ANode = parentNode.path("Loop2310A");
            if (!loop2310ANode.isMissingNode()) { // Ensure the Loop2310A node exists
                // Process NM1 Segment within Loop2310A
                JsonNode nm1Node = loop2310ANode.path("NM1");
                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within Loop2310A
                    processEdiSegment("NM1", nm1Node);
                }
            }
            // Process LOOP2310B Segment
            JsonNode loop2310BNode = parentNode.path("Loop2310B");
            if (!loop2310BNode.isMissingNode()) { // Ensure the Loop2310B node exists
                // Process NM1 Segment within Loop2310A
                JsonNode nm1Node = loop2310BNode.path("NM1");
                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within Loop2310B
                    processEdiSegment("NM1", nm1Node);
                }
                JsonNode prvNode = loop2310BNode.path("PRV");
                if (!prvNode.isMissingNode()) { // Check if the NM1 segment exists within Loop2310B
                    processEdiSegment("PRV", prvNode);
                }
            }
            // Process LOOP2320 Segment
            JsonNode loop2320Node = parentNode.path("Loop2320");
            if (!loop2320Node.isMissingNode()) { // Ensure the Loop2320 node exists
                // Process AMT Segment within Loop2310A
                JsonNode amtNode = loop2320Node.path("AMT");
                if (!amtNode.isMissingNode()) { // Check if the NM1 segment exists within Loop2320
                    processEdiSegment("AMT", amtNode);
                }
            }
            // Process LOOP2330B Segment
            JsonNode loop2330BNode = parentNode.path("Loop2330B");
            if (!loop2330BNode.isMissingNode()) { // Ensure the Loop2330B node exists
                // Process DTP Segment within Loop2310A
                JsonNode dtpNode = loop2330BNode.path("DTP");
                if (!dtpNode.isMissingNode()) { // Check if the NM1 segment exists within Loop2330B
                    processEdiSegment("DTP", dtpNode);
                }
            }
            // Process Loop2400 Segment
            JsonNode loop2400Node = parentNode.path("Loop2400");
            if (!loop2400Node.isMissingNode() && loop2400Node.isArray()) {
                for (JsonNode loop2400Item : loop2400Node) {
                    // Process LX Segment
                    JsonNode lxNode = loop2400Item.path("LX");
                    if (!lxNode.isMissingNode()) {
                        processEdiSegment("LX", lxNode);
                    }

                    // Process SV1 Segment
                    JsonNode sv1Node = loop2400Item.path("SV1");
                    if (!sv1Node.isMissingNode()) {
                            String sv1Output = String.format("SV1*%s:%s*%s*%s*%s*%s*%s*%s*%s*%s*~",
                                    sv1Node.path("SV101-1").asText(),
                                    sv1Node.path("SV101-2").asText(),
                                    sv1Node.path("SV102").asText(),
                                    sv1Node.path("SV103").asText(),
                                    sv1Node.path("SV104").asText(),
                                    sv1Node.path("SV105").asText(),
                                    sv1Node.path("SV106").asText(),
                                    sv1Node.path("SV107").asText(),
                                    sv1Node.path("SV109").asText(),
                                    sv1Node.path("SV110").asText());

                           // processEdiSegment("SV1", sv1Node);
                            appendToGlobalVariable(sv1Output);
                        }

                    // Process SV5 Segment
                    JsonNode sv5Node = loop2400Item.path("SV5");
                    if (!sv5Node.isMissingNode()) {
                        String sv5Output = String.format("SV5*%s:%s~", sv5Node.path("SV105-1").asText(), sv5Node.path("SV105-2").asText());
                        appendToGlobalVariable(sv5Output);
                        //processEdiSegment("SV5", sv5Node);
                    }
                    // Process DTP Segment
                    JsonNode dtpNode = loop2400Item.path("DTP");
                    if (!dtpNode.isMissingNode()) {
                        processEdiSegment("DTP", dtpNode);
                    }
                }
            }

            // Process SE Segment
            JsonNode seNode = parentNode.path("SE");
            if (!seNode.isMissingNode()) { // Check if the SE segment exists
                processEdiSegment("SE", seNode);
            }

            // Process GE Segment
            JsonNode geNode = parentNode.path("GE");
            if (!geNode.isMissingNode()) { // Check if the GE segment exists
                processEdiSegment("GE", geNode);
            }

            //Process IEA Segment
            JsonNode ieaNode = parentNode.path("IEA");
            if (!ieaNode.isMissingNode()) { // Check if the IEA segment exists
                processEdiSegment("IEA", ieaNode);
            }
            
            return globalEdiBuilder.toString();
           // return ediBuilder.toString();

        } catch (Exception e) {
        //    logger.error("Error converting JSON to EDI 837P: {}", e.getMessage());
            return "Error converting JSON to EDI 837P"; // Returning the Exceptions 
        }
        finally {
            // Reset the globalEdiBuilder after processing the request
        	resetGlobalEdiBuilder();
        }
    	
    }

    private String convertJsonToEdi270(String value) {
        // EDI 270 conversion logic using EdiConversionService
    	
    	StringBuilder ediBuilder = new StringBuilder();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode parentNode = objectMapper.readTree(value);

            // Implement your 270 conversion logic here

            // Process ISA, GS, ST, BHT, Loop1000A, Loop2000A, Loop2010AA, Loop2000B, Loop2010BA, Loop2010BB, Loop2000C, Loop2010CA, Loop2300, Loop2310A, Loop2310B, Loop2320, Loop2330B, Loop2400, SE, GE, IEA segments...
            // Process ISA Segment
            JsonNode isaNode = parentNode.path("ISA");
            if (!isaNode.isMissingNode()) { // Check if the ISA segment exists
                processEdiSegment("ISA", isaNode);
            }

            // Process GS Segment
            JsonNode gsNode = parentNode.path("GS");
            if (!gsNode.isMissingNode()) { // Check if the GS segment exists
                processEdiSegment("GS", gsNode);
            }

            // Process ST Segment
            JsonNode stNode = parentNode.path("ST");
            if (!stNode.isMissingNode()) { // Check if the ST segment exists
                //System.out.println("ST segment found");
                processEdiSegment("ST", stNode);
            }

            // Process BHT Segment
            JsonNode bhtNode = parentNode.path("BHT");
            if (!bhtNode.isMissingNode()) { // Check if the BHT segment exists
                processEdiSegment("BHT", bhtNode);
            }

            // Process LOOP2000A Segment
            JsonNode loop2000ANode = parentNode.path("LOOP2000A");
            if (!loop2000ANode.isMissingNode()) { // Ensure the LOOP2000A node exists
                JsonNode hlNode = loop2000ANode.path("HL");
                if (!hlNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000A
                    processEdiSegment("HL", hlNode);
                }
            }

            // Process LOOP2100A Segment
            JsonNode loop2100ANode = parentNode.path("LOOP2100A");
            if (!loop2100ANode.isMissingNode()) { // Ensure the LOOP2100A node exists
                JsonNode nm1Node = loop2100ANode.path("NM1");
                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2100A
                    processEdiSegment("NM1", nm1Node);
                }
            }


            // Process LOOP2000B Segment
            JsonNode loop2000BNode = parentNode.path("LOOP2000B");
            if (!loop2000BNode.isMissingNode()) { // Ensure the LOOP2000B node exists
                JsonNode hlNode = loop2000BNode.path("HL");
                if (!hlNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000B
                    processEdiSegment("HL", hlNode);
                }
            }

            // Process LOOP2100B Segment
            JsonNode loop2100BNode = parentNode.path("LOOP2100B");
            if (!loop2100BNode.isMissingNode()) { // Ensure the LOOP2100B node exists
                JsonNode nm1Node = loop2100BNode.path("NM1");
                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2100B
                    processEdiSegment("NM1", nm1Node);
                    if (!loop2100BNode.isMissingNode()) { // Ensure the LOOP2100B node exists
                        JsonNode n4Node = loop2100BNode.path("N4");
                        if (!n4Node.isMissingNode()) { // Check if the N4 segment exists within LOOP2100B
                            processEdiSegment("N4", n4Node);
                            if (!loop2100BNode.isMissingNode()) { // Ensure the LOOP2100B node exists
                                JsonNode prvNode = loop2100BNode.path("PRV");
                                if (!prvNode.isMissingNode()) { // Check if the PRV segment exists within LOOP2100B
                                    processEdiSegment("PRV", prvNode);
                                }
                            }

                        }
                    }
                }
            }

            // Process LOOP2000C Segment
            JsonNode loop2000CNode = parentNode.path("LOOP2000C");
            if (!loop2000CNode.isMissingNode()) { // Ensure the LOOP2000C node exists
                JsonNode hlNode = loop2000CNode.path("HL");
                if (!hlNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000B
                    processEdiSegment("HL", hlNode);
                    if (!loop2000CNode.isMissingNode()) { // Ensure the LOOP2000C node exists
                        JsonNode trnNode = loop2000CNode.path("TRN");
                        if (!trnNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000B
                            processEdiSegment("TRN", trnNode);
                        }
                    }
                }
            }

            // Process LOOP2100C Segment
            JsonNode loop2100CNode = parentNode.path("LOOP2100C");
            if (!loop2100CNode.isMissingNode()) { // Ensure the LOOP2100C node exists
                JsonNode refNode = loop2100CNode.path("REF");
                if (!refNode.isMissingNode()) { // Check if the HL segment exists within LOOP2100C
                    processEdiSegment("REF", refNode);
                    if (!loop2100CNode.isMissingNode()) { // Ensure the LOOP2100C node exists
                        JsonNode dtpNode = loop2100CNode.path("DTP");
                        if (!dtpNode.isMissingNode()) { // Check if the HL segment exists within LOOP2100C
                            processEdiSegment("DTP", dtpNode);
                            if (!loop2100CNode.isMissingNode()) { // Ensure the LOOP2100C node exists
                                JsonNode nm1Node = loop2100CNode.path("NM1");
                                if (!nm1Node.isMissingNode()) { // Check if the HL segment exists within LOOP2100C
                                    processEdiSegment("NM1", nm1Node);
                                    if (!loop2100CNode.isMissingNode()) { // Ensure the LOOP2100C node exists
                                        JsonNode dmgNode = loop2100CNode.path("DMG");
                                        if (!dmgNode.isMissingNode()) { // Check if the HL segment exists within LOOP2100C
                                            processEdiSegment("DMG", dmgNode);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Process LOOP2110C Segment
            JsonNode loop2110CNode = parentNode.path("LOOP2110C");
            if (!loop2110CNode.isMissingNode()) { // Ensure the LOOP2110C node exists
                JsonNode eqNode = loop2110CNode.path("EQ");
                if (!eqNode.isMissingNode()) { // Check if the EQ segment exists within LOOP2110C
                    processEdiSegment("EQ", eqNode);
                }
            }

            // Process SE Segment
            JsonNode seNode = parentNode.path("SE");
            if (!seNode.isMissingNode()) { // Check if the SE segment exists
                processEdiSegment("SE", seNode);
            }

            // Process GE Segment
            JsonNode geNode = parentNode.path("GE");
            if (!geNode.isMissingNode()) { // Check if the GE segment exists
                processEdiSegment("GE", geNode);
            }

            // Process IEA Segment
            JsonNode ieaNode = parentNode.path("IEA");
            if (!ieaNode.isMissingNode()) { // Check if the IEA segment exists
                processEdiSegment("IEA", ieaNode);
            }
            return globalEdiBuilder.toString();
            //return ediBuilder.toString();
        } catch (Exception e) {
      //      logger.error("Error converting JSON to EDI 270: {}", e.getMessage());
            return "Error converting JSON to EDI 270";
        }
        finally {
            // Reset the globalEdiBuilder after processing the request
            resetGlobalEdiBuilder();
        }
    }
    
    private String convertJsonToEdi837I(String value){
    	// EDI 837I conversion logic using EdiConversionService
    	StringBuilder ediBuilder = new StringBuilder();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode parentNode = objectMapper.readTree(value);

        // Process ISA Segment
        JsonNode isaNode = parentNode.path("ISA");
        if (!isaNode.isMissingNode()) { // Check if the ISA segment exists
            processEdiSegment("ISA", isaNode);
        }

        // Process GS Segment
        JsonNode gsNode = parentNode.path("GS");
        if (!gsNode.isMissingNode()) { // Check if the GS segment exists
            processEdiSegment("GS", gsNode);
        }

        // Process ST Segment
        JsonNode stNode = parentNode.path("ST");
        if (!stNode.isMissingNode()) { // Check if the ST segment exists
            //System.out.println("ST segment found");
            processEdiSegment("ST", stNode);
        }

        // Process BHT Segment
        JsonNode bhtNode = parentNode.path("BHT");
        if (!bhtNode.isMissingNode()) { // Check if the BHT segment exists
            processEdiSegment("BHT", bhtNode);
        }

        // Process LOOP1000A Segment
        JsonNode loop1000ANode = parentNode.path("LOOP1000A");
        if (!loop1000ANode.isMissingNode()) { // Ensure the LOOP1000A node exists
            JsonNode nm1Node = loop1000ANode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP1000A
                processEdiSegment("NM1", nm1Node);
            }
        }
        if (!loop1000ANode.isMissingNode()) { // Ensure the LOOP1000A node exists
            JsonNode perNode = loop1000ANode.path("PER");
            if (!perNode.isMissingNode()) { // Check if the PER segment exists within LOOP1000A
                processEdiSegment("PER", perNode);
            }
        }

        // Process LOOP1000B Segment
        JsonNode loop1000BNode = parentNode.path("LOOP1000B");
        if (!loop1000BNode.isMissingNode()) { // Ensure the LOOP1000B node exists
            JsonNode nm1Node = loop1000BNode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP1000B
                processEdiSegment("NM1", nm1Node);
            }
        }


        // Process LOOP2000A Segment
        JsonNode loop2000ANode = parentNode.path("LOOP2000A");
        if (!loop2000ANode.isMissingNode()) { // Ensure the LOOP2000A node exists
            JsonNode hlNode = loop2000ANode.path("HL");
            if (!hlNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000A
                processEdiSegment("HL", hlNode);
            }
        }
        if (!loop2000ANode.isMissingNode()) { // Ensure the LOOP2000A node exists
            JsonNode prvNode = loop2000ANode.path("PRV");
            if (!prvNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000A
                processEdiSegment("PRV", prvNode);
            }
        }

        // Process LOOP2010AA Segment
        JsonNode loop2010AANode = parentNode.path("LOOP2010AA");
        if (!loop2010AANode.isMissingNode()) { // Ensure the LOOP2010AA node exists
            JsonNode nm1Node = loop2010AANode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2010AA
                processEdiSegment("NM1", nm1Node);
                if (!loop2010AANode.isMissingNode()) { // Ensure the LOOP2010AA node exists
                    JsonNode n3Node = loop2010AANode.path("N3");
                    if (!n3Node.isMissingNode()) { // Check if the N3 segment exists within LOOP2010AA
                        processEdiSegment("N3", n3Node);
                        if (!loop2010AANode.isMissingNode()) { // Ensure the LOOP2010AA node exists
                            JsonNode n4Node = loop2010AANode.path("N4");
                            if (!n4Node.isMissingNode()) { // Check if the N4 segment exists within LOOP2010AA
                                processEdiSegment("N4", n4Node);
                                if (!loop2010AANode.isMissingNode()) { // Ensure the LOOP node2010AA exists
                                    JsonNode refNode = loop2010AANode.path("REF");
                                    if (!refNode.isMissingNode()) { // Check if the PRV segment exists within LOOP2010AA
                                        processEdiSegment("REF", refNode);
                                    }
                                }

                            }
                        }
                    }
                }
            }
            }


        // Process LOOP2000B Segment
        JsonNode loop2000BNode = parentNode.path("LOOP2000B");
        if (!loop2000BNode.isMissingNode()) { // Ensure the LOOP2000B node exists
            JsonNode hlNode = loop2000BNode.path("HL");
            if (!hlNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000B
                processEdiSegment("HL", hlNode);
                if (!loop2000BNode.isMissingNode()) { // Ensure the LOOP2000C node exists
                    JsonNode sbrNode = loop2000BNode.path("SBR");
                    if (!sbrNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000B
                        processEdiSegment("SBR", sbrNode);
                    }
                }
            }
        }

        // Process LOOP2010BASegment
        JsonNode loop2010BANode = parentNode.path("LOOP2010BA");
        if (!loop2010BANode.isMissingNode()) { // Ensure the LOOP2010BA node exists
            JsonNode nm1Node = loop2010BANode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the HL segment exists within LOOP2010BB
                processEdiSegment("NM1", nm1Node);
                if (!loop2010BANode.isMissingNode()) { // Ensure the LOOP2000C node exists
                    JsonNode dmgNode = loop2010BANode.path("DMG");
                    if (!dmgNode.isMissingNode()) { // Check if the HL segment exists within LOOP2010BB
                        processEdiSegment("DMG", dmgNode);
                    }
                }
            }
        }

        // Process LOOP2010BBSegment
        JsonNode loop2010BBNode = parentNode.path("LOOP2010BB");
        if (!loop2010BBNode.isMissingNode()) { // Ensure the LOOP2000B node exists
            JsonNode nm1Node = loop2010BBNode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2010BB
                processEdiSegment("NM1", nm1Node);
                if (!loop2010BBNode.isMissingNode()) { // Ensure the LOOP2000C node exists
                    JsonNode n4Node = loop2010BBNode.path("N4");
                    if (!n4Node.isMissingNode()) { // Check if the HL segment exists within LOOP2000B
                        processEdiSegment("N4", n4Node);
                        if (!loop2010BBNode.isMissingNode()) { // Ensure the LOOP2000C node exists
                            JsonNode refNode = loop2010BBNode.path("REF");
                            if (!refNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000B
                                processEdiSegment("REF", refNode);
                            }
                        }
                    }
                }
            }
        }


     // Process LOOP2300 Segment
        JsonNode loop2300Node = parentNode.path("LOOP2300");
        if (!loop2300Node.isMissingNode()) { // Ensure the LOOP2300 node exists
            JsonNode clmNode = loop2300Node.path("CLM");
            if (!clmNode.isMissingNode()) { // Check if the CLM segment exists within LOOP2300
                String clmOutput = String.format("CLM*%s*%s*%s*%s*%s:%s:%s*%s*%s*%s*%s*%s~",
                        clmNode.path("CLM01").asText(),
                        clmNode.path("CLM02").asText(),
                        clmNode.path("CLM03").asText(),
                        clmNode.path("CLM04").asText(),
                        clmNode.path("CLM05").asText(),
                        clmNode.path("CLM06").asText(),
                        clmNode.path("CLM07").asText(),
                        clmNode.path("CLM08").asText(),
                        clmNode.path("CLM09").asText(),
                        clmNode.path("CLM10").asText(),
                        clmNode.path("CLM11").asText(),
                        clmNode.path("CLM12").asText());

                appendToGlobalVariable(clmOutput);
            }
            // Process DTP Segment
            JsonNode dtpNode = loop2300Node.path("DTP");
            if (!dtpNode.isMissingNode()) { // Check if the DTP segment exists within LOOP2300
                processEdiSegment("DTP", dtpNode);
            }
            // Process CL1 Segment
            JsonNode cl1Node = loop2300Node.path("CL1");
            if (!cl1Node.isMissingNode()) { // Check if the CL1 segment exists within LOOP2300
                processEdiSegment("CL1", cl1Node);
            }
            // Process CN1 Segment
            JsonNode cn1Node = loop2300Node.path("CN1");
            if (!cn1Node.isMissingNode()) { // Check if the CN1 segment exists within LOOP2300
                processEdiSegment("CN1", cn1Node);
            }
            // Process REF Segment
            JsonNode refNode = loop2300Node.path("REF");
            if (!refNode.isMissingNode()) { // Check if the REF segment exists within LOOP2300
                processEdiSegment("REF", refNode);
            }
            // Process HI Segment
            JsonNode hiNode = loop2300Node.path("HI");
            if (!hiNode.isMissingNode()) { // Check if the HI segment exists within LOOP2300
                String hiOutput = String.format("HI*%s:%s~", hiNode.path("HI01-1").asText(), hiNode.path("HI01-2").asText());
                appendToGlobalVariable(hiOutput);
            }
        }



        // Process LOOP2310A Segment
        JsonNode loop2310ANode = parentNode.path("LOOP2310A");
        if (!loop2310ANode.isMissingNode()) { // Ensure the LOOP2310A node exists
            JsonNode nm1Node = loop2310ANode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2310A
                processEdiSegment("NM1", nm1Node);
                if (!loop2310ANode.isMissingNode()) { // Ensure the LOOP2310A node exists
                    JsonNode n3Node = loop2310ANode.path("N3");
                    if (!n3Node.isMissingNode()) { // Check if the N3 segment exists within LOOP2310A
                        processEdiSegment("N3", n3Node);
                        if (!loop2310ANode.isMissingNode()) { // Ensure the LOOP2310A node exists
                            JsonNode n4Node = loop2310ANode.path("N4");
                            if (!n4Node.isMissingNode()) { // Check if the N4 segment exists within LOOP2310A
                                processEdiSegment("N4", n4Node);
                                if (!loop2310ANode.isMissingNode()) { // Ensure the LOOP2310A node exists
                                    JsonNode prvNode = loop2310ANode.path("PRV");
                                    if (!prvNode.isMissingNode()) { // Check if the PRV segment exists within LOOP2310A
                                        processEdiSegment("PRV", prvNode);
                                        if (!loop2310ANode.isMissingNode()) { // Ensure the LOOP2310A node exists
                                            JsonNode refNode = loop2310ANode.path("REF");
                                            if (!refNode.isMissingNode()) { // Check if the REF segment exists within LOOP2310A
                                                processEdiSegment("REF", refNode);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

// Process LOOP2310B Segment
        JsonNode loop2310BNode = parentNode.path("LOOP2310B");
        if (!loop2310BNode.isMissingNode()) { // Ensure the LOOP2310B node exists
            JsonNode nm1Node = loop2310BNode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2310B
                processEdiSegment("NM1", nm1Node);
            }
        }

        // Process LOOP2310C Segment
        JsonNode loop2310CNode = parentNode.path("LOOP2310C");
        if (!loop2310CNode.isMissingNode()) { // Ensure the LOOP2310C node exists
            JsonNode nm1Node = loop2310CNode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2310C
                processEdiSegment("NM1", nm1Node);
            }
        }

        // Process LOOP2310E Segment
        JsonNode loop2310ENode = parentNode.path("LOOP2310E");
        if (!loop2310ENode.isMissingNode()) { // Ensure the LOOP2310E node exists
            JsonNode nm1Node = loop2310ENode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2310E
                processEdiSegment("NM1", nm1Node);
                if (!loop2310ENode.isMissingNode()) { // Ensure the LOOP2310E node exists
                    JsonNode n3Node = loop2310ENode.path("N3");
                    if (!n3Node.isMissingNode()) { // Check if the N3 segment exists within LOOP2310E
                        processEdiSegment("N3", n3Node);
                        if (!loop2310ENode.isMissingNode()) { // Ensure the LOOP2310E node exists
                            JsonNode n4Node = loop2310ENode.path("N4");
                            if (!n4Node.isMissingNode()) { // Check if the N4 segment exists within LOOP2310E
                                processEdiSegment("N4", n4Node);
                               if (!loop2310ENode.isMissingNode()) { // Ensure the LOOP2310E node exists
                                            JsonNode refNode = loop2310ENode.path("REF");
                                            if (!refNode.isMissingNode()) { // Check if the REF segment exists within LOOP2310E
                                                processEdiSegment("REF", refNode);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

        // Process LOOP2310F Segment
        JsonNode loop2310FNode = parentNode.path("LOOP2310F");
        if (!loop2310FNode.isMissingNode()) { // Ensure the LOOP2310F node exists
            JsonNode nm1Node = loop2310FNode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2310F
                processEdiSegment("NM1", nm1Node);
                if (!loop2310FNode.isMissingNode()) { // Ensure the LOOP2310E node exists
                    JsonNode n3Node = loop2310FNode.path("N3");
                    if (!n3Node.isMissingNode()) { // Check if the N3 segment exists within LOOP2310F
                        processEdiSegment("N3", n3Node);
                        if (!loop2310FNode.isMissingNode()) { // Ensure the LOOP2310F node exists
                            JsonNode n4Node = loop2310FNode.path("N4");
                            if (!n4Node.isMissingNode()) { // Check if the N4 segment exists within LOOP2310F
                                processEdiSegment("N4", n4Node);
                                if (!loop2310FNode.isMissingNode()) { // Ensure the LOOP2310F node exists
                                    JsonNode refNode = loop2310FNode.path("REF");
                                    if (!refNode.isMissingNode()) { // Check if the REF segment exists within LOOP2310F
                                        processEdiSegment("REF", refNode);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Process LOOP2320 Segment
        JsonNode loop2320Node = parentNode.path("LOOP2320");
        if (!loop2320Node.isMissingNode()) { // Ensure the LOOP2320 node exists
            JsonNode sbrNode = loop2320Node.path("SBR");
            if (!sbrNode.isMissingNode()) { // Check if the NM1 segment exists within LOOP2320
                processEdiSegment("SBR", sbrNode);
                if (!loop2320Node.isMissingNode()) { // Ensure the LOOP2320 node exists
                    JsonNode casNode = loop2320Node.path("CAS");
                    if (!casNode.isMissingNode()) { // Check if the CAS segment exists within LOOP2320
                        processEdiSegment("CAS", casNode);
                        if (!loop2320Node.isMissingNode()) { // Ensure the LOOP2320 node exists
                            JsonNode amtNode = loop2320Node.path("AMT");
                            if (!amtNode.isMissingNode()) { // Check if the AMT segment exists within LOOP2320
                                processEdiSegment("AMT", amtNode);
                            }
                        }
                    }
                }
            }
        }


        // Process LOOP2330A Segment
        JsonNode loop2330ANode = parentNode.path("LOOP2330A");
        if (!loop2330ANode.isMissingNode()) { // Ensure the LOOP2330A node exists
            JsonNode nm1Node = loop2330ANode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the AMT segment exists within LOOP2330A
                processEdiSegment("NM1", nm1Node);
            }
        }

        // Process LOOP2330B Segment
        JsonNode loop2330BNode = parentNode.path("LOOP2330B");
        if (!loop2330BNode.isMissingNode()) { // Ensure the LOOP2330B node exists
            JsonNode nm1Node = loop2330BNode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the AMT segment exists within LOOP2330B
                processEdiSegment("NM1", nm1Node);
              if (!loop2330BNode.isMissingNode()) { // Ensure the LOOP2330B node exists
                    JsonNode dtpNode = loop2330BNode.path("DTP");
                    if (!nm1Node.isMissingNode()) { // Check if the DTP segment exists within LOOP2330B
                        processEdiSegment("DTP", dtpNode);
                    }
                }
            }
        }


        // Process LOOP2400 Segment
        JsonNode loop2400Node = parentNode.path("LOOP2400");
        if (!loop2400Node.isMissingNode()) { // Ensure the LOOP2400 node exists
            JsonNode lxNode = loop2400Node.path("LX");
            if (!lxNode.isMissingNode()) { // Check if the AMT segment exists within LOOP2400
                processEdiSegment("LX", lxNode);
                if (!loop2400Node.isMissingNode()) { // Ensure the LOOP2400 node exists
                    JsonNode sv2Node = loop2400Node.path("SV2");
                    if (!sv2Node.isMissingNode()) { // Check if the SV2 segment exists within LOOP2400
                        processEdiSegment("SV2", sv2Node);
                    }
                }
            }
        }


        // Process LOOP2420D Segment
        JsonNode loop2420DNode = parentNode.path("LOOP2420D");
        if (!loop2420DNode.isMissingNode()) { // Ensure the LOOP2420D node exists
            JsonNode nm1Node = loop2420DNode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2420D
                processEdiSegment("NM1", nm1Node);
                if (!loop2420DNode.isMissingNode()) { // Ensure the LOOP2420D node exists
                    JsonNode refNode = loop2420DNode.path("REF");
                    if (!refNode.isMissingNode()) { // Check if the REF segment exists within LOOP2420D
                        processEdiSegment("REF", refNode);
                    }
                }
            }
        }

        // Process LOOP2430 Segment
        JsonNode loop2430Node = parentNode.path("LOOP2430");
        if (!loop2430Node.isMissingNode()) { // Ensure the LOOP2430 node exists
            JsonNode svdNode = loop2430Node.path("SVD");
            if (!svdNode.isMissingNode()) { // Check if the SVD segment exists within LOOP2430
                processEdiSegment("SVD", svdNode);
                if (!loop2430Node.isMissingNode()) { // Ensure the LOOP2430 node exists
                    JsonNode dtpNode = loop2430Node.path("DTP");
                    if (!dtpNode.isMissingNode()) { // Check if the DTP segment exists within LOOP2430
                        processEdiSegment("DTP", dtpNode);
                    }
                }
            }
        }

        // Process SE Segment
        JsonNode seNode = parentNode.path("SE");
        if (!seNode.isMissingNode()) { // Check if the SE segment exists
            processEdiSegment("SE", seNode);
        }

        // Process GE Segment
        JsonNode geNode = parentNode.path("GE");
        if (!geNode.isMissingNode()) { // Check if the GE segment exists
            processEdiSegment("GE", geNode);
        }

        // Process IEA Segment
        JsonNode ieaNode = parentNode.path("IEA");
        if (!ieaNode.isMissingNode()) { // Check if the IEA segment exists
            processEdiSegment("IEA", ieaNode);
        }
        return globalEdiBuilder.toString();
        //return ediBuilder.toString();
    } catch (Exception e) {
  //      logger.error("Error converting JSON to EDI 837I: {}", e.getMessage());
        return "Error converting JSON to EDI 837I"; // Returning the Expception 
    }
    finally {
        // Reset the globalEdiBuilder after processing the request
        resetGlobalEdiBuilder();
    }
}
   
       private void processEdiSegment (String segmentName, JsonNode segmentNode){
        StringBuilder segmentBuilder = new StringBuilder(segmentName);
        segmentNode.fields().forEachRemaining(field -> {
            appendFieldValues(segmentBuilder, field.getValue());
        });
        //segmentBuilder.append("~\n");
        //System.out.println(segmentBuilder);
        // Append the segment to the global EDI builder
          segmentBuilder.append(SEGMENT_DELIMITER);
          //globalEdiBuilder.append(segmentBuilder);
          appendToGlobalVariable(segmentBuilder.toString()); 
      }

      private void appendFieldValues (StringBuilder segmentBuilder, JsonNode node){
        if (node.isObject()) {
            // If the field value is an object (nested structure), recursively process its fields
            node.fields().forEachRemaining(field -> appendFieldValues(segmentBuilder, field.getValue()));
        } else {
            // If the field value is not an object, append its value to the segmentBuilder
            segmentBuilder.append(ELEMENT_DELIMITER).append(node.asText());
        }
      }
      private void resetGlobalEdiBuilder() {
    	    // Reset the globalEdiBuilder after processing the request to 0
    	    globalEdiBuilder.setLength(0);
    	}
      
}



