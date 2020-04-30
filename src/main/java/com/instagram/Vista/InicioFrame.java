package com.instagram.Vista;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.sikuli.script.Screen;

import com.instagram.Model.*;
import com.instagram.Model.Inicio_Aplicacion;

import configurations.controller.SikuliTest;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import java.awt.Color;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Font;

import javax.swing.JComboBox;

public class InicioFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String VERSION = "2.2.0";
	private JPanel contentPane;
	private final JMenuBar barMenu = new JMenuBar();
	private final JMenu mnUsuarios = new JMenu("Usuarios");
	private final JMenuItem buscarUsuario = new JMenuItem("Buscar");
	private final JMenuItem actualizarUsuario = new JMenuItem("Actualizar Usuarios");
	private final JMenu mnVpn = new JMenu("Vpn");
	private final JMenuItem registrarVpn = new JMenuItem("Registrar");
	private final JMenuItem actualizarVpn = new JMenuItem("Actualizar");
	private final JMenu mnTask = new JMenu("Tarea");
	private final JMenuItem registrarTarea = new JMenuItem("Registrar Tarea");
	private static JComboBox<String> comboBox = new JComboBox<>();
	private static JComboBox<String> comboBox1 = new JComboBox<>();
	private static Map<String, Integer> hashCa;
	private static Map<String, Integer> hashGe = new HashMap<>();
	private static JButton empezar = new JButton("Comenzar");
	private static Inicio_Aplicacion iniApli = new Inicio_Aplicacion();
	private final JLabel lblNewLabel1 = new JLabel("Campaña");
	private final JLabel lblNewLabel2 = new JLabel("Genero");
	private static Screen s;
	
	/**
	 * Launch the application.
	 * @throws SQLException 
	 */
	public static void main(String[] args) {
		SikuliTest si = new SikuliTest();
		si.run();
		s = si.getScreen();
		iniApli.setVersion(VERSION);
		if(args.length > 0) {
			iniApli.setVersion(VERSION);
			Ejecucion eje;
			try {
				eje = new Ejecucion(Integer.parseInt(args[0]),Integer.parseInt(args[1]),iniApli,s, Boolean.parseBoolean(args[2]));
				eje.inicio();
			} catch (NumberFormatException | SQLException e) {
				e.printStackTrace();
			} 
			
		}else {
			
		
			final Task_Grid taskG = new Task_Grid();
			hashCa = taskG.getCategoriesToday();
			setComboBox(hashCa);
			
			comboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					comboBox1.removeAll();
					comboBox1.removeAllItems();
					taskG.setCategories_id(Integer.parseInt(hashCa.get(comboBox.getSelectedItem().toString()).toString()));
					hashGe = taskG.getCategoriesAndGeneresToday();
					for(String st : hashGe.keySet()) comboBox1.addItem(st);
					
					if(hashGe.size() > 0) {
						empezar.setEnabled(true);
					}
				}
			});
			
			if(hashGe.size() > 0) {
				empezar.setEnabled(true);
			}
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						InicioFrame frame = new InicioFrame();
						frame.setVisible(true);
	
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		
		}
		
	}

	/**
	 * Create the frame.
	 */
	public InicioFrame() {
		setTitle("InstaBot");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 559, 383);
		barMenu.setFont(new Font("Arial", Font.PLAIN, 12));
		barMenu.setBackground(new Color(51, 153, 204));
		empezar.setEnabled(false);
		setJMenuBar(barMenu);
		mnUsuarios.setFont(new Font("Arial", Font.BOLD, 12));
		
		barMenu.add(mnUsuarios);
				
		
		buscarUsuario.setEnabled(false);
		
		mnUsuarios.add(buscarUsuario);
		actualizarUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					UpdateUsers up = new UpdateUsers();
					up.init();
				}catch (Exception e1) {
					System.err.println(e1);
				}
				
			}
		});
		mnUsuarios.add(actualizarUsuario);
		mnVpn.setFont(new Font("Arial", Font.BOLD, 12));
		
		barMenu.add(mnVpn);
		registrarVpn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistrarVPN registerVpn = new RegistrarVPN();
				registerVpn.inicio();
			}
		});
		
		mnVpn.add(registrarVpn);
		actualizarVpn.setEnabled(false);
		
		
		mnVpn.add(actualizarVpn);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(102, 153, 204));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		

		
		mnTask.add(registrarTarea);
		barMenu.add(mnTask);
		
		registrarTarea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegistrarTarea regi = new RegistrarTarea();
				regi.init();
			}
		});
		
		
		JLabel lblNewLabel = new JLabel(VERSION);
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		
		GroupLayout glContentPane = new GroupLayout(contentPane);
		glContentPane.setHorizontalGroup(
			glContentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(glContentPane.createSequentialGroup()
					.addGap(36)
					.addGroup(glContentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(glContentPane.createSequentialGroup()
							.addGroup(glContentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblNewLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblNewLabel1, GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))
							.addGap(65)
							.addGroup(glContentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE))
							.addContainerGap(170, Short.MAX_VALUE))
						.addGroup(glContentPane.createSequentialGroup()
							.addComponent(lblNewLabel)
							.addPreferredGap(ComponentPlacement.RELATED, 348, Short.MAX_VALUE)
							.addComponent(empezar)
							.addGap(43))))
		);
		
		glContentPane.setVerticalGroup(
			glContentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(glContentPane.createSequentialGroup()
					.addGap(62)
					.addGroup(glContentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel1))
					.addGap(38)
					.addGroup(glContentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel2))
					.addPreferredGap(ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
					.addComponent(lblNewLabel)
					.addGap(46))
				.addGroup(glContentPane.createSequentialGroup()
					.addContainerGap(238, Short.MAX_VALUE)
					.addComponent(empezar, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
					.addGap(36))
		);
		contentPane.setLayout(glContentPane);
		
		empezar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					int id = Integer.parseInt(hashCa.get(comboBox.getSelectedItem().toString()).toString());
					int idGenere = Integer.parseInt(hashGe.get(comboBox1.getSelectedItem().toString()).toString());
					Ejecucion eje = new Ejecucion(id,idGenere,iniApli,s,true);
					setExtendedState(ICONIFIED);
					eje.inicio();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				
			}
		});
	}
	private static JComboBox<String> setComboBox(Map<String, Integer> map) {
		comboBox = new JComboBox<>();
		
		for (String string : map.keySet()) {
			comboBox.addItem(string);
		}
	    return comboBox;
	}

}
