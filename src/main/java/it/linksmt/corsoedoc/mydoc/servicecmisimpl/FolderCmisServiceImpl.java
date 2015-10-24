package it.linksmt.corsoedoc.mydoc.servicecmisimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.springframework.stereotype.Service;

import it.linksmt.corsoedoc.mydoc.service.FolderService;


@Service
public class FolderCmisServiceImpl implements FolderService {
	
	Session s = ConnectorCmiserviceImpl.getSession();

	@Override
	public Folder getFolderByPath(String path) {
		
		Folder myFolder= (Folder) s.getObjectByPath(path);
		
		return myFolder;
		
	}

	@Override
	public Folder getFolderById(String id) {

		Folder myFolder = (Folder) s.getObject(id);

		return myFolder;
		
	}

	@Override
	public Folder getRootFolder() {

		Folder rootFolder = (Folder) s.getRootFolder();

		return rootFolder;
	}

	@Override
	public List<CmisObject> getChildrenObject(Folder folder) {
		
		List<CmisObject> sons = new ArrayList<CmisObject>();
		
		for(CmisObject c: folder.getChildren()){
			sons.add(c);
		}
		
		return sons;
	}
	
	@Override
	public void createFolder(String id, String name){
		
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("cmis:objectTypeId", "cmis:folder");
		
		Folder thisLocation = getFolderById(id);
		
		properties.put("cmis:name", name);
		thisLocation.createFolder(properties);
		
	}
	
	@Override
	public void deleteFolder(String id){
		Folder folder = (Folder) s.getObject(id);
		
		folder.deleteTree(true, UnfileObject.DELETE, true);
		
	}
	
	@Override
	public void renameFolder(String id, String name, String idParentFolder) throws Exception{
		
		Folder folder = (Folder) s.getObject(id);
		Folder parentFolder = (Folder) s.getObject(idParentFolder);
		
		for(CmisObject c: parentFolder.getChildren()){
			if(c.getName().equalsIgnoreCase(name)){
				throw new Exception("Esiste già un file con questo nome");
			}
		}
		
		folder.rename(name);
		
	}

}
