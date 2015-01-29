package org.restfulwhois.rdap.client.common.exception;

public enum ExceptionMessage {
	
	NOT_LEGAL_DTO_EXCEPTION(
			"The dto object is not a instance of "
			+ "IpDto, AutnumDto, DomainDto, EntityDto or NameserverDto."),
	
	DTO_TO_JSON_EXCEPTION(
			"Convert dto object to json error:\n"),		
			
	JSON_TO_DTO_EXCEPTION(
			"Convert json to dto object error:\n"),
	
	JSON_TO_UPDATERESPONSE_EXCEPTION(
			"Convert response json to UpdateResponse error:\n"),
	
	SET_CUSTOMPROPERTIES_EXCEPTION(
			"Set CustomProperties error:\n");
	
	String message;
	
	private ExceptionMessage(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
}
