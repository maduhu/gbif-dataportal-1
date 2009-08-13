SET NAMES 'utf8';

/**
 * Import the families
 */
drop table ipni;
create table ipni (    
    lsid varchar(100) primary key,
    id varchar(20),
    version varchar(50),
    family varchar(255),
    name varchar(255),
    author varchar(255),
    harvest_date datetime,
    index index_id(id),
    index index_version(version),
    index index_family(family),
    index index_name(name),
    index index_author(author),
    index index_harvest_date(harvest_date)) CHARACTER SET utf8 COLLATE utf8_general_ci;
  
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_0.txt'  IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_1.txt'  IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_2.txt'  IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_3.txt'  IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_4.txt'  IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_5.txt'  IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_6.txt'  IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_7.txt'  IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_8.txt'  IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_9.txt'  IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_10.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_11.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_12.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_13.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_14.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_15.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_16.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_17.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_18.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_19.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_20.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_21.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_22.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_23.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_24.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_25.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_26.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_27.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_28.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_29.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_30.txt' IGNORE into table ipni IGNORE 1 LINES;
load data LOCAL infile 'c:/temp/ipni/zipped/minimal_download_31.txt' IGNORE into table ipni IGNORE 1 LINES;
