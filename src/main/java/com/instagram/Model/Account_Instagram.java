package com.instagram.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.instagram.Interface.Model;

import configurations.connection.ConnectionIG;

public class Account_Instagram implements Model {
	
	private static final String TABLE_NAME = "accounts_instagram";
	private int accounts_instagram_id;
	private String account;
	private int generes_id;
	private int categories_id;
	private String created_at;
	private static ConnectionIG conn = new ConnectionIG();
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	
	public void insert() throws SQLException {
		setCreated_at(dateFormat.format(date));
		
		String insert = "INSERT INTO "+TABLE_NAME+""
				+ "(account,generes_id,categories_id,created_at) "
				+ " VALUE (?,?,?,?);";
		try (Connection conexion = conn.conectar();
				PreparedStatement  query =conexion.prepareStatement(insert);){
			
			
			query.setString(1, getAccount());
			query.setInt(2, getGeneres_id());
			query.setInt(3, getCategories_id());
			query.setString(4, getCreated_at());
			
			query.executeUpdate();
			
			
		}catch(SQLException e) {
			System.err.println(e);
		}
	}
	
	
	
	public int getAccounts_instagram_id() {
		return accounts_instagram_id;
	}

	public void setAccounts_instagram_id(int accounts_instagram_id) {
		this.accounts_instagram_id = accounts_instagram_id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getGeneres_id() {
		return generes_id;
	}

	public void setGeneres_id(int generes_id) {
		this.generes_id = generes_id;
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

	public void setCreated_at(String crated_at) {
		this.created_at = crated_at;
	}

	

}
