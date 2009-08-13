-- populate the cell_density for country
insert into cell_density 
select 2, c.id, cell_id, count(oc.id) 
from occurrence_record oc 
inner join country c on oc.iso_country_code=c.iso_country_code 
where oc.cell_id is not null and oc.geospatial_issue=0
group by 1,2,3;

-- populate the cell_density for home country
-- This is the data for data_providers tied to a country
insert into cell_density 
select 6, c.id, cell_id, count(oc.id) 
from occurrence_record oc 
inner join data_provider dp on oc.data_provider_id=dp.id
inner join country c on dp.iso_country_code=c.iso_country_code
where oc.cell_id is not null and oc.geospatial_issue=0
group by 1,2,3;

-- populate the cell_density for provider
insert into cell_density
select 3,data_provider_id,cell_id,count(id)
from occurrence_record
where cell_id is not null and geospatial_issue=0
group by 1,2,3;

-- populate the cell_density for resource
insert into cell_density
select 4,data_resource_id,cell_id,count(id)
from occurrence_record
where cell_id is not null and geospatial_issue=0
group by 1,2,3;

-- populate the cell_density for resource_network
insert into cell_density
select 5,nm.resource_network_id,cell_id,count(oc.id)
from occurrence_record oc
inner join network_membership nm on oc.data_resource_id=nm.data_resource_id
where cell_id is not null and oc.geospatial_issue=0
group by nm.resource_network_id, oc.cell_id;

-- populate cell densities for all ORs on the denormalised nub id
insert ignore into cell_density 
select 1, nc.kingdom_concept_id, ore.cell_id, count(ore.id)
from occurrence_record ore inner join taxon_concept nc on nc.id = ore.nub_concept_id
where nc.kingdom_concept_id is not null
and ore.cell_id is not null and ore.geospatial_issue=0
group by 1,2,3;

-- populate cell densities for all ORs on the denormalised nub id    
insert ignore into cell_density 
select 1, nc.phylum_concept_id, ore.cell_id, count(ore.id)
from occurrence_record ore 
inner join taxon_concept nc on nc.id = ore.nub_concept_id
where nc.phylum_concept_id is not null
and ore.cell_id is not null and ore.geospatial_issue=0
group by 1,2,3;
    
-- populate cell densities for all ORs on the denormalised nub id
insert ignore into cell_density
select 1, nc.class_concept_id, ore.cell_id, count(ore.id)
from occurrence_record ore 
inner join taxon_concept nc on nc.id = ore.nub_concept_id
where nc.class_concept_id is not null
and ore.cell_id is not null and ore.geospatial_issue=0
group by 1,2,3;

-- populate cell densities for all ORs on the denormalised nub id
insert ignore into cell_density 
select 1, nc.order_concept_id, ore.cell_id, count(ore.id)
from occurrence_record ore 
inner join taxon_concept nc on nc.id = ore.nub_concept_id
where nc.order_concept_id is not null
and ore.cell_id is not null and ore.geospatial_issue=0
group by 1,2,3;
    
-- populate cell densities for all ORs on the denormalised nub id
insert ignore into cell_density 
select 1, nc.family_concept_id, ore.cell_id, count(ore.id)
from occurrence_record ore 
inner join taxon_concept nc on nc.id = ore.nub_concept_id
where nc.family_concept_id is not null
and ore.cell_id is not null and ore.geospatial_issue=0
group by 1,2,3;
    
-- populate cell densities for all ORs on the denormalised nub id
insert ignore into cell_density 
select 1, nc.genus_concept_id, ore.cell_id, count(ore.id)
from occurrence_record ore 
inner join taxon_concept nc on nc.id = ore.nub_concept_id
where nc.genus_concept_id is not null
and ore.cell_id is not null and ore.geospatial_issue=0
group by 1,2,3;

-- populate cell densities for all ORs on the denormalised nub id
insert ignore into cell_density 
select 1, nc.species_concept_id, ore.cell_id, count(ore.id)
from occurrence_record ore 
inner join taxon_concept nc on nc.id = ore.nub_concept_id
where nc.species_concept_id is not null
and ore.cell_id is not null and ore.geospatial_issue=0
group by 1,2,3;

-- populate the cell_density for taxonomy RUN THIS AFTER THE DENORMALISED!!!
insert ignore into cell_density
select 1, nub_concept_id,cell_id,count(id)
from occurrence_record
where cell_id is not null and geospatial_issue=0
group by 1,2,3;

-- populate for all things
insert ignore into cell_density
select 0, 0,cell_id,count(id)
from occurrence_record
where cell_id is not null and geospatial_issue=0
group by 1,2,3;
