package it.linksmt.corsoedoc.mydoc.servicecmisimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.springframework.stereotype.Service;

import it.linksmt.corsoedoc.mydoc.service.ConnectorService;

@Service
public class ConnectorCmiserviceImpl implements ConnectorService{

	private static Session s=null;
	
	private ConnectorCmiserviceImpl() {

		Map<String, String> parameter = new HashMap<String, String>();
		parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
		parameter.put(SessionParameter.ATOMPUB_URL, "http://127.0.0.1:8080/alfresco/api/-default-/public/cmis/versions/1.0/atom");

		parameter.put(SessionParameter.USER, "admin");
		parameter.put(SessionParameter.PASSWORD, "root");

		SessionFactory factory = SessionFactoryImpl.newInstance();
		List<Repository> repositories = factory.getRepositories(parameter);
		s = repositories.get(0).createSession();
		
	}
	
	
	
	public static Session getSession() {
		
		if(s==null){
			new ConnectorCmiserviceImpl();
		}
		
		return s;
	}


}
