truncate taxon_country;

-- populate taxon_country
insert ignore into taxon_country 
select nc.kingdom_concept_id, ore.iso_country_code, count(ore.id)
from occurrence_record ore inner join taxon_concept nc on nc.id = ore.nub_concept_id
where nc.kingdom_concept_id is not null
and ore.iso_country_code is not null
group by 1,2;

-- populate taxon_country
insert ignore into taxon_country 
select nc.phylum_concept_id, ore.iso_country_code, count(ore.id)
from occurrence_record ore inner join taxon_concept nc on nc.id = ore.nub_concept_id
where nc.phylum_concept_id is not null
and ore.iso_country_code is not null
group by 1,2;

-- populate taxon_country
insert ignore into taxon_country 
select nc.class_concept_id, ore.iso_country_code, count(ore.id)
from occurrence_record ore inner join taxon_concept nc on nc.id = ore.nub_concept_id
where nc.class_concept_id is not null
and ore.iso_country_code is not null
group by 1,2;

-- populate taxon_country
insert ignore into taxon_country 
select nc.order_concept_id, ore.iso_country_code, count(ore.id)
from occurrence_record ore inner join taxon_concept nc on nc.id = ore.nub_concept_id
where nc.order_concept_id is not null
and ore.iso_country_code is not null
group by 1,2;

-- populate taxon_country
insert ignore into taxon_country 
select nc.family_concept_id, ore.iso_country_code, count(ore.id)
from occurrence_record ore inner join taxon_concept nc on nc.id = ore.nub_concept_id
where nc.family_concept_id is not null
and ore.iso_country_code is not null
group by 1,2;

-- populate taxon_country
insert ignore into taxon_country 
select nc.genus_concept_id, ore.iso_country_code, count(ore.id)
from occurrence_record ore inner join taxon_concept nc on nc.id = ore.nub_concept_id
where nc.genus_concept_id is not null
and ore.iso_country_code is not null
group by 1,2;

-- populate taxon_country
insert ignore into taxon_country 
select nc.species_concept_id, ore.iso_country_code, count(ore.id)
from occurrence_record ore inner join taxon_concept nc on nc.id = ore.nub_concept_id
where nc.species_concept_id is not null
and ore.iso_country_code is not null
group by 1,2;

-- populate taxon_country
insert ignore into taxon_country 
select nub_concept_id, iso_country_code, count(id)
from occurrence_record
where iso_country_code is not null
group by 1,2;
