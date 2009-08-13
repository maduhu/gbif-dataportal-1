truncate centi_cell_density_ns;

--generate nub concept centi cell densities
select concat('Building centi cells for nub concepts: ', now()) as debug;
insert ignore into centi_cell_density_ns
select 1,nub_concept_id,cell_id,centi_cell_id,count(id)
from occurrence_record_ns
where centi_cell_id is not null and geospatial_issue=0
group by 1,2,3,4;

--generate kingdom concept centi cell densities
select concat('Building centi cells for kingdom concepts: ', now()) as debug;
insert ignore into centi_cell_density_ns
select 1,tc.kingdom_concept_id,cell_id,centi_cell_id,sum(ccd.count)
from centi_cell_density_ns ccd
inner join taxon_concept tc ON tc.id=ccd.entity_id
where tc.kingdom_concept_id is not null and ccd.type=1
group by 1,2,3,4;

--generate phylum concept centi cell densities
select concat('Building centi cells for phyla concepts: ', now()) as debug;
insert ignore into centi_cell_density_ns
select 1,tc.phylum_concept_id,cell_id,centi_cell_id,sum(ccd.count)
from centi_cell_density_ns ccd
inner join taxon_concept tc ON tc.id=ccd.entity_id
where tc.phylum_concept_id is not null and ccd.type=1
group by 1,2,3,4;

--generate class concept centi cell densities
select concat('Building centi cells for class concepts: ', now()) as debug;
insert ignore into centi_cell_density_ns
select 1,tc.class_concept_id,cell_id,centi_cell_id,sum(ccd.count)
from centi_cell_density_ns ccd
inner join taxon_concept tc ON tc.id=ccd.entity_id
where tc.class_concept_id is not null and ccd.type=1
group by 1,2,3,4;

--generate order concept centi cell densities
select concat('Building centi cells for order concepts: ', now()) as debug;
insert ignore into centi_cell_density_ns
select 1,tc.order_concept_id,cell_id,centi_cell_id,sum(ccd.count)
from centi_cell_density_ns ccd
inner join taxon_concept tc ON tc.id=ccd.entity_id
where tc.order_concept_id is not null and ccd.type=1
group by 1,2,3,4;

--generate family concept centi cell densities
select concat('Building centi cells for family concepts: ', now()) as debug;
insert ignore into centi_cell_density_ns
select 1,tc.family_concept_id,cell_id,centi_cell_id,sum(ccd.count)
from centi_cell_density_ns ccd
inner join taxon_concept tc ON tc.id=ccd.entity_id
where tc.family_concept_id is not null and ccd.type=1
group by 1,2,3,4;

--generate genus concept centi cell densities
select concat('Building centi cells for genus concepts: ', now()) as debug;
insert ignore into centi_cell_density_ns
select 1,tc.genus_concept_id,cell_id,centi_cell_id,sum(ccd.count)
from centi_cell_density_ns ccd
inner join taxon_concept tc ON tc.id=ccd.entity_id
where tc.genus_concept_id is not null and ccd.type=1
group by 1,2,3,4;

--generate species concept centi cell densities
select concat('Building centi cells for species concepts: ', now()) as debug;
insert ignore into centi_cell_density_ns
select 1,tc.species_concept_id,cell_id,centi_cell_id,sum(ccd.count)
from centi_cell_density_ns ccd
inner join taxon_concept tc ON tc.id=ccd.entity_id
where tc.species_concept_id is not null and ccd.type=1
group by 1,2,3,4;