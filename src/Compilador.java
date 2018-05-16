import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class Compilador implements ActionListener {
	
	public Ventana ventana;
	public Parser parser;
	
	public Compilador() {
		
	}
	
	public void run(){
		ventana=new Ventana(this);
		ventana.Inicio();
	}

	public void actionPerformed(ActionEvent e)  {
		
		if (e.getActionCommand() == Ventana.Actions.Compilar.name()){
			parser = new Parser("./CasosPrueba/"+ventana.campo.getText()+".txt");
			parser.run();
			if	(parser.errores.size()!=0){
				ventana.Errores(parser.errores);
			}else{
				ventana.Opciones();
			}
			/*System.out.println("LEXEMAS RECONOCIDOS POR EL Analizador Lexico");
			par.al.reconocidos.forEach((s)->{System.out.println(s);});
			System.out.println("------------------------------------");
			System.out.println("MENSAJES DE ANALIZADOR SINTACTICO");
			par.ImprimirMensajes();*/
		}	
		
		if (e.getActionCommand() == Ventana.Actions.Volver.name()){
			ventana.generado=false;
			ventana.Inicio();
		}
		
		if (e.getActionCommand() == Ventana.Actions.Salir.name()){
			System.exit(0);
		}
		
		if (e.getActionCommand() == Ventana.Actions.GuardarTS.name()){
			String arch="TablaSimbolos/"+ventana.campo.getText()+".txt";
			try {
				this.impTS(arch);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		if (e.getActionCommand() == Ventana.Actions.GenAssembler.name()){
			String arch="Assemebler/"+ventana.campo.getText();
			try {
				@SuppressWarnings("unused")
				GeneradorCod gc= new  GeneradorCod(arch,parser.nodoListaSentencias);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			ventana.generado=true;
			ventana.Opciones();
		}
		
		if (e.getActionCommand() == Ventana.Actions.GuardarArbol.name()){
			try {
				if (!ventana.generado){
					this.guardarArbol("Arboles/"+ventana.campo.getText()+".txt");}
				else{
					this.guardarArbol("ArbolesRegistros/"+ventana.campo.getText()+".txt");}
			} catch (IOException e1) {
				System.out.println("Error al guardar el Arbol");
				e1.printStackTrace();
			}
		}
	}
	
	public void impTS(String path) throws IOException{
		
		FileWriter contTS;
		contTS = new FileWriter(new File(path));
		BufferedWriter out = new BufferedWriter(contTS);
		AnalizadorLexico.TS.forEach((String s , TablaSimbolos ts)->{
			if (ts.numLinea!=-1){
			try {
						out.write("Lexema:	"+ s +"	"+ ts.print());
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				out.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}}
			});
		out.flush();
		out.close();
	}
	
	public void guardarArbol(String path) throws IOException{
		
		FileOutputStream salidaArbol;
		salidaArbol = new FileOutputStream(path);
		OutputStreamWriter out = new OutputStreamWriter(salidaArbol);
		parser.nodoListaSentencias.printTree(out);
		out.flush();
		out.close();
	}
	
	
	
	public static void main(String[] args) throws IOException{
		Compilador p = new Compilador();
		p.run();
	}
}
