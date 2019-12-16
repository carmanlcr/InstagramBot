package com.instagram.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.instagram.Interface.Model;
import com.mysql.jdbc.Statement;


public class Inicio_Aplicacion implements Model{
	private String created_at;
	private String version;
	private static Conexion conn = new Conexion();
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Statement st;
	ResultSet rs;
	
	public void insert() {
		Connection conexion = conn.conectar();
		setCreated_at(dateFormat.format(date));
		try {
			String insert = "INSERT INTO inicio_aplicacion(version,created_at) "
					+ " VALUE ('"+getVersion()+"','"+getCreated_at()+"');";
			st = (Statement) conexion.createStatement();
			st.executeUpdate(insert);
			conexion.close();
			
		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
