/* Ties the OR to the nub */
update occurrence_record o inner join taxon_concept t on o.taxon_concept_id = t.id set o.nub_concept_id = t.partner_concept_id;
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
    

/* Copies all common names to nub */
insert into common_name(taxon_concept_id, name, iso_language_code, language)
select
    tc.partner_concept_id,cn.name,cn.iso_language_code,cn.language
from
    common_name cn inner join taxon_concept tc on tc.id = cn.taxon_concept_id;
where (tc.is_nub_concept is false or tc.is_nub_concept is null);
