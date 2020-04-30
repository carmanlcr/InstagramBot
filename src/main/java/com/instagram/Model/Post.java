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


import configurations.connection.ConnectionIG;
import configurations.interfaces.Model;


public class Post implements Model{
	
	private static final String TABLE_NAME ="posts";
	private int posts_id;
	private int users_id;
	private int categories_id;
	private int tasks_model_id;
	private int phrases_id;
	private int tasks_grid_id;
	private int tasks_maduration_id;
	private String link_publication;
	private String user_transmition;
	private String link_instagram;
	private String created_at;
	private String updated_at;
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	
	private static ConnectionIG conn = new ConnectionIG();
	Statement st;
	ResultSet rs;

	public void insert() {
		date = new Date();
		setCreated_at(dateFormat.format(date));
		setUpdated_at(dateFormat.format(date));
		String insert = "INSERT INTO "+TABLE_NAME+" "
				+ "(users_id,categories_id,tasks_model_id,phrases_id,tasks_grid_id,tasks_maduration_id,link_publication,user_transmition,link_instagram,created_at,updated_at) "
				+ " VALUE (?,?,?,?,?,?,?,?,?,?,?);";
		try (Connection conexion = conn.conectar();
				PreparedStatement  query = conexion.prepareStatement(insert);){
			
			
			
			query.setInt(1, getUsers_id());
			query.setInt(2, getCategories_id());
			query.setInt(3, getTasks_model_id());
			query.setInt(4, getPhrases_id());
			query.setInt(5, getTasks_grid_id());
			query.setInt(6, getTasks_maduration_id());
			query.setString(7, getLink_publcation());
			query.setString(8, getUser_transmition());
			query.setString(9, getLink_instagram());
			query.setString(10, getCreated_at());
			query.setString(11, getUpdated_at());
			query.executeUpdate();
			
			conexion.close();
			
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		
	}
	
	public List<String[]> getCountPostUsers(int categories_id){
		List<String[]> list = new ArrayList<>();
		String[] array = null;
		int increment = 0;
		String created_at = dateFormat1.format(date);
		String query = " SELECT c.username usuario, COUNT(*) cuenta FROM "
				+ " (SELECT us.username, pt.created_at "
				+ " FROM users us "
				+ " LEFT JOIN "+TABLE_NAME+" pt ON pt.users_id = us.users_id AND categories_id =  "+categories_id
				+ " WHERE DATE(pt.created_at) = '"+created_at+"') AS c "
				+ " GROUP BY c.username; ";
		
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();
				ResultSet rs = st.executeQuery(query)){
			
			while (rs.next() ) {
				array = new String[2];
				array[0] = rs.getString("usuario");
				array[1] = rs.getString("cuenta");
				list.add(increment, array);
				increment++;
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return list;
	}
	
	public int getLast() {
		int id = 0;
		String query = "SELECT po.posts_id FROM "+TABLE_NAME+" po ORDER BY po.posts_id DESC LIMIT 1";
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();
				ResultSet rs = st.executeQuery(query)){
			
			while (rs.next() ) {
               id =  rs.getInt("po.posts_id");
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return id;
	} 
	
	public int getLastsTasktPublic(){
		int idTask = 0;
		
		String queryExce = "SELECT * FROM tasks_model tm " + 
				"WHERE tm.tasks_model_id NOT IN (SELECT pt.tasks_model_id FROM "+TABLE_NAME+" pt WHERE users_id = ? AND DATE(pt.created_at) BETWEEN ? AND ?) " + 
				"ORDER BY RAND() LIMIT 1;";
		try (Connection conexion = conn.conectar();
				PreparedStatement  query = conexion.prepareStatement(queryExce);){
		query.setInt(1, getUsers_id());
		query.setString(2, dateFormat1.format(new Date( date.getTime()-86400000)));
		query.setString(3, dateFormat1.format(date));
		
		rs = query.executeQuery();
			
		if(rs.next()) {
			idTask = rs.getInt("tm.tasks_model_id");
		}
		}catch(SQLException e) {
			e.getStackTrace();
		}
		return idTask;
	}
	
	public int getCategories_id() {
		return categories_id;
	}

	public void setCategories_id(int categories_id) {
		this.categories_id = categories_id;
	}



	public int getUsers_id() {
		return users_id;
	}

	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}

	public int getTasks_model_id() {
		return tasks_model_id;
	}

	public void setTasks_model_id(int tasks_model_id) {
		this.tasks_model_id = tasks_model_id;
	}

	public int getPosts_id() {
		return posts_id;
	}

	public void setPosts_id(int posts_id) {
		this.posts_id = posts_id;
	}

	public int getPhrases_id() {
		return phrases_id;
	}

	public void setPhrases_id(int phrases_id) {
		this.phrases_id = phrases_id;
	}

	public int getTasks_grid_id() {
		return tasks_grid_id;
	}

	public void setTasks_grid_id(int tasks_grid_id) {
		this.tasks_grid_id = tasks_grid_id;
	}

	public int getTasks_maduration_id() {
		return tasks_maduration_id;
	}

	public void setTasks_maduration_id(int tasks_maduration_id) {
		this.tasks_maduration_id = tasks_maduration_id;
	}

	public String getLink_publication() {
		return link_publication;
	}

	public void setLink_publication(String link_publication) {
		this.link_publication = link_publication;
	}

	public String getLink_publcation() {
		return link_publication;
	}

	public void setLink_publcation(String link_publcation) {
		this.link_publication = link_publcation;
	}

	public String getUser_transmition() {
		return user_transmition;
	}

	public void setUser_transmition(String user_transmition) {
		this.user_transmition = user_transmition;
	}

	public String getLink_instagram() {
		return link_instagram;
	}

	public void setLink_instagram(String link_instagram) {
		this.link_instagram = link_instagram;
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

	@Override
	public void update() throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
}
