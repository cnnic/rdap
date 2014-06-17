
DROP DATABASE IF EXISTS `rdap`;
CREATE DATABASE `rdap` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `rdap`;

#
# Source for table "RDAP_ARPA"
#

DROP TABLE IF EXISTS `RDAP_ARPA`;
CREATE TABLE `RDAP_ARPA` (
  `ARPA_ID` int(10) NOT NULL AUTO_INCREMENT,
  `HANDLE` varchar(100) COLLATE utf8_bin NOT NULL,
  `ARPA_NAME` varchar(255) COLLATE utf8_bin NOT NULL,
  `PORT43` varchar(4096) COLLATE utf8_bin DEFAULT NULL,
  `LANG` varchar(42) COLLATE utf8_bin DEFAULT NULL,
  `STARTLOWADDRESS` bigint(20) unsigned DEFAULT NULL,
  `STARTHIGHADDRESS` bigint(20) unsigned DEFAULT NULL,
  `ENDLOWADDRESS` bigint(20) unsigned DEFAULT NULL,
  `ENDHIGHADDRESS` bigint(20) unsigned DEFAULT NULL,
  `VERSION` varchar(2) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ARPA_ID`),
  UNIQUE KEY `UK_ARPA_HANDLE` (`HANDLE`) USING BTREE,
  KEY `IDX_ARPA_NAME` (`ARPA_NAME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='The domain object class represents a DNS name and point of delegation.  For RIRs these delegation points are in the reverse DNS tree, whereas for DNRs these delegation points are in the forward DNS tree. In both cases, the high level structure of the domain object class consists of information about the domain registration, nameserver information related to the domain name, and entities related to the domain name (e.g. registrant information, contacts, etc.). Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-25">Domain</a>';

#
# Source for table "RDAP_ARPA_STATUS"
#

DROP TABLE IF EXISTS `RDAP_ARPA_STATUS`;
CREATE TABLE `RDAP_ARPA_STATUS` (
  `ARPA_STATUS_ID` int(10) NOT NULL AUTO_INCREMENT,
  `ARPA_ID` int(10) NOT NULL,
  `STATUS` varchar(20) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`ARPA_STATUS_ID`),
  KEY `IDX_ARPA_ID` (`ARPA_ID`) USING BTREE,
  CONSTRAINT `FK_RDAP_ARPA_STATUS_DOMAIN_ID` FOREIGN KEY (`ARPA_ID`) REFERENCES `RDAP_ARPA` (`ARPA_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='state of domain, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-52">Status</a>';

#
# Source for table "RDAP_AUTNUM"
#

DROP TABLE IF EXISTS `RDAP_AUTNUM`;
CREATE TABLE `RDAP_AUTNUM` (
  `AS_ID` int(10) NOT NULL AUTO_INCREMENT,
  `HANDLE` varchar(100) COLLATE utf8_bin NOT NULL,
  `START_AUTNUM` bigint(10) DEFAULT NULL,
  `END_AUTNUM` bigint(10) NOT NULL,
  `NAME` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `TYPE` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `COUNTRY` varchar(2) COLLATE utf8_bin DEFAULT NULL,
  `LANG` varchar(42) COLLATE utf8_bin DEFAULT ' ',
  `PORT43` varchar(4096) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`AS_ID`),
  UNIQUE KEY `UK_AS_HANDLE` (`HANDLE`) USING BTREE,
  KEY `IDX_AS_END_AUTNUM` (`END_AUTNUM`,`START_AUTNUM`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='The Autonomous System Number (autnum) object class models Autonomous System Number registrations found in RIRs and represents the expected response to an "/autnum" query as defined by [I-D.ietf-weirds-rdap-query].  There is no equivalent object class for DNRs.  The high level structure of the autnum object class consists of information about the network registration and entities related to the autnum registration (e.g. registrant information, contacts, etc.), and is similar to the IP Network entity object class., reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-41"> Autonomous System Number (autnum)</a>';

#
# Source for table "RDAP_AUTNUM_REDIRECT"
#

DROP TABLE IF EXISTS `RDAP_AUTNUM_REDIRECT`;
CREATE TABLE `RDAP_AUTNUM_REDIRECT` (
  `AS_REDIRECT_ID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `START_NUMBER` int(11) unsigned NOT NULL,
  `END_NUMBER` int(11) unsigned NOT NULL,
  `REDIRECT_URL` varchar(4096) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`AS_REDIRECT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='rdap_autnum_redirect is modelled on old table, which is to be modified by bootstraps';

#
# Source for table "RDAP_AUTNUM_STATUS"
#

DROP TABLE IF EXISTS `RDAP_AUTNUM_STATUS`;
CREATE TABLE `RDAP_AUTNUM_STATUS` (
  `AS_STATUS_ID` int(10) NOT NULL AUTO_INCREMENT,
  `AS_ID` int(10) NOT NULL,
  `STATUS` varchar(20) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`AS_STATUS_ID`),
  KEY `IDX_STATUS_AS_ID` (`AS_ID`) USING BTREE,
  CONSTRAINT `FK_RDAP_AUTNUM_STATUS_AS_ID` FOREIGN KEY (`AS_ID`) REFERENCES `RDAP_AUTNUM` (`AS_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='state of an autnum, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-52">Status</a>';

#
# Source for table "RDAP_CONFORMANCE"
#

DROP TABLE IF EXISTS `RDAP_CONFORMANCE`;
CREATE TABLE `RDAP_CONFORMANCE` (
  `CONFORMANCE_ID` int(10) unsigned NOT NULL,
  `RDAP_CONFORMANCE` varchar(2048) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`CONFORMANCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Rdap conformance providing a hint as to the specifications used in the construction of the response. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-8">RDAP Conformance</a>';

#
# Source for table "RDAP_DOMAIN"
#

DROP TABLE IF EXISTS `RDAP_DOMAIN`;
CREATE TABLE `RDAP_DOMAIN` (
  `DOMAIN_ID` int(10) NOT NULL AUTO_INCREMENT,
  `HANDLE` varchar(100) COLLATE utf8_bin NOT NULL,
  `LDH_NAME` varchar(255) COLLATE utf8_bin NOT NULL,
  `UNICODE_NAME` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
  `PORT43` varchar(4096) COLLATE utf8_bin DEFAULT NULL,
  `LANG` varchar(42) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`DOMAIN_ID`),
  UNIQUE KEY `UK_DOMAIN_HANDLE` (`HANDLE`) USING BTREE,
  KEY `IDX_DOMAIN_LDH_NAME` (`LDH_NAME`) USING BTREE,
  KEY `IDX_DOMAIN_UNICODE_NAME` (`UNICODE_NAME`(255)) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='The domain object class represents a DNS name and point of delegation.  For RIRs these delegation points are in the reverse DNS tree, whereas for DNRs these delegation points are in the forward DNS tree. In both cases, the high level structure of the domain object class consists of information about the domain registration, nameserver information related to the domain name, and entities related to the domain name (e.g. registrant information, contacts, etc.). Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-25">Domain</a>';

#
# Source for table "RDAP_DOMAIN_REDIRECT"
#

DROP TABLE IF EXISTS `RDAP_DOMAIN_REDIRECT`;
CREATE TABLE `RDAP_DOMAIN_REDIRECT` (
  `RDAP_DOMAIN_REDIRECT_ID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `REDIRECT_TLD` varchar(10) COLLATE utf8_bin NOT NULL,
  `REDIRECT_URL` varchar(4096) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`RDAP_DOMAIN_REDIRECT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='rdap_domain_redirect is modelled on old table, which is to be modified by bootstraps';

#
# Source for table "RDAP_DOMAIN_STATUS"
#

DROP TABLE IF EXISTS `RDAP_DOMAIN_STATUS`;
CREATE TABLE `RDAP_DOMAIN_STATUS` (
  `DOMAIN_STATUS_ID` int(10) NOT NULL AUTO_INCREMENT,
  `DOMAIN_ID` int(10) NOT NULL,
  `STATUS` varchar(20) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`DOMAIN_STATUS_ID`),
  KEY `IDX_DOMAIN_ID` (`DOMAIN_ID`) USING BTREE,
  CONSTRAINT `FK_RDAP_DOMAIN_STATUS_DOMAIN_ID` FOREIGN KEY (`DOMAIN_ID`) REFERENCES `RDAP_DOMAIN` (`DOMAIN_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='state of domain, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-52">Status</a>';

#
# Source for table "RDAP_DSDATA"
#

DROP TABLE IF EXISTS `RDAP_DSDATA`;
CREATE TABLE `RDAP_DSDATA` (
  `DSDATA_ID` int(10) NOT NULL AUTO_INCREMENT,
  `KEY_TAG` int(5) NOT NULL,
  `ALGORITHM` int(3) NOT NULL,
  `DIGEST` varchar(512) COLLATE utf8_bin NOT NULL,
  `DIGEST_TYPE` int(3) NOT NULL,
  PRIMARY KEY (`DSDATA_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Ds data for domain DNSSEC, DNSSEC provides data integrity for DNS through digital signing of resource records.  To enable DNSSEC, the zone is signed by one or more private keys and the signatures stored as RRSIG records.  To complete the chain of trust in the DNS zone hierarchy, a digest of each DNSKEY record (which contains the public key) must be loaded into the parent zone, stored as Delegation Signer (DS) records and signed by the parent''s private key (RRSIG DS record), "Resource Records for the DNS Security Extensions" [RFC4034]. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-25">Domain</a>';

#
# Source for table "RDAP_ENTITY"
#

DROP TABLE IF EXISTS `RDAP_ENTITY`;
CREATE TABLE `RDAP_ENTITY` (
  `ENTITY_ID` int(10) NOT NULL AUTO_INCREMENT,
  `HANDLE` varchar(255) COLLATE utf8_bin NOT NULL,
  `KIND` varchar(15) COLLATE utf8_bin DEFAULT NULL,
  `FN` varchar(100) COLLATE utf8_bin NOT NULL,
  `EMAIL` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `TITLE` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `ORG` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `URL` varchar(4096) COLLATE utf8_bin DEFAULT NULL,
  `PORT43` varchar(4096) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ENTITY_ID`),
  UNIQUE KEY `UK_ENTITY_HANDLE` (`HANDLE`),
  KEY `IDX_ENTITY_FN` (`FN`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='The entity object class represents the information of organizations, corporations, governments, non-profits, clubs, individual persons, and informal groups of people.  All of these representations are so similar that it is best to represent them in JSON [RFC4627] with one construct, the entity object class, to aid in the re-use of code by implementers. The entity object is served by both RIRs and DNRs. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-15">Entity</a>';

#
# Source for table "RDAP_ENTITY_STATUS"
#

DROP TABLE IF EXISTS `RDAP_ENTITY_STATUS`;
CREATE TABLE `RDAP_ENTITY_STATUS` (
  `ENTITY_STATUS_ID` int(10) NOT NULL AUTO_INCREMENT,
  `ENTITY_ID` int(10) NOT NULL,
  `STATUS` varchar(20) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`ENTITY_STATUS_ID`),
  KEY `IDX_STATUS_ENTITY_ID` (`ENTITY_ID`) USING BTREE,
  CONSTRAINT `FK_RDAP_ENTITY_STATUS_ENTITY_ID` FOREIGN KEY (`ENTITY_ID`) REFERENCES `RDAP_ENTITY` (`ENTITY_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='state of entity, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-52">Status</a>';

#
# Source for table "RDAP_ERRORMESSAGE"
#

DROP TABLE IF EXISTS `RDAP_ERRORMESSAGE`;
CREATE TABLE `RDAP_ERRORMESSAGE` (
  `ERROR_ID` int(10) NOT NULL AUTO_INCREMENT,
  `ERROR_CODE` int(10) NOT NULL,
  `TITLE` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `DESCRIPTION` varchar(1024) COLLATE utf8_bin NOT NULL,
  `LANG` varchar(42) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ERROR_ID`),
  UNIQUE KEY `UK_ERROR_CODE` (`ERROR_CODE`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Some non-answer responses may return entity bodies with information that could be more descriptive. The basic structure of that response is an object class containing an error code number (corresponding to the HTTP response code) followed by a string named "title" followed by an array of strings named "description". Reference to <a href=" http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-44">Error Response Body</a>';

#
# Source for table "RDAP_EVENT"
#

DROP TABLE IF EXISTS `RDAP_EVENT`;
CREATE TABLE `RDAP_EVENT` (
  `EVENT_ID` int(10) NOT NULL AUTO_INCREMENT,
  `EVENT_ACTION` varchar(15) COLLATE utf8_bin NOT NULL,
  `EVENT_ACTOR` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `EVENT_DATE` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`EVENT_ID`),
  KEY `IDX_EVENT_ACTOR` (`EVENT_ACTOR`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Events that have occurred on an instance of an object class, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-11">Event</a>';

#
# Source for table "RDAP_IDENTITY_ACL"
#

DROP TABLE IF EXISTS `RDAP_IDENTITY_ACL`;
CREATE TABLE `RDAP_IDENTITY_ACL` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `OBJECT_TYPE` varchar(64) COLLATE utf8_bin NOT NULL,
  `OBJECT_ID` int(10) NOT NULL,
  `ROLE_ID` int(10) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=' define Access Control Entry';

#
# Source for table "RDAP_IDENTITY_ROLE"
#

DROP TABLE IF EXISTS `RDAP_IDENTITY_ROLE`;
CREATE TABLE `RDAP_IDENTITY_ROLE` (
  `ROLE_ID` int(10) NOT NULL,
  `ROLE_NAME` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`ROLE_ID`),
  UNIQUE KEY `ROLE_NAME` (`ROLE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=' define Role';

#
# Source for table "RDAP_IDENTITY_USER"
#

DROP TABLE IF EXISTS `RDAP_IDENTITY_USER`;
CREATE TABLE `RDAP_IDENTITY_USER` (
  `USER_ID` int(10) NOT NULL,
  `USER_NAME` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `USER_PWD` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `USER_NAME` (`USER_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='define User';

#
# Source for table "RDAP_IDENTITY_USER_REL_ROLE"
#

DROP TABLE IF EXISTS `RDAP_IDENTITY_USER_REL_ROLE`;
CREATE TABLE `RDAP_IDENTITY_USER_REL_ROLE` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(10) NOT NULL,
  `ROLE_ID` int(10) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `USER_ID` (`USER_ID`),
  CONSTRAINT `RDAP_IDENTITY_USER_REL_ROLE_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `RDAP_IDENTITY_USER` (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=' define relation between User and Role';

#
# Source for table "RDAP_IP"
#

DROP TABLE IF EXISTS `RDAP_IP`;
CREATE TABLE `RDAP_IP` (
  `IP_ID` int(10) NOT NULL AUTO_INCREMENT,
  `HANDLE` varchar(100) COLLATE utf8_bin NOT NULL,
  `STARTLOWADDRESS` bigint(20) unsigned DEFAULT NULL,
  `STARTHIGHADDRESS` bigint(20) unsigned DEFAULT NULL,
  `ENDLOWADDRESS` bigint(20) unsigned DEFAULT NULL,
  `ENDHIGHADDRESS` bigint(20) unsigned DEFAULT NULL,
  `VERSION` varchar(2) COLLATE utf8_bin DEFAULT NULL,
  `NAME` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `TYPE` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `COUNTRY` varchar(2) COLLATE utf8_bin DEFAULT NULL,
  `PARENT_HANDLE` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `LANG` varchar(42) COLLATE utf8_bin DEFAULT NULL,
  `PORT43` varchar(4096) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`IP_ID`),
  UNIQUE KEY `UK_IP_HANDLE` (`HANDLE`) USING BTREE,
  KEY `IDX_IP_STARTLOWADDRESS` (`STARTLOWADDRESS`,`STARTHIGHADDRESS`,`ENDLOWADDRESS`,`ENDHIGHADDRESS`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=' The IP Network object class models IP network registrations found in RIRs and is the expected response for the "/ip" query as defined by [I-D.ietf-weirds-rdap-query].  There is no equivalent object class for DNRs.  The high level structure of the IP network object class consists of information about the network registration and entities related to the IP network (e.g. registrant information, contacts, etc...). Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-37">IP Network Object Class </a>';

#
# Source for table "RDAP_IP_REDIRECT"
#

DROP TABLE IF EXISTS `RDAP_IP_REDIRECT`;
CREATE TABLE `RDAP_IP_REDIRECT` (
  `RDAP_IP_REDIRECT_ID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `START_HIGH_ADDRESS` bigint(20) unsigned DEFAULT NULL,
  `START_LOW_ADDRESS` bigint(20) unsigned DEFAULT NULL,
  `END_HIGH_ADDRESS` bigint(20) unsigned DEFAULT NULL,
  `END_LOW_ADDRESS` bigint(20) unsigned DEFAULT NULL,
  `REDIRECT_URL` varchar(4096) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`RDAP_IP_REDIRECT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='rdap_ip_redirect is modelled on old table, which is to be modified by bootstraps';

#
# Source for table "RDAP_IP_STATUS"
#

DROP TABLE IF EXISTS `RDAP_IP_STATUS`;
CREATE TABLE `RDAP_IP_STATUS` (
  `IP_STATUS_ID` int(10) NOT NULL AUTO_INCREMENT,
  `IP_ID` int(10) NOT NULL,
  `STATUS` varchar(20) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`IP_STATUS_ID`),
  KEY `IDX_IP_STATUS_IP_ID` (`IP_ID`) USING BTREE,
  CONSTRAINT `FK_RDAP_IP_STATUS_IP_ID` FOREIGN KEY (`IP_ID`) REFERENCES `RDAP_IP` (`IP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='state of IP Network object, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-52">Status</a>';

#
# Source for table "RDAP_KEYDATA"
#

DROP TABLE IF EXISTS `RDAP_KEYDATA`;
CREATE TABLE `RDAP_KEYDATA` (
  `KEYDATA_ID` int(10) NOT NULL AUTO_INCREMENT,
  `FLAGS` int(3) NOT NULL,
  `PROTOCOL` int(1) NOT NULL,
  `PUBLIC_KEY` varchar(1024) COLLATE utf8_bin NOT NULL,
  `ALGORITHM` int(3) NOT NULL,
  PRIMARY KEY (`KEYDATA_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Key data for domain DNSSEC, DNSSEC provides data integrity for DNS through digital signing of resource records.  To enable DNSSEC, the zone is signed by one or more private keys and the signatures stored as RRSIG records.  To complete the chain of trust in the DNS zone hierarchy, a digest of each DNSKEY record (which contains the public key) must be loaded into the parent zone, stored as Delegation Signer (DS) records and signed by the parent''s private key (RRSIG DS record), "Resource Records for the DNS Security Extensions" [RFC4034]. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-25">Domain</a>';

#
# Source for table "RDAP_LINK"
#

DROP TABLE IF EXISTS `RDAP_LINK`;
CREATE TABLE `RDAP_LINK` (
  `LINK_ID` int(10) NOT NULL AUTO_INCREMENT,
  `VALUE` varchar(4098) COLLATE utf8_bin NOT NULL,
  `REL` varchar(50) COLLATE utf8_bin NOT NULL,
  `HREF` varchar(4098) COLLATE utf8_bin NOT NULL,
  `MEDIA` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `TYPE` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`LINK_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=' The "links" array is found in data structures to signify links to other resources on the Internet.  The relationship of these links is defined by the IANA registry described by [RFC5988]. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-8"> Links </a>';

#
# Source for table "RDAP_LINK_HREFLANG"
#

DROP TABLE IF EXISTS `RDAP_LINK_HREFLANG`;
CREATE TABLE `RDAP_LINK_HREFLANG` (
  `HREFLANG_ID` int(10) NOT NULL AUTO_INCREMENT,
  `LINK_ID` int(10) NOT NULL,
  `HREFLANG` varchar(42) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`HREFLANG_ID`),
  KEY `IDX_LINK_HREFLANG_LINK_ID` (`LINK_ID`) USING BTREE,
  CONSTRAINT `FK_RDAP_LINK_HREFLANG_LINK_ID` FOREIGN KEY (`LINK_ID`) REFERENCES `RDAP_LINK` (`LINK_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=184 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='link href''s lang, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-8"> Links </a>';

#
# Source for table "RDAP_LINK_TITLE"
#

DROP TABLE IF EXISTS `RDAP_LINK_TITLE`;
CREATE TABLE `RDAP_LINK_TITLE` (
  `TITLE_ID` int(10) NOT NULL AUTO_INCREMENT,
  `LINK_ID` int(10) NOT NULL,
  `TITLE` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`TITLE_ID`),
  KEY `IDX_LINK_TITLE_LINK_ID` (`LINK_ID`) USING BTREE,
  CONSTRAINT `FK_RDAP_LINK_TITLE_LINK_ID` FOREIGN KEY (`LINK_ID`) REFERENCES `RDAP_LINK` (`LINK_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=183 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='link titles, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-8"> Links </a>';

#
# Source for table "RDAP_NAMESERVER"
#

DROP TABLE IF EXISTS `RDAP_NAMESERVER`;
CREATE TABLE `RDAP_NAMESERVER` (
  `NAMESERVER_ID` int(10) NOT NULL AUTO_INCREMENT,
  `HANDLE` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `LDH_NAME` varchar(255) COLLATE utf8_bin NOT NULL,
  `UNICODE_NAME` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
  `PORT43` varchar(4096) COLLATE utf8_bin DEFAULT NULL,
  `LANG` varchar(42) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`NAMESERVER_ID`),
  UNIQUE KEY `UK_NAMESERVER_HANDLE` (`HANDLE`) USING BTREE,
  KEY `IDX_NAMESERVER_LDH_NAME` (`LDH_NAME`) USING BTREE,
  KEY `IDX_NAMESERVER_UNICODE_NAME` (`UNICODE_NAME`(255)) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=' The nameserver object class represents information regarding DNS name servers used in both forward and reverse DNS.  RIRs and some DNRs register or expose nameserver information as an attribute of a domain name, while other DNRs model nameservers as "first class objects". The nameserver object class accommodates both models and degrees of variation in between. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#section-6.2">Nameserver</a>';

#
# Source for table "RDAP_NAMESERVER_IP"
#

DROP TABLE IF EXISTS `RDAP_NAMESERVER_IP`;
CREATE TABLE `RDAP_NAMESERVER_IP` (
  `NAMESERVER_IP_ID` int(10) NOT NULL AUTO_INCREMENT,
  `NAMESERVER_ID` int(10) NOT NULL,
  `IP_HIGH` bigint(20) unsigned DEFAULT NULL,
  `IP_LOW` bigint(20) unsigned NOT NULL DEFAULT '0',
  `VERSION` varchar(2) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`NAMESERVER_IP_ID`),
  KEY `IDX_NAMESERVER_IP_NAMESERVER_ID` (`NAMESERVER_ID`) USING BTREE,
  KEY `IDX_NAMESERVER_IP_IP` (`IP_LOW`,`IP_HIGH`) USING BTREE,
  CONSTRAINT `FK_RDAP_NAMESERVER_IP_NAMESERVER_ID` FOREIGN KEY (`NAMESERVER_ID`) REFERENCES `RDAP_NAMESERVER` (`NAMESERVER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='nameserver ip, NOT RDAP IP object, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#section-6.2">Nameserver</a>';

#
# Source for table "RDAP_NAMESERVER_STATUS"
#

DROP TABLE IF EXISTS `RDAP_NAMESERVER_STATUS`;
CREATE TABLE `RDAP_NAMESERVER_STATUS` (
  `NAMESERVER_STATUS_ID` int(10) NOT NULL AUTO_INCREMENT,
  `NAMESERVER_ID` int(10) NOT NULL,
  `STATUS` varchar(20) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`NAMESERVER_STATUS_ID`),
  KEY `IDX_NAMESERVER_STATUS_NAMESERVER_ID` (`NAMESERVER_ID`) USING BTREE,
  CONSTRAINT `FK_RDAP_NAMESERVER_STATUS_NAMESERVER_ID` FOREIGN KEY (`NAMESERVER_ID`) REFERENCES `RDAP_NAMESERVER` (`NAMESERVER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='nameserver status , reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-52">Status</a>';

#
# Source for table "RDAP_NOTICE"
#

DROP TABLE IF EXISTS `RDAP_NOTICE`;
CREATE TABLE `RDAP_NOTICE` (
  `NOTICE_ID` int(10) NOT NULL AUTO_INCREMENT,
  `TYPE` varchar(6) COLLATE utf8_bin NOT NULL,
  `TITLE` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`NOTICE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='The "notices", "help" and "remarks" data structures take the same form.  The "notices" structure denotes information about the service providing RDAP information, whereas the "remarks" structure denotes information about the object class, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-9">Notices And Remarks</a>';

#
# Source for table "RDAP_NOTICE_DESCRIPTION"
#

DROP TABLE IF EXISTS `RDAP_NOTICE_DESCRIPTION`;
CREATE TABLE `RDAP_NOTICE_DESCRIPTION` (
  `NOTICE_DESC_ID` int(10) NOT NULL AUTO_INCREMENT,
  `NOTICE_ID` int(10) NOT NULL,
  `DESCRIPTION` varchar(2048) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`NOTICE_DESC_ID`),
  KEY `IDX_NOTICE_DESCRIPTION_NOTICE_ID` (`NOTICE_ID`) USING BTREE,
  CONSTRAINT `FK_RDAP_NOTICE_DESCRIPTION_NOTICE_ID` FOREIGN KEY (`NOTICE_ID`) REFERENCES `RDAP_NOTICE` (`NOTICE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Description for notice\r\nemark for registration objecthelp, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-9">Notices And Remarks</a>';

#
# Source for table "RDAP_PUBLICID"
#

DROP TABLE IF EXISTS `RDAP_PUBLICID`;
CREATE TABLE `RDAP_PUBLICID` (
  `PUBLIC_ID` int(10) NOT NULL AUTO_INCREMENT,
  `IDENTIFIER` varchar(256) COLLATE utf8_bin NOT NULL,
  `TYPE` varchar(256) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`PUBLIC_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='The publicid maps a public identifier to a registration object, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-13">Public IDs</a>';

#
# Source for table "RDAP_SECUREDNS"
#

DROP TABLE IF EXISTS `RDAP_SECUREDNS`;
CREATE TABLE `RDAP_SECUREDNS` (
  `SECUREDNS_ID` int(10) NOT NULL AUTO_INCREMENT,
  `ZONE_SIGNED` tinyint(1) DEFAULT NULL,
  `DELEGATION_SIGNED` tinyint(1) NOT NULL,
  `MAX_SIGLIFE` int(11) DEFAULT NULL,
  `DOMAIN_ID` int(10) NOT NULL,
  `DOMAIN_TYPE` varchar(6) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`SECUREDNS_ID`),
  KEY `IDX_SECUREDNS_DOMAIN_ID` (`DOMAIN_ID`,`DOMAIN_TYPE`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='SecureDNS member to represent secure DNS information about domain names.  DNSSEC provides data integrity for DNS through digital signing of resource records.  To enable DNSSEC, the zone is signed by one or more private keys and the signatures stored as RRSIG records.  To complete the chain of trust in the DNS zone hierarchy, a digest of each DNSKEY record (which contains the public key) must be loaded into the parent zone, stored as Delegation Signer (DS) records and signed by the parent''s private key (RRSIG DS record), "Resource Records for the DNS Security Extensions" [RFC4034]. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-76">Secure DNS</a>';

#
# Source for table "RDAP_VARIANT"
#

DROP TABLE IF EXISTS `RDAP_VARIANT`;
CREATE TABLE `RDAP_VARIANT` (
  `VARIANT_ID` int(10) NOT NULL AUTO_INCREMENT,
  `LDH_NAME` varchar(255) COLLATE utf8_bin NOT NULL,
  `UNICODE_NAME` varchar(1024) COLLATE utf8_bin NOT NULL,
  `IDNTABLE` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`VARIANT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=' Internationalized Domain Names (IDNs) are denoted in this specification by the separation of DNS names in LDH form and Unicode form.  Representation of IDNs in registries is described by the "variants" object, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-26">Variants</a>';

#
# Source for table "RDAP_VCARD_ADR"
#

DROP TABLE IF EXISTS `RDAP_VCARD_ADR`;
CREATE TABLE `RDAP_VCARD_ADR` (
  `ADR_ID` int(10) NOT NULL AUTO_INCREMENT,
  `POST_BOX` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
  `EXT_ADR` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
  `STREET` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
  `CITY` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `SP` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `POSTAL_CODE` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `COUNTRY` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ENTITY_ID` int(10) NOT NULL,
  `TYPE` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '; joined value. eg: home;work',
  `PREF` int(3) DEFAULT NULL,
  PRIMARY KEY (`ADR_ID`),
  KEY `IDX_ADR_ENTITY_ID` (`ENTITY_ID`) USING BTREE,
  CONSTRAINT `FK_RDAP_ENTITY_ADR_ENTITY_ID` FOREIGN KEY (`ENTITY_ID`) REFERENCES `RDAP_ENTITY` (`ENTITY_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=' The entity object class uses jCard [I-D.ietf-jcardcal-jcard] to represent contact information, including postal addresses. jCard has the ability to represent multiple language preferences, multiple email address and phone numbers, and multiple postal addresses in both a structured and unstructured format. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-15">Entity</a>';

#
# Source for table "RDAP_VCARD_TEL"
#

DROP TABLE IF EXISTS `RDAP_VCARD_TEL`;
CREATE TABLE `RDAP_VCARD_TEL` (
  `TEL_ID` int(10) NOT NULL AUTO_INCREMENT,
  `TYPE` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '; joined value. eg: home;work;text;voice;fax;cell;video;pager;textphone',
  `GLOBAL_NUMBER` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `EXT_NUMBER` varchar(8) COLLATE utf8_bin DEFAULT NULL,
  `ENTITY_ID` int(10) NOT NULL,
  `PREF` int(3) DEFAULT NULL,
  PRIMARY KEY (`TEL_ID`),
  KEY `IDX_TEL_ENTITY_ID` (`ENTITY_ID`) USING BTREE,
  CONSTRAINT `FK_RDAP_ENTITY_TEL_ENTITY_ID` FOREIGN KEY (`ENTITY_ID`) REFERENCES `RDAP_ENTITY` (`ENTITY_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=' The entity object class uses jCard [I-D.ietf-jcardcal-jcard] to represent contact information, including postal addresses. jCard has the ability to represent multiple language preferences, multiple email address and phone numbers, and multiple postal addresses in both a structured and unstructured format, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-15">Entity</a>';

#
# Source for table "REL_DOMAIN_NAMESERVER"
#

DROP TABLE IF EXISTS `REL_DOMAIN_NAMESERVER`;
CREATE TABLE `REL_DOMAIN_NAMESERVER` (
  `REL_DOMAIN_NS_ID` bigint(12) unsigned NOT NULL AUTO_INCREMENT,
  `DOMAIN_ID` int(10) NOT NULL,
  `NAMESERVER_ID` int(10) NOT NULL,
  `DOMAIN_TYPE` varchar(6) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`REL_DOMAIN_NS_ID`),
  KEY `IDX_REL_DOMAIN_NAMESERVER_NAMESERVER_ID` (`NAMESERVER_ID`) USING BTREE,
  KEY `IDX_REL_DOMAIN_NAMESERVER_DOMAIN_ID` (`DOMAIN_TYPE`,`DOMAIN_ID`) USING BTREE,
  CONSTRAINT `FK_REL_DOMAIN_NAMESERVER_NAMESERVER_ID` FOREIGN KEY (`NAMESERVER_ID`) REFERENCES `RDAP_NAMESERVER` (`NAMESERVER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='define relationship between a <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-25">Domain</a> and its <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#section-6.2">Nameserver</a>';

#
# Source for table "REL_DOMAIN_VARIANT"
#

DROP TABLE IF EXISTS `REL_DOMAIN_VARIANT`;
CREATE TABLE `REL_DOMAIN_VARIANT` (
  `REL_VARIANT_ID` int(10) NOT NULL AUTO_INCREMENT,
  `DOMAIN_ID` int(10) NOT NULL,
  `VARIANT_TYPE` varchar(25) COLLATE utf8_bin NOT NULL,
  `VARIANT_ID` int(10) NOT NULL,
  PRIMARY KEY (`REL_VARIANT_ID`),
  KEY `IDX_REL_DOMAIN_VARIANT_VARIANT_ID` (`VARIANT_ID`) USING BTREE,
  KEY `IDX_REL_DOMAIN_VARIANT_DOMAIN_ID` (`DOMAIN_ID`) USING BTREE,
  CONSTRAINT `FK_REL_DOMAIN_VARIANT_DOMAIN_ID` FOREIGN KEY (`DOMAIN_ID`) REFERENCES `RDAP_DOMAIN` (`DOMAIN_ID`),
  CONSTRAINT `FK_REL_DOMAIN_VARIANT_VARIANT_ID` FOREIGN KEY (`VARIANT_ID`) REFERENCES `RDAP_VARIANT` (`VARIANT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='define relationship between <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06##section-6.3">domain</a> and its <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-26">variants</a> ';

#
# Source for table "REL_ENTITY_REGISTRATION"
#

DROP TABLE IF EXISTS `REL_ENTITY_REGISTRATION`;
CREATE TABLE `REL_ENTITY_REGISTRATION` (
  `REL_ENTITY_ID` int(10) NOT NULL AUTO_INCREMENT,
  `REL_ID` int(10) NOT NULL,
  `REL_OBJECT_TYPE` varchar(16) COLLATE utf8_bin NOT NULL,
  `ENTITY_ID` int(10) NOT NULL,
  `ENTITY_ROLE` varchar(32) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT 'single role name',
  PRIMARY KEY (`REL_ENTITY_ID`),
  KEY `IDX_REL_ENTITY_ENTITY_ID` (`ENTITY_ID`) USING BTREE,
  KEY `IDX_REL_ENTITY_REL_ID` (`REL_ID`,`REL_OBJECT_TYPE`) USING BTREE,
  CONSTRAINT `FK_REL_ENTITY_REGISTRATION_ENTITY_ID` FOREIGN KEY (`ENTITY_ID`) REFERENCES `RDAP_ENTITY` (`ENTITY_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='define relationship between an <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-15">Entity</a> and the registration object(Entity, Domain, IP network, Autnum and Nameserver)';

#
# Source for table "REL_EVENT_REGISTRATION"
#

DROP TABLE IF EXISTS `REL_EVENT_REGISTRATION`;
CREATE TABLE `REL_EVENT_REGISTRATION` (
  `REL_EVENT_ID` int(10) NOT NULL AUTO_INCREMENT,
  `REL_ID` int(10) NOT NULL,
  `REL_OBJECT_TYPE` varchar(16) COLLATE utf8_bin NOT NULL,
  `EVENT_ID` int(10) NOT NULL,
  PRIMARY KEY (`REL_EVENT_ID`),
  KEY `IDX_REL_EVENT_EVENT_ID` (`EVENT_ID`) USING BTREE,
  KEY `IDX_REL_EVENT_REL_ID` (`REL_ID`,`REL_OBJECT_TYPE`) USING BTREE,
  CONSTRAINT `FK_REL_EVENT_REGISTRATION_EVENT_ID` FOREIGN KEY (`EVENT_ID`) REFERENCES `RDAP_EVENT` (`EVENT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=' define relationship between <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-11">Event</a> and the  registration object(Entity, Domain, IP network, Autnum and Nameserver)';

#
# Source for table "REL_LINK_OBJECT"
#

DROP TABLE IF EXISTS `REL_LINK_OBJECT`;
CREATE TABLE `REL_LINK_OBJECT` (
  `REL_LINK_ID` int(10) NOT NULL AUTO_INCREMENT,
  `REL_ID` int(10) NOT NULL,
  `REL_OBJECT_TYPE` varchar(16) COLLATE utf8_bin NOT NULL,
  `LINK_ID` int(10) NOT NULL,
  PRIMARY KEY (`REL_LINK_ID`),
  KEY `IDX_REL_LINK_LINK_ID` (`LINK_ID`) USING BTREE,
  KEY `IDX_REL_LINK_REL_ID` (`REL_ID`,`REL_OBJECT_TYPE`) USING BTREE,
  CONSTRAINT `FK_REL_LINK_OBJECT_LINK_ID` FOREIGN KEY (`LINK_ID`) REFERENCES `RDAP_LINK` (`LINK_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=' define relationship between <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-8">Link</a> and regsitration object(Entity, Domain, IP network, Autnum, Nameserver, Remark, Notice .etc)';

#
# Source for table "REL_NOTICE_REGISTRATION"
#

DROP TABLE IF EXISTS `REL_NOTICE_REGISTRATION`;
CREATE TABLE `REL_NOTICE_REGISTRATION` (
  `REL_NOTICE_ID` int(10) NOT NULL AUTO_INCREMENT,
  `REL_ID` int(10) NOT NULL,
  `REL_OBJECT_TYPE` varchar(16) COLLATE utf8_bin NOT NULL,
  `NOTICE_ID` int(10) NOT NULL,
  PRIMARY KEY (`REL_NOTICE_ID`),
  KEY `IDX_REL_NOTICE_NOTICE_ID` (`NOTICE_ID`) USING BTREE,
  KEY `IDX_REL_NOTICE_REL_ID` (`REL_ID`,`REL_OBJECT_TYPE`) USING BTREE,
  CONSTRAINT `FK_REL_NOTICE_REGISTRATION_NOTICE_ID` FOREIGN KEY (`NOTICE_ID`) REFERENCES `RDAP_NOTICE` (`NOTICE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=' define relationship between <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-9">Notice</a> and registration object(Entity, Domain, IP network, Autnum and Nameserver)';

#
# Source for table "REL_PUBLICID_REGISTRATION"
#

DROP TABLE IF EXISTS `REL_PUBLICID_REGISTRATION`;
CREATE TABLE `REL_PUBLICID_REGISTRATION` (
  `REL_PUBLICID_ID` int(10) NOT NULL AUTO_INCREMENT,
  `REL_ID` int(10) NOT NULL,
  `REL_OBJECT_TYPE` varchar(16) COLLATE utf8_bin NOT NULL,
  `PUBLIC_ID` int(10) NOT NULL,
  PRIMARY KEY (`REL_PUBLICID_ID`),
  KEY `IDX_REL_PUBLICID_PUBLIC_ID` (`PUBLIC_ID`) USING BTREE,
  KEY `IDX_REL_PUBLICID_REL_ID` (`REL_ID`,`REL_OBJECT_TYPE`) USING BTREE,
  CONSTRAINT `FK_REL_PUBLICID_REGISTRATION_PUBLIC_ID` FOREIGN KEY (`PUBLIC_ID`) REFERENCES `RDAP_PUBLICID` (`PUBLIC_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=' define relationship between <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-13">Public ID</a> and some registration object(Entity, Domain, IP network, Autnum and Nameserver)';

#
# Source for table "REL_SECUREDNS_DSKEY"
#

DROP TABLE IF EXISTS `REL_SECUREDNS_DSKEY`;
CREATE TABLE `REL_SECUREDNS_DSKEY` (
  `REL_SECUREDNS_ID` int(10) NOT NULL AUTO_INCREMENT,
  `SECUREDNS_ID` int(10) NOT NULL,
  `REL_DSKEY_TYPE` varchar(100) COLLATE utf8_bin NOT NULL,
  `REL_ID` int(10) NOT NULL,
  PRIMARY KEY (`REL_SECUREDNS_ID`),
  KEY `IDX_REL_SECUREDNS_SECUREDNS_ID` (`SECUREDNS_ID`) USING BTREE,
  KEY `IDX_REL_SECUREDNS_REL_ID` (`REL_ID`,`REL_DSKEY_TYPE`) USING BTREE,
  CONSTRAINT `FK_REL_SECUREDNS_DSKEY_SECUREDNS_ID` FOREIGN KEY (`SECUREDNS_ID`) REFERENCES `RDAP_SECUREDNS` (`SECUREDNS_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=' define relationship between securedns and dsdata or keydata. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#appendix-D">DNSSEC</a>';

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
