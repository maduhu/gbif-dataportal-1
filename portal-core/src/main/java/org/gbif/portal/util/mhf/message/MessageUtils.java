package org.gbif.portal.util.mhf.message;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.util.propertystore.MisconfiguredPropertyException;
import org.gbif.portal.util.propertystore.PropertyNotFoundException;
import org.gbif.portal.util.propertystore.PropertyStore;

/**
 * Utilities for accessing concepts within a Message 
 * 
 * @author Tim Robertson
 */
public class MessageUtils {
	/**
	 * Logger
	 */
	protected static Log logger = LogFactory.getLog(MessageUtils.class);
	
	/**
	 * The property store key for the message factory
	 * Defaults to MESSAGE.FACTORY
	 */
	protected String psKeyMessageFactory = "MESSAGE.FACTORY";
	
	/**
	 * The property store
	 */
	protected PropertyStore propertyStore;
	
	/**
	 * Utility to build a concrete Message wrapper around the raw data.
	 * The Message is created using the MessageFactory that is configured
	 * in the property store for the supplied namespace.
	 * 
	 * @param rawDataStream Stream to the raw data to be wrapped
	 * @param namespace To locate the MessageFactory in the property store
	 * @return A built message 
	 * @throws PropertyNotFoundException Should the Property not be found in the 
	 * property store
	 * @throws MisconfiguredPropertyException Should there be a misconfiguration in 
	 * the property store of the MessageFactory
	 * @throws MessageParseException Should the Raw Data not be parsable by the 
	 * configured Message Factory
	 */
	public Message buildMessage(InputStream rawDataStream, String namespace) throws PropertyNotFoundException, MisconfiguredPropertyException, MessageParseException {
		Object messageFactoryObject = propertyStore.getProperty(namespace, getPsKeyMessageFactory());
		if (!(messageFactoryObject instanceof MessageFactory)) {
			throw new MisconfiguredPropertyException(namespace, getPsKeyMessageFactory(), MessageFactory.class, messageFactoryObject.getClass());
		}	
		MessageFactory messageFactory = (MessageFactory) messageFactoryObject;
		return messageFactory.build(rawDataStream);
	}
	
	/**
	 * Extracts a sub message from a message. 
	 * @param message To extract from
	 * @param psNamespace The PS namespace to use
	 * @param psKey The key within the namespace
	 * @param Indicator whether this should throw an error if the PS does have the concept mapped
	 * If false, then null is returned
	 * @return The sub message or null
	 * @throws PropertyNotFoundException Should the property not be found in the PS
	 * @throws MisconfiguredPropertyException Should the property not be a MessageAccessor
	 * @throws MessageAccessException Should the message part be inaccessible
	 */
	public Message extractSubMessage(Message message, String psNamespace, String psKey, boolean errorIfNotMapped) throws PropertyNotFoundException, MisconfiguredPropertyException, MessageAccessException {
		try {
			MessageAccessor messageAccessor = (MessageAccessor) propertyStore.getProperty(psNamespace, psKey, MessageAccessor.class);
			Object result = messageAccessor.invoke(message);
			if (result instanceof Message) {
				return (Message) result;
			} else {
				return null;
			}
		} catch (PropertyNotFoundException e) {
			if (errorIfNotMapped) {
				throw e;
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Extracts a list of sub messages from a message.
	 * @param message To extract from
	 * @param psNamespace The PS namespace to use
	 * @param psKey The key within the namespace
	 * @param Indicator whether this should throw an error if the PS does have the concept mapped
	 * If false, then an empty list is returned
	 * @return The sub message list or empty list
	 * @throws PropertyNotFoundException Should the property not be found in the PS
	 * @throws MisconfiguredPropertyException Should the property not be a MessageAccessor
	 * @throws MessageAccessException Should the message part be inaccessible
	 */
	@SuppressWarnings("unchecked")
	public List<Message> extractSubMessageList(Message message, String psNamespace, String psKey, boolean errorIfNotMapped) throws PropertyNotFoundException, MisconfiguredPropertyException, MessageAccessException {
		try {
			MessageAccessor messageAccessor = (MessageAccessor) propertyStore.getProperty(psNamespace, psKey, MessageAccessor.class);
			Object result = messageAccessor.invoke(message);
			if (result instanceof List) {
				return (List<Message>) result;
			} else {
				throw new MisconfiguredPropertyException("Namespace[" + psNamespace + "], key[" + psKey + "] when invoked is returning[" + result.getClass() + "] but should be a List<Message>");
			}
		} catch (PropertyNotFoundException e) {
			if (errorIfNotMapped) {
				throw e;
			} else {
				return new LinkedList<Message>();
			}
		}
	}	
	
	/**
	 * Extracts a list of Strings from a message.
	 * @param message To extract from
	 * @param psNamespace The PS namespace to use
	 * @param psKey The key within the namespace
	 * @param Indicator whether this should throw an error if the PS does have the concept mapped
	 * If false, then an empty list is returned
	 * @return The list of strings or empty list
	 * @throws PropertyNotFoundException Should the property not be found in the PS
	 * @throws MisconfiguredPropertyException Should the property not be a MessageAccessor
	 * @throws MessageAccessException Should the message part be inaccessible
	 */
	@SuppressWarnings("unchecked")
	public List<String> extractConceptAsStringList(Message message, String psNamespace, String psKey, boolean errorIfNotMapped) throws PropertyNotFoundException, MisconfiguredPropertyException, MessageAccessException {
		try {
			MessageAccessor messageAccessor = (MessageAccessor) propertyStore.getProperty(psNamespace, psKey, MessageAccessor.class);
			Object result = messageAccessor.invoke(message);
			if (result instanceof List) {
				return (List<String>) result;
			} else {
				throw new MisconfiguredPropertyException("Namespace[" + psNamespace + "], key[" + psKey + "] when invoked is returning[" + result.getClass() + "] but should be a List<String>");
			}
		} catch (PropertyNotFoundException e) {
			if (errorIfNotMapped) {
				throw e;
			} else {
				return new LinkedList<String>();
			}
		}
	}

	/**
	 * Returns true is property is supported.
	 * 
	 * @param psNamespace
	 * @param psKey
	 * @return
	 */
	public boolean isPropertyMapped(String psNamespace, String psKey){
		return propertyStore.propertySupported(psNamespace, psKey);
	}
	
	/**
	 * Returns true is property is supported.
	 * 
	 * @param psNamespace
	 * @param psKey
	 * @return
	 */
	public boolean isPropertyMapped(List<String> psNamespaces, String psKey){
		return propertyStore.propertySupported(psNamespaces, psKey);
	}	
	
	/**
	 * Extracts an unknown concept from a message.
	 * @param message To extract from
	 * @param psNamespace The PS namespace to use
	 * @param psKey The key within the namespace
	 * @param Indicator whether this should throw an error if the PS does have the concept mapped
	 * If false, then an null is returned
	 * @return The concept or null
	 * @throws PropertyNotFoundException Should the property not be found in the PS
	 * @throws MisconfiguredPropertyException Should the property not be a MessageAccessor
	 * @throws MessageAccessException Should the message part be inaccessible
	 */
	@SuppressWarnings("unchecked")
	public Object extractConcept(Message message, String psNamespace, String psKey, boolean errorIfNotMapped) throws PropertyNotFoundException, MisconfiguredPropertyException, MessageAccessException {
		try {
			MessageAccessor messageAccessor = (MessageAccessor) propertyStore.getProperty(psNamespace, psKey, MessageAccessor.class);
			return messageAccessor.invoke(message);
			
		} catch (PropertyNotFoundException e) {
			if (errorIfNotMapped) {
				throw e;
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Extracts an unknown concept from a message.
	 * @param message To extract from
	 * @param psNamespaces The PS namespaces to use
	 * @param psKey The key within the namespace
	 * @param Indicator whether this should throw an error if the PS does have the concept mapped
	 * If false, then an null is returned
	 * @return The concept or null
	 * @throws PropertyNotFoundException Should the property not be found in the PS
	 * @throws MisconfiguredPropertyException Should the property not be a MessageAccessor
	 * @throws MessageAccessException Should the message part be inaccessible
	 */
	@SuppressWarnings("unchecked")
	public Object extractConcept(Message message, List<String> psNamespaces, String psKey, boolean errorIfNotMapped) throws PropertyNotFoundException, MisconfiguredPropertyException, MessageAccessException {
		try {
			MessageAccessor messageAccessor = (MessageAccessor) propertyStore.getProperty(psNamespaces, psKey, MessageAccessor.class);
			return messageAccessor.invoke(message);
			
		} catch (PropertyNotFoundException e) {
			if (errorIfNotMapped) {
				throw e;
			}
		} catch (Exception e) {
			if (errorIfNotMapped) {
				throw new MessageAccessException("Unable to extract a required value [" + psKey + "] from the message using namespaces [" + psNamespaces + "]", e);
			}
		}
		return null;
	}	
	
	/**
	 * Extracts the concept as a String
	 * @param message To extract from
	 * @param psNamespace The PS namespace in use
	 * @param psKey The key to get the accessor in the PS
	 * @param errorIfNotMapped If it is not mapped then should exception be thrown or return null
	 * @return The concept as a String or Null
	 * @throws PropertyNotFoundException Should the property not be found in the PS
	 * @throws MisconfiguredPropertyException Should the property not be a MessageAccessor
	 * @throws MessageAccessException Should the message part be inaccessible
	 */
	public String extractConceptAsString(Message message, String psNamespace, String psKey, boolean errorIfNotMapped) throws PropertyNotFoundException, MisconfiguredPropertyException, MessageAccessException {
		try {
			MessageAccessor messageAccessor = (MessageAccessor) propertyStore.getProperty(psNamespace, psKey, MessageAccessor.class);
			Object result = messageAccessor.invoke(message);
			return result.toString();
		} catch (PropertyNotFoundException e) {
			if (errorIfNotMapped) {
				throw e;
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Extracts a sub message from a message. 
	 * @param message To extract from
	 * @param psNamespaces The PS namespaces to use
	 * @param psKey The key within the namespace
	 * @param Indicator whether this should throw an error if the PS does have the concept mapped
	 * If false, then null is returned
	 * @return The sub message or null
	 * @throws PropertyNotFoundException Should the property not be found in the PS
	 * @throws MisconfiguredPropertyException Should the property not be a MessageAccessor
	 * @throws MessageAccessException Should the message part be inaccessible
	 */
	public Message extractSubMessage(Message message, List<String> psNamespaces, String psKey, boolean errorIfNotMapped) throws PropertyNotFoundException, MisconfiguredPropertyException, MessageAccessException {
		try {
			MessageAccessor messageAccessor = (MessageAccessor) propertyStore.getProperty(psNamespaces, psKey, MessageAccessor.class);
			Object result = messageAccessor.invoke(message);
			if (result instanceof Message) {
				return (Message) result;
			} else {
				return null;
			}
		} catch (PropertyNotFoundException e) {
			if (errorIfNotMapped) {
				throw e;
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Extracts a list of sub messages from a message.
	 * @param message To extract from
	 * @param psNamespaces The PS namespaces to use
	 * @param psKey The key within the namespace
	 * @param Indicator whether this should throw an error if the PS does have the concept mapped
	 * If false, then an empty list is returned
	 * @return The sub message or null
	 * @throws PropertyNotFoundException Should the property not be found in the PS
	 * @throws MisconfiguredPropertyException Should the property not be a MessageAccessor
	 * @throws MessageAccessException Should the message part be inaccessible
	 */
	@SuppressWarnings("unchecked")
	public List<Message> extractSubMessageList(Message message, List<String> psNamespaces, String psKey, boolean errorIfNotMapped) throws PropertyNotFoundException, MisconfiguredPropertyException, MessageAccessException {
		try {
			MessageAccessor messageAccessor = (MessageAccessor) propertyStore.getProperty(psNamespaces, psKey, MessageAccessor.class);
			Object result = messageAccessor.invoke(message);
			if (result instanceof List) {
				return (List<Message>) result;
			} else {
				throw new MisconfiguredPropertyException("Namespace[" + psNamespaces + "], key[" + psKey + "] when invoked is returning[" + result.getClass() + "] but should be a List<Message>");
			}
		} catch (PropertyNotFoundException e) {
			if (errorIfNotMapped) {
				throw e;
			} else {
				return new LinkedList<Message>();
			}
		}
	}
	
	/**
	 * Extracts the concept as a String
	 * @param message To extract from
	 * @param psNamespaces The PS namespaces to use
	 * @param psKey The key to get the accessor in the PS
	 * @param errorIfNotMapped If it is not mapped then should exception be thrown or return null
	 * @return The concept as a String or Null
	 * @throws PropertyNotFoundException Should the property not be found in the PS
	 * @throws MisconfiguredPropertyException Should the property not be a MessageAccessor
	 * @throws MessageAccessException Should the message part be inaccessible
	 */
	public String extractConceptAsString(Message message, List<String> psNamespaces, String psKey, boolean errorIfNotMapped) throws PropertyNotFoundException, MisconfiguredPropertyException, MessageAccessException {
		try {
			MessageAccessor messageAccessor = (MessageAccessor) propertyStore.getProperty(psNamespaces, psKey, MessageAccessor.class);
			Object result = messageAccessor.invoke(message);
			return result.toString();
		} catch (PropertyNotFoundException e) {
			if (errorIfNotMapped) {
				throw e;
			}
		} catch (Exception e) {
			if (errorIfNotMapped) {
				throw new MessageAccessException("Unable to extract a required value [" + psKey + "] from the message using namespaces [" + psNamespaces + "]", e);
			}
		}
		return null;
	}
	
	/**
	 * Extracts the concept as a Integer
	 * @param message To extract from
	 * @param psNamespaces The PS namespaces to use
	 * @param psKey The key to get the accessor in the PS
	 * @param errorIfNotMapped If it is not mapped then should exception be thrown or return null
	 * @return The concept as a String or Null
	 * @throws PropertyNotFoundException Should the property not be found in the PS
	 * @throws MisconfiguredPropertyException Should the property not be a MessageAccessor
	 * @throws MessageAccessException Should the message part be inaccessible
	 */
	public Integer extractConceptAsInteger(Message message, List<String> psNamespaces, String psKey, boolean errorIfNotMapped) throws PropertyNotFoundException, MisconfiguredPropertyException, MessageAccessException {
		String conceptAsString = extractConceptAsString(message, psNamespaces, psKey, errorIfNotMapped);
		try {
			return Integer.parseInt(conceptAsString);
		} catch (NumberFormatException e){
			if(errorIfNotMapped){
				throw new MessageAccessException("Unable to extract a required value [" + psKey + "] from the message using namespaces [" + psNamespaces + "] due to badly formatted number", e);
			}
		}
		return null;
	}
	
	/**
	 * @return Returns the propertyStore.
	 */
	public PropertyStore getPropertyStore() {
		return propertyStore;
	}

	/**
	 * @param propertyStore The propertyStore to set.
	 */
	public void setPropertyStore(PropertyStore propertyStore) {
		this.propertyStore = propertyStore;
	}

	/**
	 * @return Returns the psKeyMessageFactory.
	 */
	public String getPsKeyMessageFactory() {
		return psKeyMessageFactory;
	}

	/**
	 * @param psKeyMessageFactory The psKeyMessageFactory to set.
	 */
	public void setPsKeyMessageFactory(String psKeyMessageFactory) {
		this.psKeyMessageFactory = psKeyMessageFactory;
	}	
}