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

import com.instagram.Interface.Model;
import com.mysql.jdbc.Statement;


public class Post implements Model{
	
	private final String TABLE_NAME ="posts";
	private int posts_id;
	private int users_id;
	private int categories_id;
	private int tasks_model_id;
	private int phrases_id;
	private String link_publication;
	private String user_transmition;
	private String link_instagram;
	private String created; 
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	
	private static Conexion conn = new Conexion();
	Statement st;
	ResultSet rs;

	public void insert() {
		setCreated(dateFormat.format(date));
		Connection conexion = conn.conectar();
		try {
			String insert = "INSERT INTO "+TABLE_NAME+""
					+ "(users_id,categories_id,tasks_model_id,phrases_id,link_publication,user_transmition,link_instagram,created_at) "
					+ " VALUE (?,?,?,?,?,?,?,?);";
			
			PreparedStatement  query = (PreparedStatement) conexion.prepareStatement(insert);
			query.setInt(1, getUsers_id());
			query.setInt(2, getCategories_id());
			query.setInt(3, getTasks_model_id());
			query.setInt(4, getPhrases_id());
			query.setString(5, getLink_publcation());
			query.setString(6, getUser_transmition());
			query.setString(7, getLink_instagram());
			query.setString(8, getCreated());
			
			query.executeUpdate();
			
			conexion.close();
			
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		
	}
	
	public List<String[]> getCountPostUsers(int categories_id){
		List<String[]> list = new ArrayList<String[]>();
		String[] array = null;
		int increment = 0;
		String created_at = dateFormat1.format(date);
		String query = " SELECT c.username usuario, COUNT(*) cuenta FROM "
				+ " (SELECT us.username, pt.created_at "
				+ " FROM users us "
				+ " LEFT JOIN "+TABLE_NAME+" pt ON pt.users_id = us.users_id AND categories_id =  "+categories_id
				+ " WHERE DATE(pt.created_at) = '"+created_at+"') AS c "
				+ " GROUP BY c.username; ";
		Statement st = null;
		ResultSet rs = null;
		Connection conexion = conn.conectar();
		
		try {
			
			st = (Statement) conexion.createStatement();
			rs = st.executeQuery(query);
			
			while (rs.next() ) {
				array = new String[2];
				array[0] = rs.getString("usuario");
				array[1] = rs.getString("cuenta");
				list.add(increment, array);
				increment++;
			}
			conexion.close();
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return list;
	}
	
	public int getLast() {
		int id = 0;
		Connection conexion = conn.conectar();
		
		
		try {
			
			st = (Statement) conexion.createStatement();
			rs = st.executeQuery("SELECT po.posts_id FROM "+TABLE_NAME+" po ORDER BY po.posts_id DESC LIMIT 1");

			
			while (rs.next() ) {
               id =  rs.getInt("po.posts_id");
			}
			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return id;
	} 
	
	public int getLastsTasktPublic(){
		int idTask = 0;
		Connection conexion = conn.conectar();
		
		try {
			String queryExce = "SELECT * FROM tasks_model tm " + 
					"WHERE tm.tasks_model_id NOT IN (SELECT pt.tasks_model_id FROM "+TABLE_NAME+" pt WHERE users_id = ? AND DATE(pt.created_at) BETWEEN ? AND ?) " + 
					"ORDER BY RAND() LIMIT 1;";
	
		PreparedStatement  query = (PreparedStatement) conexion.prepareStatement(queryExce);
		query.setInt(1, getUsers_id());
		query.setString(2, dateFormat1.format(new Date( date.getTime()-86400000)));
		query.setString(3, dateFormat1.format(date));
		
		rs = query.executeQuery();
			
		if(rs.next()) {
			idTask = rs.getInt("tm.tasks_model_id");
		}
		conexion.close();
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

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
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
	
}
