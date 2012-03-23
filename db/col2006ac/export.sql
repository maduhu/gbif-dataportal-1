/**
 * A script to export the Catalogue of Life Annual Checklist 2006
 * to a set of text files, for use in importing to the GBIF Portal
 */
 
/**
 * Export the Denormalised Families
 */
select 
    trim(f.kingdom) as 'kingdom',
    trim(f.phylum) as 'phylum',
    trim(f.class) as 'class',
    trim(f.order) as 'order',
    trim(f.superfamily) as 'super_family',
    trim(f.family) as 'family',
    trim(d.database_name_displayed) as 'database_name'
from 
    families f 
    join col2006ac.databases d on d.database_name = f.database_name
into outfile 'c:/temp/col2006/families.txt';
    
/**
 *  Export the denormalised Taxonomy
 *  sp2000_status_id (for 2006) is:
 *   1) accepted name
 *   2) ambiguous synonym
 *   3) misapplied name
 *   4) provisionally applied name
 *   5) synonym
 */
select
    trim(sn.name_code) as 'name_code',
    trim(f.kingdom) as 'kingdom',
    trim(f.phylum) as 'phylum',
    trim(f.class) as 'class',
    trim(f.order) as 'order',
    trim(f.superfamily) as 'super_family',
    trim(f.family) as 'family',
    trim(sn.genus) as 'genus',
    trim(sn.species) as 'species',
    trim(sn.infraspecies) as 'infraspecies',
    trim(sn.infraspecies_marker) as 'infraspecies_marker',
    trim(sn.author) as 'author',    
    trim(t.name) as 'scientific_name',
    trim(t.taxon) as 'rank',
    trim(t.is_accepted_name) as 'is_accepted_name',
    trim(sn.accepted_name_code) as 'accepted_name_code',
    trim(t.sp2000_status_id) as'2006_status_id',
    trim(d.database_name_displayed) as 'database_name'
from 
    scientific_names sn
    join families f on f.record_id = sn.family_id
    join col2006ac.databases d on d.record_id = sn.database_id
    left join taxa t on t.name_code = sn.name_code
into outfile
    'c:/temp/col2006/taxonomy.txt';
    
/** 
 * Export the distribution
 */
select
    trim(d.name_code) as 'name_code',
    trim(d.distribution) as 'distribution'
from 
    distribution d
into outfile
    'c:/temp/col2006/distribution.txt';
    
/** 
 * Export the vernacular names
 */
select
    trim(cn.name_code) as 'name_code',
    trim(cn.common_name) as 'name',
    trim(cn.language) as 'language',
    trim(cn.country) as 'country'
from 
    common_names cn
into outfile
    'c:/temp/col2006/vernacular.txt';

/**
 * Export the publication
 */
select
    trim(snr.name_code) as 'name_code',
    trim(snr.reference_type) as 'reference_type',
    trim(r.author) as 'author',
    trim(r.year) as 'year',
    trim(r.title) as 'title',
    trim(r.source) as 'source'
from 
    scientific_name_references snr
    inner join col2006ac.references r on r.record_id = snr.reference_id
into outfile
    'c:/temp/col2006/publication.txt';