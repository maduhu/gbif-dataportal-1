update qrtz_triggers set NEXT_FIRE_TIME=NEXT_FIRE_TIME-(1000*60*60*24*7) where job_group='harvesting';
update qrtz_triggers set NEXT_FIRE_TIME=NEXT_FIRE_TIME+(1000*60*60*24*7) where job_group<>'harvesting';
