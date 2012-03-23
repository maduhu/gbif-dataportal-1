	-- occurrence record
create table occurrence_record_ns (
       id int unsigned not null 
     , data_provider_id smallint unsigned not null
     , data_resource_id smallint unsigned not null
     , institution_code_id smallint unsigned not null
     , collection_code_id mediumint unsigned not null
     , catalogue_number_id int unsigned not null
     , taxon_concept_id int unsigned not null
     , taxon_name_id int unsigned not null
	 , nub_concept_id int unsigned 
     , left_concept_id mediumint unsigned 
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
     , geospatial_issue mediumint unsigned not null default 0
     , other_issue tinyint unsigned not null default 0
	 , modified timestamp null default null
     , deleted timestamp null default null
     , primary key (id)
) engine=myisam;
alter table occurrence_record_ns
    add index ix_or_data_provider_id (data_provider_id),
    add index ix_or_data_resource_id (data_resource_id),
    add index ix_or_institution_code (institution_code_id),
    add index ix_or_collection_code (collection_code_id),
    add index ix_or_catalogue_number (catalogue_number_id),
	add index ix_or_taxon_name_id (taxon_name_id),
    add index ix_or_taxon_concept_id (taxon_concept_id),
    add index ix_or_iso_country_code (iso_country_code),
    add index ix_or_occurrence_date (occurrence_date),
    add index ix_or_month (month),
    add index ix_or_year (year),
    add index ix_or_altitude_metres (altitude_metres),
	add index ix_or_depth_centimetres (depth_centimetres),
    add index ix_or_left_concept_id_cell_mod_cell (left_concept_id, cell_id, mod360_cell_id),
	add index ix_or_cell_mod_cell (cell_id, mod360_cell_id),
    add index ix_or_nub_country (nub_concept_id, iso_country_code);

insert into occurrence_record_ns
select id 
     , data_provider_id
     , data_resource_id
     , institution_code_id
     , collection_code_id
     , catalogue_number_id
     , taxon_concept_id
     , taxon_name_id
	 , nub_concept_id 
     , NULL 
     , iso_country_code
     , latitude
     , longitude
     , cell_id
     , centi_cell_id
     , mod360_cell_id
	 , NULL
	 , NULL
     , year 
     , month
     , occurrence_date
     , basis_of_record
     , taxonomic_issue
     , geospatial_issue
     , other_issue
	 , NULL
     , deleted
from occurrence_record;