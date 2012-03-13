update taxon_concept tc inner join relationship_assertion ra ON ra.from_concept_id=tc.id
SET is_accepted=false;

update taxon_concept tc set tc.is_accepted=true where tc.is_accepted is NULL;