package org.restfulwhois.rdap.common.dto.embedded;

import java.util.List;

import org.restfulwhois.rdap.common.dto.BaseDto;

/**
 * VariantDto.
 * 
 * @author jiashuo.
 * 
 */
public class VariantDto extends BaseDto {
    /**
     * relation.
     */
    private List<String> relation;
    /**
     * idnTable.
     */
    private String idnTable;
    /**
     * variantNames.
     */
    private List<VariantNameDto> variantNames;

    /**
     * get relation.
     * 
     * @return relation.
     */
    public List<String> getRelation() {
        return relation;
    }

    /**
     * set relation.
     * 
     * @param relation
     *            relation.
     */
    public void setRelation(List<String> relation) {
        this.relation = relation;
    }

    /**
     * get idnTable.
     * 
     * @return idnTable.
     */
    public String getIdnTable() {
        return idnTable;
    }

    /**
     * set idnTable.
     * 
     * @param idnTable
     *            idnTable.
     */
    public void setIdnTable(String idnTable) {
        this.idnTable = idnTable;
    }

    /**
     * get variantNames.
     * 
     * @return variantNames.
     */
    public List<VariantNameDto> getVariantNames() {
        return variantNames;
    }

    /**
     * set variantNames.
     * 
     * @param variantNames
     *            variantNames.
     */
    public void setVariantNames(List<VariantNameDto> variantNames) {
        this.variantNames = variantNames;
    }

}
