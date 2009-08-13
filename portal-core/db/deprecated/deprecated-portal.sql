/**
 * This is the real database script
 */
CREATE TABLE data_provider (
       id INT NOT NULL AUTO_INCREMENT
     , name VARCHAR(255)
     , description TEXT
     , address VARCHAR(255)
     , website_url VARCHAR(255)
     , logo_url VARCHAR(255)
     , email VARCHAR(255)
     , telephone VARCHAR(255)
     , uuid CHAR(50)
     , concept_count INT DEFAULT 0
     , higher_concept_count INT DEFAULT 0
     , species_count INT DEFAULT 0
     , occurrence_count INT DEFAULT 0
     , occurrence_coordinate_count INT DEFAULT 0
     , created DATETIME
     , modified DATETIME
     , deleted DATETIME
     , iso_country_code CHAR(2)
     , stated_count_served INT
     , gbif_approver VARCHAR(150)
     , lock_description TINYINT(1) NOT NULL DEFAULT 0
     , lock_iso_country_code TINYINT(1) NOT NULL DEFAULT 0
     , data_resource_count INT DEFAULT 0     
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';


CREATE INDEX IX_data_provider_name ON data_provider (name ASC);
CREATE INDEX IX_data_provider_country ON data_provider (iso_country_code ASC);

CREATE TABLE raw_occurrence_record (
       id INT NOT NULL AUTO_INCREMENT
     , data_provider_id INT NOT NULL
     , data_resource_id INT NOT NULL
     , resource_access_point_id INT NOT NULL
     , institution_code VARCHAR(255)
     , collection_code VARCHAR(255)
     , catalogue_number VARCHAR(255)
     , scientific_name VARCHAR(255)
     , author VARCHAR(255)
     , rank VARCHAR(50)
     , kingdom VARCHAR(150)
     , phylum VARCHAR(150)
     , class VARCHAR(250)
     , order_rank VARCHAR(50)
     , family VARCHAR(250)
     , genus VARCHAR(150)
     , species VARCHAR(150)
     , subspecies VARCHAR(150)
     , latitude VARCHAR(50)
     , longitude VARCHAR(50)
     , lat_long_precision VARCHAR(50)
     , max_altitude VARCHAR(50)
     , min_altitude VARCHAR(50)
     , altitude_precision VARCHAR(50)
     , min_depth VARCHAR(50)
     , max_depth VARCHAR(50)
     , depth_precision VARCHAR(50)
     , continent_ocean VARCHAR(100)
     , country VARCHAR(100)
     , state_province VARCHAR(100)
     , county VARCHAR(100)
     , collector_name VARCHAR(255)
     , locality TEXT
     , year VARCHAR(50)
     , month VARCHAR(50)
     , day VARCHAR(50)
     , basis_of_record VARCHAR(100)
     , identifier_name VARCHAR(255)
     , identification_date DATETIME
     , unit_qualifier VARCHAR(255)
     , created DATETIME
     , modified DATETIME
     , deleted DATETIME
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_raw_occurrence_record_4 ON raw_occurrence_record (data_resource_id ASC);
CREATE INDEX IX_raw_occurrence_record_5 ON raw_occurrence_record (resource_access_point_id ASC);
CREATE INDEX IX_raw_occurrence_record_6 ON raw_occurrence_record (created ASC);
CREATE INDEX IX_raw_occurrence_record_7 ON raw_occurrence_record (modified ASC);
CREATE INDEX IX_DR_INST_COLL_CAT_UNIT ON raw_occurrence_record (data_resource_id,institution_code(10),collection_code(10),catalogue_number(25),unit_qualifier(10));
CREATE INDEX IX_RAP_MOD_ID ON raw_occurrence_record (resource_access_point_id,modified,id);

CREATE TABLE occurrence_record (
       id INT NOT NULL AUTO_INCREMENT
     , raw_occurrence_record_id INT NOT NULL
     , data_provider_id INT
     , data_resource_id INT
     , provider_iso_country_code CHAR(2)
     , institution_code VARCHAR(255)
     , collection_code VARCHAR(255)
     , catalogue_number VARCHAR(255)
     , taxon_concept_id INT NOT NULL
     , taxon_name_id INT NOT NULL
     , nub_concept_id INT
     , nub_name_id INT
     , iso_country_code CHAR(2)
     , latitude FLOAT
     , longitude FLOAT
     , cell_id INT
     , centi_cell_id SMALLINT
     , lat_long_precision INT
     , altitude_meters FLOAT
     , altitude_precision INT
     , depth_meters FLOAT
     , depth_precision INT
     , year INT
     , month INT
     , day INT
     , occurrence_date DATETIME
     , basis_of_record INT NOT NULL
     , collector_name VARCHAR(255)
     , locality VARCHAR(255)
     , taxonomic_issue TINYINT NOT NULL DEFAULT 0
     , geospatial_issue TINYINT NOT NULL DEFAULT 0
     , other_issue TINYINT NOT NULL DEFAULT 0
     , created DATETIME
     , modified DATETIME
     , deleted DATETIME
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_occurrence_record_raw_occurrence_record_id ON occurrence_record (raw_occurrence_record_id ASC);
CREATE INDEX IX_occurrence_record_data_provider_id ON occurrence_record (data_provider_id ASC);
CREATE INDEX IX_occurrence_record_data_resource_id ON occurrence_record (data_resource_id ASC);
CREATE INDEX IX_occurrence_record_institution_code ON occurrence_record (institution_code ASC);
CREATE INDEX IX_occurrence_record_collection_code ON occurrence_record (collection_code ASC);
CREATE INDEX IX_occurrence_record_catalogue_number ON occurrence_record (catalogue_number ASC);
CREATE INDEX IX_occurrence_record_taxon_concept_id ON occurrence_record (taxon_concept_id ASC);
CREATE INDEX IX_occurrence_record_taxon_name_id ON occurrence_record (taxon_name_id ASC);
CREATE INDEX IX_occurrence_record_nub_concept_id ON occurrence_record (nub_concept_id ASC);
CREATE INDEX IX_occurrence_record_latitude ON occurrence_record (latitude ASC);
CREATE INDEX IX_occurrence_record_longitude ON occurrence_record (longitude ASC);
CREATE INDEX IX_occurrence_record_cell_id ON occurrence_record (cell_id ASC);
CREATE INDEX IX_occurrence_record_centi_cell_id ON occurrence_record (centi_cell_id ASC);
CREATE INDEX IX_occurrence_record_iso_country_code ON occurrence_record (iso_country_code ASC);
CREATE INDEX IX_occurrence_record_occurrence_date ON occurrence_record (occurrence_date ASC);
CREATE INDEX IX_occurrence_record_month ON occurrence_record (month ASC);
CREATE INDEX IX_occurrence_record_year ON occurrence_record (year ASC);
CREATE INDEX IX_occurrence_record_geospatial_issue ON occurrence_record (geospatial_issue ASC);
CREATE INDEX IX_occurrence_record_basis_of_record ON occurrence_record (basis_of_record ASC);
CREATE INDEX IX_occurrence_record_modified ON occurrence_record (modified ASC);
CREATE INDEX IX_LAT_LONG ON occurrence_record (latitude,longitude);
CREATE INDEX IX_NUB_CELL ON occurrence_record (cell_id, nub_concept_id);
CREATE INDEX IX_NUB_COUNTRY ON occurrence_record (iso_country_code, nub_concept_id);

CREATE TABLE publication (
       id INT NOT NULL AUTO_INCREMENT
     , title VARCHAR(255) NOT NULL
     , year VARCHAR(4)
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE property_store_namespace (
       id INT NOT NULL AUTO_INCREMENT
     , namespace VARCHAR(255)
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE agent (
       id INT NOT NULL AUTO_INCREMENT
     , name VARCHAR(255) NOT NULL
     , address VARCHAR(255)
     , email VARCHAR(255)
     , telephone VARCHAR(255)
     , created DATETIME
     , modified DATETIME
     , deleted DATETIME
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE country (
       id INT NOT NULL AUTO_INCREMENT
     , iso_country_code CHAR(2)
     , concept_count INT DEFAULT 0
     , species_count INT DEFAULT 0
     , occurrence_count INT DEFAULT 0
     , occurrence_coordinate_count INT DEFAULT 0
     , continent_code ENUM('AF', 'AS', 'EU', 'NA', 'OC', 'SA', 'AN')
     , region char(3)
     , min_latitude FLOAT
     , max_latitude FLOAT
     , min_longitude FLOAT
     , max_longitude FLOAT
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_country_iso_country_code ON country (iso_country_code ASC);
CREATE INDEX IX_country_continent_code ON country (continent_code ASC);
CREATE INDEX IX_country_region ON country (region ASC);

CREATE TABLE ip_country (
       id INT NOT NULL AUTO_INCREMENT
     , start CHAR(15) NOT NULL
     , end CHAR(15) NOT NULL
     , start_long BIGINT UNSIGNED
     , end_long BIGINT UNSIGNED
     , iso_country_code CHAR(12)
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_ip_country_range ON ip_country (start_long ASC, end_long ASC);

CREATE TABLE cell_density (
       type SMALLINT NOT NULL
     , concept_id INT NOT NULL
     , cell_id INT NOT NULL
     , count INT
     , PRIMARY KEY (type, concept_id, cell_id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE centi_cell_density (
       type SMALLINT NOT NULL
     , concept_id INT NOT NULL
     , cell_id INT NOT NULL
     , centi_cell_id INT NOT NULL
     , count INT
     , PRIMARY KEY (type, concept_id, cell_id, centi_cell_id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE cell_country (
       cell_id INT NOT NULL
     , iso_country_code CHAR(2) NOT NULL
     , PRIMARY KEY (cell_id, iso_country_code)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_cell_ids ON cell_country (cell_id ASC);
CREATE INDEX IX_iso_country_codes ON cell_country (iso_country_code ASC);

CREATE TABLE year_density (
       type SMALLINT NOT NULL
     , entity_id INT NOT NULL
     , year INT NOT NULL
     , count INT
     , PRIMARY KEY (type, entity_id, year)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE month_density (
       type SMALLINT NOT NULL
     , entity_id INT NOT NULL
     , month INT NOT NULL
     , count INT
     , PRIMARY KEY (type, entity_id, month)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';


CREATE TABLE resource_country (
       data_resource_id INT NOT NULL
     , iso_country_code CHAR(2) NOT NULL
     , count INT
     , PRIMARY KEY (data_resource_id, iso_country_code)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_resource_country_1 ON resource_country (data_resource_id ASC);
CREATE INDEX IX_resource_country_2 ON resource_country (iso_country_code ASC);

CREATE TABLE taxon_country (
       taxon_concept_id INT NOT NULL
     , iso_country_code CHAR(2) NOT NULL
     , count INT
     , PRIMARY KEY (taxon_concept_id, iso_country_code)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_taxon_country_1 ON taxon_country (taxon_concept_id ASC);
CREATE INDEX IX_taxon_country_2 ON taxon_country (iso_country_code ASC);

CREATE TABLE resource_network (
       id INT NOT NULL AUTO_INCREMENT
     , name VARCHAR(255)
     , code VARCHAR(50)
     , description TEXT
     , address VARCHAR(255)
     , website_url VARCHAR(255)
     , logo_url VARCHAR(255)
     , email VARCHAR(255)
     , telephone VARCHAR(255)
     , concept_count INT DEFAULT 0
     , species_count INT DEFAULT 0
     , occurrence_count INT DEFAULT 0
     , occurrence_coordinate_count INT DEFAULT 0
     , created DATETIME
     , modified DATETIME
     , deleted DATETIME
     , data_resource_count INT DEFAULT 0     
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_resource_network_name ON resource_network (name ASC);

CREATE TABLE index_data (
       id INT NOT NULL AUTO_INCREMENT
     , resource_access_point_id INT NOT NULL
     , type INT
     , lower_value VARCHAR(255)
     , upper_value VARCHAR(255)
     , started DATETIME
     , finished DATETIME
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_indexing_data_1 ON index_data (resource_access_point_id ASC);
CREATE INDEX IX_indexing_data_2 ON index_data (started ASC);

CREATE TABLE image_record (
       id INT NOT NULL AUTO_INCREMENT
     , data_provider_id INT NOT NULL
     , data_resource_id INT NOT NULL
     , raw_occurrence_record_id INT
     , occurrence_record_id INT
     , taxon_concept_id INT
     , raw_image_type VARCHAR(128)
     , image_type INT NOT NULL DEFAULT 0
     , url VARCHAR(255) NOT NULL
     , description TEXT
     , rights TEXT
     , html_for_display TEXT
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_image_record_1 ON image_record (occurrence_record_id ASC);
CREATE INDEX IX_image_record_2 ON image_record (data_resource_id ASC);
CREATE INDEX IX_image_record_3 ON image_record (image_type ASC);
CREATE INDEX IX_image_record_4 ON image_record (taxon_concept_id ASC);
CREATE INDEX IX_image_record_5 ON image_record (raw_occurrence_record_id ASC);

CREATE TABLE identifier_record (
       id INT NOT NULL AUTO_INCREMENT
     , data_provider_id INT NOT NULL
     , data_resource_id INT NOT NULL
     , raw_occurrence_record_id INT
     , occurrence_record_id INT
     , identifier_type INT NOT NULL DEFAULT 0
     , identifier VARCHAR(255) NOT NULL
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_identifier_record_1 ON identifier_record (occurrence_record_id ASC);
CREATE INDEX IX_identifier_record_2 ON identifier_record (data_resource_id ASC);
CREATE INDEX IX_identifier_record_3 ON identifier_record (identifier_type ASC);
CREATE INDEX IX_identifier_record_4 ON identifier_record (identifier ASC);
CREATE INDEX IX_identifier_record_5 ON identifier_record (raw_occurrence_record_id ASC);

CREATE TABLE link_record (
       id INT NOT NULL AUTO_INCREMENT
     , data_provider_id INT NOT NULL
     , data_resource_id INT NOT NULL
     , raw_occurrence_record_id INT
     , occurrence_record_id INT
     , taxon_concept_id INT
     , raw_link_type VARCHAR(128)
     , link_type INT NOT NULL DEFAULT 0
     , url VARCHAR(255) NOT NULL
     , description TEXT
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_link_record_1 ON link_record (occurrence_record_id ASC);
CREATE INDEX IX_link_record_2 ON link_record (data_resource_id ASC);
CREATE INDEX IX_link_record_3 ON link_record (taxon_concept_id ASC);
CREATE INDEX IX_link_record_4 ON link_record (link_type ASC);
CREATE INDEX IX_link_record_5 ON link_record (raw_occurrence_record_id ASC);

CREATE TABLE gbif_log_message (
       id INT NOT NULL AUTO_INCREMENT
     , portal_instance_id INT
     , log_group_id INT
     , event_id INT DEFAULT 0
     , level INT
     , data_provider_id INT
     , data_resource_id INT
     , occurrence_record_id INT
     , taxon_concept_id INT
     , user_id INT
     , message TEXT
     , restricted TINYINT(1)
     , count INT
     , timestamp TIMESTAMP NOT NULL
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_gbif_log_message_log_group ON gbif_log_message (log_group_id ASC);
CREATE INDEX IX_gbif_log_message_instance ON gbif_log_message (portal_instance_id ASC);
CREATE INDEX IX_gbif_log_message_provider ON gbif_log_message (data_provider_id ASC);
CREATE INDEX IX_gbif_log_message_resource ON gbif_log_message (data_resource_id ASC);
CREATE INDEX IX_gbif_log_message_level ON gbif_log_message (level ASC);
CREATE INDEX IX_gbif_log_message_event ON gbif_log_message (event_id ASC);
CREATE INDEX IX_gbif_log_message_date ON gbif_log_message (timestamp ASC);
CREATE INDEX IX_gbif_log_message_user ON gbif_log_message (user_id ASC);
CREATE INDEX IX_gbif_log_message_instance_log_group ON gbif_log_message (portal_instance_id ASC, log_group_id ASC);
CREATE INDEX IX_gbif_log_message_instance_event_id_data_resource_id ON gbif_log_message (event_id ASC, data_resource_id ASC);
CREATE INDEX IX_gbif_log_message_instance_event_id_data_provider_id ON gbif_log_message (event_id ASC, data_provider_id ASC);

CREATE TABLE typification_record (
       id INT NOT NULL AUTO_INCREMENT
     , data_provider_id INT NOT NULL
     , data_resource_id INT NOT NULL
     , raw_occurrence_record_id INT
     , occurrence_record_id INT
     , taxon_name_id INT NOT NULL DEFAULT 0
     , scientific_name VARCHAR(255)
     , publication VARCHAR(255)
     , type_status VARCHAR(255) NOT NULL
     , notes TEXT
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_typification_record_1 ON typification_record (occurrence_record_id ASC);
CREATE INDEX IX_typification_record_2 ON typification_record (data_resource_id ASC);
CREATE INDEX IX_typification_record_3 ON typification_record (taxon_name_id ASC);
CREATE INDEX IX_typification_record_4 ON typification_record (raw_occurrence_record_id ASC);

CREATE TABLE resource_rank (
       id INT NOT NULL
     , resource_type INT
     , entity_id INT
     , entity_type INT
     , rank INT
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_resource_rank_type ON resource_rank (resource_type ASC);
CREATE INDEX IX_resource_rank_entity_id ON resource_rank (entity_id ASC);
CREATE INDEX IX_resource_rank_entity_type ON resource_rank (entity_type ASC);
CREATE INDEX IX_resource_rank_rank ON resource_rank (rank ASC);

CREATE TABLE rank (
       id INT NOT NULL
     , name CHAR(50)
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_rank_1 ON rank (name ASC);

CREATE TABLE taxon_name (
       id INT NOT NULL AUTO_INCREMENT
     , canonical VARCHAR(255) NOT NULL
     , supra_generic VARCHAR(255)
     , generic VARCHAR(255)
     , infrageneric VARCHAR(255)
     , specific_epithet VARCHAR(255)
     , infraspecific VARCHAR(255)
     , infraspecific_marker VARCHAR(255)
     , is_hybrid BOOL
     , rank INT
     , author VARCHAR(255)
     , searchable_canonical VARCHAR(255)
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_taxon_name_canonical ON taxon_name (canonical ASC);
CREATE INDEX IX_taxon_name_specific_epithet ON taxon_name (specific_epithet ASC);
CREATE INDEX IX_taxon_name_author ON taxon_name (author ASC);
CREATE INDEX IX_taxon_name_rank ON taxon_name (rank ASC);
CREATE INDEX IX_taxon_name_searchable_canonical ON taxon_name (searchable_canonical ASC);
CREATE INDEX IX_can_auth_rank ON taxon_name (canonical(10), author(10), rank);


CREATE TABLE data_resource (
       id INT NOT NULL AUTO_INCREMENT
     , data_provider_id INT NOT NULL
     , name VARCHAR(255)
     , display_name VARCHAR(255)
     , description TEXT
     , rights TEXT
     , citation TEXT
     , logo_url VARCHAR(255)
     , shared_taxonomy BOOL DEFAULT false
     , concept_count INT DEFAULT 0
     , higher_concept_count INT DEFAULT 0
     , species_count INT DEFAULT 0
     , occurrence_count INT DEFAULT 0
     , occurrence_coordinate_count INT DEFAULT 0
     , basis_of_record INT NOT NULL DEFAULT 0
     , created DATETIME
     , modified DATETIME
     , deleted DATETIME
     , citable_agent VARCHAR(255)
     , root_taxon_rank INT
     , root_taxon_name VARCHAR(150)
     , scope_continent_code CHAR(2)
     , scope_country_code CHAR(2)
     , provider_record_count INT
     , taxonomic_priority INT NOT NULL DEFAULT 100
     , website_url VARCHAR(255)
     , occurrence_clean_geospatial_count INT DEFAULT 0
     , lock_display_name TINYINT(1) NOT NULL DEFAULT 0
     , lock_citable_agent TINYINT(1) NOT NULL DEFAULT 0
     , lock_basis_of_record TINYINT(1) NOT NULL DEFAULT 0
     , PRIMARY KEY (id)
     , INDEX (data_provider_id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_data_resource_name ON data_resource (name ASC);
CREATE INDEX IX_data_resource_display_name ON data_resource (display_name ASC);
CREATE INDEX IX_data_resource_shared_taxonomy ON data_resource (shared_taxonomy ASC);

CREATE TABLE taxon_concept (
       id INT NOT NULL AUTO_INCREMENT
     , rank INT NOT NULL
     , taxon_name_id INT NOT NULL
     , data_provider_id INT NOT NULL
     , data_resource_id INT NOT NULL
     , publication_id INT
     , parent_concept_id INT
     , kingdom_concept_id INT
     , phylum_concept_id INT
     , class_concept_id INT
     , order_concept_id INT
     , family_concept_id INT
     , genus_concept_id INT
     , species_concept_id INT
     , nub_concept_id INT
     , is_accepted BOOL DEFAULT true
     , is_nub_concept BOOL DEFAULT false
     , partner_concept_id INT
     , modified DATETIME
     , created DATETIME
     , deleted DATETIME
     , priority INT NOT NULL DEFAULT 100 
     , is_secondary BOOL DEFAULT false
     , PRIMARY KEY (id)
     , INDEX (taxon_name_id)
     , INDEX (data_provider_id)
     , INDEX (data_resource_id)
     , INDEX (parent_concept_id)
     , INDEX (kingdom_concept_id)
     , INDEX (phylum_concept_id)
     , INDEX (class_concept_id)
     , INDEX (order_concept_id)
     , INDEX (family_concept_id)
     , INDEX (genus_concept_id)
     , INDEX (species_concept_id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_taxon_concept_rank ON taxon_concept (rank ASC);
CREATE INDEX IX_taxon_concept_2 ON taxon_concept (is_nub_concept ASC);
CREATE INDEX IX_taxon_concept_3 ON taxon_concept (partner_concept_id ASC);
CREATE INDEX IX_taxon_concept_4 ON taxon_concept (priority ASC);

CREATE TABLE resource_access_point (
       id INT NOT NULL AUTO_INCREMENT
     , data_provider_id INT
     , data_resource_id INT
     , url VARCHAR(255)
     , remote_id_at_url VARCHAR(255)
     , uuid CHAR(50)
     , created DATETIME
     , modified DATETIME
     , deleted DATETIME
     , last_harvest_start datetime
     , last_extract_start datetime
     , supports_date_last_modified boolean not null default false
     , interval_metadata_days int
     , interval_harvest_days int
     , PRIMARY KEY (id)
     , INDEX (data_provider_id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE distribution (
       id INT NOT NULL AUTO_INCREMENT
     , taxon_concept_id INT
     , text VARCHAR(255) NOT NULL
     , iso_language_code CHAR(2) NOT NULL
     , language VARCHAR(255)
     , PRIMARY KEY (id)
     , INDEX (taxon_concept_id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_distribution_text ON distribution (text ASC);
CREATE INDEX IX_distribution_iso_language_code ON distribution (iso_language_code ASC);
CREATE INDEX IX_distribution_language ON distribution (language ASC);

CREATE TABLE namespace_mapping (
       resource_access_point_id INT NOT NULL
     , property_store_namespace_id INT NOT NULL
     , priority INT NOT NULL
     , PRIMARY KEY (property_store_namespace_id, resource_access_point_id)
     , INDEX (property_store_namespace_id)
     , INDEX (resource_access_point_id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE data_resource_agent (
       id INT NOT NULL AUTO_INCREMENT
     , data_resource_id INT NOT NULL
     , agent_id INT NOT NULL
     , agent_type INT NOT NULL
     , PRIMARY KEY (id)
     , INDEX (data_resource_id)
     , INDEX (agent_id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE data_provider_agent (
       id INT NOT NULL AUTO_INCREMENT
     , data_provider_id INT NOT NULL
     , agent_id INT NOT NULL
     , agent_type INT NOT NULL
     , PRIMARY KEY (id)
     , INDEX (data_provider_id)
     , INDEX (agent_id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE relationship_assertion (
       from_concept_id INT NOT NULL
     , to_concept_id INT NOT NULL
     , relationship_type INT NOT NULL
     , PRIMARY KEY (from_concept_id, to_concept_id, relationship_type)
     , INDEX (from_concept_id)
     , INDEX (to_concept_id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_relationship_assertion_1 ON relationship_assertion (relationship_type ASC);

CREATE TABLE remote_concept (
       id INT NOT NULL AUTO_INCREMENT
     , taxon_concept_id INT NOT NULL
     , id_type INT NOT NULL
     , remote_id VARCHAR(255)
     , modified TIMESTAMP
     , PRIMARY KEY (id)
     , INDEX (taxon_concept_id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_remote_concept_remote_id ON remote_concept (remote_id ASC);
CREATE INDEX IX_remote_concept_id_type ON remote_concept (id_type ASC);
CREATE INDEX IX_remote_concept_modified ON remote_concept (modified ASC);

CREATE TABLE country_name (
       id INT NOT NULL AUTO_INCREMENT
     , country_id INT
     , name VARCHAR(255)
     , searchable_name VARCHAR(255)
     , iso_country_code CHAR(2)
     , locale CHAR(2)
     , PRIMARY KEY (id)
     , INDEX (country_id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_country_name_name ON country_name (name ASC);
CREATE INDEX IX_country_name_searchable_name ON country_name (searchable_name ASC);
CREATE INDEX IX_country_name_iso_country_code ON country_name (iso_country_code ASC);
CREATE INDEX IX_country_name_locale ON country_name (locale ASC);

CREATE TABLE network_membership (
       id INT NOT NULL AUTO_INCREMENT
     , data_resource_id INT NOT NULL
     , resource_network_id INT NOT NULL
     , PRIMARY KEY (id)
     , INDEX (data_resource_id)
     , INDEX (resource_network_id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_network_membership_id ON network_membership (id ASC);

CREATE TABLE common_name (
       id INT NOT NULL AUTO_INCREMENT
     , taxon_concept_id INT
     , name VARCHAR(255) NOT NULL
     , iso_language_code CHAR(2) NOT NULL
     , language VARCHAR(255)
     , PRIMARY KEY (id)
     , INDEX (taxon_concept_id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_common_name_1 ON common_name (name ASC);
CREATE INDEX IX_common_name_2 ON common_name (iso_language_code ASC);
CREATE INDEX IX_common_name_3 ON common_name (language ASC);

CREATE TABLE gbif_user (
       id INT NOT NULL AUTO_INCREMENT
     , portal_instance_id INT NOT NULL
     , name VARCHAR(255) NOT NULL
     , email VARCHAR(255) NOT NULL
     , verified BOOL NOT NULL DEFAULT false
     , PRIMARY KEY (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_gbif_user_unique ON gbif_user (portal_instance_id ASC, name(50) ASC, email(50) ASC);
CREATE INDEX IX_gbif_user_verified ON gbif_user (verified ASC);

CREATE TABLE registration_login (
  id int(11) NOT NULL auto_increment,
  login_id varchar(255) default NULL,
  business_key varchar(255) default NULL,
  PRIMARY KEY  (id)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';
CREATE INDEX IX_registration_login_login_id ON registration_login (login_id ASC);
CREATE INDEX IX_registration_login_business_key ON registration_login (business_key ASC);

insert into property_store_namespace(namespace) values('http://www.biocase.org/schemas/protocol/1.3');
insert into property_store_namespace(namespace) values('http://www.tdwg.org/schemas/abcd/1.2');
insert into property_store_namespace(namespace) values('http://www.tdwg.org/schemas/abcd/2.05');
insert into property_store_namespace(namespace) values('http://www.tdwg.org/schemas/abcd/2.06');
insert into property_store_namespace(namespace) values('http://digir.net/schema/protocol/2003/1.0');
insert into property_store_namespace(namespace) values('http://digir.net/schema/conceptual/darwin/2003/1.0');
insert into property_store_namespace(namespace) values('http://manis.digir.net/schema/protocol/2003/1.0');
insert into property_store_namespace(namespace) values('http://manis.digir.net/schema/conceptual/darwin/2003/1.0');
insert into property_store_namespace(namespace) values('http://rs.tdwg.org/tapir/1.0');
insert into property_store_namespace(namespace) values('http://rs.tdwg.org/dwc/dwcore');
insert into property_store_namespace(namespace) values('http://rs.tdwg.org/dwc/geospatial');
insert into property_store_namespace(namespace) values('http://rs.tdwg.org/dwc/curatorial');
insert into property_store_namespace(namespace) values('http://digir.net/schema/conceptual/darwin/manis/1.21');

DROP TABLE IF EXISTS QRTZ_JOB_LISTENERS;
DROP TABLE IF EXISTS QRTZ_TRIGGER_LISTENERS;
DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;


CREATE TABLE QRTZ_JOB_DETAILS
  (
    JOB_NAME  VARCHAR(100) NOT NULL,
    JOB_GROUP VARCHAR(100) NOT NULL,
    DESCRIPTION VARCHAR(255) NULL,
    JOB_CLASS_NAME   VARCHAR(128) NOT NULL,
    IS_DURABLE VARCHAR(1) NOT NULL,
    IS_VOLATILE VARCHAR(1) NOT NULL,
    IS_STATEFUL VARCHAR(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (JOB_NAME,JOB_GROUP)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE QRTZ_JOB_LISTENERS
  (
    JOB_NAME  VARCHAR(100) NOT NULL,
    JOB_GROUP VARCHAR(100) NOT NULL,
    JOB_LISTENER VARCHAR(100) NOT NULL,
    PRIMARY KEY (JOB_NAME,JOB_GROUP,JOB_LISTENER),
    FOREIGN KEY (JOB_NAME,JOB_GROUP)
        REFERENCES QRTZ_JOB_DETAILS(JOB_NAME,JOB_GROUP)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE QRTZ_TRIGGERS
  (
    TRIGGER_NAME VARCHAR(100) NOT NULL,
    TRIGGER_GROUP VARCHAR(100) NOT NULL,
    JOB_NAME  VARCHAR(100) NOT NULL,
    JOB_GROUP VARCHAR(100) NOT NULL,
    IS_VOLATILE VARCHAR(1) NOT NULL,
    DESCRIPTION VARCHAR(255) NULL,
    NEXT_FIRE_TIME BIGINT(13) NULL,
    PREV_FIRE_TIME BIGINT(13) NULL,
    PRIORITY INTEGER NULL,
    TRIGGER_STATE VARCHAR(16) NOT NULL,
    TRIGGER_TYPE VARCHAR(8) NOT NULL,
    START_TIME BIGINT(13) NOT NULL,
    END_TIME BIGINT(13) NULL,
    CALENDAR_NAME VARCHAR(255) NULL,
    MISFIRE_INSTR SMALLINT(2) NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (JOB_NAME,JOB_GROUP)
        REFERENCES QRTZ_JOB_DETAILS(JOB_NAME,JOB_GROUP)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
  (
    TRIGGER_NAME VARCHAR(100) NOT NULL,
    TRIGGER_GROUP VARCHAR(100) NOT NULL,
    REPEAT_COUNT BIGINT(7) NOT NULL,
    REPEAT_INTERVAL BIGINT(12) NOT NULL,
    TIMES_TRIGGERED BIGINT(7) NOT NULL,
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE QRTZ_CRON_TRIGGERS
  (
    TRIGGER_NAME VARCHAR(100) NOT NULL,
    TRIGGER_GROUP VARCHAR(100) NOT NULL,
    CRON_EXPRESSION VARCHAR(255) NOT NULL,
    TIME_ZONE_ID VARCHAR(255),
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE QRTZ_BLOB_TRIGGERS
  (
    TRIGGER_NAME VARCHAR(100) NOT NULL,
    TRIGGER_GROUP VARCHAR(100) NOT NULL,
    BLOB_DATA BLOB NULL,
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE QRTZ_TRIGGER_LISTENERS
  (
    TRIGGER_NAME  VARCHAR(100) NOT NULL,
    TRIGGER_GROUP VARCHAR(100) NOT NULL,
    TRIGGER_LISTENER VARCHAR(100) NOT NULL,
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_LISTENER),
    FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';


CREATE TABLE QRTZ_CALENDARS
  (
    CALENDAR_NAME  VARCHAR(255) NOT NULL,
    CALENDAR BLOB NOT NULL,
    PRIMARY KEY (CALENDAR_NAME)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';



CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
  (
    TRIGGER_GROUP  VARCHAR(100) NOT NULL, 
    PRIMARY KEY (TRIGGER_GROUP)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE QRTZ_FIRED_TRIGGERS
  (
    ENTRY_ID VARCHAR(95) NOT NULL,
    TRIGGER_NAME VARCHAR(100) NOT NULL,
    TRIGGER_GROUP VARCHAR(100) NOT NULL,
    IS_VOLATILE VARCHAR(1) NOT NULL,
    INSTANCE_NAME VARCHAR(255) NOT NULL,
    FIRED_TIME BIGINT(13) NOT NULL,
    PRIORITY INTEGER NOT NULL,
    STATE VARCHAR(16) NOT NULL,
    JOB_NAME VARCHAR(100) NULL,
    JOB_GROUP VARCHAR(100) NULL,
    IS_STATEFUL VARCHAR(1) NULL,
    REQUESTS_RECOVERY VARCHAR(1) NULL,
    PRIMARY KEY (ENTRY_ID)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE QRTZ_SCHEDULER_STATE
  (
    INSTANCE_NAME VARCHAR(255) NOT NULL,
    LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
    CHECKIN_INTERVAL BIGINT(13) NOT NULL,
    PRIMARY KEY (INSTANCE_NAME)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';

CREATE TABLE QRTZ_LOCKS
  (
    LOCK_NAME  VARCHAR(40) NOT NULL, 
    PRIMARY KEY (LOCK_NAME)
) ENGINE=MYISAM DATA DIRECTORY='/var/lib/mysql/portal/extra_d' INDEX DIRECTORY='/var/lib/mysql/portal/extra_i';


INSERT INTO QRTZ_LOCKS values('TRIGGER_ACCESS');
INSERT INTO QRTZ_LOCKS values('JOB_ACCESS');
INSERT INTO QRTZ_LOCKS values('CALENDAR_ACCESS');
INSERT INTO QRTZ_LOCKS values('STATE_ACCESS');
INSERT INTO QRTZ_LOCKS values('MISFIRE_ACCESS');

/**
 * Just nice for testing at the moment
 */
CREATE VIEW view_rap AS 
select 
    rap.id as id,
    rap.url as url,
    rap.remote_id_at_url as remote_identifier,
    psn.namespace
from
    resource_access_point rap
        inner join namespace_mapping nm on nm.resource_access_point_id = rap.id
        inner join property_store_namespace psn on psn.id = nm.property_Store_namespace_id;



CREATE VIEW view_indexing AS
select 'Raw Occurrence Records', max(id) from raw_occurrence_record
UNION
select 'Occurrence Records (Extracted)', max(id) from occurrence_record
UNION
select 'Taxon Concept', max(id) from taxon_concept
UNION
select 'Taxon Names', max(id) from taxon_name
UNION
select 'Data Resources', max(id) from data_resource
UNION
select 'Data Providers', max(id) from data_provider
UNION
select 'Resource Access Points', max(id) from resource_access_point
UNION
select 'Queued Metadata Jobs', count(*) from QRTZ_TRIGGERS where job_group='metadata'
UNION
select 'Queued Harvesting Jobs', count(*) from QRTZ_TRIGGERS where job_group='harvesting'
UNION
select 'Queued Extract Jobs', count(*) from QRTZ_TRIGGERS where job_group='extract'
UNION
select 'Running Metadata Jobs', count(*) from QRTZ_FIRED_TRIGGERS where job_group='metadata'
UNION
select 'Running Harvesting Jobs', count(*) from QRTZ_FIRED_TRIGGERS where job_group='harvesting'
UNION
select 'Running Extract Jobs', count(*) from QRTZ_FIRED_TRIGGERS where job_group='extract'
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
 