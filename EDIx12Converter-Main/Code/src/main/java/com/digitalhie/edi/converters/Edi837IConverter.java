package com.digitalhie.edi.converters;

import com.digitalhie.edi.EdiSegmentProcessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Converts JSON data representing an Institutional Claim Form (837I) into EDIx12 format.
 * The conversion process involves parsing the JSON data and generating corresponding EDIx12 segments.
 * The EDI segments are  ISA, GS, ST, BHT, LOOP1000A, LOOP1000B, LOOP2000A, LOOP2010AA, LOOP2000B, LOOP2010BA, LOOP2010BB, 
 * LOOP2300, LOOP2310A, LOOP2310B, LOOP2310C, LOOP2310E, LOOP2310F, LOOP2320, LOOP2330A, LOOP2330B, 
 * LOOP2400, LOOP2420D, LOOP2430, SE, GE, and IEA segments.
 * 
 * @author namrathag
 * @version 1.0
 */

public class Edi837IConverter implements EdiConverter {
	
	/** The segment processor responsible for processing individual EDI segments. */
	private final EdiSegmentProcessor segmentProcessor = new EdiSegmentProcessor();
	
	 /**
     * 837I (Institutional) Claim Form into EDIx12 format.
     * 
     * @param title: The title of the EDIx12 document.
     * @param decodedString: The decoded input JSON string representing the Claim form (837I).
     * @return The converted EDIx12 as a string.
     */
	
	@Override
	public String convertJsonToEdi(String title, String decodedString){
    	// EDI 837I conversion logic using EdiConverter
    	StringBuilder ediBuilder = new StringBuilder();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode parentNode = objectMapper.readTree(decodedString);

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

        // Process LOOP1000A Segment
        JsonNode loop1000ANode = parentNode.path("LOOP1000A");
        if (!loop1000ANode.isMissingNode()) { // Ensure the LOOP1000A node exists
            JsonNode nm1Node = loop1000ANode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP1000A
            	segmentProcessor.processEdiSegment("NM1", nm1Node);
            }
        }
        if (!loop1000ANode.isMissingNode()) { // Ensure the LOOP1000A node exists
            JsonNode perNode = loop1000ANode.path("PER");
            if (!perNode.isMissingNode()) { // Check if the PER segment exists within LOOP1000A
            	segmentProcessor.processEdiSegment("PER", perNode);
            }
        }

        // Process LOOP1000B Segment
        JsonNode loop1000BNode = parentNode.path("LOOP1000B");
        if (!loop1000BNode.isMissingNode()) { // Ensure the LOOP1000B node exists
            JsonNode nm1Node = loop1000BNode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP1000B
            	segmentProcessor.processEdiSegment("NM1", nm1Node);
            }
        }


        // Process LOOP2000A Segment
        JsonNode loop2000ANode = parentNode.path("LOOP2000A");
        if (!loop2000ANode.isMissingNode()) { // Ensure the LOOP2000A node exists
            JsonNode hlNode = loop2000ANode.path("HL");
            if (!hlNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000A
            	segmentProcessor.processEdiSegment("HL", hlNode);
            }
        }
        if (!loop2000ANode.isMissingNode()) { // Ensure the LOOP2000A node exists
            JsonNode prvNode = loop2000ANode.path("PRV");
            if (!prvNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000A
            	segmentProcessor.processEdiSegment("PRV", prvNode);
            }
        }

        // Process LOOP2010AA Segment
        JsonNode loop2010AANode = parentNode.path("LOOP2010AA");
        if (!loop2010AANode.isMissingNode()) { // Ensure the LOOP2010AA node exists
            JsonNode nm1Node = loop2010AANode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2010AA
            	segmentProcessor.processEdiSegment("NM1", nm1Node);
                if (!loop2010AANode.isMissingNode()) { // Ensure the LOOP2010AA node exists
                    JsonNode n3Node = loop2010AANode.path("N3");
                    if (!n3Node.isMissingNode()) { // Check if the N3 segment exists within LOOP2010AA
                    	segmentProcessor.processEdiSegment("N3", n3Node);
                        if (!loop2010AANode.isMissingNode()) { // Ensure the LOOP2010AA node exists
                            JsonNode n4Node = loop2010AANode.path("N4");
                            if (!n4Node.isMissingNode()) { // Check if the N4 segment exists within LOOP2010AA
                            	segmentProcessor.processEdiSegment("N4", n4Node);
                                if (!loop2010AANode.isMissingNode()) { // Ensure the LOOP node2010AA exists
                                    JsonNode refNode = loop2010AANode.path("REF");
                                    if (!refNode.isMissingNode()) { // Check if the PRV segment exists within LOOP2010AA
                                    	segmentProcessor.processEdiSegment("REF", refNode);
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
            	segmentProcessor.processEdiSegment("HL", hlNode);
                if (!loop2000BNode.isMissingNode()) { // Ensure the LOOP2000C node exists
                    JsonNode sbrNode = loop2000BNode.path("SBR");
                    if (!sbrNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000B
                    	segmentProcessor.processEdiSegment("SBR", sbrNode);
                    }
                }
            }
        }

        // Process LOOP2010BASegment
        JsonNode loop2010BANode = parentNode.path("LOOP2010BA");
        if (!loop2010BANode.isMissingNode()) { // Ensure the LOOP2010BA node exists
            JsonNode nm1Node = loop2010BANode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the HL segment exists within LOOP2010BB
            	segmentProcessor.processEdiSegment("NM1", nm1Node);
                if (!loop2010BANode.isMissingNode()) { // Ensure the LOOP2000C node exists
                    JsonNode dmgNode = loop2010BANode.path("DMG");
                    if (!dmgNode.isMissingNode()) { // Check if the HL segment exists within LOOP2010BB
                    	segmentProcessor.processEdiSegment("DMG", dmgNode);
                    }
                }
            }
        }

        // Process LOOP2010BBSegment
        JsonNode loop2010BBNode = parentNode.path("LOOP2010BB");
        if (!loop2010BBNode.isMissingNode()) { // Ensure the LOOP2000B node exists
            JsonNode nm1Node = loop2010BBNode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2010BB
            	segmentProcessor.processEdiSegment("NM1", nm1Node);
                if (!loop2010BBNode.isMissingNode()) { // Ensure the LOOP2000C node exists
                    JsonNode n4Node = loop2010BBNode.path("N4");
                    if (!n4Node.isMissingNode()) { // Check if the HL segment exists within LOOP2000B
                    	segmentProcessor.processEdiSegment("N4", n4Node);
                        if (!loop2010BBNode.isMissingNode()) { // Ensure the LOOP2000C node exists
                            JsonNode refNode = loop2010BBNode.path("REF");
                            if (!refNode.isMissingNode()) { // Check if the HL segment exists within LOOP2000B
                            	segmentProcessor.processEdiSegment("REF", refNode);
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

                segmentProcessor.appendToGlobalVariable(clmOutput);
            }
            // Process DTP Segment
            JsonNode dtpNode = loop2300Node.path("DTP");
            if (!dtpNode.isMissingNode()) { // Check if the DTP segment exists within LOOP2300
            	segmentProcessor.processEdiSegment("DTP", dtpNode);
            }
            // Process CL1 Segment
            JsonNode cl1Node = loop2300Node.path("CL1");
            if (!cl1Node.isMissingNode()) { // Check if the CL1 segment exists within LOOP2300
            	segmentProcessor.processEdiSegment("CL1", cl1Node);
            }
            // Process CN1 Segment
            JsonNode cn1Node = loop2300Node.path("CN1");
            if (!cn1Node.isMissingNode()) { // Check if the CN1 segment exists within LOOP2300
            	segmentProcessor.processEdiSegment("CN1", cn1Node);
            }
            // Process REF Segment
            JsonNode refNode = loop2300Node.path("REF");
            if (!refNode.isMissingNode()) { // Check if the REF segment exists within LOOP2300
            	segmentProcessor.processEdiSegment("REF", refNode);
            }
            // Process HI Segment
            JsonNode hiNode = loop2300Node.path("HI");
            if (!hiNode.isMissingNode()) { // Check if the HI segment exists within LOOP2300
                String hiOutput = String.format("HI*%s:%s~", hiNode.path("HI01-1").asText(), hiNode.path("HI01-2").asText());
                segmentProcessor.appendToGlobalVariable(hiOutput);
            }
        }



        // Process LOOP2310A Segment
        JsonNode loop2310ANode = parentNode.path("LOOP2310A");
        if (!loop2310ANode.isMissingNode()) { // Ensure the LOOP2310A node exists
            JsonNode nm1Node = loop2310ANode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2310A
            	segmentProcessor.processEdiSegment("NM1", nm1Node);
                if (!loop2310ANode.isMissingNode()) { // Ensure the LOOP2310A node exists
                    JsonNode n3Node = loop2310ANode.path("N3");
                    if (!n3Node.isMissingNode()) { // Check if the N3 segment exists within LOOP2310A
                    	segmentProcessor.processEdiSegment("N3", n3Node);
                        if (!loop2310ANode.isMissingNode()) { // Ensure the LOOP2310A node exists
                            JsonNode n4Node = loop2310ANode.path("N4");
                            if (!n4Node.isMissingNode()) { // Check if the N4 segment exists within LOOP2310A
                            	segmentProcessor.processEdiSegment("N4", n4Node);
                                if (!loop2310ANode.isMissingNode()) { // Ensure the LOOP2310A node exists
                                    JsonNode prvNode = loop2310ANode.path("PRV");
                                    if (!prvNode.isMissingNode()) { // Check if the PRV segment exists within LOOP2310A
                                    	segmentProcessor.processEdiSegment("PRV", prvNode);
                                        if (!loop2310ANode.isMissingNode()) { // Ensure the LOOP2310A node exists
                                            JsonNode refNode = loop2310ANode.path("REF");
                                            if (!refNode.isMissingNode()) { // Check if the REF segment exists within LOOP2310A
                                            	segmentProcessor.processEdiSegment("REF", refNode);
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
            	segmentProcessor.processEdiSegment("NM1", nm1Node);
            }
        }

        // Process LOOP2310C Segment
        JsonNode loop2310CNode = parentNode.path("LOOP2310C");
        if (!loop2310CNode.isMissingNode()) { // Ensure the LOOP2310C node exists
            JsonNode nm1Node = loop2310CNode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2310C
            	segmentProcessor.processEdiSegment("NM1", nm1Node);
            }
        }

        // Process LOOP2310E Segment
        JsonNode loop2310ENode = parentNode.path("LOOP2310E");
        if (!loop2310ENode.isMissingNode()) { // Ensure the LOOP2310E node exists
            JsonNode nm1Node = loop2310ENode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2310E
            	segmentProcessor.processEdiSegment("NM1", nm1Node);
                if (!loop2310ENode.isMissingNode()) { // Ensure the LOOP2310E node exists
                    JsonNode n3Node = loop2310ENode.path("N3");
                    if (!n3Node.isMissingNode()) { // Check if the N3 segment exists within LOOP2310E
                    	segmentProcessor.processEdiSegment("N3", n3Node);
                        if (!loop2310ENode.isMissingNode()) { // Ensure the LOOP2310E node exists
                            JsonNode n4Node = loop2310ENode.path("N4");
                            if (!n4Node.isMissingNode()) { // Check if the N4 segment exists within LOOP2310E
                            	segmentProcessor.processEdiSegment("N4", n4Node);
                               if (!loop2310ENode.isMissingNode()) { // Ensure the LOOP2310E node exists
                                            JsonNode refNode = loop2310ENode.path("REF");
                                            if (!refNode.isMissingNode()) { // Check if the REF segment exists within LOOP2310E
                                            	segmentProcessor.processEdiSegment("REF", refNode);
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
            	segmentProcessor.processEdiSegment("NM1", nm1Node);
                if (!loop2310FNode.isMissingNode()) { // Ensure the LOOP2310E node exists
                    JsonNode n3Node = loop2310FNode.path("N3");
                    if (!n3Node.isMissingNode()) { // Check if the N3 segment exists within LOOP2310F
                    	segmentProcessor.processEdiSegment("N3", n3Node);
                        if (!loop2310FNode.isMissingNode()) { // Ensure the LOOP2310F node exists
                            JsonNode n4Node = loop2310FNode.path("N4");
                            if (!n4Node.isMissingNode()) { // Check if the N4 segment exists within LOOP2310F
                            	segmentProcessor.processEdiSegment("N4", n4Node);
                                if (!loop2310FNode.isMissingNode()) { // Ensure the LOOP2310F node exists
                                    JsonNode refNode = loop2310FNode.path("REF");
                                    if (!refNode.isMissingNode()) { // Check if the REF segment exists within LOOP2310F
                                    	segmentProcessor.processEdiSegment("REF", refNode);
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
            	segmentProcessor.processEdiSegment("SBR", sbrNode);
                if (!loop2320Node.isMissingNode()) { // Ensure the LOOP2320 node exists
                    JsonNode casNode = loop2320Node.path("CAS");
                    if (!casNode.isMissingNode()) { // Check if the CAS segment exists within LOOP2320
                    	segmentProcessor.processEdiSegment("CAS", casNode);
                        if (!loop2320Node.isMissingNode()) { // Ensure the LOOP2320 node exists
                            JsonNode amtNode = loop2320Node.path("AMT");
                            if (!amtNode.isMissingNode()) { // Check if the AMT segment exists within LOOP2320
                            	segmentProcessor.processEdiSegment("AMT", amtNode);
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
                segmentProcessor.processEdiSegment("NM1", nm1Node);
            }
        }

        // Process LOOP2330B Segment
        JsonNode loop2330BNode = parentNode.path("LOOP2330B");
        if (!loop2330BNode.isMissingNode()) { // Ensure the LOOP2330B node exists
            JsonNode nm1Node = loop2330BNode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the AMT segment exists within LOOP2330B
                segmentProcessor.processEdiSegment("NM1", nm1Node);
              if (!loop2330BNode.isMissingNode()) { // Ensure the LOOP2330B node exists
                    JsonNode dtpNode = loop2330BNode.path("DTP");
                    if (!nm1Node.isMissingNode()) { // Check if the DTP segment exists within LOOP2330B
                        segmentProcessor.processEdiSegment("DTP", dtpNode);
                    }
                }
            }
        }


        // Process LOOP2400 Segment
        JsonNode loop2400Node = parentNode.path("LOOP2400");
        if (!loop2400Node.isMissingNode()) { // Ensure the LOOP2400 node exists
            JsonNode lxNode = loop2400Node.path("LX");
            if (!lxNode.isMissingNode()) { // Check if the AMT segment exists within LOOP2400
                segmentProcessor.processEdiSegment("LX", lxNode);
                if (!loop2400Node.isMissingNode()) { // Ensure the LOOP2400 node exists
                    JsonNode sv2Node = loop2400Node.path("SV2");
                    if (!sv2Node.isMissingNode()) { // Check if the SV2 segment exists within LOOP2400
                        segmentProcessor.processEdiSegment("SV2", sv2Node);
                    }
                }
            }
        }


        // Process LOOP2420D Segment
        JsonNode loop2420DNode = parentNode.path("LOOP2420D");
        if (!loop2420DNode.isMissingNode()) { // Ensure the LOOP2420D node exists
            JsonNode nm1Node = loop2420DNode.path("NM1");
            if (!nm1Node.isMissingNode()) { // Check if the NM1 segment exists within LOOP2420D
                segmentProcessor.processEdiSegment("NM1", nm1Node);
                if (!loop2420DNode.isMissingNode()) { // Ensure the LOOP2420D node exists
                    JsonNode refNode = loop2420DNode.path("REF");
                    if (!refNode.isMissingNode()) { // Check if the REF segment exists within LOOP2420D
                        segmentProcessor.processEdiSegment("REF", refNode);
                    }
                }
            }
        }

        // Process LOOP2430 Segment
        JsonNode loop2430Node = parentNode.path("LOOP2430");
        if (!loop2430Node.isMissingNode()) { // Ensure the LOOP2430 node exists
            JsonNode svdNode = loop2430Node.path("SVD");
            if (!svdNode.isMissingNode()) { // Check if the SVD segment exists within LOOP2430
                segmentProcessor.processEdiSegment("SVD", svdNode);
                if (!loop2430Node.isMissingNode()) { // Ensure the LOOP2430 node exists
                    JsonNode dtpNode = loop2430Node.path("DTP");
                    if (!dtpNode.isMissingNode()) { // Check if the DTP segment exists within LOOP2430
                        segmentProcessor.processEdiSegment("DTP", dtpNode);
                    }
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

        // Process IEA Segment
        JsonNode ieaNode = parentNode.path("IEA");
        if (!ieaNode.isMissingNode()) { // Check if the IEA segment exists
            segmentProcessor.processEdiSegment("IEA", ieaNode);
        }
         return segmentProcessor.getGlobalEdiBuilderContent();
    } catch (Exception e) {
    	// Error Handling
        return "Error converting JSON to EDI 837I";
    }
    finally {
        // Reset the globalEdiBuilder after processing the request
    	segmentProcessor.resetGlobalEdiBuilder();
    }
}
   
}
