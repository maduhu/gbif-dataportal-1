-- MySQL dump 10.13  Distrib 5.1.26-rc, for apple-darwin9.0.0b5 (i686)
--
-- Host: krayt    Database: portal
-- ------------------------------------------------------
-- Server version	5.0.51a-community-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Not dumping tablespaces as no INFORMATION_SCHEMA.FILES table on this server
--

--
-- Table structure for table `QRTZ_BLOB_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `trigger_name` varchar(100) NOT NULL,
  `trigger_group` varchar(100) NOT NULL,
  `blob_data` blob,
  PRIMARY KEY  (`trigger_name`,`trigger_group`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `QRTZ_CALENDARS`
--

DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `QRTZ_CALENDARS` (
  `calendar_name` varchar(255) NOT NULL,
  `calendar` blob NOT NULL,
  PRIMARY KEY  (`calendar_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `QRTZ_CRON_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `trigger_name` varchar(100) NOT NULL,
  `trigger_group` varchar(100) NOT NULL,
  `cron_expression` varchar(255) NOT NULL,
  `time_zone_id` varchar(255) default NULL,
  PRIMARY KEY  (`trigger_name`,`trigger_group`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `QRTZ_FIRED_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `entry_id` varchar(95) NOT NULL,
  `trigger_name` varchar(100) NOT NULL,
  `trigger_group` varchar(100) NOT NULL,
  `is_volatile` varchar(1) NOT NULL,
  `instance_name` varchar(255) NOT NULL,
  `fired_time` bigint(13) NOT NULL,
  `priority` int(11) NOT NULL,
  `state` varchar(16) NOT NULL,
  `job_name` varchar(100) default NULL,
  `job_group` varchar(100) default NULL,
  `is_stateful` varchar(1) default NULL,
  `requests_recovery` varchar(1) default NULL,
  PRIMARY KEY  (`entry_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `QRTZ_JOB_DETAILS`
--

DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `job_name` varchar(100) NOT NULL,
  `job_group` varchar(100) NOT NULL,
  `description` varchar(255) default NULL,
  `job_class_name` varchar(128) NOT NULL,
  `is_durable` varchar(1) NOT NULL,
  `is_volatile` varchar(1) NOT NULL,
  `is_stateful` varchar(1) NOT NULL,
  `requests_recovery` varchar(1) NOT NULL,
  `job_data` blob,
  PRIMARY KEY  (`job_name`,`job_group`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `QRTZ_JOB_LISTENERS`
--

DROP TABLE IF EXISTS `QRTZ_JOB_LISTENERS`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `QRTZ_JOB_LISTENERS` (
  `job_name` varchar(100) NOT NULL,
  `job_group` varchar(100) NOT NULL,
  `job_listener` varchar(100) NOT NULL,
  PRIMARY KEY  (`job_name`,`job_group`,`job_listener`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `QRTZ_LOCKS`
--

DROP TABLE IF EXISTS `QRTZ_LOCKS`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `QRTZ_LOCKS` (
  `lock_name` varchar(40) NOT NULL,
  PRIMARY KEY  (`lock_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `QRTZ_PAUSED_TRIGGER_GRPS`
--

DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `trigger_group` varchar(100) NOT NULL,
  PRIMARY KEY  (`trigger_group`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `QRTZ_SCHEDULER_STATE`
--

DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `instance_name` varchar(255) NOT NULL,
  `last_checkin_time` bigint(13) NOT NULL,
  `checkin_interval` bigint(13) NOT NULL,
  PRIMARY KEY  (`instance_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `QRTZ_SIMPLE_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `trigger_name` varchar(100) NOT NULL,
  `trigger_group` varchar(100) NOT NULL,
  `repeat_count` bigint(7) NOT NULL,
  `repeat_interval` bigint(12) NOT NULL,
  `times_triggered` bigint(7) NOT NULL,
  PRIMARY KEY  (`trigger_name`,`trigger_group`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `QRTZ_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `QRTZ_TRIGGERS` (
  `trigger_name` varchar(100) NOT NULL,
  `trigger_group` varchar(100) NOT NULL,
  `job_name` varchar(100) NOT NULL,
  `job_group` varchar(100) NOT NULL,
  `is_volatile` varchar(1) NOT NULL,
  `description` varchar(255) default NULL,
  `next_fire_time` bigint(13) default NULL,
  `prev_fire_time` bigint(13) default NULL,
  `priority` int(11) default NULL,
  `trigger_state` varchar(16) NOT NULL,
  `trigger_type` varchar(8) NOT NULL,
  `start_time` bigint(13) NOT NULL,
  `end_time` bigint(13) default NULL,
  `calendar_name` varchar(255) default NULL,
  `misfire_instr` smallint(2) default NULL,
  `job_data` blob,
  PRIMARY KEY  (`trigger_name`,`trigger_group`),
  KEY `job_name` (`job_name`,`job_group`),
  KEY `ix_qrtz_nft` (`next_fire_time`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `QRTZ_TRIGGER_LISTENERS`
--

DROP TABLE IF EXISTS `QRTZ_TRIGGER_LISTENERS`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `QRTZ_TRIGGER_LISTENERS` (
  `trigger_name` varchar(100) NOT NULL,
  `trigger_group` varchar(100) NOT NULL,
  `trigger_listener` varchar(100) NOT NULL,
  PRIMARY KEY  (`trigger_name`,`trigger_group`,`trigger_listener`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `agent`
--

DROP TABLE IF EXISTS `agent`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `agent` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) default NULL,
  `email` varchar(255) default NULL,
  `telephone` varchar(255) default NULL,
  `created` datetime default NULL,
  `modified` datetime default NULL,
  `deleted` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `bi_relation_tag`
--

DROP TABLE IF EXISTS `bi_relation_tag`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `bi_relation_tag` (
  `id` int(11) NOT NULL auto_increment,
  `tag_id` int(11) default NULL,
  `to_entity_id` int(11) default NULL,
  `from_entity_id` int(11) default NULL,
  `count` int(10) unsigned default NULL,
  `is_system_generated` bit(1) default NULL,
  PRIMARY KEY  (`id`),
  KEY `tag_id` (`tag_id`),
  KEY `ix_bi_relation_tag_tag_id` (`tag_id`),
  KEY `ix_bi_relation_tag_tag_id_to_entity_id` (`tag_id`,`to_entity_id`),
  KEY `ix_bi_relation_tag_tag_id_from_entity_id` (`tag_id`,`from_entity_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `boolean_tag`
--

DROP TABLE IF EXISTS `boolean_tag`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `boolean_tag` (
  `id` int(11) NOT NULL auto_increment,
  `tag_id` int(11) default NULL,
  `entity_id` int(11) default NULL,
  `is_true` tinyint(1) default '0',
  `is_system_generated` bit(1) default NULL,
  PRIMARY KEY  (`id`),
  KEY `tag_id` (`tag_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `catalogue_number`
--

DROP TABLE IF EXISTS `catalogue_number`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `catalogue_number` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `code` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `ix_cn_catalogue_number` (`code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `cell_country`
--

DROP TABLE IF EXISTS `cell_country`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `cell_country` (
  `cell_id` int(11) NOT NULL,
  `iso_country_code` char(2) NOT NULL,
  PRIMARY KEY  (`cell_id`,`iso_country_code`),
  KEY `IX_cell_ids` (`cell_id`),
  KEY `IX_iso_country_codes` (`iso_country_code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `cell_density`
--

DROP TABLE IF EXISTS `cell_density`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `cell_density` (
  `type` smallint(5) unsigned NOT NULL,
  `entity_id` int(10) unsigned NOT NULL,
  `cell_id` smallint(5) unsigned NOT NULL,
  `count` int(10) unsigned default NULL,
  PRIMARY KEY  (`type`,`entity_id`,`cell_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `centi_cell_density`
--

DROP TABLE IF EXISTS `centi_cell_density`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `centi_cell_density` (
  `type` smallint(5) unsigned NOT NULL,
  `entity_id` int(10) unsigned NOT NULL,
  `cell_id` smallint(5) unsigned NOT NULL,
  `centi_cell_id` tinyint(3) unsigned NOT NULL,
  `count` int(10) unsigned default NULL,
  PRIMARY KEY  (`type`,`entity_id`,`cell_id`,`centi_cell_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `classification`
--

DROP TABLE IF EXISTS `classification`;
/*!50001 DROP VIEW IF EXISTS `classification`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `classification` (
  `name` varchar(255),
  `k` varchar(255),
  `p` varchar(255),
  `c` varchar(255),
  `o` varchar(255),
  `f` varchar(255),
  `g` varchar(255),
  `s` varchar(255),
  `concept_id` int(10) unsigned,
  `rank` smallint(5) unsigned,
  `parent_id` int(10) unsigned,
  `name_id` mediumint(8) unsigned,
  `provider` smallint(5) unsigned,
  `resource` smallint(5) unsigned
) */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `collection_code`
--

DROP TABLE IF EXISTS `collection_code`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `collection_code` (
  `id` mediumint(8) unsigned NOT NULL auto_increment,
  `code` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `ix_cc_collection_code` (`code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `common_name`
--

DROP TABLE IF EXISTS `common_name`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `common_name` (
  `id` int(11) NOT NULL auto_increment,
  `taxon_concept_id` int(11) default NULL,
  `name` varchar(255) NOT NULL,
  `iso_language_code` char(2) NOT NULL,
  `language` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `taxon_concept_id` (`taxon_concept_id`),
  KEY `IX_common_name_1` (`name`),
  KEY `IX_common_name_2` (`iso_language_code`),
  KEY `IX_common_name_3` (`language`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `country` (
  `id` int(11) NOT NULL auto_increment,
  `iso_country_code` char(2) default NULL,
  `concept_count` int(11) default '0',
  `species_count` int(11) default '0',
  `occurrence_count` int(11) default '0',
  `occurrence_coordinate_count` int(11) default '0',
  `continent_code` enum('AF','AS','EU','NA','OC','SA','AN') default NULL,
  `region` char(3) default NULL,
  `min_latitude` float default NULL,
  `max_latitude` float default NULL,
  `min_longitude` float default NULL,
  `max_longitude` float default NULL,
  PRIMARY KEY  (`id`),
  KEY `IX_country_iso_country_code` (`iso_country_code`),
  KEY `IX_country_continent_code` (`continent_code`),
  KEY `IX_country_region` (`region`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `country_name`
--

DROP TABLE IF EXISTS `country_name`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `country_name` (
  `id` int(11) NOT NULL auto_increment,
  `country_id` int(11) default NULL,
  `name` varchar(255) default NULL,
  `searchable_name` varchar(255) default NULL,
  `iso_country_code` char(2) default NULL,
  `locale` char(2) default NULL,
  PRIMARY KEY  (`id`),
  KEY `country_id` (`country_id`),
  KEY `IX_country_name_name` (`name`),
  KEY `IX_country_name_searchable_name` (`searchable_name`),
  KEY `IX_country_name_iso_country_code` (`iso_country_code`),
  KEY `IX_country_name_locale` (`locale`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `data_provider`
--

DROP TABLE IF EXISTS `data_provider`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `data_provider` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `description` text,
  `address` varchar(255) default NULL,
  `city` varchar(255) default NULL,
  `at_latitude` float default NULL,
  `at_longitude` float default NULL,
  `website_url` varchar(255) default NULL,
  `logo_url` varchar(255) default NULL,
  `email` varchar(255) default NULL,
  `telephone` varchar(255) default NULL,
  `uuid` char(50) default NULL,
  `concept_count` int(11) default '0',
  `higher_concept_count` int(11) default '0',
  `species_count` int(11) default '0',
  `occurrence_count` int(11) default '0',
  `occurrence_coordinate_count` int(11) default '0',
  `created` datetime default NULL,
  `modified` datetime default NULL,
  `deleted` datetime default NULL,
  `iso_country_code` char(2) default NULL,
  `stated_count_served` int(11) default NULL,
  `gbif_approver` varchar(255) default NULL,
  `lock_description` tinyint(1) NOT NULL default '0',
  `lock_iso_country_code` tinyint(1) NOT NULL default '0',
  `data_resource_count` int(11) default '0',
  PRIMARY KEY  (`id`),
  KEY `IX_data_provider_name` (`name`),
  KEY `IX_data_provider_country` (`iso_country_code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `data_provider_agent`
--

DROP TABLE IF EXISTS `data_provider_agent`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `data_provider_agent` (
  `id` int(11) NOT NULL auto_increment,
  `data_provider_id` int(11) NOT NULL,
  `agent_id` int(11) NOT NULL,
  `agent_type` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `data_provider_id` (`data_provider_id`),
  KEY `agent_id` (`agent_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `data_resource`
--

DROP TABLE IF EXISTS `data_resource`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `data_resource` (
  `id` int(11) NOT NULL auto_increment,
  `data_provider_id` int(11) NOT NULL,
  `name` varchar(255) default NULL,
  `display_name` varchar(255) default NULL,
  `description` text,
  `rights` text,
  `citation` text,
  `logo_url` varchar(255) default NULL,
  `shared_taxonomy` tinyint(1) default '0',
  `concept_count` int(11) default '0',
  `higher_concept_count` int(11) default '0',
  `species_count` int(11) default '0',
  `occurrence_count` int(11) default '0',
  `occurrence_coordinate_count` int(11) default '0',
  `basis_of_record` int(11) NOT NULL default '0',
  `created` datetime default NULL,
  `modified` datetime default NULL,
  `deleted` datetime default NULL,
  `citable_agent` varchar(255) default NULL,
  `root_taxon_rank` int(11) default NULL,
  `root_taxon_name` varchar(150) default NULL,
  `scope_continent_code` char(2) default NULL,
  `scope_country_code` char(2) default NULL,
  `provider_record_count` int(11) default NULL,
  `taxonomic_priority` int(11) NOT NULL default '100',
  `website_url` varchar(255) default NULL,
  `occurrence_clean_geospatial_count` int(11) default NULL,
  `lock_display_name` tinyint(1) NOT NULL default '0',
  `lock_citable_agent` tinyint(1) NOT NULL default '0',
  `lock_basis_of_record` tinyint(1) NOT NULL default '0',
  `override_citation` tinyint(1) NOT NULL default '0',
  `gbif_registry_uuid` varchar(50) default NULL,
  PRIMARY KEY  (`id`),
  KEY `data_provider_id` (`data_provider_id`),
  KEY `IX_data_resource_name` (`name`),
  KEY `IX_data_resource_display_name` (`display_name`),
  KEY `IX_data_resource_shared_taxonomy` (`shared_taxonomy`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `data_resource_agent`
--

DROP TABLE IF EXISTS `data_resource_agent`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `data_resource_agent` (
  `id` int(11) NOT NULL auto_increment,
  `data_resource_id` int(11) NOT NULL,
  `agent_id` int(11) NOT NULL,
  `agent_type` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `data_resource_id` (`data_resource_id`),
  KEY `agent_id` (`agent_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `entity_type`
--

DROP TABLE IF EXISTS `entity_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `entity_type` (
  `id` smallint(5) unsigned NOT NULL,
  `entity_type` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `gbif_log_message`
--

DROP TABLE IF EXISTS `gbif_log_message`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `gbif_log_message` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `portal_instance_id` tinyint(3) unsigned default NULL,
  `log_group_id` int(10) unsigned default NULL,
  `event_id` mediumint(8) unsigned default '0',
  `level` smallint(5) unsigned default NULL,
  `data_provider_id` smallint(5) unsigned default NULL,
  `data_resource_id` smallint(5) unsigned default NULL,
  `occurrence_id` int(10) unsigned default NULL,
  `taxon_concept_id` int(10) unsigned default NULL,
  `user_id` smallint(5) unsigned default NULL,
  `message` text,
  `restricted` tinyint(1) unsigned default NULL,
  `count` int(10) unsigned default NULL,
  `timestamp` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`),
  KEY `log_group_id` (`log_group_id`),
  KEY `portal_instance_id` (`portal_instance_id`),
  KEY `level` (`level`),
  KEY `event_id` (`event_id`),
  KEY `timestamp` (`timestamp`),
  KEY `user_id` (`user_id`),
  KEY `portal_instance_id_2` (`portal_instance_id`,`log_group_id`),
  KEY `data_resource_id` (`data_resource_id`,`event_id`,`timestamp`),
  KEY `data_provider_id` (`data_provider_id`,`event_id`,`timestamp`),
  KEY `occurrence_id` (`occurrence_id`,`event_id`,`timestamp`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `gbif_user`
--

DROP TABLE IF EXISTS `gbif_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `gbif_user` (
  `id` int(11) NOT NULL auto_increment,
  `portal_instance_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `verified` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `IX_gbif_user_verified` (`verified`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `geo_mapping`
--

DROP TABLE IF EXISTS `geo_mapping`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `geo_mapping` (
  `geo_region_id` int(11) NOT NULL,
  `occurrence_id` int(11) NOT NULL,
  PRIMARY KEY  (`geo_region_id`,`occurrence_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `geo_region`
--

DROP TABLE IF EXISTS `geo_region`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `geo_region` (
  `id` int(11) NOT NULL default '0',
  `name` varchar(255) default NULL,
  `region_type` smallint(6) default NULL,
  `iso_country_code` char(2) default NULL,
  `concept_count` int(11) default '0',
  `species_count` int(11) default '0',
  `occurrence_count` int(11) default NULL,
  `occurrence_coordinate_count` int(11) default '0',
  `min_latitude` float default NULL,
  `max_latitude` float default NULL,
  `min_longitude` float default NULL,
  `max_longitude` float default NULL,
  PRIMARY KEY  (`id`),
  KEY `ix_geo_region_iso_country_code` (`iso_country_code`),
  KEY `ix_geo_region_name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `geographical_coverage_tag`
--

DROP TABLE IF EXISTS `geographical_coverage_tag`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `geographical_coverage_tag` (
  `id` int(11) NOT NULL auto_increment,
  `tag_id` int(11) default NULL,
  `entity_id` int(11) default NULL,
  `min_longitude` float default NULL,
  `min_latitude` float default NULL,
  `max_longitude` float default NULL,
  `max_latitude` float default NULL,
  `is_system_generated` bit(1) default NULL,
  PRIMARY KEY  (`id`),
  KEY `tag_id` (`tag_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `identifier_record`
--

DROP TABLE IF EXISTS `identifier_record`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `identifier_record` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `data_resource_id` smallint(5) unsigned NOT NULL,
  `occurrence_id` int(10) unsigned default NULL,
  `identifier_type` smallint(5) unsigned NOT NULL default '0',
  `identifier` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `ix_identifier_record_occurrence` (`occurrence_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `image_record`
--

DROP TABLE IF EXISTS `image_record`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `image_record` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `data_resource_id` smallint(5) unsigned NOT NULL,
  `occurrence_id` int(10) unsigned default NULL,
  `taxon_concept_id` int(10) unsigned default NULL,
  `raw_image_type` varchar(128) default NULL,
  `image_type` tinyint(3) unsigned NOT NULL default '0',
  `url` varchar(255) NOT NULL,
  `description` text,
  `rights` text,
  `html_for_display` text,
  PRIMARY KEY  (`id`),
  KEY `ix_image_record_occurrence` (`occurrence_id`),
  KEY `ix_image_record_resource_taxon_concept` (`data_resource_id`,`taxon_concept_id`),
  KEY `ix_image_record_taxon_concept_id` (`taxon_concept_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `index_data`
--

DROP TABLE IF EXISTS `index_data`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `index_data` (
  `id` int(11) NOT NULL auto_increment,
  `resource_access_point_id` int(11) NOT NULL,
  `type` int(11) default NULL,
  `lower_value` varchar(255) default NULL,
  `upper_value` varchar(255) default NULL,
  `started` datetime default NULL,
  `finished` datetime default NULL,
  PRIMARY KEY  (`id`),
  KEY `IX_indexing_data_1` (`resource_access_point_id`),
  KEY `IX_indexing_data_2` (`started`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `institution_code`
--

DROP TABLE IF EXISTS `institution_code`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `institution_code` (
  `id` smallint(5) unsigned NOT NULL auto_increment,
  `code` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `ix_ic_institution_code` (`code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `ip_country`
--

DROP TABLE IF EXISTS `ip_country`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ip_country` (
  `id` int(11) NOT NULL auto_increment,
  `start` char(15) NOT NULL,
  `end` char(15) NOT NULL,
  `start_long` bigint(20) unsigned default NULL,
  `end_long` bigint(20) unsigned default NULL,
  `iso_country_code` char(12) default NULL,
  PRIMARY KEY  (`id`),
  KEY `IX_ip_country_range` (`start_long`,`end_long`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `link_record`
--

DROP TABLE IF EXISTS `link_record`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `link_record` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `data_resource_id` smallint(5) unsigned NOT NULL,
  `occurrence_id` int(10) unsigned default NULL,
  `taxon_concept_id` int(10) unsigned default NULL,
  `raw_link_type` varchar(128) default NULL,
  `link_type` smallint(5) unsigned NOT NULL default '0',
  `url` varchar(255) NOT NULL,
  `description` text,
  PRIMARY KEY  (`id`),
  KEY `ix_link_record_occurrence` (`occurrence_id`),
  KEY `ix_link_record_taxon_concept` (`taxon_concept_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `lookup_agent_type`
--

DROP TABLE IF EXISTS `lookup_agent_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lookup_agent_type` (
  `la_key` int(11) default NULL,
  `la_value` varchar(100) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `lookup_basis_of_record`
--

DROP TABLE IF EXISTS `lookup_basis_of_record`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lookup_basis_of_record` (
  `br_key` int(11) default NULL,
  `br_value` varchar(100) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `lookup_cell_density_type`
--

DROP TABLE IF EXISTS `lookup_cell_density_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lookup_cell_density_type` (
  `cd_key` int(11) default NULL,
  `cd_value` varchar(100) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `lookup_identifier_type`
--

DROP TABLE IF EXISTS `lookup_identifier_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lookup_identifier_type` (
  `it_key` int(11) default NULL,
  `it_value` varchar(100) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `lookup_image_type`
--

DROP TABLE IF EXISTS `lookup_image_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lookup_image_type` (
  `im_key` int(11) default NULL,
  `im_value` varchar(100) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `lookup_link_type`
--

DROP TABLE IF EXISTS `lookup_link_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lookup_link_type` (
  `li_key` int(11) default NULL,
  `li_value` varchar(100) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `lookup_log_event_type`
--

DROP TABLE IF EXISTS `lookup_log_event_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lookup_log_event_type` (
  `le_key` int(11) default NULL,
  `le_value` varchar(100) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `lookup_relationship_assertion_type`
--

DROP TABLE IF EXISTS `lookup_relationship_assertion_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lookup_relationship_assertion_type` (
  `ra_key` int(11) default NULL,
  `ra_value` varchar(100) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `namespace_mapping`
--

DROP TABLE IF EXISTS `namespace_mapping`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `namespace_mapping` (
  `resource_access_point_id` int(11) NOT NULL,
  `property_store_namespace_id` int(11) NOT NULL,
  `priority` int(11) NOT NULL,
  PRIMARY KEY  (`property_store_namespace_id`,`resource_access_point_id`),
  KEY `property_store_namespace_id` (`property_store_namespace_id`),
  KEY `resource_access_point_id` (`resource_access_point_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `network_membership`
--

DROP TABLE IF EXISTS `network_membership`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `network_membership` (
  `id` int(11) NOT NULL auto_increment,
  `data_resource_id` int(11) NOT NULL,
  `resource_network_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `data_resource_id` (`data_resource_id`),
  KEY `resource_network_id` (`resource_network_id`),
  KEY `IX_network_membership_id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `number_tag`
--

DROP TABLE IF EXISTS `number_tag`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `number_tag` (
  `id` int(11) NOT NULL auto_increment,
  `tag_id` int(11) default NULL,
  `entity_id` int(11) default NULL,
  `value` float default NULL,
  `is_system_generated` bit(1) default NULL,
  PRIMARY KEY  (`id`),
  KEY `tag_id` (`tag_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `occurrence_record`
--

DROP TABLE IF EXISTS `occurrence_record`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `occurrence_record` (
  `id` int(10) unsigned NOT NULL,
  `data_provider_id` smallint(5) unsigned NOT NULL,
  `data_resource_id` smallint(5) unsigned NOT NULL,
  `institution_code_id` smallint(5) unsigned NOT NULL,
  `collection_code_id` mediumint(8) unsigned NOT NULL,
  `catalogue_number_id` int(10) unsigned NOT NULL,
  `taxon_concept_id` int(10) unsigned NOT NULL,
  `taxon_name_id` mediumint(8) unsigned NOT NULL,
  `kingdom_concept_id` int(10) unsigned default NULL,
  `phylum_concept_id` int(10) unsigned default NULL,
  `class_concept_id` int(10) unsigned default NULL,
  `order_concept_id` int(10) unsigned default NULL,
  `family_concept_id` int(10) unsigned default NULL,
  `genus_concept_id` int(10) unsigned default NULL,
  `species_concept_id` int(10) unsigned default NULL,
  `nub_concept_id` int(10) unsigned default NULL,
  `iso_country_code` char(2) default NULL,
  `latitude` float default NULL,
  `longitude` float default NULL,
  `cell_id` smallint(5) unsigned default NULL,
  `centi_cell_id` tinyint(3) unsigned default NULL,
  `mod360_cell_id` smallint(5) unsigned default NULL,
  `year` smallint(5) unsigned default NULL,
  `month` tinyint(3) unsigned default NULL,
  `occurrence_date` date default NULL,
  `basis_of_record` tinyint(3) unsigned NOT NULL default '0',
  `taxonomic_issue` tinyint(3) unsigned NOT NULL default '0',
  `geospatial_issue` mediumint(8) unsigned default NULL,
  `other_issue` tinyint(3) unsigned NOT NULL default '0',
  `deleted` timestamp NULL default NULL,
  `altitude_metres` smallint(6) default NULL,
  `depth_centimetres` mediumint(8) unsigned default NULL,
  `modified` timestamp NULL default NULL,
  PRIMARY KEY  (`id`),
  KEY `ix_or_data_provider_id` (`data_provider_id`),
  KEY `ix_or_data_resource_id` (`data_resource_id`),
  KEY `ix_or_institution_code` (`institution_code_id`),
  KEY `ix_or_collection_code` (`collection_code_id`),
  KEY `ix_or_catalogue_number` (`catalogue_number_id`),
  KEY `ix_or_taxon_concept_id` (`taxon_concept_id`),
  KEY `ix_or_taxon_name_id` (`taxon_name_id`),
  KEY `ix_or_iso_country_code` (`iso_country_code`),
  KEY `ix_or_occurrence_date` (`occurrence_date`),
  KEY `ix_or_month` (`month`),
  KEY `ix_or_year` (`year`),
  KEY `ix_or_k_cell_mod_cell` (`kingdom_concept_id`,`cell_id`,`mod360_cell_id`),
  KEY `ix_or_p_cell_mod_cell` (`phylum_concept_id`,`cell_id`,`mod360_cell_id`),
  KEY `ix_or_c_cell_mod_cell` (`class_concept_id`,`cell_id`,`mod360_cell_id`),
  KEY `ix_or_o_cell_mod_cell` (`order_concept_id`,`cell_id`,`mod360_cell_id`),
  KEY `ix_or_f_cell_mod_cell` (`family_concept_id`,`cell_id`,`mod360_cell_id`),
  KEY `ix_or_g_cell_mod_cell` (`genus_concept_id`,`cell_id`,`mod360_cell_id`),
  KEY `ix_or_s_cell_mod_cell` (`species_concept_id`,`cell_id`,`mod360_cell_id`),
  KEY `ix_or_cell_mod_cell_nub` (`cell_id`,`mod360_cell_id`,`nub_concept_id`),
  KEY `ix_or_nub_country` (`nub_concept_id`,`iso_country_code`),
  KEY `ix_or_altitude_metres` (`altitude_metres`),
  KEY `ix_or_depth_centimetres` (`depth_centimetres`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `property_store_namespace`
--

DROP TABLE IF EXISTS `property_store_namespace`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `property_store_namespace` (
  `id` int(11) NOT NULL auto_increment,
  `namespace` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `quad_relation_tag`
--

DROP TABLE IF EXISTS `quad_relation_tag`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `quad_relation_tag` (
  `id` int(11) NOT NULL auto_increment,
  `tag_id` int(11) default NULL,
  `entity1_id` int(10) unsigned default NULL,
  `entity2_id` int(10) unsigned default NULL,
  `entity3_id` int(10) unsigned default NULL,
  `entity4_id` int(10) unsigned default NULL,
  `count` int(10) unsigned default NULL,
  `rollover_id` mediumint(8) unsigned default NULL,
  PRIMARY KEY  (`id`),
  KEY `entity1_id` (`entity1_id`,`entity2_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rank`
--

DROP TABLE IF EXISTS `rank`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rank` (
  `id` int(11) NOT NULL,
  `name` char(50) default NULL,
  PRIMARY KEY  (`id`),
  KEY `IX_rank_1` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `raw_occurrence_record`
--

DROP TABLE IF EXISTS `raw_occurrence_record`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `raw_occurrence_record` (
  `id` int(11) NOT NULL auto_increment,
  `data_provider_id` smallint(6) default NULL,
  `data_resource_id` smallint(6) default NULL,
  `resource_access_point_id` smallint(6) default NULL,
  `institution_code` varchar(255) default NULL,
  `collection_code` varchar(255) default NULL,
  `catalogue_number` varchar(255) default NULL,
  `scientific_name` varchar(255) default NULL,
  `author` varchar(255) default NULL,
  `rank` varchar(50) default NULL,
  `kingdom` varchar(150) default NULL,
  `phylum` varchar(150) default NULL,
  `class` varchar(250) default NULL,
  `order_rank` varchar(50) default NULL,
  `family` varchar(250) default NULL,
  `genus` varchar(150) default NULL,
  `species` varchar(150) default NULL,
  `subspecies` varchar(150) default NULL,
  `latitude` varchar(50) default NULL,
  `longitude` varchar(50) default NULL,
  `lat_long_precision` varchar(50) default NULL,
  `max_altitude` varchar(50) default NULL,
  `min_altitude` varchar(50) default NULL,
  `altitude_precision` varchar(50) default NULL,
  `min_depth` varchar(50) default NULL,
  `max_depth` varchar(50) default NULL,
  `depth_precision` varchar(50) default NULL,
  `continent_ocean` varchar(100) default NULL,
  `country` varchar(100) default NULL,
  `state_province` varchar(100) default NULL,
  `county` varchar(100) default NULL,
  `collector_name` varchar(255) default NULL,
  `locality` text,
  `year` varchar(50) default NULL,
  `month` varchar(50) default NULL,
  `day` varchar(50) default NULL,
  `basis_of_record` varchar(100) default NULL,
  `identifier_name` varchar(255) default NULL,
  `identification_date` datetime default NULL,
  `unit_qualifier` varchar(255) default NULL,
  `created` timestamp NULL default NULL,
  `modified` timestamp NULL default NULL,
  `deleted` timestamp NULL default NULL,
  PRIMARY KEY  (`id`),
  KEY `created` (`created`,`modified`),
  KEY `data_resource_id` (`data_resource_id`,`catalogue_number`),
  KEY `resource_access_point_id` (`resource_access_point_id`,`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `registration_login`
--

DROP TABLE IF EXISTS `registration_login`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `registration_login` (
  `id` int(11) NOT NULL auto_increment,
  `login_id` varchar(255) default NULL,
  `business_key` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `IX_registration_login_login_id` (`login_id`),
  KEY `IX_registration_login_business_key` (`business_key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `relationship_assertion`
--

DROP TABLE IF EXISTS `relationship_assertion`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `relationship_assertion` (
  `from_concept_id` int(11) NOT NULL,
  `to_concept_id` int(11) NOT NULL,
  `relationship_type` int(11) NOT NULL,
  PRIMARY KEY  (`from_concept_id`,`to_concept_id`,`relationship_type`),
  KEY `from_concept_id` (`from_concept_id`),
  KEY `IX_relationship_assertion_1` (`relationship_type`),
  KEY `IX_TO_CONCEPT_ID` (`to_concept_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `remote_concept`
--

DROP TABLE IF EXISTS `remote_concept`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `remote_concept` (
  `id` int(11) NOT NULL auto_increment,
  `taxon_concept_id` int(11) NOT NULL,
  `remote_id` varchar(255) default NULL,
  `id_type` smallint(5) unsigned NOT NULL,
  `modified` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`),
  KEY `ix_remote_concept_remote_id` (`remote_id`),
  KEY `ix_remote_concept_tc_id` (`taxon_concept_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `resource_access_point`
--

DROP TABLE IF EXISTS `resource_access_point`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `resource_access_point` (
  `id` int(11) NOT NULL auto_increment,
  `data_provider_id` int(11) default NULL,
  `data_resource_id` int(11) default NULL,
  `url` varchar(255) default NULL,
  `remote_id_at_url` varchar(255) default NULL,
  `uuid` char(50) default NULL,
  `created` datetime default NULL,
  `modified` datetime default NULL,
  `deleted` datetime default NULL,
  `last_harvest_start` datetime default NULL,
  `last_extract_start` datetime default NULL,
  `supports_date_last_modified` tinyint(1) NOT NULL default '0',
  `interval_metadata_days` int(11) default NULL,
  `interval_harvest_days` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `data_provider_id` (`data_provider_id`),
  KEY `ix_rap_dr` (`data_resource_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `resource_country`
--

DROP TABLE IF EXISTS `resource_country`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `resource_country` (
  `data_resource_id` int(11) NOT NULL,
  `iso_country_code` char(2) NOT NULL,
  `count` int(11) default NULL,
  `occurrence_coordinate_count` int(11) default '0',
  PRIMARY KEY  (`data_resource_id`,`iso_country_code`),
  KEY `IX_data_resource_ids` (`data_resource_id`),
  KEY `IX_iso_country_codes` (`iso_country_code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `resource_network`
--

DROP TABLE IF EXISTS `resource_network`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `resource_network` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `code` varchar(50) default NULL,
  `description` text,
  `address` varchar(255) default NULL,
  `website_url` varchar(255) default NULL,
  `logo_url` varchar(255) default NULL,
  `email` varchar(255) default NULL,
  `telephone` varchar(255) default NULL,
  `concept_count` int(11) default '0',
  `species_count` int(11) default '0',
  `occurrence_count` int(11) default '0',
  `occurrence_coordinate_count` int(11) default '0',
  `created` datetime default NULL,
  `modified` datetime default NULL,
  `deleted` datetime default NULL,
  `data_resource_count` int(11) default '0',
  PRIMARY KEY  (`id`),
  KEY `IX_resource_network_name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `resource_rank`
--

DROP TABLE IF EXISTS `resource_rank`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `resource_rank` (
  `id` int(11) NOT NULL,
  `resource_type` int(11) default NULL,
  `entity_id` int(11) default NULL,
  `entity_type` int(11) default NULL,
  `rank` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `IX_resource_rank_type` (`resource_type`),
  KEY `IX_resource_rank_entity_id` (`entity_id`),
  KEY `IX_resource_rank_entity_type` (`entity_type`),
  KEY `IX_resource_rank_rank` (`rank`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rollover`
--

DROP TABLE IF EXISTS `rollover`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rollover` (
  `id` int(11) NOT NULL auto_increment,
  `rollover_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `stats_country_contribution`
--

DROP TABLE IF EXISTS `stats_country_contribution`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `stats_country_contribution` (
  `id` int(11) NOT NULL auto_increment,
  `rollover_id` int(11) NOT NULL,
  `iso_country_code` char(2) default NULL,
  `provider_count` int(11) default '0',
  `dataset_count` int(11) default '0',
  `occurrence_count` int(11) default '0',
  `occurrence_georeferenced_count` int(11) default '0',
  `created` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `stats_participant_contribution`
--

DROP TABLE IF EXISTS `stats_participant_contribution`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `stats_participant_contribution` (
  `id` int(11) NOT NULL auto_increment,
  `rollover_id` int(11) NOT NULL,
  `gbif_approver` varchar(255) default NULL,
  `provider_count` int(11) default '0',
  `dataset_count` int(11) default '0',
  `occurrence_count` int(11) default '0',
  `occurrence_georeferenced_count` int(11) default '0',
  `created` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `string_tag`
--

DROP TABLE IF EXISTS `string_tag`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `string_tag` (
  `id` int(11) NOT NULL auto_increment,
  `tag_id` int(11) default NULL,
  `entity_id` int(11) default NULL,
  `value` text,
  `is_system_generated` bit(1) default NULL,
  PRIMARY KEY  (`id`),
  KEY `tag_id` (`tag_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tag` (
  `id` smallint(5) unsigned NOT NULL auto_increment,
  `name` char(200) default NULL,
  `entity_type` smallint(5) unsigned default NULL,
  `tag_table` char(30) default NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `taxon_concept`
--

DROP TABLE IF EXISTS `taxon_concept`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `taxon_concept` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `rank` smallint(5) unsigned NOT NULL,
  `taxon_name_id` mediumint(8) unsigned NOT NULL,
  `data_provider_id` smallint(5) unsigned NOT NULL,
  `data_resource_id` smallint(5) unsigned NOT NULL,
  `parent_concept_id` int(10) unsigned default NULL,
  `kingdom_concept_id` int(10) unsigned default NULL,
  `phylum_concept_id` int(10) unsigned default NULL,
  `class_concept_id` int(10) unsigned default NULL,
  `order_concept_id` int(10) unsigned default NULL,
  `family_concept_id` int(10) unsigned default NULL,
  `genus_concept_id` int(10) unsigned default NULL,
  `species_concept_id` int(10) unsigned default NULL,
  `is_accepted` tinyint(1) default '1',
  `is_nub_concept` tinyint(1) default '0',
  `partner_concept_id` int(10) unsigned default NULL,
  `priority` smallint(6) NOT NULL default '100',
  `is_secondary` tinyint(1) default '0',
  `created` timestamp NULL default NULL,
  `modified` timestamp NULL default NULL,
  `deleted` timestamp NULL default NULL,
  PRIMARY KEY  (`id`),
  KEY `ix_taxon_concept_dr_rank` (`data_resource_id`,`rank`),
  KEY `ix_taxon_concept_partner_dr` (`partner_concept_id`,`data_resource_id`),
  KEY `ix_taxon_concept_tn_dr` (`taxon_name_id`,`data_resource_id`),
  KEY `ix_taxon_concept_parent` (`parent_concept_id`),
  KEY `ix_taxon_concept_k` (`kingdom_concept_id`),
  KEY `ix_taxon_concept_p` (`phylum_concept_id`),
  KEY `ix_taxon_concept_c` (`class_concept_id`),
  KEY `ix_taxon_concept_o` (`order_concept_id`),
  KEY `ix_taxon_concept_f` (`family_concept_id`),
  KEY `ix_taxon_concept_g` (`genus_concept_id`),
  KEY `ix_taxon_concept_s` (`species_concept_id`),
  KEY `ix_taxon_concept_dp` (`data_provider_id`),
  KEY `ix_taxon_concept_dp_rank` (`data_provider_id`,`rank`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `taxon_country`
--

DROP TABLE IF EXISTS `taxon_country`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `taxon_country` (
  `taxon_concept_id` int(11) NOT NULL,
  `iso_country_code` char(2) NOT NULL,
  `count` int(11) default NULL,
  PRIMARY KEY  (`taxon_concept_id`,`iso_country_code`),
  KEY `IX_taxon_concept_ids` (`taxon_concept_id`),
  KEY `IX_iso_country_codes` (`iso_country_code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `taxon_name`
--

DROP TABLE IF EXISTS `taxon_name`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `taxon_name` (
  `id` mediumint(8) unsigned NOT NULL auto_increment,
  `canonical` varchar(255) NOT NULL,
  `supra_generic` varchar(255) default NULL,
  `generic` varchar(255) default NULL,
  `infrageneric` varchar(255) default NULL,
  `specific_epithet` varchar(255) default NULL,
  `infraspecific` varchar(255) default NULL,
  `infraspecific_marker` varchar(255) default NULL,
  `is_hybrid` tinyint(1) default NULL,
  `rank` smallint(5) unsigned NOT NULL,
  `author` varchar(255) default NULL,
  `searchable_canonical` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `ix_taxon_name_canonical_rank` (`canonical`,`rank`),
  KEY `ix_taxon_name_searchable_canonical_rank` (`searchable_canonical`,`rank`),
  KEY `IX_taxon_name_specific_epithet` (`specific_epithet`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `temporal_coverage_tag`
--

DROP TABLE IF EXISTS `temporal_coverage_tag`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `temporal_coverage_tag` (
  `id` int(11) NOT NULL auto_increment,
  `tag_id` int(11) default NULL,
  `entity_id` int(11) default NULL,
  `start_date` date NOT NULL,
  `end_date` date default NULL,
  `is_system_generated` bit(1) default NULL,
  PRIMARY KEY  (`id`),
  KEY `id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `typification_record`
--

DROP TABLE IF EXISTS `typification_record`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `typification_record` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `data_resource_id` smallint(5) unsigned NOT NULL,
  `occurrence_id` int(10) unsigned default NULL,
  `taxon_name_id` mediumint(8) unsigned default NULL,
  `scientific_name` varchar(255) default NULL,
  `publication` text default NULL,
  `type_status` varchar(255) NOT NULL,
  `notes` text,
  PRIMARY KEY  (`id`),
  KEY `ix_typification_record_occurrence` (`occurrence_id`),
  KEY `ix_typification_record_name` (`taxon_name_id`),
  KEY `ix_typification_record_resource` (`data_resource_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_quartz`
--

DROP TABLE IF EXISTS `v_quartz`;
/*!50001 DROP VIEW IF EXISTS `v_quartz`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_quartz` (
  `trigger_name` varchar(100),
  `trigger_group` varchar(100),
  `job_name` varchar(100),
  `job_group` varchar(100),
  `next_fire_time` varbinary(29)
) */;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `classification`
--

/*!50001 DROP TABLE `classification`*/;
/*!50001 DROP VIEW IF EXISTS `classification`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%.gbif.org` SQL SECURITY DEFINER VIEW `classification` AS select `tn`.`canonical` AS `name`,`kn`.`canonical` AS `k`,`pn`.`canonical` AS `p`,`cn`.`canonical` AS `c`,`orn`.`canonical` AS `o`,`fn`.`canonical` AS `f`,`gn`.`canonical` AS `g`,`sn`.`canonical` AS `s`,`tc`.`id` AS `concept_id`,`tc`.`rank` AS `rank`,`tc`.`parent_concept_id` AS `parent_id`,`tn`.`id` AS `name_id`,`tc`.`data_provider_id` AS `provider`,`tc`.`data_resource_id` AS `resource` from (((((((((((((((`taxon_concept` `tc` join `taxon_name` `tn` on((`tc`.`taxon_name_id` = `tn`.`id`))) left join `taxon_concept` `kc` on((`tc`.`kingdom_concept_id` = `kc`.`id`))) left join `taxon_concept` `pc` on((`tc`.`phylum_concept_id` = `pc`.`id`))) left join `taxon_concept` `cc` on((`tc`.`class_concept_id` = `cc`.`id`))) left join `taxon_concept` `oc` on((`tc`.`order_concept_id` = `oc`.`id`))) left join `taxon_concept` `fc` on((`tc`.`family_concept_id` = `fc`.`id`))) left join `taxon_concept` `gc` on((`tc`.`genus_concept_id` = `gc`.`id`))) left join `taxon_concept` `sc` on((`tc`.`species_concept_id` = `sc`.`id`))) left join `taxon_name` `kn` on((`kc`.`taxon_name_id` = `kn`.`id`))) left join `taxon_name` `pn` on((`pc`.`taxon_name_id` = `pn`.`id`))) left join `taxon_name` `cn` on((`cc`.`taxon_name_id` = `cn`.`id`))) left join `taxon_name` `orn` on((`oc`.`taxon_name_id` = `orn`.`id`))) left join `taxon_name` `fn` on((`fc`.`taxon_name_id` = `fn`.`id`))) left join `taxon_name` `gn` on((`gc`.`taxon_name_id` = `gn`.`id`))) left join `taxon_name` `sn` on((`sc`.`taxon_name_id` = `sn`.`id`))) */;

--
-- Final view structure for view `v_quartz`
--

/*!50001 DROP TABLE `v_quartz`*/;
/*!50001 DROP VIEW IF EXISTS `v_quartz`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%.gbif.org` SQL SECURITY DEFINER VIEW `v_quartz` AS select `QRTZ_TRIGGERS`.`trigger_name` AS `trigger_name`,`QRTZ_TRIGGERS`.`trigger_group` AS `trigger_group`,`QRTZ_TRIGGERS`.`job_name` AS `job_name`,`QRTZ_TRIGGERS`.`job_group` AS `job_group`,(_utf8'1970-01-01' + interval ((`QRTZ_TRIGGERS`.`next_fire_time` * 1000) + (((1000 * 1000) * 60) * 60)) microsecond) AS `next_fire_time` from `QRTZ_TRIGGERS` */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2009-05-26 12:43:46
