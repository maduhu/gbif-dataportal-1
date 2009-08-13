update qrtz_triggers set NEXT_FIRE_TIME=NEXT_FIRE_TIME-(1000*60*60*24*7) where job_group='extract';
update qrtz_triggers set NEXT_FIRE_TIME=NEXT_FIRE_TIME+(1000*60*60*24*7) where job_group<>'extract';


/** See how far we've got... **/
drop table extract;
create table extract (data_resource_id int, rap_id int, ror_count int, or_count int) TYPE=MYISAM;
insert into extract (data_resource_id, rap_id, ror_count) select data_resource_id, resource_access_point_id, count(id) from raw_occurrence_record group by 1,2 order by 1;
update extract e set e.or_count=(select count(id) from occurrence_record o where o.data_resource_id = e.data_resource_id);
select * from extract where (0.9 * ror_count)>or_count;
