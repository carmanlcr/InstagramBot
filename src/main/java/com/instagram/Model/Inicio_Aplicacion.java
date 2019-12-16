package com.instagram.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.instagram.Interface.Model;


public class Inicio_Aplicacion implements Model{
	private String created_at;
	private String version;
	private static Conexion conn = new Conexion();
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	
	public void insert() {
		setCreated_at(dateFormat.format(date));
		try (Connection conexion = conn.conectar();){
			String insert = "INSERT INTO inicio_aplicacion(version,created_at) "
					+ " VALUE (?,?);";
			
			PreparedStatement pst = conexion.prepareStatement(insert);
			pst.setString(1, getVersion());
			pst.setString(2, getCreated_at());
			
			pst.executeUpdate();
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
