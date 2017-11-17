package de.bergtiger.tigerquiz.listener;

import java.util.ArrayList;
import java.util.List;

import de.bergtiger.tigerquiz.TigerQuiz;

public class MyListenerOverview {

	private TigerQuiz plugin;
	private List<MyListener> listener = new ArrayList<MyListener>();
	private MyPlayerTabListener myPlayerTabListener;
	
	public MyListenerOverview(TigerQuiz plugin) {
		this.plugin = plugin;
		this.myPlayerTabListener = new MyPlayerTabListener(this.plugin);
		this.myPlayerTabListener.aktivate();
		this.listener.add(new MyInventory(this.plugin));
	}
	
	public void aktivate() {
		for(MyListener listener: this.listener) {
			listener.aktivate();
		}
	}
	
	public void deaktivate() {
		for(MyListener listener: this.listener) {
			listener.disable();
		}
	}
	
}
