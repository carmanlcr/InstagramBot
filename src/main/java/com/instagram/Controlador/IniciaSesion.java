package com.instagram.Controlador;


import java.util.concurrent.ThreadLocalRandom;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

public class IniciaSesion {
	
	private String username;
	private String password;
	private Screen screen;
	
	public IniciaSesion(String username, String password, Screen screen) {
		this.username = username;
		this.password = password;
		this.screen = screen;
	}
	
	public int init() throws InterruptedException {
		System.out.println("Ir a la pagina de inicio de sesi�n");
		//Insertar el usuario
		Thread.sleep(getNumberRandomForSecond(1256, 1487));
		configurations.controller.RobotController robot = new configurations.controller.RobotController();
		if(screen.exists("C:\\ImagenesSikuli\\username-Instagram.png") != null) {
			clickException("C:\\ImagenesSikuli\\username-Instagram.png");
			Thread.sleep(1889);
			System.out.println("Escribir usuario");
			robot.inputWrite(username);
			//Insertar el password
			Thread.sleep(getNumberRandomForSecond(465, 897));
			System.out.println("Escribir contrasena");
			robot.pressTab();
			Thread.sleep(getNumberRandomForSecond(565, 897));
			robot.inputWrite(password);
			//Presionar boton de inicio de sesi�n
			Thread.sleep(getNumberRandomForSecond(465, 898));
			System.out.println("Pulsar iniciar sesion");
			robot.enter();
			
			Thread.sleep(2250);
			
			if(screen.exists("C:\\ImagenesSikuli\\validate-block5.png") != null) {
				return 0;
			}
			
			if(screen.exists("C:\\ImagenesSikuli\\validate-block6.png") != null) {
				return 2;
			}
			
			if(screen.exists("C:\\ImagenesSikuli\\validate-block7.png") != null) {
				return 3;
			}
		}
		
		
		return 1;

		

	}

	private void clickException(String pathPhoto) {
		try {
			screen.click(pathPhoto);
		}catch(IllegalThreadStateException | FindFailed e) {
			System.out.println("Error con la imagen");
		}
		
	}
	
	private static int getNumberRandomForSecond(int init, int fin) {
		return ThreadLocalRandom.current().nextInt(init, fin + 1);  
	}

}
