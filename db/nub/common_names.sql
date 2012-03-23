/**
 * This associates the common_name with nub concepts (as opposed to COL concepts).
 */
insert ignore into common_name (taxon_concept_id, name, iso_language_code, language)
select 
    tc.partner_concept_id, cn.name, cn.iso_language_code, cn.language 
from 
    common_name cn 
        inner join taxon_concept tc on cn.taxon_concept_id=tc.id
where 
    tc.data_provider_id=2;
