/**
 * This is a script that allocates indexes to predefined buffers in the server.
 * To run as on startup add the --init-file=/etc/mysql/init-portal.sql
 * E.g.:
 *   mysqld_safe --init-file=/etc/mysql/init-portal.sql &
 *
 * To use this, one must have something like the following in the my.cnf file:

# Create some custom key buffers that will hold specific indexes
# These are huge caches designed to get the entire index into memory
taxon_concept_cache.key_buffer_size=2000M
# caches for the occurrence_record
occurrence_record_cache_1.key_buffer_size=3990M
occurrence_record_cache_2.key_buffer_size=3990M
occurrence_record_cache_3.key_buffer_size=3990M
occurrence_record_cache_4.key_buffer_size=3990M
occurrence_record_cache_5.key_buffer_size=3990M
occurrence_record_cache_6.key_buffer_size=3990M
occurrence_record_cache_7.key_buffer_size=3990M
occurrence_record_cache_8.key_buffer_size=3990M
 
 */

/**
 * Cache the entire TC index in the buffer
 */
cache index taxon_concept in taxon_concept_cache;
load index into cache taxon_concept;

/**
 * This makes an educated guess at splitting up the indexes on the occurrence record into sensible chunks
 */
cache index occurrence_record index(ix_or_k_cell_mod_cell,ix_or_data_resource_id,ix_or_occurrence_date) in occurrence_record_cache_1;
cache index occurrence_record index(ix_or_p_cell_mod_cell,ix_or_institution_code,ix_or_month) in occurrence_record_cache_2;
cache index occurrence_record index(ix_or_c_cell_mod_cell,ix_or_collection_code,ix_or_data_provider_id) in occurrence_record_cache_3;
cache index occurrence_record index(ix_or_o_cell_mod_cell,ix_or_catalogue_number) in occurrence_record_cache_4;
cache index occurrence_record index(ix_or_f_cell_mod_cell,ix_or_taxon_concept_id) in occurrence_record_cache_5;
cache index occurrence_record index(ix_or_g_cell_mod_cell,ix_or_taxon_name_id) in occurrence_record_cache_6;
cache index occurrence_record index(ix_or_s_cell_mod_cell,ix_or_iso_country_code,ix_or_year) in occurrence_record_cache_7;
cache index occurrence_record index(ix_or_cell_mod_cell_nub, ix_or_nub_country) in occurrence_record_cache_8;
load index into cache occurrence_record;
