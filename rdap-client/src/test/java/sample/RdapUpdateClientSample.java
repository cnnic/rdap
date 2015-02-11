package sample;

import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.client.RdapUpdateClient;
import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.dto.UpdateResponse;
import org.restfulwhois.rdap.common.dto.embedded.RemarkDto;

public class RdapUpdateClientSample{
    
    public UpdateResponse createSample(){
        String url = "http://www.sample.com";
        RdapUpdateClient client = new RdapUpdateClient(url);
        client.setConnectTimeout(6000);
        client.setReadTimeout(150000);
        
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
    
    public UpdateResponse updateSample(){
        String url = "http://www.sample.com";
        RdapUpdateClient client = new RdapUpdateClient(url);
        client.setConnectTimeout(6000);
        client.setReadTimeout(150000);
        
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
    
    public UpdateResponse deleteSample(){
        String url = "http://www.sample.com";
        RdapUpdateClient client = new RdapUpdateClient(url);
        client.setConnectTimeout(6000);
        client.setReadTimeout(150000);
        
        UpdateResponse response;
        try {
            response = client.deleteIp("ip-1");
        } catch (RdapClientException e) {
            response = null;
        }
        return response;
    }
}