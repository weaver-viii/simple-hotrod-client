package org.everythingjboss.jdg;

import java.io.Serializable;

public class DummyClass implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -235277260571551968L;
	private byte [] blob;
	
	public DummyClass() {
		this.blob = new byte[Integer.parseInt(System.getProperty("bytes"))];
	}
}
