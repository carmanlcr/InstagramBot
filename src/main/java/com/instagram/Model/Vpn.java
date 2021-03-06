package com.instagram.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import configurations.connection.ConnectionIG;
import configurations.interfaces.Model;



public class Vpn implements Model{

	private static final String TABLE_NAME = "vpn";
	private int vpn_id;
	private String name;
	private boolean active;
	private String created_at;
	private String updated_at;
	private Date date;
	private static ConnectionIG conn = new ConnectionIG();
	
	
	public Vpn getVpn() throws SQLException {
		Vpn v = null;
		String sql = "SELECT  * FROM "+TABLE_NAME+" WHERE vpn_id = ?;";
		
		try (Connection conexion = conn.conectar();
				PreparedStatement pre = conexion.prepareStatement(sql);){
		
			pre.setInt(1, getVpn_id());
			
			ResultSet rs = pre.executeQuery();
			
			if(rs.next()) {
				v = new Vpn();
				v.setVpn_id(rs.getInt("vpn_id"));
				v.setName(rs.getString("name"));
				v.setActive(rs.getBoolean("active"));
			}
		}catch (SQLException e) {
			e.getStackTrace();
		}
		
		return v;
		
	}
	
	public Map<Integer,String> getAllActive() throws SQLException {

		
		Map<Integer,String> list = new HashMap<>();
		Statement st = null;
	    ResultSet rs = null;
		try(Connection conexion = conn.conectar();) {
			st = conexion.createStatement();
			
			
			rs = st.executeQuery("SELECT * FROM vpn WHERE active = 1 ORDER BY name ASC");
			
			list.put(0,"Seleccione");
			while (rs.next() ) {
				list.put(rs.getInt("vpn_id"),rs.getString("name"));
               
			}
		}catch(Exception e) {
			System.err.println(e);
		}finally {
			st.close();
		}
		
		return list;
	}
	
	public String getNameVPN(String name) throws SQLException {
		String nameVpn = "";
		Statement st = null;
		ResultSet rs = null;
		
		try (Connection conexion = conn.conectar();){
			
			st = conexion.createStatement();
			
			
			rs = st.executeQuery("SELECT * FROM vpn WHERE name = '"+nameVpn+"';");
			
			while (rs.next() ) {
				nameVpn = rs.getString("name");
               
			}
		}catch(Exception e) {
			System.err.println(e);
		}finally{
			st.close();
		}
		
		return nameVpn;
		
	}
	
	public int getFind(String name) throws SQLException {
		int idVpn = 0;
		ResultSet rs = null;
		Statement st = null;
		
		try (Connection conexion = conn.conectar();){
			
			st = conexion.createStatement();
			String query = "SELECT * FROM vpn WHERE name = '"+name+"';";
			
			rs = st.executeQuery(query);
			
			while (rs.next() ) {
				idVpn = rs.getInt("vpn_id");
               
			}
		}catch(Exception e) {
			System.err.println(e);
		}finally {
			rs.close();
		}
			
		
		
		return idVpn;
	}
	
	public int findOrCreate(String name) throws SQLException {
		int idVpn = 0;
		ResultSet rs = null;
		Statement st = null;
		
		try(Connection conexion = conn.conectar();) {
			
			st = conexion.createStatement();
			String query = "SELECT * FROM vpn WHERE UPPER(name) = '"+name.toUpperCase()+"';";
			
			rs = st.executeQuery(query);
			
			while (rs.next() ) {
				idVpn = rs.getInt("vpn_id");
               
			}
		}catch(Exception e) {
			System.err.println(e);
		}finally {
			rs.close();
		}
			
		if(idVpn==0) {
			setName(name);
			insertVpn();
		}
		
		return idVpn;
	}
	
	public boolean insertVpn() throws SQLException {
		Statement st = null;
		Connection conexion = conn.conectar();
		date = new Date();
		DateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		setCreated_at(formate.format(date));
		setUpdated_at(formate.format(date));
		if(getNameVPN(getName()) == null) {
			return false;
		}else {
			try {
				String insert = "INSERT INTO vpn(name) VALUES ('"+getName()+"','"+getCreated_at()+"','"+getUpdated_at()+"');";
				st = conexion.createStatement();
				st.executeUpdate(insert);
				
				
			} catch(SQLException e)  {
				
				System.err.println(e);
				return false;
			}finally {
				st.close();
				conexion.close();
			}
			
			return true;
		}
		
	}
	
	public Map<String,Integer> getAllVpn(){
		Map<String,Integer> mapGe = new HashMap<>();
		
		String query = "SELECT * FROM "+TABLE_NAME+" v WHERE active = 1;";
		
		try (Connection conexion = conn.conectar();
				PreparedStatement pst = conexion.prepareStatement(query);
				ResultSet rs = pst.executeQuery();){
			
			while (rs.next() ) {
				mapGe.put(rs.getString("v.name"), rs.getInt("v.vpn_id"));
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		
		
		return mapGe;
	}
	
	
	
	public int getVpn_id() {
		return vpn_id;
	}

	public void setVpn_id(int vpn_id) {
		this.vpn_id = vpn_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public void insert() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	
}
