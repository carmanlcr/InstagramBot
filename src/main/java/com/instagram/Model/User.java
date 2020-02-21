package com.instagram.Model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.instagram.Interface.Model;


public class User implements Model{
	
	private final String TABLE_NAME = "users";	
	private int users_id;
	private String username;
	private String email;
	private String full_name;
	private BigInteger phone;
	private String password;
	private String creator;
	private String date_of_birth;
	private boolean isBlock;
	private int categories_id;
	private int sim_card_number;
	private int vpn_id;
	private boolean active;
	private static Conexion conn = new Conexion();
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String created_at;
	private String updated_at;
	
	
	public User getUser() throws SQLException{
		User user = null;
		String query = "SELECT u.users_id, u.username, u.email, u.full_name, u.phone, "
				+ "u.password, u.creator, u.date_of_birth, u.active, u.sim_card_number, "
				+ "u.vpn_id, IFNULL(ub.users_block_id,0) as user_blo_null "
				+ "FROM "+TABLE_NAME+" u "
				+ "LEFT JOIN users_block ub ON ub.users_id = u.users_id AND ub.active = 1 "
				+ "WHERE u.email = '"+getEmail()+"' OR u.username= '"+getUsername()+"';";
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();
				ResultSet rs = st.executeQuery(query);){
			while (rs.next() ) {
               user = new User();
               user.setUsers_id(rs.getInt("u.users_id"));
               user.setUsername(rs.getString("u.username"));
               user.setPassword(rs.getString("u.password"));
               user.setVpn_id(rs.getInt("u.vpn_id"));
               user.setEmail(rs.getString("u.email"));
               user.setActive(rs.getBoolean("u.active"));
               user.setCreator(rs.getString("u.creator"));
               boolean block = rs.getInt("user_blo_null")  == 0 ? false : true; 
               user.setBlock(block);
			}
			
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return user;
 	}
	
	
	public String[] getOneRandom() throws SQLException{
		String[] list = null;
		String query = "SELECT * FROM "+TABLE_NAME+" us "
				+ "INNER JOIN vpn vp ON vp.vpn_id = us.vpn_id "
				+ "ORDER BY rand() LIMIT 1";
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();
				ResultSet rs = st.executeQuery(query);){
			
			list = new String[6];
			while (rs.next() ) {
               list[0] =  rs.getString("us.users_id");
               list[1] = rs.getString("us.username");
               list[2] = rs.getString("us.phone");
               list[3] = rs.getString("us.password");
               list[4] = rs.getString("vp.name");
               list[5] = rs.getString("us.email");
               
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return list;
		
	}
	
	public int getIdUser(){
		int id = 0;
		
		String query = "SELECT us.users_id FROM "+
				TABLE_NAME+" us WHERE username = '"+
				getUsername()+"' GROUP BY us.users_id;";
		
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();
				ResultSet rs = st.executeQuery(query)){
			
			while (rs.next() ) {
               id =  rs.getInt("us.users_id");
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return id;
		
	}
	
	public void insert() throws SQLException {
		date = new Date();
		setCreated_at(dateFormat.format(date));
		setUpdated_at(dateFormat.format(date));
		try (Connection conexion = conn.conectar();){
			String insert = "INSERT INTO "+TABLE_NAME+"(username,email,full_name,phone,password,"
					+ "creator,date_of_birth,created_at,updated_at,sim_card_number,vpn_id)"
					+ " VALUES (?,?,?,?,?, ?,?,?,?,?,?);";
			PreparedStatement exe = conexion.prepareStatement(insert);
			exe.setString(1, getUsername());
			exe.setString(2, getEmail());
			exe.setString(3, getFull_name());
			exe.setBigDecimal(4, new BigDecimal(getPhone()));
			exe.setString(5, getPassword());
			exe.setString(6, getCreator());
			exe.setString(7, getDate_of_birth());
			exe.setString(8, getCreated_at());
			exe.setString(9, getUpdated_at());
			exe.setInt(10, getSim_card_number());
			exe.setInt(11, getVpn_id());
			
			exe.executeUpdate();
			
			User_Categorie usercate = new User_Categorie();
			usercate.setCategories_id(getCategories_id());
			usercate.setUsers_id(getIdUser());
			usercate.insert();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		
	}
	

	
	public String getDifferentRandomUser(String username) throws SQLException{
		String list = null;
		
		String query = "SELECT * FROM "+TABLE_NAME+" us" 
				+ " WHERE NOT us.username = '"+username+"' "
				+ " ORDER BY RAND() LIMIT 1;";
		
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();
				ResultSet rs = st.executeQuery(query);){
			while (rs.next() ) {
				list = rs.getString("us.username");
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return list;
		
	}
	
	public List<String> getUserCategorieAndGenere(int id, int id_genere) throws SQLException{
		ArrayList<String> lista = new ArrayList<String>();
		dateFormat = new SimpleDateFormat("yyy-MM-dd");
		String query = "SELECT u.username FROM tasks_grid tg " + 
				"INNER JOIN tasks_grid_detail tgd ON tg.tasks_grid_id = tgd.tasks_grid_id " + 
				"INNER JOIN "+TABLE_NAME+" u ON u.users_id = tgd.users_id " + 
				"WHERE tgd.users_id NOT IN (SELECT pt.users_id FROM posts pt WHERE DATE(pt.created_at) = ? AND pt.tasks_grid_id = tg.tasks_grid_id) " + 
				"AND tg.categories_id = ? AND tg.generes_id = ? AND tg.active = ? AND DATE(tg.date_publication) = ? " + 
				"ORDER BY tg.date_publication ASC;";
		date = new Date();
		try (Connection conexion = conn.conectar();
				PreparedStatement pre = conexion.prepareStatement(query);){
			pre.setString(1, dateFormat.format(date));
			pre.setInt(2, id);
			pre.setInt(3, id_genere);
			pre.setInt(4, 1);
			pre.setString(5, dateFormat.format(date));
			ResultSet rs = pre.executeQuery();
			while (rs.next() ) {
				lista.add(rs.getString("u.username"));
			}
			
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return lista;
 	}
	
	public List<String> getUserCategories() throws SQLException{
		List<String> list = new ArrayList<String>();
		String query = "SELECT us.username "
				+ "FROM "+TABLE_NAME+" us "
				+ "INNER JOIN users_categories uc ON uc.users_id = us.users_id "
				+ "WHERE uc.categories_id = "+getCategories_id()+" "
				+ "GROUP BY us.username";
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();
				ResultSet rs = st.executeQuery(query)){
			
			while (rs.next() ) {
			   list.add(rs.getString("us.username"));
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		return list;
	}

	public int getUsers_id() {
		return users_id;
	}


	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}


	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getFull_name() {
		return full_name;
	}



	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}



	public BigInteger getPhone() {
		return phone;
	}



	public void setPhone(BigInteger phone) {
		this.phone = phone;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getCreator() {
		return creator;
	}



	public int getCategories_id() {
		return categories_id;
	}

	public void setCategories_id(int categories_id) {
		this.categories_id = categories_id;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}



	public String getDate_of_birth() {
		return date_of_birth;
	}



	public void setDate_of_birth(String date_of_birth) {
		this.date_of_birth = date_of_birth;
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




	public int getSim_card_number() {
		return sim_card_number;
	}



	public void setSim_card_number(int sim_card_number) {
		this.sim_card_number = sim_card_number;
	}



	public int getVpn_id() {
		return vpn_id;
	}



	public void setVpn_id(int vpn_id) {
		this.vpn_id = vpn_id;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}


	public boolean isBlock() {
		return isBlock;
	}


	public void setBlock(boolean isBlock) {
		this.isBlock = isBlock;
	}

	
	

}
