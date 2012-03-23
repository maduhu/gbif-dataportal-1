/**
 * Creates ranges for the RAP id
 */
DROP procedure if EXISTS createIndexDataRange;
delimiter |
create procedure createIndexDataRange(IN _resource_access_point_id int)
begin    
    declare i int default 0;
    declare j int default 0;
    while (i<26) do
        set j = 0;
        while (j<25) do
            insert into index_data(resource_access_point_id, type, lower_value, upper_value) 
                values(
                    _resource_access_point_id, 
                    1, 
                    concat(char(65+i), char(65+j), char(65)),
                    concat(char(65+i), char(66+j), char(65)));
            insert into index_data(resource_access_point_id, type, lower_value, upper_value) 
                values(
                    _resource_access_point_id, 
                    1, 
                    concat(char(97+i), char(97+j), char(65)),
                    concat(char(97+i), char(98+j), char(65)));
            set j = j+1;
        end while;
        set i = i+1;
    end while;
end;
|
delimiter ;

truncate table index_data;
call createIndexDataRange(1087);
select * from index_data;

