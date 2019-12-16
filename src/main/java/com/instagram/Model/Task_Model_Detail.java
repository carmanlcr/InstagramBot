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

import com.instagram.Interface.Model;


public class Task_Model_Detail implements Model {
	
	private int tasks_model_id;
	private int tasks_id;
	private String created_at;
	private Date date = new Date();
	private DateFormat dateFormatDateTime = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private static Conexion conn = new Conexion();
	Statement st;
	ResultSet rs;
	
	public void insert() throws SQLException {
		setCreated_at(dateFormatDateTime.format(date));
		String insert = "INSERT INTO tasks_model_detail(tasks_model_id,tasks_id,created_at) VALUE "
				+ " (?,?,?);";
		try (Connection conexion = conn.conectar();){
			
			PreparedStatement exe = conexion.prepareStatement(insert);
			exe.setInt(1, getTasks_model_id());
			exe.setInt(2, getTasks_id());
			exe.setString(3, getCreated_at());
			
			exe.executeUpdate();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}
	
	
	public List<Integer> getTaskModelDetailDiferent(){
		List<Integer> list = new ArrayList<Integer>();
		String queryExce = "SELECT tmd.tasks_id FROM tasks_model_detail tmd " + 
				"WHERE tasks_model_id = ?;";
		try (Connection conexion = conn.conectar();){
			PreparedStatement  query = (PreparedStatement) conexion.prepareStatement(queryExce);
			query.setInt(1, getTasks_model_id());
			rs = query.executeQuery();
			
			while (rs.next() ) {
				 list.add(rs.getInt("tmd.tasks_id"));
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		return list;
	}
	
	public int getTasks_model_id() {
		return tasks_model_id;
	}

	public void setTasks_model_id(int tasks_model_id) {
		this.tasks_model_id = tasks_model_id;
	}

	public int getTasks_id() {
		return tasks_id;
	}

	public void setTasks_id(int tasks_id) {
		this.tasks_id = tasks_id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
	
	

}
