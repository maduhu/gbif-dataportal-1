-- occurrence record
create table occurrence_record (
       id int unsigned not null 
     , data_provider_id smallint unsigned not null
     , data_resource_id smallint unsigned not null
     , institution_code_id smallint unsigned not null
     , collection_code_id mediumint unsigned not null
     , catalogue_number_id int unsigned not null
     , taxon_concept_id int unsigned not null
     , taxon_name_id mediumint unsigned not null
     , kingdom_concept_id int unsigned
     , phylum_concept_id int unsigned
     , class_concept_id int unsigned
     , order_concept_id int unsigned
     , family_concept_id int unsigned
     , genus_concept_id int unsigned
     , species_concept_id int unsigned
     , nub_concept_id int unsigned 
     , iso_country_code char(2)
     , latitude float
     , longitude float
     , cell_id smallint unsigned
     , centi_cell_id tinyint unsigned
     , mod360_cell_id smallint unsigned
	 , altitude_metres smallint signed
	 , depth_centimetres mediumint unsigned
     , year smallint unsigned 
     , month tinyint unsigned
     , occurrence_date date
     , basis_of_record tinyint unsigned not null default 0
     , taxonomic_issue tinyint unsigned not null default 0
     , geospatial_issue tinyint unsigned not null default 0
     , other_issue tinyint unsigned not null default 0
	 , modified timestamp null default null
     , deleted timestamp null default null
     , primary key (id)
) engine=myisam;
alter table occurrence_record
    add index ix_or_data_provider_id (data_provider_id),
    add index ix_or_data_resource_id (data_resource_id),
    add index ix_or_institution_code (institution_code_id),
    add index ix_or_collection_code (collection_code_id),
    add index ix_or_catalogue_number (catalogue_number_id),
    add index ix_or_taxon_concept_id (taxon_concept_id),
    add index ix_or_taxon_name_id (taxon_name_id),
    add index ix_or_iso_country_code (iso_country_code),
    add index ix_or_occurrence_date (occurrence_date),
    add index ix_or_month (month),
    add index ix_or_year (year),
    add index ix_or_altitude_metres (altitude_metres),
	add index ix_or_depth_centimetres (depth_centimetres),
    add index ix_or_k_cell_mod_cell (kingdom_concept_id, cell_id, mod360_cell_id),
    add index ix_or_p_cell_mod_cell (phylum_concept_id, cell_id, mod360_cell_id),
    add index ix_or_c_cell_mod_cell (class_concept_id, cell_id, mod360_cell_id),
    add index ix_or_o_cell_mod_cell (order_concept_id, cell_id, mod360_cell_id),
    add index ix_or_f_cell_mod_cell (family_concept_id, cell_id, mod360_cell_id),
    add index ix_or_g_cell_mod_cell (genus_concept_id, cell_id, mod360_cell_id),
    add index ix_or_s_cell_mod_cell (species_concept_id, cell_id, mod360_cell_id),
    add index ix_or_cell_mod_cell_nub (cell_id, mod360_cell_id, nub_concept_id),
    add index ix_or_nub_country (nub_concept_id, iso_country_code);
    
-- institution code
create table institution_code(
    id smallint unsigned not null auto_increment,
    code varchar(255) not null,
    primary key (id)
) engine=myisam;
create index ix_ic_institution_code on institution_code (code);

-- collection code
create table collection_code(
    id mediumint unsigned not null auto_increment,
    code varchar(255) not null,
    primary key (id)
) engine=myisam;
create index ix_cc_collection_code on collection_code (code);

-- catalogue number
create table catalogue_number(
    id int unsigned not null auto_increment,
    code varchar(255) not null,
    primary key (id)
) engine=myisam;
create index ix_cn_catalogue_number on catalogue_number (code);

-- ror
create table raw_occurrence_record (
       id int unsigned not null auto_increment
     , data_provider_id smallint unsigned not null
     , data_resource_id smallint unsigned not null
     , resource_access_point_id smallint unsigned not null
     , institution_code varchar(255) not null
     , collection_code varchar(255) not null
     , catalogue_number varchar(255) not null
     , scientific_name varchar(255)
     , author varchar(255)
     , rank varchar(50)
     , kingdom varchar(150)
     , phylum varchar(150)
     , class varchar(250)
     , order_rank varchar(50)
     , family varchar(250)
     , genus varchar(150)
     , species varchar(150)
     , subspecies varchar(150)
     , latitude varchar(50)
     , longitude varchar(50)
     , lat_long_precision varchar(50)
     , max_altitude varchar(50)
     , min_altitude varchar(50)
     , altitude_precision varchar(50)
     , min_depth varchar(50)
     , max_depth varchar(50)
     , depth_precision varchar(50)
     , continent_ocean varchar(100)
     , country varchar(100)
     , state_province varchar(100)
     , county varchar(100)
     , collector_name varchar(255)
     , locality text
     , year varchar(50)
     , month varchar(50)
     , day varchar(50)
     , basis_of_record varchar(100)
     , identifier_name varchar(255)
     , identification_date datetime
     , unit_qualifier varchar(255)
     , created timestamp null default null
     , modified timestamp null default null
     , deleted timestamp null default null
     , primary key (id)
) engine=myisam;
create index ix_created_modified on raw_occurrence_record (created, modified);
create index ix_dr_cat on raw_occurrence_record (data_resource_id, catalogue_number);
create index ix_rapid_id on raw_occurrence_record (resource_access_point_id, id);

-- image record
create table image_record (
       id int unsigned not null auto_increment
     , data_resource_id smallint unsigned not null
     , occurrence_id int unsigned
     , taxon_concept_id int unsigned
     , raw_image_type varchar(128)
     , image_type tinyint unsigned not null default 0
     , url varchar(255) not null
     , description text
     , rights text
     , html_for_display text
     , primary key (id)
) engine=myisam;
create index ix_image_record_occurrence on image_record (occurrence_id asc);
create index ix_image_record_resource_taxon_concept on image_record (data_resource_id, taxon_concept_id);
-- image record - required for species pages (note the above multiple column index not used as taxon_concept_id is not the lefter most column  
create index ix_image_record_taxon_concept_id on image_record (taxon_concept_id);

-- identifier record
create table identifier_record (
       id int unsigned not null auto_increment
     , data_resource_id  smallint unsigned not null
     , occurrence_id int unsigned not null
     , identifier_type smallint unsigned not null default 0
     , identifier varchar(255) not null
     , primary key (id)
) engine=myisam;
create index ix_identifier_record_occurrence on identifier_record (occurrence_id asc);

-- link record
create table link_record (
       id int unsigned not null auto_increment
     , data_resource_id smallint unsigned  not null
     , occurrence_id int unsigned not null
     , taxon_concept_id int unsigned
     , raw_link_type varchar(128)
     , link_type smallint unsigned not null default 0
     , url varchar(255) not null
     , description text
     , primary key (id)
) engine=myisam;
create index ix_link_record_occurrence on link_record (occurrence_id asc);
create index ix_link_record_taxon_concept on link_record (taxon_concept_id asc);

-- typification record
create table typification_record (
       id int unsigned not null auto_increment
     , data_resource_id smallint unsigned  not null
     , occurrence_id int unsigned
     , taxon_name_id mediumint unsigned 
     , scientific_name varchar(255)
     , publication varchar(255)
     , type_status varchar(255) not null
     , notes text
     , primary key (id)
) engine=myisam;
create index ix_typification_record_occurrence on typification_record (occurrence_id asc);
create index ix_typification_record_name on typification_record (taxon_name_id asc);
create index ix_typification_record_resource on typification_record (data_resource_id asc);

-- taxon name
create table taxon_name (
       id mediumint unsigned not null auto_increment
     , canonical varchar(255) not null
     , supra_generic varchar(255)
     , generic varchar(255)
     , infrageneric varchar(255)
     , specific_epithet varchar(255)
     , infraspecific varchar(255)
     , infraspecific_marker varchar(255)
     , is_hybrid bool
     , rank smallint unsigned not null
     , author varchar(255)
     , searchable_canonical varchar(255)
     , primary key (id)
) engine=myisam;
create index ix_taxon_name_canonical_rank on taxon_name (canonical, rank);
-- index needed for initial searches which does  a taxonname.specific_epithet like 'Puma concolor'
create index IX_taxon_name_specific_epithet ON taxon_name (specific_epithet ASC);
create index ix_taxon_name_searchable_canonical_rank on taxon_name (searchable_canonical, rank);

-- taxon concept
create table taxon_concept (
       id int unsigned not null auto_increment
     , rank smallint unsigned not null
     , taxon_name_id mediumint unsigned not null
     , data_provider_id smallint unsigned not null
     , data_resource_id  smallint unsigned not null
     , parent_concept_id int unsigned
     , kingdom_concept_id int unsigned
     , phylum_concept_id int unsigned
     , class_concept_id int unsigned
     , order_concept_id int unsigned
     , family_concept_id int unsigned
     , genus_concept_id int unsigned
     , species_concept_id int unsigned
     , is_accepted bool default true
     , is_nub_concept bool default false
     , partner_concept_id int unsigned
     , priority smallint not null default 100 
     , is_secondary bool default false
     , modified timestamp
     , created timestamp
     , deleted timestamp
     , primary key (id)
) engine=myisam;
create index ix_taxon_concept_dr_rank on taxon_concept(data_resource_id, rank);
-- added index to support select by data provider (used by Enrico Boldini)
create index ix_taxon_concept_dp on taxon_concept(data_provider_id);
create index ix_taxon_concept_partner_dr on taxon_concept(partner_concept_id,data_resource_id);
create index ix_taxon_concept_tn_dr on taxon_concept(taxon_name_id, data_resource_id);
create index ix_taxon_concept_parent on taxon_concept(parent_concept_id);
create index ix_taxon_concept_k on taxon_concept(kingdom_concept_id);
create index ix_taxon_concept_p on taxon_concept(phylum_concept_id);
create index ix_taxon_concept_c on taxon_concept(class_concept_id);
create index ix_taxon_concept_o on taxon_concept(order_concept_id);
create index ix_taxon_concept_f on taxon_concept(family_concept_id);
create index ix_taxon_concept_g on taxon_concept(genus_concept_id);
create index ix_taxon_concept_s on taxon_concept(species_concept_id);

-- remote concept
create table remote_concept (
       id int not null auto_increment
     , taxon_concept_id int not null
     , id_type smallint unsigned not null
     , remote_id varchar(255)
     , modified timestamp
     , primary key (id)
) engine=myisam;
create index ix_remote_concept_taxon_concept_type_remote_id on remote_concept (taxon_concept_id, id_type, remote_id);

-- cell density
create table cell_density (
       type smallint unsigned not null
     , entity_id int unsigned not null
     , cell_id mediumint unsigned not null
     , count int unsigned
     , primary key (type, entity_id, cell_id)
) engine=myisam;

-- centi cell density
create table centi_cell_density (
       type smallint unsigned not null
     , entity_id int unsigned not null
     , cell_id mediumint unsigned not null
     , centi_cell_id tinyint unsigned not null
     , count int unsigned
     , primary key (type, entity_id, cell_id, centi_cell_id)
) engine=myisam;

-- gbif log message
create table gbif_log_message (
       id int unsigned not null auto_increment
     , portal_instance_id tinyint unsigned
     , log_group_id int unsigned
     , event_id mediumint unsigned default 0
     , level smallint unsigned
     , data_provider_id smallint unsigned
     , data_resource_id smallint unsigned
     , occurrence_id int unsigned
     , taxon_concept_id int unsigned
     , user_id smallint unsigned
     , message text
     , restricted tinyint(1) unsigned
     , count int unsigned
     , timestamp timestamp not null
     , primary key (id)
) engine=myisam;
create index ix_gbif_log_message_log_group on gbif_log_message (log_group_id asc);
create index ix_gbif_log_message_instance on gbif_log_message (portal_instance_id asc);
create index ix_gbif_log_message_level on gbif_log_message (level asc);
create index ix_gbif_log_message_event on gbif_log_message (event_id asc);
create index ix_gbif_log_message_date on gbif_log_message (timestamp asc);
create index ix_gbif_log_message_user on gbif_log_message (user_id asc);
create index ix_gbif_log_message_instance_log_group on gbif_log_message (portal_instance_id asc, log_group_id asc);
create index ix_gbif_log_message_resource_event_timestamp on gbif_log_message (data_resource_id, event_id, timestamp);
create index ix_gbif_log_message_provider_event_timestamp on gbif_log_message (data_provider_id, event_id, timestamp);
create index ix_gbif_log_message_occurrence_event_timestamp on gbif_log_message (occurrence_id, event_id, timestamp);

create table data_provider (
       id int not null auto_increment
     , name varchar(255)
     , description text
     , address varchar(255)
     , website_url varchar(255)
     , logo_url varchar(255)
     , email varchar(255)
     , telephone varchar(255)
     , uuid char(50)
     , concept_count int default 0
     , higher_concept_count int default 0
     , species_count int default 0
     , occurrence_count int default 0
     , occurrence_coordinate_count int default 0
     , created datetime
     , modified datetime
     , deleted datetime
     , iso_country_code char(2)
     , stated_count_served int
     , gbif_approver varchar(150)
     , lock_description tinyint(1) not null default 0
     , lock_iso_country_code tinyint(1) not null default 0
     , data_resource_count int default 0     
     , primary key (id)
) engine=myisam;
create index ix_data_provider_name on data_provider (name asc);
create index ix_data_provider_country on data_provider (iso_country_code asc);

-- Insert the nub data provider
insert into data_provider(id,name,description) values(1,'Portal','The provider of the portal, not datasource. Needs to be id=1 and have a correspondent data_resource');

create table property_store_namespace (
       id int not null auto_increment
     , namespace varchar(255)
     , primary key (id)
) engine=myisam;

create table agent (
       id int not null auto_increment
     , name varchar(255) not null
     , address varchar(255)
     , email varchar(255)
     , telephone varchar(255)
     , created datetime
     , modified datetime
     , deleted datetime
     , primary key (id)
) engine=myisam;

create table country (
       id int not null auto_increment
     , iso_country_code char(2)
     , concept_count int default 0
     , species_count int default 0
     , occurrence_count int default 0
     , occurrence_coordinate_count int default 0
     , continent_code enum('af', 'as', 'eu', 'na', 'oc', 'sa', 'an')
     , region char(3)
     , min_latitude float
     , max_latitude float
     , min_longitude float
     , max_longitude float
     , primary key (id)
) engine=myisam;
create index ix_country_iso_country_code on country (iso_country_code asc);
create index ix_country_continent_code on country (continent_code asc);
create index ix_country_region on country (region asc);

create table ip_country (
       id int not null auto_increment
     , start char(15) not null
     , end char(15) not null
     , start_long bigint unsigned
     , end_long bigint unsigned
     , iso_country_code char(12)
     , primary key (id)
) engine=myisam;
create index ix_ip_country_range on ip_country (start_long asc, end_long asc);

-- geographic region
create table geo_region (
       id int not null auto_increment
     , name varchar(255)
	 , region_type smallint
     , iso_country_code char(2)
     , concept_count int default 0
     , species_count int default 0
     , occurrence_count int default 0
     , occurrence_coordinate_count int default 0
     , min_latitude float
     , max_latitude float
     , min_longitude float
     , max_longitude float
     , primary key (id)
) engine=myisam;
create index ix_geo_region_iso_country_code on geo_region (iso_country_code asc);
create index ix_geo_region_name on geo_region (name asc);

-- a join table for geo_region and occurrence
create table geo_mapping (
       geo_region_id int not null
     , occurrence_id int not null
     , primary key (geo_region_id, occurrence_id)
) engine=myisam;

create table cell_country (
       cell_id int not null
     , iso_country_code char(2) not null
     , primary key (cell_id, iso_country_code)
) engine=myisam;
create index ix_cell_ids on cell_country (cell_id asc);
create index ix_iso_country_codes on cell_country (iso_country_code asc);

create table year_density (
       type smallint not null
     , entity_id int not null
     , year int not null
     , count int
     , primary key (type, entity_id, year)
) engine=myisam;

create table month_density (
       type smallint not null
     , entity_id int not null
     , month int not null
     , count int
     , primary key (type, entity_id, month)
) engine=myisam;

create table resource_country (
       data_resource_id int not null
     , iso_country_code char(2) not null
     , count int
     , occurrence_coordinate_count int default 0
     , primary key (data_resource_id, iso_country_code)
) engine=myisam;
create index ix_resource_country_1 on resource_country (data_resource_id asc);
create index ix_resource_country_2 on resource_country (iso_country_code asc);

create table taxon_country (
       taxon_concept_id int not null
     , iso_country_code char(2) not null
     , count int
     , primary key (taxon_concept_id, iso_country_code)
) engine=myisam;
create index ix_taxon_country_1 on taxon_country (taxon_concept_id asc);
create index ix_taxon_country_2 on taxon_country (iso_country_code asc);

create table resource_network (
       id int not null auto_increment
     , name varchar(255)
     , code varchar(50)
     , description text
     , address varchar(255)
     , website_url varchar(255)
     , logo_url varchar(255)
     , email varchar(255)
     , telephone varchar(255)
     , concept_count int default 0
     , species_count int default 0
     , occurrence_count int default 0
     , occurrence_coordinate_count int default 0
     , created datetime
     , modified datetime
     , deleted datetime
     , data_resource_count int default 0     
     , primary key (id)
) engine=myisam;
create index ix_resource_network_name on resource_network (name asc);

create table index_data (
       id int not null auto_increment
     , resource_access_point_id int not null
     , type int
     , lower_value varchar(255)
     , upper_value varchar(255)
     , started datetime
     , finished datetime
     , primary key (id)
) engine=myisam;
create index ix_indexing_data_1 on index_data (resource_access_point_id asc);
create index ix_indexing_data_2 on index_data (started asc);

create table resource_rank (
       id int not null
     , resource_type int
     , entity_id int
     , entity_type int
     , rank int
     , primary key (id)
) engine=myisam;
create index ix_resource_rank_type on resource_rank (resource_type asc);
create index ix_resource_rank_entity_id on resource_rank (entity_id asc);
create index ix_resource_rank_entity_type on resource_rank (entity_type asc);
create index ix_resource_rank_rank on resource_rank (rank asc);

create table rank (
       id int not null
     , name char(50)
     , primary key (id)
) engine=myisam;
create index ix_rank_1 on rank (name asc);

create table data_resource (
       id int not null auto_increment
     , data_provider_id int not null
     , name varchar(255)
     , display_name varchar(255)
     , description text
     , rights text
     , citation text
     , logo_url varchar(255)
     , shared_taxonomy bool default false
     , concept_count int default 0
     , higher_concept_count int default 0
     , species_count int default 0
     , occurrence_count int default 0
     , occurrence_coordinate_count int default 0
     , basis_of_record int not null default 0
     , created datetime
     , modified datetime
     , deleted datetime
     , citable_agent varchar(255)
     , root_taxon_rank int
     , root_taxon_name varchar(150)
     , scope_continent_code char(2)
     , scope_country_code char(2)
     , provider_record_count int
     , taxonomic_priority int not null default 100
     , website_url varchar(255)
     , occurrence_clean_geospatial_count int default 0
     , lock_display_name tinyint(1) not null default 0
     , lock_citable_agent tinyint(1) not null default 0
     , lock_basis_of_record tinyint(1) not null default 0
     , primary key (id)
     , index (data_provider_id)
) engine=myisam;
create index ix_data_resource_name on data_resource (name asc);
create index ix_data_resource_display_name on data_resource (display_name asc);
create index ix_data_resource_shared_taxonomy on data_resource (shared_taxonomy asc);

insert into data_resource(id,data_provider_id,name,display_name,description) values(1,1,'Portal Index','Portal Index','The data resource of the portal. Needs to be id=1 and have a correspondent data_provider');

create table resource_access_point (
       id int not null auto_increment
     , data_provider_id int
     , data_resource_id int
     , url varchar(255)
     , remote_id_at_url varchar(255)
     , uuid char(50)
     , created datetime
     , modified datetime
     , deleted datetime
     , last_harvest_start datetime
     , last_extract_start datetime
     , supports_date_last_modified boolean not null default false
     , interval_metadata_days int
     , interval_harvest_days int
     , primary key (id)
     , index (data_provider_id)
) engine=myisam;

create table namespace_mapping (
       resource_access_point_id int not null
     , property_store_namespace_id int not null
     , priority int not null
     , primary key (property_store_namespace_id, resource_access_point_id)
     , index (property_store_namespace_id)
     , index (resource_access_point_id)
) engine=myisam;

create table data_resource_agent (
       id int not null auto_increment
     , data_resource_id int not null
     , agent_id int not null
     , agent_type int not null
     , primary key (id)
     , index (data_resource_id)
     , index (agent_id)
) engine=myisam;

create table data_provider_agent (
       id int not null auto_increment
     , data_provider_id int not null
     , agent_id int not null
     , agent_type int not null
     , primary key (id)
     , index (data_provider_id)
     , index (agent_id)
) engine=myisam;

create table relationship_assertion (
       from_concept_id int not null
     , to_concept_id int not null
     , relationship_type smallint not null
     , primary key (from_concept_id, to_concept_id, relationship_type)
     , index (from_concept_id)
     , index (to_concept_id)
) engine=myisam;
create index ix_relationship_assertion_1 on relationship_assertion (relationship_type asc);

create table country_name (
       id int not null auto_increment
     , country_id int
     , name varchar(255)
     , searchable_name varchar(255)
     , iso_country_code char(2)
     , locale char(2)
     , primary key (id)
     , index (country_id)
) engine=myisam;
create index ix_country_name_name on country_name (name asc);
create index ix_country_name_searchable_name on country_name (searchable_name asc);
create index ix_country_name_iso_country_code on country_name (iso_country_code asc);
create index ix_country_name_locale on country_name (locale asc);

create table network_membership (
       id int not null auto_increment
     , data_resource_id int not null
     , resource_network_id int not null
     , primary key (id)
     , index (data_resource_id)
     , index (resource_network_id)
) engine=myisam;
create index ix_network_membership_id on network_membership (id asc);

create table common_name (
       id int not null auto_increment
     , taxon_concept_id int
     , name varchar(255) not null
     , iso_language_code char(2) not null
     , language varchar(255)
     , primary key (id)
     , index (taxon_concept_id)
) engine=myisam;
create index ix_common_name_1 on common_name (name asc);
create index ix_common_name_2 on common_name (iso_language_code asc);
create index ix_common_name_3 on common_name (language asc);

create table gbif_user (
       id int not null auto_increment
     , portal_instance_id int not null
     , name varchar(255) not null
     , email varchar(255) not null
     , verified bool not null default false
     , primary key (id)
) engine=myisam;
create index ix_gbif_user_unique on gbif_user (portal_instance_id asc, name(50) asc, email(50) asc);
create index ix_gbif_user_verified on gbif_user (verified asc);

create table registration_login (
  id int(11) not null auto_increment,
  login_id varchar(255) default null,
  business_key varchar(255) default null,
  primary key  (id)
) engine=myisam;
create index ix_registration_login_login_id on registration_login (login_id asc);
create index ix_registration_login_business_key on registration_login (business_key asc);

-- Tag schema starts here
CREATE TABLE tag (
  id smallint(5) unsigned,
  name char(200) default NULL,
  entity_type smallint(5) unsigned default NULL,
  tag_table char(30) default NULL,
  description varchar(255) default NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM; 

CREATE TABLE entity_type (
  id smallint(5) unsigned NOT NULL,
  entity_type varchar(255) default NULL,
  PRIMARY KEY  (id)
) ENGINE=MyISAM;

CREATE TABLE string_tag (
  id int(11) NOT NULL auto_increment,
  tag_id int(11) default NULL,
  entity_id int(11) default NULL,
  value text,
  is_system_generated bit(1) default NULL,
  PRIMARY KEY  (id),
  index ix_string_tag_tag_id (tag_id, entity_id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE boolean_tag (
  id int(11) NOT NULL auto_increment,
  tag_id int(11) default NULL,
  entity_id int(11) default NULL,
  is_true tinyint(1) default '0',
  is_system_generated bit(1) default NULL,
  PRIMARY KEY  (id),
  index ix_boolean_tag_tag_id (tag_id, entity_id)
) ENGINE=MyISAM;

CREATE TABLE number_tag (
  id int(11) NOT NULL auto_increment,
  tag_id int(11) default NULL,
  entity_id int(11) default NULL,
  value float default NULL,
  is_system_generated bit(1) default NULL,
  PRIMARY KEY  (id),
  index ix_number_tag_tag_id (tag_id, entity_id)
) ENGINE=MyISAM;

CREATE TABLE temporal_coverage_tag (
  id int(11) NOT NULL auto_increment,
  tag_id int(11) default NULL,
  entity_id int(11) default NULL,
  start_date date default NULL,
  end_date date default NULL,
  is_system_generated bit(1) default NULL,
  PRIMARY KEY  (id),
  index ix_temporal_coverage_tag_tag_id (tag_id, entity_id)
) ENGINE=MyISAM;

CREATE TABLE geographical_coverage_tag (
  id int(11) NOT NULL auto_increment,
  tag_id int(11) default NULL,
  entity_id int(11) default NULL,
  min_longitude float default NULL,
  min_latitude float default NULL,
  max_longitude float default NULL,
  max_latitude float default NULL,
  is_system_generated bit(1) default NULL,
  PRIMARY KEY  (id),
  index ix_geographical_coverage_tag_tag_id (tag_id, entity_id)
) ENGINE=MyISAM;

CREATE TABLE bi_relation_tag (
  id int(11) NOT NULL auto_increment,
  tag_id int(11) default NULL,
  from_entity_id int(11) default NULL,
  to_entity_id int(11) default NULL,
  count int(10) unsigned default NULL,
  is_system_generated bit(1) default NULL,
  PRIMARY KEY  (id),
  index ix_bi_relation_tag_id (tag_id),
  index ix_bi_relation_tag_tag_id_to_entity_id (tag_id,to_entity_id),
  index ix_bi_relation_tag_tag_id_from_entity_id (tag_id,from_entity_id)
) ENGINE=MyISAM;

create table quad_relation_tag (
       id int not null auto_increment
     , tag_id int
     , entity1_id int unsigned
     , entity2_id int unsigned
     , entity3_id int unsigned
     , entity4_id int unsigned
     , count int unsigned
     , rollover_id mediumint unsigned
     , primary key (id)
     , index ix_quad_relation_tag (entity1_id,entity2_id)
) engine=myisam;

CREATE TABLE rollover (
  id int(11) NOT NULL auto_increment,
  rollover_date timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (id)
) ENGINE=MyISAM;

-- Tag schema ends here

insert into property_store_namespace(namespace) values('http://www.biocase.org/schemas/protocol/1.3');
insert into property_store_namespace(namespace) values('http://www.tdwg.org/schemas/abcd/1.2');
insert into property_store_namespace(namespace) values('http://www.tdwg.org/schemas/abcd/2.05');
insert into property_store_namespace(namespace) values('http://www.tdwg.org/schemas/abcd/2.06');
insert into property_store_namespace(namespace) values('http://digir.net/schema/protocol/2003/1.0');
insert into property_store_namespace(namespace) values('http://digir.net/schema/conceptual/darwin/2003/1.0');
insert into property_store_namespace(namespace) values('http://manis.digir.net/schema/protocol/2003/1.0');
insert into property_store_namespace(namespace) values('http://manis.digir.net/schema/conceptual/darwin/2003/1.0');
insert into property_store_namespace(namespace) values('http://rs.tdwg.org/tapir/1.0');
insert into property_store_namespace(namespace) values('http://rs.tdwg.org/dwc/dwcore/');
insert into property_store_namespace(namespace) values('http://rs.tdwg.org/dwc/geospatial/');
insert into property_store_namespace(namespace) values('http://rs.tdwg.org/dwc/curatorial/');
insert into property_store_namespace(namespace) values('http://digir.net/schema/conceptual/darwin/manis/1.21');
insert into property_store_namespace(namespace) values('http://rs.tdwg.org/tapirlite/1.0');

drop table if exists QRTZ_JOB_LISTENERS;
drop table if exists QRTZ_TRIGGER_LISTENERS;
drop table if exists QRTZ_FIRED_TRIGGERS;
drop table if exists QRTZ_PAUSED_TRIGGER_GRPS;
drop table if exists QRTZ_SCHEDULER_STATE;
drop table if exists QRTZ_LOCKS;
drop table if exists QRTZ_SIMPLE_TRIGGERS;
drop table if exists QRTZ_CRON_TRIGGERS;
drop table if exists QRTZ_BLOB_TRIGGERS;
drop table if exists QRTZ_TRIGGERS;
drop table if exists QRTZ_JOB_DETAILS;
drop table if exists QRTZ_CALENDARS;

create table QRTZ_JOB_DETAILS
  (
    job_name  varchar(100) not null,
    job_group varchar(100) not null,
    description varchar(255) null,
    job_class_name   varchar(128) not null,
    is_durable varchar(1) not null,
    is_volatile varchar(1) not null,
    is_stateful varchar(1) not null,
    requests_recovery varchar(1) not null,
    job_data blob null,
    primary key (job_name,job_group)
) engine=myisam;

create table QRTZ_JOB_LISTENERS
  (
    job_name  varchar(100) not null,
    job_group varchar(100) not null,
    job_listener varchar(100) not null,
    primary key (job_name,job_group,job_listener),
    foreign key (job_name,job_group)
        references QRTZ_JOB_DETAILS(job_name,job_group)
) engine=myisam;

create table QRTZ_TRIGGERS
  (
    trigger_name varchar(100) not null,
    trigger_group varchar(100) not null,
    job_name  varchar(100) not null,
    job_group varchar(100) not null,
    is_volatile varchar(1) not null,
    description varchar(255) null,
    next_fire_time bigint(13) null,
    prev_fire_time bigint(13) null,
    priority integer null,
    trigger_state varchar(16) not null,
    trigger_type varchar(8) not null,
    start_time bigint(13) not null,
    end_time bigint(13) null,
    calendar_name varchar(255) null,
    misfire_instr smallint(2) null,
    job_data blob null,
    primary key (trigger_name,trigger_group),
    foreign key (job_name,job_group)
        references QRTZ_JOB_DETAILS(job_name,job_group)
) engine=myisam;

create table QRTZ_SIMPLE_TRIGGERS
  (
    trigger_name varchar(100) not null,
    trigger_group varchar(100) not null,
    repeat_count bigint(7) not null,
    repeat_interval bigint(12) not null,
    times_triggered bigint(7) not null,
    primary key (trigger_name,trigger_group),
    foreign key (trigger_name,trigger_group)
        references QRTZ_TRIGGERS(trigger_name,trigger_group)
) engine=myisam;

create table QRTZ_CRON_TRIGGERS
  (
    trigger_name varchar(100) not null,
    trigger_group varchar(100) not null,
    cron_expression varchar(255) not null,
    time_zone_id varchar(255),
    primary key (trigger_name,trigger_group),
    foreign key (trigger_name,trigger_group)
        references QRTZ_TRIGGERS(trigger_name,trigger_group)
) engine=myisam;

create table QRTZ_BLOB_TRIGGERS
  (
    trigger_name varchar(100) not null,
    trigger_group varchar(100) not null,
    blob_data blob null,
    primary key (trigger_name,trigger_group),
    foreign key (trigger_name,trigger_group)
        references QRTZ_TRIGGERS(trigger_name,trigger_group)
) engine=myisam;

create table QRTZ_TRIGGER_LISTENERS
  (
    trigger_name  varchar(100) not null,
    trigger_group varchar(100) not null,
    trigger_listener varchar(100) not null,
    primary key (trigger_name,trigger_group,trigger_listener),
    foreign key (trigger_name,trigger_group)
        references QRTZ_TRIGGERS(trigger_name,trigger_group)
) engine=myisam;


create table QRTZ_CALENDARS
  (
    calendar_name  varchar(255) not null,
    calendar blob not null,
    primary key (calendar_name)
) engine=myisam;

create table QRTZ_PAUSED_TRIGGER_GRPS
  (
    trigger_group  varchar(100) not null, 
    primary key (trigger_group)
) engine=myisam;

create table QRTZ_FIRED_TRIGGERS
  (
    entry_id varchar(95) not null,
    trigger_name varchar(100) not null,
    trigger_group varchar(100) not null,
    is_volatile varchar(1) not null,
    instance_name varchar(255) not null,
    fired_time bigint(13) not null,
    priority integer not null,
    state varchar(16) not null,
    job_name varchar(100) null,
    job_group varchar(100) null,
    is_stateful varchar(1) null,
    requests_recovery varchar(1) null,
    primary key (entry_id)
) engine=myisam;

create table QRTZ_SCHEDULER_STATE
  (
    instance_name varchar(255) not null,
    last_checkin_time bigint(13) not null,
    checkin_interval bigint(13) not null,
    primary key (instance_name)
) engine=myisam;

create table QRTZ_LOCKS
  (
    lock_name  varchar(40) not null, 
    primary key (lock_name)
) engine=myisam;

insert into QRTZ_LOCKS values('trigger_access');
insert into QRTZ_LOCKS values('job_access');
insert into QRTZ_LOCKS values('calendar_access');
insert into QRTZ_LOCKS values('state_access');
insert into QRTZ_LOCKS values('misfire_access');

/**
 * just nice for testing at the moment
 */
create view view_rap as 
select 
    rap.id as id,
    rap.url as url,
    rap.remote_id_at_url as remote_identifier,
    psn.namespace
from
    resource_access_point rap
        inner join namespace_mapping nm on nm.resource_access_point_id = rap.id
        inner join property_store_namespace psn on psn.id = nm.property_store_namespace_id;


create view view_indexing as
select 'raw occurrence records', max(id) from raw_occurrence_record
union
select 'occurrence records (extracted)', max(id) from occurrence_record
union
select 'taxon concept', max(id) from taxon_concept
union
select 'taxon names', max(id) from taxon_name
union
select 'data resources', max(id) from data_resource
union
select 'data providers', max(id) from data_provider
union
select 'resource access points', max(id) from resource_access_point
union
select 'queued metadata jobs', count(*) from qrtz_triggers where job_group='metadata'
union
select 'queued harvesting jobs', count(*) from qrtz_triggers where job_group='harvesting'
union
select 'queued extract jobs', count(*) from qrtz_triggers where job_group='extract'
union
select 'running metadata jobs', count(*) from qrtz_fired_triggers where job_group='metadata'
union
select 'running harvesting jobs', count(*) from qrtz_fired_triggers where job_group='harvesting'
union
select 'running extract jobs', count(*) from qrtz_fired_triggers where job_group='extract'
;

create or replace view classification as
select
    tn.canonical as name,
    kn.canonical as k,
    pn.canonical as p,
    cn.canonical as c,
    orn.canonical as o,
    fn.canonical as f,
    gn.canonical as g,
    sn.canonical as s,
    tc.id as concept_id,
    tc.rank as rank,
    tc.parent_concept_id as parent_id,
    tn.id as name_id,
    tc.data_provider_id as provider,
    tc.data_resource_id as resource
from
    taxon_concept tc
        inner join taxon_name tn on tc.taxon_name_id = tn.id
        left join taxon_concept kc on tc.kingdom_concept_id = kc.id
        left join taxon_concept pc on tc.phylum_concept_id = pc.id
        left join taxon_concept cc on tc.class_concept_id = cc.id
        left join taxon_concept oc on tc.order_concept_id = oc.id
        left join taxon_concept fc on tc.family_concept_id = fc.id
        left join taxon_concept gc on tc.genus_concept_id = gc.id
        left join taxon_concept sc on tc.species_concept_id = sc.id
        left join taxon_name kn on kc.taxon_name_id = kn.id
        left join taxon_name pn on pc.taxon_name_id = pn.id
        left join taxon_name cn on cc.taxon_name_id = cn.id
        left join taxon_name orn on oc.taxon_name_id = orn.id
        left join taxon_name fn on fc.taxon_name_id = fn.id
        left join taxon_name gn on gc.taxon_name_id = gn.id
        left join taxon_name sn on sc.taxon_name_id = sn.id;
 
INSERT INTO rank (id, name) VALUES (0,'unranked');
INSERT INTO rank (id, name) VALUES (800,'superkingdom');
INSERT INTO rank (id, name) VALUES (1000,'kingdom');
INSERT INTO rank (id, name) VALUES (1200,'subkingdom');
INSERT INTO rank (id, name) VALUES (1800,'superphylum');
INSERT INTO rank (id, name) VALUES (2000,'phylum');
INSERT INTO rank (id, name) VALUES (2200,'subphylum');
INSERT INTO rank (id, name) VALUES (2800,'superclass');
INSERT INTO rank (id, name) VALUES (3000,'class');
INSERT INTO rank (id, name) VALUES (3200,'subclass');
INSERT INTO rank (id, name) VALUES (3350,'infraclass');
INSERT INTO rank (id, name) VALUES (3800,'superorder');
INSERT INTO rank (id, name) VALUES (4000,'order');
INSERT INTO rank (id, name) VALUES (4200,'suborder');
INSERT INTO rank (id, name) VALUES (4350,'infraorder');
INSERT INTO rank (id, name) VALUES (4400,'parvorder');
INSERT INTO rank (id, name) VALUES (4500,'superfamily');
INSERT INTO rank (id, name) VALUES (5000,'family');
INSERT INTO rank (id, name) VALUES (5500,'subfamily');
INSERT INTO rank (id, name) VALUES (5600,'tribe');
INSERT INTO rank (id, name) VALUES (5700,'subtribe');
INSERT INTO rank (id, name) VALUES (6000,'genus');
INSERT INTO rank (id, name) VALUES (6001,'nothogenus');
INSERT INTO rank (id, name) VALUES (6500,'subgenus');
INSERT INTO rank (id, name) VALUES (6600,'section');
INSERT INTO rank (id, name) VALUES (6700,'subsection');
INSERT INTO rank (id, name) VALUES (6800,'series');
INSERT INTO rank (id, name) VALUES (6900,'subseries');
INSERT INTO rank (id, name) VALUES (6950,'speciesgroup');
INSERT INTO rank (id, name) VALUES (6975,'speciessubgroup');
INSERT INTO rank (id, name) VALUES (7000,'species');
INSERT INTO rank (id, name) VALUES (7001,'nothospecies');
INSERT INTO rank (id, name) VALUES (8000,'subspecies');
INSERT INTO rank (id, name) VALUES (8001,'nothosubspecies');
INSERT INTO rank (id, name) VALUES (8010,'variety');
INSERT INTO rank (id, name) VALUES (8011,'nothovariety');
INSERT INTO rank (id, name) VALUES (8020,'form');
INSERT INTO rank (id, name) VALUES (8021,'nothoform');
INSERT INTO rank (id, name) VALUES (8030,'biovar');
INSERT INTO rank (id, name) VALUES (8040,'serovar');
INSERT INTO rank (id, name) VALUES (8050,'cultivar');
INSERT INTO rank (id, name) VALUES (8080,'pathovar');
INSERT INTO rank (id, name) VALUES (8090,'infraspecific');
INSERT INTO rank (id, name) VALUES (8100,'aberration');
INSERT INTO rank (id, name) VALUES (8110,'mutation');
INSERT INTO rank (id, name) VALUES (8120,'race');
INSERT INTO rank (id, name) VALUES (8130,'confersubspecies');
INSERT INTO rank (id, name) VALUES (8140,'formaspecialis');
INSERT INTO rank (id, name) VALUES (8150,'hybrid');

-- entity types - these values tally with the values in org.gbif.portal.dto.util.EntityType
insert into entity_type (id, entity_type) values (1, 'taxon');
insert into entity_type (id, entity_type) values (2, 'country');
insert into entity_type (id, entity_type) values (3, 'provider');
insert into entity_type (id, entity_type) values (4, 'resource');
insert into entity_type (id, entity_type) values (5, 'network');
insert into entity_type (id, entity_type) values (6, 'homeCountry');
insert into entity_type (id, entity_type) values (7, 'occurrence');

-- country tags
insert into tag (id, name, entity_type, tag_table, description) values (6000, 'host_country_country_kingdom_basis', 6, 'quad_relation_tag','A breakdown of hosts by country, kingom and basis of record');

-- data resource tags
insert into tag (id, name, entity_type, tag_table, description) values (4000, 'data_resource_keywords', 4, 'string_tag','Keywords associated with this dataset');
insert into tag (id, name, entity_type, tag_table, description) values (4100, 'data_resource_occurrences_country', 4, 'bi_relation_tag', 'This data resource has this many occurrences in this country');
insert into tag (id, name, entity_type, tag_table, description) values (4101, 'data_resource_occurrences_bounding_box', 4, 'geographic_coverage_tag', 'This data resource has occurrence data within this bounding box');
insert into tag (id, name, entity_type, tag_table, description) values (4102, 'data_resource_occurrences_wkt_polygon', 4, 'string_tag', 'A WKT Polygon describing the occurrences for this data resource');
insert into tag (id, name, entity_type, tag_table, description) values (4120, 'data_resource_occurrences_date_range', 4, 'temporal_coverage_tag', 'The temporal coverage of a data resource ');
insert into tag (id, name, entity_type, tag_table, description) values (4121, 'data_resource_occurrences_month', 4, 'number_tag', 'The months this data resource has data for');
insert into tag (id, name, entity_type, tag_table, description) values (4140, 'data_resource_occurrences_species', 4, 'bi_relation_tag', 'This data resource has this many occurrences for this species');
insert into tag (id, name, entity_type, tag_table, description) values (4141, 'data_resource_occurrences_genus', 4, 'bi_relation_tag', 'This data resource has this many occurrences for this genus');
insert into tag (id, name, entity_type, tag_table, description) values (4142, 'data_resource_occurrences_family', 4, 'bi_relation_tag','This data resource has this many occurrences for this family');
insert into tag (id, name, entity_type, tag_table, description) values (4150, 'data_resource_taxonomic_scope', 4, 'bi_relation_tag','The taxonomic scope for this resource. Points to a nub concept');
insert into tag (id, name, entity_type, tag_table, description) values (4151, 'data_resource_associated_kingdom', 4, 'bi_relation_tag','This kingdom this data resource contains data for');
insert into tag (id, name, entity_type, tag_table, description) values (4152, 'data_resource_common_names', 4, 'bi_relation_tag','Species common names associated with this resource');
insert into tag (id, name, entity_type, tag_table, description) values (4160, 'data_resource_contains_type_specimens', 4, 'number_tag','Indicates whether a data resource contains type records');
insert into tag (id, name, entity_type, tag_table, description) values (4161, 'data_resource_collector', 4, 'string_tag','Names of collectors who have collected for this dataset');