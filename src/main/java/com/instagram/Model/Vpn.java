package com.instagram.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.Statement;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class Vpn {

	private String name;
	private boolean activo;
	private static Conexion conn = new Conexion();
	
	public Map<Integer,String> getAllActive() throws SQLException {

		Connection conexion = conn.conectar();
		Map<Integer,String> list = new HashMap<Integer,String>();
		Statement st = null;
	    ResultSet rs = null;
		try {
			st = (Statement) conexion.createStatement();
			
			
			rs = st.executeQuery("SELECT * FROM vpn WHERE active = 1 ORDER BY name ASC");
			
			list.put(0,"Seleccione");
			while (rs.next() ) {
				list.put(rs.getInt("vpn_id"),rs.getString("name"));
               
			}
		}catch(Exception e) {
			System.err.println(e);
		}finally {
			st.close();
			conexion.close();
		}
		
		return list;
	}
	
	public String getNameVPN(String name) throws SQLException {
		String nameVpn = "";
		Statement st = null;
		ResultSet rs = null;
		Connection conexion = conn.conectar();
		try {
			
			st = (Statement) conexion.createStatement();
			
			
			rs = st.executeQuery("SELECT * FROM vpn WHERE name = '"+nameVpn+"';");
			
			while (rs.next() ) {
				nameVpn = rs.getString("name");
               
			}
		}catch(Exception e) {
			System.err.println(e);
		}finally{
			st.close();
			conexion.close();
		}
		
		return nameVpn;
		
	}
	
	public int getFind(String name) throws SQLException {
		int idVpn = 0;
		ResultSet rs = null;
		Statement st = null;
		Connection conexion = conn.conectar();
		try {
			
			st = (Statement) conexion.createStatement();
			String query = "SELECT * FROM vpn WHERE name = '"+name+"';";
			
			rs = st.executeQuery(query);
			
			while (rs.next() ) {
				idVpn = rs.getInt("vpn_id");
               
			}
		}catch(Exception e) {
			System.err.println(e);
		}finally {
			rs.close();
			conexion.close();
		}
			
		
		
		return idVpn;
	}
	
	public int findOrCreate(String name) throws SQLException {
		int idVpn = 0;
		ResultSet rs = null;
		Statement st = null;
		Connection conexion = conn.conectar();
		try {
			
			st = (Statement) conexion.createStatement();
			String query = "SELECT * FROM vpn WHERE UPPER(name) = '"+name.toUpperCase()+"';";
			
			rs = st.executeQuery(query);
			
			while (rs.next() ) {
				idVpn = rs.getInt("vpn_id");
               
			}
		}catch(Exception e) {
			System.err.println(e);
		}finally {
			rs.close();
			conexion.close();
		}
			
		if(idVpn==0) {
			setName(name);
			insert();
		}
		
		return idVpn;
	}
	
	public boolean insert() throws SQLException {
		Statement st = null;
		Connection conexion = conn.conectar();
		if(getNameVPN(getName()) == null) {
			return false;
		}else {
			try {
				String insert = "INSERT INTO vpn(name) VALUES ('"+getName()+"');";
				st = (Statement) conexion.createStatement();
				st.executeUpdate(insert);
				
				
			}catch(MySQLIntegrityConstraintViolationException e) {
				System.err.println(e);
				return false;
			} catch(Exception e)  {
				
				System.err.println(e);
				return false;
			}finally {
				st.close();
				conexion.close();
			}
			
			return true;
		}
		
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	
}
