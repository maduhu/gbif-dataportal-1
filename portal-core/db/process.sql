/**
 * The process script will finalise the building of a database AFTER the nub has been built and the 
 * taxonomies denormalised. 
 *
 * last updated: 03.04.2009 (ahahn): added statistical tables for country and participant contribution overview
 * -- 12.8.2008 (ahahn): regenerate network membership for country nodes 
 */

-- May 6, 2008: 134,704,562 records on rancor (applies to all processing counts below)

-- Renews the network membership information for the country nodes
-- step 1: delete old records
-- !! take care: this relies on a literal value in the resource_network table which might change!
-- !! take care: this does not add new entries to resource_network!
select concat('Deleting network membership entries for country nodes: ', now()) as debug;
delete  network_membership.* 
from resource_network 
join network_membership on resource_network.id = network_membership.resource_network_id 
where resource_network.name like 'Resources hosted by%' ;

-- Renews the network membership information for the country nodes
-- step 2: generate new entries for network_membership based on providers' iso country codes
select concat('Adding new network membership entries for country nodes: ', now()) as debug;
insert into network_membership (data_resource_id, resource_network_id)
select d.id,
rn.id
from data_resource d
join data_provider p on d.data_provider_id = p.id
join resource_network rn on p.iso_country_code = rn.code
where p.iso_country_code is not null
and d.deleted is null;

-- Renews the network membership information for the country nodes
-- step 3: update the dataset counts for the resource network table
select concat('Updating the dataset counts for resource networks: ', now()) as debug;
update resource_network rn set data_resource_count =
(select count(nm.data_resource_id) from network_membership nm where nm.resource_network_id = rn.id);

-- Ties the OR to the nub and copies in the denormalised data 
-- Query OK, 14485805 rows affected (1 hour 35 min 8.55 sec)
-- Rows matched: 134704562  Changed: 14485805  Warnings: 0
select concat('Starting joining the OR to TC: ', now()) as debug;
update occurrence_record o inner join taxon_concept t on o.taxon_concept_id = t.id set o.nub_concept_id = t.partner_concept_id;

-- Copy the denormalised taxonomy to occurrence_record
-- Query OK, 16032162 rows affected (3 hours 25 min 51.67 sec)
-- Rows matched: 134476889  Changed: 16032162  Warnings: 0
select concat('Starting copying the denormalised taxonomy to OR: ', now()) as debug;
update occurrence_record o 
    inner join taxon_concept tc on o.nub_concept_id=tc.id
set 
    o.kingdom_concept_id=tc.kingdom_concept_id,
    o.phylum_concept_id=tc.phylum_concept_id,
    o.class_concept_id=tc.class_concept_id,
    o.order_concept_id=tc.order_concept_id,
    o.family_concept_id=tc.family_concept_id,
    o.genus_concept_id=tc.genus_concept_id,
    o.species_concept_id=tc.species_concept_id;

-- clear the centi cells
-- Query OK, 0 rows affected (54.06 sec)
select concat('Clearing centi cells: ', now()) as debug;    
truncate table centi_cell_density;

-- populate the centi_cell_density for country
-- Query OK, 405400 rows affected (17 min 2.61 sec)
-- Records: 405400  Duplicates: 0  Warnings: 0
select concat('Building centi cells for country: ', now()) as debug;
insert into centi_cell_density 
select 2, c.id, cell_id, centi_cell_id, count(oc.id) 
from occurrence_record oc 
inner join country c on oc.iso_country_code=c.iso_country_code 
where oc.centi_cell_id is not null and oc.geospatial_issue=0
group by 1,2,3,4;

-- populate the cell_density for home country
-- This is the data for data_providers tied to a country
-- Query OK, 486945 rows affected (8 min 44.25 sec)
-- Records: 486945  Duplicates: 0  Warnings: 0
select concat('Building centi cells for home country: ', now()) as debug;
insert into centi_cell_density 
select 6, c.id, cell_id, centi_cell_id, count(oc.id) 
from occurrence_record oc 
inner join data_provider dp on oc.data_provider_id=dp.id
inner join country c on dp.iso_country_code=c.iso_country_code
where oc.centi_cell_id is not null and oc.geospatial_issue=0
group by 1,2,3,4;

-- populate home country for international networks
-- Query OK, 262835 rows affected (56.98 sec)
-- Records: 262835  Duplicates: 0  Warnings: 0
select concat('Building centi cells for international networks: ', now()) as debug;
insert into centi_cell_density 
select 6, 0, cell_id, centi_cell_id, count(oc.id) 
from occurrence_record oc 
inner join data_provider dp on oc.data_provider_id=dp.id
where dp.iso_country_code is null and oc.centi_cell_id is not null and oc.geospatial_issue=0
group by 3,4;

-- populate the centi_cell_density for provider
-- Query OK, 1010956 rows affected (3 min 39.17 sec)
-- Records: 1010956  Duplicates: 0  Warnings: 0
select concat('Building centi cells for provider: ', now()) as debug;
insert into centi_cell_density
select 3,data_provider_id,cell_id,centi_cell_id,count(id)
from occurrence_record
where centi_cell_id is not null and geospatial_issue=0
group by 1,2,3,4;

-- populate the centi_cell_density for resource
-- Query OK, 1350386 rows affected (3 min 40.00 sec)
-- Records: 1350386  Duplicates: 0  Warnings: 0
select concat('Building centi cells for resource: ', now()) as debug;
insert into centi_cell_density
select 4,data_resource_id,cell_id,centi_cell_id,count(id)
from occurrence_record
where centi_cell_id is not null and geospatial_issue=0
group by 1,2,3,4;

-- populate the centi_cell_density for resource_network
-- Query OK, 820967 rows affected (10 min 31.83 sec)
-- Records: 820967  Duplicates: 0  Warnings: 0
select concat('Building centi cells for network: ', now()) as debug;
insert into centi_cell_density
select 5,nm.resource_network_id,cell_id,centi_cell_id,count(oc.id)
from occurrence_record oc
inner join network_membership nm on oc.data_resource_id=nm.data_resource_id
where centi_cell_id is not null and oc.geospatial_issue=0
group by nm.resource_network_id, oc.cell_id, oc.centi_cell_id;

-- populate cell densities for all ORs on the denormalised nub id
-- Query OK, 873791 rows affected (3 min 58.67 sec)
-- Records: 873791  Duplicates: 0  Warnings: 0
select concat('Building centi cells for kingdom: ', now()) as debug;
insert ignore into centi_cell_density 
select 1, ore.kingdom_concept_id, ore.cell_id, ore.centi_cell_id, count(ore.id)
from occurrence_record ore
where ore.kingdom_concept_id is not null
and ore.centi_cell_id is not null and ore.geospatial_issue=0
group by 1,2,3,4;

-- populate cell densities for all ORs on the denormalised nub id    
-- Query OK, 1411770 rows affected (4 min 5.77 sec)
-- Records: 1411770  Duplicates: 0  Warnings: 0
select concat('Building centi cells for phylum: ', now()) as debug;
insert ignore into centi_cell_density 
select 1, ore.phylum_concept_id, ore.cell_id, ore.centi_cell_id, count(ore.id)
from occurrence_record ore 
where ore.phylum_concept_id is not null
and ore.centi_cell_id is not null and ore.geospatial_issue=0
group by 1,2,3,4;
    
-- populate cell densities for all ORs on the denormalised nub id
-- Query OK, 1868755 rows affected (4 min 9.33 sec)
-- Records: 1868755  Duplicates: 0  Warnings: 0
select concat('Building centi cells for class: ', now()) as debug;
insert ignore into centi_cell_density
select 1, ore.class_concept_id, ore.cell_id, ore.centi_cell_id, count(ore.id)
from occurrence_record ore 
where ore.class_concept_id is not null
and ore.centi_cell_id is not null and ore.geospatial_issue=0
group by 1,2,3,4;

-- populate cell densities for all ORs on the denormalised nub id
-- Query OK, 4427337 rows affected (4 min 42.53 sec)
-- Records: 4427337  Duplicates: 0  Warnings: 0
select concat('Building centi cells for order: ', now()) as debug;
insert ignore into centi_cell_density 
select 1, ore.order_concept_id, ore.cell_id, ore.centi_cell_id, count(ore.id)
from occurrence_record ore 
where ore.order_concept_id is not null
and ore.centi_cell_id is not null and ore.geospatial_issue=0
group by 1,2,3,4;
    
-- populate cell densities for all ORs on the denormalised nub id
-- Query OK, 7395361 rows affected (5 min 22.05 sec)
-- Records: 7395361  Duplicates: 0  Warnings: 0
select concat('Building centi cells for family: ', now()) as debug;
insert ignore into centi_cell_density 
select 1, ore.family_concept_id, ore.cell_id, ore.centi_cell_id, count(ore.id)
from occurrence_record ore 
where ore.family_concept_id is not null
and ore.centi_cell_id is not null and ore.geospatial_issue=0
group by 1,2,3,4;
    
-- populate cell densities for all ORs on the denormalised nub id
-- Query OK, 15041868 rows affected (22 min 31.16 sec)
-- Records: 15041868  Duplicates: 0  Warnings: 0
select concat('Building centi cells for genus: ', now()) as debug;
insert ignore into centi_cell_density 
select 1, ore.genus_concept_id, ore.cell_id, ore.centi_cell_id, count(ore.id)
from occurrence_record ore 
where ore.genus_concept_id is not null
and ore.centi_cell_id is not null and ore.geospatial_issue=0
group by 1,2,3,4;

-- populate cell densities for all ORs on the denormalised nub id
-- Query OK, 20795641 rows affected (46 min 45.89 sec)
-- Records: 20795641  Duplicates: 0  Warnings: 0
select concat('Building centi cells for species: ', now()) as debug;
insert ignore into centi_cell_density 
select 1, ore.species_concept_id, ore.cell_id, ore.centi_cell_id, count(ore.id)
from occurrence_record ore 
where ore.species_concept_id is not null
and ore.centi_cell_id is not null and ore.geospatial_issue=0
group by 1,2,3,4;

-- populate the centi_cell_density for taxonomy RUN THIS AFTER THE DENORMALISED!!!
-- Query OK, 2433039 rows affected, 5441 warnings (58 min 51.94 sec)
-- Records: 23600228  Duplicates: 21167189  Warnings: 5441
-- (warnings are expected and can be ignored: "insert ignore")
select concat('Building centi cells for nub concept: ', now()) as debug;
insert ignore into centi_cell_density
select 1, nub_concept_id,cell_id,centi_cell_id,count(id)
from occurrence_record
where centi_cell_id is not null and geospatial_issue=0
group by 1,2,3,4;

-- populate for all things
-- Query OK, 579411 rows affected (3 min 13.33 sec)
-- Records: 579411  Duplicates: 0  Warnings: 0
select concat('Building centi cells for all things: ', now()) as debug;
insert ignore into centi_cell_density
select 0, 0,cell_id,centi_cell_id,count(id)
from occurrence_record
where centi_cell_id is not null and geospatial_issue=0
group by 1,2,3,4;

-- clear the cell densities
-- Query OK, 0 rows affected (0.28 sec)
select concat('Clearing cells: ', now()) as debug;
truncate table cell_density;

-- build the cell_densities
-- Query OK, 13623180 rows affected (18 min 0.63 sec)
-- Records: 13623180  Duplicates: 0  Warnings: 0
select concat('Building cell densities: ', now()) as debug;
insert ignore into cell_density 
select type,entity_id,cell_id,sum(count)
from centi_cell_density
group by 1,2,3;

-- updates and sets the countries count
-- Query OK, 240 rows affected (5 min 17.92 sec)
-- Rows matched: 243  Changed: 240  Warnings: 0
select concat('Starting country occurrence count: ', now()) as debug;
update country c set occurrence_count =
(select count(id) from occurrence_record o where o.iso_country_code=c.iso_country_code);
    
-- set occurrence record coordinate count
-- Query OK, 235 rows affected (0.05 sec)
-- Rows matched: 243  Changed: 235  Warnings: 0
select concat('Starting country occurrence coordinate count: ', now()) as debug;
update country c set occurrence_coordinate_count =   
(select sum(cd.count) from cell_density cd where cd.entity_id=c.id and cd.type=2);
    
-- set species count per country
-- this used to be species and lower concepts as well - changed 12.8.08
-- Query OK, 238 rows affected (7 min 35.92 sec)
-- Rows matched: 243  Changed: 238  Warnings: 0
select concat('Starting country species count: ', now()) as debug;
update country c set species_count = 
(select count(distinct o.species_concept_id) from occurrence_record o where o.iso_country_code = c.iso_country_code);      
/* was before update 12.8.08: (select count(distinct o.nub_concept_id) from occurrence_record o where o.iso_country_code = c.iso_country_code); */

-- set occurrence record count
-- Query OK, 662 rows affected (4 min 14.25 sec)
-- Rows matched: 2277  Changed: 662  Warnings: 0
select concat('Starting resource occurrence count: ', now()) as debug;
update data_resource dr set occurrence_count =   
(select count(o.id) from occurrence_record o where o.data_resource_id=dr.id);

-- set occurrence record coordinate count
-- Query OK, 642 rows affected (3 min 52.58 sec)
-- Rows matched: 2277  Changed: 642  Warnings: 0
select concat('Starting resource occurrence coordinate count: ', now()) as debug;
update data_resource dr set occurrence_coordinate_count =   
(select count(o.id) from occurrence_record o where o.data_resource_id=dr.id and o.cell_id is not null);

-- set occurrence record clean geospatial count
-- Query OK, 656 rows affected (0.45 sec)
-- Rows matched: 2277  Changed: 656  Warnings: 0
select concat('Starting resource occurrence coordinate count: ', now()) as debug;
update data_resource dr set occurrence_clean_geospatial_count =   
(select sum(cd.count) from cell_density cd where cd.type=4 and cd.entity_id=dr.id);

-- set species count
-- Query OK, 371 rows affected (36.66 sec)
-- Rows matched: 2277  Changed: 371  Warnings: 0
select concat('Starting resource species count: ', now()) as debug;
update data_resource dr set species_count =       
(select count(tc.id) from taxon_concept tc where tc.data_resource_id = dr.id and tc.rank>=7000 and tc.rank<8000);

-- set concept count   
-- Query OK, 401 rows affected (18.61 sec)
-- Rows matched: 2277  Changed: 401  Warnings: 0
select concat('Starting resource taxon concept count: ', now()) as debug;
update data_resource dr set concept_count =       
(select count(tc.id) from taxon_concept tc where tc.data_resource_id = dr.id);
    
-- set occurrence record count
-- Query OK, 61 rows affected (0.05 sec)
-- Rows matched: 250  Changed: 61  Warnings: 0
select concat('Starting provider occurrence count: ', now()) as debug;
update data_provider dp set occurrence_count =   
(select sum(dr.occurrence_count) from data_resource dr where dr.data_provider_id=dp.id);

-- set occurrence record coordinate count
-- Query OK, 56 rows affected (0.02 sec)
-- Rows matched: 250  Changed: 56  Warnings: 0
select concat('Starting provider occurrence coordinate count: ', now()) as debug;
update data_provider dp set occurrence_coordinate_count =   
(select sum(dr.occurrence_coordinate_count) from data_resource dr where dr.data_provider_id=dp.id);

-- set concept count  
-- Query OK, 67 rows affected (0.01 sec)
-- Rows matched: 250  Changed: 67  Warnings: 0 
select concat('Starting provider taxon concept count: ', now()) as debug; 
update data_provider dp set concept_count =       
(select sum(dr.concept_count) from data_resource dr where dr.data_provider_id=dp.id);
    
-- data resource count    
-- modified 22.1.09, ahahn: exclude deleted resources from count
-- Query OK, 25 rows affected (0.01 sec)
-- Rows matched: 250  Changed: 25  Warnings: 0
select concat('Starting provider resource count: ', now()) as debug; 
update data_provider dp set data_resource_count = 
(select count(dr.id) from data_resource dr where dr.data_provider_id=dp.id and deleted is null);     
    
-- occurrence count
-- Query OK, 21 rows affected (0.05 sec)
-- Rows matched: 44  Changed: 21  Warnings: 0
select concat('Starting network occurrence count: ', now()) as debug; 
update resource_network rn set occurrence_count =   
(select sum(dr.occurrence_count) from data_resource dr 
    inner join network_membership nm on dr.id = nm.data_resource_id where nm.resource_network_id=rn.id);    

-- occurrence record coordinate count 
-- Query OK, 19 rows affected (0.01 sec)
-- Rows matched: 44  Changed: 19  Warnings: 0
select concat('Starting network occurrence coordinate count: ', now()) as debug; 
update resource_network rn set occurrence_coordinate_count =   
(select sum(dr.occurrence_coordinate_count) from data_resource dr 
    inner join network_membership nm on dr.id = nm.data_resource_id where nm.resource_network_id=rn.id);       
    
-- tidy up floating taxa (14719007 = unknown kingdom)
-- Query OK, 3 rows affected (1.91 sec)
-- Rows matched: 3  Changed: 3  Warnings: 0
select concat('Tidying up floating taxa: ', now()) as debug; 
update taxon_concept set parent_concept_id=14719007
where data_resource_id=1 and rank>1000 and priority>10 and priority<10000 and parent_concept_id is null;

-- generate resource_country join table
-- Query OK, 34749 rows affected, 1257 warnings (3 min 15.73 sec)
-- Records: 34749  Duplicates: 0  Warnings: 1257
select concat('Starting resource_country generation: ', now()) as debug;
truncate table resource_country;
insert into resource_country 
      select data_resource_id, iso_country_code, count(id), 0 
      from occurrence_record 
      group by data_resource_id, iso_country_code;

-- generate occurrence_coordinate_count value for resource_country join table (using temporary table)
-- tested on 119m DB - takes 8 mins to fully execute 
-- Query OK, 15070 rows affected (2 min 56.34 sec)
-- Records: 15070  Duplicates: 0  Warnings: 0
select concat('Starting resource_country occurrence_coordinate_count generation: ', now()) as debug;
create table temp_resource_country_occ 
select oc.data_resource_id, oc.iso_country_code, count(oc.id) as occurrence_coordinate_count
from occurrence_record oc 
where oc.cell_id is not null
group by oc.data_resource_id, oc.iso_country_code;


-- Query OK, 34749 rows affected (7 min 42.06 sec)
-- Rows matched: 34749  Changed: 34749  Warnings: 0
select concat('Update resource_country with the values calculated in previous step: ', now()) as debug;
update resource_country rc set rc.occurrence_coordinate_count =
(select temp_rc_occ.occurrence_coordinate_count 
from temp_resource_country_occ temp_rc_occ 
where rc.data_resource_id = temp_rc_occ.data_resource_id
and rc.iso_country_code = temp_rc_occ.iso_country_code);
-- drop temp table
drop table temp_resource_country_occ;


-- Query OK, 1550 rows affected (3 min 42.20 sec)
-- Records: 1550  Duplicates: 0  Warnings: 0
select concat('Starting taxon_country kingdom generation: ', now()) as debug;
truncate table taxon_country;
-- populate taxon_country
insert ignore into taxon_country 
select kingdom_concept_id, iso_country_code, count(*)
from occurrence_record 
where kingdom_concept_id is not null
and iso_country_code is not null
group by 1,2;


-- populate taxon_country
-- Query OK, 9932 rows affected (3 min 43.63 sec)
-- Records: 9932  Duplicates: 0  Warnings: 0
select concat('Starting taxon_country phylum generation: ', now()) as debug;
insert ignore into taxon_country 
select phylum_concept_id, iso_country_code, count(*)
from occurrence_record 
where phylum_concept_id is not null
and iso_country_code is not null
group by 1,2;

-- populate taxon_country
-- Query OK, 20544 rows affected (3 min 33.58 sec)
-- Records: 20544  Duplicates: 0  Warnings: 0
select concat('Starting taxon_country class generation: ', now()) as debug;
insert ignore into taxon_country 
select class_concept_id, iso_country_code, count(*)
from occurrence_record 
where class_concept_id is not null
and iso_country_code is not null
group by 1,2;

-- populate taxon_country
-- Query OK, 74553 rows affected (3 min 36.94 sec)
-- Records: 74553  Duplicates: 0  Warnings: 0
select concat('Starting taxon_country order generation: ', now()) as debug;
insert ignore into taxon_country 
select order_concept_id, iso_country_code, count(*)
from occurrence_record 
where order_concept_id is not null
and iso_country_code is not null
group by 1,2;

-- populate taxon_country
-- Query OK, 250666 rows affected (3 min 45.73 sec)
-- Records: 250666  Duplicates: 0  Warnings: 0
select concat('Starting taxon_country family generation: ', now()) as debug;
insert ignore into taxon_country 
select family_concept_id, iso_country_code, count(*)
from occurrence_record 
where family_concept_id is not null
and iso_country_code is not null
group by 1,2;

-- populate taxon_country
-- Query OK, 941366 rows affected (4 min 10.50 sec)
-- Records: 941366  Duplicates: 0  Warnings: 0
select concat('Starting taxon_country genus generation: ', now()) as debug;
insert ignore into taxon_country 
select genus_concept_id, iso_country_code, count(*)
from occurrence_record 
where genus_concept_id is not null
and iso_country_code is not null
group by 1,2;

-- populate taxon_country
-- Query OK, 2478276 rows affected (4 min 41.34 sec)
-- Records: 2478276  Duplicates: 0  Warnings: 0
select concat('Starting taxon_country species generation: ', now()) as debug;
insert ignore into taxon_country 
select species_concept_id, iso_country_code, count(*)
from occurrence_record 
where species_concept_id is not null
and iso_country_code is not null
group by 1,2;

-- populate taxon_country
-- Query OK, 463453 rows affected, 167 warnings (5 min 49.99 sec)
-- Records: 3148580  Duplicates: 2685127  Warnings: 167
select concat('Starting taxon_country nub concept generation: ', now()) as debug;
insert ignore into taxon_country 
select nub_concept_id, iso_country_code, count(*)
from occurrence_record
where iso_country_code is not null
group by 1,2;

-- temporal range - temporal range for this dataset
-- Query OK, 2083 rows affected, 1200 warnings (2 min 24.52 sec)
-- Records: 2083  Duplicates: 0  Warnings: 1200
-- (NULL values in non-null column start-date)
select concat('Starting temporal_coverage_tag generation: ', now()) as debug;
delete from temporal_coverage_tag where tag_id=4120;
insert into temporal_coverage_tag (tag_id, entity_id, start_date, end_date, is_system_generated)
select 4120, data_resource_id, min(occurrence_date), max(occurrence_date), true 
from occurrence_record 
group by data_resource_id;
-- remove null or erroneous entries
delete from temporal_coverage_tag where start_date = '0000-00-00';
delete from temporal_coverage_tag where start_date < '1700-01-01';

-- bounding box - a bounding box for this dataset
-- Query OK, 2083 rows affected (2 min 31.25 sec)
-- Records: 2083  Duplicates: 0  Warnings: 0
select concat('Starting geographical_coverage_tag (bounding box) generation: ', now()) as debug;
delete from geographical_coverage_tag where tag_id=4101;
insert into geographical_coverage_tag (tag_id, entity_id, min_longitude, min_latitude, max_longitude, max_latitude,is_system_generated)
select 4101, data_resource_id, min(longitude), min(latitude), max(longitude), max(latitude), true 
from occurrence_record 
group by data_resource_id;
-- remove null entries
delete from geographical_coverage_tag where min_longitude is null;

-- taxonomic coverage for a resource by family, genus and species
-- Query OK, 3288465 rows affected (3 min 46.58 sec)
-- Records: 3288465  Duplicates: 0  Warnings: 0
select concat('Starting bi_relation_tag - species coverage generation: ', now()) as debug;
delete from bi_relation_tag where tag_id=4140;
insert into bi_relation_tag (tag_id, from_entity_id, to_entity_id, count, is_system_generated)
select 4140, data_resource_id, species_concept_id, count(id), true
from occurrence_record oc
where species_concept_id is not null
group by data_resource_id, species_concept_id;

-- Query OK, 932714 rows affected (5 min 8.92 sec)
-- Records: 932714  Duplicates: 0  Warnings: 0
select concat('Starting bi_relation_tag - genera coverage generation: ', now()) as debug;
delete from bi_relation_tag where tag_id=4141;
insert into bi_relation_tag (tag_id, from_entity_id, to_entity_id, count, is_system_generated)
select 4141, data_resource_id, genus_concept_id, count(id), true
from occurrence_record oc
where genus_concept_id is not null
group by data_resource_id, genus_concept_id;

-- Query OK, 190419 rows affected (3 min 30.31 sec)
-- Records: 190419  Duplicates: 0  Warnings: 0
select concat('Starting bi_relation_tag - family coverage generation: ', now()) as debug;
delete from number_tag where tag_id=4142;
insert into bi_relation_tag (tag_id, from_entity_id, to_entity_id, count, is_system_generated)
select 4142, data_resource_id, family_concept_id, count(id), true
from occurrence_record oc
where family_concept_id is not null
group by data_resource_id, family_concept_id;

-- contains type status
-- Query OK, 234 rows affected (1.75 sec)
-- Records: 234  Duplicates: 0  Warnings: 0
select concat('Starting number_tag - typification_record count: ', now()) as debug;
delete from number_tag where tag_id=4160;
insert into number_tag (entity_id, tag_id, value, is_system_generated)
select data_resource_id,4160,count(data_resource_id),true from typification_record group by data_resource_id;

-- month tags
-- Query OK, 10048 rows affected (1 min 21.41 sec)
-- Records: 10048  Duplicates: 0  Warnings: 0
select concat('Starting number_tag - occurrences by month: ', now()) as debug;
delete from number_tag where tag_id=4121;
insert into number_tag (value, entity_id, tag_id, is_system_generated)
select distinct month, data_resource_id, 4121, true from occurrence_record 
where month is not null
group by month, data_resource_id 
order by month, data_resource_id;

-- host country x country x kingdoms x basis of record (25 mins to generate on 6GB machine, 120m DB)
-- Query OK, 20732 rows affected (12 min 12.44 sec)
-- Records: 20732  Duplicates: 0  Warnings: 0
select concat('Starting country x country x kingdoms x basis of record tags: ', now()) as debug;
create table temp_hc_c_k_bor 
select dp.iso_country_code host, o.iso_country_code country, o.kingdom_concept_id, o.basis_of_record, count(*) as hc_count, 0 as rollover_id 
from data_provider dp left outer join occurrence_record o on o.data_provider_id=dp.id group by 4,3,2,1 order by 1,2,3,4;

-- add the rollover date to this table
-- Query OK, 20732 rows affected (0.05 sec)
-- Rows matched: 20732  Changed: 20732  Warnings: 0
insert into rollover (rollover_date) values (now());
update temp_hc_c_k_bor t set t.rollover_id  = (select max(r.id) from rollover r);

-- delete the unknown countries, since the UI has a bug that will be fixed in a future release
delete from quad_relation_tag where entity2_id=0 and tag_id=2001;

-- insert tags in quadnomial_tag
-- Query OK, 20732 rows affected (1.34 sec)
-- Records: 20732  Duplicates: 0  Warnings: 0
insert into quad_relation_tag (tag_id, entity1_id, entity2_id, entity3_id, entity4_id, count, rollover_id)
select 2001, hc.id, co.id, kingdom_concept_id, basis_of_record, hc_count, rollover_id
from temp_hc_c_k_bor
left outer join country hc on temp_hc_c_k_bor.host = hc.iso_country_code
left outer join country co on temp_hc_c_k_bor.country = co.iso_country_code;

-- set to 0 where entity_id is null
-- Query OK, 1613 rows affected (0.05 sec)
-- Rows matched: 1613  Changed: 1613  Warnings: 0
update quad_relation_tag set entity1_id=0 where entity1_id is null and tag_id=2001; show warnings;
-- Query OK, 399 rows affected (0.02 sec)
-- Rows matched: 399  Changed: 399  Warnings: 0
update quad_relation_tag set entity2_id=0 where entity2_id is null and tag_id=2001; show warnings;
-- assign tags with null kingdom ids to unknown kingdom
-- Query OK, 3967 rows affected (0.01 sec)
-- Rows matched: 3967  Changed: 3967  Warnings: 0
update quad_relation_tag set entity3_id=14719007 where entity3_id is null; show warnings;
drop table temp_hc_c_k_bor;

-- add statistics for counts per country
INSERT INTO stats_country_contribution (
rollover_id, iso_country_code, provider_count, dataset_count, occurrence_count, occurrence_georeferenced_count, created)
SELECT 
(select max(r.id) from rollover r), dp.iso_country_code, count(distinct dp.id), count(distinct dr.id), sum(dr.occurrence_count), sum(dr.occurrence_coordinate_count), now()
from data_provider dp
left join data_resource dr on dp.id = dr.data_provider_id
where dr.deleted is null and dp.deleted is null and dp.id > 3
group by 2;

-- add statistics for counts per participant node
INSERT INTO stats_participant_contribution (
rollover_id, gbif_approver, provider_count, dataset_count, occurrence_count, occurrence_georeferenced_count, created)
SELECT 
(select max(r.id) from rollover r), dp.gbif_approver, count(distinct dp.id), count(distinct dr.id), sum(dr.occurrence_count), sum(dr.occurrence_coordinate_count), now()
from data_provider dp
left join data_resource dr on dp.id = dr.data_provider_id
where dr.deleted is null and dp.deleted is null and dp.id > 3
group by 2;

-- to eliminate possiblity of infinites loops in the taxonomy
update taxon_concept c
inner join taxon_concept p on c.parent_concept_id=p.id
set c.parent_concept_id=null
where p.rank>=c.rank;

-- populate temporary table to assign unique keys to each of the participant nodes available
truncate table temp_participant_nodes;
insert into temp_participant_nodes(node_name)
select distinct(data_provider.gbif_approver) from data_provider where data_provider.gbif_approver!='NULL'

-- save stats for the communication portal. Files will be residing on the /tmp/ folder on the machine running process.sql
select * from temp_participant_nodes into outfile '/tmp/comm_nodes.txt';
select dp.id,tpn.id,dp.name from data_provider dp inner join temp_participant_nodes tpn on dp.gbif_approver = tpn.node_name where dp.deleted is null and dp.id>3 and dp.id!=223 and dp.id!=226  into outfile '/tmp/comm_dataprovider.txt';
select dr.id, dp.id, dr.name, dr.occurrence_count, dr.occurrence_coordinate_count from data_resource dr inner join data_provider dp on dr.data_provider_id=dp.id  where dp.deleted is null and dp.id>3 and dp.id!=223 and dp.id!=226 and dr.deleted is null into outfile '/tmp/comm_dataresource.txt';

select concat('Rollover complete: ', now()) as debug;