SET NAMES 'cp1251';

/**
 * This is a MYSQL5 script that will build the Taxonomy for the Nub tree into the GBIF Portal.
 * The script is essentially that of the COL2006 at present, but will morph into somehing more
 * meaningful when the GBIF Taxonomy Project gets more involved in 2007.
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
 * Ensures that there EXISTS a record in the taxon_name table for ALL the names found in the col_taxa table
 */
DROP procedure if EXISTS synchroniseTaxonNames;
delimiter |
create procedure synchroniseTaxonNames()
begin
    INSERT INTO taxon_name(canonical, supra_generic, author, rank) 
        SELECT kingdom, kingdom, null, 1000 FROM col_families cf left outer JOIN taxon_name tn on canonical = kingdom WHERE kingdom is NOT NULL AND (canonical is null or tn.rank!=1000) group by 1
        union distinct
        SELECT distinct phylum, phylum, null, 2000 FROM col_families cf left outer JOIN taxon_name tn on canonical = phylum WHERE phylum is NOT NULL AND (canonical is null or tn.rank!=2000) group by 1
        union distinct
        SELECT distinct class, class, null, 3000 FROM col_families cf left outer JOIN taxon_name tn on canonical = class WHERE class is NOT NULL AND (canonical is null or tn.rank!=3000) group by 1
        union distinct
        SELECT distinct order_rank, order_rank, null, 4000 FROM col_families cf left outer JOIN taxon_name tn on canonical = order_rank WHERE order_rank is NOT NULL AND (canonical is null or tn.rank!=4000) group by 1
        union distinct
        SELECT distinct super_family, super_family, null, 4500 FROM col_families cf  left outer JOIN taxon_name tn on canonical = super_family WHERE super_family is NOT NULL AND (canonical is null or tn.rank!=4500) group by 1
        union distinct
        SELECT distinct family, family, null, 5000 FROM col_families cf left outer JOIN taxon_name tn on canonical = family WHERE family is NOT NULL AND (canonical is null or tn.rank!=5000) group by 1;
    
    INSERT INTO taxon_name(canonical, generic, author, rank) 
        SELECT distinct genus, genus, null, 6000 FROM col_taxa ct left outer JOIN taxon_name tn on canonical = genus WHERE genus is NOT NULL AND (canonical is null or tn.rank!=6000) group by 1;
        
    INSERT INTO taxon_name(canonical, generic, specific_epithet, author, rank)         
        SELECT scientific_name, genus, species, ct.author, 7000 FROM col_taxa ct left outer JOIN taxon_name tn on canonical = scientific_name WHERE species is NOT NULL AND ct.rank='Species' AND (canonical is null or tn.rank!=7000 or tn.author!=ct.author) group by scientific_name, author;
        
    INSERT INTO taxon_name(canonical, generic, specific_epithet, infraspecific, infraspecific_marker, author, rank)         
        SELECT scientific_name, genus, species, infraspecies, infraspecies_marker, ct.author, 8000 FROM col_taxa ct left outer JOIN taxon_name tn on canonical = scientific_name WHERE infraspecies is NOT NULL AND ct.rank='Infraspecies' AND (canonical is null or tn.rank!=8000 or tn.author!=ct.author) group by scientific_name, author;
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
        WHERE parent.id = _parent_concept_id AND child.taxon_name_id = _taxon_name_id AND child.rank = _rank
        AND parent.data_provider_id = @_data_provider_id;
    else 
        SELECT id INTO _existing_id 
        FROM taxon_concept 
        WHERE taxon_name_id = _taxon_name_id AND rank = _rank and data_provider_id = @_data_provider_id;
    end if;

    if _existing_id is null then 
        INSERT INTO taxon_concept(taxon_name_id, parent_concept_id, rank, data_provider_id, data_resource_id) VALUES (_taxon_name_id, _parent_concept_id, _rank, @_data_provider_id, @_data_resource_id);
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
        
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and rank = 1000 and author is null;
        call createOrRetreiveTaxonConcept(_name_id, null, 1000, null, _concept_id);
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
        
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and rank = 2000 and author is null;
        call createOrRetreiveTaxonConcept(_name_id, _parent_id, 2000, null, _concept_id);
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

        -- select concat('Starting class: ', _name)  as debug;
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and rank = 3000 and author is null;
        call createOrRetreiveTaxonConcept(_name_id, _parent_id, 3000, null, _concept_id);
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

        -- select concat('Starting order: ', _name)  as debug;
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and rank = 4000 and author is null;
        call createOrRetreiveTaxonConcept(_name_id, _parent_id, 4000, null, _concept_id);
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

        -- select concat('Starting superfamily: ', _name)  as debug;
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and rank = 4500 and author is null;
        call createOrRetreiveTaxonConcept(_name_id, _parent_id, 4500, null, _concept_id);        
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

        -- select concat('Starting family: ', _name)  as debug;
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and rank = 5000 and author is null;
        call createOrRetreiveTaxonConcept(_name_id, _parent_id, 5000, null, _concept_id);
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
        -- there is bad data in COL that means some genera are incorrect and have a "Aus bus" instead of "Aus"
        -- these have no author though
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and author is null and rank = 6000;
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
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and author = _author and rank = 7000;
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
        SELECT id INTO _name_id FROM taxon_name WHERE canonical = _name and author = _author and rank = 8000;
        call createOrRetreiveTaxonConcept(_name_id, _parent_id, 8000, _name_code, _concept_id);
        -- select concat('Finished infraspecies: ', _name)  as debug;
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
    -- select _name_code, _accepted_name_code;
    -- Something funny goes on with this - the query is correct in that it won't insert duplicates, but when this is called in a cursor
    -- we hit drama's!  Hence the Ignore is used.
    insert IGNORE into relationship_assertion(from_concept_id, to_concept_id, relationship_type)    
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
            left join relationship_assertion existing_to on existing_to.to_concept_id = to_concept.id 
                and existing_to.relationship_type = _relationship_type
    where 
        remote_from.remote_id = _name_code and
        remote_to.remote_id = _accepted_name_code and
        from_concept.data_provider_id = @_data_provider_id and
        to_concept.data_provider_id = @_data_provider_id and
        (existing_from.from_concept_id is null or 
        existing_to.to_concept_id is null or 
        existing_to.from_concept_id != existing_from.to_concept_id or
        existing_from.to_concept_id = existing_to.from_concept_id);
    end;
|
delimiter ;


/**
 * Build's the lot!
 */
set @_data_provider_name = 'The Global Biodiversity Information Facility';
set @_default_resource_name = 'ECAT Taxonomy (Under Development)';
call createOrRetreiveDataProvider(@_data_provider_name, @_data_provider_id);
call createOrRetreiveDataResource(@_default_resource_name, @_data_resource_id);
call buildKingdoms();

/**
 * Tidy up...
 *
DROP procedure if EXISTS synchroniseTaxonNames;
DROP procedure if EXISTS synchroniseTaxonNamesMySQL;
DROP procedure if EXISTS createOrRetreiveDataProvider;
DROP procedure if EXISTS createOrRetreiveTaxonConcept;
DROP procedure if EXISTS encodeRankString;
DROP procedure if EXISTS buildKingdoms;
DROP procedure if EXISTS buildPhyla;
DROP procedure if EXISTS buildClasses;
DROP procedure if EXISTS buildOrders;
DROP procedure if EXISTS buildSuperFamilies;
DROP procedure if EXISTS buildFamilies;
DROP procedure if EXISTS buildGenera;
DROP procedure if EXISTS buildSpecies;
DROP procedure if EXISTS buildInfraspecies;
DROP procedure if EXISTS buildAmbiguousSynonyms;
DROP procedure if EXISTS buildMisappliedNames;
DROP procedure if EXISTS buildProvisionallyApplied;
DROP procedure if EXISTS buildSynonyms;
DROP procedure if EXISTS createRelationshipIfNotExists;
*/