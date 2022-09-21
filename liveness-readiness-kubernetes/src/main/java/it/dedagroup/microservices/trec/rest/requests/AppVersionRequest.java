package it.dedagroup.microservices.trec.rest.requests;

import java.io.Serializable;

import it.dedagroup.microservices.trec.appversionchecker.enums.Platform;

public class AppVersionRequest implements Serializable {
	
	private static final long serialVersionUID = -5394614144186683206L;
	
	private Platform platform;
	private String version;
	
	public AppVersionRequest() {
		super();
	}
	
	public AppVersionRequest(Platform platform, String version) {
		super();
		this.platform = platform;
		this.version = version;
	}
	
	public Platform getPlatform() {
		return platform;
	}
	public void setPlatform(Platform platform) {
		this.platform = platform;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

}
