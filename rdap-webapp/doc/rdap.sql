# Host: localhost  (Version: 5.5.34)
# Date: 2014-05-19 15:50:50
# Generator: MySQL-Front 5.3  (Build 2.42)

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;

DROP DATABASE IF EXISTS `rdap`;
CREATE DATABASE `rdap` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `rdap`;

#
# Source for table "const_entity_role"
#

DROP TABLE IF EXISTS `const_entity_role`;
CREATE TABLE `const_entity_role` (
  `role` varchar(100) NOT NULL,
  PRIMARY KEY (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='role is a string signifying the relationship an object would have with its closest containing object, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#section-10.2.3">Roles</a>';

#
# Data for table "const_entity_role"
#


#
# Source for table "const_event_action"
#

DROP TABLE IF EXISTS `const_event_action`;
CREATE TABLE `const_event_action` (
  `action` varchar(100) NOT NULL,
  PRIMARY KEY (`action`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='action is a string denoting the reason for the event, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#section-10.2.2">Action</a>';

#
# Data for table "const_event_action"
#


#
# Source for table "const_status"
#

DROP TABLE IF EXISTS `const_status`;
CREATE TABLE `const_status` (
  `status` varchar(100) NOT NULL,
  PRIMARY KEY (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='status is a string indicating the state of a registered object, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#section-10.2.1">Status</a>';

#
# Data for table "const_status"
#


#
# Source for table "const_variant"
#

DROP TABLE IF EXISTS `const_variant`;
CREATE TABLE `const_variant` (
  `variant_type` varchar(25) NOT NULL,
  PRIMARY KEY (`variant_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='variant type is denoting the relationship between the variants and the containing domain object, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#section-10.2.4">Variant Relations</a>';

#
# Data for table "const_variant"
#


#
# Source for table "rdap_autnum"
#

DROP TABLE IF EXISTS `rdap_autnum`;
CREATE TABLE `rdap_autnum` (
  `as_id` int(10) NOT NULL AUTO_INCREMENT,
  `handle` varchar(100) NOT NULL,
  `start_autnum` bigint(10) DEFAULT NULL,
  `end_autnum` bigint(10) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `country` varchar(2) DEFAULT NULL,
  `lang` varchar(42) DEFAULT ' ',
  `port43` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`as_id`),
  UNIQUE KEY `uk_as_handle` (`handle`) USING BTREE,
  KEY `idx_as_end_autnum` (`end_autnum`,`start_autnum`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='The Autonomous System Number (autnum) object class models Autonomous System Number registrations found in RIRs and represents the expected response to an "/autnum" query as defined by [I-D.ietf-weirds-rdap-query].  There is no equivalent object class for DNRs.  The high level structure of the autnum object class consists of information about the network registration and entities related to the autnum registration (e.g. registrant information, contacts, etc.), and is similar to the IP Network entity object class., reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-41"> Autonomous System Number (autnum)</a>';

#
# Data for table "rdap_autnum"
#


#
# Source for table "rdap_autnum_redirect"
#

DROP TABLE IF EXISTS `rdap_autnum_redirect`;
CREATE TABLE `rdap_autnum_redirect` (
  `as_redirect_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `start_number` int(11) unsigned NOT NULL,
  `end_number` int(11) unsigned NOT NULL,
  `redirect_url` varchar(4096) NOT NULL,
  PRIMARY KEY (`as_redirect_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='rdap_autnum_redirect is modelled on old table, which is to be modified by bootstraps';

#
# Data for table "rdap_autnum_redirect"
#


#
# Source for table "rdap_autnum_status"
#

DROP TABLE IF EXISTS `rdap_autnum_status`;
CREATE TABLE `rdap_autnum_status` (
  `as_status_id` int(10) NOT NULL AUTO_INCREMENT,
  `as_id` int(10) NOT NULL,
  `status` varchar(20) NOT NULL,
  PRIMARY KEY (`as_status_id`),
  KEY `idx_status_as_id` (`as_id`) USING BTREE,
  CONSTRAINT `fk_rdap_autnum_status_as_id` FOREIGN KEY (`as_id`) REFERENCES `rdap_autnum` (`as_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='state of an autnum, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-52">Status</a>';

#
# Data for table "rdap_autnum_status"
#


#
# Source for table "rdap_conformance"
#

DROP TABLE IF EXISTS `rdap_conformance`;
CREATE TABLE `rdap_conformance` (
  `conformance_id` int(10) unsigned NOT NULL,
  `rdap_conformance` varchar(2048) NOT NULL,
  PRIMARY KEY (`conformance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Rdap conformance providing a hint as to the specifications used in the construction of the response. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-8">RDAP Conformance</a>';

#
# Data for table "rdap_conformance"
#


#
# Source for table "rdap_domain"
#

DROP TABLE IF EXISTS `rdap_domain`;
CREATE TABLE `rdap_domain` (
  `domain_id` int(10) NOT NULL AUTO_INCREMENT,
  `handle` varchar(100) NOT NULL,
  `ldh_name` varchar(255) NOT NULL,
  `unicode_name` varchar(1024) DEFAULT NULL,
  `port43` varchar(4096) DEFAULT NULL,
  `lang` varchar(42) DEFAULT NULL,
  `type` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`domain_id`),
  UNIQUE KEY `uk_domain_handle` (`handle`) USING BTREE,
  KEY `idx_domain_ldh_name` (`ldh_name`) USING BTREE,
  KEY `idx_domain_unicode_name` (`unicode_name`(255)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='The domain object class represents a DNS name and point of delegation.  For RIRs these delegation points are in the reverse DNS tree, whereas for DNRs these delegation points are in the forward DNS tree. In both cases, the high level structure of the domain object class consists of information about the domain registration, nameserver information related to the domain name, and entities related to the domain name (e.g. registrant information, contacts, etc.). Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-25">Domain</a>';

#
# Data for table "rdap_domain"
#


#
# Source for table "rdap_domain_redirect"
#

DROP TABLE IF EXISTS `rdap_domain_redirect`;
CREATE TABLE `rdap_domain_redirect` (
  `redirect_type` varchar(10) NOT NULL,
  `redirect_url` varchar(4096) NOT NULL,
  `rdap_domain_redirect_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`rdap_domain_redirect_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='rdap_domain_redirect is modelled on old table, which is to be modified by bootstraps';

#
# Data for table "rdap_domain_redirect"
#


#
# Source for table "rdap_domain_status"
#

DROP TABLE IF EXISTS `rdap_domain_status`;
CREATE TABLE `rdap_domain_status` (
  `domain_status_id` int(10) NOT NULL AUTO_INCREMENT,
  `domain_id` int(10) NOT NULL,
  `status` varchar(20) NOT NULL,
  PRIMARY KEY (`domain_status_id`),
  KEY `idx_domain_id` (`domain_id`) USING BTREE,
  CONSTRAINT `fk_rdap_domain_status_domain_id` FOREIGN KEY (`domain_id`) REFERENCES `rdap_domain` (`domain_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='state of domain, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-52">Status</a>';

#
# Data for table "rdap_domain_status"
#


#
# Source for table "rdap_dsdata"
#

DROP TABLE IF EXISTS `rdap_dsdata`;
CREATE TABLE `rdap_dsdata` (
  `dsdata_id` int(10) NOT NULL AUTO_INCREMENT,
  `key_tag` int(5) NOT NULL,
  `algorithm` int(3) NOT NULL,
  `digest` varchar(512) NOT NULL,
  `digest_type` int(3) NOT NULL,
  PRIMARY KEY (`dsdata_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Ds data for domain DNSSEC, DNSSEC provides data integrity for DNS through digital signing of resource records.  To enable DNSSEC, the zone is signed by one or more private keys and the signatures stored as RRSIG records.  To complete the chain of trust in the DNS zone hierarchy, a digest of each DNSKEY record (which contains the public key) must be loaded into the parent zone, stored as Delegation Signer (DS) records and signed by the parent''s private key (RRSIG DS record), "Resource Records for the DNS Security Extensions" [RFC4034]. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-25">Domain</a>';

#
# Data for table "rdap_dsdata"
#


#
# Source for table "rdap_entity"
#

DROP TABLE IF EXISTS `rdap_entity`;
CREATE TABLE `rdap_entity` (
  `entity_id` int(10) NOT NULL AUTO_INCREMENT,
  `handle` varchar(255) NOT NULL,
  `kind` varchar(15) DEFAULT NULL,
  `fn` varchar(100) NOT NULL,
  `email` varchar(256) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `org` varchar(100) DEFAULT NULL,
  `url` varchar(4096) DEFAULT NULL,
  `port43` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`entity_id`),
  UNIQUE KEY `uk_entity_handle` (`handle`),
  KEY `idx_entity_fn` (`fn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='The entity object class represents the information of organizations, corporations, governments, non-profits, clubs, individual persons, and informal groups of people.  All of these representations are so similar that it is best to represent them in JSON [RFC4627] with one construct, the entity object class, to aid in the re-use of code by implementers. The entity object is served by both RIRs and DNRs. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-15">Entity</a>';

#
# Data for table "rdap_entity"
#


#
# Source for table "rdap_entity_status"
#

DROP TABLE IF EXISTS `rdap_entity_status`;
CREATE TABLE `rdap_entity_status` (
  `entity_status_id` int(10) NOT NULL AUTO_INCREMENT,
  `entity_id` int(10) NOT NULL,
  `status` varchar(20) NOT NULL,
  PRIMARY KEY (`entity_status_id`),
  KEY `idx_status_entity_id` (`entity_id`) USING BTREE,
  CONSTRAINT `fk_rdap_entity_status_entity_id` FOREIGN KEY (`entity_id`) REFERENCES `rdap_entity` (`entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='state of entity, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-52">Status</a>';

#
# Data for table "rdap_entity_status"
#


#
# Source for table "rdap_errormessage"
#

DROP TABLE IF EXISTS `rdap_errormessage`;
CREATE TABLE `rdap_errormessage` (
  `error_id` int(10) NOT NULL AUTO_INCREMENT,
  `error_code` int(10) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `description` varchar(1024) NOT NULL,
  `lang` varchar(42) DEFAULT NULL,
  PRIMARY KEY (`error_id`),
  UNIQUE KEY `uk_error_code` (`error_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='Some non-answer responses may return entity bodies with information that could be more descriptive. The basic structure of that response is an object class containing an error code number (corresponding to the HTTP response code) followed by a string named "title" followed by an array of strings named "description". Reference to <a href=" http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-44">Error Response Body</a>';

#
# Data for table "rdap_errormessage"
#

INSERT INTO `rdap_errormessage` VALUES (1,400,'BAD REQUEST','BAD REQUEST DESC','cn'),(2,404,'NOT FOUND','NOT FOUND',NULL),(3,500,'INTERNAL SERVER ERROR','INTERNAL SERVER ERROR',NULL);

#
# Source for table "rdap_event"
#

DROP TABLE IF EXISTS `rdap_event`;
CREATE TABLE `rdap_event` (
  `event_id` int(10) NOT NULL AUTO_INCREMENT,
  `event_action` varchar(15) NOT NULL,
  `event_actor` varchar(100) DEFAULT NULL,
  `event_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`event_id`),
  KEY `idx_event_actor` (`event_actor`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='Events that have occurred on an instance of an object class, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-11">Event</a>';

#
# Data for table "rdap_event"
#


#
# Source for table "rdap_ip"
#

DROP TABLE IF EXISTS `rdap_ip`;
CREATE TABLE `rdap_ip` (
  `ip_id` int(10) NOT NULL AUTO_INCREMENT,
  `handle` varchar(100) NOT NULL,
  `startlowaddress` bigint(10) DEFAULT NULL,
  `starthighaddress` bigint(10) DEFAULT NULL,
  `endlowaddress` bigint(10) DEFAULT NULL,
  `endhighaddress` bigint(10) DEFAULT NULL,
  `version` varchar(2) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `country` varchar(2) DEFAULT NULL,
  `parent_handle` varchar(100) DEFAULT NULL,
  `lang` varchar(42) DEFAULT NULL,
  `port43` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`ip_id`),
  UNIQUE KEY `uk_ip_handle` (`handle`) USING BTREE,
  KEY `idx_ip_startlowaddress` (`startlowaddress`,`starthighaddress`,`endlowaddress`,`endhighaddress`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' The IP Network object class models IP network registrations found in RIRs and is the expected response for the "/ip" query as defined by [I-D.ietf-weirds-rdap-query].  There is no equivalent object class for DNRs.  The high level structure of the IP network object class consists of information about the network registration and entities related to the IP network (e.g. registrant information, contacts, etc...). Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-37">IP Network Object Class </a>';

#
# Data for table "rdap_ip"
#


#
# Source for table "rdap_ip_redirect"
#

DROP TABLE IF EXISTS `rdap_ip_redirect`;
CREATE TABLE `rdap_ip_redirect` (
  `rdap_ip_redirect_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `start_high_address` bigint(11) DEFAULT NULL,
  `start_low_address` bigint(11) DEFAULT NULL,
  `end_high_address` bigint(11) DEFAULT NULL,
  `end_low_address` bigint(11) DEFAULT NULL,
  `redirect_url` varchar(4096) NOT NULL,
  PRIMARY KEY (`rdap_ip_redirect_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='rdap_ip_redirect is modelled on old table, which is to be modified by bootstraps';

#
# Data for table "rdap_ip_redirect"
#


#
# Source for table "rdap_ip_status"
#

DROP TABLE IF EXISTS `rdap_ip_status`;
CREATE TABLE `rdap_ip_status` (
  `ip_status_id` int(10) NOT NULL AUTO_INCREMENT,
  `ip_id` int(10) NOT NULL,
  `status` varchar(20) NOT NULL,
  PRIMARY KEY (`ip_status_id`),
  KEY `idx_ip_status_ip_id` (`ip_id`) USING BTREE,
  CONSTRAINT `fk_rdap_ip_status_ip_id` FOREIGN KEY (`ip_id`) REFERENCES `rdap_ip` (`ip_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='state of IP Network object, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-52">Status</a>';

#
# Data for table "rdap_ip_status"
#


#
# Source for table "rdap_keydata"
#

DROP TABLE IF EXISTS `rdap_keydata`;
CREATE TABLE `rdap_keydata` (
  `keydata_id` int(10) NOT NULL AUTO_INCREMENT,
  `flags` int(3) NOT NULL,
  `protocol` int(1) NOT NULL,
  `public_key` varchar(1024) NOT NULL,
  `algorithm` int(3) NOT NULL,
  PRIMARY KEY (`keydata_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Key data for domain DNSSEC, DNSSEC provides data integrity for DNS through digital signing of resource records.  To enable DNSSEC, the zone is signed by one or more private keys and the signatures stored as RRSIG records.  To complete the chain of trust in the DNS zone hierarchy, a digest of each DNSKEY record (which contains the public key) must be loaded into the parent zone, stored as Delegation Signer (DS) records and signed by the parent''s private key (RRSIG DS record), "Resource Records for the DNS Security Extensions" [RFC4034]. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-25">Domain</a>';

#
# Data for table "rdap_keydata"
#


#
# Source for table "rdap_link"
#

DROP TABLE IF EXISTS `rdap_link`;
CREATE TABLE `rdap_link` (
  `link_id` int(10) NOT NULL AUTO_INCREMENT,
  `value` varchar(4098) NOT NULL,
  `rel` varchar(50) NOT NULL,
  `href` varchar(4098) NOT NULL,
  `media` varchar(50) DEFAULT NULL,
  `type` varchar(50) NOT NULL,
  PRIMARY KEY (`link_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT=' The "links" array is found in data structures to signify links to other resources on the Internet.  The relationship of these links is defined by the IANA registry described by [RFC5988]. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-8"> Links </a>';

#
# Data for table "rdap_link"
#


#
# Source for table "rdap_link_hreflang"
#

DROP TABLE IF EXISTS `rdap_link_hreflang`;
CREATE TABLE `rdap_link_hreflang` (
  `hreflang_id` int(10) NOT NULL AUTO_INCREMENT,
  `link_id` int(10) NOT NULL,
  `hreflang` varchar(42) DEFAULT NULL,
  PRIMARY KEY (`hreflang_id`),
  KEY `idx_link_hreflang_link_id` (`link_id`) USING BTREE,
  CONSTRAINT `fk_rdap_link_hreflang_link_id` FOREIGN KEY (`link_id`) REFERENCES `rdap_link` (`link_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='link href''s lang, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-8"> Links </a>';

#
# Data for table "rdap_link_hreflang"
#


#
# Source for table "rdap_link_title"
#

DROP TABLE IF EXISTS `rdap_link_title`;
CREATE TABLE `rdap_link_title` (
  `title_id` int(10) NOT NULL AUTO_INCREMENT,
  `link_id` int(10) NOT NULL,
  `title` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`title_id`),
  KEY `idx_link_title_link_id` (`link_id`) USING BTREE,
  CONSTRAINT `fk_rdap_link_title_link_id` FOREIGN KEY (`link_id`) REFERENCES `rdap_link` (`link_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='link titles, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-8"> Links </a>';

#
# Data for table "rdap_link_title"
#


#
# Source for table "rdap_nameserver"
#

DROP TABLE IF EXISTS `rdap_nameserver`;
CREATE TABLE `rdap_nameserver` (
  `nameserver_id` int(10) NOT NULL AUTO_INCREMENT,
  `handle` varchar(100) DEFAULT NULL,
  `ldh_name` varchar(255) NOT NULL,
  `unicode_name` varchar(1024) DEFAULT NULL,
  `port43` varchar(4096) DEFAULT NULL,
  `lang` varchar(42) DEFAULT NULL,
  PRIMARY KEY (`nameserver_id`),
  UNIQUE KEY `uk_nameserver_handle` (`handle`) USING BTREE,
  KEY `idx_nameserver_ldh_name` (`ldh_name`) USING BTREE,
  KEY `idx_nameserver_unicode_name` (`unicode_name`(255)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' The nameserver object class represents information regarding DNS name servers used in both forward and reverse DNS.  RIRs and some DNRs register or expose nameserver information as an attribute of a domain name, while other DNRs model nameservers as "first class objects". The nameserver object class accommodates both models and degrees of variation in between. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#section-6.2">Nameserver</a>';

#
# Data for table "rdap_nameserver"
#


#
# Source for table "rdap_nameserver_ip"
#

DROP TABLE IF EXISTS `rdap_nameserver_ip`;
CREATE TABLE `rdap_nameserver_ip` (
  `nameserver_ip_id` int(10) NOT NULL AUTO_INCREMENT,
  `nameserver_id` int(10) NOT NULL,
  `ip_high` bigint(10) DEFAULT NULL,
  `ip_low` bigint(10) NOT NULL,
  `version` varchar(2) NOT NULL,
  PRIMARY KEY (`nameserver_ip_id`),
  KEY `idx_nameserver_ip_nameserver_id` (`nameserver_id`) USING BTREE,
  KEY `idx_nameserver_ip_ip` (`ip_low`,`ip_high`) USING BTREE,
  CONSTRAINT `fk_rdap_nameserver_ip_nameserver_id` FOREIGN KEY (`nameserver_id`) REFERENCES `rdap_nameserver` (`nameserver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='nameserver ip, NOT RDAP IP object, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#section-6.2">Nameserver</a>';

#
# Data for table "rdap_nameserver_ip"
#


#
# Source for table "rdap_nameserver_status"
#

DROP TABLE IF EXISTS `rdap_nameserver_status`;
CREATE TABLE `rdap_nameserver_status` (
  `nameserver_status_id` int(10) NOT NULL AUTO_INCREMENT,
  `nameserver_id` int(10) NOT NULL,
  `status` varchar(20) NOT NULL,
  PRIMARY KEY (`nameserver_status_id`),
  KEY `idx_nameserver_status_nameserver_id` (`nameserver_id`) USING BTREE,
  CONSTRAINT `fk_rdap_nameserver_status_nameserver_id` FOREIGN KEY (`nameserver_id`) REFERENCES `rdap_nameserver` (`nameserver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='nameserver status , reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-52">Status</a>';

#
# Data for table "rdap_nameserver_status"
#


#
# Source for table "rdap_notice"
#

DROP TABLE IF EXISTS `rdap_notice`;
CREATE TABLE `rdap_notice` (
  `notice_id` int(10) NOT NULL AUTO_INCREMENT,
  `type` varchar(6) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='The "notices", "help" and "remarks" data structures take the same form.  The "notices" structure denotes information about the service providing RDAP information, whereas the "remarks" structure denotes information about the object class, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-9">Notices And Remarks</a>';

#
# Data for table "rdap_notice"
#


#
# Source for table "rdap_notice_description"
#

DROP TABLE IF EXISTS `rdap_notice_description`;
CREATE TABLE `rdap_notice_description` (
  `notice_desc_id` int(10) NOT NULL AUTO_INCREMENT,
  `notice_id` int(10) NOT NULL,
  `description` varchar(2048) NOT NULL,
  PRIMARY KEY (`notice_desc_id`),
  KEY `idx_notice_description_notice_id` (`notice_id`) USING BTREE,
  CONSTRAINT `fk_rdap_notice_description_notice_id` FOREIGN KEY (`notice_id`) REFERENCES `rdap_notice` (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='Description for notice\r\nemark for registration objecthelp, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-9">Notices And Remarks</a>';

#
# Data for table "rdap_notice_description"
#


#
# Source for table "rdap_publicid"
#

DROP TABLE IF EXISTS `rdap_publicid`;
CREATE TABLE `rdap_publicid` (
  `public_id` int(10) NOT NULL AUTO_INCREMENT,
  `identifier` varchar(256) NOT NULL,
  `type` varchar(256) NOT NULL,
  PRIMARY KEY (`public_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='The publicid maps a public identifier to a registration object, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-13">Public IDs</a>';

#
# Data for table "rdap_publicid"
#


#
# Source for table "rdap_securedns"
#

DROP TABLE IF EXISTS `rdap_securedns`;
CREATE TABLE `rdap_securedns` (
  `securedns_id` int(10) NOT NULL AUTO_INCREMENT,
  `zone_signed` tinyint(1) DEFAULT NULL,
  `delegation_signed` tinyint(1) NOT NULL,
  `max_sigLife` int(11) DEFAULT NULL,
  `domain_id` int(10) NOT NULL,
  PRIMARY KEY (`securedns_id`),
  KEY `idx_securedns_domain_id` (`domain_id`) USING BTREE,
  CONSTRAINT `fk_rdap_securedns_domain_id` FOREIGN KEY (`domain_id`) REFERENCES `rdap_domain` (`domain_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SecureDNS member to represent secure DNS information about domain names.  DNSSEC provides data integrity for DNS through digital signing of resource records.  To enable DNSSEC, the zone is signed by one or more private keys and the signatures stored as RRSIG records.  To complete the chain of trust in the DNS zone hierarchy, a digest of each DNSKEY record (which contains the public key) must be loaded into the parent zone, stored as Delegation Signer (DS) records and signed by the parent''s private key (RRSIG DS record), "Resource Records for the DNS Security Extensions" [RFC4034]. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-76">Secure DNS</a>';

#
# Data for table "rdap_securedns"
#


#
# Source for table "rdap_variant"
#

DROP TABLE IF EXISTS `rdap_variant`;
CREATE TABLE `rdap_variant` (
  `variant_id` int(10) NOT NULL AUTO_INCREMENT,
  `ldh_name` varchar(255) NOT NULL,
  `unicode_name` varchar(1024) NOT NULL,
  `idntable` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`variant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' Internationalized Domain Names (IDNs) are denoted in this specification by the separation of DNS names in LDH form and Unicode form.  Representation of IDNs in registries is described by the "variants" object, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-26">Variants</a>';

#
# Data for table "rdap_variant"
#


#
# Source for table "rdap_vcard_adr"
#

DROP TABLE IF EXISTS `rdap_vcard_adr`;
CREATE TABLE `rdap_vcard_adr` (
  `adr_id` int(10) NOT NULL AUTO_INCREMENT,
  `post_box` varchar(1024) DEFAULT NULL,
  `ext_adr` varchar(1024) DEFAULT NULL,
  `street` varchar(1024) DEFAULT NULL,
  `city` varchar(256) DEFAULT NULL,
  `sp` varchar(64) DEFAULT NULL,
  `postal_code` varchar(16) DEFAULT NULL,
  `country` varchar(64) DEFAULT NULL,
  `entity_id` int(10) NOT NULL,
  PRIMARY KEY (`adr_id`),
  KEY `idx_adr_entity_id` (`entity_id`) USING BTREE,
  CONSTRAINT `fk_rdap_entity_adr_entity_id` FOREIGN KEY (`entity_id`) REFERENCES `rdap_entity` (`entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' The entity object class uses jCard [I-D.ietf-jcardcal-jcard] to represent contact information, including postal addresses. jCard has the ability to represent multiple language preferences, multiple email address and phone numbers, and multiple postal addresses in both a structured and unstructured format. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-15">Entity</a>';

#
# Data for table "rdap_vcard_adr"
#


#
# Source for table "rdap_vcard_tel"
#

DROP TABLE IF EXISTS `rdap_vcard_tel`;
CREATE TABLE `rdap_vcard_tel` (
  `tel_id` int(10) NOT NULL AUTO_INCREMENT,
  `tel_type` varchar(10) DEFAULT NULL,
  `data_type` varchar(100) DEFAULT NULL,
  `data_value` varchar(4096) DEFAULT NULL,
  `entity_id` int(10) NOT NULL,
  PRIMARY KEY (`tel_id`),
  KEY `idx_tel_entity_id` (`entity_id`) USING BTREE,
  CONSTRAINT `fk_rdap_entity_tel_entity_id` FOREIGN KEY (`entity_id`) REFERENCES `rdap_entity` (`entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' The entity object class uses jCard [I-D.ietf-jcardcal-jcard] to represent contact information, including postal addresses. jCard has the ability to represent multiple language preferences, multiple email address and phone numbers, and multiple postal addresses in both a structured and unstructured format, reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-15">Entity</a>';

#
# Data for table "rdap_vcard_tel"
#


#
# Source for table "rel_domain_nameserver"
#

DROP TABLE IF EXISTS `rel_domain_nameserver`;
CREATE TABLE `rel_domain_nameserver` (
  `rel_domain_ns_id` bigint(12) unsigned NOT NULL AUTO_INCREMENT,
  `domain_id` int(10) NOT NULL,
  `nameserver_id` int(10) NOT NULL,
  PRIMARY KEY (`rel_domain_ns_id`),
  KEY `idx_rel_domain_nameserver_nameserver_id` (`nameserver_id`) USING BTREE,
  KEY `idx_rel_domain_nameserver_domain_id` (`domain_id`) USING BTREE,
  CONSTRAINT `fk_rel_domain_nameserver_domain_id` FOREIGN KEY (`domain_id`) REFERENCES `rdap_domain` (`domain_id`),
  CONSTRAINT `fk_rel_domain_nameserver_nameserver_id` FOREIGN KEY (`nameserver_id`) REFERENCES `rdap_nameserver` (`nameserver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='define relationship between a <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-25">Domain</a> and its <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#section-6.2">Nameserver</a>';

#
# Data for table "rel_domain_nameserver"
#


#
# Source for table "rel_domain_network"
#

DROP TABLE IF EXISTS `rel_domain_network`;
CREATE TABLE `rel_domain_network` (
  `rel_domain_network_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `domain_id` int(10) NOT NULL,
  `network_id` int(10) NOT NULL,
  PRIMARY KEY (`rel_domain_network_id`),
  KEY `idx_rel_domain_network_network_id` (`network_id`) USING BTREE,
  KEY `idx_rel_domain_network_domain_id` (`domain_id`) USING BTREE,
  CONSTRAINT `fk_rel_domain_network_domain_id` FOREIGN KEY (`domain_id`) REFERENCES `rdap_domain` (`domain_id`),
  CONSTRAINT `fk_rel_domain_network_network_id` FOREIGN KEY (`network_id`) REFERENCES `rdap_ip` (`ip_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='define relationship between a <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-25">Domain</a> and its <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-37">Network</a>';

#
# Data for table "rel_domain_network"
#


#
# Source for table "rel_domain_variant"
#

DROP TABLE IF EXISTS `rel_domain_variant`;
CREATE TABLE `rel_domain_variant` (
  `rel_variant_id` int(10) NOT NULL AUTO_INCREMENT,
  `domain_id` int(10) NOT NULL,
  `variant_type` varchar(25) NOT NULL,
  `variant_id` int(10) NOT NULL,
  PRIMARY KEY (`rel_variant_id`),
  KEY `idx_rel_domain_variant_variant_id` (`variant_id`) USING BTREE,
  KEY `idx_rel_domain_variant_domain_id` (`domain_id`) USING BTREE,
  CONSTRAINT `fk_rel_domain_variant_domain_id` FOREIGN KEY (`domain_id`) REFERENCES `rdap_domain` (`domain_id`),
  CONSTRAINT `fk_rel_domain_variant_variant_id` FOREIGN KEY (`variant_id`) REFERENCES `rdap_variant` (`variant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='define relationship between <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06##section-6.3">domain</a> and its <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-26">variants</a> ';

#
# Data for table "rel_domain_variant"
#


#
# Source for table "rel_entity_registration"
#

DROP TABLE IF EXISTS `rel_entity_registration`;
CREATE TABLE `rel_entity_registration` (
  `rel_entity_id` int(10) NOT NULL AUTO_INCREMENT,
  `rel_id` int(10) NOT NULL,
  `rel_object_type` varchar(16) NOT NULL,
  `entity_id` int(10) NOT NULL,
  `entity_role` varchar(15) NOT NULL,
  PRIMARY KEY (`rel_entity_id`),
  KEY `idx_rel_entity_entity_id` (`entity_id`) USING BTREE,
  KEY `idx_rel_entity_rel_id` (`rel_id`,`rel_object_type`) USING BTREE,
  CONSTRAINT `fk_rel_entity_registration_entity_id` FOREIGN KEY (`entity_id`) REFERENCES `rdap_entity` (`entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='define relationship between an <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-15">Entity</a> and the registration object(Entity, Domain, IP network, Autnum and Nameserver)';

#
# Data for table "rel_entity_registration"
#


#
# Source for table "rel_event_registration"
#

DROP TABLE IF EXISTS `rel_event_registration`;
CREATE TABLE `rel_event_registration` (
  `rel_event_id` int(10) NOT NULL AUTO_INCREMENT,
  `rel_id` int(10) NOT NULL,
  `rel_object_type` varchar(16) NOT NULL,
  `event_id` int(10) NOT NULL,
  PRIMARY KEY (`rel_event_id`),
  KEY `idx_rel_event_event_id` (`event_id`) USING BTREE,
  KEY `idx_rel_event_rel_id` (`rel_id`,`rel_object_type`) USING BTREE,
  CONSTRAINT `fk_rel_event_registration_event_id` FOREIGN KEY (`event_id`) REFERENCES `rdap_event` (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT=' define relationship between <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-11">Event</a> and the  registration object(Entity, Domain, IP network, Autnum and Nameserver)';

#
# Data for table "rel_event_registration"
#


#
# Source for table "rel_link_object"
#

DROP TABLE IF EXISTS `rel_link_object`;
CREATE TABLE `rel_link_object` (
  `rel_link_id` int(10) NOT NULL AUTO_INCREMENT,
  `rel_id` int(10) NOT NULL,
  `rel_object_type` varchar(16) NOT NULL,
  `link_id` int(10) NOT NULL,
  PRIMARY KEY (`rel_link_id`),
  KEY `idx_rel_link_link_id` (`link_id`) USING BTREE,
  KEY `idx_rel_link_rel_id` (`rel_id`,`rel_object_type`) USING BTREE,
  CONSTRAINT `fk_rel_link_object_link_id` FOREIGN KEY (`link_id`) REFERENCES `rdap_link` (`link_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT=' define relationship between <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-8">Link</a> and regsitration object(Entity, Domain, IP network, Autnum, Nameserver, Remark, Notice .etc)';

#
# Data for table "rel_link_object"
#


#
# Source for table "rel_notice_registration"
#

DROP TABLE IF EXISTS `rel_notice_registration`;
CREATE TABLE `rel_notice_registration` (
  `rel_notice_id` int(10) NOT NULL AUTO_INCREMENT,
  `rel_id` int(10) NOT NULL,
  `rel_object_type` varchar(16) NOT NULL,
  `notice_id` int(10) NOT NULL,
  PRIMARY KEY (`rel_notice_id`),
  KEY `idx_rel_notice_notice_id` (`notice_id`) USING BTREE,
  KEY `idx_rel_notice_rel_id` (`rel_id`,`rel_object_type`) USING BTREE,
  CONSTRAINT `fk_rel_notice_registration_notice_id` FOREIGN KEY (`notice_id`) REFERENCES `rdap_notice` (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT=' define relationship between <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-9">Notice</a> and registration object(Entity, Domain, IP network, Autnum and Nameserver)';

#
# Data for table "rel_notice_registration"
#


#
# Source for table "rel_publicid_registration"
#

DROP TABLE IF EXISTS `rel_publicid_registration`;
CREATE TABLE `rel_publicid_registration` (
  `rel_publicid_id` int(10) NOT NULL AUTO_INCREMENT,
  `rel_id` int(10) NOT NULL,
  `rel_object_type` varchar(16) NOT NULL,
  `public_id` int(10) NOT NULL,
  PRIMARY KEY (`rel_publicid_id`),
  KEY `idx_rel_publicid_public_id` (`public_id`) USING BTREE,
  KEY `idx_rel_publicid_rel_id` (`rel_id`,`rel_object_type`) USING BTREE,
  CONSTRAINT `fk_rel_publicid_registration_public_id` FOREIGN KEY (`public_id`) REFERENCES `rdap_publicid` (`public_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' define relationship between <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#page-13">Public ID</a> and some registration object(Entity, Domain, IP network, Autnum and Nameserver)';

#
# Data for table "rel_publicid_registration"
#


#
# Source for table "rel_securedns_dskey"
#

DROP TABLE IF EXISTS `rel_securedns_dskey`;
CREATE TABLE `rel_securedns_dskey` (
  `rel_securedns_id` int(10) NOT NULL AUTO_INCREMENT,
  `securedns_id` int(10) NOT NULL,
  `rel_dskey_type` varchar(100) NOT NULL,
  `rel_id` int(10) NOT NULL,
  PRIMARY KEY (`rel_securedns_id`),
  KEY `idx_rel_securedns_securedns_id` (`securedns_id`) USING BTREE,
  KEY `idx_rel_securedns_rel_id` (`rel_id`,`rel_dskey_type`) USING BTREE,
  CONSTRAINT `fk_rel_securedns_dskey_securedns_id` FOREIGN KEY (`securedns_id`) REFERENCES `rdap_securedns` (`securedns_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' define relationship between securedns and dsdata or keydata. Reference to <a href="http://tools.ietf.org/html/draft-ietf-weirds-json-response-06#appendix-D">DNSSEC</a>';

#
# Data for table "rel_securedns_dskey"
#


/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
