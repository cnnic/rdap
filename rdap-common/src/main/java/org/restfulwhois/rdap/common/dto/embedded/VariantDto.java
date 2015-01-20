package org.restfulwhois.rdap.common.dto.embedded;

import java.util.List;

import org.restfulwhois.rdap.common.dto.BaseDto;

public class VariantDto extends BaseDto{
	private List<String> relation;
	private String idnTable;
	private List<VariantNameDto> variantNames;
	public List<String> getRelation() {
		return relation;
	}
	public void setRelation(List<String> relation) {
		this.relation = relation;
	}
	public String getIdnTable() {
		return idnTable;
	}
	public void setIdnTable(String idnTable) {
		this.idnTable = idnTable;
	}
	public List<VariantNameDto> getVariantNames() {
		return variantNames;
	}
	public void setVariantNames(List<VariantNameDto> variantNames) {
		this.variantNames = variantNames;
	}
	
	
}
