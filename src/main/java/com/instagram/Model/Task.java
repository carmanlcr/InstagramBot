package com.instagram.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import configurations.connection.ConnectionIG;
import configurations.interfaces.Model;


public class Task implements Model{
	
	private static final String TABLE_NAME = "tasks";
	private String name;
	private String created_at;
	private String updated_at;
	private boolean active;
	private Date date = new Date();
	private DateFormat dateFormatDateTime = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private ConnectionIG conn = new ConnectionIG();
	Statement st;
	ResultSet rs;
	
	public void insert() throws SQLException {
		date = new Date();
		setCreated_at(dateFormatDateTime.format(date));
		setUpdated_at(dateFormatDateTime.format(date));
		String insert = "INSERT INTO "+TABLE_NAME+"(name,created_at) VALUE "
				+ " (?,?,?);";
		try (Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(insert);){
			
			exe.setString(1, getName());
			exe.setString(2, getCreated_at());
			exe.setString(3, getUpdated_at());
			exe.executeUpdate();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}

	public Map<String, Integer> getTasksActive(){
		Map<String, Integer> list = new HashMap<>();
		
		
		String query = "SELECT t.tasks_id,t.name FROM "+TABLE_NAME+" t " + 
				"WHERE active = ?;"; 
		try (Connection conexion = conn.conectar();
				PreparedStatement pst = conexion.prepareStatement(query);){
			
			pst.setInt(1, 1);
			rs = pst.executeQuery();
			while (rs.next() ) {
				list.put(rs.getString("t.name"),rs.getInt("t.tasks_id"));
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public void update() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	
	

}
