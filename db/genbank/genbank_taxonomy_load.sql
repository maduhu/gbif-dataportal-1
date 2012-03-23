drop table gb_name;
drop table gb_node;

create table gb_name (
    id INT NOT NULL AUTO_INCREMENT, 
    gb_id INT NOT NULL, 
    name VARCHAR(150), 
    unique_name VARCHAR(150), 
    name_type VARCHAR(50), 
    primary key(id), 
    index(gb_id), 
    index(name), 
    index(name_type));
    
load data infile '/Users/dhobern/taxdump/names.dmp' 
    into table gb_name 
    fields terminated by '|' 
        optionally enclosed by '\t' 
    (gb_id, name, unique_name, name_type);
    
create table gb_node (
    id INT NOT NULL AUTO_INCREMENT,
    gb_id INT NOT NULL,
    parent_id INT,
    gb_rank VARCHAR(50),
    embl_code VARCHAR(50),
    division_id INT,
    inherited_div TINYINT(1),
    genetic_code_id INT,
    inherited_genetic_code TINYINT(1),
    mitochondrial_genetic_code_id INT,
    inherited_mitochondrial_genetic_code TINYINT(1),
    hidden TINYINT(1),
    hidden_subtree_root TINYINT(1),
    comments VARCHAR(255),
    primary key(id),
    index(gb_id),
    index(gb_rank));
    
load data infile '/Users/dhobern/taxdump/nodes.dmp' 
    into table gb_node 
    fields terminated by '|' 
        optionally enclosed by '\t' 
    (gb_id, parent_id, gb_rank, embl_code, division_id, inherited_div, genetic_code_id, inherited_genetic_code, mitochondrial_genetic_code_id, inherited_mitochondrial_genetic_code, hidden, hidden_subtree_root, comments);
    
(select 'Id', 'ParentId', 'Rank', 'ScientificName') 
   union (select gb_node.gb_id, parent_id, gb_rank, name into outfile '/tmp/genBankExtract.txt' from gb_node, gb_name where gb_node.gb_id=gb_name.gb_id and name_type="scientific name");

