
CREATE TABLE index_stats (
       id INT NOT NULL AUTO_INCREMENT
     , data_provider_id INT
     , data_resource_id INT
     , resource_access_point_id CHAR(10)
     , provider_name VARCHAR(255)
     , resource_code VARCHAR(255)
     , resource_name VARCHAR(255)
     , indexing BOOL DEFAULT false
     , resource_record_count INT
     , provider_record_count INT
     , PRIMARY KEY (id)
);

