package com.instagram.Controlador;

import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

import com.instagram.Model.*;



public class InicioController {
	
	private final String PAGE = "www.instagram.com";
	private final String PATH_IMAGES_SIKULI = "C:\\ImagenesSikuli\\";
	private Post post1 = new Post();
	private static String[] user;
	private static User users;
	private static RobotController robot;
	private List<JCheckBox> usuarios;
	private ArrayList<JTextArea> pieDeFoto = new ArrayList<JTextArea>();
	private static List<List<String>> checkBoxHashTag = new ArrayList<List<String>>();
	private static List<JComboBox<String>> comboBoxGenere = new ArrayList<JComboBox<String>>();
	private List<JTextField> listUsers = new ArrayList<JTextField>();
	private int idUser;
	private int categoria_id;
	private int idGenere;
	private int usuariosAProcesar = 1;
	private int ini = 0;
	private int count = 0;
	private boolean banderaVpn = false; 
	private boolean banderaToggle = true;
	private Screen screen;
	@SuppressWarnings("static-access")
	public InicioController(int categoria_id, List<JCheckBox> listCheckBoxUsersSend, List<JTextArea> listTextARea,
			List<List<String>> listChechBoxSelected, List<JTextField> listTextFieldUser,
			List<JComboBox<String>> listJComboBoxGenere, Screen screen) {
		this.categoria_id = categoria_id;
		this.usuarios = listCheckBoxUsersSend;
		this.pieDeFoto = (ArrayList<JTextArea>) listTextARea;
		this.checkBoxHashTag = listChechBoxSelected;
		this.listUsers = listTextFieldUser;
		this.comboBoxGenere = listJComboBoxGenere;
		this.screen = screen;

	}


	public void init() throws InterruptedException, AWTException, SQLException, IOException, FindFailed {
		count = comboBoxGenere.size() - 1;
		
		for (JCheckBox jCheckBox : usuarios) {
			
			users = new User();
			users.setUsername(jCheckBox.getText());
			users.setEmail(jCheckBox.getText());
			user = users.getUser();
			idUser = Integer.parseInt(user[0]);
			Post post = new Post();
			post.setUsers_id(Integer.parseInt(user[0]));
			int idlistTask = post.getLastsTasktPublic();
			
			if(idlistTask == 0) {
				System.out.println("El usuario no tiene mas tareas por publicar");
			}else {
				robot = new RobotController();
				
				String ip = validateIP();
				VpnController vpn = new VpnController(robot);
				vpn.iniciarVpn(user[4], banderaVpn);
				String ipActual = validateIP();
				
				System.out.println(usuariosAProcesar+" usuario(s) de "+usuarios.size()+" usuario(s)");
				//Valida si la vpn conecto
				if(ip.equals(ipActual)) {	
					System.err.println("El usuario "+user[1]+ " no se puedo conectar a la vpn");
					usuariosAProcesar++;
				}else {
					//Lanzamiento de la pagina   
					robot.openChromeIncognit();
				    Thread.sleep(getNumberRandomForSecond(5540, 7150));
				    //Maximizar Chrome
					robot.maximizar();
					//Escribir la pagina a ingresar
					robot.inputWrite(PAGE);
					//Darle enter parawww ir a la pagina
					robot.enter();
					Thread.sleep(getNumberRandomForSecond(12540, 14150));
					//Cambiar a developer
					robot.changeDeveloper(banderaToggle);
					Thread.sleep(getNumberRandomForSecond(1540, 2150));
					if(screen.exists(PATH_IMAGES_SIKULI+"instagram_install.png") != null) {
						robot.dimensions(633, 519);
						Thread.sleep(getNumberRandomForSecond(540, 650));
						robot.clickPressed();
					}else {
						robot.dimensions(633, 494);
						Thread.sleep(getNumberRandomForSecond(540, 650));
						robot.clickPressed();
					}
					
					Thread.sleep(getNumberRandomForSecond(2254, 3984));

					System.out.println("*********************"+user[1]+"***********************");
					IniciaSesion sesion = new IniciaSesion(user[1],user[3],screen);
					sesion.init();
					
					
					//Esperar que cargue la pagina para que cargue el dom completamente
					Thread.sleep(getNumberRandomForSecond(5250, 5650)); 
					robot.changeDeveloper(banderaToggle);
					Thread.sleep(getNumberRandomForSecond(1250, 1650)); 
					if(!blockUser()) {
						startProgram(idlistTask);
					}

					//Desconectar la vpn para el siguiente usuario
					usuariosAProcesar++;
					robot.close();
					vpn.desconectVpn();
					banderaVpn = true;
					banderaToggle = false;
					Thread.sleep(getNumberRandomForSecond(1999, 2125));
				}//Fin del if
			}
			
				
		}//Fin del for
			System.out.println("Finalizo con exito el programa");
			System.exit(1);
	}//Fin del init
	
	/**
	 * 
	 * 
	 */
	private boolean blockUser() {
		
		if(screen.exists("C:\\ImagenesSikuli\\validate-block1.png") != null) {
			userBlock("Confirma que eres tu para iniciar sesion");
			return true;
		}else if(screen.exists("C:\\ImagenesSikuli\\validate-block2.png") != null) {
			userBlock("Intento de inicio de sesion sospechoso");
			return true;
		}else if(screen.exists("C:\\ImagenesSikuli\\validate-block3.png") != null) {
			userBlock("Intento de inicio de sesion sospechoso");
			return true;
		}else if(screen.exists("C:\\ImagenesSikuli\\validate-block4.png") != null) {
			userBlock("Intento de inicio de sesion sospechoso");
			return true;
		}
		
		return false;
	}
	/**
	 * Inico del proceso de instagram luego que el inicio de sesi�n sea exitoso
	 * @param idlistTask 
	 * 
	 * @throws InterruptedException
	 * @throws SQLException
	 * @throws FindFailed 
	 */
	private void startProgram(int idlistTask) throws InterruptedException, SQLException, FindFailed {
		
			
		System.out.println("Usuario logueado");
		robot.dimensions(642, 647);
		Thread.sleep(getNumberRandomForSecond(1265, 1658));
		robot.clickPressed();
		
		
		scrollDown(34);
		scrollUp(34);
		
		System.out.println("Buscando tareas que haya publicado el usuario");

		
		Task_Model_Detail tmd = new Task_Model_Detail();
		tmd.setTasks_model_id(idlistTask);
		List<Integer> listTask = tmd.getTaskModelDetailDiferent();
		System.out.println("Buscando tareas para publicaci�n del usuario");
		random(listTask,idlistTask);
		
		
		System.out.println("***************El usuario hizo todas las publicaciones correctamente*****************");
		
		sesionClose();
		
		
		System.out.println("Se cerro la sesion del usuario "+user[1]);
	}
	
	private void random(List<Integer> listTask, int taskModel_Id) throws InterruptedException, SQLException, FindFailed {
		int valueScroll = (int) (Math.random() * 15) + 1;
		int taskModelId = taskModel_Id;
		for(Integer li : listTask) {
//			li = 7;
			switch(li) {
				case 1:
					//Publicar Historia
					/*uploadStory();
					System.out.println("El usuario publico una historia");*/
					break;
				case 2:
					//Revisar las notificaciones
					reviewNotifications();
					break;
				case 3:
					//Revisar los mensajes
					reviewMessage();
					break;
				case 4:
					//Publicacion final
					publicFinal(taskModelId);
					break;
				case 5:
					//Entrar en perfil y dar like
					perfilLike(valueScroll);
					break;
				case 6:
					//Publicacion normal
					uploadImage();
				case 7:
					followUsers();
				default:
					break;
			}	
			returnHome();
		}//Fin del For
		
	}
	
	private void scrollDown(int value) throws InterruptedException {
		robot.mouseScroll(value);
		Thread.sleep(getNumberRandomForSecond(1098, 1896));
		
	}
	
	private void scrollUp(int value) throws InterruptedException{
		robot.mouseScroll(value * -1);
		Thread.sleep(getNumberRandomForSecond(1098, 1896));
	}

	
	private String uploadImageFinal(String pie, String usuario) throws InterruptedException, SQLException, FindFailed {
		int value = (int) (Math.random() * 100000) + 1;
		int dimensionx = (int) (Math.random() * 3) + 0;
		int dimensiony = (int) (Math.random() * 5) + 0;
		//630,957
		System.out.println("Abrir el selector de imagenes");
		
		if(screen.exists(PATH_IMAGES_SIKULI+"add-images.png") != null) {
			screen.click(PATH_IMAGES_SIKULI+"add-images.png");
		}else {
			robot.dimensions(630, 957);
			Thread.sleep(getNumberRandomForSecond(256, 985));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(3548, 4572));
		//Ponerse para cambiar el tipo de archivo
		robot.dimensions(500, 919);
		Thread.sleep(getNumberRandomForSecond(256, 985));
		robot.clickPressed();
		Thread.sleep(getNumberRandomForSecond(1048, 1572));
		//Darle a todos los archivos
		robot.dimensions(541, 957);
		Thread.sleep(getNumberRandomForSecond(256, 985));
		robot.clickPressed();
		Thread.sleep(getNumberRandomForSecond(1048, 1572));
		String user = (String) comboBoxGenere.get(ini).getSelectedItem();
		Genere gene = new Genere();
		gene.setName(user);
		idGenere = gene.getIdGenere();
		System.out.println("Buscar direccion de fotos");
		Path_Photo pa_ph = new Path_Photo();
		pa_ph.setCategories_id(categoria_id);
		pa_ph.setGeneres_id(idGenere);
		String path = pa_ph.getPathPhotos();
		System.out.println("La direccion seleccionada es "+path);
		Thread.sleep(getNumberRandomForSecond(2250, 3863));
		//Darle click a la carpeta imagenes
		RobotController click = new RobotController();
		click.maxiIzquierda();
		Thread.sleep(getNumberRandomForSecond(2001, 2099));
		click.dimensions(364, 44);
		//Hacer click
		click.clickPressed();
		System.out.println("presionar para cambiar la direccion de busqueada");
		Thread.sleep(getNumberRandomForSecond(1502, 1539));
		click.copy(path);
		Thread.sleep(getNumberRandomForSecond(720, 853));
		click.paste();
		Thread.sleep(getNumberRandomForSecond(720, 854));
		click.enter();
		System.out.println("Enter para entrar en la direccion especificada");
		Thread.sleep(getNumberRandomForSecond(2001, 2098));
				
		click.dimensions(220, 180);
		click.clickPressed();
		for(int i = 0; i <= value;i++) {
			click.mouseScroll(1);
		}
		System.out.println("Elegir una foto de manera aleatoria");
		int[] arrayx = new int[4];
		int[] arrayy = new int[6];
		arrayx[0] = 220;
		arrayx[1] = 330;
		arrayx[2] = 440;
		arrayx[3] = 540;
		arrayy[0] = 180;
		arrayy[1] = 280;
		arrayy[2] = 420;
		arrayy[3] = 540;
		arrayy[4] = 680;
		arrayy[5] = 810;
		Thread.sleep(getNumberRandomForSecond(2001, 2099));
		click.dimensions(arrayx[dimensionx], arrayy[dimensiony]);
		//Hacer doble click
		click.clickPressed();
		click.clickPressed();
		System.out.println("Seleccionar foto");
		
		//1215,132
		
		
		Thread.sleep(2000); 
		System.out.println("Siguiente");
		robot.dimensions(1215, 132);
		Thread.sleep(getNumberRandomForSecond(478, 896));
		robot.clickPressed();
		Thread.sleep(getNumberRandomForSecond(2489, 3549));
		//Escribir pie de foto
		Phrases frase = new Phrases();
		frase.setCategories_id(categoria_id);
		frase.setGeneres_id(idGenere);
		
		Phrases fraseRandom = frase.getPhraseRandom();
		System.out.println("Encontrar frase");
		post1.setPhrases_id(fraseRandom.getPhrases_id());
		post1.setLink_publcation(pie);
		post1.setUser_transmition(usuario);
		List<String> copia = checkBoxHashTag.get(ini);
		String hash = "";
	    
		if (copia.size() > 2) {
			Collections.shuffle(copia);
			
			hash += copia.get(0) +  " ";
			hash += copia.get(1) +  " ";
			hash += copia.get(2) +  " ";
			
		} else if (copia.size() > 0 && copia.size() < 2) {
			Collections.shuffle(copia);
			
			hash += copia.get(0) +  " ";
		}
		//234, 192
		robot.dimensions(234, 192);
		Thread.sleep(getNumberRandomForSecond(478, 896));
		robot.clickPressed();
		Thread.sleep(getNumberRandomForSecond(2489, 3549));
		
		robot.inputWriteUsers(fraseRandom.getPhrase()+" "+pie+" "+hash+" ",usuario);
//		inputWritePieDeFoto("_472V_",fraseRandom.getPhrase()+""+pie+" "+hash,usuario);
		Thread.sleep(getNumberRandomForSecond(1489, 1549));
		//1215,134
		
		robot.dimensions(1215, 134);
		Thread.sleep(getNumberRandomForSecond(478, 896));
		robot.clickPressed();
		System.out.println("El usuario hizo la publicaci�n correctamente.");
		
		Thread.sleep(getNumberRandomForSecond(8001, 10099));
										
		
		return hash;
	}
	
	private void uploadImage() throws InterruptedException, SQLException, FindFailed {
		Categories ca = new Categories();
		List<String> la = ca.getSubCategorieConcat();
		if(la.size() < 1) {
			int value = (int) (Math.random() * 100000) + 1;
			int dimensionx = (int) (Math.random() * 3) + 0;
			int dimensiony = (int) (Math.random() * 5) + 0;
			System.out.println("Abrir el selector de imagenes");
			if(screen.exists(PATH_IMAGES_SIKULI+"add-images.png") != null) {
				screen.click(PATH_IMAGES_SIKULI+"add-images.png");
			}else {
				robot.dimensions(630, 957);
				Thread.sleep(getNumberRandomForSecond(256, 985));
				robot.clickPressed();
			}
			Thread.sleep(getNumberRandomForSecond(3548, 4572));
			
			//Darle click a la carpeta imagenes
			RobotController click = new RobotController();
			System.out.println("Abierto el buscador de fotos");
			click.maxiIzquierda();
			Thread.sleep(getNumberRandomForSecond(2001, 2099));
			click.dimensions(364, 44);
			//Hacer click
			click.clickPressed();
			Thread.sleep(getNumberRandomForSecond(1502, 1539));
			
			Path_Photo pathP = new Path_Photo();
			int cate_id = Integer.parseInt(la.get(0));
			int subCate_id = Integer.parseInt(la.get(2));
			System.out.println("Buscar direccion de fotos");
			pathP.setCategories_id(cate_id);				
			pathP.setSub_categories_id(subCate_id);
			String pathPho = pathP.getPathPhotos();
			click.copy(pathPho);
			System.out.println("La direcci�n a buscar en fotos es: "+pathPho);
			Thread.sleep(getNumberRandomForSecond(720, 853));
			click.paste();
			Thread.sleep(getNumberRandomForSecond(720, 854));
			click.enter();
				
			Thread.sleep(getNumberRandomForSecond(2001, 2098));
					
			click.dimensions(220, 180);
			click.clickPressed();
			for(int i = 0; i <= value;i++) {
				click.mouseScroll(1);
			}
			
			System.out.println("Elegir una foto de manera aleatoria");
			int[] arrayx = new int[4];
			int[] arrayy = new int[6];
			arrayx[0] = 220;
			arrayx[1] = 330;
			arrayx[2] = 440;
			arrayx[3] = 540;
			arrayy[0] = 180;
			arrayy[1] = 280;
			arrayy[2] = 420;
			arrayy[3] = 540;
			arrayy[4] = 680;
			arrayy[5] = 810;
			Thread.sleep(getNumberRandomForSecond(2001, 2099));
			click.dimensions(arrayx[dimensionx], arrayy[dimensiony]);
			//Hacer doble click
			click.clickPressed();
			click.clickPressed();
			System.out.println("Seleccionar foto");
			
			
			System.out.println("Siguiente");
			robot.dimensions(1215, 132);
			Thread.sleep(getNumberRandomForSecond(478, 896));
			robot.clickPressed();
			Thread.sleep(getNumberRandomForSecond(2489, 3549));
			
			Thread.sleep(2000); 
			//Escribir pie de foto
			Phrases frase = new Phrases();
			frase.setCategories_id(cate_id);
			frase.setSub_categories_id(subCate_id);
			String phrase = frase.getPhraseRandomSubCategorie();
			System.out.println("Encontrar frase");
			robot.dimensions(234, 192);
			Thread.sleep(getNumberRandomForSecond(478, 896));
			robot.clickPressed();
			Thread.sleep(getNumberRandomForSecond(2489, 3549));
			robot.inputWrite(phrase);
			Thread.sleep(getNumberRandomForSecond(1489, 1549));
			//1215,134
			
			robot.dimensions(1215, 134);
			Thread.sleep(getNumberRandomForSecond(478, 896));
			robot.clickPressed();
			System.out.println("El usuario hizo la publicaci�n correctamente.");
			
			Thread.sleep(getNumberRandomForSecond(8001, 10099));
			
			Thread.sleep(getNumberRandomForSecond(2001, 2099)); 
			//Compartir foto
		}else {
			System.out.println("El usuario no puede publicar normal ya que las subcategorias no contienen, frase o direccion de fotos");
		}
		
		
	}
	

	
	
	
	/**
	 * 
	 * Pulsa el bot�n de retornar 
	 * @throws InterruptedException 
	 * @throws FindFailed 
	 * 
	 */
	private void returnHome() throws InterruptedException, FindFailed {
		System.out.println("Retornar al inicio");
		
		if(screen.exists(PATH_IMAGES_SIKULI+"home.png") != null) {
			screen.click(PATH_IMAGES_SIKULI+"home.png");
		}else if(screen.exists(PATH_IMAGES_SIKULI+"home1.png") != null) {
			screen.click(PATH_IMAGES_SIKULI+"home1.png");
		}else {
			robot.dimensions(123, 968);
			Thread.sleep(getNumberRandomForSecond(152, 899));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(1952, 2099));
		System.out.println("Volvio al inicio");
		
		scrollDown(154);
		scrollUp(154);
	}
	
	private void reviewNotifications() throws InterruptedException, FindFailed {
		System.out.println("REVISAR LAS NOTIFICACIONES");
		System.out.println("Ingreso en las notificaciones");
		if(screen.exists(PATH_IMAGES_SIKULI+"notifications.png") != null) {
			screen.click(PATH_IMAGES_SIKULI+"notifications.png");
		}else {
			robot.dimensions(889, 966);
			Thread.sleep(getNumberRandomForSecond(847, 898));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(3001, 3099));
		//Hacer scroll en las notificaciones
		System.out.println("Hacer Scrool en notificaciones");
		scrollDown(30);
		scrollUp(30);
		Thread.sleep(getNumberRandomForSecond(1254, 1267));
		//Volver al inicio
		returnHome();
	}
	
	private void reviewMessage() throws InterruptedException, FindFailed {
		System.out.println("REVISAR LOS MENSAJES");
		if(screen.exists(PATH_IMAGES_SIKULI+"message.png") != null) {
			screen.click(PATH_IMAGES_SIKULI+"message.png");
		}else {
			robot.dimensions(1242, 125);
			Thread.sleep(getNumberRandomForSecond(689, 897));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(1689, 1759));
		
		System.out.println("Hacer scroll en los mensajes");
		scrollDown(30);
		scrollUp(30);
		Thread.sleep(getNumberRandomForSecond(1254, 1267));
		robot.pressEsc();
		//Volver al inicio
		System.out.println("Volver al inicio");
		
		if(screen.exists(PATH_IMAGES_SIKULI+"back-message.png") != null) {
			screen.click(PATH_IMAGES_SIKULI+"back-message.png");
		}else {
			robot.dimensions(36, 131);
			Thread.sleep(getNumberRandomForSecond(689, 759));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(1952, 2099));
	}
		
	private void publicFinal(int taskModelId) throws InterruptedException, SQLException, FindFailed {
		System.out.println("PUBLICACION DE: "+listUsers.get(ini).getText());
		String hash = uploadImageFinal(pieDeFoto.get(ini).getText(),listUsers.get(ini).getText());
		
		Thread.sleep(getNumberRandomForSecond(1001, 2558));
		ini++;

		if(ini > count) {
			ini = 0;
		}
		
		registerPost(taskModelId, hash);
		returnHome();
		System.out.println("El usuario publico correctamente");
	}
	
	private void registerPost(int taskModelId, String hash) throws SQLException, InterruptedException, FindFailed {
		post1.setUsers_id(Integer.parseInt(user[0]));
		post1.setCategories_id(Integer.parseInt(user[6]));
		post1.setTasks_model_id(taskModelId);

		//Ir al perfil del usuario
		robot.dimensions(1142, 967);
		Thread.sleep(getNumberRandomForSecond(254, 621));
		robot.clickPressed();
		Thread.sleep(getNumberRandomForSecond(2001, 3099));
		//Seleccionar la ultima foto que publico
		robot.dimensions(319, 745);
		Thread.sleep(getNumberRandomForSecond(254, 456));
		robot.clickPressed();
		Thread.sleep(getNumberRandomForSecond(3325, 3878));
		//Seleccionar el link
		robot.dimensions(315, 50);
		Thread.sleep(getNumberRandomForSecond(254, 456));
		robot.clickPressed();
		Thread.sleep(getNumberRandomForSecond(3325, 3878));
		robot.copyCtrlC();
		
		Thread run = new Thread(new Runnable() {
			
			public void run() {
				try {
					Thread.sleep(2556);
					robot.paste();
					Thread.sleep(14);
					robot.enter();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		run.start();
		String link_instagram = JOptionPane.showInputDialog("PRESIONAR ctrl+v Y PULSE ACEPTAR");
		robot.pressEsc();
		
		post1.setLink_instagram(link_instagram);
		if(post1.getLink_instagram() == null 
				|| !validateUrl(link_instagram)) {
			System.out.println("No se agragará el post ya que no hay link para agregar");
		}else {
			post1.insert();
			
			String[] hashT = hash.split(" ");
			
			Post_Detail postDe = new Post_Detail();
			postDe.setPosts_id(post1.getLast());
			HashTag has = new HashTag();
			
			for(int i = 0;i < hashT.length; i++) {
				has.setName(hashT[i]);
				postDe.setHashtag_id(has.getIdCategorieHashTag());
				postDe.insert();
			}
		}
		returnHome();
		
		
	}
	
	private boolean validateUrl(String path) {
		for(int i = 0; i<path.length()-6; i++) {
			if(path.substring(i,i+6).equals(".com/p")) {
				return true;
			}
		}
		return false;
	}
	private void perfilLike(int valueScroll) throws InterruptedException, SQLException, FindFailed {
		System.out.println("ENTRAR EN PERFIL Y DAR LIKE!");
		Account_Instagram_User acount = new Account_Instagram_User();
		acount.setUsers_id(Integer.parseInt(user[0]));
		Account_Instagram acInsta = acount.getAccountsFollowUser();
		//Buscar usuario
		if(acInsta != null && searchUser(acInsta)) {
			Thread.sleep(getNumberRandomForSecond(4963, 5099));
			//Seguir o dejar de seguir
			follow(acInsta);
			//Hacer scroll a su perfil
			System.out.println("Hacer scroll");
			scrollDown(7);
			//darle click a la primera imagen
			//310,500
			robot.dimensions(310, 500);
			Thread.sleep(getNumberRandomForSecond(256, 895));
			robot.clickPressed();
			Thread.sleep(getNumberRandomForSecond(2654, 3095));
			
			System.out.println("Se abrio la primera imagen del perfil");
			
			//808,722
			//Darle click a like
			if(screen.exists(PATH_IMAGES_SIKULI+"like.png") != null) {
				screen.click(PATH_IMAGES_SIKULI+"like.png");
			}else {
				robot.dimensions(808, 722);
				Thread.sleep(getNumberRandomForSecond(256, 895));
				robot.clickPressed();
			}
			Thread.sleep(getNumberRandomForSecond(1654, 2095));
			
			robot.pressEsc();
			
			scrollUp(7);
			
		}else {
			
		}
		//Volver al inicio
		System.out.println("Volver al inicio");
		returnHome();
	}
	
	private boolean searchUser(Account_Instagram account) throws InterruptedException, FindFailed {
		System.out.println("Ingresar a buscar usuario");
		//Darle click al boton de search
		if(screen.exists(PATH_IMAGES_SIKULI+"search.png") != null) {
			screen.click(PATH_IMAGES_SIKULI+"search.png");
		}else {
			robot.dimensions(385, 965);
			Thread.sleep(getNumberRandomForSecond(264, 658));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(2264, 2658));
		
		System.out.println("Darle al boton de buscar usuario");
		
		//Darle clic al input donde se escribe 
		if(screen.exists(PATH_IMAGES_SIKULI+"search-text.png") != null) {
			screen.click(PATH_IMAGES_SIKULI+"search-text.png");
		}else {
			robot.dimensions(645, 131);
			Thread.sleep(getNumberRandomForSecond(264, 658));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(2264, 2658));
		
		System.out.println("Escribiendo en el cuadro de texto");
		//Escribir en el input
		robot.inputWrite(account.getAccount());
		Thread.sleep(getNumberRandomForSecond(798, 1245));
		
		//293,243
		//Pararse sobre el usuario y hacerle click para entrar
		System.out.println("Hacer click sobre el usuario");
		robot.dimensions(293, 243);
		Thread.sleep(getNumberRandomForSecond(264, 658));
		robot.clickPressed();

		
		return true;
		
	}
	
	
	private void follow(Account_Instagram account) throws InterruptedException, FindFailed {
		//Ponerse en el boton de seguir
		//908,234
		System.out.println("Pulsar el boton de seguir");
		if(screen.exists(PATH_IMAGES_SIKULI+"follow-user.png") != null) {
			screen.click(PATH_IMAGES_SIKULI+"follow-user.png");
		}else {
			robot.dimensions(908, 234);
			Thread.sleep(getNumberRandomForSecond(289, 645));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(2456, 3215));
		
		Account_Instagram_User aco = new Account_Instagram_User();
		aco.setUsers_id(Integer.parseInt(user[0]));
		aco.setAccounts_instagram_id(account.getAccounts_instagram_id());
		try {
			aco.insert();
			System.out.println("Se siguio al usuario "+account.getAccount());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void sesionClose() throws InterruptedException, FindFailed {
		Thread.sleep(getNumberRandomForSecond(2002, 2065));
		//Ir a mi perfil
		System.out.println("Ir al perfil del usuario");		
		//1142 , 967
		if(screen.exists(PATH_IMAGES_SIKULI+"profile.png") != null) {
			screen.click(PATH_IMAGES_SIKULI+"profile.png");
		}else {
			robot.dimensions(1142, 967);
			Thread.sleep(getNumberRandomForSecond(254, 621));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(3001, 3099));
		
		//Darle click a la barra de opciones
		System.out.println("Darle a la barra de opciones");
		if(screen.exists(PATH_IMAGES_SIKULI+"option.png") != null) {
			screen.click(PATH_IMAGES_SIKULI+"option.png");
		}else {
			robot.dimensions(35, 131);
			Thread.sleep(getNumberRandomForSecond(254, 621));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(2001, 3099));
		System.out.println("Darle a Cerrar Sesion");
		if(screen.exists(PATH_IMAGES_SIKULI+"close-session.png") != null) {
			screen.click(PATH_IMAGES_SIKULI+"close-session.png");
		}else {
			robot.dimensions(67, 904);
			Thread.sleep(getNumberRandomForSecond(254, 621));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(2001, 3099));
		
		//642,580
		if(screen.exists(PATH_IMAGES_SIKULI+"exit.png") != null) {
			screen.click(PATH_IMAGES_SIKULI+"exit.png");
		}else {
			robot.dimensions(642, 580);
			Thread.sleep(getNumberRandomForSecond(254, 621));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(3001, 4099));
	}
  
	
	private void followUsers() throws InterruptedException, FindFailed {
		System.out.println("Ingresar a buscar hashtag");

		if(screen.exists(PATH_IMAGES_SIKULI+"search.png") != null) {
			screen.click(PATH_IMAGES_SIKULI+"search.png");
		}else {
			robot.dimensions(385, 965);
			Thread.sleep(getNumberRandomForSecond(264, 658));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(2264, 2658));
		
		System.out.println("Darle al boton de buscar");
		
		//Darle clic al input donde se escribe 
		if(screen.exists(PATH_IMAGES_SIKULI+"search-text.png") != null) {
			screen.click(PATH_IMAGES_SIKULI+"search-text.png");
		}else {
			robot.dimensions(645, 131);
			Thread.sleep(getNumberRandomForSecond(264, 658));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(2264, 2658));

		System.out.println("Escribiendo en el cuadro de texto");
		//Escribir en el input
		String[] hashtag = {"#followforfollow","#followback","#followme","#follow4follow","#FollowPyramid","#TEAMFOLLOWBACK",
				"#SiguemeYTeSigo","#F4F"};
		robot.inputWrite(hashtag[getNumberRandomForSecond(0, hashtag.length-1)]);
		Thread.sleep(getNumberRandomForSecond(3198, 4245));
		robot.enter();
		robot.enter();
		Thread.sleep(getNumberRandomForSecond(4198, 5245));
		selectPhotoAndFollow();
	}
	
	private void selectPhotoAndFollow() throws InterruptedException, FindFailed {
		for(int i = 1; i <=15; i++) {
			//Click a la primera foto
			robot.pressTab();
			Thread.sleep(getNumberRandomForSecond(125, 214));
			robot.pressTab();
			Thread.sleep(getNumberRandomForSecond(125, 214));
			for(int j = 0; j < i; j++) {
				robot.pressTab();
				Thread.sleep(getNumberRandomForSecond(125, 214));
			}
			robot.enter();
			Thread.sleep(getNumberRandomForSecond(4125, 4214));
			if(screen.exists("C:\\ImagenesSikuli\\follow.png") != null) {
				screen.click("C:\\ImagenesSikuli\\follow.png");
				Thread.sleep(getNumberRandomForSecond(125, 214));
			}
			robot.pressEsc();
			Thread.sleep(getNumberRandomForSecond(725, 814));
			
			
		}
		
	}
	
	private void userBlock(String name) {
		User_Block userB = new User_Block();
		userB.setUsers_id(idUser);
		userB.setComentario(name);
		if (userB.getIdUser() == 0) {
			userB.insert();
		}
	}
	
	private String validateIP() {
		
		try {

            URL whatismyip = new URL("http://checkip.amazonaws.com");

            BufferedReader in = new BufferedReader(new InputStreamReader(

            whatismyip.openStream()));     
            
            return in.readLine();
            
                 

        } catch (MalformedURLException ex) {

            System.err.println(ex);
            return "190.146.186.130";
        } catch (IOException ex) {

            System.err.println(ex);
            return "190.146.186.130";
        }
		
		
	}
	
	private static int getNumberRandomForSecond(int init, int fin) {
		return ThreadLocalRandom.current().nextInt(init, fin + 1); 
	}
	

}
