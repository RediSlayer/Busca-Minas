import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class BuscaMinas extends JFrame implements ActionListener{
	
	//Atributos de la clase
	private int dificultad, casillasLibres, filas, columnas, casillasClickeadas, numObtenerCasillas, minasRestantes, banderasRestantes; //Para la dificultad y para las casillas que son libre, junto a las filas y columnas	//Todas los atributos que voy a usar!
	private int[][] tableroJuego; //Tablero donde pasa todo
	private JButton[][] tableroJuegoBotones; //Botones que van a simular casillas donde estaran las minas
	private boolean[][] banderasColocadas; //Para saber cuantas banderas hay
	private JButton btnJugar, btnReiniciar; //Botones que voy a usar para jugar y reiniciar
	private JComboBox<String> dificultadTablero; //Representación de la dificultad en un comboBox
	private JPopupMenu accionesDisponibles; //Las acciones que puede hacer que serian quitar bandera poner bandera y cavar
	private JMenuItem accionCavar, accionFlag, accionUnflag; //Los items que iran a JPopupMenu
	private int segundosTranscurridos = 0; //Tiempo en sabeis
	private Timer relojGlobal; //Timer para pararlo cuando gane alguien
	
	//Lo que se va a encargar de poner las diferentes pantallas, es decir gestionar que entro en una PARTIDA o en el MENU
	private CardLayout cardLayout; //Componente que nos ayuda a switchear de pantalla
	private JPanel contenedor; //El contenedor de todo
	private JPanel panelMenu; //Pantallas, pantalla del menu
	private JPanel panelJuego; //Pantallas tipo, pantalla del juego
	private JLabel bienvidoAlJuego, temporizador, dificultadSeleccionada, minasRestantesLabel, banderasRestantesLabel; //Etiquetas
	private boolean partidaTerminada = false; //Variable de control
	
	//Variables adicionales para la selección de casillas
	private int filaSeleccionada = -1;
	private int columnaSeleccionada = -1;
	private JPanel panelTablero; //Panel donde van los botones del tablero
	
	//Colores de la interfaz
	private static final Color COLOR_FONDO = new Color(245, 247, 250);
	private static final Color COLOR_PRIMARIO = new Color(52, 101, 164);
	private static final Color COLOR_TEXTO = new Color(40, 44, 52);
	
	//funcón que inicia tablero y botones a sus respecticas tallas
	public void IniciarTablero(int filas, int columnas){
		casillasClickeadas = 0;
		tableroJuego = new int[filas][columnas];
		tableroJuegoBotones = new JButton[filas][columnas];
		banderasColocadas = new boolean[filas][columnas];
		for(int i = 0; i < tableroJuego.length; i++){
			for(int j = 0; j < tableroJuego[i].length; j++){
				tableroJuego[i][j] = 0;
				banderasColocadas[i][j] = false;
			}
		}
	}
	
	//Reloj para cuando empiece partidaTerminada
	public void IniciarReloj(){
		relojGlobal = new Timer(1000, e ->{
			segundosTranscurridos++;
			temporizador.setText("Han pasado: " + segundosTranscurridos + " segundos");
		});
		
		relojGlobal.setRepeats(true);
		relojGlobal.start();
	}
	
	//Para saber cuantas casillas Libres hay en el tablero
	public void ContarCasillasLibres(){
		casillasLibres = 0;
		for(int i = 0; i < tableroJuego.length; i++){
			for(int j = 0; j < tableroJuego[i].length; j++){
				if(tableroJuego[i][j] != -1){
					casillasLibres++;
				}
			}
		}
	}
	
	//Función que pone minas aleatoriamente
	public void PonerMinas(){
		//Quiero obtener numeros randoms asi que usare collection shuffle
		ArrayList<Integer> numPosBuscaMinas = new ArrayList<>();
		for(int k = 0; k < filas * columnas; k++){
			numPosBuscaMinas.add(k);
		}
		Collections.shuffle(numPosBuscaMinas);
		
		//Agregamos aleatoriamente las minas en el tablero
		int f, c;
		for(int k = 0; k < minasRestantes;k++){
			f = numPosBuscaMinas.get(k) / numObtenerCasillas;
			c = numPosBuscaMinas.get(k) % numObtenerCasillas;
			tableroJuego[f][c] = -1;
		}
	}

	//Revela todas las minas si pierde
	public void RevelarMinas(){
		for(int i = 0; i < tableroJuego.length; i++){
			for(int j = 0; j < tableroJuego[i].length; j++){
				if(tableroJuego[i][j] == -1){
					tableroJuegoBotones[i][j].setText("💣");
				}
			}
		}
	}
	
	//Función que quita casillas
	public void QuitarCasilla(int filas, int columnas){
		if (tableroJuego[filas][columnas] == 0 && banderasColocadas[filas][columnas] != true){
			QuitarCero(filas, columnas);
			VerificarGanador();
		}else{
			if(tableroJuego[filas][columnas] == -1 && banderasColocadas[filas][columnas] != true){
				JOptionPane.showMessageDialog(this, "Has perdido!");
				btnReiniciar.setVisible(true);
				RevelarMinas();
				partidaTerminada = true;
				return;
			}
			if(tableroJuego[filas][columnas] > 0 && banderasColocadas[filas][columnas] != true){
				tableroJuegoBotones[filas][columnas].setEnabled(false);
				tableroJuegoBotones[filas][columnas].setText(String.valueOf(tableroJuego[filas][columnas]));
				tableroJuegoBotones[filas][columnas].setForeground(COLOR_PRIMARIO); 
				casillasClickeadas++;
				VerificarGanador();
			}
		}
	}
	
	//Funcion que controla el estado de juego
	public boolean VerificarGanador(){
		if (casillasClickeadas == casillasLibres){
			partidaTerminada = true;
			return true;
		}else{
			return false;
		}
	}
	
	//Funcion que quita cero, como virus que se propaga
	public void QuitarCero(int filas, int columnas){
		int nuevaFila, nuevaColumna;
		nuevaFila = 0;
		nuevaColumna = 0;
		//Condiciones super necesarias antes de poder ejecutar, una es para evitar pasarse o indexOut
		if ((filas < 0) || (filas >=tableroJuego.length)){
			return;
		}
		if ((columnas < 0) || (columnas >= tableroJuego[filas].length)){
			return;
		}
		if (!(tableroJuegoBotones[filas][columnas].isEnabled())){
			return;
		}
		if (tableroJuego[filas][columnas] == -1){
			return;
		}
		if (banderasColocadas[filas][columnas] == true){
			return;
		}
		//Si cumplen todo esto se ejecuta esto y los if son los preeliminares
		if (tableroJuego[filas][columnas] > 0){
			tableroJuegoBotones[filas][columnas].setText(String.valueOf(tableroJuego[filas][columnas]));
			tableroJuegoBotones[filas][columnas].setEnabled(false);
			casillasClickeadas++;
			VerificarGanador();
		}else{
			tableroJuegoBotones[filas][columnas].setText(" ");
			tableroJuegoBotones[filas][columnas].setEnabled(false);
			casillasClickeadas++;
			VerificarGanador();
		}
		if (tableroJuego[filas][columnas] != 0){
			return;
		}
		//Visitamos sus vecinos
		for(int i = 0; i < 8; i++){
			switch(i + 1){
				case 1:
					nuevaFila = filas + -1;
					nuevaColumna = columnas + -1;
					break;
				case 2:
					nuevaFila = filas + -1;
					nuevaColumna = columnas + 0;
					break;
				case 3:
					nuevaFila = filas + -1;
					nuevaColumna = columnas + 1;
					break;
				case 4:
					nuevaFila = filas + 0;
					nuevaColumna = columnas + -1;
					break;
				case 5:
					nuevaFila = filas + 0;
					nuevaColumna = columnas + 1;
					break;
				case 6:
					nuevaFila = filas + 1;
					nuevaColumna = columnas + -1;
					break;
				case 7:
					nuevaFila = filas + 1;
					nuevaColumna = columnas + 0;
					break;
				case 8:
					nuevaFila = filas + 1;
					nuevaColumna = columnas + 1;
					break;
			}
			QuitarCero(nuevaFila, nuevaColumna);
		}
	}
	
	//Función para colocar bandera
	public void ColocarBandera(int filas, int columnas){
		if(banderasColocadas[filas][columnas] == false && banderasRestantes > 0){
			tableroJuegoBotones[filas][columnas].setText("🚩");
			banderasColocadas[filas][columnas] = true;
			banderasRestantes--;
		}
	}
	
	//Función reciproca de ColocarBandera
	public void QuitarBandera(int filas, int columnas){
		if(banderasColocadas[filas][columnas] == true){
			tableroJuegoBotones[filas][columnas].setText(" ");
			banderasColocadas[filas][columnas] = false;
			banderasRestantes++;
		}
	}
	
	//Función que calcula las minas alrededor
	public void ContarCuantasMinas(){
		int nuevaFila = 0, nuevaColumna = 0;
		for (int i = 0; i < tableroJuego.length; i++){
			for (int j = 0; j < tableroJuego[i].length; j++){
				if (tableroJuego[i][j] == -1){
					//Visitamos sus 8 vecinos
					for (int k = 0; k < 8; k++){
						switch (k + 1){
							case 1:
								nuevaFila = i + -1;
								nuevaColumna = j + -1;
								break;
							case 2:
								nuevaFila = i + -1;
								nuevaColumna = j + 0;
								break;
							case 3:
								nuevaFila = i + -1;
								nuevaColumna = j + 1;
								break;
							case 4:
								nuevaFila = i + 0;
								nuevaColumna = j + -1;
								break;
							case 5:
								nuevaFila = i + 0;
								nuevaColumna = j + 1;
								break;
							case 6:
								nuevaFila = i + 1;
								nuevaColumna = j + -1;
								break;
							case 7:
								nuevaFila = i + 1;
								nuevaColumna = j + 0;
								break;
							case 8:
								nuevaFila = i + 1;
								nuevaColumna = j + 1;
								break;
						}
						//Evitamos pasarnos del tablero, continue salta solo este vecino
						if ((nuevaFila < 0) || (nuevaFila >= tableroJuego.length)){
							continue;
						}
						if ((nuevaColumna < 0) || (nuevaColumna >= tableroJuego[nuevaFila].length)){
							continue;
						}
						//Si el vecino es otra mina no se modifica
						if (tableroJuego[nuevaFila][nuevaColumna] == -1){
							continue; //pasamos a i++
						}
						tableroJuego[nuevaFila][nuevaColumna]++; //El numero de minas alrededor lo sumamos, nunca habia pensado en esta forma
					}
				}
			}
		}
	}
	
	//Funcion de reiniciar buscaminas
	public void Reiniciar(){
		if(relojGlobal != null){
			relojGlobal.stop();
		}
		cardLayout.show(contenedor, "MENU");
	}
	
	//Actualiza las etiquetas de banderas
	private void ActualizarEtiquetas(){
		banderasRestantesLabel.setText("Banderas: " + banderasRestantes);
	}
	
	//Crea una opcion del menu desplegable de acciones
	private JMenuItem CrearItemAccion(String texto){
		JMenuItem item = new JMenuItem(texto);
		item.setFont(new Font("SansSerif",Font.BOLD,16));
		item.setHorizontalAlignment(SwingConstants.CENTER);
		item.addActionListener(this);
		return item;
	}
	
	//Constructor BuscaMinas
	public BuscaMinas(){
		//Ventana
		super("Buscaminas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(960, 720);
		setResizable(false);
		setLocationRelativeTo(null);
		
		//Creacion de la interfaz grafica
		cardLayout = new CardLayout();
		contenedor = new JPanel(cardLayout);
		panelMenu = new JPanel(null);
		panelMenu.setBackground(COLOR_FONDO);
		panelJuego = new JPanel(null);
		panelJuego.setBackground(COLOR_FONDO);
		contenedor.add(panelMenu, "MENU");
		contenedor.add(panelJuego, "JUEGO");
		this.add(contenedor);
		
		// ================= PANTALLA DEL MENU =================
		bienvidoAlJuego = new JLabel("BUSCAMINAS");
		bienvidoAlJuego.setFont(new Font("SansSerif", Font.BOLD, 45));
		bienvidoAlJuego.setBounds(280, 130, 400, 60);
		bienvidoAlJuego.setHorizontalAlignment(SwingConstants.CENTER);
		panelMenu.add(bienvidoAlJuego);
		
		JLabel subtitulo = new JLabel("Elige un nivel y empieza a jugar");
		subtitulo.setBounds(300, 195, 360, 30);
		subtitulo.setHorizontalAlignment(SwingConstants.CENTER);
		panelMenu.add(subtitulo);
		
		dificultadTablero = new JComboBox<>(new String[]{"Fácil", "Medio", "Difícil"});
		dificultadTablero.setBounds(330, 280, 300, 35);
		panelMenu.add(dificultadTablero);
		
		btnJugar = new JButton("Jugar");
		btnJugar.setBounds(350, 350, 260, 45);
		btnJugar.addActionListener(this);
		panelMenu.add(btnJugar);
		
		// ================= PANTALLA DEL JUEGO =================
		dificultadSeleccionada = new JLabel("Nivel: -");
		dificultadSeleccionada.setBounds(30, 15, 200, 25);
		panelJuego.add(dificultadSeleccionada);
		
		minasRestantesLabel = new JLabel("Minas: 0");
		minasRestantesLabel.setBounds(30, 40, 200, 25);
		panelJuego.add(minasRestantesLabel);
		
		temporizador = new JLabel("Han pasado: 0 segundos");
		temporizador.setBounds(330, 15, 300, 25);
		temporizador.setHorizontalAlignment(SwingConstants.CENTER);
		panelJuego.add(temporizador);
		
		banderasRestantesLabel = new JLabel("Banderas: 0");
		banderasRestantesLabel.setBounds(760, 15, 170, 25);
		panelJuego.add(banderasRestantesLabel);
		
		//Menu desplegable con las acciones
		accionesDisponibles = new JPopupMenu();
		accionCavar = CrearItemAccion("⛏️");
		accionFlag = CrearItemAccion("🚩");
		accionUnflag = CrearItemAccion("Quitar bandera");
		accionesDisponibles.add(accionCavar);
		accionesDisponibles.add(accionFlag);
		accionesDisponibles.add(accionUnflag);
		
		//Panel del tablero, se rellena al pulsar Jugar porque depende del nivel
		panelTablero = new JPanel();
		panelJuego.add(panelTablero);
		
		//Parte de abajo: reiniciar
		btnReiniciar = new JButton("Reiniciar");
		btnReiniciar.setBounds(380, 610, 200, 40);
		btnReiniciar.addActionListener(this);
		btnReiniciar.setVisible(false);
		panelJuego.add(btnReiniciar);
		
		//Empezamos en el menu
		cardLayout.show(contenedor, "MENU");
	}
		
	//Eventos
	@Override // A partir de ahora hay que empezar a usarlo 
	public void actionPerformed(ActionEvent e){
		//Cuando el usuario elige el nivel, inicializamos dificultad y pistas una sola vez
		if(e.getSource() == btnJugar){
			dificultad = dificultadTablero.getSelectedIndex() + 1;
			String nombreDificultad = (String) dificultadTablero.getSelectedItem();
			dificultadSeleccionada.setText("Nivel: " + nombreDificultad);
			switch(dificultad){
				case 1: //Nivel facil
					filas = 9;
					columnas = 9;
					minasRestantes = 10;
					numObtenerCasillas = 9;
					banderasRestantes = 10;
					break;
				case 2: //Nivel medio
					filas = 16;
					columnas = 16;
					minasRestantes = 40;
					numObtenerCasillas = 16;
					banderasRestantes = 40;
					break;
				case 3: //Nivel dificil
					filas = 16;
					columnas = 30;
					minasRestantes = 99;
					numObtenerCasillas = 30;
					banderasRestantes = 99;
					break;
				default:
					filas = 9;
					columnas = 9;
					minasRestantes = 9;
					numObtenerCasillas = 30;
					banderasRestantes = 10;
					break;
			}
			
			//Preparamos la partida
			partidaTerminada = false;
			filaSeleccionada = -1;
			columnaSeleccionada = -1;
			IniciarTablero(filas, columnas);
			PonerMinas();
			ContarCasillasLibres();
			ContarCuantasMinas();
			btnReiniciar.setVisible(false);
			minasRestantesLabel.setText("Minas: " + minasRestantes);
			ActualizarEtiquetas();
			
			//Reloj
			if(relojGlobal != null){
				relojGlobal.stop();
			}
			segundosTranscurridos = 0;
			temporizador.setText("Han pasado: 0 segundos");
			IniciarReloj();
			
			//Creamos los botones del tablero segun el nivel
			int tamCasilla = 30;
			panelTablero.removeAll();
			panelTablero.setLayout(new GridLayout(filas, columnas));
			panelTablero.setBounds((960 - columnas * tamCasilla) / 2, 75, columnas * tamCasilla, filas * tamCasilla);
			for(int i = 0; i < filas; i++){
				for(int j = 0; j < columnas; j++){
					JButton casilla = new JButton("");
					casilla.setFont(new Font("SansSerif", Font.BOLD, 14));
					casilla.setForeground(COLOR_PRIMARIO);
					casilla.setFocusPainted(false);
					casilla.setMargin(new Insets(0, 0, 0, 0));
					casilla.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 1));
					casilla.addActionListener(this);
					tableroJuegoBotones[i][j] = casilla;
					panelTablero.add(casilla);
				}
			}
			panelTablero.revalidate();
			panelTablero.repaint();
			
			cardLayout.show(contenedor, "JUEGO");
		}
		
		//Reiniciar juego
		if(e.getSource() == btnReiniciar){
			Reiniciar();
		}
		
		//Acciones del menu desplegable
		if(e.getSource() == accionCavar || e.getSource() == accionFlag || e.getSource() == accionUnflag){
			if(filaSeleccionada != -1 && columnaSeleccionada != -1 && !partidaTerminada){
				if(e.getSource() == accionCavar){
					QuitarCasilla(filaSeleccionada, columnaSeleccionada);
				}
				if(e.getSource() == accionFlag){
					ColocarBandera(filaSeleccionada, columnaSeleccionada);
				}
				if(e.getSource() == accionUnflag){
					QuitarBandera(filaSeleccionada, columnaSeleccionada);
				}
				ActualizarEtiquetas();
				
				//Si la partida ha terminado paramos el reloj
				if(partidaTerminada){
					relojGlobal.stop();
					btnReiniciar.setVisible(true);
					if(VerificarGanador()){
						JOptionPane.showMessageDialog(this, "¡Felicidades, has ganado!");
					}
				}
			}
		}
		
		//Comprobar clics en las casillas del tablero
		if(tableroJuegoBotones != null){
			for(int i = 0; i < tableroJuegoBotones.length; i++){
				for(int j = 0; j < tableroJuegoBotones[i].length; j++){
					if(e.getSource() == tableroJuegoBotones[i][j]){
						if(!partidaTerminada){
							filaSeleccionada = i;
							columnaSeleccionada = j;
							accionesDisponibles.show(tableroJuegoBotones[i][j], tableroJuegoBotones[i][j].getWidth() / 2, tableroJuegoBotones[i][j].getHeight() / 2);
						}
					}
				}
			}
		}
	}
	
	public static void main(String args[]){
		BuscaMinas interfaz = new BuscaMinas();
		interfaz.setVisible(true);
	}
}