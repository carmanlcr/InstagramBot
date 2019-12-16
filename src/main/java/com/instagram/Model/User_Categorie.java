package com.instagram.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.instagram.Interface.Model;
import com.mysql.jdbc.Statement;


public class User_Categorie implements Model{
	
	private final String TABLE_NAME = "users_categories";
	private int users_id;
	private int categories_id;
	private String created_at;
	private static Conexion conn = new Conexion();
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	Statement st;
	ResultSet rs;
	
	public void insert() {
		Connection conexion = conn.conectar();
		setCreated_at(dateFormat.format(date));
		try {
			String insert = "INSERT INTO "+TABLE_NAME+"(users_id,categories_id,created_at) "
					+ " VALUE ("+getUsers_id()+", "+getCategories_id()+", '"+getCreated_at()+"');";
			st = (Statement) conexion.createStatement();
			st.executeUpdate(insert);

			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}
	
	public boolean inserts() {
		Connection conexion = conn.conectar();
		setCreated_at(dateFormat.format(date));
		try {
			String insert = "INSERT INTO "+TABLE_NAME+"(users_id,categories_id,created_at) "
					+ " VALUE ("+getUsers_id()+", "+getCategories_id()+", '"+getCreated_at()+"');";
			st = (Statement) conexion.createStatement();
			st.executeUpdate(insert);

			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
			return false;
		}
		
		return true;
	}
	
	public void updateCategories() throws SQLException{
		Connection conexion = conn.conectar();
		setCreated_at(dateFormat.format(date));
		try {
			String update = "UPDATE "+TABLE_NAME+" SET categories_id ="+getCategories_id()+" WHERE users_id = "+getUsers_id();
			st = (Statement) conexion.createStatement();
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
}
