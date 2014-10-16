USE `rdap`;

#
# Data for table "RDAP_AUTNUM"
#

INSERT INTO `RDAP_AUTNUM` (`AS_ID`,`HANDLE`,`START_AUTNUM`,`END_AUTNUM`,`NAME`,`TYPE`,`COUNTRY`,`LANG`,`PORT43`) VALUES (1,'as-1',62464,63487,'as-1:62464~63487','DIRECT ALLOCATION','CN','en','cnnic.cn'),(2,'as-2',1,19,'as-2:1~19','DIRECT ALLOCATION','CN','en','cnnic.cn'),(3,'as-3',9981,9981,'as-3:9981','DIRECT ALLOCATION','CN','en','cnnic.cn'),(4,'as-4',31,31,'as-4:31','DIRECT ALLOCATION','CN','en','cnnic.cn'),(5,'as-5',20,30,'as-5:20~30','DIRECT ALLOCATION','CN','en','cnnic.cn'),(6,'as-6',167295,167295,'as-6:167295','DIRECT ALLOCATION','CN','en','cnnic.cn');

#
# Data for table "RDAP_AUTNUM_REDIRECT"
#

INSERT INTO `RDAP_AUTNUM_REDIRECT` (`AS_REDIRECT_ID`,`START_AUTNUM`,`END_AUTNUM`,`REDIRECT_URL`) VALUES (1,31,100,'https://whois.cn/rdap/.well-known/rdap'),(2,101,200,'https://whois.cn/rdap/.well-known/rdap');

#
# Data for table "RDAP_AUTNUM_STATUS"
#

INSERT INTO `RDAP_AUTNUM_STATUS` (`AS_STATUS_ID`,`AS_ID`,`STATUS`) VALUES (1,1,'validated'),(2,2,'transfer prohibited'),(3,3,'private'),(4,4,'associated'),(5,5,'active'),(6,6,'pending update');

#
# Data for table "RDAP_CONFORMANCE"
#

INSERT INTO `RDAP_CONFORMANCE` (`CONFORMANCE_ID`,`RDAP_CONFORMANCE`) VALUES (1,'rdap_level_0');

#
# Data for table "RDAP_DOMAIN"
#

INSERT INTO `RDAP_DOMAIN` (`DOMAIN_ID`,`HANDLE`,`LDH_NAME`,`UNICODE_NAME`,`PORT43`,`LANG`,`TYPE`,`NETWORK_ID`) VALUES (1,'dt-1','xn--123123.cn','xn--123123.cn','example.com','en','dnr',NULL),(2,'dt-2','cnnic.cn','cnnic.cn','whois.example.cn','en','dnr',NULL),(3,'dt-3','xn--fiqa61au8b7zsevnm8ak20mc4a87e.cn','中国互联网络信息中心.cn','cnnic.cn','en','dnr',NULL),(4,'dt-4','xn--elaaaa.xn--fiqs8s','ȅȅȅȅ.中国','cnnic.cn','en','dnr',NULL),(5,'dt-5','xn--fiq8iy4u6s7b8bb.cn','中国互联网.cn','whois.example.cn','en','dnr',NULL),(6,'dt-6','example.cn','example.cn','whois.example.cn','en','dnr',NULL),(7,'dt-7','xn--0zwm56d.xn--fiqs8s','测试.中国','cnnic.cn','en','dnr',NULL),(8,'dt-8','xn--1231234.cn','xn--1231234.cn','cnnic.cn','en','dnr',NULL),(9,'dt-9','0.0.cn','0.0.cn','cnnic.cn','en','dnr',NULL),(10,'dt-10','0.1.cn','0.1.cn','cnnic.cn','en','dnr',NULL),(11,'dt-11','xn--fiqa61au8b7zsevnm8ak20mc4a87e.xn--fiqs8s','中国互联网络信息中心.中国','cnnic.cn','en','dnr',NULL);

#
# Data for table "RDAP_DOMAIN_REDIRECT"
#

INSERT INTO `RDAP_DOMAIN_REDIRECT` (`RDAP_DOMAIN_REDIRECT_ID`,`REDIRECT_TLD`,`REDIRECT_URL`) VALUES (1,'com','https://rdap.com/rdap/'),(2,'edu.cn','https://rdap.edu.cn'),(3,'xn--55qx5d','https://rdap.xn--55qx5d/rdap/');

#
# Data for table "RDAP_DOMAIN_STATUS"
#

INSERT INTO `RDAP_DOMAIN_STATUS` (`DOMAIN_STATUS_ID`,`DOMAIN_ID`,`STATUS`) VALUES (1,1,'validated'),(2,2,'active'),(3,3,'active'),(4,4,'active'),(5,5,'active'),(6,6,'active'),(7,7,'active'),(8,8,'active'),(9,9,'active'),(10,10,'active'),(11,11,'active');

#
# Data for table "RDAP_DSDATA"
#

INSERT INTO `RDAP_DSDATA` (`DSDATA_ID`,`KEY_TAG`,`ALGORITHM`,`DIGEST`,`DIGEST_TYPE`) VALUES (1,12345,3,'49FD46E6C4B45C55D4AC',1),(2,12343,2,'49FD46E6C4B45C55D4AQ',1);

#
# Data for table "RDAP_ENTITY"
#

INSERT INTO `RDAP_ENTITY` (`ENTITY_ID`,`HANDLE`,`KIND`,`FN`,`EMAIL`,`TITLE`,`ORG`,`URL`,`PORT43`,`LANG`) VALUES (1,'et-1','individual','Joe','Joe@example.cn','Research Scientist','work orgnization','http://example.cn','http://whois.cnnic.cn','en'),(2,'et-2','org','Grace Smith','grace@example.cn','Research Scientist','example','http://example.cn','http://whois.cnnic.cn','en'),(3,'et-3','individual','John','john@example.cn','Research Scientist','example','http://example.cn','http://whois.cnnic.cn','en'),(4,'et-4','individual','Lisa','lisa@example.cn','Research Scientist','example','http://example.cn','http://whois.cnnic.cn','en'),(5,'et-5','org','Bruce Edward','bruce@example.cn','Research Scientist','example','http://example.cn','http://whois.cnnic.cn','en');

#
# Data for table "RDAP_ENTITY_STATUS"
#

INSERT INTO `RDAP_ENTITY_STATUS` (`ENTITY_STATUS_ID`,`ENTITY_ID`,`STATUS`) VALUES (1,1,'active'),(2,2,'delete prohibited'),(3,3,'active'),(4,4,'active'),(5,5,'active');

#
# Data for table "RDAP_ERRORMESSAGE"
#

INSERT INTO `RDAP_ERRORMESSAGE` (`ERROR_ID`,`ERROR_CODE`,`TITLE`,`DESCRIPTION`,`LANG`) VALUES (1,400,'BAD REQUEST','BAD REQUEST','en'),(2,404,'NOT FOUND','NOT FOUND','en'),(3,500,'INTERNAL SERVER ERROR','INTERNAL SERVER ERROR','en'),(4,405,'METHOD NOT ALLOWED','METHOD NOT ALLOWED','en'),(5,415,'UNSUPPORTED MEDIA TYPE','UNSUPPORTED MEDIA TYPE','en'),(6,422,'UNPROCESSABLE ENTITY','UNPROCESSABLE ENTITY','en'),(7,401,'Unauthorized','Unauthorized','en'),(8,403,'Forbidden','Forbidden','en'),(9,509,'Bandwidth Limit Exceeded','Bandwidth Limit Exceeded','en'),(10,429,'Too Many Requests','Too Many Requests','en');

#
# Data for table "RDAP_EVENT"
#

INSERT INTO `RDAP_EVENT` (`EVENT_ID`,`EVENT_ACTION`,`EVENT_ACTOR`,`EVENT_DATE`) VALUES (1,'registration','et-1','2014-05-01 16:18:41'),(2,'last changed','et-2','2014-05-19 16:18:59'),(3,'unlocked','et-3','2014-05-19 17:49:14'),(4,'registration','et-4','2014-05-28 16:15:14'),(5,'last changed','et-5','2014-06-12 17:23:46');

#
# Data for table "RDAP_IDENTITY_ACL"
#


#
# Data for table "RDAP_IDENTITY_ROLE"
#


#
# Data for table "RDAP_IDENTITY_USER"
#

INSERT INTO `RDAP_IDENTITY_USER` (`USER_ID`,`USER_NAME`,`USER_PWD`) VALUES (0,'cnnic','e99a18c428cb38d5f260853678922e03');

#
# Data for table "RDAP_IDENTITY_USER_REL_ROLE"
#


#
# Data for table "RDAP_IP"
#

INSERT INTO `RDAP_IP` (`IP_ID`,`HANDLE`,`STARTLOWADDRESS`,`STARTHIGHADDRESS`,`ENDLOWADDRESS`,`ENDHIGHADDRESS`,`VERSION`,`NAME`,`TYPE`,`COUNTRY`,`PARENT_HANDLE`,`LANG`,`PORT43`) VALUES (1,'218.241.111.1-v4',3673255681,0,3673255681,0,'v4','cnnic-1','DIRECT ALLOCATION','CN','218.241.111.1/24-v4','en','whois.cnnic.cn'),(2,'218.241.111.1/24-v4',3673255680,NULL,3673255935,NULL,'v4','cnnic-2','DIRECT ALLOCATION','CN',NULL,'en','cnnic.cn'),(3,'218.0.0.3-v4',3657433091,NULL,3657433091,NULL,'v4','cnnic-3','DIRECT ALLOCATION','CN',NULL,'en','whois.cnnic.cn'),(4,'218.241.0.0-218.241.0.3-v4',3673227264,0,3673227267,0,'v4','cnnic-4','DIRECT ALLOCATION','CN',NULL,'en','cnnic.cn'),(5,'111.255.0.0-111.255.255.255-v4',1878982656,0,1879048191,0,'v4','cnnic-5','DIRECT ALLOCATION','CN',NULL,'en','cnnic.cn'),(6,'3000:0DB8:0000:0000:0000:0000:1428:0000-v6',338165760,3458779597745684480,338165760,3458779597745684480,'v6','cnnic-6','DIRECT ALLOCATION','CN',NULL,'en','cnnic.cn'),(7,'3000:0000:0000:001F:FFFF:FFFF:FFFF:0000-3000:0000:0000:001F:FFFF:FFFF:FFFF:FFFF',18446744073709486080,3458764513820540959,18446744073709551615,3458764513820540959,'v6','cnnic-7','DIRECT ALLOCATION','CN',NULL,'en','cnnic.cn');

#
# Data for table "RDAP_IP_REDIRECT"
#

INSERT INTO `RDAP_IP_REDIRECT` (`RDAP_IP_REDIRECT_ID`,`STARTLOWADDRESS`,`ENDHIGHADDRESS`,`STARTHIGHADDRESS`,`ENDLOWADDRESS`,`REDIRECT_URL`,`VERSION`) VALUES (1,0,3458784528368140288,3458784528368140288,65535,'https://whois.cn/rdap/.well-known/rdap','v6'),(2,3724541952,NULL,NULL,3741319167,'https://whois.cn/rdap/.well-known/rdap','v4');

#
# Data for table "RDAP_IP_STATUS"
#

INSERT INTO `RDAP_IP_STATUS` (`IP_STATUS_ID`,`IP_ID`,`STATUS`) VALUES (1,1,'active'),(2,2,'active'),(3,3,'pending update'),(4,4,'pending update'),(5,5,'pending update'),(6,6,'pending update'),(7,7,'delete prohibited');

#
# Data for table "RDAP_KEYDATA"
#

INSERT INTO `RDAP_KEYDATA` (`KEYDATA_ID`,`FLAGS`,`PROTOCOL`,`PUBLIC_KEY`,`ALGORITHM`) VALUES (1,257,3,'AQPJ////4Q==',1),(2,256,2,'AQPJ////5Q==',1);

#
# Data for table "RDAP_LINK"
#

INSERT INTO `RDAP_LINK` (`LINK_ID`,`VALUE`,`REL`,`HREF`,`MEDIA`,`TYPE`) VALUES (1,'http://rdap.restfulwhois.org/.well-known/rdap/','self','http://rdap.restfulwhois.org/.well-known/rdap/','screen','application/rdap+json'),(2,'http://rdap.restfulwhois.org/.well-known/rdap/','self','http://rdap.restfulwhois.org/.well-known/rdap/','screen','application/rdap+json'),(3,'https://github.com/cnnic/rdap/wiki/api-doc','alternate','https://github.com/cnnic/rdap/wiki/api-doc',NULL,'text/html'),(4,'http://rdap.restfulwhois.org/.well-known/rdap/autnum/62464','self','http://rdap.restfulwhois.org/.well-known/rdap/autnum/63487',NULL,'application/rdap+json'),(5,'http://rdap.restfulwhois.org/.well-known/rdap/autnum/1','self','http://rdap.restfulwhois.org/.well-known/rdap/autnum/19',NULL,'application/rdap+json'),(6,'http://rdap.restfulwhois.org/.well-known/rdap/autnum/9981','self','http://rdap.restfulwhois.org/.well-known/rdap/autnum/9981',NULL,'application/rdap+json'),(7,'http://rdap.restfulwhois.org/.well-known/rdap/domain/111.241.218.in-addr.arpa','self','http://rdap.restfulwhois.org/.well-known/rdap/domain/111.241.218.in-addr.arpa',NULL,'application/rdap+json'),(8,'http://rdap.restfulwhois.org/.well-known/rdap/domain/1.2.3.4.5.6.7.8.9.0.1.2.3.4.5.6.7.8.9.0.1.2.3.4.5.6.7.8.0.0.0.3.ip6.arpa','self','http://rdap.restfulwhois.org/.well-known/rdap/domain/1.2.3.4.5.6.7.8.9.0.1.2.3.4.5.6.7.8.9.0.1.2.3.4.5.6.7.8.0.0.0.3.ip6.arpa',NULL,'application/rdap+json'),(9,'http://rdap.restfulwhois.org/.well-known/rdap/domain/156.in-addr.arpa','self','http://rdap.restfulwhois.org/.well-known/rdap/domain/156.in-addr.arpa',NULL,'application/rdap+json'),(12,'http://rdap.restfulwhois.org/.well-known/rdap/domain/3.0.0.218.in-addr.arpa','self','http://rdap.restfulwhois.org/.well-known/rdap/domain/3.0.0.218.in-addr.arpa',NULL,'application/rdap+json'),(15,'http://rdap.restfulwhois.org/.well-known/rdap/autnum/31','self','http://rdap.restfulwhois.org/.well-known/rdap/autnum/31',NULL,'application/rdap+json'),(16,'http://rdap.restfulwhois.org/.well-known/rdap/autnum/20','self','http://rdap.restfulwhois.org/.well-known/rdap/autnum/30',NULL,'application/rdap+json'),(17,'http://rdap.restfulwhois.org/.well-known/rdap/autnum/167295','self','http://rdap.restfulwhois.org/.well-known/rdap/autnum/167295',NULL,'application/rdap+json'),(18,'http://rdap.restfulwhois.org/.well-known/rdap/domain/xn--123123.cn','self','http://rdap.restfulwhois.org/.well-known/rdap/domain/xn--123123.cn',NULL,'application/rdap+json'),(19,'http://rdap.restfulwhois.org/.well-known/rdap/domain/cnnic.cn','self','http://rdap.restfulwhois.org/.well-known/rdap/domain/cnnic.cn',NULL,'application/rdap+json'),(20,'http://rdap.restfulwhois.org/.well-known/rdap/domain/xn--fiqa61au8b7zsevnm8ak20mc4a87e.cn','self','http://rdap.restfulwhois.org/.well-known/rdap/domain/xn--fiqa61au8b7zsevnm8ak20mc4a87e.cn',NULL,'application/rdap+json'),(21,'http://rdap.restfulwhois.org/.well-known/rdap/domain/xn--elaaaa.xn--fiqs8s','self','http://rdap.restfulwhois.org/.well-known/rdap/domain/xn--elaaaa.xn--fiqs8s',NULL,'application/rdap+json'),(22,'http://rdap.restfulwhois.org/.well-known/rdap/domain/xn--fiq8iy4u6s7b8bb.cn','self','http://rdap.restfulwhois.org/.well-known/rdap/domain/xn--fiq8iy4u6s7b8bb.cn',NULL,'application/rdap+json'),(23,'http://rdap.restfulwhois.org/.well-known/rdap/domain/example.cn','self','http://rdap.restfulwhois.org/.well-known/rdap/domain/example.cn',NULL,'application/rdap+json'),(24,'http://rdap.restfulwhois.org/.well-known/rdap/domain/xn--0zwm56d.xn--fiqs8s','self','http://rdap.restfulwhois.org/.well-known/rdap/domain/xn--0zwm56d.xn--fiqs8s',NULL,'application/rdap+json'),(25,'http://rdap.restfulwhois.org/.well-known/rdap/domain/xn--1231234.cn','self','http://rdap.restfulwhois.org/.well-known/rdap/domain/xn--1231234.cn',NULL,'application/rdap+json'),(26,'http://rdap.restfulwhois.org/.well-known/rdap/domain/0.0.cn','self','http://rdap.restfulwhois.org/.well-known/rdap/domain/0.0.cn',NULL,'application/rdap+json'),(27,'http://rdap.restfulwhois.org/.well-known/rdap/domain/0.1.cn','self','http://rdap.restfulwhois.org/.well-known/rdap/domain/0.1.cn',NULL,'application/rdap+json'),(28,'http://rdap.restfulwhois.org/.well-known/rdap/domain/xn--fiqa61au8b7zsevnm8ak20mc4a87e.xn--fiqs8s','self','http://rdap.restfulwhois.org/.well-known/rdap/domain/xn--fiqa61au8b7zsevnm8ak20mc4a87e.xn--fiqs8s',NULL,'application/rdap+json'),(29,'http://rdap.restfulwhois.org/.well-known/rdap/entity/et-1','self','http://rdap.restfulwhois.org/.well-known/rdap/entity/et-1',NULL,'application/rdap+json'),(30,'http://rdap.restfulwhois.org/.well-known/rdap/entity/et-2','self','http://rdap.restfulwhois.org/.well-known/rdap/entity/et-2',NULL,'application/rdap+json'),(31,'http://rdap.restfulwhois.org/.well-known/rdap/entity/et-3','self','http://rdap.restfulwhois.org/.well-known/rdap/entity/et-3',NULL,'application/rdap+json'),(32,'http://rdap.restfulwhois.org/.well-known/rdap/entity/et-4','self','http://rdap.restfulwhois.org/.well-known/rdap/entity/et-4',NULL,'application/rdap+json'),(33,'http://rdap.restfulwhois.org/.well-known/rdap/entity/et-5','self','http://rdap.restfulwhois.org/.well-known/rdap/entity/et-5',NULL,'application/rdap+json'),(34,'http://rdap.restfulwhois.org/.well-known/rdap/ip/218.241.111.1','self','http://rdap.restfulwhois.org/.well-known/rdap/ip/218.241.111.1',NULL,'application/rdap+json'),(35,'http://rdap.restfulwhois.org/.well-known/rdap/ip/218.241.111.0/24','self','http://rdap.restfulwhois.org/.well-known/rdap/ip/218.241.111.255/24',NULL,'application/rdap+json'),(36,'http://rdap.restfulwhois.org/.well-known/rdap/ip/218.0.0.3','self','http://rdap.restfulwhois.org/.well-known/rdap/ip/218.0.0.3',NULL,'application/rdap+json'),(37,'http://rdap.restfulwhois.org/.well-known/rdap/ip/218.241.0.0/30','self','http://rdap.restfulwhois.org/.well-known/rdap/ip/218.241.0.0/30',NULL,'application/rdap+json'),(38,'http://rdap.restfulwhois.org/.well-known/rdap/ip/111.255.0.0/16','self','http://rdap.restfulwhois.org/.well-known/rdap/ip/111.255.0.0/16',NULL,'application/rdap+json'),(39,'http://rdap.restfulwhois.org/.well-known/rdap/ip/3000:0DB8:0000:0000:0000:0000:1428:0000','self','http://rdap.restfulwhois.org/.well-known/rdap/ip/3000:0DB8:0000:0000:0000:0000:1428:0000',NULL,'application/rdap+json'),(40,'http://rdap.restfulwhois.org/.well-known/rdap/ip/3000:0000:0000:001F:FFFF:FFFF:FFFF:0000/112','self','http://rdap.restfulwhois.org/.well-known/rdap/ip/3000:0000:0000:001F:FFFF:FFFF:FFFF:0000/112',NULL,'application/rdap+json'),(41,'http://rdap.restfulwhois.org/.well-known/rdap/nameserver/xn--1-dr6av31f.xn--0zwm56d.xn--fiqs8s','self','http://rdap.restfulwhois.org/.well-known/rdap/nameserver/xn--1-dr6av31f.xn--0zwm56d.xn--fiqs8s',NULL,'application/rdap+json'),(42,'http://rdap.restfulwhois.org/.well-known/rdap/nameserver/ns1.host.cn','self','http://rdap.restfulwhois.org/.well-known/rdap/nameserver/ns1.host.cn',NULL,'application/rdap+json'),(43,'http://rdap.restfulwhois.org/.well-known/rdap/nameserver/ns1.xn--tiq422d.xn--fiqs8s','self','http://rdap.restfulwhois.org/.well-known/rdap/nameserver/ns1.xn--tiq422d.xn--fiqs8s',NULL,'application/rdap+json'),(44,'http://rdap.restfulwhois.org/.well-known/rdap/nameserver/ns2.xn--tiq422d.xn--fiqs8s','self','http://rdap.restfulwhois.org/.well-known/rdap/nameserver/ns2.xn--tiq422d.xn--fiqs8s',NULL,'application/rdap+json'),(45,'http://rdap.restfulwhois.org/.well-known/rdap/nameserver/xn--tiq422d.xn--0zwm56d.xn--fiqs8s','self','http://rdap.restfulwhois.org/.well-known/rdap/nameserver/xn--tiq422d.xn--0zwm56d.xn--fiqs8s',NULL,'application/rdap+json');

#
# Data for table "RDAP_LINK_HREFLANG"
#

INSERT INTO `RDAP_LINK_HREFLANG` (`HREFLANG_ID`,`LINK_ID`,`HREFLANG`) VALUES (1,1,'en'),(2,2,'en'),(3,3,'en');

#
# Data for table "RDAP_LINK_TITLE"
#

INSERT INTO `RDAP_LINK_TITLE` (`TITLE_ID`,`LINK_ID`,`TITLE`) VALUES (1,1,'the title of the notice'),(2,2,'link title'),(3,3,'the help title');

#
# Data for table "RDAP_NAMESERVER"
#

INSERT INTO `RDAP_NAMESERVER` (`NAMESERVER_ID`,`HANDLE`,`LDH_NAME`,`UNICODE_NAME`,`PORT43`,`LANG`) VALUES (1,'ns-1','xn--1-dr6av31f.xn--0zwm56d.xn--fiqs8s','主机1.测试.中国','whois.主机1.测试.中国','en'),(2,'ns-2','ns1.host.cn','ns1.host.cn','whois.host.cn','en'),(3,'ns-3','ns1.xn--tiq422d.xn--fiqs8s','ns1.主机.中国','ns1.主机.中国','en'),(4,'ns-4','ns.xn--tiq422d.xn--fiqs8s','ns.主机.中国','whois.ns.主机.中国','en'),(5,'ns-5','xn--tiq422d.xn--0zwm56d.xn--fiqs8s','主机.测试.中国','whois.测试.主机.中国','en');

#
# Data for table "RDAP_NAMESERVER_IP"
#

INSERT INTO `RDAP_NAMESERVER_IP` (`NAMESERVER_IP_ID`,`NAMESERVER_ID`,`IP_HIGH`,`IP_LOW`,`VERSION`) VALUES (1,1,NULL,3673255776,'v4'),(2,2,NULL,3673255776,'v4'),(3,3,NULL,318767109,'v4'),(4,4,2306139568115548160,2260596444381562,'v6'),(5,5,2306139568115548160,2260596444381562,'v6');

#
# Data for table "RDAP_NAMESERVER_STATUS"
#

INSERT INTO `RDAP_NAMESERVER_STATUS` (`NAMESERVER_STATUS_ID`,`NAMESERVER_ID`,`STATUS`) VALUES (1,1,'delete prohibited'),(2,2,'active'),(3,3,'active'),(4,4,'pending renew'),(5,5,'update prohibited');

#
# Data for table "RDAP_NOTICE"
#

INSERT INTO `RDAP_NOTICE` (`NOTICE_ID`,`TYPE`,`TITLE`) VALUES (1,'notice','the notices for response'),(2,'remark','the remarks title'),(3,'help','this is help test');

#
# Data for table "RDAP_NOTICE_DESCRIPTION"
#

INSERT INTO `RDAP_NOTICE_DESCRIPTION` (`NOTICE_DESC_ID`,`NOTICE_ID`,`DESCRIPTION`) VALUES (1,1,'Service subject to The Registry of the CNNIC.'),(2,1,'Copyright (c) 2020 CNNIC'),(3,2,'remarks_description'),(4,3,'the description of help');

#
# Data for table "RDAP_POLICY"
#


#
# Data for table "RDAP_PUBLICID"
#

INSERT INTO `RDAP_PUBLICID` (`PUBLIC_ID`,`IDENTIFIER`,`TYPE`) VALUES (1,'cnnic-1','cnnic'),(2,'IANA-1','IANA');

#
# Data for table "RDAP_SECUREDNS"
#

INSERT INTO `RDAP_SECUREDNS` (`SECUREDNS_ID`,`ZONE_SIGNED`,`DELEGATION_SIGNED`,`MAX_SIGLIFE`,`DOMAIN_ID`) VALUES (1,1,1,3600,1),(2,1,1,7200,2);

#
# Data for table "RDAP_VARIANT"
#

INSERT INTO `RDAP_VARIANT` (`VARIANT_ID`,`LDH_NAME`,`UNICODE_NAME`,`IDNTABLE`) VALUES (1,'xn--g6ws64d.xn--fiqs8s','測试.中国','.EXAMPLE Swedish'),(2,'xn--0zwm56d.xn--fiqy8s','测试.中圀',NULL);

#
# Data for table "RDAP_VCARD_ADR"
#

INSERT INTO `RDAP_VCARD_ADR` (`ADR_ID`,`POST_BOX`,`EXT_ADR`,`STREET`,`CITY`,`SP`,`POSTAL_CODE`,`COUNTRY`,`ENTITY_ID`,`TYPE`,`PREF`) VALUES (1,'1010','Building 1010','South 4th Street','beijing','BJ','100190','CN',1,'work',1),(2,'1011','Building 058','First Street','beijing','BJ','100120','CN',1,'home',2);

#
# Data for table "RDAP_VCARD_TEL"
#

INSERT INTO `RDAP_VCARD_TEL` (`TEL_ID`,`TYPE`,`GLOBAL_NUMBER`,`EXT_NUMBER`,`ENTITY_ID`,`PREF`) VALUES (1,'work','+86.01058813179','3179',1,1),(2,'home','+86.13511110202','0202',1,2);

#
# Data for table "REL_DOMAIN_NAMESERVER"
#

INSERT INTO `REL_DOMAIN_NAMESERVER` (`REL_DOMAIN_NS_ID`,`DOMAIN_ID`,`NAMESERVER_ID`) VALUES (1,2,1),(2,2,2),(3,1,2),(4,1,5),(5,11,1),(6,3,1),(7,4,1),(8,5,1),(9,6,1),(11,7,1),(12,8,1),(13,9,1),(14,10,1),(15,2,2),(16,3,2),(17,4,2);

#
# Data for table "REL_DOMAIN_VARIANT"
#

INSERT INTO `REL_DOMAIN_VARIANT` (`REL_VARIANT_ID`,`DOMAIN_ID`,`VARIANT_TYPE`,`VARIANT_ID`) VALUES (1,7,'unregistered',1),(2,7,'registration restricted',2);

#
# Data for table "REL_ENTITY_REGISTRATION"
#

INSERT INTO `REL_ENTITY_REGISTRATION` (`REL_ENTITY_ID`,`REL_ID`,`REL_OBJECT_TYPE`,`ENTITY_ID`,`ENTITY_ROLE`) VALUES (1,1,'arpa',1,'registrant'),(2,2,'domain',2,'billing'),(3,1,'autnum',1,'noc'),(4,1,'ip',3,'technical'),(5,1,'nameServer',4,'administrative'),(6,1,'entity',5,'sponsor');

#
# Data for table "REL_EVENT_REGISTRATION"
#

INSERT INTO `REL_EVENT_REGISTRATION` (`REL_EVENT_ID`,`REL_ID`,`REL_OBJECT_TYPE`,`EVENT_ID`) VALUES (1,1,'arpa',1),(2,2,'domain',2),(3,1,'ip',3),(4,1,'autnum',4),(5,1,'entity',1),(6,2,'entity',1),(7,1,'nameServer',5);

#
# Data for table "REL_LINK_OBJECT"
#

INSERT INTO `REL_LINK_OBJECT` (`REL_LINK_ID`,`REL_ID`,`REL_OBJECT_TYPE`,`LINK_ID`) VALUES (1,1,'notice',1),(2,2,'remark',2),(3,3,'help',3),(4,1,'autnum',4),(5,2,'autnum',5),(6,3,'autnum',6),(7,1,'arpa',7),(8,2,'arpa',8),(9,3,'arpa',9),(11,4,'arpa',12),(15,4,'autnum',15),(16,5,'autnum',16),(17,6,'autnum',17),(18,1,'domain',18),(19,2,'domain',19),(20,3,'domain',20),(21,4,'domain',21),(22,5,'domain',22),(23,6,'domain',23),(24,7,'domain',24),(25,8,'domain',25),(26,9,'domain',26),(27,10,'domain',27),(28,11,'domain',28),(29,1,'entity',29),(30,2,'entity',30),(31,3,'entity',31),(32,4,'entity',32),(33,5,'entity',33),(34,1,'ip',34),(35,2,'ip',35),(36,3,'ip',36),(37,4,'ip',37),(38,5,'ip',38),(39,6,'ip',39),(40,7,'ip',40),(41,1,'nameServer',41),(42,2,'nameServer',42),(43,3,'nameServer',43),(44,4,'nameServer',44),(45,5,'nameServer',45);

#
# Data for table "REL_NOTICE_REGISTRATION"
#

INSERT INTO `REL_NOTICE_REGISTRATION` (`REL_NOTICE_ID`,`REL_ID`,`REL_OBJECT_TYPE`,`NOTICE_ID`) VALUES (1,1,'arpa',2),(2,2,'domain',2),(3,1,'autnum',2),(4,1,'nameServer',2),(5,1,'entity',2),(6,1,'ip',2);

#
# Data for table "REL_PUBLICID_REGISTRATION"
#

INSERT INTO `REL_PUBLICID_REGISTRATION` (`REL_PUBLICID_ID`,`REL_ID`,`REL_OBJECT_TYPE`,`PUBLIC_ID`) VALUES (1,1,'arpa',1),(2,2,'domain',2),(3,1,'entity',1);

#
# Data for table "REL_SECUREDNS_DSKEY"
#

INSERT INTO `REL_SECUREDNS_DSKEY` (`REL_SECUREDNS_ID`,`SECUREDNS_ID`,`REL_DSKEY_TYPE`,`REL_ID`) VALUES (1,1,'dsData',1),(2,1,'keyData',1),(3,2,'dsData',2),(4,2,'keyData',2);