package com.swarmer.server.units.utility;

import java.io.Serializable;

public class LocationInformation implements Serializable {

	private String serverUnitDescription;
	private String serverUnitIp;
	private int serverUnitPort;

	public LocationInformation(String serverUnitDescription, String serverUnitIp, int serverUnitPort) {
		setServerUnitDescription(serverUnitDescription);
		setServerUnitIp(serverUnitIp);
		setServerUnitPort(serverUnitPort);
	}

	public String getInetAddress() {
		return "/" + serverUnitIp + ":" + serverUnitPort;
	}

	public void setServerUnitDescription(String serverUnitDescription) {
		this.serverUnitDescription = serverUnitDescription;
	}

	public String getServerUnitIp() {
		return serverUnitIp;
	}

	public void setServerUnitIp(String serverUnitIp) {
		this.serverUnitIp = serverUnitIp.replace("/", "");
	}

	public int getServerUnitPort() {
		return serverUnitPort;
	}

	public void setServerUnitPort(int serverUnitPort) {
		this.serverUnitPort = serverUnitPort;
	}

	@Override
	public String toString() {
		return "LocationInformation{" +
				"serverUnitDescription='" + serverUnitDescription + '\'' +
				", serverUnitIp='" + serverUnitIp + '\'' +
				", serverUnitPort=" + serverUnitPort +
				'}';
	}
}
