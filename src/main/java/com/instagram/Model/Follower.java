package com.instagram.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import configurations.connection.ConnectionIG;
import configurations.interfaces.Model;

public class Follower implements Model {

	private static final String TABLE_NAME = "followers";
	private int followers_id;
	private String user;
	private String name;
	private String link;
	private boolean active;
	private String created_at;
	private String updated_at;
	private DateFormat dateFormatDateTime = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private static ConnectionIG conn = new ConnectionIG();
	
	@Override
	public void insert() throws SQLException {
		Date date = new Date();
		StringBuilder insert = new StringBuilder();
		insert.append("INSERT INTO "+TABLE_NAME+"(user,name,link,created_at,updated_at) ");
		insert.append("VALUES (?,?,?,?,?);");
		setCreated_at(dateFormatDateTime.format(date));
		setUpdated_at(dateFormatDateTime.format(date));
		
		try (Connection con = conn.conectar();
				PreparedStatement pr = con.prepareStatement(insert.toString())){
			pr.setString(1, getUser());
			pr.setString(2, getName());
			pr.setString(3, getLink());
			pr.setString(4, getCreated_at());
			pr.setString(5, getUpdated_at());
			
			pr.executeUpdate();
		}catch(SQLException e) {
			System.err.println(e);
		}

	}
	
	public Follower find(){
		Follower follower = new Follower();
		StringBuilder insert = new StringBuilder();
		insert.append("SELECT * FROM "+TABLE_NAME);
		insert.append(" WHERE user = ?;");
		
		try (Connection con = conn.conectar();
				PreparedStatement pr = con.prepareStatement(insert.toString())){
			pr.setString(1, getUser());
			ResultSet rs = pr.executeQuery();
			if(rs.next()) {
				follower.setFollowers_id(rs.getInt("followers_id"));
				follower.setUser(rs.getString("user"));
				follower.setName(rs.getString("name"));
				follower.setLink(rs.getString("link"));
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		return follower;
	}
	@Override
	public void update() throws SQLException {
		// None

	}

	public int getFollowers_id() {
		return followers_id;
	}

	public void setFollowers_id(int followers_id) {
		this.followers_id = followers_id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
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
