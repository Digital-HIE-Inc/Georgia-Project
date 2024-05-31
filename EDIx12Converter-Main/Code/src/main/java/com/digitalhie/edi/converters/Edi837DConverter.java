package com.digitalhie.edi.converters;

import com.digitalhie.edi.EdiSegmentProcessor;

public class Edi837DConverter implements EdiConverter {
	
	/** The segment processor responsible for processing individual EDI segments. */
	private final EdiSegmentProcessor segmentProcessor = new EdiSegmentProcessor();
	
	 /**
     * 837D (Dental) Claim Form into EDIx12 format.
     * 
     * @param title: The title of the EDIx12 document.
     * @param decodedString: The decoded input JSON string representing the Claim form (837D).
     * @return The converted EDIx12 as a string.
     */
	@Override
	public String convertJsonToEdi(String title, String decodedString) {
		// TODO Auto-generated method stub
		return null;
	}

}
