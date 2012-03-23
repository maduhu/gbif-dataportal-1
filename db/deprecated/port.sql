-- after the original data is loaded in

-- clean up the temp tables
drop table col_vernacular_name;
drop table distribution;
drop table duplicated_taxon_names;
drop table extract;
drop table nbn_tab;
drop table publication;
drop table range_stats;
drop table temp_MOBOT;
drop table temp_fishbase;
drop table temp_nbn;
drop table temp_to_remove;
drop table temp_to_remove2;
drop view view_indexing;
drop view view_nubbed;
drop view classification;

-- ROR can be modified
alter table raw_occurrence_record
    modify column data_provider_id smallint,
    modify column data_resource_id smallint,
    modify column resource_access_point_id smallint,
    modify column created timestamp null default null,
    modify column modified timestamp null default null,
    modify column deleted timestamp null default null,    
    drop index IX_raw_occurrence_record_4,
    drop index IX_raw_occurrence_record_5,
    drop index IX_raw_occurrence_record_6,
    drop index IX_raw_occurrence_record_7,
    drop index ix_scientific_name,
    drop index ix_data_provider_id,
    drop index IX_RAP_MOD_ID,
    drop index IX_DR_INST_COLL_CAT,
    add index (created, modified),
    add index (data_resource_id, catalogue_number),
    add index (resource_access_point_id, id);

-- cell densities will be recreated, so let's just recreate tables
drop table cell_density;
drop table centi_cell_density;
create table cell_density (
       type smallint unsigned not null
     , entity_id int unsigned not null
     , cell_id smallint unsigned not null
     , count int unsigned
     , primary key (type, entity_id, cell_id)
) engine=myisam;
create table centi_cell_density (
       type smallint unsigned not null
     , entity_id int unsigned not null
     , cell_id smallint unsigned not null
     , centi_cell_id tinyint unsigned not null
     , count int unsigned
     , primary key (type, entity_id, cell_id, centi_cell_id)
) engine=myisam;

-- taxon name
rename table taxon_name to old_taxon_name;
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
create index ix_taxon_name_searchable_canonical_rank on taxon_name (searchable_canonical, rank);
insert into taxon_name select * from old_taxon_name;


-- taxon concept
rename table taxon_concept to old_taxon_concept;
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
     , created timestamp null default null
     , modified timestamp null default null
     , deleted timestamp null default null
     , primary key (id)
) engine=myisam;
create index ix_taxon_concept_dr_rank on taxon_concept(data_resource_id, rank);
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
insert into taxon_concept
select id,rank,taxon_name_id,data_provider_id,data_resource_id,parent_concept_id,kingdom_concept_id,phylum_concept_id,class_concept_id,order_concept_id,family_concept_id,genus_concept_id,species_concept_id,is_accepted,is_nub_concept,partner_concept_id,priority,is_secondary,created,modified,deleted
from old_taxon_concept;

-- remote_concept
rename table remote_concept to old_remote_concept;
create table remote_concept (
       id int not null auto_increment
     , taxon_concept_id int not null
     , remote_id varchar(255)
     , id_type smallint unsigned not null
     , modified timestamp
     , primary key (id)
) engine=myisam;
create index ix_remote_concept_taxon_concept_type_remote_id on remote_concept (taxon_concept_id, id_type, remote_id);
insert into remote_concept select * from old_remote_concept;

-- gbif log message
rename table gbif_log_message to old_gbif_log_message;
create table gbif_log_message (
       id int unsigned not null auto_increment
     , portal_instance_id tinyint unsigned
     , log_group_id int unsigned
     , event_id mediumint unsigned default 0
     , level smallint unsigned
     , data_provider_id smallint unsigned
     , data_resource_id smallint unsigned
     , occurrence_record_id int unsigned
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
create index ix_gbif_log_message_occurrence_event_timestamp on gbif_log_message (occurrence_record_id, event_id, timestamp);
insert into gbif_log_message select * from old_gbif_log_message;

-- institution code
create table institution_code(
    id smallint unsigned not null auto_increment,
    code varchar(255) not null,
    primary key (id)
) engine=myisam;
insert ignore into institution_code(code) 
select distinct institution_code from occurrence_record;
create index ix_ic_institution_code on institution_code (code);

-- collection code
create table collection_code(
    id mediumint unsigned not null auto_increment,
    code varchar(255) not null,
    primary key (id)
) engine=myisam;
insert ignore into collection_code(code) 
select distinct collection_code from occurrence_record;
create index ix_cc_collection_code on collection_code (code);

-- catalogue number
create table catalogue_number(
    id int unsigned not null auto_increment,
    code varchar(255) not null,
    primary key (id)
) engine=myisam;
insert ignore into catalogue_number(code) 
select distinct catalogue_number from occurrence_record;
create index ix_cn_catalogue_number on catalogue_number (code);

-- This get modified schemas so let's move them out the way then create and populate later
rename table occurrence_record to old_occurrence_record;
update old_occurrence_record set month=null where month<1 or month>12;
update old_occurrence_record set year=null where year<1700 or year>2008;
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
     , year smallint unsigned 
     , month tinyint unsigned
     , occurrence_date date
     , basis_of_record tinyint unsigned not null default 0
     , taxonomic_issue tinyint unsigned not null default 0
     , geospatial_issue tinyint unsigned not null default 0
     , other_issue tinyint unsigned not null default 0
     , deleted timestamp null default null
     , primary key (id)
) engine=myisam;
insert ignore into occurrence_record(id,data_provider_id,data_resource_id,institution_code_id,collection_code_id,catalogue_number_id,taxon_concept_id,taxon_name_id,
    nub_concept_id,iso_country_code,latitude,longitude,cell_id,mod360_cell_id,centi_cell_id,year,month,occurrence_date,basis_of_record,taxonomic_issue,geospatial_issue,other_issue,deleted)
select straight_join
    o.raw_occurrence_record_id,
    o.data_provider_id,
    o.data_resource_id,
    i.id,
    c.id,
    n.id,
    o.taxon_concept_id,
    o.taxon_name_id,
    o.nub_concept_id,
    o.iso_country_code,
    o.latitude,
    o.longitude,
    o.cell_id,
    mod(o.cell_id, 360),
    o.centi_cell_id,
    o.year,
    o.month,
    o.occurrence_date,
    o.basis_of_record,
    o.taxonomic_issue,
    o.geospatial_issue,
    o.other_issue,
    o.deleted
from old_occurrence_record o
    inner join catalogue_number n on n.code = o.catalogue_number
    inner join institution_code i on i.code = o.institution_code
    inner join collection_code c on c.code = o.collection_code;
show warnings;    
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
    add index ix_or_k_cell_mod_cell (kingdom_concept_id, cell_id, mod360_cell_id),
    add index ix_or_p_cell_mod_cell (phylum_concept_id, cell_id, mod360_cell_id),
    add index ix_or_c_cell_mod_cell (class_concept_id, cell_id, mod360_cell_id),
    add index ix_or_o_cell_mod_cell (order_concept_id, cell_id, mod360_cell_id),
    add index ix_or_f_cell_mod_cell (family_concept_id, cell_id, mod360_cell_id),
    add index ix_or_g_cell_mod_cell (genus_concept_id, cell_id, mod360_cell_id),
    add index ix_or_s_cell_mod_cell (species_concept_id, cell_id, mod360_cell_id),
    add index ix_or_cell_mod_cell_nub (cell_id, mod360_cell_id, nub_concept_id),
    add index ix_or_nub_country (nub_concept_id, iso_country_code);

-- image record
rename table image_record to old_image_record;
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
insert into image_record
select id,data_resource_id,raw_occurrence_record_id,taxon_concept_id,raw_image_type,image_type,url,description,rights,html_for_display
from old_image_record;

-- identifier record
rename table identifier_record to old_identifier_record;
create table identifier_record (
       id int unsigned not null auto_increment
     , data_resource_id  smallint unsigned not null
     , occurrence_id int unsigned 
     , identifier_type smallint unsigned not null default 0
     , identifier varchar(255) not null
     , primary key (id)
) engine=myisam;
create index ix_identifier_record_occurrence on identifier_record (occurrence_id asc);
insert into identifier_record
select id,data_resource_id,raw_occurrence_record_id,identifier_type,identifier
from old_identifier_record;

-- link record
rename table link_record to old_link_record;
create table link_record (
       id int unsigned not null auto_increment
     , data_resource_id smallint unsigned  not null
     , occurrence_id int unsigned 
     , taxon_concept_id int unsigned
     , raw_link_type varchar(128)
     , link_type smallint unsigned not null default 0
     , url varchar(255) not null
     , description text
     , primary key (id)
) engine=myisam;
create index ix_link_record_occurrence on link_record (occurrence_id asc);
create index ix_link_record_taxon_concept on link_record (taxon_concept_id asc);
insert into link_record
select id,data_resource_id,raw_occurrence_record_id,taxon_concept_id,raw_link_type,link_type,url,description
from old_link_record;

-- typification record
rename table typification_record to old_typification_record;
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
insert into typification_record
select id,data_resource_id,raw_occurrence_record_id,taxon_name_id,scientific_name,publication,type_status,notes
from old_typification_record;

-- deduplicate the taxon_names
create table taxon_name_unique (
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
     , unique key (canonical(200), author(100), rank)
) engine=myisam;
insert ignore into taxon_name_unique select * from taxon_name;
create index ix_taxon_name_unique_canonical_rank on taxon_name_unique (canonical, rank);
create table tn_duplicate (
    unique_id mediumint unsigned,
    non_unique_id mediumint unsigned
) engine=myisam;
-- put in the ones that are not meant to be there
insert into tn_duplicate(non_unique_id)
select tn.id from taxon_name tn left join taxon_name_unique tnu on tn.id=tnu.id where tnu.id is null;
-- add an index
create index ix_tn_dup_non_unique on tn_duplicate (non_unique_id);
-- now update what they should be
update taxon_name tn inner join tn_duplicate tnd on tnd.non_unique_id=tn.id inner join taxon_name_unique tnu on tn.canonical=tnu.canonical and tn.rank=tnu.rank
and tn.author=tnu.author set tnd.unique_id=tnu.id;
-- add an index to check for error
create index ix_tn_dup_unique on tn_duplicate (unique_id);
-- now do some updates to correct the misused TN IDs
update typification_record tr
    inner join tn_duplicate tnd on tr.taxon_name_id = tnd.non_unique_id
set tr.taxon_name_id=tnd.unique_id;
update taxon_concept tc
    inner join tn_duplicate tnd on tc.taxon_name_id = tnd.non_unique_id
set tc.taxon_name_id=tnd.unique_id;
update occurrence_record o 
    inner join tn_duplicate tnd on o.taxon_name_id = tnd.non_unique_id
set o.taxon_name_id=tnd.unique_id;
delete tn.* from taxon_name tn inner join tn_duplicate tnd on tnd.non_unique_id=tn.id;
