SET NAMES 'utf8';

/**
 * This is a MYSQL5 script that will build the Taxonomy for the IPNI DB
 * into the GBIF Portal
 *
 * Note - this script should really lock the entire DB before running but does not
 * It makes use of system variables!!!
 * @author Tim Robertson
 */
 
/**
 * Checks to see if the data provider already EXISTS, AND if not creates it
 */
DROP procedure if EXISTS createOrRetreiveDataProvider;
delimiter |
create procedure createOrRetreiveDataProvider(IN _data_provider_name CHAR(255), INOUT _created_id int)
begin    
    declare continue handler for not found SET _created_id = null;
    
    SELECT id INTO _created_id
    FROM data_provider WHERE name = _data_provider_name;
    
    if _created_id is null then 
        INSERT INTO data_provider(name) values(_data_provider_name);
        SELECT max(id) INTO _created_id FROM data_provider;        
    end if;
  end;
|
delimiter ;
 
/**
 * Checks to see if the data resource already EXISTS, AND if not creates it
 */
DROP procedure if EXISTS createOrRetreiveDataResource;
delimiter |
create procedure createOrRetreiveDataResource(IN _data_resource_name CHAR(255), INOUT _created_id int)
begin    
    declare continue handler for not found SET _created_id = null;
    
    SELECT id INTO _created_id 
    FROM data_resource WHERE name = _data_resource_name
    AND data_provider_id = @_data_provider_id;
    
    if _created_id is null then 
        INSERT INTO data_resource(name, data_provider_id) values(_data_resource_name, @_data_provider_id);
        SELECT max(id) INTO _created_id FROM data_resource;        
    end if;
  end;
|
delimiter ;

/**
 * Ensures that there EXISTS a record in the taxon_name table for ALL the names found in the ipni table
 */
DROP procedure if EXISTS synchroniseTaxonNames;
delimiter |
create procedure synchroniseTaxonNames()
begin
    INSERT INTO taxon_name(canonical, author) 
        SELECT family, null FROM ipni left outer JOIN taxon_name on canonical = family WHERE family is NOT NULL AND canonical is null 
        union distinct
        SELECT i.name, i.author FROM ipni i left outer JOIN taxon_name tn on tn.canonical = i.name and i.author = tn.author WHERE i.name is NOT NULL AND canonical is null;
end;
|
delimiter ;

/**
 * Checks to see if the concept already EXISTS, AND if not creates it
 */
DROP procedure if EXISTS createOrRetreiveTaxonConcept;
delimiter |
create procedure createOrRetreiveTaxonConcept(IN _taxon_name_id INT, IN _parent_concept_id INT, IN _rank INT, IN _name_code CHAR(50), INOUT _created_id int)
begin    
    declare _existing_id INT DEFAULT null;
    declare continue handler for not found SET _existing_id = null;
    -- select concat('Starting: ', _taxon_name_id, ' ', _rank, ' ', _name_code);
    if _parent_concept_id is NOT NULL then
        SELECT child.id INTO _existing_id 
        FROM taxon_concept child inner JOIN taxon_concept parent on parent.id = child.parent_concept_id
        WHERE parent.id = _parent_concept_id AND child.taxon_name_id = _taxon_name_id AND child.taxon_rank = _rank
        AND parent.data_provider_id = @_data_provider_id;
    else 
        SELECT id INTO _existing_id 
        FROM taxon_concept 
        WHERE taxon_name_id = _taxon_name_id AND taxon_rank = _rank and data_provider_id = @_data_provider_id;
    end if;

    if _existing_id is null then 
        INSERT INTO taxon_concept(taxon_name_id, parent_concept_id, taxon_rank, data_provider_id, data_resource_id) VALUES (_taxon_name_id, _parent_concept_id, _rank, @_data_provider_id, @_data_resource_id);
        SELECT max(id) INTO _created_id FROM taxon_concept;
        
        if _name_code is not null then
            INSERT INTO remote_concept(taxon_concept_id, remote_id) values(_created_id, _name_code);
        end if;
        
        /* now wire up the denormalised version */
        case _rank
            when 1000 then 
                UPDATE taxon_concept SET kingdom_concept_id = _created_id WHERE id = _created_id;
                
            when 2000 then 
                if _parent_concept_id is NOT NULL then
                    UPDATE taxon_concept child, taxon_concept parent 
                    SET 
                        child.kingdom_concept_id = parent.kingdom_concept_id,
                        child.phylum_concept_id = _created_id
                    WHERE child.id = _created_id AND parent.id = child.parent_concept_id;        
                else 
                    UPDATE taxon_concept SET phylum_concept_id = _created_id WHERE id = _created_id;
                end if;                
                
            when 3000 then 
                if _parent_concept_id is NOT NULL then
                    UPDATE taxon_concept child, taxon_concept parent 
                    SET 
                        child.kingdom_concept_id = parent.kingdom_concept_id,
                        child.phylum_concept_id = parent.phylum_concept_id,
                        child.class_concept_id = _created_id
                    WHERE child.id = _created_id AND parent.id = child.parent_concept_id;        
                else 
                    UPDATE taxon_concept SET class_concept_id = _created_id WHERE id = _created_id;
                end if;
                
            when 4000 then 
                if _parent_concept_id is NOT NULL then
                    UPDATE taxon_concept child, taxon_concept parent 
                    SET 
                        child.kingdom_concept_id = parent.kingdom_concept_id,
                        child.phylum_concept_id = parent.phylum_concept_id,
                        child.class_concept_id = parent.class_concept_id,
                        child.order_concept_id = _created_id
                    WHERE child.id = _created_id AND parent.id = child.parent_concept_id;        
                else 
                    UPDATE taxon_concept SET order_concept_id = _created_id WHERE id = _created_id;
                end if;

            when 4500 then 
                if _parent_concept_id is NOT NULL then
                    UPDATE taxon_concept child, taxon_concept parent 
                    SET 
                        child.kingdom_concept_id = parent.kingdom_concept_id,
                        child.phylum_concept_id = parent.phylum_concept_id,
                        child.class_concept_id = parent.class_concept_id,
                        child.order_concept_id = _created_id
                    WHERE child.id = _created_id AND parent.id = child.parent_concept_id;        
                end if;
                
            when 5000 then 
                if _parent_concept_id is NOT NULL then
                    UPDATE taxon_concept child, taxon_concept parent 
                    SET 
                        child.kingdom_concept_id = parent.kingdom_concept_id,
                        child.phylum_concept_id = parent.phylum_concept_id,
                        child.class_concept_id = parent.class_concept_id,
                        child.order_concept_id = parent.order_concept_id,
                        child.family_concept_id = _created_id
                    WHERE child.id = _created_id AND parent.id = child.parent_concept_id;        
                else 
                    UPDATE taxon_concept SET family_concept_id = _created_id WHERE id = _created_id;
                end if;                
                
            when 6000 then 
                if _parent_concept_id is NOT NULL then
                    UPDATE taxon_concept child, taxon_concept parent 
                    SET 
                        child.kingdom_concept_id = parent.kingdom_concept_id,
                        child.phylum_concept_id = parent.phylum_concept_id,
                        child.class_concept_id = parent.class_concept_id,
                        child.order_concept_id = parent.order_concept_id,
                        child.family_concept_id = parent.family_concept_id,
                        child.genus_concept_id = _created_id
                    WHERE child.id = _created_id AND parent.id = child.parent_concept_id;        
                else 
                    UPDATE taxon_concept SET genus_concept_id = _created_id WHERE id = _created_id;
                end if;                
                
            when 7000 then 
                if _parent_concept_id is NOT NULL then
                    UPDATE taxon_concept child, taxon_concept parent 
                    SET 
                        child.kingdom_concept_id = parent.kingdom_concept_id,
                        child.phylum_concept_id = parent.phylum_concept_id,
                        child.class_concept_id = parent.class_concept_id,
                        child.order_concept_id = parent.order_concept_id,
                        child.family_concept_id = parent.family_concept_id,
                        child.genus_concept_id = parent.genus_concept_id,
                        child.species_concept_id = _created_id
                    WHERE child.id = _created_id AND parent.id = child.parent_concept_id;        
                else 
                    UPDATE taxon_concept SET species_concept_id = _created_id WHERE id = _created_id;
                end if;                
                
            when 8000 then 
                if _parent_concept_id is NOT NULL then
                    UPDATE taxon_concept child, taxon_concept parent 
                    SET 
                        child.kingdom_concept_id = parent.kingdom_concept_id,
                        child.phylum_concept_id = parent.phylum_concept_id,
                        child.class_concept_id = parent.class_concept_id,
                        child.order_concept_id = parent.order_concept_id,
                        child.family_concept_id = parent.family_concept_id,
                        child.genus_concept_id = parent.genus_concept_id,
                        child.species_concept_id = parent.species_concept_id
                    WHERE child.id = _created_id AND parent.id = child.parent_concept_id;        
                end if;                
                
            /* mysql requires an else condition */
            else SET _created_id = _created_id;
        end case;    
    else
        -- select concat('Concept already EXISTS with ID [', _existing_id,']');
        SET _created_id = _existing_id;
    end if;    
end;
|
delimiter ;

/**
 * Builds FROM the family down for the VALUES provided
 */
DROP procedure if EXISTS buildFamilies;
delimiter |
create procedure buildFamilies(IN _parent_id INT)
begin
    declare _done BOOLEAN DEFAULT false;
    declare _name CHAR(255);
    declare _name_id int;
    declare _concept_id int;
    declare _cur1 cursor for SELECT distinct family FROM ipni;
    declare continue handler for not found SET _done = true;
open _cur1;
    _loop: loop
        fetch _cur1 INTO _name;
        if _done then 
            close _cur1;
            leave _loop;
        end if;
        -- select concat('Starting family: ', _name)  as debug;
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and author is null;
        call createOrRetreiveTaxonConcept(_name_id, _parent_id, 5000, null, _concept_id);
        -- call buildGenera(_concept_id, _name);
        -- select concat('Finished family: ', _name)  as debug;
    end loop _loop;
end;
|
delimiter ;

/**
 * Builds FROM the genera down for the VALUES provided
 */
DROP procedure if EXISTS buildGenera;
delimiter |
create procedure buildGenera(IN _parent_id INT, IN _family_name CHAR(255))
begin
    declare _done BOOLEAN DEFAULT false;
    declare _name CHAR(255);
    declare _name_id int;
    declare _concept_id int;
    declare _cur1 cursor for SELECT name, author FROM ipni WHERE family = _family_name group by name, author;
    declare continue handler for not found SET _done = true;
    open _cur1;
    _loop: loop
        fetch _cur1 INTO _name;
        
        if _done then 
            if _super_family_name is NOT NULL then
                close _cur1;
            else 
                close _cur2;
            end if;
            leave _loop;
        end if;
        -- select concat('Starting genera: ', _name)  as debug;
        call createOrRetreiveDataResource(_database_name, @_data_resource_id);
        -- there is bad data in COL that means some genera are incorrect and have a "Aus bus" instead of "Aus"
        -- these have no author though
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and author is null;
        call createOrRetreiveTaxonConcept(_name_id, _parent_id, 6000, null, _concept_id);
        call buildSpecies(_concept_id, _kingdom_name, _phylum_name, _class_name, _order_name, _super_family_name, _family_name, _name);
        -- select concat('Finished genera: ', _name) as debug;
    end loop _loop;
end;
|
delimiter ;


/**
 * Builds FROM the species down for the VALUES provided
 */
DROP procedure if EXISTS buildSpecies;
delimiter |
create procedure buildSpecies(IN _parent_id INT, IN _kingdom_name CHAR(255), IN _phylum_name CHAR(255), IN _class_name CHAR(255), IN _order_name CHAR(255), IN _super_family_name CHAR(255), IN _family_name CHAR(255), IN _genus_name CHAR(255))
begin
    declare _done BOOLEAN DEFAULT false;
    declare _name CHAR(255);
    declare _species CHAR(255);
    declare _author CHAR(255);
    declare _name_id int;
    declare _database_name CHAR(255);
    declare _name_code CHAR(255);
    declare _concept_id int;
    declare _cur1 cursor for SELECT scientific_name, author, species, database_name, name_code FROM col_taxa col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class = _class_name AND order_rank = _order_name AND super_family = _super_family_name AND family = _family_name AND genus = _genus_name AND species is NOT NULL AND rank = 'Species' group by 1,2,3,4;
    declare _cur2 cursor for SELECT scientific_name, author, species, database_name, name_code FROM col_taxa col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class = _class_name AND order_rank = _order_name AND super_family is null AND family = _family_name AND genus = _genus_name AND species is NOT NULL AND rank = 'Species' group by 1,2,3,4;
    declare continue handler for not found SET _done = true;
    if _super_family_name is NOT NULL then
        open _cur1;
    else 
        open _cur2;
    end if;
    _loop: loop
        if _super_family_name is NOT NULL then
            fetch _cur1 INTO _name, _author, _species, _database_name,_name_code;
        else 
            fetch _cur2 INTO _name, _author, _species, _database_name,_name_code;
        end if;
        
        if _done then 
            if _super_family_name is NOT NULL then
                close _cur1;
            else 
                close _cur2;
            end if;
            leave _loop;
        end if;
        -- select concat('Starting species: ', _name)  as debug;
        call createOrRetreiveDataResource(_database_name, @_data_resource_id);
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and author = _author;
        call createOrRetreiveTaxonConcept(_name_id, _parent_id, 7000, _name_code, _concept_id);
        call buildInfraspecies(_concept_id, _kingdom_name, _phylum_name, _class_name, _order_name, _super_family_name, _family_name, _genus_name, _species);
        -- select concat('Finished species: ', _name)  as debug;
    end loop _loop;
end;
|
delimiter ;


/**
 * Builds FROM the infra species down for the VALUES provided
 */
DROP procedure if EXISTS buildInfraspecies;
delimiter |
create procedure buildInfraspecies(IN _parent_id INT, IN _kingdom_name CHAR(255), IN _phylum_name CHAR(255), IN _class_name CHAR(255), IN _order_name CHAR(255), IN _super_family_name CHAR(255), IN _family_name CHAR(255), IN _genus_name CHAR(255), IN _species_name CHAR(255))
begin
    declare _done BOOLEAN DEFAULT false;
    declare _name CHAR(255);
    declare _name_id int;
    declare _author CHAR(255);
    declare _concept_id int;
    declare _database_name CHAR(255);
    declare _name_code CHAR(255);
    declare _cur1 cursor for SELECT scientific_name, author, database_name, name_code FROM col_taxa col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class = _class_name AND order_rank = _order_name AND super_family = _super_family_name AND family = _family_name AND genus = _genus_name AND species = _species_name AND infraspecies is not null  group by 1,2,3,4;
    declare _cur2 cursor for SELECT scientific_name, author, database_name, name_code FROM col_taxa col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class = _class_name AND order_rank = _order_name AND super_family is null AND family = _family_name AND genus = _genus_name AND species = _species_name AND infraspecies is not null  group by 1,2,3,4;
    declare continue handler for not found SET _done = true;
    if _super_family_name is NOT NULL then
        open _cur1;
    else 
        open _cur2;
    end if;
    _loop: loop
        if _super_family_name is NOT NULL then
            fetch _cur1 INTO _name, _author, _database_name, _name_code;
        else 
            fetch _cur2 INTO _name, _author, _database_name, _name_code;
        end if; 
        
        if _done then 
            if _super_family_name is NOT NULL then
                close _cur1;
            else 
                close _cur2;
            end if;
            leave _loop;
        end if;
        -- select concat('Starting infraspecies: ', _name)  as debug;
        call createOrRetreiveDataResource(_database_name, @_data_resource_id);
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and author = _author;
        call createOrRetreiveTaxonConcept(_name_id, _parent_id, 8000, _name_code, _concept_id);
        -- select concat('Finished infraspecies: ', _name)  as debug;
    end loop _loop;
end;
|
delimiter ;

/**
 * Build's the lot!
 */
set @_data_provider_name = 'The International Plant Names Index';
set @_default_resource_name = 'The International Plant Names Index';
call createOrRetreiveDataProvider(@_data_provider_name, @_data_provider_id);
call createOrRetreiveDataResource(@_data_provider_name, @_data_resource_id);
call synchroniseTaxonNames(); 
call buildFamilys(null);

/**
 * Nice little script...
SELECT taxon_concept.id AS id,
    taxon_rank AS rank,
    parent_concept_id AS p_id,  
    kingdom_concept_id AS k_id,
    family_concept_id AS fa_id,
    genus_concept_id AS ge_id,
    species_concept_id AS sp_id,
    taxon_name_id AS tn_id,
    canonical AS name
FROM taxon_concept inner JOIN taxon_name on taxon_name.id = taxon_name_id 
order by taxon_concept.id desc
limit 10;
 */
 
/**
 * Tidy up...
 */
DROP procedure if EXISTS synchroniseTaxonNames;
DROP procedure if EXISTS createOrRetreiveDataProvider;
DROP procedure if EXISTS createOrRetreiveTaxonConcept;
DROP procedure if EXISTS buildFamilies;
DROP procedure if EXISTS buildGenera;
DROP procedure if EXISTS buildSpecies;
DROP procedure if EXISTS buildInfraspecies;
