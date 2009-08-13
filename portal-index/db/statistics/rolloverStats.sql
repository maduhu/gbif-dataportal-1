-- Number of registered occurrence data providers 
select count(id) as providerCount into outfile '/var/lib/mysql/stats/providerCount.txt' from data_provider where deleted is null and id>1;

-- Number of registered occurrence datasets 
select count(id) as occurrenceResourceCount into outfile '/var/lib/mysql/stats/occurrenceResourceCount.txt' from data_resource where (basis_of_record<=100 or basis_of_record>=200) and deleted is null and data_provider_id>1;

-- Number of indexed occurrence datasets (including partially indexed) 
select count(id) as indexedDatasetsCount into outfile '/var/lib/mysql/stats/indexedDatasetsCount.txt' from data_resource where occurrence_count>0 and deleted is null;

-- Number of indexed occurrence records 
select sum(occurrence_count) as indexedOccurrenceRecordCount into outfile '/var/lib/mysql/stats/indexedOccurrenceRecordCount.txt' from data_resource where deleted is null;

--Number of georeferenced indexed occurrence records 
select count(id) as indexedGeoreferencedOccurrenceRecordIncludingErroneousCount into outfile '/var/lib/mysql/stats/indexedGeoreferencedOccurrenceRecordIncludingErroneousCount.txt' from occurrence_record where latitude is not null and longitude is not null and deleted is null;

-- Number of georeferenced indexed occurrence records without detected geospatial issues (type 3 = provider)
select sum(count) as indexedGeoreferencedOccurrenceRecordNoErrorsCount into outfile '/var/lib/mysql/stats/indexedGeoreferencedOccurrenceRecordNoErrorsCount.txt' from cell_density where type=3;

-- Number of nomenclatural/taxonomic data providers (exclude GBIF) 
select count(distinct data_provider_id) as taxonomicDataProviderCount into outfile '/var/lib/mysql/stats/taxonomicDataProviderCount.txt' from data_resource where basis_of_record>100 and basis_of_record<200 and data_provider_id>1;

-- Number of nomenclatural/taxonomic datasets 
select count(id) as taxonomicDataResourceCount into outfile '/var/lib/mysql/stats/taxonomicDataResourceCount.txt' from data_resource where basis_of_record>100 and basis_of_record<200 and data_provider_id>1;

-- Number of taxon names supplied by nomenclatural/taxonomic datasets 
select sum(concept_count) as taxonomicDataProviderConceptsCount into outfile '/var/lib/mysql/stats/taxonomicDataProviderConceptsCount.txt' from data_resource where basis_of_record>100 and basis_of_record<200 and data_provider_id>1;

-- Number of taxon names with associated occurrence data 
select count(distinct taxon_name_id) as taxonNamesFromOccurrences into outfile '/var/lib/mysql/stats/taxonNamesFromOccurrences.txt' from occurrence_record;

-- List of provider names, counts, ids and resource names
select dp.id as providerId, dp.name as providerName, dr.id as resourceId, dr.name as resourceName, dr.occurrence_count as resourceOccurrences, dr.concept_count as resourceConceptCount
into outfile '/var/lib/mysql/stats/providerResourceBreakdown.txt' 
from data_provider dp inner join data_resource dr on dr.data_provider_id=dp.id
where dp.deleted is null and dr.deleted is null;
