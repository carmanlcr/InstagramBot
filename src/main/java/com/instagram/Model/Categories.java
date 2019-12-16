package com.instagram.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.instagram.Interface.Model;



public class Categories implements Model{

	
	private String name;
	private static Conexion conn = new Conexion();
	
	
	public void insert() {
		String insert = "INSERT INTO categories(name) "
				+ "VALUES (?);";
		try (Connection conexion = conn.conectar();){
			
			PreparedStatement exe = conexion.prepareStatement(insert);
			exe.setString(1, getName());
			exe.executeUpdate();
			
		} catch(SQLException e)  {
			System.err.println(e);
		} catch(Exception e){
			System.err.println(e);
			
		}
			
	}
	
	public List<String> getAllActive()  {
		ArrayList<String> list = new ArrayList<String>();
		
		ResultSet rs = null;
		try (Connection conexion = conn.conectar();){
			String queryExce = "SELECT * FROM categories WHERE active = ? ;";
			PreparedStatement  query = (PreparedStatement) conexion.prepareStatement(queryExce);
			query.setInt(1, 1);
			rs = query.executeQuery();


			while (rs.next() ) {
				list.add(rs.getString("name"));
               
			}
			
		}catch(SQLException e) {
			System.err.println(e);
		}finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}

	public int getIdCategories(String name) throws SQLException {
		int id = 0;
		String query = "SELECT * FROM categories WHERE name = '"+name+"' AND active = 1;";
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();
				ResultSet rs = st.executeQuery(query)){
			
			while (rs.next() ) {
				id = rs.getInt("categories_id");
               
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return id;
		
	}
	
	public int getIdCategorieHashTag(String name) throws SQLException {
		int id = 0;
		ResultSet rs = null;
		
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();){
			
			rs = st.executeQuery("SELECT * FROM categories WHERE name = '"+name+"' AND active = 0;");
			
			while (rs.next() ) {
				id = rs.getInt("categories_id");
               
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		return id;
		
	}
	
	public String getNameCategories(int id) throws SQLException {
		String name = "";
		ResultSet rs = null;
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();){
			
			rs = st.executeQuery("SELECT * FROM categories WHERE categories_id = "+id+";");
			
			while (rs.next() ) {
				name = rs.getString("name");
               
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return name;
		
	}
	
	public List<String> getSubCategorieConcat() throws SQLException {
		List<String> concat  = new ArrayList<String>();
		ResultSet rs = null;
		
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();){
			
			String query = "SELECT ca.categories_id, ca.name, sb.sub_categories_id, sb.name " + 
					"FROM instagram.categories ca " + 
					"INNER JOIN sub_categories sb ON ca.categories_id = sb.categories_id " + 
					"INNER JOIN hashtag ht ON ht.sub_categories_id = sb.sub_categories_id " + 
					"INNER JOIN phrases ph ON ph.sub_categories_id = sb.sub_categories_id " + 
					"INNER JOIN path_photos pp ON pp.sub_categories_id = sb.sub_categories_id " + 
					"ORDER BY RAND() LIMIT 1;";
			rs = st.executeQuery(query);
			
			if(rs.next()) {
				concat.add(rs.getString("ca.categories_id"));
				concat.add(rs.getString("ca.name"));
				concat.add(rs.getString("sb.sub_categories_id"));
				concat.add(rs.getString("sb.name"));
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
				
		return concat;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
