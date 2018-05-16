import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Ventana {
	
	static  enum  Actions {
		Compilar,
		Volver,
		GenAssembler,
		GuardarArbol,
		GuardarTS,
		Salir
	}
	
	static Compilador compilador;
	static JFrame Ventana;
	public JTextField campo;
	public boolean generado=false;
	
	@SuppressWarnings("static-access")
	public Ventana(Compilador c) {
		 
		 this.compilador=c;
		 Ventana =new JFrame("Inicio");
		 campo = new JTextField(10);
		 
	 };
	 
	 public void Inicio(){
		 
		 Ventana.dispose();
		 
		 Ventana = new JFrame("Inicio");
		 
		 Ventana.setLayout( new BorderLayout());
			
		 Ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		 //PANEL INPUT
		 JPanel INPUT = new JPanel(new BorderLayout());
		 INPUT.setLayout(new BoxLayout(INPUT, BoxLayout.PAGE_AXIS));
		 
		 //INPUT
		 JLabel label = new JLabel(" Nombre del archivo ");
		 INPUT.add(label,BorderLayout.NORTH);
		 INPUT.add(campo, BorderLayout.CENTER);
		 
		 //PANEL BOTONES
		 JPanel Botones = new JPanel(new BorderLayout());
		 Botones.setLayout(new BoxLayout(Botones, BoxLayout.PAGE_AXIS));
		
		 //Compilar archivo
		 JButton compilar = new JButton("  Generar Arbol Sintactico   ");
		 compilar.setActionCommand("Compilar");
		 compilar.addActionListener( (ActionListener) compilador );
		 compilar.setToolTipText("Genera el Arbol Sintactico");
		 INPUT.add( compilar,BorderLayout.SOUTH);
		 
		 //PANEL TEXTO
		 
		 JPanel Texto = new JPanel(new BorderLayout());
		 Texto.setLayout(new BoxLayout(Texto, BoxLayout.PAGE_AXIS));
		 Texto.add(new JLabel("Escribir el nombre del archivo ubicado en la carpeta CasosPrueba sin extension"),BorderLayout.SOUTH);
		 Texto.add(new JLabel("Los archivos deben terminar con un salto de linea"),BorderLayout.SOUTH);
		 
		 Ventana.add(INPUT,BorderLayout.NORTH);
		 Ventana.add(Botones,BorderLayout.CENTER);
		 Ventana.add(Texto,BorderLayout.SOUTH);

		 
		 //Display the window
		 Ventana.pack();
		 Ventana.setLocationRelativeTo(null); 
		 Ventana.setVisible(true);	 
	 };
	 
	 public void Errores(Vector<String> errores){
		 
		 Ventana.dispose();
		 
		 Ventana = new JFrame("Errores");
		 
		 Ventana.setLayout( new FlowLayout() );
			
		 Ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		 JPanel TEXT = new JPanel(new BorderLayout());
		 TEXT.setLayout(new BoxLayout(TEXT, BoxLayout.PAGE_AXIS));
		 errores.forEach((s) ->{
				JLabel label = new JLabel(s);
				TEXT.add(label);
				System.out.println(s);
		 });
		 
		 JButton volver = new JButton("  Volver a Inicio   ");
		 volver.setActionCommand("Volver");
		 volver.setToolTipText("Vuelve a la pantalla de compilacion");
		 volver.addActionListener( (ActionListener) compilador );
		 
		 Ventana.add(TEXT,BorderLayout.CENTER);
		 Ventana.add(volver,BorderLayout.SOUTH);
		 
		 //Display the window
		 Ventana.pack();
		 Ventana.setLocationRelativeTo(null); 
		 Ventana.setVisible(true);	 
		 
	 }
	 
	 public static void Error(String s){
		 
		 Ventana.dispose();
		 
		 Ventana = new JFrame("Errores");
		 
		 Ventana.setLayout( new FlowLayout() );
			
		 Ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		 JPanel TEXT = new JPanel(new BorderLayout());
		 TEXT.setLayout(new BoxLayout(TEXT, BoxLayout.PAGE_AXIS));
		 JLabel label = new JLabel(s);
		 TEXT.add(label);
		 
		 JButton volver = new JButton("  Salir  ");
		 volver.setActionCommand("Salir");
		 volver.addActionListener( (ActionListener) compilador );
		 
		 Ventana.add(TEXT,BorderLayout.CENTER);
		 Ventana.add(volver,BorderLayout.SOUTH);
		 
		 //Display the window
		 Ventana.pack();
		 Ventana.setLocationRelativeTo(null); 
		 Ventana.setVisible(true);	 
	 }
	 
	 public void Opciones(){
		 
		 Ventana.dispose();
		 
		 Ventana = new JFrame("Opciones");
		 
		 Ventana.setLayout( new FlowLayout() );
			
		 Ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		 JPanel Botones = new JPanel(new BorderLayout());
		 Botones.setLayout(new BoxLayout(Botones, BoxLayout.PAGE_AXIS));
		
		 //Generar Assembler
		 JButton gAssembler = new JButton("         Generar Assembler          ");
		 gAssembler.setActionCommand(Actions.GenAssembler.name());
		 gAssembler.addActionListener( (ActionListener) compilador );
		 gAssembler.setToolTipText("Genera el codigo assembler en la carpeta Assembler");
		 Botones.add( gAssembler,BorderLayout.WEST);
		 
		 //Guardar Arbol
		 JButton gArbol;
		 if (generado){
		 	gArbol= new JButton("    Guardar Arbol de Registros ");
		 	gArbol.setToolTipText("Guarda el Arbol luego de haber sido utilizado para generar el codigo en la carpeta ArbolesRegistros");
		 }
		 else{
			 gArbol= new JButton("               Guardar Arbol              "); 
			 gArbol.setToolTipText("Guarda el arbol sintactico en la carpeta Arboles (Utilizar antes de generar el codigo Assembler)");
		 }
		 gArbol.setActionCommand(Actions.GuardarArbol.name());
		 gArbol.addActionListener( (ActionListener) compilador );
		 Botones.add( gArbol,BorderLayout.CENTER);
		 
		 //Guardar Tabla de Simbolos
		 JButton gTS = new JButton("  Guardar Tabla de Simbolos   ");
		 gTS.setToolTipText("Guarda la tabla de simbolos en la carpeta TablaSimbolos");
		 gTS.setActionCommand(Actions.GuardarTS.name());
		 gTS.addActionListener( (ActionListener) compilador );
		 Botones.add( gTS,BorderLayout.EAST);
		 
		 //Volver
		 JButton volver = new JButton("                      Volver                      ");
		 volver.setToolTipText("Vuelve a la pantalla de compilacion");
		 volver.setActionCommand(Actions.Volver.name());
		 volver.addActionListener( (ActionListener) compilador );
		 Botones.add( volver,BorderLayout.SOUTH);
		 
		 Ventana.add(Botones,BorderLayout.WEST);
		 
		 //Display the window
		 Ventana.pack();
		 Ventana.setLocationRelativeTo(null); 
		 Ventana.setVisible(true);
	 }

}
