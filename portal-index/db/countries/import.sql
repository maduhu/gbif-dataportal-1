truncate table country;
truncate table country_name;
truncate table cell_country;

load data 
    infile '/tmp/countries/iso_3116-1_country_codes.txt'
into table
    country
     (iso_country_code, continent_code, region) ; 

load data 
    infile '/tmp/countries/iso_3116-1_countries_en.txt'
into table
    country_name
     (iso_country_code, name, searchable_name, locale); 
        
load data 
    infile '/tmp/countries/iso_3116-1_countries_fr.txt'
into table
    country_name
     (iso_country_code, name, searchable_name, locale);

load data 
    infile '/tmp/countries/local_names.txt'
into table
    country_name
     (iso_country_code, name); 

load data 
    infile '/tmp/countries/country_names.txt'
into table
    country_name
     (iso_country_code, name); 
     
load data infile '/tmp/countries/ip_country.txt' into table ip_country (start, end, start_long, end_long, iso_country_code);
     
update country_name cn set country_id =   
(select 
    id
from 
    country c
where 
    c.iso_country_code=cn.iso_country_code);     

load data
    infile '/tmp/countries/iso_3116-1_country_cell_intersections.txt'
ignore
into table
    cell_country
     (iso_country_code, cell_id);
     
update country c 
    set min_latitude=
        (select (floor((min(cell_id)/360))-90) 
            from cell_country cc 
                where c.iso_country_code=cc.iso_country_code 
            group by cc.iso_country_code);

update country c 
    set max_latitude=
        (select (ceiling((max(cell_id)/360))-90) 
            from cell_country cc 
                where c.iso_country_code=cc.iso_country_code 
            group by cc.iso_country_code);
            
update country c 
    set min_longitude=
        (select (min(cell_id%360)-180) 
            from cell_country cc 
                where c.iso_country_code=cc.iso_country_code 
            group by cc.iso_country_code);
            
update country c 
    set max_longitude=
        (select (max(cell_id%360)-179) 
            from cell_country cc 
                where c.iso_country_code=cc.iso_country_code 
            group by cc.iso_country_code);            
            