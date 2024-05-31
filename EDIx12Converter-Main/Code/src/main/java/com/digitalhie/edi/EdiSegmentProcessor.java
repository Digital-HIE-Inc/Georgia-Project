package com.digitalhie.edi;

import com.digitalhie.edi.converters.EdiConverter;
import com.digitalhie.edi.converters.EdiConverterCase;
import com.fasterxml.jackson.databind.JsonNode;


/**
 * The EdiSegmentProcessor class provides functionality to process EDI segments and convert JSON data to EDI format.
 * 
 * Consists common functions used across the converters
 */
public class EdiSegmentProcessor {

	 /**
     * The StringBuilder used to build the global EDI output.
     */
    private final StringBuilder globalEdiBuilder = new StringBuilder();
    
    /**
     * The delimiter(~) used to separate segments in the EDI output.
     */
    private static final String SEGMENT_DELIMITER = "~";
    
    /**
     * The delimiter(*) used to separate elements within a segment in the EDI output.
     */
    private static final String ELEMENT_DELIMITER = "*";
    
    /**
     * The EdiConverter used to convert JSON data format to EDIx12 format.
     * The EdiConverterCase checks the title for the corresponding transaction set to be converted.
     */
    private final EdiConverter ediConverter = new EdiConverterCase(); // Ensure EdiConverter is properly imported
    
    /**
     * Converts JSON data to EDIx12 format.
     * 
     * @param title: The transaction type of the EDI document.
     * @param decodedString: The input JSON data to be converted to EDIx12 format.
     * @return The converted EDIx12 string.
     */
    public String convertJsonToEdi(String title, String decodedString) {
        return ediConverter.convertJsonToEdi(title, decodedString);
    }
    
    /**
     * Processes an EDIx12 segment by building the segment and appending it to the global EDIx12 output.
     * 
     * @param segmentName: The name of the segment.
     * @param segmentNode: The JSON node representing the segment.
     */
    public void processEdiSegment(String segmentName, JsonNode segmentNode) {
        StringBuilder segmentBuilder = new StringBuilder(segmentName);
        segmentNode.fields().forEachRemaining(field -> appendFieldValues(segmentBuilder, field.getValue()));
        segmentBuilder.append(SEGMENT_DELIMITER);
        appendToGlobalVariable(segmentBuilder.toString());
    }

    /**
     * Recursively appends field values to a segment builder.
     * 
     * @param segmentBuilder: The StringBuilder used to build the segment.
     * @param node: The JSON node containing the field values.
     */
    public void appendFieldValues(StringBuilder segmentBuilder, JsonNode node) {
        if (node.isObject()) {
            node.fields().forEachRemaining(field -> appendFieldValues(segmentBuilder, field.getValue()));
        } else {
            segmentBuilder.append(ELEMENT_DELIMITER).append(node.asText());
        }
    }

    /**
     * Resets the global EDI builder by setting its length to zero.
     */
    public void resetGlobalEdiBuilder() {
        globalEdiBuilder.setLength(0);
    }

    /**
     * Appends the specified output to the global EDI builder, followed by a newline character.
     * 
     * @param output The output to be appended to the global EDI builder.
     * [Implemented for segments that have ":" instead of "*" as element delimiter]
     */
    public void appendToGlobalVariable(String output) {
        globalEdiBuilder.append(output).append("\n");
    }
    
    /**
     * Retrieves the content of the global EDI builder.
     * 
     * @return Returns the content of the global EDI builder as a string.
     */
    public String getGlobalEdiBuilderContent() {
        return globalEdiBuilder.toString();
    }
}
