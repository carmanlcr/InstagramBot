package com.instagram.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import java.sql.Connection;

import configurations.connection.ConnectionIG;
import configurations.interfaces.Model;


public class Follower_Report implements Model {

	
	private static final String TABLE_NAME = "followers_reports";
	private int followers_reports_id;
	private int users_id;
	private int followers;
	private int following;
	private String created_at;
	private String updated_at;
	private static ConnectionIG conn = new ConnectionIG();
	private Date date;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	@Override
	public void insert() throws SQLException {
		date = new Date();
		setCreated_at(dateFormat.format(date));
		setUpdated_at(dateFormat.format(date));
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO "+TABLE_NAME+"(users_id,followers,following,created_at,updated_at) ");
		query.append("VALUE(?,?,?,?,?)");
		
		try(Connection conexion =  conn.conectar();
				PreparedStatement pre = conexion.prepareStatement(query.toString());){
			pre.setInt(1, getUsers_id());
			pre.setInt(2, getFollowers());
			pre.setInt(3, getFollowing());
			pre.setString(4, getCreated_at());
			pre.setString(5, getUpdated_at());
			pre.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void update() throws SQLException {
		date = new Date();
		setUpdated_at(dateFormat.format(date));
		StringBuilder query = new StringBuilder();
		query.append("UPDATE "+TABLE_NAME+" SET followers = ?, following =?, updated_at = ? ");
		query.append("WHERE followers_reports_id = ?");
		
		try(Connection conexion =  conn.conectar();
				PreparedStatement pre = conexion.prepareStatement(query.toString());){
			pre.setInt(1, getFollowers());
			pre.setInt(2, getFollowing());
			pre.setString(3, getUpdated_at());
			pre.setInt(4, getFollowers_reports_id());
			
			pre.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}

	}
	
	public Follower_Report find(int id) {
		Follower_Report userF = null;
		StringBuilder str = new StringBuilder();
		str.append("SELECT * FROM "+TABLE_NAME+" WHERE users_id = ?;");
			
		try(Connection conexion =  conn.conectar();
				PreparedStatement pre = conexion.prepareStatement(str.toString());){
			pre.setInt(1, id);
			
			
			ResultSet rs = pre.executeQuery();
			
			if(rs.next()) {
				userF = new Follower_Report();
				userF.setFollowers_reports_id(rs.getInt("followers_reports_id"));
				userF.setUsers_id(rs.getInt("users_id"));
				userF.setFollowers(rs.getInt("followers"));
				userF.setFollowing(rs.getInt("following"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return userF;
	}
	
	public void updateOrCreate() {
		Follower_Report userF =find(getUsers_id());
		if(userF == null) {
			userF = new Follower_Report();
			userF.setUsers_id(getUsers_id());
			userF.setFollowers(getFollowers());
			userF.setFollowing(getFollowing());
			try {
				userF.insert();
				System.out.println("Insertado Followers");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}else {
			userF.setFollowers(getFollowers());
			userF.setFollowing(getFollowing());
			try {
				userF.update();
				System.out.println("Actualizar followers");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}



	public int getFollowers_reports_id() {
		return followers_reports_id;
	}

	public void setFollowers_reports_id(int followers_reports_id) {
		this.followers_reports_id = followers_reports_id;
	}

	public int getUsers_id() {
		return users_id;
	}

	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}

	public int getFollowers() {
		return followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	public int getFollowing() {
		return following;
	}

	public void setFollowing(int following) {
		this.following = following;
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
