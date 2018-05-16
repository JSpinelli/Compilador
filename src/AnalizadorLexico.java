import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class AnalizadorLexico {
	
	public StringBuilder lexema;
	public char c=0;
	public static  HashMap<String, TablaSimbolos> TS;
	private int estadoAct=0;
	public int numLinea=1;
	public boolean consumir= true;
	public boolean tokenFound= true;
	public Integer token;
	public Integer nuevosTokens;
	public BufferedReader fuente;
	public static boolean endOfFile;
	public int readInt;
	public ParserVal yyval;
	public boolean esCTE=false;
	public int tipo;
	public int tipoTabla=2;
	public String lastID;
	public Vector<String> reconocidos;
	public String lastCAD;
	public String lastCTE;
	public int cadenas=0;
	public boolean extra=false;
	
	
	public Transicion [][] TransicionEstados= {
			//12 es estado FINAL
    		//0  						1  							2  						 	3  							4  							5  							6  							7  							8  							9 							10 							11 							FINAL
/*LETRAS*/  {new AS2_InicLexema(8), 	new AS3_agregarAString(1), 	new AS3_agregarAString(1), 	new AS3_agregarAString(1), 	new AS3_agregarAString(1), 	new AS5_noConsumir(12), 	new AS3_agregarAString(6), 	new AS7_reconocerRango(12),	new AS3_agregarAString(8),	new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS5_noConsumir(12), 	new AS1_BuscarEnTabla()}, //Letras
/*DIGITOS*/	{new AS2_InicLexema(7),		new AS3_agregarAString(1), 	new AS3_agregarAString(1),	new AS3_agregarAString(1),	new AS3_agregarAString(1), 	new AS5_noConsumir(12), 	new AS3_agregarAString(6), 	new AS3_agregarAString(7),	new AS3_agregarAString(8),	new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS1_BuscarEnTabla()}, //Digitos
/* * */		{new AS2_InicLexema(5),		new AS3_agregarAString(1),  new AS3_agregarAString(1), 	new AS3_agregarAString(1), 	new AS3_agregarAString(1), 	new AS3_agregarAString(6), 	new AS3_agregarAString(6), 	new AS7_reconocerRango(12),	new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS5_noConsumir(12), 	new AS5_noConsumir(12), 	new AS1_BuscarEnTabla()}, // *
/* = */    	{new AS2_InicLexema(11),	new AS3_agregarAString(1),	new AS3_agregarAString(1),	new AS3_agregarAString(1),	new AS3_agregarAString(1),	new AS5_noConsumir(12),		new AS3_agregarAString(6),	new AS7_reconocerRango(12),	new AS5_noConsumir(12),		new AS3_agregarAString(12),	new AS3_agregarAString(12),	new AS3_agregarAString(12),	new AS1_BuscarEnTabla()}, // =
/* < */    	{new AS2_InicLexema(9), 	new AS3_agregarAString(1), 	new AS3_agregarAString(1), 	new AS3_agregarAString(1), 	new AS3_agregarAString(1), 	new AS5_noConsumir(12), 	new AS3_agregarAString(6), 	new AS7_reconocerRango(12), new AS5_noConsumir(12),		new AS5_noConsumir(12), 	new AS5_noConsumir(12), 	new AS5_noConsumir(12), 	new AS1_BuscarEnTabla()}, // <
/* > */    	{new AS2_InicLexema(10),	new AS3_agregarAString(1),	new AS3_agregarAString(1),	new AS3_agregarAString(1),	new AS3_agregarAString(1),	new AS5_noConsumir(12),		new AS3_agregarAString(6),	new AS7_reconocerRango(12),	new AS5_noConsumir(12),		new AS3_agregarAString(12),	new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS1_BuscarEnTabla()}, // >
/*unicos*/  {new AS2_InicLexema(12),	new AS3_agregarAString(1),	new AS3_agregarAString(1),	new AS3_agregarAString(1),	new AS3_agregarAString(1),	new AS5_noConsumir(12),		new AS3_agregarAString(6),	new AS7_reconocerRango(12),	new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS1_BuscarEnTabla()}, // ( ) : + - / , 
/* . */    	{new AS2_InicLexema(12),	new AS3_agregarAString(2),	new AS3_agregarAString(3),	new AS3_agregarAString(4),	new AS3_agregarAString(4),	new AS5_noConsumir(12),		new AS3_agregarAString(6),	new AS7_reconocerRango(12),	new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS1_BuscarEnTabla()}, // .
/* " */    	{new AS2_InicLexema(1),		new AS3_agregarAString(12),	new AS3_agregarAString(12),	new AS3_agregarAString(12),	new AS3_agregarAString(12),	new AS5_noConsumir(12),		new AS3_agregarAString(6),	new AS7_reconocerRango(12),	new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS1_BuscarEnTabla()}, // "
/* _ */  	{new AS6_Error(0), 			new AS3_agregarAString(1), 	new AS3_agregarAString(1), 	new AS3_agregarAString(1), 	new AS3_agregarAString(1), 	new AS5_noConsumir(12), 	new AS3_agregarAString(6), 	new AS7_reconocerRango(12),	new AS3_agregarAString(8),	new AS5_noConsumir(12), 	new AS5_noConsumir(12), 	new AS5_noConsumir(12), 	new AS1_BuscarEnTabla()}, // _
/*Salto*/   {new AS4_reconocerSalto(0), new AS4_reconocerSalto(-1) ,new AS4_reconocerSalto(-1),	new AS4_reconocerSalto(-1),	new AS4_reconocerSalto(1),	new AS5_noConsumir(12),		new AS4_reconocerSalto(0),	new AS7_reconocerRango(12),	new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS5_noConsumir(12),		new AS1_BuscarEnTabla()}, // Salto de Linea
/*Tab/Blanco*/{new AS2_InicLexema(0), 	new AS3_agregarAString(1),	new AS3_agregarAString(1), 	new AS3_agregarAString(1), 	new AS3_agregarAString(1), 	new AS5_noConsumir(12), 	new AS3_agregarAString(6), 	new AS7_reconocerRango(12),	new AS5_noConsumir(12),		new AS5_noConsumir(12), 	new AS5_noConsumir(12), 	new AS5_noConsumir(12), 	new AS1_BuscarEnTabla()},//	 Tab o Blanco	
	};
	
	public AnalizadorLexico(String Arch,ParserVal yyval) {
		TS = new HashMap<String, TablaSimbolos>();
		reconocidos= new Vector<String>();
		this.yyval=yyval;
		try {
			fuente = new BufferedReader(new FileReader(Arch) );
			endOfFile=false;
		} catch (FileNotFoundException e) {System.out.println("No encontro el archivoo");} 
		
		//Palabras Reservadas
		TS.put("IF", new TablaSimbolos(257));
		TS.put("ELSE",  new TablaSimbolos(259));
		TS.put("SWITCH",  new TablaSimbolos(263));
		TS.put("CASE",  new TablaSimbolos(264));
		TS.put("UINT",  new TablaSimbolos(265));
		TS.put("ULONG",  new TablaSimbolos(266));
		TS.put("LET",  new TablaSimbolos(269));
		TS.put("BEGIN",  new TablaSimbolos(261));
		TS.put("END",  new TablaSimbolos(262));
		TS.put("OUT",  new TablaSimbolos(276));
		TS.put("END_OUT",  new TablaSimbolos(6));
		TS.put("THEN",  new TablaSimbolos(258));
		TS.put("END_IF",  new TablaSimbolos(260));
		
		//Conversion
		TS.put("UI_UL",  new TablaSimbolos(277));
		
		//Comparadores y caracteres unicos
		TS.put("<",  new TablaSimbolos(60));
		TS.put(">",  new TablaSimbolos(62));
		TS.put("(",  new TablaSimbolos(40));
		TS.put(")",  new TablaSimbolos(41));
		TS.put("+",  new TablaSimbolos(43));
		TS.put("-",  new TablaSimbolos(45));
		TS.put(".", new TablaSimbolos( 46));
		TS.put("/",  new TablaSimbolos(47));
		TS.put("*",  new TablaSimbolos(42));
		TS.put("{",  new TablaSimbolos(123));
		TS.put("}",  new TablaSimbolos(125));
		TS.put("=",  new TablaSimbolos(61));
		TS.put("<=",  new TablaSimbolos(271));
		TS.put(">=",  new TablaSimbolos(272));
		TS.put("==",  new TablaSimbolos(270));
		TS.put("<>",  new TablaSimbolos(273));
		TS.put(":",  new TablaSimbolos(58));
		TS.put(",",  new TablaSimbolos(44));
		
		nuevosTokens=280;
	}
	
	public int numeroLinea(){
		return numLinea;
	}
	public String lexema(){
		return new String(lexema);
	}
	public String lastID(){
		return lastID;
	}
	
	public int nextToken(){
		tokenFound=false;
		while (!tokenFound && !endOfFile){
			int fila = -1;	
			if (consumir){
				try {
					readInt = fuente.read();
					if (extra){readInt=-1;}
					if ((readInt==-1)&&(!extra)){
						readInt=10;
						extra=true;}
				} catch (IOException e) {
				}}
			else	{
				consumir= true;
			}
			if (readInt!=-1) {
			
				c=(char) readInt;
				if ( Character.isDigit(c) ) { 																			//Caracteres Letras
					fila=1;}
				
				if (Character.isLetter(c)) {																			//Caracteres Digitos
					fila=0;}
				
				if ( (c=='/') || (c=='-') || (c=='+') || (c=='(') || (c==')') || (c==',') || (c==':') || (c=='{') || (c=='}') ) { 				//Caracteres unarios de transicion directa
					fila=6;}
				
				if ( (c=='\t') || (c==' ') ||(readInt==10) ){ 	//Espacios, Tabulaciones
					fila=11;}
				
				if ((readInt==13)){ //ENTER, no quiere leer correctamente el char salto de linea
					fila=10;}
				
				if (c=='.'){
					fila=7;}
				
				if (c=='*'){ 
					fila=2;}
				
				if (c=='='){
					fila=3;}
				
				if (c=='<'){
					fila=4;}
				
				if (c=='>'){
					fila=5;}
				
				if (c=='_'){
					fila=9;}
				
				if (c=='"'){
					fila=8;}
				
				if (fila==-1){
					System.out.println("Error reconociendo caracter, caracter no válido");}
				if (estadoAct==-1){
					reconocidos.add("Caracter no valido en la linea: "+ numLinea);
					estadoAct=0;}
				else{
					TransicionEstados[fila][estadoAct].ejecutar();}
					
			}
			else
			{
				endOfFile = true;
				token=0;
			}
		}
	return token;
	}

	
	public abstract class Transicion{
		public abstract void ejecutar();
	}
	
	public class AS1_BuscarEnTabla extends Transicion{
		
		
		public void ejecutar() {
			consumir=false;
			if (tipoTabla==1){ //ES CTE
				String aux=new String(lexema);
				if (tipo==275)
					TS.put(aux.toLowerCase(),  new TablaSimbolos(tipo,true,numLinea));
				else
					TS.put(aux.toLowerCase(),  new TablaSimbolos(tipo,false,numLinea));
				lastCTE=aux;
				esCTE=false;
				token=tipo;
				reconocidos.add("Se reconocio la constante "+ lexema+" en la linea "+ numLinea);
			}
			
			if (tipoTabla==2){ //ES IDENTIFICADOR
				String id = new String(lexema);
				if ( TS.containsKey(new String(id)) ){
					token = TS.get( (new String(id)) ).valorToken;
					if (token==278){
						lastID=new String(id);
					}
					reconocidos.add("Se reconocio el lexema "+ lexema+" en la linea "+ numLinea);
				}
				else{
					if ( TS.containsKey(new String("@"+id)) ){
							token=278;
							lastID=new String("@"+id);
					}else{
						id="@"+id;
						if (lexema.length()<16){
							String aux=new String(id);
							//TS.put(aux.toLowerCase(),  new TablaSimbolos(278,numLinea));
							lastID=aux.toLowerCase();
							token=278;
							reconocidos.add("Se reconocio el identificador "+ lexema+" en la linea "+ numLinea);
							}
						else{
							reconocidos.add("Identificador mas largo que 15 caracteres en la linea: "+numLinea);
							}
						}
					}
			}
			
			if (tipoTabla==3){ //ES CADENA
				//String aux=new String(lexema.substring(1, lexema.length()-1));
				String aux=new String(lexema);
				String nombre="CAD"+cadenas;
				TS.put(nombre,  new TablaSimbolos(267,numLinea,aux));
				cadenas++;
				lastCAD=nombre;
				token=267;
				reconocidos.add("Se reconocio la cadena "+ lexema+" en la linea "+ numLinea);
			}
			tipoTabla=2;
			estadoAct=0;
			tokenFound=true;
		}	
	}
	
	public class AS2_InicLexema extends Transicion{
		
		private int estadoProximo;
		
		public AS2_InicLexema (int estadoProximo){
			this.estadoProximo=estadoProximo;
		}

		public void ejecutar() {
			lexema= new StringBuilder();
			lexema.append(c);
			estadoAct=estadoProximo;
		}
		
	}
	
	public class AS3_agregarAString extends Transicion{
		
		private int estadoProximo;
		
		public AS3_agregarAString (int estadoProximo){
			this.estadoProximo=estadoProximo;
		}

		public void ejecutar() {
			if (c=='"'){tipoTabla=3;}else{tipoTabla=2;}
			if (c!='\n')
			lexema.append(c);
			estadoAct=estadoProximo;
		}
		
	}
	
	public class AS4_reconocerSalto extends Transicion{
		
		private int estadoProximo;
		
		public AS4_reconocerSalto (int estadoProximo){
			this.estadoProximo=estadoProximo;
		}

		public void ejecutar() {
			
			numLinea++;
			estadoAct=estadoProximo;
			if (estadoProximo==1){
				String aux=new String (lexema);
				aux=aux.substring(0,aux.length()-3);
				aux=aux+" ";
				lexema=new StringBuilder(aux);
			}	
		}
		
	}
	
	public class AS5_noConsumir extends Transicion{
		
		private int estadoProximo;
		
		public AS5_noConsumir (int estadoProximo){
			this.estadoProximo=estadoProximo;
		}

		public void ejecutar() {
			
			consumir = false;
			estadoAct=estadoProximo;
		}
		
	}
	
	public class AS6_Error extends Transicion{
		
		private int estadoProximo;
		
		public AS6_Error (int estadoProximo){
			this.estadoProximo=estadoProximo;
		}

		public void ejecutar() {
			
			System.out.println("Error léxico");
			estadoAct=estadoProximo;
		}
	}
	
	public class AS7_reconocerRango extends Transicion{
		
		private int estadoProximo;
		
		public AS7_reconocerRango (int estadoProximo){
			this.estadoProximo=estadoProximo;
		}

		public void ejecutar() {
			consumir=false;
			Long rango= new Long(new String(lexema));
			Long tope= new Long (new String("4294967296"));
			tipoTabla=1;
			if ((-1 < rango) && (rango < 65536)){
				tipo=275;
			}else{
				if ( (-1 < rango) && (rango< tope)){
					tipo=274;

				}else{
					reconocidos.add("Cosntante fuera de rango en la linea "+ numLinea);
					tipo=279;
				}
				}
			estadoAct=estadoProximo;
		}
	}


	
	
	


}


