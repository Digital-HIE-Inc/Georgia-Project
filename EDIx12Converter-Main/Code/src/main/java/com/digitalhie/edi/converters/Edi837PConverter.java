package com.digitalhie.edi.converters;

import com.digitalhie.edi.EdiSegmentProcessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Converts JSON data representing an Professional Claim Form (837P) into EDIx12 format.
 * The conversion process involves parsing the JSON data and generating corresponding EDIx12 segments.
 * The EDI segments are  Process ISA, GS, ST, BHT, Loop1000A, Loop2000A, Loop2010AA, Loop2000B, Loop2010BA, Loop2010BB, Loop2000C, Loop2010CA, 
 * Loop2300, Loop2310A, Loop2310B, Loop2320, Loop2330B, Loop2400, SE, GE, IEA segments..
 * 
 * @author namrathag
 * @version 1.0
 */

public class Edi837PConverter implements EdiConverter{
	
	/** The segment processor responsible for processing individual EDI segments. */
	private final EdiSegmentProcessor segmentProcessor = new EdiSegmentProcessor();
	
	 /**
     * 837P (Professional) Claim Form into EDIx12 format.
     * 
     * @param title: The title of the EDIx12 document.
     * @param decodedString: The decoded input JSON string representing the Claim form (837P).
     * @return The converted EDIx12 as a string.
     */
	
	 public String convertJsonToEdi(String title, String decodedString) {
	        // EDI 837P conversion logic using EdiConversionService
	    	 StringBuilder ediBuilder = new StringBuilder();
	         try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode parentNode = objectMapper.readTree(decodedString);

	            // Implement your 837P conversion logic here   
	            
	            // Process ISA, GS, ST, BHT, Loop1000A, Loop2000A, Loop2010AA, Loop2000B, Loop2010BA, Loop2010BB, Loop2000C, Loop2010CA, Loop2300, Loop2310A, Loop2310B, Loop2320, Loop2330B, Loop2400, SE, GE, IEA segments.
	            //Process ISA Segment
	            JsonNode isaNode = parentNode.path("ISA");
	            if (!isaNode.isMissingNode()) {
	                segmentProcessor.processEdiSegment("ISA", isaNode);
	            }
	            // Process GS Segment
	            JsonNode gsNode = parentNode.path("GS");
	            if (!gsNode.isMissingNode()) {
	                segmentProcessor.processEdiSegment("GS", gsNode);
	            }
	            // Process ST Segment
	            JsonNode stNode = parentNode.path("ST");
	            if (!stNode.isMissingNode()) {
	                segmentProcessor.processEdiSegment("ST", stNode);
	            }
	            // Process BHT Segment
	            JsonNode bhtNode = parentNode.path("BHT");
	            if (!bhtNode.isMissingNode()) {
	                segmentProcessor.processEdiSegment("BHT", bhtNode);
	            }
	            // Process LOOP1000A Segment
	            JsonNode loop1000ANode = parentNode.path("Loop1000A");
	            if (!loop1000ANode.isMissingNode()) { // Ensure the Loop1000A node exists
	                JsonNode nm1Node = loop1000ANode.path("NM1");
	                if (!nm1Node.isMissingNode()) { // Check if the HL segment exists within LOOP2000B
	                    segmentProcessor.processEdiSegment("NM1", nm1Node);
	                    if (!loop1000ANode.isMissingNode()) { // Ensure the LOOP2000C node exists
	                        JsonNode perNode = loop1000ANode.path("PER");
	                        if (!perNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000B
	                            segmentProcessor.processEdiSegment("PER", perNode);
	                        }
	                    }
	                }
	            }
	            // Process LOOP1000B Segment
	            JsonNode loop1000BNode = parentNode.path("Loop1000B");
	            if (!loop1000BNode.isMissingNode()) { // Ensure the Loop1000A node exists
	                JsonNode nm1Node = loop1000BNode.path("NM1");
	                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2000B
	                    segmentProcessor.processEdiSegment("NM1", nm1Node);
	                }
	            }
	            // Process Loop2000A Segment
	            JsonNode loop2000ANode = parentNode.path("Loop2000A");
	            if (!loop2000ANode.isMissingNode()) { // Ensure the Loop2000A node exists
	                // Process HL Segment within Loop2000A
	                JsonNode hlNode = loop2000ANode.path("HL");
	                if (!hlNode.isMissingNode()) { // Check if the HL segment exists within Loop2000A
	                    segmentProcessor.processEdiSegment("HL", hlNode);

	                    // Process PRV Segment within Loop2000A
	                    JsonNode prvNode = loop2000ANode.path("PRV");
	                    if (!prvNode.isMissingNode()) { // Check if the PRV segment exists within Loop2000A
	                        segmentProcessor.processEdiSegment("PRV", prvNode);
	                    }
	                }
	            }
	            // Process Loop2010AA Segment within Loop2000A
	            JsonNode loop2010AANode = parentNode.path("Loop2010AA");
	            if (!loop2010AANode.isMissingNode()) { // Ensure the Loop2010AA node exists within Loop2000A
	                // Process NM1 Segment within Loop2010AA
	                JsonNode nm1Node = loop2010AANode.path("NM1");
	                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within Loop2010AA
	                    segmentProcessor.processEdiSegment("NM1", nm1Node);
	                }

	                // Process N3 Segment within Loop2010AA
	                JsonNode n3Node = loop2010AANode.path("N3");
	                if (!n3Node.isMissingNode()) { // Check if the N3 segment exists within Loop2010AA
	                    segmentProcessor.processEdiSegment("N3", n3Node);
	                }

	                // Process N4 Segment within Loop2010AA
	                JsonNode n4Node = loop2010AANode.path("N4");
	                if (!n4Node.isMissingNode()) { // Check if the N4 segment exists within Loop2010AA
	                    segmentProcessor.processEdiSegment("N4", n4Node);
	                }

	                // Process REF Segment within Loop2010AA
	                JsonNode refNode = loop2010AANode.path("REF");
	                if (!refNode.isMissingNode()) { // Check if the REF segment exists within Loop2010AA
	                    segmentProcessor.processEdiSegment("REF", refNode);
	                }
	            }

	            // Process LOOP2000B Segment
	            JsonNode loop2000BNode = parentNode.path("Loop2000B");
	            if (!loop2000BNode.isMissingNode()) { // Ensure the Loop2000B node exists
	                // Process HL Segment within Loop2000B
	                JsonNode hlNode = loop2000BNode.path("HL");
	                if (!hlNode.isMissingNode()) { // Check if the HL segment exists within Loop2000B
	                    segmentProcessor.processEdiSegment("HL", hlNode);
	                }

	                // Process SBR Segment within Loop2000B
	                JsonNode sbrNode = loop2000BNode.path("SBR");
	                if (!sbrNode.isMissingNode()) { // Check if the SBR segment exists within Loop2000B
	                    segmentProcessor.processEdiSegment("SBR", sbrNode);
	                }
	            }
	            // Process LOOP2010BA Segment
	            JsonNode loop2010BANode = parentNode.path("Loop2010BA");
	            if (!loop2010BANode.isMissingNode()) { // Ensure the Loop2010BA node exists
	                // Process NM1 Segment within Loop2010BA
	                JsonNode nm1Node = loop2010BANode.path("NM1");
	                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within Loop2010BA
	                    segmentProcessor.processEdiSegment("NM1", nm1Node);
	                }

	                // Process DMG Segment within Loop2010BA
	                JsonNode dmgNode = loop2010BANode.path("DMG");
	                if (!dmgNode.isMissingNode()) { // Check if the DMG segment exists within Loop2010BA
	                    segmentProcessor.processEdiSegment("DMG", dmgNode);
	                }
	            }

	            // Process LOOP2010BB Segment
	            JsonNode loop2010BBNode = parentNode.path("Loop2010BB");
	            if (!loop2010BBNode.isMissingNode()) { // Ensure the Loop2010BB node exists
	                // Process NM1 Segment within Loop2010BB
	                JsonNode nm1Node = loop2010BBNode.path("NM1");
	                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within Loop2010BB
	                    segmentProcessor.processEdiSegment("NM1", nm1Node);
	                }
	                // Process N4 Segment within Loop2010BB
	                JsonNode n4Node = loop2010BBNode.path("N4");
	                if (!n4Node.isMissingNode()) { // Check if the N4 segment exists within Loop2010BB
	                    segmentProcessor.processEdiSegment("N4", n4Node);
	                }
	                // Process REF Segment within Loop2010BB
	                JsonNode refNode = loop2010BBNode.path("REF");
	                if (!refNode.isMissingNode()) { // Check if the REF segment exists within Loop2010BB
	                    segmentProcessor.processEdiSegment("REF", refNode);
	                }
	            }

	            // Process LOOP2000C Segment
	            JsonNode loop2000CNode = parentNode.path("Loop2000C");
	            if (!loop2000CNode.isMissingNode()) { // Ensure the Loop2000B node exists
	                // Process HL Segment within Loop2000C
	                JsonNode hlNode = loop2000CNode.path("HL");
	                if (!hlNode.isMissingNode()) { // Check if the HL segment exists within Loop2000C
	                    segmentProcessor.processEdiSegment("HL", hlNode);
	                }

	                // Process PAT Segment within Loop2000C
	                JsonNode patNode = loop2000CNode.path("PAT");
	                if (!patNode.isMissingNode()) { // Check if the SBR segment exists within Loop2000C
	                    segmentProcessor.processEdiSegment("PAT", patNode);
	                }
	            }
	            // Process LOOP2010CA Segment
	            JsonNode loop2010CANode = parentNode.path("Loop2010CA");
	            if (!loop2010CANode.isMissingNode()) { // Ensure the Loop2010CA node exists
	                // Process NM1 Segment within Loop2010CA
	                JsonNode nm1Node = loop2010CANode.path("NM1");
	                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within Loop2010CA
	                    segmentProcessor.processEdiSegment("NM1", nm1Node);
	                }
	                // Process N3 Segment within Loop2010CA
	                JsonNode n3Node = loop2010CANode.path("N3");
	                if (!n3Node.isMissingNode()) { // Check if the N3 segment exists within Loop2010CA
	                    segmentProcessor.processEdiSegment("N3", n3Node);
	                }

	                // Process N4 Segment within Loop2010CA
	                JsonNode n4Node = loop2010CANode.path("N4");
	                if (!n4Node.isMissingNode()) { // Check if the N4 segment exists within Loop2010CA
	                    segmentProcessor.processEdiSegment("N4", n4Node);
	                }
	                // Process DMG Segment within Loop2010CA
	                JsonNode dmgNode = loop2010CANode.path("DMG");
	                if (!dmgNode.isMissingNode()) { // Check if the DMG segment exists within Loop2010CA
	                    segmentProcessor.processEdiSegment("DMG", dmgNode);
	                }
	            }
	            // Process Loop2300 Segment
	            JsonNode loop2300Node = parentNode.path("Loop2300");
	            if (!loop2300Node.isMissingNode()) { // Ensure the Loop2300 node exists

	                // Process CLM Segment
	                JsonNode clmNode = loop2300Node.path("CLM");
	                if (!clmNode.isMissingNode()) { // Check if the CLM segment exists within Loop2300
	                    segmentProcessor.processEdiSegment("CLM", clmNode);
	                }
	                //REF
	                JsonNode refNode = loop2300Node.path("REF");
	                if (!refNode.isMissingNode()) { // Check if the REF segment exists within Loop2300
	                    segmentProcessor.processEdiSegment("REF", refNode);
	                }
	                //HI
	                JsonNode hiNode = loop2300Node.path("HI");
	                if (!hiNode.isMissingNode()) { // Check if the HI segment exists within Loop2300
	                    String hiOutput = String.format("HI*%s:%s~", hiNode.path("HI01-1").asText(), hiNode.path("HI01-2").asText());
	                    //segmentProcessor.processEdiSegment("HI", hiNode);
	                    segmentProcessor.appendToGlobalVariable(hiOutput);
	                }

	            }
	            // Process LOOP2310A Segment
	            JsonNode loop2310ANode = parentNode.path("Loop2310A");
	            if (!loop2310ANode.isMissingNode()) { // Ensure the Loop2310A node exists
	                // Process NM1 Segment within Loop2310A
	                JsonNode nm1Node = loop2310ANode.path("NM1");
	                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within Loop2310A
	                    segmentProcessor.processEdiSegment("NM1", nm1Node);
	                }
	            }
	            // Process LOOP2310B Segment
	            JsonNode loop2310BNode = parentNode.path("Loop2310B");
	            if (!loop2310BNode.isMissingNode()) { // Ensure the Loop2310B node exists
	                // Process NM1 Segment within Loop2310A
	                JsonNode nm1Node = loop2310BNode.path("NM1");
	                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within Loop2310B
	                    segmentProcessor.processEdiSegment("NM1", nm1Node);
	                }
	                JsonNode prvNode = loop2310BNode.path("PRV");
	                if (!prvNode.isMissingNode()) { // Check if the NM1 segment exists within Loop2310B
	                    segmentProcessor.processEdiSegment("PRV", prvNode);
	                }
	            }
	            // Process LOOP2320 Segment
	            JsonNode loop2320Node = parentNode.path("Loop2320");
	            if (!loop2320Node.isMissingNode()) { // Ensure the Loop2320 node exists
	                // Process AMT Segment within Loop2310A
	                JsonNode amtNode = loop2320Node.path("AMT");
	                if (!amtNode.isMissingNode()) { // Check if the NM1 segment exists within Loop2320
	                    segmentProcessor.processEdiSegment("AMT", amtNode);
	                }
	            }
	            // Process LOOP2330B Segment
	            JsonNode loop2330BNode = parentNode.path("Loop2330B");
	            if (!loop2330BNode.isMissingNode()) { // Ensure the Loop2330B node exists
	                // Process DTP Segment within Loop2310A
	                JsonNode dtpNode = loop2330BNode.path("DTP");
	                if (!dtpNode.isMissingNode()) { // Check if the NM1 segment exists within Loop2330B
	                    segmentProcessor.processEdiSegment("DTP", dtpNode);
	                }
	            }
	            // Process Loop2400 Segment
	            JsonNode loop2400Node = parentNode.path("Loop2400");
	            if (!loop2400Node.isMissingNode() && loop2400Node.isArray()) {
	                for (JsonNode loop2400Item : loop2400Node) {
	                    // Process LX Segment
	                    JsonNode lxNode = loop2400Item.path("LX");
	                    if (!lxNode.isMissingNode()) {
	                        segmentProcessor.processEdiSegment("LX", lxNode);
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

	                           // segmentProcessor.processEdiSegment("SV1", sv1Node);
	                            segmentProcessor.appendToGlobalVariable(sv1Output);
	                        }

	                    // Process SV5 Segment
	                    JsonNode sv5Node = loop2400Item.path("SV5");
	                    if (!sv5Node.isMissingNode()) {
	                        String sv5Output = String.format("SV5*%s:%s~", sv5Node.path("SV105-1").asText(), sv5Node.path("SV105-2").asText());
	                        segmentProcessor.appendToGlobalVariable(sv5Output);
	                        //segmentProcessor.processEdiSegment("SV5", sv5Node);
	                    }
	                    // Process DTP Segment
	                    JsonNode dtpNode = loop2400Item.path("DTP");
	                    if (!dtpNode.isMissingNode()) {
	                        segmentProcessor.processEdiSegment("DTP", dtpNode);
	                    }
	                }
	            }

	            // Process SE Segment
	            JsonNode seNode = parentNode.path("SE");
	            if (!seNode.isMissingNode()) { // Check if the SE segment exists
	                segmentProcessor.processEdiSegment("SE", seNode);
	            }

	            // Process GE Segment
	            JsonNode geNode = parentNode.path("GE");
	            if (!geNode.isMissingNode()) { // Check if the GE segment exists
	                segmentProcessor.processEdiSegment("GE", geNode);
	            }

	            //Process IEA Segment
	            JsonNode ieaNode = parentNode.path("IEA");
	            if (!ieaNode.isMissingNode()) { // Check if the IEA segment exists
	                segmentProcessor.processEdiSegment("IEA", ieaNode);
	            }
	            
	            return segmentProcessor.getGlobalEdiBuilderContent();
	        } catch (Exception e) {
	        	// Error Handling
	            return "Error converting JSON to EDI 837P";
	        }
	        finally {
	            // Reset the globalEdiBuilder after processing the request
	        	segmentProcessor.resetGlobalEdiBuilder();
	        }
	    	
	    }

}
