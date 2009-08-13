SET NAMES 'cp1251';

/**
 * Imports to temporary tables, the Catalogue of Life data
 */

/**
 * Import the families
 */
drop table col_families;
create table col_families (    
    kingdom varchar(255) not null,
    phylum varchar(255),
    class varchar(255),
    order_rank varchar(255),
    super_family varchar(255),
    family varchar(255),
    database_name varchar(255) not null,
    index index_kingdom(kingdom),
    index index_phylum(phylum),
    index index_class(class),
    index index_order(order_rank),
    index index_super_family(super_family),
    index index_family(family),
    index index_database(database_name)) TYPE=InnoDB;
load data 
    infile '/tmp/col2006/families.txt'
into table
    col_families;

/**
 * Import taxa
 *   1) accepted name (is_accepted_name = 1)
 *   2) ambiguous synonym (is_accepted_name = 0)
 *   3) misapplied name (is_accepted_name = 0)
 *   4) provisionally applied name (is_accepted_name = 1)
 *   5) synonym (is_accepted_name = 0)
 */
drop table col_taxa;
create table col_taxa (    
    name_code varchar(100) not null,
    kingdom varchar(255) not null,
    phylum varchar(255),
    class varchar(255),
    order_rank varchar(255),
    super_family varchar(255),
    family varchar(255),
    genus varchar(255),
    species varchar(255),
    infraspecies varchar(255),
    infraspecies_marker varchar(100),
    author varchar(255),
    scientific_name varchar(255),
    rank varchar(50),
    is_accepted_name int(1) not null,
    accepted_name_code varchar(100),
    2006_status_id int(1),
    database_name varchar(255) not null,
    primary key (name_code),
    index index_kingdom(kingdom),
    index index_phylum(phylum),
    index index_class(class),
    index index_order(order_rank),
    index index_super_family(super_family),
    index index_family(family),
    index index_genus(genus),
    index index_species(species),
    index index_infraspecies(infraspecies),
    index index_author(author),
    index index_database_name(database_name),
    index index_scientific_name(scientific_name),
    index index_rank(rank)) TYPE=InnoDB;
load data 
    infile '/tmp/col2006/taxonomy.txt'
into table
    col_taxa;

/**
 * Import distribution
 */
drop table col_distribution;
create table col_distribution (    
    name_code varchar(100) not null,
    distribution varchar(555) not null
) TYPE=InnoDB;
load data 
    infile '/tmp/col2006/distribution.txt'
into table
    col_distribution;
    
/**
 * Import the vernacular names
 */
drop table col_vernacular_name;
create table col_vernacular_name (    
    name_code varchar(100) not null,
    name varchar(255) not null,
    language varchar(255) not null,
    country varchar(255) not null
) TYPE=InnoDB;
load data 
    infile '/tmp/col2006/vernacular.txt'
into table
    col_vernacular_name;

/** 
 * Import the publications
 */
drop table col_publication;
create table col_publication (    
    name_code varchar(100) not null,
    reference_type varchar(100) not null,
    author varchar(255),
    year varchar(100),
    title varchar(555),
    source varchar(555)    
) TYPE=InnoDB;
load data 
    infile '/tmp/col2006/publication.txt'
into table
    col_publication;
    

/**
 * some sanity checking
 */
update col_families set super_family = null where length(super_family)=0;
update col_taxa set super_family = null where length(super_family)=0;
update col_taxa set infraspecies = null where length(infraspecies)=0;
update col_taxa set species = null where length(species)=0;
update col_taxa set genus = null where length(genus)=0;
update col_taxa set author = null where length(author)=0;
