package com.instagram.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import configurations.connection.ConnectionIG;
import configurations.interfaces.Model;


public class User_Follower implements Model {
	
	private static final String TABLE_NAME = "users_followers";
	private int users_id;
	private int followers_id;
	private boolean active;
	private String created_at;
	private String updated_at;
	private static ConnectionIG conn = new ConnectionIG();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void insert() throws SQLException {
		Date date = new Date();
		setCreated_at(dateFormat.format(date));
		setUpdated_at(dateFormat.format(date));
		
		StringBuilder insert = new StringBuilder();
		insert.append("INSERT INTO "+TABLE_NAME+"(users_id,followers_id,created_at,updated_at) ");
		insert.append("VALUES (?,?,?,?);");
		
		try(Connection con = conn.conectar();
				PreparedStatement pre = con.prepareStatement(insert.toString());){
			pre.setInt(1, getUsers_id());
			pre.setInt(2, getFollowers_id());
			pre.setString(3, getCreated_at());
			pre.setString(4, getUpdated_at());
			pre.executeUpdate();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}
	
	public User_Follower validateMatch() {
		User_Follower uf = null;
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM "+TABLE_NAME);
		query.append(" WHERE users_id = ? AND followers_id = ? AND active = 1;");
		
		try(Connection conexion = conn.conectar();
				PreparedStatement pre = conexion.prepareStatement(query.toString());){
			pre.setInt(1, getUsers_id());
			pre.setInt(2, getFollowers_id());
			
			ResultSet rs = pre.executeQuery();
			
			if(rs.next()) {
				uf = new User_Follower();
				uf.setUsers_id(getUsers_id());
				uf.setFollowers_id(getFollowers_id());
				uf.setActive(rs.getBoolean("active"));
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		return uf;
	}
	
	public List<Follower> getUserFollowe(){
		List<Follower> list = new ArrayList<>();
		Date date = new Date();
		StringBuilder string = new StringBuilder();
		string.append("SELECT fo.followers_id, fo.user, fo.name, fo.link FROM "+TABLE_NAME+" uf ");
		string.append("INNER JOIN followers fo ON fo.followers_id = uf.followers_id");
		string.append("WHERE uf.users_id = ? AND uf.active = 1 ");
		string.append("DATE(uf.created_at) <> ? ");
		string.append("ORDER BY uf.created_at DESC LIMIT 8;");
		
		try(Connection conexion = conn.conectar();
				PreparedStatement pre = conexion.prepareStatement(string.toString());){
			pre.setInt(1, getUsers_id());
			pre.setString(2, dateFormat.format(date));
			
			ResultSet rs = pre.executeQuery();
			
			if(rs.next()) {
				Follower folo = new Follower();
				folo.setFollowers_id(rs.getInt("fo.followers_id"));
				folo.setUser(rs.getString("fo.user"));
				folo.setName(rs.getString("fo.name"));
				folo.setLink(rs.getString("fo.link"));
				list.add(folo);
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		
		return list;
	}

	@Override
	public void update() throws SQLException {
		// None
		
	}

	public int getUsers_id() {
		return users_id;
	}

	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}

	public int getFollowers_id() {
		return followers_id;
	}

	public void setFollowers_id(int followers_id) {
		this.followers_id = followers_id;
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
