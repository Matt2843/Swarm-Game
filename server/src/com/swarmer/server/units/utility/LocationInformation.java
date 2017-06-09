package com.swarmer.server.units.utility;

import java.io.Serializable;

public class LocationInformation implements Serializable {

	private String serverUnitDescription;
	private String serverUnitIp;
	private int serverUnitPort;
	private String serverUUID;

	public LocationInformation(String serverUnitDescription, String serverUnitIp, int serverUnitPort, String serverUUID) {
		setServerUnitDescription(serverUnitDescription);
		setServerUnitIp(serverUnitIp);
		setServerUnitPort(serverUnitPort);
		setServerUUID(serverUUID);
	}

	public String getServerUUID() {
		return serverUUID;
	}

	public void setServerUUID(String serverUUID) {
		this.serverUUID = serverUUID;
	}

	public String getInetAddress() {
		return "/" + serverUnitIp + ":" + serverUnitPort;
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
		this.serverUnitIp = serverUnitIp.replace("/", "");
	}

	public int getServerUnitPort() {
		return serverUnitPort;
	}

	public void setServerUnitPort(int serverUnitPort) {
		this.serverUnitPort = serverUnitPort;
	}

	@Override
	public int hashCode() {
		return serverUnitIp.hashCode() + serverUnitPort * 17 + serverUUID.hashCode();
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
