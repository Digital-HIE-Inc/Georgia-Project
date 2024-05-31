package com.digitalhie.edi.converters;

/**
 * The EdiConverterCase class provides a mechanism to dynamically 
 * choose the appropriate transaction set to be converted
 * based on the title and the input JSON to EDIx12 format implementing 
 * the respective converter.
 */

public class EdiConverterCase implements EdiConverter {

 /**
 * Converts JSON data to the corresponding EDIx12 format based on the specified title.
 * @param title: The title indicating the transaction type of EDIx12 format to convert to.
 * @param decodedString: The input JSON data to convert to EDIx12 format.
 * @return The converted EDIx12 format string.
 * @throws IllegalArgumentException If an invalid title is provided.
*/
    @Override
    public String convertJsonToEdi(String title, String decodedString) {
        switch (title) {
            case "270":
                return new Edi270Converter().convertJsonToEdi(title, decodedString);
            case "837P":
                return new Edi837PConverter().convertJsonToEdi(title, decodedString);
            case "837I":
                return new Edi837IConverter().convertJsonToEdi(title, decodedString);
            default:
                throw new IllegalArgumentException("Invalid title");
        }
    }
}
