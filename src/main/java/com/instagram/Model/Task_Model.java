package com.instagram.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.instagram.Interface.Model;


public class Task_Model implements Model {

	private String name;
	private boolean active;
	private String created_at;
	private String updated_at;
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private static Conexion conn = new Conexion();
	ResultSet rs;
	
	public void insert() throws SQLException {
		date = new Date();
		setCreated_at(dateFormat.format(date));
		setUpdated_at(dateFormat.format(date));
		String insert = "INSERT INTO tasks_model(name,created_at,updated_at) VALUE "
				+ " (?,?,?);";
		try (Connection conexion = conn.conectar();){
			
			PreparedStatement exe = conexion.prepareStatement(insert);
			exe.setString(1, getName());
			exe.setString(2, getCreated_at());
			exe.setString(3, getUpdated_at());
			exe.executeUpdate();
		}catch(SQLException e) {
			System.err.println(e);
		}

	}
	
	public int getLast() {
		int id = 0;
		String query = "SELECT tk.tasks_model_id FROM tasks_model tk ORDER BY tk.tasks_model_id DESC LIMIT 1";
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();){
			rs = st.executeQuery(query);
			while (rs.next() ) {
               id =  rs.getInt("tk.tasks_model_id");
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return id;
	}

	public int getTaskModelDetailDiferent(String values){
		int list = 0;
		
		try (Connection conexion = conn.conectar();){
			
			String queryExce = "SELECT tm.tasks_model_id FROM tasks_model tm " + 
							"WHERE tm.tasks_model_id NOT IN ("+values+") ORDER BY rand() LIMIT 1;";
			
			PreparedStatement  query = conexion.prepareStatement(queryExce);
			rs = query.executeQuery();
			
			if (rs.next() ) {
				list = rs.getInt("tm.tasks_model_id");
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		return list;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

}
