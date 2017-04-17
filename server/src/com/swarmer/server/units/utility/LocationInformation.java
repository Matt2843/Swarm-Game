package com.swarmer.server.units.utility;

/**
 * Created by Matt on 04/17/2017.
 */
public class LocationInformation {

	private String serverUnitDescription;
	private String serverUnitIp;
	private int serverUnitPort;

	public LocationInformation(String serverUnitDescription, String serverUnitIp, int serverUnitPort) {
		this.serverUnitDescription = serverUnitDescription;
		this.serverUnitIp = serverUnitIp;
		this.serverUnitPort = serverUnitPort;
	}

	public String getServerUnitDescription() {
		return serverUnitDescription;
	}

	public void setServerUnitDescription(String serverUnitDescription) {
		this.serverUnitDescription = serverUnitDescription;
	}

	public String getServerUnitIp() {
		return serverUnitIp;
	}

	public void setServerUnitIp(String serverUnitIp) {
		this.serverUnitIp = serverUnitIp;
	}

	public int getServerUnitPort() {
		return serverUnitPort;
	}

	public void setServerUnitPort(int serverUnitPort) {
		this.serverUnitPort = serverUnitPort;
	}
}
