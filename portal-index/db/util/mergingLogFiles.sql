-- first find out when the logs need merged from

-- first go to the machine that you are preparing the DB on and will put live
select date(timestamp), count(id) from gbif_log_message where (timestamp>now()-(1000*60*60*24*100)) and event_id>=3000 group by 1;
-- should be clear on what date the last rollover was

-- now go to the live DB and get the usage log
select *
into outfile '/tmp/usage.txt'
from gbif_log_message
where timestamp>'20070225'
and event_id>=2000;

-- and users
select * 
into outfile '/tmp/users.txt' 
from gbif_user;

-- now FTP the files to the preparing DB and go to the preparing DB mysql prompt:
truncate table gbif_user;
load data infile '/var/lib/mysql/users.txt' 
into table gbif_user;

-- temp table for usage
create table temp_usage like gbif_log_message;
load data infile '/var/lib/mysql/usage.txt' 
into table temp_usage;

-- copy in
insert into gbif_log_message(portal_instance_id, log_group_id, event_id, level, data_provider_id, data_resource_id, occurrence_id, taxon_concept_id, user_id, message, restricted, count, timestamp)
select portal_instance_id, log_group_id, event_id, level, data_provider_id, data_resource_id, occurrence_id, taxon_concept_id, user_id, message, restricted, count, timestamp from temp_usage;

-- remove temp table
drop table temp_usage;
