package org.restfulwhois.rdap.client.core.update.dto.embedded;

import java.util.List;

public class SecureDnsDto{
	private boolean zoneSigned;
	private boolean delegationSigned;
	private long maxSigLife;
	private List<KeyDataDto> keyData;
}