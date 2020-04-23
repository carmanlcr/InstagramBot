package com.instagram.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;


import configurations.connection.ConnectionIG;
import configurations.interfaces.Model;


public class Phrases implements Model{
	
	private final String TABLE_NAME = "phrases";
	private int phrases_id;
	private String phrase;
	private boolean active;
	private int categories_id;
	private int sub_categories_id;
	private int generes_id;
	private static ConnectionIG conn = new ConnectionIG();
	
	
	public void insert() throws SQLException {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd H:m:s");
		String strDate= formatter.format(date);
		String insert = "";
			try (Connection conexion = conn.conectar();
					Statement st = conexion.createStatement();){
				if(getGeneres_id() == 0 && getSub_categories_id() == 0) {
					insert = "INSERT INTO "+TABLE_NAME+"(phrase,created_at,updated_at,categories_id) "
							+ "VALUE ('"+getPhrase()+"','"+strDate+"', '"+strDate+"',"+getCategories_id()+");";
				}else if(getGeneres_id() == 0) {
					insert = "INSERT INTO "+TABLE_NAME+"(phrase,created_at,updated_at,categories_id,sub_categories_id) "
							+ "VALUE ('"+getPhrase()+"','"+strDate+"', '"+strDate+"',"+getCategories_id()+","+getSub_categories_id()+");";
				}else if(getSub_categories_id() == 0) {
					insert = "INSERT INTO "+TABLE_NAME+"(phrase,created_at,updated_at,categories_id,generes_id) "
							+ "VALUE ('"+getPhrase()+"','"+strDate+"','"+strDate+"',"+getCategories_id()+","+getGeneres_id()+");";
				}else {
					insert = "INSERT INTO "+TABLE_NAME+"(phrase,created_at,updated_at,categories_id,sub_categories_id,generes_id) "
							+ "VALUE ('"+getPhrase()+"','"+strDate+"','"+strDate+"',"+getCategories_id()+","+getSub_categories_id()+","
							+getGeneres_id()+");";
				}
				st.executeUpdate(insert);
				
			} catch(SQLException e)  {
				System.err.println(e);
			}
	}
	
	 
	public Phrases getPhraseRandom() throws SQLException{
		
		Phrases phra = new Phrases();
		
		
		ResultSet rs = null;
		String queryExce = "SELECT * FROM "+TABLE_NAME+" ph "
				+ "WHERE ph.active = ? AND ph.categories_id = ? "
				+ "AND ph.generes_id = ? "
				+ "ORDER BY RAND() LIMIT 1;";
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();
				PreparedStatement  query =  conexion.prepareStatement(queryExce);){
			
			
			
			query.setInt(1, 1);
			query.setInt(2, getCategories_id());
			query.setInt(3,getGeneres_id());
			rs = query.executeQuery();

			if (rs.next() ) {
				phra.setPhrases_id(rs.getInt("ph.phrases_id"));
				phra.setPhrase(rs.getString("ph.phrase"));
				phra.setCategories_id(rs.getInt("ph.categories_id"));
				phra.setGeneres_id(rs.getInt("ph.generes_id"));
				phra.setActive(rs.getBoolean("ph.active"));
				phra.setSub_categories_id(rs.getInt("ph.sub_categories_id"));
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return phra;
 	}
	
	public String getPhraseRandomSubCategorie() throws SQLException{
		
		String list = "";
		
		ResultSet rs = null;
		String queryExce = "SELECT ph.phrase FROM "+TABLE_NAME+" ph "
				+ "WHERE ph.active = ? AND ph.categories_id = ? "
				+ "AND ph.sub_categories_id = ? "
				+ "ORDER BY RAND() LIMIT 1;";
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();
				PreparedStatement  query = conexion.prepareStatement(queryExce);){
			
			
			
			query.setInt(1, 1);
			query.setInt(2, getCategories_id());
			query.setInt(3, getSub_categories_id());
			rs = query.executeQuery();

			while (rs.next() ) {
               list +=  rs.getString("phrase");
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return list;
 	}
	
	public int getPhrases_id() {
		return phrases_id;
	}


	public void setPhrases_id(int phrases_id) {
		this.phrases_id = phrases_id;
	}


	public String getPhrase() {
		return phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
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

	public void setCategories_id(int campaings_id) {
		this.categories_id = campaings_id;
	}

	public int getSub_categories_id() {
		return sub_categories_id;
	}

	public void setSub_categories_id(int sub_categories_id) {
		this.sub_categories_id = sub_categories_id;
	}

	public int getGeneres_id() {
		return generes_id;
	}

	public void setGeneres_id(int generes_id) {
		this.generes_id = generes_id;
	}


	@Override
	public void update() throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
	
}
