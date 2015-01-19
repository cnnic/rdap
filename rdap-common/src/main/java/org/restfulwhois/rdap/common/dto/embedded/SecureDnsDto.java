package org.restfulwhois.rdap.common.dto.embedded;

import java.util.List;

public class SecureDnsDto{
	private boolean zoneSigned;
	private boolean delegationSigned;
	private long maxSigLife;
	private List<KeyDataDto> keyData;
	
}
