alter table RDAP_ARPA drop column HANDLE;
alter table RDAP_ARPA drop column ARPA_NAME;
alter table RDAP_ARPA drop column PORT43;
alter table RDAP_ARPA drop column LANG;
alter table REL_DOMAIN_NAMESERVER drop column DOMAIN_TYPE;
alter table RDAP_SECUREDNS drop column DOMAIN_TYPE;
alter table RDAP_DOMAIN add column TYPE varchar(10) default 'dnr';
alter table RDAP_DOMAIN add column NETWORK_ID int(10);
drop table RDAP_ARPA_STATUS;
drop table RDAP_ARPA;

drop index IDX_IP_STARTLOWADDRESS on RDAP_IP;
alter table RDAP_IP drop column STARTLOWADDRESS;
alter table RDAP_IP drop column STARTHIGHADDRESS;
alter table RDAP_IP drop column ENDLOWADDRESS;
alter table RDAP_IP drop column ENDHIGHADDRESS;
alter table RDAP_IP add column STARTADDRESS varbinary(16) not null after HANDLE;
alter table RDAP_IP add column ENDADDRESS varbinary(16) not null after HANDLE;

alter table RDAP_IP_REDIRECT drop column STARTLOWADDRESS;
alter table RDAP_IP_REDIRECT drop column STARTHIGHADDRESS;
alter table RDAP_IP_REDIRECT drop column ENDLOWADDRESS;
alter table RDAP_IP_REDIRECT drop column ENDHIGHADDRESS;
alter table RDAP_IP_REDIRECT add column STARTADDRESS varbinary(16) not null after RDAP_IP_REDIRECT_ID;
alter table RDAP_IP_REDIRECT add column ENDADDRESS varbinary(16) not null after RDAP_IP_REDIRECT_ID;

drop index IDX_NAMESERVER_IP_IP on RDAP_NAMESERVER_IP;
alter table RDAP_NAMESERVER_IP drop column IP_HIGH;
alter table RDAP_NAMESERVER_IP drop column IP_LOW;
alter table RDAP_NAMESERVER_IP add column IP varbinary(16) not null after NAMESERVER_ID;