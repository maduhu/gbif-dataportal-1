package org.gbif.portal.dao.tag;

/**
 * Placeholder interface for tag id enums.
 * 
 * @author davejmartin
 */
public interface TagDAO {

	/** Tag id for host country - country tag */
	public static final int HOSTCOUNTRY_COUNTRY_TAG_ID = 2001;
	
	/** Keywords associated with this dataset */
	public static final int DATA_RESOURCE_KEYWORDS = 4000;
	
	public static final int DATA_RESOURCE_OCCURRENCES_COUNTRY = 4100;
	
	public static final int DATA_RESOURCE_OCCURRENCES_BOUNDING_BOX = 4101;
	
	public static final int DATA_RESOURCE_OCCURRENCES_WKT_POLYGON = 4102;
	
	public static final int DATA_RESOURCE_OCCURRENCES_DATE_RANGE = 4120;
	
	public static final int DATA_RESOURCE_OCCURRENCES_MONTH = 4121;
	
	public static final int DATA_RESOURCE_OCCURRENCES_SPECIES = 4140;
	
	public static final int DATA_RESOURCE_OCCURRENCES_GENUS = 4141;
	
	public static final int DATA_RESOURCE_OCCURRENCES_FAMILY = 4142;
	
	public static final int DATA_RESOURCE_TAXONOMIC_SCOPE = 4150; 
	
	public static final int DATA_RESOURCE_ASSOCIATED_KINGDOM = 4151;

	/** Species common names associated with this resource */
	public static final int DATA_RESOURCE_COMMON_NAMES = 4152;

	/** Indicates whether a data resource contains type records */
	public static final int DATA_RESOURCE_CONTAINS_TYPE_SPECIMENS = 4160;

	/** Names of collectors who have collected for this dataset */
	public static final int DATA_RESOURCE_COLLECTOR = 4161;
}