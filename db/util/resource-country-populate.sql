--
-- Creates the Data Resource to Country join table
--

truncate resource_country;

insert into resource_country 
      select data_resource_id, iso_country_code, count(id) 
      from occurrence_record 
      group by data_resource_id, iso_country_code;
