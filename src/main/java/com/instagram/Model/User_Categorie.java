package com.instagram.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import configurations.connection.ConnectionIG;
import configurations.interfaces.Model;


public class User_Categorie implements Model{
	
	private static final String TABLE_NAME = "users_categories";
	private int users_id;
	private int categories_id;
	private String created_at;
	private static ConnectionIG conn = new ConnectionIG();
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	Statement st;
	ResultSet rs;
	
	public void insert() {
		
		setCreated_at(dateFormat.format(date));
		String insert = "INSERT INTO "+TABLE_NAME+"(users_id,categories_id,created_at) "
				+ " VALUE (?,?,?);";
		try (Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(insert);){
			
			
			exe.setInt(1, getUsers_id());
			exe.setInt(2, getCategories_id());
			exe.setString(3, getCreated_at());
			exe.executeUpdate();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}
	
	public boolean inserts() {
		String insert = "INSERT INTO "+TABLE_NAME+"(users_id,categories_id,created_at) "
				+ " VALUE (?,?,?);";
		setCreated_at(dateFormat.format(date));
		try (Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(insert);){
			
			exe.setInt(1, getUsers_id());
			exe.setInt(2, getCategories_id());
			exe.setString(3, getCreated_at());
			
			exe.executeUpdate();

		}catch(SQLException e) {
			System.err.println(e);
			return false;
		}
		
		return true;
	}
	
	public void updateCategories() throws SQLException{
		String update = "UPDATE "+TABLE_NAME+" SET categories_id ="+getCategories_id()+" WHERE users_id = "+getUsers_id();
		setCreated_at(dateFormat.format(date));
		try (Connection conexion = conn.conectar();){
			
			st = conexion.createStatement();
			st.executeUpdate(update);

			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
	}
	
	public int getUsers_id() {
		return users_id;
	}
	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}
	public int getCategories_id() {
		return categories_id;
	}
	public void setCategories_id(int categories_id) {
		this.categories_id = categories_id;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String date) {
		this.created_at = date;
	}

	@Override
	public void update() throws SQLException {
		// TODO Auto-generated method stub
		
	}
}
