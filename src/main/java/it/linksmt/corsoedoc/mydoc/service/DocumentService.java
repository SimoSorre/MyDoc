package it.linksmt.corsoedoc.mydoc.service;

import java.util.List;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Property;

public interface DocumentService {
	
	CmisObject getObjectByPath(String path);
	CmisObject getObjectById(String id);
	List<Property<?>> getProperties(CmisObject object);
	void createDocument(String idFolder, String nome, String text);
	void deleteDocument(String id);
	void renameDocument(String id, String name, String idFolder) throws Exception;
	
	
}
