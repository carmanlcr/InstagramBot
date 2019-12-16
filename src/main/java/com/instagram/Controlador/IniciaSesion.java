package com.instagram.Controlador;
public class IniciaSesion {
	
	private String username;
	private String password;
	private RobotController robot;
	
	public IniciaSesion(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public void init() throws InterruptedException {
		System.out.println("Ir a la pagina de inicio de sesi�n");
		//Insertar el usuario
		Thread.sleep(getNumberRandomForSecond(1256, 1487));
		robot = new RobotController();
		//ponerse en el input de usuario
		robot.dimensions(598, 623);
		robot.clickPressed();
		System.out.println("Escribir usuario");
		robot.inputWrite(username);
		//Insertar el password
		Thread.sleep(getNumberRandomForSecond(465, 897));
		System.out.println("Escribir contrase�a");
		robot.dimensions(598, 665);
		robot.clickPressed();
		robot.inputWrite(password);
		//Presionar boton de inicio de sesi�n
		Thread.sleep(getNumberRandomForSecond(465, 898));
		System.out.println("Pulsar iniciar sesion");
		robot.dimensions(630, 753);
		robot.clickPressed();

	}

	
	private static int getNumberRandomForSecond(int init, int fin) {
		return (int) (Math.random() * init) + fin; 
	}

}
