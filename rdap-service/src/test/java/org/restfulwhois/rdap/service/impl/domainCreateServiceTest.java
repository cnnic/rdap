package org.restfulwhois.rdap.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.UpdateResponse;
import org.restfulwhois.rdap.common.dto.embedded.DsDataDto;
import org.restfulwhois.rdap.common.dto.embedded.EntityHandleDto;
import org.restfulwhois.rdap.common.dto.embedded.EventDto;
import org.restfulwhois.rdap.common.dto.embedded.KeyDataDto;
import org.restfulwhois.rdap.common.dto.embedded.LinkDto;
import org.restfulwhois.rdap.common.dto.embedded.PublicIdDto;
import org.restfulwhois.rdap.common.dto.embedded.RemarkDto;
import org.restfulwhois.rdap.common.dto.embedded.SecureDnsDto;
import org.restfulwhois.rdap.common.dto.embedded.VariantDto;
import org.restfulwhois.rdap.common.dto.embedded.VariantNameDto;
import org.restfulwhois.rdap.common.model.Domain;
import org.restfulwhois.rdap.common.model.Domain.DomainType;
import org.restfulwhois.rdap.common.service.UpdateService;
import org.restfulwhois.rdap.common.util.UpdateValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class domainCreateServiceTest extends BaseTest {
    @Autowired
    @Qualifier("domainCreateServiceImpl")
    private UpdateService<DomainDto, Domain> createService;

    @Test
    public void test_invalid_unicodeName_exceedMaxLength() throws Exception {
        DomainDto dto = generateDomainDto();
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_UNICODENAME + 1);
        dto.setUnicodeName(stringExceedOneMoreChar);
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(4003, response.getSubErrorCode());
    }

    @Test
    public void test_invalid_status_exceedMaxLength() throws Exception {
        DomainDto dto = generateDomainDto();
        List<String> status = new ArrayList<String>();
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_STATUS + 1);
        status.add(stringExceedOneMoreChar);
        dto.setStatus(status);
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(4003, response.getSubErrorCode());
    }

    @Test
    public void test_invalid_variant_idnTable_exceedMaxLength()
            throws Exception {
        DomainDto dto = generateDomainDto();
        List<VariantDto> variantDto = new ArrayList<VariantDto>();
        dto.setVariants(variantDto);
        VariantDto v = new VariantDto();
        variantDto.add(v);
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_255 + 1);
        v.setIdnTable(stringExceedOneMoreChar);
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(4003, response.getSubErrorCode());
    }

    @Test
    public void test_invalid_variant_relation_exceedMaxLength()
            throws Exception {
        DomainDto dto = generateDomainDto();
        List<VariantDto> variantDto = new ArrayList<VariantDto>();
        dto.setVariants(variantDto);
        VariantDto v = new VariantDto();
        v.setIdnTable("idnTable");
        variantDto.add(v);
        List<String> relations = new ArrayList<String>();
        v.setRelation(relations);
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_255 + 1);
        relations.add(stringExceedOneMoreChar);
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(4003, response.getSubErrorCode());
    }

    @Test
    public void test_invalid_variant_ldhName_exceedMaxLength() throws Exception {
        DomainDto dto = generateDomainDto();
        List<VariantDto> variantDto = new ArrayList<VariantDto>();
        dto.setVariants(variantDto);
        VariantDto v = new VariantDto();
        v.setIdnTable("idnTable");
        variantDto.add(v);
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_LDHNAME + 1);
        List<VariantNameDto> variantNames = new ArrayList<VariantNameDto>();
        VariantNameDto name = new VariantNameDto();
        variantNames.add(name);
        name.setLdhName(stringExceedOneMoreChar);
        v.setVariantNames(variantNames);
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(4003, response.getSubErrorCode());
    }

    @Test
    public void test_invalid_variant_unicodeName_exceedMaxLength()
            throws Exception {
        DomainDto dto = generateDomainDto();
        List<VariantDto> variantDto = new ArrayList<VariantDto>();
        dto.setVariants(variantDto);
        VariantDto v = new VariantDto();
        v.setIdnTable("idnTable");
        variantDto.add(v);
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_UNICODENAME + 1);
        List<VariantNameDto> variantNames = new ArrayList<VariantNameDto>();
        VariantNameDto name = new VariantNameDto();
        variantNames.add(name);
        name.setLdhName("ldhName");
        name.setUnicodeName(stringExceedOneMoreChar);
        v.setVariantNames(variantNames);
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(4003, response.getSubErrorCode());
    }

    @Test
    public void test_invalid_secureDns_maxSigLife_exceedMin() throws Exception {
        DomainDto dto = generateDomainDto();
        SecureDnsDto secureDns = new SecureDnsDto();
        dto.setSecureDNS(secureDns);
        secureDns.setMaxSigLife(UpdateValidateUtil.MIN_VAL_FOR_INT_COLUMN - 1);
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(40010, response.getSubErrorCode());
    }

    @Test
    public void test_invalid_secureDns_maxSigLife_exceedMax() throws Exception {
        DomainDto dto = generateDomainDto();
        SecureDnsDto secureDns = new SecureDnsDto();
        dto.setSecureDNS(secureDns);
        secureDns.setMaxSigLife(UpdateValidateUtil.MAX_VAL_FOR_INT_COLUMN + 1);
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(40010, response.getSubErrorCode());
    }

    @Test
    public void test_invalid_secureDns_ds_keyTag_exceedMax() throws Exception {
        DomainDto dto = generateDomainDto();
        SecureDnsDto secureDns = new SecureDnsDto();
        dto.setSecureDNS(secureDns);
        List<DsDataDto> dsDatas = new ArrayList<DsDataDto>();
        secureDns.setDsData(dsDatas);
        DsDataDto dsData = new DsDataDto();
        dsDatas.add(dsData);
        dsData.setKeyTag(UpdateValidateUtil.MAX_VAL_FOR_INT_COLUMN + 1);
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(40010, response.getSubErrorCode());
    }

    @Test
    public void test_invalid_secureDns_ds_keyTag_exceedMin() throws Exception {
        DomainDto dto = generateDomainDto();
        SecureDnsDto secureDns = new SecureDnsDto();
        dto.setSecureDNS(secureDns);
        List<DsDataDto> dsDatas = new ArrayList<DsDataDto>();
        secureDns.setDsData(dsDatas);
        DsDataDto dsData = new DsDataDto();
        dsDatas.add(dsData);
        dsData.setKeyTag(UpdateValidateUtil.MIN_VAL_FOR_INT_COLUMN - 1);
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(40010, response.getSubErrorCode());
    }

    @Test
    public void test_invalid_secureDns_key_flags_exceedMin() throws Exception {
        DomainDto dto = generateDomainDto();
        SecureDnsDto secureDns = new SecureDnsDto();
        dto.setSecureDNS(secureDns);
        List<KeyDataDto> keyDatas = new ArrayList<KeyDataDto>();
        secureDns.setKeyData(keyDatas);
        KeyDataDto keyData = new KeyDataDto();
        keyDatas.add(keyData);
        keyData.setFlags(UpdateValidateUtil.MIN_VAL_FOR_INT_COLUMN - 1);
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(40010, response.getSubErrorCode());
    }

    @Test
    public void test_invalid_secureDns_key_flags_exceedMax() throws Exception {
        DomainDto dto = generateDomainDto();
        SecureDnsDto secureDns = new SecureDnsDto();
        dto.setSecureDNS(secureDns);
        List<KeyDataDto> keyDatas = new ArrayList<KeyDataDto>();
        secureDns.setKeyData(keyDatas);
        KeyDataDto keyData = new KeyDataDto();
        keyDatas.add(keyData);
        keyData.setFlags(UpdateValidateUtil.MAX_VAL_FOR_INT_COLUMN + 1);
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(40010, response.getSubErrorCode());
    }

    @Test
    public void test_invalid_entities_role_maxLength() throws Exception {
        DomainDto dto = generateDomainDto();
        List<EntityHandleDto> entities = new ArrayList<EntityHandleDto>();
        dto.setEntities(entities);
        EntityHandleDto entityHandle = new EntityHandleDto();
        entities.add(entityHandle);
        String stringMaxLength =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_255);
        List<String> roles = new ArrayList<String>();
        roles.add("okRole");
        roles.add(stringMaxLength);
        entityHandle.setRoles(roles);
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(0, response.getErrorCode());
    }

    @Test
    public void test_invalid_entities_role_exceedMaxLength() throws Exception {
        DomainDto dto = generateDomainDto();
        List<EntityHandleDto> entities = new ArrayList<EntityHandleDto>();
        dto.setEntities(entities);
        EntityHandleDto entityHandle = new EntityHandleDto();
        entities.add(entityHandle);
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_255 + 1);
        List<String> roles = new ArrayList<String>();
        roles.add("okRole");
        roles.add(stringExceedOneMoreChar);
        entityHandle.setRoles(roles);
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(4003, response.getSubErrorCode());
    }

    @Test
    public void test_invalid_publicId_identifier_exceedMaxLength()
            throws Exception {
        DomainDto dto = generateDomainDto();
        List<PublicIdDto> publicIds = new ArrayList<PublicIdDto>();
        dto.setPublicIds(publicIds);
        PublicIdDto publicId = new PublicIdDto();
        publicIds.add(publicId);
        publicId.setIdentifier(createStringWithLength(UpdateValidateUtil.MAX_LENGTH_255 + 1));
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(4003, response.getSubErrorCode());
    }

    @Test
    public void test_invalid_remark_title_exceedMaxLength() throws Exception {
        DomainDto dto = generateDomainDto();
        List<RemarkDto> remarks = new ArrayList<RemarkDto>();
        dto.setRemarks(remarks);
        RemarkDto remark = new RemarkDto();
        remarks.add(remark);
        remark.setTitle(createStringWithLength(UpdateValidateUtil.MAX_LENGTH_255 + 1));
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(4003, response.getSubErrorCode());
    }

    @Test
    public void test_invalid_link_title_exceedMaxLength() throws Exception {
        DomainDto dto = generateDomainDto();
        List<LinkDto> links = new ArrayList<LinkDto>();
        dto.setLinks(links);
        LinkDto link = new LinkDto();
        links.add(link);
        link.setTitle(createStringWithLength(UpdateValidateUtil.MAX_LENGTH_255 + 1));
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(4003, response.getSubErrorCode());
    }

    @Test
    public void test_invalid_event_action_exceedMaxLength() throws Exception {
        DomainDto dto = generateDomainDto();
        List<EventDto> events = new ArrayList<EventDto>();
        dto.setEvents(events);
        EventDto event = new EventDto();
        events.add(event);
        event.setEventAction(createStringWithLength(UpdateValidateUtil.MAX_LENGTH_255 + 1));
        UpdateResponse response = createService.execute(dto);
        assertNotNull(response);
        assertEquals(400, response.getErrorCode());
        assertEquals(4003, response.getSubErrorCode());
    }

    private DomainDto generateDomainDto() {
        DomainDto domain = new DomainDto();
        domain.setHandle("h1");
        domain.setLdhName("cnnic.cn");
        domain.setUnicodeName("cnnic.cn");
        domain.setPort43("port43");
        domain.setLang("zh");
        domain.setType(DomainType.DNR.getName());
        return domain;
    }

    private String createStringWithLength(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("1");
        }
        return sb.toString();
    }
}
