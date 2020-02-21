package com.instagram.Controlador;

import org.sikuli.script.Screen;

/**
 * 
 * Se movio al proyecto Configurations 
 * 
 * @deprecated
 * @author Luis Morales
 *
 */
public class SikuliTest implements Runnable {
	
	private Screen screen;
	
	public SikuliTest() {
		// TODO Auto-generated constructor stub
		
	}
	
	
	public Screen getScreen() {
		return this.screen;
	}
	
	public void setScreen(Screen screen) {
		this.screen = screen;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		screen = new Screen();
	}
}
