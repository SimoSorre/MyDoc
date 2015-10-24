package it.linksmt.corsoedoc.mydoc.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.impl.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import it.linksmt.corsoedoc.mydoc.servicecmisimpl.DocumentCmisServiceImpl;
import it.linksmt.corsoedoc.mydoc.servicecmisimpl.FolderCmisServiceImpl;

@Controller
@RequestMapping(value = "/")
public class MyDocController {

	@Autowired
	FolderCmisServiceImpl folderCmis;

	@Autowired
	DocumentCmisServiceImpl documentCmis;

	@RequestMapping
	public ModelAndView getIndex() {

		Folder folder = folderCmis.getFolderByPath("/Siti/my-doc/documentLibrary/");
		List<CmisObject> sons = folderCmis.getChildrenObject(folder);

		ModelAndView mv = new ModelAndView("index");

		mv.addObject("folder", folder);
		mv.addObject("sons", sons);

		return mv;
	}

	@RequestMapping(value = "detail/{id}")
	public ModelAndView getDetails(@PathVariable String id) {

		Folder folder = folderCmis.getFolderById(id);
		List<CmisObject> sons = folderCmis.getChildrenObject(folder);

		ModelAndView mv = new ModelAndView("index");

		mv.addObject("folder", folder);
		mv.addObject("sons", sons);

		return mv;
	}

	@RequestMapping(value = "property/{id}")
	public ModelAndView getProperties(@PathVariable String id) {

		CmisObject document = documentCmis.getObjectById(id);
		List<Property<?>> propList = documentCmis.getProperties(document);

		ModelAndView mv = new ModelAndView("property");

		mv.addObject("document", document);
		mv.addObject("propList", propList);

		return mv;
	}

	@RequestMapping(value = "newfolder")
	public ModelAndView createFolder(@RequestParam("idFolder") String id, @RequestParam("folderName") String name) {

		folderCmis.createFolder(id, name);

		Folder folder = folderCmis.getFolderById(id);
		List<CmisObject> sons = folderCmis.getChildrenObject(folder);

		ModelAndView mv = new ModelAndView("redirect:/detail/" + id);
		mv.addObject("folder", folder);
		mv.addObject("sons", sons);

		return mv;
	}

	@RequestMapping(value = "newdocument")
	public ModelAndView createDocument(@RequestParam("idFolder") String id, @RequestParam("documentName") String name,
			@RequestParam("documentText") String text) {

		documentCmis.createDocument(id, name, text);

		Folder folder = folderCmis.getFolderById(id);
		List<CmisObject> sons = folderCmis.getChildrenObject(folder);

		ModelAndView mv = new ModelAndView("redirect:/detail/" + id);
		mv.addObject("folder", folder);
		mv.addObject("sons", sons);

		return mv;

	}

	@RequestMapping(value = "download/{id}")
	public ModelAndView download(@PathVariable("id") String documentId, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		Document doc = (Document) documentCmis.getObjectById(documentId);

		String filename = doc.getName();
		System.out.println("FileController -> getStream: filename=" + filename);
		String mime_type = null;
		Property<?> property = doc.getProperty(PropertyIds.CONTENT_STREAM_MIME_TYPE);
		mime_type = property.getValueAsString();
		response.setContentType(mime_type);
		System.out.println("FileController -> getStream: mime type:=" + mime_type);
		int size = 0;
		Property<?> size_prop = doc.getProperty(PropertyIds.CONTENT_STREAM_LENGTH);
		size = Integer.parseInt(size_prop.getValueAsString());
		response.setContentLength(size);
		System.out.println("FileController -> getStream: size:=" + size);

		String inline_p = request.getParameter("inline");
		boolean inline = Boolean.parseBoolean(inline_p);

		if (!inline) {
			/* attachment */
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		} else {
			/* inline */
			response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
		}

		ContentStream cs = doc.getContentStream();
		InputStream is = cs.getStream();
		try {
			IOUtils.copy(is, response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/");

	}

	@RequestMapping(value = "delete/{id}/{idFolder}")
	public ModelAndView deleteObject(@PathVariable("id") String id, @PathVariable("idFolder") String idFolder) {

		if (documentCmis.getObjectById(id).getClass().getSimpleName().equals("FolderImpl")) {
			folderCmis.deleteFolder(id);
		} else {
			documentCmis.deleteDocument(id);
		}

		ModelAndView mv = new ModelAndView("redirect:/detail/" + idFolder);

		mv.addObject("folder", folderCmis.getFolderById(idFolder));
		mv.addObject("sons", folderCmis.getChildrenObject(folderCmis.getFolderById(idFolder)));

		return mv;

	}

	@RequestMapping(value = "rename")
	public ModelAndView renameObject(@RequestParam("object") String id, @RequestParam("idFolder") String idFolder,
			@RequestParam("newName") String newName) {

		if (documentCmis.getObjectById(id).getClass().getSimpleName().equals("FolderImpl")) {
			try {

				folderCmis.renameFolder(id, newName, idFolder);
				
			} catch (Exception e) {

				// e.printStackTrace();

				// System.out.println("**************************************CATCH");

				ModelAndView mv = new ModelAndView("index");
				mv.addObject("folder", folderCmis.getFolderById(idFolder));
				mv.addObject("sons", folderCmis.getChildrenObject(folderCmis.getFolderById(idFolder)));
				mv.addObject("errore", e.getMessage()+": "+newName);

				return mv;
			}
		} else {
			try {
				documentCmis.renameDocument(id, newName, idFolder);
			} catch (Exception e) {
				
				ModelAndView mv = new ModelAndView("index");
				mv.addObject("folder", folderCmis.getFolderById(idFolder));
				mv.addObject("sons", folderCmis.getChildrenObject(folderCmis.getFolderById(idFolder)));
				mv.addObject("errore", e.getMessage()+": "+newName);

				return mv;
				
			}
		}

		//ModelAndView mv = new ModelAndView("redirect:/detail/" + idFolder);

//		mv.addObject("folder", folderCmis.getFolderById(idFolder));
//		mv.addObject("sons", folderCmis.getChildrenObject(folderCmis.getFolderById(idFolder)));
		
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("folder", folderCmis.getFolderById(idFolder));
		mv.addObject("sons", folderCmis.getChildrenObject(folderCmis.getFolderById(idFolder)));
		//mv.addObject("errore", e.getMessage()+": "+newName);

		return mv;
	}

}
