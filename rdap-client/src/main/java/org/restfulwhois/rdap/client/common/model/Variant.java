package org.restfulwhois.rdap.client.common.model;

import java.util.List;

public class Variant{
	private String[] relation;
	private List<VariantName> variantNames;
	public String[] getRelation() {
		return relation;
	}
	public void setRelation(String[] relation) {
		this.relation = relation;
	}
	public List<VariantName> getVariantNames() {
		return variantNames;
	}
	public void setVariantNames(List<VariantName> variantNames) {
		this.variantNames = variantNames;
	}
}