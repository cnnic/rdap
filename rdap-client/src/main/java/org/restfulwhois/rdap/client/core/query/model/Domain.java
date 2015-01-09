package org.restfulwhois.rdap.client.core.query.model;

import java.util.List;

import org.restfulwhois.rdap.client.common.model.BaseModel;
import org.restfulwhois.rdap.client.common.model.DelegationKey;
import org.restfulwhois.rdap.client.common.model.Variant;

public class Domain extends BaseModel{
	private String ldhName;
	private String unicodeName;
	private List<Variant> variants;
	private List<Nameserver> nameServers;
	private List<DelegationKey> delegationKeys;
	private Ip network;
	public String getLdhName() {
		return ldhName;
	}
	public void setLdhName(String ldhName) {
		this.ldhName = ldhName;
	}
	public String getUnicodeName() {
		return unicodeName;
	}
	public void setUnicodeName(String unicodeName) {
		this.unicodeName = unicodeName;
	}
	public List<Variant> getVariants() {
		return variants;
	}
	public void setVariants(List<Variant> variants) {
		this.variants = variants;
	}
	public List<Nameserver> getNameServers() {
		return nameServers;
	}
	public void setNameServers(List<Nameserver> nameServers) {
		this.nameServers = nameServers;
	}
	public List<DelegationKey> getDelegationKeys() {
		return delegationKeys;
	}
	public void setDelegationKeys(List<DelegationKey> delegationKeys) {
		this.delegationKeys = delegationKeys;
	}
	public Ip getNetwork() {
		return network;
	}
	public void setNetwork(Ip network) {
		this.network = network;
	}
}