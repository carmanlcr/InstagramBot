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
	private static final String VERSION = "2.1.8";
	private JPanel contentPane;
	private final JMenuBar barMenu = new JMenuBar();
	private final JMenu mnUsuarios = new JMenu("Usuarios");
	private final JMenuItem registrarUsuario = new JMenuItem("Registrar");
	private final JMenuItem buscarUsuario = new JMenuItem("Buscar");
	private final JMenuItem actualizarUsuario = new JMenuItem("Actualizar Usuarios");
	private final JMenu mnVpn = new JMenu("Vpn");
	private final JMenuItem registrarVpn = new JMenuItem("Registrar");
	private final JMenuItem actualizarVpn = new JMenuItem("Actualizar");
	private final JMenu mnCategorias = new JMenu("Categorias");
	private final JMenuItem registrarCategoria = new JMenuItem("Registrar");
	private final JMenuItem mntmRegistrarSubCategorie = new JMenuItem("Registrar Sub Categoria");
	private final JMenu mnFrases = new JMenu("Frases");
	private final JMenuItem registrarFrase = new JMenuItem("Registrar");
	private final JMenuItem registrarHashTag = new JMenuItem("Registrar Hashtag"); 
	private final JMenu mnGenero = new JMenu("Genero");
	private final JMenuItem registrarGenero = new JMenuItem("Registrar Genero");
	private final JMenu mnTask = new JMenu("Tarea");
	private final JMenuItem registrarTarea = new JMenuItem("Registrar Tarea");
	private final JMenu mnPhotos = new JMenu("Fotos");
	private final JMenuItem registrarDireccionDeFotos = new JMenuItem("Registrar Fotos");
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
		registrarUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegistrarUsuario registerUser;
				try {
					registerUser = new RegistrarUsuario();
					registerUser.inicio();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		mnUsuarios.add(registrarUsuario);
		
		
		
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
		
		mnCategorias.add(registrarCategoria);
		mnCategorias.add(mntmRegistrarSubCategorie);
		barMenu.add(mnCategorias);
		
		registrarCategoria.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				RegistrarCategories registrar = new RegistrarCategories();
				registrar.inicio();
			}
		});
		
		mntmRegistrarSubCategorie.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				RegistrarSubCategorie regis;
				try {
					regis = new RegistrarSubCategorie();
					regis.init();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				
			}
		});
		
		
		mnFrases.add(registrarFrase);
		mnFrases.add(registrarHashTag);
		barMenu.add(mnFrases);
		
		registrarFrase.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				RegistrarFrase registrar;
				
					registrar = new RegistrarFrase();
					registrar.inicio();
				
				
			}
		});
		
		registrarHashTag.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				RegistrarHashTag registrar;
				try {
					registrar = new RegistrarHashTag();
					registrar.inicio();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				
			}
		});
		
		
		mnGenero.add(registrarGenero);
		barMenu.add(mnGenero);
		
		registrarGenero.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				RegistrarGenero registrar = new RegistrarGenero();
				registrar.inicio();
			}
			
		});
		
		mnTask.add(registrarTarea);
		barMenu.add(mnTask);
		
		registrarTarea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegistrarTarea regi = new RegistrarTarea();
				regi.init();
			}
		});
		
		mnPhotos.add(registrarDireccionDeFotos);
		barMenu.add(mnPhotos);
		
		registrarDireccionDeFotos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegistrarDireccionFotos dirFotos = new RegistrarDireccionFotos();
				dirFotos.inicio();
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
		iniApli.setVersion(VERSION);
		empezar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					int id = Integer.parseInt(hashCa.get(comboBox.getSelectedItem().toString()).toString());
					int idGenere = Integer.parseInt(hashGe.get(comboBox1.getSelectedItem().toString()).toString());
					Ejecucion eje = new Ejecucion(id,idGenere,iniApli,s);
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
