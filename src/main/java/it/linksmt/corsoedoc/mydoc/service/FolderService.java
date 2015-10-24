package it.linksmt.corsoedoc.mydoc.service;

import java.util.List;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;

public interface FolderService {
	
	Folder getRootFolder();
	Folder getFolderByPath(String path);
	Folder getFolderById(String id);
	List<CmisObject> getChildrenObject(Folder folder);
	void createFolder(String id, String name);
	void deleteFolder(String id);
	void renameFolder(String id, String name, String idParentFolder) throws Exception;
	
}
