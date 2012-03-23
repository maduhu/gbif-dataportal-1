/**
 * This associates the relationship_assertions with nub concepts (as opposed to COL concepts).
 *
 * This script requires the relationship_assertions to be populated for CoL concepts.
 * This script is currently falling foul of a problem with partner_concept_ids not being correctly
 * set on all non-nub concepts.
 */
insert ignore into relationship_assertion
select
from_concept.partner_concept_id,
to_concept.partner_concept_id,
ra.relationship_type
from
relationship_assertion ra
inner join taxon_concept from_concept on from_concept.id = ra.from_concept_id
inner join taxon_concept to_concept on to_concept.id = ra.to_concept_id
where 
from_concept.is_nub_concept = false and
to_concept.is_nub_concept = false and
from_concept.partner_concept_id is not null and
to_concept.partner_concept_id is not null;