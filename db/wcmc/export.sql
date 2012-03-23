--
-- This is the export script that will create files for importing into the WCMC processing database (PostGIS)
-- for Spain and Madagascar 
-- 
-- Spain (bounding boxes)
--  i)  cell:45167, 48785
--  ii) cell:41920, 43367
--
-- Madagascar (with a buffer)
--  i) cell:22541, 29032
--
-- Basis of record
--  unknown, 0
--  observation, 1
--  specimen, 2
--  living, 3
--  germplasm, 4
--  fossil, 5
--  literature, 6
--


/*
CREATE TABLE `gbif_occurrence` (
  `id` int(10) unsigned NOT NULL,
  `data_provider_id` smallint(5) unsigned NOT NULL,
  `data_provider_name` varchar(255) default NULL,
  `data_resource_id` smallint(5) unsigned NOT NULL,
  `data_resource_name` varchar(255) default NULL,
  `nub_concept_id` int(10) unsigned default NULL,
  `kingdom_concept_id` int(10) unsigned default '0',
  `kingdom_name` varchar(255),
  `phylum_concept_id` int(10) unsigned default '0',
  `phylum_name` varchar(255),
  `class_concept_id` int(10) unsigned default '0',
  `class_name` varchar(255),
  `order_concept_id` int(10) unsigned default '0',
  `order_name` varchar(255),
  `family_concept_id` int(10) unsigned default '0',
  `family_name` varchar(255),
  `genus_concept_id` int(10) unsigned default '0',
  `genus_name` varchar(255),
  `scientific_name` varchar(255),
  `latitude` float default NULL,
  `longitude` float default NULL,
  `cell_id` smallint(5) unsigned default NULL,
  `centi_cell_id` tinyint(3) unsigned default NULL,
  `basis_of_record` tinyint(3) unsigned NOT NULL default '0',
  `year` smallint(5) unsigned default NULL,
  `month` tinyint(3) unsigned default NULL,
  `date` date default NULL
);
*/

-- spain i
select 
	o.id,
	o.data_provider_id,
	dp.name,
	o.data_resource_id,
	dr.name,
	o.nub_concept_id,
	kc.id,
	kn.canonical,
	pc.id,
	pn.canonical,
	cc.id,
	cn.canonical,
	oc.id,
	orn.canonical,
	fc.id,
	fn.canonical,
	gc.id,
	gn.canonical,
	sn.canonical,
	o.latitude,
	o.longitude,
	o.cell_id,
	o.centi_cell_id,
	o.basis_of_record,
	o.year,
	o.month,
	o.occurrence_date,
	o.iso_country_code
into outfile '/tmp/spain-mainland.txt'
from
	occurrence_record o
		inner join data_provider dp on o.data_provider_id = dp.id
		inner join data_resource dr on o.data_resource_id = dr.id
        inner join taxon_concept tc on o.nub_concept_id = tc.id
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
	        left join taxon_name sn on sc.taxon_name_id = sn.id
where
	o.cell_id >= 45167 and 
	o.cell_id <= 48785 and
	o.mod360_cell_id >= mod(45167, 360) and 
	o.mod360_cell_id <= mod(48785, 360);

-- spain ii
select 
	o.id,
	o.data_provider_id,
	dp.name,
	o.data_resource_id,
	dr.name,
	o.nub_concept_id,
	kc.id,
	kn.canonical,
	pc.id,
	pn.canonical,
	cc.id,
	cn.canonical,
	oc.id,
	orn.canonical,
	fc.id,
	fn.canonical,
	gc.id,
	gn.canonical,
	sn.canonical,
	o.latitude,
	o.longitude,
	o.cell_id,
	o.centi_cell_id,
	o.basis_of_record,
	o.year,
	o.month,
	o.occurrence_date,
	o.iso_country_code
into outfile '/tmp/spain-canaries.txt'
from
	occurrence_record o
		inner join data_provider dp on o.data_provider_id = dp.id
		inner join data_resource dr on o.data_resource_id = dr.id
        inner join taxon_concept tc on o.nub_concept_id = tc.id
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
	        left join taxon_name sn on sc.taxon_name_id = sn.id
where
	o.cell_id >= 41920 and 
	o.cell_id <= 43367 and
	o.mod360_cell_id >= mod(41920, 360) and 
	o.mod360_cell_id <= mod(43367, 360);


-- madagascar
select 
	o.id,
	o.data_provider_id,
	dp.name,
	o.data_resource_id,
	dr.name,
	o.nub_concept_id,
	kc.id,
	kn.canonical,
	pc.id,
	pn.canonical,
	cc.id,
	cn.canonical,
	oc.id,
	orn.canonical,
	fc.id,
	fn.canonical,
	gc.id,
	gn.canonical,
	sn.canonical,
	o.latitude,
	o.longitude,
	o.cell_id,
	o.centi_cell_id,
	o.basis_of_record,
	o.year,
	o.month,
	occurrence_date
into outfile '/tmp/madagascar.txt'
from
	occurrence_record o
		inner join data_provider dp on o.data_provider_id = dp.id
		inner join data_resource dr on o.data_resource_id = dr.id
        inner join taxon_concept tc on o.nub_concept_id = tc.id
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
	        left join taxon_name sn on sc.taxon_name_id = sn.id
where
	o.cell_id >= 22541 and 
	o.cell_id <= 29032 and
	o.mod360_cell_id >= mod(22541, 360) and 
	o.mod360_cell_id <= mod(29032, 360);





