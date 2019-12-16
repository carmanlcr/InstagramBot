package com.instagram.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.instagram.Interface.Model;




public class Genere implements Model{
	private String name;
	private String created_at;
	private int categories_id;
	private boolean active;
	private Date date = new Date();
	private DateFormat dateFormatDateTime = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private static Conexion conn = new Conexion();
	Statement st;
	ResultSet rs;
	
	public void insert() {
		Connection conexion = conn.conectar();
		setCreated_at(dateFormatDateTime.format(date));
		
		try {
			String insert = "INSERT INTO generes(name,created_at,categories_id) VALUE "
					+ " ('"+getName()+"','"+getCreated_at()+"','"+getCategories_id()+"');";
			st = (Statement) conexion.createStatement();
			st.executeUpdate(insert);
			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}
	
	
	public List<String> getGeneresActive(){
		List<String> list = new ArrayList<String>();
		Connection conexion = conn.conectar();
		String query = "SELECT g.generes_id, g.name FROM generes g " + 
				"WHERE categories_id = ? AND active = ?;"; 
		try {
			PreparedStatement pst = conexion.prepareStatement(query);
			pst.setInt(1, getCategories_id());
			pst.setInt(2, 1);
			rs = pst.executeQuery();
			
			while (rs.next()) {
				list.add(rs.getString("g.name"));
			}
			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		
		
		return list;
	}
	
	public List<String> getGeneresActiveWithPhrasesHashTagPhoto(){
		List<String> list = new ArrayList<String>();
		Connection conexion = conn.conectar();
		String query = "SELECT DISTINCT(g.generes_id), g.name FROM (" + 
				"SELECT g.generes_id, g.name FROM generes g " + 
				"INNER JOIN path_photos pp ON pp.generes_id = g.generes_id AND pp.active = ? " + 
				"INNER JOIN phrases ph ON ph.generes_id = g.generes_id AND ph.active = ? " + 
				"INNER JOIN hashtag ht ON ht.generes_id = g.generes_id AND ht.active = ? " + 
				"WHERE g.categories_id = ? AND g.active = ?) g ;"; 
		try {
			PreparedStatement pst = conexion.prepareStatement(query);
			pst.setInt(1, 1);
			pst.setInt(2, 1);
			pst.setInt(3, 1);
			pst.setInt(4, getCategories_id());
			pst.setInt(5, 1);
			rs = pst.executeQuery();
			
			while (rs.next() ) {
				list.add(rs.getString("g.name"));
			}
			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		
		
		return list;
	}
	
	
	public int getIdGenere() {
		int idGenere = 0;
		Connection conexion = conn.conectar();
		String query = "SELECT g.generes_id FROM generes g " + 
				"WHERE g.active = ? AND g.name = ?;"; 
		try {
			PreparedStatement pst = conexion.prepareStatement(query);
			pst.setInt(1, 1);
			pst.setString(2, getName());
			rs = pst.executeQuery();
			
			while (rs.next() ) {
				idGenere = rs.getInt("g.generes_id");
			}
			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return idGenere;
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
	public void setCreated_at(String created_id) {
		this.created_at = created_id;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	public int getCategories_id() {
		return categories_id;
	}

	public void setCategories_id(int categories_id) {
		this.categories_id = categories_id;
	}

	
}
