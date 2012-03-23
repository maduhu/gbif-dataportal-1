/**
 *  Exports the Taxonomy into seperate files for ranks
 
 *  sp2000_status_id (for 2007) is:
 *   1) accepted name
 *   2) ambiguous synonym
 *   3) misapplied name
 *   4) provisionally applied name
 *   5) synonym
 */
select
    trim(t.record_id) as col_taxa_id,
    trim(t.name) as scientific_name,
    null as 'genus',
    null as 'species',
    null as 'infraspecies',
    null as 'infraspecies_marker',
    null as 'author',   
    1000 as rank, 
    trim(t.name_code) as name_code,
    t.parent_id as parent_id,
    t.sp2000_status_id as status_id,
    t.is_accepted_name as is_accepted,
    t.database_id as database_id,
    null as 'provider_link_url',
    null as accepted_name_code,
    null as 'specialist',    
    null as 'scrutiny_date'
into outfile 'c:/temp/col2007/kingdoms.txt'
from
    taxa t
where taxon='Kingdom' order by t.sp2000_status_id;

select
    trim(t.record_id) as col_taxa_id,
    trim(t.name) as scientific_name,
    null as 'genus',
    null as 'species',
    null as 'infraspecies',
    null as 'infraspecies_marker',
    null as 'author',    
    2000 as rank,
    trim(t.name_code) as name_code,
    t.parent_id as parent_id,
    t.sp2000_status_id as status_id,
    t.is_accepted_name as is_accepted,
    t.database_id as database_id,
    null as 'provider_link_url',
    null as accepted_name_code,
    null as 'specialist',    
    null as 'scrutiny_date'
into outfile 'c:/temp/col2007/phyla.txt'   
from
    taxa t
where taxon='Phylum' order by t.sp2000_status_id;

select
    trim(t.record_id) as col_taxa_id,
    trim(t.name) as scientific_name,
    null as 'genus',
    null as 'species',
    null as 'infraspecies',
    null as 'infraspecies_marker',
    null as 'author',   
    3000 as rank, 
    trim(t.name_code) as name_code,
    t.parent_id as parent_id,
    t.sp2000_status_id as status_id,
    t.is_accepted_name as is_accepted,
    t.database_id as database_id,
    null as 'provider_link_url',
    null as accepted_name_code,
    null as 'specialist',    
    null as 'scrutiny_date'
into outfile 'c:/temp/col2007/classes.txt'    
from
    taxa t
where taxon='Class' order by t.sp2000_status_id;

select
    trim(t.record_id) as col_taxa_id,
    trim(t.name) as scientific_name,
    null as 'genus',
    null as 'species',
    null as 'infraspecies',
    null as 'infraspecies_marker',
    null as 'author',   
    4000 as rank, 
    trim(t.name_code) as name_code,
    t.parent_id as parent_id,
    t.sp2000_status_id as status_id,
    t.is_accepted_name as is_accepted,
    t.database_id as database_id,
    null as 'provider_link_url',
    null as accepted_name_code,
    null as 'specialist',    
    null as 'scrutiny_date'
into outfile 'c:/temp/col2007/orders.txt'   
from
    taxa t
where taxon='Order' order by t.sp2000_status_id;

select
    trim(t.record_id) as col_taxa_id,
    trim(t.name) as scientific_name,
    null as 'genus',
    null as 'species',
    null as 'infraspecies',
    null as 'infraspecies_marker',
    null as 'author',  
    4500 as rank,  
    trim(t.name_code) as name_code,
    t.parent_id as parent_id,
    t.sp2000_status_id as status_id,
    t.is_accepted_name as is_accepted,
    t.database_id as database_id,
    null as 'provider_link_url',
    null as accepted_name_code,
    null as 'specialist',    
    null as 'scrutiny_date'
into outfile 'c:/temp/col2007/superfamilies.txt' 
from
    taxa t
where taxon='Superfamily' order by t.sp2000_status_id;

select
    trim(t.record_id) as col_taxa_id,
    trim(t.name) as scientific_name,
    null as 'genus',
    null as 'species',
    null as 'infraspecies',
    null as 'infraspecies_marker',
    null as 'author',    
    5000 as rank,
    trim(t.name_code) as name_code,
    t.parent_id as parent_id,
    t.sp2000_status_id as status_id,
    t.is_accepted_name as is_accepted,
    t.database_id as database_id,
    null as 'provider_link_url',
    null as accepted_name_code,
    null as 'specialist',    
    null as 'scrutiny_date'
into outfile 'c:/temp/col2007/families.txt'
from
    taxa t
where taxon='Family' order by t.sp2000_status_id;

select
    trim(t.record_id) as col_taxa_id,
    trim(t.name) as scientific_name,
    trim(t.name) as 'genus',
    null as 'species',
    null as 'infraspecies',
    null as 'infraspecies_marker',
    null as 'author',    
    6000 as rank,
    trim(t.name_code) as name_code,
    t.parent_id as parent_id,
    t.sp2000_status_id as status_id,
    t.is_accepted_name as is_accepted,
    t.database_id as database_id,
    null as 'provider_link_url',
    null as accepted_name_code,
    null as 'specialist',    
    null as 'scrutiny_date'
into outfile 'c:/temp/col2007/genera.txt'    
from
    taxa t
where taxon='Genus' order by t.sp2000_status_id;

select
    trim(t.record_id) as col_taxa_id,
    replace(replace(trim(t.name), '\r\n', ' '), '\n', ' ') as scientific_name,
    trim(sn.genus) as 'genus',
    replace(replace(trim(sn.species), '\r\n', ' '), '\n', ' ') as 'species',
    null as 'infraspecies',
    null as 'infraspecies_marker',
    replace(replace(trim(sn.author), '\r\n', ' / '), '\n', '/') as 'author',    
    7000 as rank,
    trim(t.name_code) as name_code,
    t.parent_id as parent_id,
    t.sp2000_status_id as status_id,
    t.is_accepted_name as is_accepted,
    t.database_id as database_id,
    replace(trim(sn.web_site),'#','') as 'provider_link_url',
    trim(sn.accepted_name_code) as accepted_name_code,
    replace(replace(trim(sp.specialist_name), '\r\n', ' / '), '\n', '/') as 'specialist',    
    replace(replace(trim(sn.scrutiny_date), '\r\n', ' / '), '\n', '/') as 'scrutiny_date'
into outfile 'c:/temp/col2007/species.txt'       
from
    taxa t
        left join scientific_names sn on sn.name_code = t.name_code
        left join specialists sp on sn.specialist_id = sp.record_id
        left join families f on f.record_id = sn.family_id
where taxon='Species';

select
    trim(t.record_id) as col_taxa_id,
    replace(replace(trim(t.name), '\r\n', ' '), '\n', ' ') as scientific_name,
    trim(sn.genus) as 'genus',
    replace(replace(trim(sn.species), '\r\n', ' '), '\n', ' ') as 'species',
    trim(sn.infraspecies) as 'infraspecies',
    trim(sn.infraspecies_marker) as 'infraspecies_marker',
    replace(replace(trim(sn.author), '\r\n', ' / '), '\n', '/') as 'author',    
    8000 as rank,
    trim(t.name_code) as name_code,
    t.parent_id as parent_id,
    t.sp2000_status_id as status_id,
    t.is_accepted_name as is_accepted,
    t.database_id as database_id,
    replace(trim(sn.web_site),'#','') as 'provider_link_url',
    trim(sn.accepted_name_code) as accepted_name_code,
    replace(replace(trim(sp.specialist_name), '\r\n', ' / '), '\n', '/') as 'specialist',    
    replace(replace(trim(sn.scrutiny_date), '\r\n', ' / '), '\n', '/') as 'scrutiny_date'
into outfile
    'c:/temp/col2007/infraspecies.txt'    
from
    taxa t
        inner join taxa p on p.record_id = t.parent_id
        left join scientific_names sn on sn.name_code = t.name_code
        left join families f on f.record_id = sn.family_id
        left join specialists sp on sn.specialist_id = sp.record_id
where t.taxon='Infraspecies';

/**
 * Get the databases table for the DataResource
 */
select
    record_id,
    database_full_name,
    web_site,
    organization,
    contact_person,
    abstract,
    authors_editors
into outfile
    'c:/temp/col2007/databases-TO_MODIFY.txt'
from 
    col2007ac.databases;
    

/**
 * Get the common names
 */
select
    name_code,
    common_name,
    language,
    country
into outfile
    'c:/temp/col2007/commonNames.txt'
from 
    col2007ac.common_names;
    
