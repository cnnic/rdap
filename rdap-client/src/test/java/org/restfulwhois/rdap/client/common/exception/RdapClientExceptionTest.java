package org.restfulwhois.rdap.client.common.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RdapClientExceptionTest{

	@Test
	public void test_message(){
		String message = null;
		try{
			throw new RdapClientException("test");
		}catch(RdapClientException e){
			message = e.getMessage();
		}
		assertEquals("test", message);
	}
}

