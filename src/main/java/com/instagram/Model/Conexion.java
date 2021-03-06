package com.instagram.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexion {
	private final String URL = "jdbc:mysql://HOST:3306/";
    private final String BD = "instagram"; // Nombre de la BD.
    private final String USER = "username"; //Nomber del usuario
    private final String PASSWORD = "password"; //contrase�a

    public Connection connect = null;

    @SuppressWarnings("finally")
    public Connection conectar() {
    	
        try {
        	/*String URL = "jdbc:mysql://MORALESSYS:3306/"; // Ubicaci�n de la BD.
            Class.forName("com.mysql.jdbc.Driver");*/
            //Conexion claro
            
              // Ubicaci�n de la BD.
             Class.forName("com.mysql.cj.jdbc.Driver");
              
            connect = (Connection) DriverManager.getConnection(URL + BD, USER, PASSWORD);
        }catch(SQLException e2) {
        	System.err.println("Error al conectarse a la base de datos "+e2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return connect;
        }
    }
}
