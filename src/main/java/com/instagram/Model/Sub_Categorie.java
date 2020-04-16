package com.instagram.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.instagram.Interface.Model;

import configurations.connection.ConnectionIG;


public class Sub_Categorie implements Model {
	
	private String name;
	private int categories_id;
	private static ConnectionIG conn = new ConnectionIG();
	
	public void insert() throws SQLException {
		
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		String strDate= formatter.format(date);
			try (Connection conexion = conn.conectar();
					Statement st = conexion.createStatement()){
				String insert = "INSERT INTO sub_categories(name,categories_id,created_at,updated_at) "
						+ "VALUES ('"+getName()+"',"+getCategories_id()+",'"+strDate+"','"+strDate+"');";
				st.executeUpdate(insert);
				
			} catch(SQLException e)  {
				System.err.println(e);
			} 
	}
	
	public List<String> getSubCategories(){
		List<String> list = new ArrayList<>();

		
		ResultSet rs = null;
		String queryExce = "SELECT sca.name FROM sub_categories sca "
				+ "WHERE sca.categories_id = ? ; ";
		
		try (Connection conexion = conn.conectar();
				PreparedStatement  query = conexion.prepareStatement(queryExce);){
			
			
		
			query.setInt(1, getCategories_id());
			rs = query.executeQuery();

			while (rs.next() ) {
               list.add(rs.getString("sca.name"));
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return list;
	}

	public int getIdPhraseSubCategories(String name) throws SQLException {

		int indice = 0;
		
		ResultSet rs = null;
		String queryExce = "SELECT sca.sub_categories_id FROM sub_categories sca "
				+ "WHERE sca.name = ? LIMIT 1; ";
		try (Connection conexion = conn.conectar();
				PreparedStatement  query = conexion.prepareStatement(queryExce);){
			
			
			
			
			query.setString(1, name);
			rs = query.executeQuery();

			while (rs.next() ) {
               indice =  rs.getInt("sca.sub_categories_id");
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return indice;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCategories_id() {
		return categories_id;
	}

	public void setCategories_id(int categories_id) {
		this.categories_id = categories_id;
	}

}
