--
-- Populates the Country Statistics
-- This functionality should eventually be moved into the index workflow.
--
-- NOTE: Please run portal-index/db/countries/import.sql before running this statistics script

--initial insert, setting occurrence record  count
update country c set occurrence_count =
(select 
    count(id) 
from 
    occurrence_record o
where 
    o.iso_country_code=c.iso_country_code);
    
--set occurrence record coordinate count
update country c set occurrence_coordinate_count =   
(select 
    sum(cd.count)
from 
    cell_density cd
where 
    cd.entity_id=c.id and
    cd.type=2);
    
--this will be species and lower concepts as well - needs to be changed
update country c set species_count =       
( select count(distinct o.nub_concept_id) from occurrence_record o
where    
    o.iso_country_code = c.iso_country_code);
