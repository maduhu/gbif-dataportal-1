SET NAMES 'cp1251';

/**
 * This is a MYSQL5 script that will build the Taxonomy for the Catalogue of Life
 * annual checklist 2006 into the GBIF Portal
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
 * Ensures that there EXISTS a record in the taxon_name table for ALL the names found in the col_families table
 */
DROP procedure if EXISTS synchroniseTaxonNamesForHigherTaxa;
delimiter |
create procedure synchroniseTaxonNamesForHigherTaxa()
begin
    INSERT INTO taxon_name(canonical, supra_generic, author, rank) 
        SELECT kingdom, kingdom, null, 1000 
        FROM col_families cf left outer JOIN taxon_name tn on canonical = kingdom and tn.rank = 1000
        WHERE kingdom is NOT NULL AND canonical is null group by 1;
    
    INSERT INTO taxon_name(canonical, supra_generic, author, rank) 
        SELECT phylum, phylum, null, 2000
        FROM col_families cf left outer JOIN taxon_name tn on canonical = phylum and tn.rank = 2000
        WHERE phylum is NOT NULL AND canonical is null group by 1;
        
    INSERT INTO taxon_name(canonical, supra_generic, author, rank) 
        SELECT class, class, null, 3000
        FROM col_families cf left outer JOIN taxon_name tn on canonical = class and tn.rank = 3000
        WHERE class is NOT NULL AND canonical is null group by 1;
    
    INSERT INTO taxon_name(canonical, supra_generic, author, rank) 
        SELECT order_rank, order_rank, null, 4000
        FROM col_families cf left outer JOIN taxon_name tn on canonical = order_rank and tn.rank = 4000
        WHERE order_rank is NOT NULL AND canonical is null group by 1;
    
    INSERT INTO taxon_name(canonical, supra_generic, author, rank) 
        SELECT super_family, super_family, null, 4500
        FROM col_families cf left outer JOIN taxon_name tn on canonical = super_family and tn.rank = 4500
        WHERE super_family is NOT NULL AND canonical is null group by 1;
    
    INSERT INTO taxon_name(canonical, supra_generic, author, rank) 
        SELECT family, family, null, 5000
        FROM col_families cf left outer JOIN taxon_name tn on canonical = family and tn.rank = 5000
        WHERE family is NOT NULL AND canonical is null group by 1;
end;
|
delimiter ;

/**
 * Ensures that there EXISTS a record in the taxon_name table for ALL the genus and lower names found in the col_taxa table
 */
DROP procedure if EXISTS synchroniseTaxonNames;
delimiter |
create procedure synchroniseTaxonNames()
begin
    INSERT INTO taxon_name(canonical, generic, author, rank) 
        SELECT genus, genus, null, 6000 
        FROM col_taxa ct left outer JOIN taxon_name tn on canonical = genus and tn.rank = 6000
        WHERE genus is NOT NULL AND canonical is null 
        GROUP by genus;
        
    INSERT INTO taxon_name(canonical, generic, specific_epithet, author, rank)         
        SELECT scientific_name, genus, species, ct.author, 7000 
        FROM col_taxa ct left outer JOIN taxon_name tn on canonical = scientific_name and tn.rank = 7000 and (tn.author = ct.author or tn.author is null and ct.author is null)
        WHERE species is NOT NULL AND ct.rank='Species' AND canonical is null
        GROUP BY scientific_name, author;
        
    INSERT INTO taxon_name(canonical, generic, specific_epithet, infraspecific, infraspecific_marker, author, rank)         
        SELECT scientific_name, genus, species, infraspecies, infraspecies_marker, ct.author, 8000 
        FROM col_taxa ct left outer JOIN taxon_name tn on canonical = scientific_name and tn.rank = 8000 and (tn.author = ct.author or tn.author is null and ct.author is null)
        WHERE infraspecies is NOT NULL AND ct.rank='Infraspecies' AND canonical is null 
        GROUP BY scientific_name, author;
end;
|
delimiter ;

/**
 * Checks to see if the concept already EXISTS, AND if not creates it
 */
DROP procedure if EXISTS createOrRetreiveTaxonConcept;
delimiter |
create procedure createOrRetreiveTaxonConcept(IN _taxon_name_id INT, IN _parent_concept_id INT, IN _rank INT, IN _name_code CHAR(50), IN _accepted BOOL, INOUT _created_id int)
begin    
    declare _existing_id INT DEFAULT null;
    declare continue handler for not found SET _existing_id = null;
    -- select concat('Starting: ', _taxon_name_id, ' ', _rank, ' ', _name_code);
    
    -- this is fatal, so log it for debugging
    if _taxon_name_id is null then
        select concat('Attempting to create concept when TAXON_NAME ID is null - name_code: ', _name_code);
    end if;
    
    if _parent_concept_id is NOT NULL then
        if _rank < 7000 then 
            SELECT child.id INTO _existing_id 
            FROM taxon_concept child inner JOIN taxon_concept parent on parent.id = child.parent_concept_id
            WHERE parent.id = _parent_concept_id AND child.taxon_name_id = _taxon_name_id AND child.rank = _rank
            AND parent.data_provider_id = @_data_provider_id;
         else
            SELECT child.id INTO _existing_id 
            FROM taxon_concept child inner JOIN taxon_concept parent on parent.id = child.parent_concept_id inner join remote_concept rc on rc.taxon_concept_id = child.id
            WHERE parent.id = _parent_concept_id AND child.taxon_name_id = _taxon_name_id AND child.rank = _rank AND rc.remote_id = _name_code
            AND parent.data_provider_id = @_data_provider_id;
         end if;
    else 
        SELECT id INTO _existing_id 
        FROM taxon_concept 
        WHERE taxon_name_id = _taxon_name_id AND rank = _rank and data_provider_id = @_data_provider_id;
    end if;

    if _existing_id is null then 
        INSERT INTO taxon_concept(taxon_name_id, parent_concept_id, rank, data_provider_id, data_resource_id, is_accepted) VALUES (_taxon_name_id, _parent_concept_id, _rank, @_data_provider_id, @_data_resource_id, _accepted);
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
                        child.order_concept_id = parent.order_concept_id
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
 * Encodes the string to the rank value
 */
DROP procedure if EXISTS encodeRankString;
delimiter |
create procedure encodeRankString(IN _rankString CHAR(50), INOUT _rank int)
begin
    case trim(upper(replace(_rankString, '_', '')))
        when 'KINGDOM' then SET _rank = 1000;
        when 'PHYLUM' then SET _rank = 2000;
        when 'CLASS' then SET _rank = 3000;
        when 'ORDER' then SET _rank = 4000;
        when 'SUPERFAMILY' then SET _rank = 4500;
        when 'FAMILY' then SET _rank = 5000;
        when 'GENUS' then SET _rank = 6000;
        when 'SPECIES' then SET _rank = 7000;
        when 'INFRASPECIES' then SET _rank = 8000;
        else SET _rank = 0;
    end case;
end;
|
delimiter ;


/**
 * Builds FROM the kingdom down
 */
DROP procedure if EXISTS buildKingdoms;
delimiter |
create procedure buildKingdoms()
begin
    declare _done BOOLEAN DEFAULT false;
    declare _name CHAR(255);
    declare _database_name CHAR(255);
    declare _count int;
    declare _name_id int;
    declare _concept_id int;
    declare _cur cursor for SELECT kingdom, database_name, count(distinct database_name) FROM col_families col WHERE kingdom is not null group by kingdom;
    declare continue handler for not found SET _done = true;
    open _cur;
    _loop: loop
        fetch _cur INTO _name, _database_name, _count;
        if _done then 
            close _cur;
            leave _loop;
        end if;
        -- select concat('Starting kingdom: ', _name) as debug;
        -- if there is more than one database, set to global resource
        if _count > 1 then
            call createOrRetreiveDataResource(@_default_resource_name, @_data_resource_id);
        else 
            call createOrRetreiveDataResource(_database_name, @_data_resource_id);
        end if;        
        
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and rank = 1000 and author is null;
        call createOrRetreiveTaxonConcept(_name_id, null, 1000, null, true, _concept_id);
        call buildPhyla(_concept_id, _name);
        -- select concat('Finished kingdom: ', _name)  as debug;
    end loop _loop;
end;
|
delimiter ;

/**
 * Builds FROM the phylum down for the kingdom provided
 */
DROP procedure if EXISTS buildPhyla;
delimiter |
create procedure buildPhyla(IN _parent_id INT, IN _kingdom_name CHAR(255))
begin
    declare _done BOOLEAN DEFAULT false;
    declare _name CHAR(255);
    declare _database_name CHAR(255);
    declare _count int;
    declare _name_id int;
    declare _concept_id int;
    declare _cur cursor for SELECT phylum, database_name, count(distinct database_name) FROM col_families col WHERE kingdom = _kingdom_name AND phylum is not null group by phylum;
    declare continue handler for not found SET _done = true;
    open _cur;
    _loop: loop
        fetch _cur INTO _name, _database_name, _count;
        if _done then 
            close _cur;
            leave _loop;
        end if;
        -- select concat('Starting phylum: ', _name)  as debug;
        -- if there is more than one database, set to global resource
        if _count > 1 then
            call createOrRetreiveDataResource(@_default_resource_name, @_data_resource_id);
        else 
            call createOrRetreiveDataResource(_database_name, @_data_resource_id);
        end if;        
        
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and rank = 2000 and author is null;
        call createOrRetreiveTaxonConcept(_name_id, _parent_id, 2000, null,true, _concept_id);
        call buildClasses(_concept_id, _kingdom_name, _name);
        -- select concat('Finished phylum: ', _name)  as debug;
    end loop _loop;
end;
|
delimiter ;

/**
 * Builds FROM the class down for the phylum provided
 */
DROP procedure if EXISTS buildClasses;
delimiter |
create procedure buildClasses(IN _parent_id INT, IN _kingdom_name CHAR(255), IN _phylum_name CHAR(255))
begin
    declare _done BOOLEAN DEFAULT false;
    declare _name CHAR(255);
    declare _name_id int;
    declare _database_name CHAR(255);
    declare _count int;
    declare _concept_id int;
    declare _cur cursor for SELECT class, database_name, count(distinct database_name) FROM col_families col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class is not null group by class;
    declare continue handler for not found SET _done = true;
    open _cur;
    _loop: loop
        fetch _cur INTO _name, _database_name, _count;
        if _done then 
            close _cur;
            leave _loop;
        end if;
        -- if there is more than one database, set to global resource
        if _count > 1 then
            call createOrRetreiveDataResource(@_default_resource_name, @_data_resource_id);
        else 
            call createOrRetreiveDataResource(_database_name, @_data_resource_id);
        end if;        
        -- select concat('Starting class: ', _name)  as debug;
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and rank = 3000 and author is null;
        call createOrRetreiveTaxonConcept(_name_id, _parent_id, 3000, null, true, _concept_id);
        call buildOrders(_concept_id, _kingdom_name, _phylum_name, _name);
        -- select concat('Finished class: ', _name)  as debug;
    end loop _loop;
end;
|
delimiter ;

/**
 * Builds FROM the order down for the class provided
 */
DROP procedure if EXISTS buildOrders;
delimiter |
create procedure buildOrders(IN _parent_id INT, IN _kingdom_name CHAR(255), IN _phylum_name CHAR(255), IN _class_name CHAR(255))
begin
    declare _done BOOLEAN DEFAULT false;
    declare _name CHAR(255);
    declare _name_id int;
    declare _database_name CHAR(255);
    declare _count int;
    declare _concept_id int;
    declare _cur cursor for SELECT order_rank, database_name, count(distinct database_name) FROM col_families col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class = _class_name AND order_rank is not null group by order_rank;
    declare continue handler for not found SET _done = true;
    open _cur;
    _loop: loop
        fetch _cur INTO _name, _database_name, _count;
        if _done then 
            close _cur;
            leave _loop;
        end if;
        -- if there is more than one database, set to global resource
        if _count > 1 then
            call createOrRetreiveDataResource(@_default_resource_name, @_data_resource_id);
        else 
            call createOrRetreiveDataResource(_database_name, @_data_resource_id);
        end if;        
        -- select concat('Starting order: ', _name)  as debug;
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and rank = 4000 and author is null;
        call createOrRetreiveTaxonConcept(_name_id, _parent_id, 4000, null, true, _concept_id);
        call buildSuperFamilies(_concept_id, _kingdom_name, _phylum_name, _class_name, _name);
        /* The non super family ones */
        call buildFamilies(_concept_id, _kingdom_name, _phylum_name, _class_name, _name, null);
        -- select concat('Finished superfamily: ', _name)  as debug;
    end loop _loop;
end;
|
delimiter ;


/**
 * Builds FROM the super family down for the order provided
 */
DROP procedure if EXISTS buildSuperFamilies;
delimiter |
create procedure buildSuperFamilies(IN _parent_id INT, IN _kingdom_name CHAR(255), IN _phylum_name CHAR(255), IN _class_name CHAR(255), IN _order_name CHAR(255))
begin
    declare _done BOOLEAN DEFAULT false;
    declare _name CHAR(255);
    declare _name_id int;
    declare _database_name CHAR(255);
    declare _count int;
    declare _concept_id int;
    declare _cur cursor for SELECT super_family, database_name, count(distinct database_name) FROM col_families col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class = _class_name AND order_rank = _order_name AND super_family is not null group by super_family;
    declare continue handler for not found SET _done = true;
    /* SELECT concat('Super family: ', _kingdom_name, ' : ', _phylum_name, ' : ', _class_name, ' : ',  _order_name); */
    open _cur;
    _loop: loop
        fetch _cur INTO _name, _database_name, _count;
        if _done then 
            close _cur;
            leave _loop;
        end if;
        -- if there is more than one database, set to global resource
        if _count > 1 then
            call createOrRetreiveDataResource(@_default_resource_name, @_data_resource_id);
        else 
            call createOrRetreiveDataResource(_database_name, @_data_resource_id);
        end if;        
        -- select concat('Starting superfamily: ', _name)  as debug;
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and rank = 4500 and author is null;
        call createOrRetreiveTaxonConcept(_name_id, _parent_id, 4500, null, true, _concept_id);        
        call buildFamilies(_concept_id, _kingdom_name, _phylum_name, _class_name, _order_name, _name);
        -- select concat('Finished superfamily: ', _name)  as debug;
    end loop _loop;
end;
|
delimiter ;

/**
 * Builds FROM the family down for the VALUES provided
 */
DROP procedure if EXISTS buildFamilies;
delimiter |
create procedure buildFamilies(IN _parent_id INT, IN _kingdom_name CHAR(255), IN _phylum_name CHAR(255), IN _class_name CHAR(255), IN _order_name CHAR(255), IN _super_family_name CHAR(255))
begin
    declare _done BOOLEAN DEFAULT false;
    declare _name CHAR(255);
    declare _name_id int;
    declare _database_name CHAR(255);
    declare _count int;
    declare _concept_id int;
    declare _cur1 cursor for SELECT family, database_name, count(distinct database_name) FROM col_families col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class = _class_name AND order_rank = _order_name AND super_family = _super_family_name AND family is not null group by family;
    declare _cur2 cursor for SELECT family, database_name, count(distinct database_name) FROM col_families col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class = _class_name AND order_rank = _order_name AND super_family is null AND family is not null group by family;
    declare continue handler for not found SET _done = true;
    if _super_family_name is NOT NULL then
        open _cur1;
    else 
        open _cur2;
    end if;
    _loop: loop
        if _super_family_name is NOT NULL then
            fetch _cur1 INTO _name, _database_name, _count;
        else 
            fetch _cur2 INTO _name, _database_name, _count;
        end if;
        
        if _done then 
            if _super_family_name is NOT NULL then
                close _cur1;
            else 
                close _cur2;
            end if;
            leave _loop;
        end if;
        -- if there is more than one database, set to global resource
        if _count > 1 then
            call createOrRetreiveDataResource(@_default_resource_name, @_data_resource_id);
        else 
            call createOrRetreiveDataResource(_database_name, @_data_resource_id);
        end if;        
        -- select concat('Starting family: ', _name)  as debug;
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and rank = 5000 and author is null;
        call createOrRetreiveTaxonConcept(_name_id, _parent_id, 5000, null, true, _concept_id);
        call buildGenera(_concept_id, _kingdom_name, _phylum_name, _class_name, _order_name, _super_family_name, _name);
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
create procedure buildGenera(IN _parent_id INT, IN _kingdom_name CHAR(255), IN _phylum_name CHAR(255), IN _class_name CHAR(255), IN _order_name CHAR(255), IN _super_family_name CHAR(255), IN _family_name CHAR(255))
begin
    declare _done BOOLEAN DEFAULT false;
    declare _name CHAR(255);
    declare _name_id int;
    declare _database_name CHAR(255);
    declare _concept_id int;
    declare _cur1 cursor for SELECT genus, database_name FROM col_taxa col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class = _class_name AND order_rank = _order_name AND super_family = _super_family_name AND family = _family_name AND genus is not null group by genus;
    declare _cur2 cursor for SELECT genus, database_name FROM col_taxa col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class = _class_name AND order_rank = _order_name AND super_family is null AND family = _family_name AND genus is not null group by genus;
    declare continue handler for not found SET _done = true;
    if _super_family_name is NOT NULL then
        open _cur1;
    else 
        open _cur2;
    end if;
    _loop: loop
        if _super_family_name is NOT NULL then
            fetch _cur1 INTO _name, _database_name;
        else 
            fetch _cur2 INTO _name, _database_name;
        end if;
        
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
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and author is null and rank = 6000;
        call createOrRetreiveTaxonConcept(_name_id, _parent_id, 6000, null, true, _concept_id);
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
    declare _accepted int;
    declare _cur1 cursor for SELECT scientific_name, author, species, database_name, name_code, is_accepted_name FROM col_taxa col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class = _class_name AND order_rank = _order_name AND super_family = _super_family_name AND family = _family_name AND genus = _genus_name AND species is NOT NULL AND rank = 'Species' group by 1,2,3,4,5;
    declare _cur2 cursor for SELECT scientific_name, author, species, database_name, name_code, is_accepted_name FROM col_taxa col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class = _class_name AND order_rank = _order_name AND super_family is null AND family = _family_name AND genus = _genus_name AND species is NOT NULL AND rank = 'Species' group by 1,2,3,4,5;
    declare continue handler for not found SET _done = true;
    if _super_family_name is NOT NULL then
        open _cur1;
    else 
        open _cur2;
    end if;
    _loop: loop
        if _super_family_name is NOT NULL then
            fetch _cur1 INTO _name, _author, _species, _database_name,_name_code, _accepted;
        else 
            fetch _cur2 INTO _name, _author, _species, _database_name,_name_code, _accepted;
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
        
        if _author is not null then 
            SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and author = _author and rank = 7000;
        else 
            SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and author is null and rank = 7000;
        end if;
                
        if (_accepted = 1) then
            call createOrRetreiveTaxonConcept(_name_id, _parent_id, 7000, _name_code, true, _concept_id);
        else 
            call createOrRetreiveTaxonConcept(_name_id, _parent_id, 7000, _name_code, false, _concept_id);
        end if;
        -- call buildInfraspecies(_concept_id, _kingdom_name, _phylum_name, _class_name, _order_name, _super_family_name, _family_name, _genus_name, _species);
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
    declare _accepted int;    
    declare _cur1 cursor for SELECT scientific_name, author, database_name, name_code, is_accepted_name FROM col_taxa col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class = _class_name AND order_rank = _order_name AND super_family = _super_family_name AND family = _family_name AND genus = _genus_name AND species = _species_name AND infraspecies is not null  group by 1,2,3,4;
    declare _cur2 cursor for SELECT scientific_name, author, database_name, name_code, is_accepted_name FROM col_taxa col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class = _class_name AND order_rank = _order_name AND super_family is null AND family = _family_name AND genus = _genus_name AND species = _species_name AND infraspecies is not null  group by 1,2,3,4;
    declare continue handler for not found SET _done = true;
    if _super_family_name is NOT NULL then
        open _cur1;
    else 
        open _cur2;
    end if;
    _loop: loop
        if _super_family_name is NOT NULL then
            fetch _cur1 INTO _name, _author, _database_name, _name_code, _accepted;
        else 
            fetch _cur2 INTO _name, _author, _database_name, _name_code, _accepted;
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
       
        if _author is not null then 
            SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and author = _author and rank = 8000;
        else 
            SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and author is null and rank = 8000;
        end if;

        if (_accepted = 1) then
            call createOrRetreiveTaxonConcept(_name_id, _parent_id, 8000, _name_code, true, _concept_id);
        else 
            call createOrRetreiveTaxonConcept(_name_id, _parent_id, 8000, _name_code, false, _concept_id);
        end if;
        
        -- select concat('Finished infraspecies: ', _name)  as debug;
    end loop _loop;
end;
|
delimiter ;

/**
 * New version of the infraspecies building
 * The previous one had errors - If there were 13 species that had different authors there would be 13 subspecies created...
 * Since there are not so many sub species they can be created seperately
 */
DROP procedure if EXISTS buildAllInfraspecies;
delimiter |
create procedure buildAllInfraspecies()
begin
    declare _done BOOLEAN DEFAULT false;
    declare _kingdom CHAR(255);
    declare _phylum CHAR(255);
    declare _class CHAR(255);
    declare _order CHAR(255);
    declare _super_family CHAR(255);
    declare _family CHAR(255);
    declare _genus CHAR(255);
    declare _species CHAR(255);
    declare _database_name CHAR(255);
    declare _name_code CHAR(255);
    declare _species_name_code CHAR(255);
    declare _scientific_name CHAR(255);
    declare _author CHAR(255);
    declare _taxon_concept_id INT;
    declare _name_id INT;
    declare _accepted INT;
    declare _count INT default 0;
    declare _concept_id int;
    declare _cur1 cursor for SELECT kingdom, phylum, class, order_rank, super_family, family, genus, species, database_name, scientific_name, author, name_code, is_accepted_name from col_taxa where rank='Infraspecies';
    declare continue handler for not found SET _done = true;
    open _cur1;
    _loop: loop
        fetch _cur1 INTO _kingdom, _phylum, _class, _order, _super_family, _family, _genus, _species, _database_name, _scientific_name, _author, _name_code, _accepted;
        if _done then 
            close _cur1;
            leave _loop;
        end if;
        call createOrRetreiveDataResource(_database_name, @_data_resource_id);
        set _count = _count +1;
        
        /*
         * look for a species and then the genus to attach to if the species is not known
         * There are subspecies in COL that are attached only to genus
         */
        call getNameCodeOfSpecies(_kingdom, _phylum, _class, _order, _super_family, _family, _genus, _species, _database_name, _species_name_code);
        
        if _species_name_code is null then
            select concat('Getting a Genus');
            call getTaxonConceptIdOfGenus(_kingdom, _phylum, _class, _order, _family, _genus, _taxon_concept_id);
            select concat('Attaching to genus id: ', _taxon_concept_id);
        else
            select tc.id into _taxon_concept_id from taxon_concept tc inner join remote_concept rc on rc.taxon_concept_id = tc.id where rc.remote_id=_species_name_code;
        end if;
        
        if _taxon_concept_id is null then
            select concat('No concept id for: ', _scientific_name);
        else 
            if _author is not null then 
                SELECT id INTO _name_id FROM taxon_name WHERE canonical = _scientific_name and author = _author and rank = 8000;
            else 
                SELECT id INTO _name_id FROM taxon_name WHERE canonical = _scientific_name and author is null and rank = 8000;
            end if;
    
            if (_accepted = 1) then
                call createOrRetreiveTaxonConcept(_name_id, _taxon_concept_id, 8000, _name_code, true, _concept_id);
            else 
                call createOrRetreiveTaxonConcept(_name_id, _taxon_concept_id, 8000, _name_code, false, _concept_id);
            end if;
            
            /*
            * call buildInfraspecies(_taxon_concept_id, _kingdom, _phylum, _class, _order, _super_family, _family, _genus, _species);
            */
        end if;
    end loop _loop;
    select concat('Created ' , _count, ' infraspecies');
end;
|
delimiter ;

/**
 * This will get the species concept name_code from the col_taxa table for the classification provided
 * It defaults to the first accepted concept, then the first provisionally accepted, and then the first concept found
 */
DROP procedure if EXISTS getNameCodeOfSpecies;
delimiter |
create procedure getNameCodeOfSpecies(IN _kingdom_name CHAR(255), IN _phylum_name CHAR(255), IN _class_name CHAR(255), IN _order_name CHAR(255), IN _super_family_name CHAR(255), IN _family_name CHAR(255), IN _genus_name CHAR(255), IN _species_name CHAR(255), IN _database_name CHAR(255), INOUT _name_code CHAR(255))
begin
    declare _done BOOLEAN DEFAULT false;
    declare _cur1 cursor for SELECT name_code FROM col_taxa col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class = _class_name AND order_rank = _order_name AND (super_family is null or (super_family is not null and super_family = _super_family_name)) AND family = _family_name AND genus = _genus_name AND species = _species_name AND infraspecies is null AND database_name=_database_name and 2006_status_id in(1,4) order by 2006_status_id;
    declare _cur2 cursor for SELECT name_code FROM col_taxa col WHERE kingdom = _kingdom_name AND phylum = _phylum_name AND class = _class_name AND order_rank = _order_name AND (super_family is null or (super_family is not null and super_family = _super_family_name)) AND family = _family_name AND genus = _genus_name AND species = _species_name AND infraspecies is null AND database_name=_database_name;
    declare continue handler for not found SET _done = true;
    open _cur1;
    _loop: loop
        fetch _cur1 INTO _name_code;
        close _cur1;
        leave _loop;
    end loop _loop;

    if _name_code is null then
        SET _done = false;
        open _cur2;
        _loop2: loop
            fetch _cur2 INTO _name_code;
            close _cur2;
            leave _loop2;
        end loop _loop2;
    end if;
end;
|
delimiter ;

/**
 * This will get the taxon_concept id of the given classification of genus
 */
DROP procedure if EXISTS getTaxonConceptIdOfGenus;
delimiter |
create procedure getTaxonConceptIdOfGenus(IN _kingdom_name CHAR(255), IN _phylum_name CHAR(255), IN _class_name CHAR(255), IN _order_name CHAR(255), IN _family_name CHAR(255), IN _genus_name CHAR(255), INOUT _genus_id INT)
begin
    declare _done BOOLEAN DEFAULT false;
    declare _cur1 cursor for 
        select gc.id
        from taxon_concept gc inner join taxon_name gn on gc.taxon_name_id = gn.id
            inner join taxon_concept fc on gc.family_concept_id = fc.id inner join taxon_name fn on fc.taxon_name_id = fn.id
            inner join taxon_concept oc on gc.order_concept_id = oc.id inner join taxon_name orn on oc.taxon_name_id = orn.id
            inner join taxon_concept cc on gc.class_concept_id = cc.id inner join taxon_name cn on cc.taxon_name_id = cn.id
            inner join taxon_concept pc on gc.phylum_concept_id = pc.id inner join taxon_name pn on pc.taxon_name_id = pn.id
            inner join taxon_concept kc on gc.kingdom_concept_id = kc.id inner join taxon_name kn on kc.taxon_name_id = kn.id
        where gc.data_resource_id = @_data_resource_id and 
            kn.canonical = _kingdom_name and
            pn.canonical = _phylum_name and
            cn.canonical = _class_name and
            orn.canonical = _order_name and
            gn.canonical = _genus_name;
    declare continue handler for not found SET _done = true;
    open _cur1;
    _loop: loop
        fetch _cur1 INTO _genus_id;
        close _cur1;
        leave _loop;
    end loop _loop;
end;
|
delimiter ;

/**
 * Build the relationships for:
 * 2) ambiguous synonym (GBIF relationship_type 1)
 */
DROP procedure if EXISTS buildAmbiguousSynonyms;
delimiter |
create procedure buildAmbiguousSynonyms()
begin
    declare _done BOOLEAN DEFAULT false;
    declare _name_code CHAR(255);    
    declare _accepted_name_code CHAR(255);
    declare _cur1 cursor for SELECT name_code, accepted_name_code FROM col_taxa col WHERE 2006_status_id=2;
    declare continue handler for not found SET _done = true;
    open _cur1;
    _loop: loop
        fetch _cur1 INTO _name_code, _accepted_name_code;
        if _done then 
            close _cur1;
            leave _loop;
        end if;
        call createRelationshipIfNotExists(1, _name_code, _accepted_name_code);
    end loop _loop;
end;
|
delimiter ;

/**
 * Build the relationships for:
 * 3) misapplied name (GBIF relationship_type 2)
 */
DROP procedure if EXISTS buildMisappliedNames;
delimiter |
create procedure buildMisappliedNames()
begin
    declare _done BOOLEAN DEFAULT false;
    declare _name_code CHAR(255);    
    declare _accepted_name_code CHAR(255);
    declare _cur1 cursor for SELECT name_code, accepted_name_code FROM col_taxa col WHERE 2006_status_id=3;
    declare continue handler for not found SET _done = true;
    open _cur1;
    _loop: loop
        fetch _cur1 INTO _name_code, _accepted_name_code;
        if _done then 
            close _cur1;
            leave _loop;
        end if;
        call createRelationshipIfNotExists(2, _name_code, _accepted_name_code);
    end loop _loop;
end;
|
delimiter ;

/**
 * Build the relationships for:
 * 4) provisionally applied name (GBIF relationship_type 3 - should point to itself)
 */
DROP procedure if EXISTS buildProvisionallyApplied;
delimiter |
create procedure buildProvisionallyApplied()
begin
    declare _done BOOLEAN DEFAULT false;
    declare _name_code CHAR(255);    
    declare _accepted_name_code CHAR(255);
    declare _cur1 cursor for SELECT name_code, name_code FROM col_taxa col WHERE 2006_status_id=4;
    declare continue handler for not found SET _done = true;
    open _cur1;
    _loop: loop
        fetch _cur1 INTO _name_code, _accepted_name_code;
        if _done then 
            close _cur1;
            leave _loop;
        end if;
        call createRelationshipIfNotExists(3, _name_code, _accepted_name_code);
    end loop _loop;
end;
|
delimiter ;

/**
 * Build the relationships for:
 * 5) synonym (GBIF relationship_type 4)
 */
DROP procedure if EXISTS buildSynonyms;
delimiter |
create procedure buildSynonyms()
begin
    declare _done BOOLEAN DEFAULT false;
    declare _name_code CHAR(255);    
    declare _accepted_name_code CHAR(255);
    declare _cur1 cursor for SELECT name_code, accepted_name_code FROM col_taxa col WHERE 2006_status_id=5;
    declare continue handler for not found SET _done = true;
    open _cur1;
    _loop: loop
        fetch _cur1 INTO _name_code, _accepted_name_code;
        if _done then 
            close _cur1;
            leave _loop;
        end if;
        call createRelationshipIfNotExists(4, _name_code, _accepted_name_code);
    end loop _loop;
end;
|
delimiter ;

/**
 * Creates the relationship if it does not already exist
 */
DROP procedure if EXISTS createRelationshipIfNotExists;
delimiter |
create procedure createRelationshipIfNotExists(IN _relationship_type INT, IN _name_code char(255), IN _accepted_name_code char(255))
begin
    insert ignore into relationship_assertion(from_concept_id, to_concept_id, relationship_type)    
    select
        from_concept.id,
        to_concept.id,
        _relationship_type
    from 
        taxon_concept from_concept
            inner join remote_concept remote_from on remote_from.taxon_concept_id = from_concept.id
            left join relationship_assertion existing_from on existing_from.from_concept_id = from_concept.id 
                and existing_from.relationship_type = _relationship_type,
        taxon_concept to_concept
            inner join remote_concept remote_to on remote_to.taxon_concept_id = to_concept.id
    where 
        remote_from.remote_id = _name_code and
        remote_to.remote_id = _accepted_name_code and
        from_concept.data_provider_id = @_data_provider_id and
        to_concept.data_provider_id = @_data_provider_id and
        (existing_from.to_concept_id is null or 
        existing_from.to_concept_id != to_concept.id)
    group by 1,2,3;
end;
|
delimiter ;


/**
 * Build's the lot!
 */
call synchroniseTaxonNamesForHigherTaxa(); 
call synchroniseTaxonNames(); 
set @_data_provider_name = 'Catalogue of Life: 2006 Annual Checklist';
set @_default_resource_name = 'Species 2000 & ITIS Catalogue of Life Hierarchy, Edition 1 (2006)';
call createOrRetreiveDataProvider(@_data_provider_name, @_data_provider_id);
call buildKingdoms();
call buildAllInfraspecies();
call buildAmbiguousSynonyms();
call buildMisappliedNames();
call buildProvisionallyApplied();
call buildSynonyms();    



/**
 * Build the distributions
 * DON'T RUN MORE THAN ONCE :o)
 */
insert into distribution (taxon_concept_id, text, iso_language_code)
select rc.taxon_concept_id, cd.distribution, 'EN' from
remote_concept rc inner join col_distribution cd on cd.name_code = rc.remote_id;
 

/**
 * Build the common names
 * DON'T RUN MORE THAN ONCE :o)
 */
insert into common_name (taxon_concept_id, name, language)
select rc.taxon_concept_id, cvn.name, cvn.language from
remote_concept rc inner join col_vernacular_name cvn on cvn.name_code = rc.remote_id;


/**
 * update the genera to be non accepted
 */
update taxon_concept set is_accepted=false where rank=6000;


/**
 * update the genera to be accepted for any that are
 */
update 
    taxon_concept pc 
        inner join taxon_concept cc on cc.parent_concept_id = pc.id and cc.is_accepted=true
set pc.is_accepted=true
where pc.rank=6000;

/**
 * Update the denormalised part
 */
update taxon_concept c
    inner join taxon_concept p on c.phylum_concept_id = p.id
    inner join taxon_name tn on p.taxon_name_id=tn.id
set
    c.phylum_concept_id = null
where 
    tn.canonical='Not assigned';

update taxon_concept c
    inner join taxon_concept cl on c.class_concept_id = cl.id
    inner join taxon_name tn on cl.taxon_name_id=tn.id
set
    c.class_concept_id = null
where 
    tn.canonical='Not assigned';
    
update taxon_concept c
    inner join taxon_concept o on c.order_concept_id = o.id
    inner join taxon_name tn on o.taxon_name_id=tn.id
set
    c.order_concept_id = null
where 
    tn.canonical='Not assigned';

update taxon_concept c
    inner join taxon_concept f on c.family_concept_id = f.id
    inner join taxon_name tn on f.taxon_name_id=tn.id
set
    c.family_concept_id = null
where 
    tn.canonical='Not assigned';

/**
 * Now update the parents
 */ 
update 
    taxon_concept c
        inner join taxon_concept p on c.parent_concept_id=p.id
        inner join taxon_name pn on p.taxon_name_id=pn.id
set
    c.parent_concept_id = p.parent_concept_id,
    c.phylum_concept_id=null
where 
    pn.canonical='Not assigned' and
    c.data_provider_id = @_data_provider_id@ and 
    p.rank=2000;    

update 
    taxon_concept c
        inner join taxon_concept p on c.parent_concept_id=p.id
        inner join taxon_name pn on p.taxon_name_id=pn.id
set
    c.parent_concept_id = p.parent_concept_id,
    c.phylum_concept_id = p.phylum_concept_id,
    c.class_concept_id=null
where 
    pn.canonical='Not assigned' and
    c.data_provider_id = @_data_provider_id@ and  
    p.rank=3000;
    
update 
    taxon_concept c
        inner join taxon_concept p on c.parent_concept_id=p.id
        inner join taxon_name pn on p.taxon_name_id=pn.id
set
    c.parent_concept_id = p.parent_concept_id,
    c.phylum_concept_id = p.phylum_concept_id,
    c.class_concept_id=p.class_concept_id,
    c.order_concept_id=null
where 
    pn.canonical='Not assigned' and
    c.data_provider_id = @_data_provider_id@ and  
    p.rank=4000;

update 
    taxon_concept c
        inner join taxon_concept p on c.parent_concept_id=p.id
        inner join taxon_name pn on p.taxon_name_id=pn.id
set
    c.parent_concept_id = p.parent_concept_id,
    c.phylum_concept_id = p.phylum_concept_id,
    c.class_concept_id=p.class_concept_id,
    c.order_concept_id=p.order_concept_id,
    c.family_concept_id=null
where 
    pn.canonical='Not assigned' and
    c.data_provider_id = @_data_provider_id@ and  
    p.rank=5000;

update 
    taxon_concept c
        inner join taxon_concept p on c.parent_concept_id=p.id
        inner join taxon_name pn on p.taxon_name_id=pn.id
set
    c.parent_concept_id = p.parent_concept_id,
    c.phylum_concept_id = p.phylum_concept_id,
    c.class_concept_id=p.class_concept_id,
    c.order_concept_id=p.order_concept_id,
    c.family_concept_id=p.family_concept_id,
    c.genus_concept_id=null
where 
    pn.canonical='Not assigned' and
    c.data_provider_id = @_data_provider_id@ and  
    p.rank=6000;

/**
* And delete the concepts
*/
   update taxon_concept tc 
        inner join taxon_name tn on tc.taxon_name_id=tn.id
set 
    kingdom_concept_id = null,
    phylum_concept_id = null,
    class_concept_id = null,
    order_concept_id = null,
    family_concept_id = null,
    genus_concept_id = null,
    species_concept_id = null
where
    tn.canonical='Not assigned';


delete 
    tc.* 
from taxon_concept tc 
        inner join taxon_name tn on tc.taxon_name_id=tn.id
where 
    tn.canonical='Not assigned';

   
