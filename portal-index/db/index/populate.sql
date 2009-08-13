truncate table index_stats;

insert into 
    index_stats(data_provider_id, data_resource_id, resource_access_point_id, resource_record_count)
select
    data_provider_id, data_resource_id, resource_access_point_id, count(id)
from
    raw_occurrence_record
group by 
    1,2,3;

update 
    index_stats i, 
    (select data_provider_id, sum(resource_record_count) count from index_stats group by data_provider_id) sum 
set 
    i.provider_record_count = sum.count 
 where i.data_provider_id = sum.data_provider_id;

update index_stats i
    inner join data_provider dp on dp.id = i.data_provider_id
set i.provider_name = dp.name;

update index_stats i
    inner join data_resource dr on dr.id = i.data_resource_id
set i.resource_name = dr.name;

update index_stats i
    inner join resource_access_point rap on rap.id = i.resource_access_point_id
set i.resource_code = rap.remote_id_at_url;

update index_stats i
    inner join index_data id on id.resource_access_point_id = i.resource_access_point_id
set i.indexing = true
where id.finished is null;

/**
select
    data_provider_id, data_resource_id, resource_access_point_id, SUBSTRING(provider_name,1,25), SUBSTRING(trim(resource_code),1,25), SUBSTRING(trim(resource_name),1,25), indexing, resource_record_count, provider_record_count
from index_stats order by 4,5,6
into outfile '/tmp/indexStats6.txt';
*/
