package org.restfulwhois.rdap.client.core.query;


public class RdapQueryClient{
	/*@Autowired
	private ExchangeService service;
	
	
	
	public Ip ipQuery(String...param){
		return callService(ObjectType.ip, Ip.class, param);
	}
	
	public Autnum autnumQuery(String param){
		return callService(ObjectType.autnum, Autnum.class, param);
	}
	
	public Domain domainQuery(String param){
		return callService(ObjectType.domain, Domain.class, param);
	}
	
	public Entity entityQuery(String param){
		return callService(ObjectType.entity, Entity.class, param);
	}
	
	public Nameserver nameserverQuery(String param){
		return callService(ObjectType.nameserver, Nameserver.class, param);
	}
	
	public Help helpQuery(String param){
		return callService(ObjectType.help, Help.class, param);
	}
	
	private <T extends BaseModel> T callService(ObjectType objectType, Class<T> model, String... param) throws RdapException{
		String response;
		try {
			response = service.doQuery(arrayToList(param), objectType);
			return JsonUtil.toBean(response, model);
		} catch (IOException e) {
			RdapException re = new RdapException();
			//TODO
			//re.setErrorCode(e.g);
			re.setErrorMessage(e.getMessage());
			throw re;
		} catch (RestClientException e){
			RdapException re = new RdapException();
			//TODO
			//re.setErrorCode(e.g);
			re.setErrorMessage(e.getMessage());
			throw re;
		}
	}
	
	private List<String> arrayToList(String[] array){
		List<String> rtnList = new ArrayList<String>();
		if(array == null || array.length == 0 || array.length > 2){
			//TODO
		}else{
			if(array.length > 1){
				String a = array[0];
				String b = array[1];
				if(a.contains(":") || a.contains(".")){
					rtnList.add(a);
					rtnList.add(b);
				}else{
					rtnList.add(b);
					rtnList.add(a);
				}
			}else
				rtnList.add(array[0]);
		}
		return rtnList;
	}*/
}