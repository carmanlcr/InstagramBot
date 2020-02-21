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
	private String updated_at;
	private String version;
	private int categories_id;
	private int generes_id;
	private static Conexion conn = new Conexion();
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	
	public void insert() {
		setCreated_at(dateFormat.format(date));
		setUpdated_at(dateFormat.format(date));
		try (Connection conexion = conn.conectar();){
			String insert = "INSERT INTO inicio_aplicacion(categories_id,generes_id,version,created_at,updated_at) "
					+ " VALUE (?,?,?,?,?);";
			
			PreparedStatement pst = conexion.prepareStatement(insert);
			pst.setInt(1, getCategories_id());
			pst.setInt(2, getGeneres_id());
			pst.setString(3, getVersion());
			pst.setString(4, getCreated_at());
			pst.setString(5, getUpdated_at());
			
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

	public int getCategories_id() {
		return categories_id;
	}

	public void setCategories_id(int categories_id) {
		this.categories_id = categories_id;
	}

	public int getGeneres_id() {
		return generes_id;
	}

	public void setGeneres_id(int generes_id) {
		this.generes_id = generes_id;
	}
	
	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
