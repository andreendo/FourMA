package com.general.mbts4ma.view.framework.vo;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class GraphProjectVO extends AbstractVO implements Serializable {

	private transient String fileSavingPath;

	private String name;
	private String description;
	private String androidProjectPath;

	private String graphXML;
	
	private boolean itsAndroidProject = false;
	
	private String framework;

	private String applicationPackage;
	private String mainTestingActivity;

	private Map<String, String> methodTemplatesByVertices;
	private Map<String, Map<String, String>> methodTemplatesPropertiesByVertices;
	private Map<String, String> edgeTemplates;

	private String user;
	private Date lastDate;

	public GraphProjectVO() {
		super();
		this.id = generateUUID();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAndroidProjectPath() {
		return this.androidProjectPath;
	}

	public void setAndroidProjectPath(String androidProjectPath) {
		this.androidProjectPath = androidProjectPath;
	}

	public String getGraphXML() {
		return this.graphXML;
	}

	public void setGraphXML(String graphXML) {
		this.graphXML = graphXML;
	}
	
	public String getFramework(){
		return this.framework;
	}
	
	public void setFramework(String framework){
		this.framework = framework;
	}
	
	public boolean getItsAndroidProject() {
		return this.itsAndroidProject;
	}

	public void setItsAndroidProject(boolean itsAndroidProject) {
		this.itsAndroidProject = itsAndroidProject;
	}

	public String getApplicationPackage() {
		return this.applicationPackage;
	}

	public void setApplicationPackage(String applicationPackage) {
		this.applicationPackage = applicationPackage;
	}

	public String getMainTestingActivity() {
		return this.mainTestingActivity;
	}

	public void setMainTestingActivity(String mainTestingActivity) {
		this.mainTestingActivity = mainTestingActivity;
	}

	public Map<String, String> getMethodTemplatesByVertices() {
		if (this.methodTemplatesByVertices == null) {
			this.methodTemplatesByVertices = new LinkedHashMap<String, String>();
		}

		return this.methodTemplatesByVertices;
	}

	public void updateMethodTemplateByVertice(String verticeId, String methodTemplate) {
		this.getMethodTemplatesByVertices().put(verticeId, methodTemplate);
	}

	public void removeMethodTemplateByVertice(String verticeId) {
		this.getMethodTemplatesByVertices().remove(verticeId);
	}

	public void setMethodTemplatesByVertices(Map<String, String> methodTemplatesByVertices) {
		this.methodTemplatesByVertices = methodTemplatesByVertices;
	}

	public Map<String, Map<String, String>> getMethodTemplatesPropertiesByVertices() {
		if (this.methodTemplatesPropertiesByVertices == null) {
			this.methodTemplatesPropertiesByVertices = new LinkedHashMap<String, Map<String, String>>();
		}

		return this.methodTemplatesPropertiesByVertices;
	}

	public void updateMethodTemplatePropertiesByVertice(String verticeId, Map<String, String> properties) {
		this.getMethodTemplatesPropertiesByVertices().put(verticeId, properties);
	}

	public void removeMethodTemplatePropertiesByVertice(String verticeId) {
		this.getMethodTemplatesPropertiesByVertices().remove(verticeId);
	}

	public void setMethodTemplatesPropertiesByVertices(Map<String, Map<String, String>> methodTemplatesPropertiesByVertices) {
		this.methodTemplatesPropertiesByVertices = methodTemplatesPropertiesByVertices;
	}

	public Map<String, String> getEdgeTemplates() {
		if (this.edgeTemplates == null) {
			this.edgeTemplates = new LinkedHashMap<String, String>();
		}

		return this.edgeTemplates;
	}

	public void updateEdgeTemplateByVertice(String verticeId, String edgeTemplate) {
		this.getEdgeTemplates().put(verticeId, edgeTemplate);
	}

	public void removeEdgeTemplateByVertice(String verticeId) {
		this.getEdgeTemplates().remove(verticeId);
	}

	public void setEdgeTemplates(Map<String, String> edgeTemplates) {
		this.edgeTemplates = edgeTemplates;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getLastDate() {
		return this.lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public String getFileSavingPath() {
		return this.fileSavingPath;
	}

	public String getFileSavingDirectory() {
		if (this.hasFileSavingPath()) {
			return this.fileSavingPath.substring(0, this.fileSavingPath.lastIndexOf(File.separator));
		}

		return System.getProperty("user.home");
	}

	public boolean hasFileSavingPath() {
		return this.fileSavingPath != null && !"".equalsIgnoreCase(this.fileSavingPath);
	}

	public void setFileSavingPath(String fileSavingPath) {
		this.fileSavingPath = fileSavingPath;
	}

}
