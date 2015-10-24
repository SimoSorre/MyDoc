package it.linksmt.corsoedoc.mydoc.servicecmisimpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.springframework.stereotype.Service;

import it.linksmt.corsoedoc.mydoc.service.DocumentService;

@Service
public class DocumentCmisServiceImpl implements DocumentService {
	
	Session s = ConnectorCmiserviceImpl.getSession();

	@Override
	public CmisObject getObjectByPath(String path) {
		
		CmisObject obj = s.getObjectByPath(path);
		
		return obj;
	}

	@Override
	public CmisObject getObjectById(String id) {
		
		CmisObject obj = s.getObject(id);
		
		return obj;
	}

	@Override
	public List<Property<?>> getProperties(CmisObject object) {
		
		List<Property<?>> propList = new ArrayList<Property<?>>();
		
		for(Property<?> prop : object.getProperties()){
			propList.add(prop);
		}
		
		return propList;
	}
	
	@Override
	public void createDocument(String idFolder, String nome, String text) {

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("cmis:objectTypeId", "cmis:document");
		properties.put("cmis:name", nome);
		byte[] content = text.getBytes();
		InputStream stream = new ByteArrayInputStream(content);
		ContentStream contentStream = new ContentStreamImpl(nome, (BigInteger.valueOf(content.length)), "text/plain", stream);

		Folder thisLocation = (Folder) s.getObject(idFolder);
		thisLocation.createDocument(properties, contentStream, VersioningState.MAJOR);

	}
	
	@Override
	public void deleteDocument(String id){
		Document document = (Document) s.getObject(id);
		
		document.delete(true);
	}
	
	@Override
	public void renameDocument(String id, String name, String idParentFolder) throws Exception{
		
		Document document = (Document) s.getObject(id);
		Folder parentFolder = (Folder) s.getObject(idParentFolder);
		
		for(CmisObject c: parentFolder.getChildren()){
			if(c.getName().equalsIgnoreCase(name)){
				throw new Exception("Esiste già un file con questo nome");
			}
		}
		
		document.rename(name);
		
	}

	
}
