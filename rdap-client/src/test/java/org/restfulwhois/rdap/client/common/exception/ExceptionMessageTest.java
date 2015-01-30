package org.restfulwhois.rdap.client.common.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExceptionMessageTest{
	
	@Test
	public void test_getMessage_legal(){
		String not_legal_dto_error = "The dto object is not a instance of "
				+ "IpDto, AutnumDto, DomainDto, EntityDto or NameserverDto.";
		assertEquals(not_legal_dto_error, 
				ExceptionMessage.NOT_LEGAL_DTO_ERROR.getMessage());
	}
	
	@Test
	public void test_getMessage_dtoToJson(){
		String dto_to_json_error = "Convert dto object to json error:\n";
		assertEquals(dto_to_json_error, 
				ExceptionMessage.DTO_TO_JSON_ERROR.getMessage());
	}
	
	@Test
	public void test_getMessage_jsonToDto(){
		String json_to_dto_error = "Convert json to dto object error:\n";
		assertEquals(json_to_dto_error, 
				ExceptionMessage.JSON_TO_DTO_ERROR.getMessage());
	}
	
	@Test
	public void test_getMessage_jsonToResponse(){
		String json_to_updateresponse_error = "Convert response json "
				+ "to UpdateResponse error:\n";
		assertEquals(json_to_updateresponse_error, 
				ExceptionMessage.JSON_TO_UPDATERESPONSE_ERROR.getMessage());
	}
	
	@Test
	public void test_getMessage_custom(){
		String set_customproperties_error = "Set CustomProperties error:\n";
		assertEquals(set_customproperties_error, 
				ExceptionMessage.SET_CUSTOMPROPERTIES_ERROR.getMessage());
	}
}