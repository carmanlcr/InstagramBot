package com.instagram.Controlador;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JOptionPane;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

import com.instagram.Model.Account_Instagram;
import com.instagram.Model.Account_Instagram_User;
import com.instagram.Model.Categories;
import com.instagram.Model.Follower;
import com.instagram.Model.Path_Photo;
import com.instagram.Model.Phrases;
import com.instagram.Model.Post;
import com.instagram.Model.Task_Grid;
import com.instagram.Model.Task_Grid_Tags;
import com.instagram.Model.Task_Model_Detail;
import com.instagram.Model.User;
import com.instagram.Model.User_Block;
import com.instagram.Model.User_Follower;
import com.instagram.Model.Follower_Report;
import com.instagram.Model.Vpn;

import configurations.controller.VpnController;
import configurations.controller.DriverController;
import configurations.controller.RobotController;
import configurations.controller.SftpController;


public class InicioController {
	
	private static final String PAGE = "https://www.instagram.com/accounts/login/";
	private static final String PATH_IMAGES_SIKULI = "C:\\ImagenesSikuli\\";
	protected static final  String PATH_IMAGES_SFTP = "C:\\imagesSftp\\";
	private static final String PATH_IMAGES_BLOCK = "C:\\ImagenesSikuli\\validate-block";
	private DriverController driver;
	private User users;
	private RobotController robot;
	private List<String> listCheckBoxUsers = new ArrayList<>();
	private int idUser;
	private int categoriaId;
	private int tasksGridId;
	private String phrase;
	private String image;
	private boolean isPublication;
	private Post po = new Post();
	private Screen screen;
	private Date date = new Date();
	private SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public InicioController(int categoriaId, List<String> listCheckBoxUsers, Screen screen) {
		this.listCheckBoxUsers = listCheckBoxUsers;
		this.categoriaId = categoriaId;
		this.screen = screen;
	}


	public void init() throws InterruptedException, SQLException, IOException {
		int usuariosAProcesar = 0;
		VpnController vpn;
		Task_Grid taskG = new Task_Grid();
		taskG.setCategories_id(this.categoriaId);
		List<Task_Grid> listTask = taskG.getTaskGridToday();
		if(listTask.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Ya no quedan tareas para hoy");
		}else {
			for(String str : listCheckBoxUsers) {
				usuariosAProcesar++;
				System.out.println(usuariosAProcesar+" usuario(s) de "+listCheckBoxUsers.size()+" usuario(s)");
				vpn = null;
				users = new User();
				users.setUsername(str);
				users.setEmail(str);
				users = users.getUser();
				System.out.println("*********************"+users.getUsername()+"***********************");
				if(!users.isBlock()) {
					idUser = users.getUsers_id();
					po.setUsers_id(idUser);
					taskG = new Task_Grid();
					taskG = taskG.getTaskForUser(idUser);
					
					if(taskG != null) {
						String dateCu = simpleFormat.format(date);
						while(dateCu.compareTo(taskG.getDate_publication()) < 0) {
							Thread.sleep(5000);
							date = new Date();
							dateCu = simpleFormat.format(date);
						}
						
						phrase = taskG.getPhrase();
						image = taskG.getImage();
						isPublication = taskG.isPublication();
						tasksGridId = taskG.getTasks_grid_id();
						
						int idlistTask = po.getLastsTasktPublic();
						
						if(idlistTask == 0) {
							System.out.println("El usuario no tiene mas tareas por publicar");
						}else {
							robot = new RobotController();
							String ip = validateIP();
							String ipActual = "01.02.03.04";
							if(0 != 0) {
								Vpn v = new Vpn();
								v.setVpn_id(users.getVpn_id());
								v = v.getVpn();
								vpn = new VpnController(v.getName());
								vpn.connectVpn();
								ipActual = validateIP();
							}
							
							//Valida si la vpn conecto
							if(ip.equals(ipActual)) {	
								System.err.println("El usuario "+users.getUsername()+ " no se puedo conectar a la vpn");
								usuariosAProcesar++;
							}else {
								//Lanzamiento de la pagina   
								driver = new DriverController();
							    driver.optionsChrome();
								Thread.sleep(getNumberRandomForSecond(2540, 4150));
								driver.goPage(PAGE);
								Thread.sleep(2300);
								IniciaSesion sesion = new IniciaSesion("carmanandres","Instagraml4ms",screen);
								int resultSession =sesion.init();
								
								switch(resultSession) {
									case 0:
										System.out.println("El usuario esta bloqueado");
										userBlock("La cuenta ha sido desactivada");
										break;
									case 1:
										//Esperar que cargue la pagina para que cargue el dom completamente
										Thread.sleep(getNumberRandomForSecond(3250, 4650)); 
										robot.changeDeveloper(screen, PATH_IMAGES_SIKULI+"dev-tools-Chorme.png");
										Thread.sleep(getNumberRandomForSecond(1250, 1650)); 
										if(!blockUser()) {
											startProgram(idlistTask);
										}
										break;
									case 2:
										System.out.println("El nombre de usuario no pertenece a ninguna cuenta");
										break;
									case 3:
										System.out.println("La contraseña es incorrecta");
										break;
									default:
									break;
								}

								robot.close();
								//Desconectar la vpn para el siguiente usuario
								if(vpn != null) {
									vpn.disconnectVpn();
								}
								Thread.sleep(getNumberRandomForSecond(1999, 2125));
								
							}
						}
					}
				}
			}
		}//Fin del init
		System.out.println("Finalizo con exito el programa");
		System.exit(1);
	}
	
	/**
	 * 
	 * 
	 */
	private boolean blockUser() {
		
		if(screen.exists(PATH_IMAGES_BLOCK+"1.png") != null) {
			userBlock("Confirma que eres tu para iniciar sesion");
			return true;
		}else if(screen.exists(PATH_IMAGES_BLOCK+"2.png") != null) {
			userBlock("Intento de inicio de sesion sospechoso");
			return true;
		}else if(screen.exists(PATH_IMAGES_BLOCK+"3.png") != null) {
			userBlock("Intento de inicio de sesion sospechoso");
			return true;
		}else if(screen.exists(PATH_IMAGES_BLOCK+"4.png") != null) {
			userBlock("Intento de inicio de sesion sospechoso");
			return true;
		}else if(screen.exists(PATH_IMAGES_BLOCK+"8.png") != null) {
			userBlock("Intento de inicio de sesion sospechoso");
			return true;
		}else if(screen.exists(PATH_IMAGES_BLOCK+"9.png") != null) {
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
	 * @throws IOException 
	 */
	private void startProgram(int idlistTask) throws InterruptedException, SQLException {
		
			
		System.out.println("Usuario logueado");
		
		if(screen.exists(PATH_IMAGES_SIKULI+"activate_notifications-Instagram.png") != null) {
			clickException(PATH_IMAGES_SIKULI+"activate_notifications-Instagram.png");
			Thread.sleep(1250);
		}
		
		scrollDown(34);
		scrollUp(34);
		
		System.out.println("Buscando tareas que haya publicado el usuario");

		
		Task_Model_Detail tmd = new Task_Model_Detail();
		tmd.setTasks_model_id(idlistTask);
		List<Integer> listTask = tmd.getTaskModelDetailDiferent();
		System.out.println("Buscando tareas para publicacion del usuario");
		random(listTask,idlistTask);
		
		
		System.out.println("***************El usuario hizo todas las publicaciones correctamente*****************");
		
		sesionClose();
		
		
		System.out.println("Se cerro la sesion del usuario "+users.getUsername());
	}
	
	private void random(List<Integer> listTask, int taskModelId) throws InterruptedException, SQLException {
		for(Integer li : listTask) {
			li = 7;
			switch(li) {
				case 1:
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
					if(isPublication && image != null) { 
						SftpController sftp= new SftpController();
						System.out.println("Descangando Imagen "+image);
						sftp.downloadFileSftp(SftpController.PATH_IMAGE_UPLOAD,image,SftpController.PATH_IMAGE_DOWNLOAD_FTP);
						publicFinal(taskModelId);
					}
					break;
				case 5:
					//Entrar en perfil y dar like
					perfilLike();
					break;
				case 6:
					//Publicacion normal
					uploadImage();
					break;
				case 7:
					followUsers();
					break;
				case 8:
					getFollowers();
					break;
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

	
	private void uploadImageFinal() throws InterruptedException {
		File archivo1 = new File(PATH_IMAGES_SFTP+image);
		if(archivo1.exists()) {
			//Si el archivo existe hacer el procedimiento de subir la imagen
			System.out.println("Abrir el selector de imagenes");
			
			if(screen.exists(PATH_IMAGES_SIKULI+"add-images.png") != null) {
				clickException(PATH_IMAGES_SIKULI+"add-images.png");
			}else {
				robot.dimensions(630, 957);
				Thread.sleep(getNumberRandomForSecond(256, 985));
				robot.clickPressed();
			}
			
			robot.copy(PATH_IMAGES_SFTP+image);
			Thread.sleep(getNumberRandomForSecond(5548, 6572));
			robot.paste();
			
			Thread.sleep(getNumberRandomForSecond(3548, 4572));
			
			System.out.println("Siguiente");
			robot.dimensions(1215, 132);
			Thread.sleep(getNumberRandomForSecond(478, 896));
			robot.clickPressed();
			Thread.sleep(getNumberRandomForSecond(2489, 3549));

		    
			//234, 192
			robot.dimensions(234, 192);
			Thread.sleep(getNumberRandomForSecond(478, 896));
			robot.clickPressed();
			Thread.sleep(getNumberRandomForSecond(2489, 3549));
			
			System.out.println("Escribir frase");
			
			robot.inputWriteUsers(phrase);
			Thread.sleep(getNumberRandomForSecond(1489, 1549));
			
			robot.dimensions(1215, 134);
			Thread.sleep(getNumberRandomForSecond(478, 896));
			robot.clickPressed();
			System.out.println("El usuario hizo la publicaci�n correctamente.");
			archivo1.delete();
			Thread.sleep(getNumberRandomForSecond(8001, 10099));	
		}else {
			System.out.println("No hay imagen, no se puede subir");
		}
		
		
	}
	
	private void uploadImage() throws InterruptedException, SQLException {
		Categories ca = new Categories();
		List<String> la = ca.getSubCategorieConcat();
		if(la.isEmpty()) {
			int value = (int) (Math.random() * 100000) + 1;
			int dimensionx = (int) (Math.random() * 3) + 0;
			int dimensiony = (int) (Math.random() * 5) + 0;
			System.out.println("Abrir el selector de imagenes");
			if(screen.exists(PATH_IMAGES_SIKULI+"add-images.png") != null) {
				clickException(PATH_IMAGES_SIKULI+"add-images.png");
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
			int categorieId = Integer.parseInt(la.get(0));
			int subCateId = Integer.parseInt(la.get(2));
			System.out.println("Buscar direccion de fotos");
			pathP.setCategories_id(categorieId);				
			pathP.setSub_categories_id(subCateId);
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
			frase.setCategories_id(categorieId);
			frase.setSub_categories_id(subCateId);
			String phraseRandom = frase.getPhraseRandomSubCategorie();
			System.out.println("Encontrar frase");
			robot.dimensions(234, 192);
			Thread.sleep(getNumberRandomForSecond(478, 896));
			robot.clickPressed();
			Thread.sleep(getNumberRandomForSecond(2489, 3549));
			robot.inputWrite(phraseRandom);
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
	private void returnHome() throws InterruptedException {
		System.out.println("Retornar al inicio");
		
		if(screen.exists(PATH_IMAGES_SIKULI+"home.png") != null) {
			clickException(PATH_IMAGES_SIKULI+"home.png");
		}else if(screen.exists(PATH_IMAGES_SIKULI+"home1.png") != null) {
			clickException(PATH_IMAGES_SIKULI+"home1.png");
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
	
	private void reviewNotifications() throws InterruptedException {
		System.out.println("REVISAR LAS NOTIFICACIONES");
		System.out.println("Ingreso en las notificaciones");
		if(screen.exists(PATH_IMAGES_SIKULI+"notifications.png") != null) {
			clickException(PATH_IMAGES_SIKULI+"notifications.png");
		}else {
			robot.dimensions(889, 966);
			Thread.sleep(getNumberRandomForSecond(847, 898));
			robot.clickPressed();
		}
		
		robot.dimensions(320, 320);
		Thread.sleep(getNumberRandomForSecond(3001, 3099));
		//Hacer scroll en las notificaciones
		System.out.println("Hacer Scrool en notificaciones");
		scrollDown(30);
		scrollUp(30);
		Thread.sleep(getNumberRandomForSecond(1254, 1267));
		//Volver al inicio
		returnHome();
	}
	
	private void reviewMessage() throws InterruptedException {
		System.out.println("REVISAR LOS MENSAJES");
		
		if(screen.exists(PATH_IMAGES_SIKULI+"message.png") != null) {
			clickException(PATH_IMAGES_SIKULI+"message.png");
		}else {
			robot.dimensions(1242, 125);
			Thread.sleep(getNumberRandomForSecond(689, 897));
			robot.clickPressed();
		}
		
		Thread.sleep(2500);
		
		if(screen.exists(PATH_IMAGES_SIKULI+"activate_notifications-Instagram.png") != null) {
			clickException(PATH_IMAGES_SIKULI+"activate_notifications-Instagram.png");
			Thread.sleep(1250);
		}
		
		
		
		robot.dimensions(320, 320);
		Thread.sleep(getNumberRandomForSecond(1689, 1759));
		
		System.out.println("Hacer scroll en los mensajes");
		scrollDown(30);
		scrollUp(30);
		Thread.sleep(getNumberRandomForSecond(1254, 1267));
		robot.pressEsc();
		//Volver al inicio
		System.out.println("Volver al inicio");
		
		if(screen.exists(PATH_IMAGES_SIKULI+"back-message.png") != null) {
			clickException(PATH_IMAGES_SIKULI+"back-message.png");
		}else {
			robot.dimensions(36, 131);
			Thread.sleep(getNumberRandomForSecond(689, 759));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(1952, 2099));
	}
		
	private void publicFinal(int taskModelId) throws InterruptedException {
		System.out.println("PUBLICACION FINAL");
		uploadImageFinal();
		
		Thread.sleep(getNumberRandomForSecond(1001, 2558));

		
		registerPost(taskModelId);
		returnHome();
		System.out.println("El usuario publico correctamente");
	}
	
	private void registerPost(int taskModelId) throws  InterruptedException {
		po.setUsers_id(users.getUsers_id());
		po.setCategories_id(categoriaId);
		po.setTasks_model_id(taskModelId);
		po.setTasks_grid_id(tasksGridId);
		po.setPhrases_id(0);

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
					Thread.currentThread().interrupt();
				}
			}
		});
		
		run.start();
		String linkInstagram = JOptionPane.showInputDialog("PRESIONAR ctrl+v Y PULSE ACEPTAR");
		
		po.setLink_instagram(linkInstagram);
		if(po.getLink_instagram() == null 
				|| !validateUrl(linkInstagram)) {
			System.out.println("No se agragará el post ya que no hay link para agregar");
		}else {
			po.insert();
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
	private void perfilLike() throws InterruptedException {
		System.out.println("ENTRAR EN PERFIL Y DAR LIKE!");
		Account_Instagram_User acount = new Account_Instagram_User();
		acount.setUsers_id(users.getUsers_id());
		Account_Instagram acInsta = acount.getAccountsFollowUser();
		//Buscar usuario
		if(acInsta != null) {
			searchUser(acInsta);
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
				clickException(PATH_IMAGES_SIKULI+"like.png");
			}else {
				robot.dimensions(808, 722);
				Thread.sleep(getNumberRandomForSecond(256, 895));
				robot.clickPressed();
			}
			Thread.sleep(getNumberRandomForSecond(1654, 2095));
			
			robot.pressEsc();
			
			scrollUp(7);
			
		}
		//Volver al inicio
		System.out.println("Volver al inicio");
		returnHome();
	}
	
	private void searchUser(Account_Instagram account) throws InterruptedException {
		System.out.println("Ingresar a buscar usuario");
		//Darle click al boton de search
		if(screen.exists(PATH_IMAGES_SIKULI+"search.png") != null) {
			clickException(PATH_IMAGES_SIKULI+"search.png");
		}else {
			robot.dimensions(385, 965);
			Thread.sleep(getNumberRandomForSecond(264, 658));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(2264, 2658));
		
		System.out.println("Darle al boton de buscar usuario");
		
		//Darle clic al input donde se escribe 
		if(screen.exists(PATH_IMAGES_SIKULI+"search-text.png") != null) {
			clickException(PATH_IMAGES_SIKULI+"search-text.png");
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

		
	}
	
	
	private void follow(Account_Instagram account) throws InterruptedException {
		//Ponerse en el boton de seguir
		//908,234
		System.out.println("Pulsar el boton de seguir");
		if(screen.exists(PATH_IMAGES_SIKULI+"follow-user.png") != null) {
			clickException(PATH_IMAGES_SIKULI+"follow-user.png");
		}else {
			robot.dimensions(908, 234);
			Thread.sleep(getNumberRandomForSecond(289, 645));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(2456, 3215));
		
		Account_Instagram_User aco = new Account_Instagram_User();
		aco.setUsers_id(users.getUsers_id());
		aco.setAccounts_instagram_id(account.getAccounts_instagram_id());
		try {
			aco.insert();
			System.out.println("Se siguio al usuario "+account.getAccount());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void sesionClose() throws InterruptedException {
		Thread.sleep(getNumberRandomForSecond(2002, 2065));
		//Ir a mi perfil
		System.out.println("Ir al perfil del usuario");		
		//1142 , 967
		if(screen.exists(PATH_IMAGES_SIKULI+"profile.png") != null) {
			clickException(PATH_IMAGES_SIKULI+"profile.png");
		}else {
			robot.dimensions(1142, 967);
			Thread.sleep(getNumberRandomForSecond(254, 621));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(3001, 3099));
		
		//Darle click a la barra de opciones
		System.out.println("Darle a la barra de opciones");
		if(screen.exists(PATH_IMAGES_SIKULI+"option.png") != null) {
			clickException(PATH_IMAGES_SIKULI+"option.png");
		}else {
			robot.dimensions(35, 131);
			Thread.sleep(getNumberRandomForSecond(254, 621));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(2001, 3099));
		System.out.println("Darle a Cerrar Sesion");
		if(screen.exists(PATH_IMAGES_SIKULI+"close-session.png") != null) {
			clickException(PATH_IMAGES_SIKULI+"close-session.png");
		}else {
			robot.dimensions(67, 904);
			Thread.sleep(getNumberRandomForSecond(254, 621));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(2001, 3099));
		
		//642,580
		if(screen.exists(PATH_IMAGES_SIKULI+"exit.png") != null) {
			clickException(PATH_IMAGES_SIKULI+"exit.png");
		}else {
			robot.dimensions(642, 580);
			Thread.sleep(getNumberRandomForSecond(254, 621));
			robot.clickPressed();
		}
		Thread.sleep(getNumberRandomForSecond(3001, 4099));
	}
  
	
	private void followUsers() throws InterruptedException {
		System.out.println("Ingresar a buscar palabras");
		
		Task_Grid_Tags tgt = new Task_Grid_Tags();
		tgt.setTasks_grid_id(tasksGridId);
		List<Task_Grid_Tags> list = tgt.getTagsForTask();
		
		for(Task_Grid_Tags tags : list) {
			if(screen.exists(PATH_IMAGES_SIKULI+"search.png") != null) {
				clickException(PATH_IMAGES_SIKULI+"search.png");
			}else {
				robot.dimensions(385, 965);
				Thread.sleep(getNumberRandomForSecond(264, 658));
				robot.clickPressed();
			}
			
			robot.dimensions(320, 320);
			Thread.sleep(getNumberRandomForSecond(2264, 2658));
			
			System.out.println("Darle al boton de buscar");
			
			//Darle clic al input donde se escribe 
			if(screen.exists(PATH_IMAGES_SIKULI+"search-text.png") != null) {
				clickException(PATH_IMAGES_SIKULI+"search-text.png");
			}else {
				robot.dimensions(645, 131);
				Thread.sleep(getNumberRandomForSecond(264, 658));
				robot.clickPressed();
			}
			Thread.sleep(getNumberRandomForSecond(2264, 2658));

			System.out.println("Escribiendo en el cuadro de texto");
			//Escribir en el input
		
			robot.inputWrite(tags.getTags());
			Thread.sleep(getNumberRandomForSecond(3198, 4245));
			robot.enter();
			robot.enter();
			Thread.sleep(getNumberRandomForSecond(4198, 5245));
			if(tags.getTags().substring(0,1).equals("#")) {
				selectPhotoAndFollow();
			}else if(tags.getTags().substring(0,1).equals("@")) {
				selectFollowerOfUser();
			}
			
		}

	}
	
	private void selectPhotoAndFollow() throws InterruptedException {
		
		for(int i = 1; i <=15; i++) {
			
			robot.pressShiftTabulador();
			Thread.sleep(200);
			
		}
		
		robot.enter();
		Thread.sleep(getNumberRandomForSecond(2500, 3250));
		
		for(int j = 1; j<= 15;j++) {
			if(screen.exists("C:\\ImagenesSikuli\\follow.png") != null) {
				clickException("C:\\ImagenesSikuli\\follow.png");
				Thread.sleep(getNumberRandomForSecond(125, 214));
			}
		    
			robot.pressLeft();
			
			Thread.sleep(getNumberRandomForSecond(2500, 3250));
		}
		
		robot.pressEsc();
		Thread.sleep(getNumberRandomForSecond(2500, 3250));
	}
	
	private void selectFollowerOfUser() throws InterruptedException {
		System.out.println("Ingresar en los followers del usuario");
		
		if(driver.searchElement(1, "/html/body/div[1]/section/main/div/header/section/ul/li[2]/a") != 0) {
			driver.clickButton(1, "/html/body/div[1]/section/main/div/header/section/ul/li[2]/a", "Followings");
			
			Thread.sleep(3450);
			int numRand = getNumberRandomForSecond(55, 700);
			scrollDown(numRand);
			scrollUp(numRand);
			
			int quantituUsers = driver.getQuantityElements(1, "/html/body/div[4]/div/div[2]/ul/div/li");
			
			List<Integer> list = getnumbersRandom(quantituUsers);
			
			List<Follower> listFollowers = new ArrayList<>();
			
			for(Integer inte : list) {
				
				String user = driver.getText(1, "/html/body/div[4]/div/div[2]/ul/div/li["+inte+"]/div/div[1]/div[2]/div[1]/a");
				String name = driver.getText(1, "/html/body/div[4]/div/div[2]/ul/div/li["+inte+"]/div/div[1]/div[2]/div[2]");
				String link = driver.getHref(1, "/html/body/div[4]/div/div[2]/ul/div/li["+inte+"]/div/div[1]/div[2]/div[1]/a");
				
				
				Follower follower = new Follower();
				follower.setUser(user);
				follower.setName(name);
				follower.setLink(link);
				
				listFollowers.add(follower);
			}
			
			for(Follower follow : listFollowers){
				System.out.println("Ingresar en el perfil "+follow.getUser());
				driver.goPage(follow.getLink());
				
				if(driver.searchElement(1, "/html/body/div[1]/section/main/div/header/section/div[2]/div/span/span[1]/button") != 0) {
					
					System.out.println("Seguir al usuario");
					driver.clickButton(1, "/html/body/div[1]/section/main/div/header/section/div[2]/div/span/span[1]/button", "Seguir xPath");
					
					validateFollower(follow);
				}else if(driver.searchElement(1, "/html/body/div[1]/section/main/div/header/section/div[1]/div[1]/span/span[1]/button") != 0) {
					System.out.println("Seguir al usuario");
					driver.clickButton(1, "/html/body/div[1]/section/main/div/header/section/div[1]/div[1]/span/span[1]/button", "Seguir xPath");
					
					validateFollower(follow);
				}
			}
		}
		
		
		robot.changeDeveloper(screen, PATH_IMAGES_SIKULI+"dev-tools-Chorme.png");
		
		
	}
	
	private List<Integer> getnumbersRandom(int quantity){
		List<Integer> list = new ArrayList<>();
		
		while(list.size() < 12) {
			int random = getNumberRandomForSecond(1, quantity);
			if(!list.contains(random)) list.add(random);
		}
		
		return list;
	}
	
	private void validateFollower(Follower follow) {
		Follower f = new Follower();
		f.setUser(follow.getUser());
		f = f.find();
		
		if(f.getUser() == null) {
			System.out.println("El usuario no esta en la base de datos.");
			try {
				follow.insert();
				System.out.println("Usuario "+follow.getUser()+" agregado a la base de datos");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			f = new Follower();
			f.setUser(follow.getUser());
			f = f.find();
			
		}
		
		User_Follower uf = new User_Follower();
		uf.setUsers_id(idUser);
		uf.setFollowers_id(f.getFollowers_id());
		
		uf = uf.validateMatch();
		
		if(uf == null) {
			System.out.println("Agregar el match");
			matchFollower(f);
		}
	}
	
	private void matchFollower(Follower follower) {
		User_Follower uf = new User_Follower();
		uf.setUsers_id(idUser);
		uf.setFollowers_id(follower.getFollowers_id());
		try {
			uf.insert();
			System.out.println("Match con usuario realizado");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private void getFollowers() throws InterruptedException {
		//Ir al perfil del usuario
		robot.dimensions(1142, 967);
		Thread.sleep(getNumberRandomForSecond(254, 621));
		robot.clickPressed();
		Thread.sleep(getNumberRandomForSecond(2001, 3099));
		
		
		int followers = 0, following = 0;
		
		
		String folowersString = driver.getTitle(1, "/html/body/div[1]/section/main/div/header/section/ul/li[2]/a/span");
		String followingString = driver.getText(1, "/html/body/div[1]/section/main/div/header/section/ul/li[3]/a/span");
		
		followers = Integer.parseInt(folowersString);
		following = Integer.parseInt(followingString);
		Follower_Report userF = new Follower_Report();
		userF.setUsers_id(idUser);
		userF.setFollowers(followers);
		userF.setFollowing(following);
		
		userF = userF.find(idUser);
		
		
		if(userF == null) {
			userF = new Follower_Report();
			userF.setUsers_id(idUser);
			userF.setFollowers(followers);
			userF.setFollowing(following);
			try {
				userF.insert();
				System.out.println("Se insertaron los datos correctamente");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			if(following != userF.getFollowing() || followers != userF.getFollowers()) {
				userF.setFollowers(followers);
				userF.setFollowing(following);
				try {
					userF.insert();
					System.out.println("Se insertaron nuevos datos correctamente");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void userBlock(String name) {
		User_Block userB = new User_Block();
		userB.setUsers_id(idUser);
		userB.setComentario(name);
		if (userB.getIdUser() == 0) {
			userB.insert();
		}
		
		robot.close();
	}
	
	private String validateIP() throws MalformedURLException {
		
		try {

            URL whatismyip = new URL("http://checkip.amazonaws.com");

            BufferedReader in = new BufferedReader(new InputStreamReader(

            whatismyip.openStream()));     
            
            return in.readLine();
            
                 

        } catch (IOException ex) {

            System.err.println(ex);
            return "45.162.83.106";
        }
		
		
	}
	
	private void clickException(String pathPhoto) throws InterruptedException {
		try {
			screen.click(pathPhoto);
			Thread.sleep(850);
			robot.dimensions(1, 1);
		}catch(IllegalThreadStateException | FindFailed e) {
			System.out.println("Error con el clik");
		}
		
	}
	
	private static int getNumberRandomForSecond(int init, int fin) {
		return ThreadLocalRandom.current().nextInt(init, fin + 1); 
	}
	

}
