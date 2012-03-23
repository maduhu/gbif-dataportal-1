alter table occurrence_record
	add altitude_metres smallint signed,
	add depth_centimetres mediumint unsigned,
    add index ix_or_altitude_metres (altitude_metres),
	add index ix_or_depth_centimetres (depth_centimetres);