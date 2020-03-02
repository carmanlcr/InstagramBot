package com.instagram.Controlador;


import java.util.concurrent.ThreadLocalRandom;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

public class IniciaSesion {
	
	private String username;
	private String password;
	private Screen screen;
	private RobotController robot;
	
	public IniciaSesion(String username, String password, Screen screen) {
		this.username = username;
		this.password = password;
		this.screen = screen;
	}
	
	public void init() throws InterruptedException, FindFailed {
		System.out.println("Ir a la pagina de inicio de sesi�n");
		//Insertar el usuario
		Thread.sleep(getNumberRandomForSecond(1256, 1487));
		robot = new RobotController();
		if(screen.exists("C:\\ImagenesSikuli\\username-Instagram.png") != null) {
			screen.click("C:\\ImagenesSikuli\\username-Instagram.png");
			Thread.sleep(889);
			robot.inputWrite(username);
			//Insertar el password
			Thread.sleep(getNumberRandomForSecond(465, 897));
			System.out.println("Escribir contrasena");
			robot.pressTab();
			Thread.sleep(getNumberRandomForSecond(165, 297));
			robot.inputWrite(password);
			//Presionar boton de inicio de sesi�n
			Thread.sleep(getNumberRandomForSecond(465, 898));
			System.out.println("Pulsar iniciar sesion");
			robot.enter();
		}else {
			if(screen.exists("C:\\ImagenesSikuli\\instagram_install.png") != null) {
				//ponerse en el input de usuario
				robot.dimensions(598, 580);
				robot.clickPressed();
				System.out.println("Escribir usuario");
				robot.inputWrite(username);
				//Insertar el password
				Thread.sleep(getNumberRandomForSecond(465, 897));
				System.out.println("Escribir contrasena");
				robot.pressTab();
				Thread.sleep(getNumberRandomForSecond(165, 297));
				robot.inputWrite(password);
				//Presionar boton de inicio de sesi�n
				Thread.sleep(getNumberRandomForSecond(465, 898));
				System.out.println("Pulsar iniciar sesion");
				robot.enter();
			}else {
				//ponerse en el input de usuario
				robot.dimensions(598, 560);
				robot.clickPressed();
				System.out.println("Escribir usuario");
				robot.inputWrite(username);
				//Insertar el password
				Thread.sleep(getNumberRandomForSecond(465, 897));
				System.out.println("Escribir contrasena");
				robot.pressTab();
				Thread.sleep(getNumberRandomForSecond(165, 297));
				robot.inputWrite(password);
				//Presionar boton de inicio de sesi�n
				Thread.sleep(getNumberRandomForSecond(465, 689));
				System.out.println("Pulsar iniciar sesion");
				robot.enter();
			}
		}
		
		

	}

	
	private static int getNumberRandomForSecond(int init, int fin) {
		return ThreadLocalRandom.current().nextInt(init, fin + 1);  
	}

}
