package com.digitalhie.edi.converters;

import com.digitalhie.edi.EdiSegmentProcessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Converts JSON data representing a health care eligibility inquiry (270) into EDIx12 format.
 * The conversion process involves parsing the JSON data and generating corresponding EDIx12 segments.
 * The EDI segments are ISA, GS, ST, BHT, Loop2000A, Loop2100A, Loop2000B, Loop2100B,
 * Loop2000C, Loop2100C, SE, GE, and IEA segments.
 * 
 * @author namrathag
 * @version 1.0
 */
public class Edi270Converter implements EdiConverter{
	
	/** The segment processor responsible for processing individual EDI segments. */
	private final EdiSegmentProcessor segmentProcessor = new EdiSegmentProcessor();
	 /**
     * 270 Claim Form into EDIx12 format.
     * 
     * @param title: The title of the EDIx12 document.
     * @param decodedString: The decoded input JSON string representing the healthcare eligibility inquiry (270).
     * @return The converted EDIx12 as a string.
     */
	@Override
	public String convertJsonToEdi(String title, String decodedString) {
        // EDI 270 conversion logic using EdiConverter
    	
    	StringBuilder ediBuilder = new StringBuilder();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode parentNode = objectMapper.readTree(decodedString);

            // Implement your 270 conversion logic here

            // Process ISA, GS, ST, BHT, Loop1000A, Loop2000A, Loop2010AA, Loop2000B, Loop2010BA, Loop2010BB, Loop2000C, Loop2010CA, Loop2300, Loop2310A, Loop2310B, Loop2320, Loop2330B, Loop2400, SE, GE, IEA segments
            // Process ISA Segment
            JsonNode isaNode = parentNode.path("ISA");
            if (!isaNode.isMissingNode()) { // Check if the ISA segment exists
                segmentProcessor.processEdiSegment("ISA", isaNode);
            }

            // Process GS Segment
            JsonNode gsNode = parentNode.path("GS");
            if (!gsNode.isMissingNode()) { // Check if the GS segment exists
                segmentProcessor.processEdiSegment("GS", gsNode);
            }

            // Process ST Segment
            JsonNode stNode = parentNode.path("ST");
            if (!stNode.isMissingNode()) { // Check if the ST segment exists
                //System.out.println("ST segment found");
                segmentProcessor.processEdiSegment("ST", stNode);
            }

            // Process BHT Segment
            JsonNode bhtNode = parentNode.path("BHT");
            if (!bhtNode.isMissingNode()) { // Check if the BHT segment exists
                segmentProcessor.processEdiSegment("BHT", bhtNode);
            }

            // Process LOOP2000A Segment
            JsonNode loop2000ANode = parentNode.path("LOOP2000A");
            if (!loop2000ANode.isMissingNode()) { // Ensure the LOOP2000A node exists
                JsonNode hlNode = loop2000ANode.path("HL");
                if (!hlNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000A
                    segmentProcessor.processEdiSegment("HL", hlNode);
                }
            }

            // Process LOOP2100A Segment
            JsonNode loop2100ANode = parentNode.path("LOOP2100A");
            if (!loop2100ANode.isMissingNode()) { // Ensure the LOOP2100A node exists
                JsonNode nm1Node = loop2100ANode.path("NM1");
                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2100A
                    segmentProcessor.processEdiSegment("NM1", nm1Node);
                }
            }


            // Process LOOP2000B Segment
            JsonNode loop2000BNode = parentNode.path("LOOP2000B");
            if (!loop2000BNode.isMissingNode()) { // Ensure the LOOP2000B node exists
                JsonNode hlNode = loop2000BNode.path("HL");
                if (!hlNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000B
                    segmentProcessor.processEdiSegment("HL", hlNode);
                }
            }

            // Process LOOP2100B Segment
            JsonNode loop2100BNode = parentNode.path("LOOP2100B");
            if (!loop2100BNode.isMissingNode()) { // Ensure the LOOP2100B node exists
                JsonNode nm1Node = loop2100BNode.path("NM1");
                if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2100B
                    segmentProcessor.processEdiSegment("NM1", nm1Node);
                    if (!loop2100BNode.isMissingNode()) { // Ensure the LOOP2100B node exists
                        JsonNode n4Node = loop2100BNode.path("N4");
                        if (!n4Node.isMissingNode()) { // Check if the N4 segment exists within LOOP2100B
                            segmentProcessor.processEdiSegment("N4", n4Node);
                            if (!loop2100BNode.isMissingNode()) { // Ensure the LOOP2100B node exists
                                JsonNode prvNode = loop2100BNode.path("PRV");
                                if (!prvNode.isMissingNode()) { // Check if the PRV segment exists within LOOP2100B
                                    segmentProcessor.processEdiSegment("PRV", prvNode);
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
                    segmentProcessor.processEdiSegment("HL", hlNode);
                    if (!loop2000CNode.isMissingNode()) { // Ensure the LOOP2000C node exists
                        JsonNode trnNode = loop2000CNode.path("TRN");
                        if (!trnNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000B
                            segmentProcessor.processEdiSegment("TRN", trnNode);
                        }
                    }
                }
            }

            // Process LOOP2100C Segment
            JsonNode loop2100CNode = parentNode.path("LOOP2100C");
            if (!loop2100CNode.isMissingNode()) { // Ensure the LOOP2100C node exists
                JsonNode refNode = loop2100CNode.path("REF");
                if (!refNode.isMissingNode()) { // Check if the HL segment exists within LOOP2100C
                    segmentProcessor.processEdiSegment("REF", refNode);
                    if (!loop2100CNode.isMissingNode()) { // Ensure the LOOP2100C node exists
                        JsonNode dtpNode = loop2100CNode.path("DTP");
                        if (!dtpNode.isMissingNode()) { // Check if the HL segment exists within LOOP2100C
                            segmentProcessor.processEdiSegment("DTP", dtpNode);
                            if (!loop2100CNode.isMissingNode()) { // Ensure the LOOP2100C node exists
                                JsonNode nm1Node = loop2100CNode.path("NM1");
                                if (!nm1Node.isMissingNode()) { // Check if the HL segment exists within LOOP2100C
                                    segmentProcessor.processEdiSegment("NM1", nm1Node);
                                    if (!loop2100CNode.isMissingNode()) { // Ensure the LOOP2100C node exists
                                        JsonNode dmgNode = loop2100CNode.path("DMG");
                                        if (!dmgNode.isMissingNode()) { // Check if the HL segment exists within LOOP2100C
                                            segmentProcessor.processEdiSegment("DMG", dmgNode);
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
                    segmentProcessor.processEdiSegment("EQ", eqNode);
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

            // Process IEA Segment
            JsonNode ieaNode = parentNode.path("IEA");
            if (!ieaNode.isMissingNode()) { // Check if the IEA segment exists
                segmentProcessor.processEdiSegment("IEA", ieaNode);
            }
            return segmentProcessor.getGlobalEdiBuilderContent();
            
        } catch (Exception e) {
        	// Error Handling
            return "Error converting JSON to EDI 270";
        }
        finally {
            // Reset the globalEdiBuilder after processing the request
        	segmentProcessor.resetGlobalEdiBuilder();
        }
    }
}
