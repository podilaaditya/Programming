
package com.innominds.innocameraservice;



public interface Observable {
	public void addObserver(Observer obs);
	public void deleteObserver(Observer obs);
}
