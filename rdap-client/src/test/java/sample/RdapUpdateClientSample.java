package sample;

import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.client.RdapUpdateClient;
import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.service.RdapClientConfig;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.dto.UpdateResponse;
import org.restfulwhois.rdap.common.dto.embedded.RemarkDto;

public class RdapUpdateClientSample {
    public RdapClientConfig config(){
        String url = "http://www.sample.com";
        RdapClientConfig config = new RdapClientConfig(url);
        config.setConnectTimeout(10000);
        config.setReadTimeout(10000);
        return config;
    }

    public UpdateResponse createSample() {
        RdapUpdateClient client = new RdapUpdateClient(config());

        IpDto ipDto = new IpDto();
        ipDto.setHandle("ip-1");
        ipDto.setStartAddress("192.168.1.1");
        ipDto.setEndAddress("192.168.1.255");
        List<RemarkDto> remarks = new ArrayList<RemarkDto>();
        RemarkDto remarkDto = new RemarkDto();
        remarkDto.setHandle("remark-1");
        remarks.add(remarkDto);
        ipDto.setRemarks(remarks);

        UpdateResponse response;
        try {
            response = client.create(ipDto);
        } catch (RdapClientException e) {
            response = null;
        }
        return response;
    }

    public UpdateResponse updateSample() {
        RdapUpdateClient client = new RdapUpdateClient(config());

        IpDto ipDto = new IpDto();
        ipDto.setHandle("ip-1");
        ipDto.setStartAddress("0000::0001");
        ipDto.setEndAddress("0000:ffff");
        ipDto.setIpVersion("v6");
        List<RemarkDto> remarks = new ArrayList<RemarkDto>();
        RemarkDto remarkDto = new RemarkDto();
        remarkDto.setHandle("remark-1");
        remarks.add(remarkDto);
        ipDto.setRemarks(remarks);

        UpdateResponse response;
        try {
            response = client.update(ipDto);
        } catch (RdapClientException e) {
            response = null;
        }
        return response;
    }

    public UpdateResponse deleteSample() {
        RdapUpdateClient client = new RdapUpdateClient(config());

        UpdateResponse response;
        try {
            response = client.deleteIp("ip-1");
        } catch (RdapClientException e) {
            response = null;
        }
        return response;
    }
}