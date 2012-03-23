/*
Andrea has done manual changes - this PART of the file must not be run.
We need to take a table dump and use that instead

truncate table resource_network;
truncate table network_membership;

load data 
    infile '/tmp/networks/resource_networks.txt'
into table
    resource_network
     (name, code, description, website_url, logo_url) ; 
     
update resource_network set created=current_timestamp() where created is null;
update resource_network set modified=current_timestamp() where created is null;
*/
     
-- Some crude initial settings for membership
/*
insert into network_membership (resource_network_id, data_resource_id) select 1, data_resource_id from resource_access_point where data_resource_id!=0 and (url like '%manis%' or remote_id_at_url like "%manis%" or remote_id_at_url like "%mammal%");
insert into network_membership (resource_network_id, data_resource_id) select 3, data_resource_id from resource_access_point where data_resource_id!=0 and (url like '%obis%' or remote_id_at_url like "%obis%");
insert into network_membership (resource_network_id, data_resource_id) select 4, data_resource_id from resource_access_point where data_resource_id!=0 and url like '%biocase%';
insert into network_membership (resource_network_id, data_resource_id) select 6, data_resource_id from resource_access_point where data_resource_id!=0 and (url like '%herp%' or remote_id_at_url like "%herp%");



-- create a network for the resources associtated with a country
insert into resource_network(name, code, description, created, modified)
select 
    concat('Resources hosted by ', cn.name),
    c.iso_country_code,
    concat('A network consisting of the data resources hosted by ', cn.name),
    now(),
    now()
from
    data_provider dp
        inner join country c on c.iso_country_code = dp.iso_country_code
        inner join country_name cn on cn.iso_country_code = c.iso_country_code
group by dp.iso_country_code;

insert into network_membership(data_resource_id, resource_network_id)
select
    dr.id, rn.id
from 
    data_resource dr
        inner join data_provider dp on dr.data_provider_id = dp.id
        inner join resource_network rn on rn.code = dp.iso_country_code;
-- end network per country
*/




-- Set data resource counts
update resource_network rn set data_resource_count = (select count(nm.id) from network_membership nm  where nm.resource_network_id= rn.id);
update data_provider dp set dp.data_resource_count = (select count(dr.id) from data_resource dr  where dr.data_provider_id=dp.id);
