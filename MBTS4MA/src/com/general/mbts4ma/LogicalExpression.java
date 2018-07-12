package com.general.mbts4ma;

public class LogicalExpression {
    private EventInstance left;
    private EventInstance right;
    private String logicalConnector;
    
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
	
	public String getLogicalConnector() {
		return logicalConnector;
	}
	
	public void setLogicalConnector(String logicalConnector) {
		this.logicalConnector = logicalConnector;
	}
    
    
}
