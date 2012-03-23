/**
 * 
 */
package org.gbif.portal.dto;

/**
 * Key Value Pair DTO Factory
 * 
 * @author dmartin
 */
public class KeyValueDTOFactory extends BaseDTOFactory {

	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(java.lang.Object)
	 */
	public Object createDTO(Object modelObject) {
		if(modelObject!=null && modelObject instanceof Object[]){
			Object[] kvps = (Object[]) modelObject;
			if(kvps.length==2){
				String value = null;
				if(kvps[1]!=null)
					value = kvps[1].toString();
				return new KeyValueDTO(kvps[0].toString(), value);
			} else{
				return new KeyValueDTO(kvps[0].toString(), kvps[0].toString());
			}
		}
		return null;
	}
}
