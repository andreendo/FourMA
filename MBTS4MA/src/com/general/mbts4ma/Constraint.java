package com.general.mbts4ma;

public class Constraint {
	private String id;
	private String logicalConnector;
	private EventInstance left;
	private EventInstance right;

	public Constraint() {
	}

	public Constraint(String id, EventInstance leftEvent, EventInstance rightEvent, String logicalConnector) {
		this.id = id;
		this.left = leftEvent;
		this.right = rightEvent;
		this.logicalConnector = logicalConnector;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogicalConnector() {
		return logicalConnector;
	}

	public void setLogicalConnector(String logicalConnector) {
		this.logicalConnector = logicalConnector;
	}

	public EventInstance getLeft() {
		return left;
	}

	public void setLeft(EventInstance left) {
		this.left = left;
	}

	public EventInstance getRight() {
		return right;
	}

	public void setRight(EventInstance right) {
		this.right = right;
	}

}
