package com.instagram.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.instagram.Interface.Model;

public class Account_Instagram_User implements Model {

	private final String TABLE_NAME = "account_instagram_users";
	private int account_instagram_users_id;
	private int users_id;
	private int accounts_instagram_id;
	private String created_at;
	private static Conexion conn = new Conexion();
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	ResultSet rs;
	
	public void insert() throws SQLException {
		setCreated_at(dateFormat.format(date));
		
		try (Connection conexion = conn.conectar();){
			String insert = "INSERT INTO "+TABLE_NAME+""
					+ "(users_id,accounts_instagram_id,created_at) "
					+ " VALUE (?,?,?);";
			
			PreparedStatement  query = (PreparedStatement) conexion.prepareStatement(insert);
			query.setInt(1, getUsers_id());
			query.setInt(2, getAccounts_instagram_id());
			query.setString(3, getCreated_at());
			
			query.executeUpdate();
			
			
		}catch(SQLException e) {
			System.err.println(e);
		}
	}
	
	public Account_Instagram getAccountsFollowUser() {
		
		Account_Instagram ac = null;
		try(Connection conexion = conn.conectar();) {
			String query = "SELECT ai.* FROM accounts_instagram ai " + 
					"WHERE ai.accounts_instagram_id NOT IN " + 
					"(SELECT aiu.accounts_instagram_id FROM "+TABLE_NAME+" aiu WHERE aiu.users_id = ?) " + 
					"ORDER BY RAND() LIMIT 1; ";
			
			PreparedStatement queryExe = conexion.prepareStatement(query);
			queryExe.setInt(1,getUsers_id());
			
			rs = queryExe.executeQuery();
			
			if(rs.next()) {
				ac = new Account_Instagram();
				ac.setAccounts_instagram_id(rs.getInt("ai.accounts_instagram_id"));
				ac.setAccount(rs.getString("ai.account"));
				ac.setGeneres_id(rs.getInt("ai.generes_id"));
				ac.setCategories_id(rs.getInt("ai.categories_id"));
				ac.setCreated_at(rs.getString("ai.created_at"));
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		return ac;
	}

	public int getAccount_instagram_users_id() {
		return account_instagram_users_id;
	}

	public void setAccount_instagram_users_id(int account_instagram_users_id) {
		this.account_instagram_users_id = account_instagram_users_id;
	}

	public int getUsers_id() {
		return users_id;
	}

	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}

	public int getAccounts_instagram_id() {
		return accounts_instagram_id;
	}

	public void setAccounts_instagram_id(int accounts_instagrams_id) {
		this.accounts_instagram_id = accounts_instagrams_id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
	

}
