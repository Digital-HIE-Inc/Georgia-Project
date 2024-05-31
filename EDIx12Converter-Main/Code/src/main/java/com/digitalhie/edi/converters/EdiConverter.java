package com.digitalhie.edi.converters;


/**
 * The EdiConverter interface handles the conversion of JSON data
 * to Electronic Data Interchange (EDIx12) format.
 */

public interface EdiConverter {
	
	 /**
     * Converts JSON data to EDIx12 format.
     * 
     * @param title: The title is the transaction type of the EDI document.
     * @param decodedString: The JSON input data to be converted to EDIx12 format.
     * @return The EDIx12 representation of the JSON data.
     */
	
    String convertJsonToEdi(String title, String decodedString);
}
