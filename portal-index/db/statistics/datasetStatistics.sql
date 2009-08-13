--
-- Creates the Data Resource and Provider Statistics
-- This functionality should eventually be moved into the index workflow.
--


--Data Resource Statistics

--set occurrence record count
update data_resource dr set occurrence_count =   
(select 
    count(o.id)
from 
    occurrence_record o
where 
    o.data_resource_id=dr.id);

--set occurrence record coordinate count
update data_resource dr set occurrence_coordinate_count =   
(select 
    count(o.id)
from 
    occurrence_record o
where 
    o.data_resource_id=dr.id and
    o.cell_id is not null);

--set occurrence record clean geospatial count
update data_resource dr set occurrence_clean_geospatial_count =   
(select 
    count(o.id)
from 
    occurrence_record o
where 
    o.data_resource_id=dr.id and
    o.cell_id is not null and 
    o.geospatial_issue=0);

--set species count
update data_resource dr set species_count =       
( select count(tc.id) from taxon_concept tc
where    
    tc.data_resource_id = dr.id and
    tc.rank>=7000 and tc.rank<8000);

--set higher concept count
/*
update data_resource dr set species_count =       
( select count(tc.id) from taxon_concept tc
where    
    tc.data_resource_id = dr.id and
    tc.rank<7000);
*/
    
-- set concept count    
update data_resource dr set concept_count =       
( select count(tc.id) from taxon_concept tc
where    
    tc.data_resource_id = dr.id);
    
    
-- Data Provider Statistics

--set occurrence record count
update data_provider dp set occurrence_count =   
(select 
    count(o.id)
from 
    occurrence_record o
where 
    o.data_provider_id=dp.id);

--set occurrence record coordinate count
update data_provider dp set occurrence_coordinate_count =   
(select 
    sum(cd.count)
from 
    cell_density cd
where 
    cd.concept_id=dp.id and
    cd.type=3);

--set species count
/*
update data_provider dp set species_count =       
( select count(tc.id) from taxon_concept tc
where    
    tc.data_provider_id=dp.id and
    tc.rank=7000);
*/
--set species count
/*
update data_provider dp set higher_concept_count =       
( select count(tc.id) from taxon_concept tc
where    
    tc.data_provider_id=dp.id and
    tc.rank<7000);
*/  
-- set concept count    
update data_provider dp set concept_count =       
( select count(tc.id) from taxon_concept tc
where    
    tc.data_provider_id=dp.id);    
    
-- data resource count    
update data_provider dp set data_resource_count = 
( select count(dr.id) from data_resource dr
where 
dr.data_provider_id=dp.id);     
    
-- Resource Network Statistics
    
update resource_network rn set occurrence_count =   
(select 
    sum(dr.occurrence_count)
from 
    data_resource dr inner join network_membership nm on dr.id = nm.data_resource_id
where 
    nm.resource_network_id=rn.id);    
    
update resource_network rn set occurrence_coordinate_count =   
(select 
    sum(dr.occurrence_coordinate_count)
from 
    data_resource dr inner join network_membership nm on dr.id = nm.data_resource_id
where 
    nm.resource_network_id=rn.id);       
    
    
