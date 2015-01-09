package org.restfulwhois.rdap.client.common.model;

public class DelegationKey{
	private int algorithm;
	private String digest;
	private int digestType;
	private long keyTag;
	public int getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(int algorithm) {
		this.algorithm = algorithm;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public int getDigestType() {
		return digestType;
	}
	public void setDigestType(int digestType) {
		this.digestType = digestType;
	}
	public long getKeyTag() {
		return keyTag;
	}
	public void setKeyTag(long keyTag) {
		this.keyTag = keyTag;
	}
	
	
}