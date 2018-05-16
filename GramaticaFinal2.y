%{import java.util.Vector;
import java.util.Stack;
import java.util.HashMap;%}
%token IF THEN ELSE END_IF BEGIN END SWITCH CASE UINT ULONG CAD CADMULTI LET IGUAL MENIGUAL MAYIGUAL DISTINTO CTE_ULONG CTE_UINT OUT UI_UL ID CTE_FUERARANGO
%start programa

%%
programa : listaSentencias 							{	//programa : listaSentencias
														exitos.add("The compiler goes skrra");}
;

listaSentencias : 		sentencia					{	//listaSentencias : sentencia
														exitos.add("Lista de Sentencias reconocidas con exito en la linea: "+al.numeroLinea());
														if (!fueDeclarativa) {
															nodoListaSentencias = nodoSentencia;
														}
														fueDeclarativa = false;}
														
					|	listaSentencias sentencia  		{	//listaSentencias : listaSentencias sentencia
														exitos.add("Lista de Sentencias reconocidas con exito en la linea: "+al.numeroLinea());
														if (!fueDeclarativa){
															if (nodoListaSentencias==null){
																nodoListaSentencias=nodoSentencia;
															}else{
																nodoListaSentencias.addMasDer(nodoSentencia);
															}
														}
														fueDeclarativa = false;
														}
														
;

sentencia :   	sentenciaDeclarativa				{	//sentencia : sentenciaDeclarativa
														exitos.add("Sentencia reconocida con exito en la linea: "+al.numeroLinea());
														fueDeclarativa = true;}
														
				| sentenciaNoDeclarativa			{	//sentencia : sentenciaNoDeclarativa
														exitos.add("Sentencia reconocida con exito en la linea: "+al.numeroLinea());
														esCASE = true;
														nodoSentencia = nodoSND;}
;

sentenciaDeclarativa: 	listaID ':' tipoVar '.'		{	//sentenciaDeclarativa: 	listaID ':' tipoVar '.'
														exitos.add("Sentencia declarativa reconocida con exito en la linea: "+al.numeroLinea());
														for ( String id : listaid ) {
															if ( AnalizadorLexico.TS.containsKey(id)) {
																if (repetidos.containsKey(id)){
																	int cantidadRepes = repetidos.get(id);
																	String arrobas = "";
																	for (int i = 0; i <= cantidadRepes; i++)
																		arrobas += "@";
																	repetidos.replace(id, cantidadRepes + 1);
																	AnalizadorLexico.TS.put( arrobas + id, new TablaSimbolos( 278,tipo, al.numeroLinea() ) );
																}
																else {
																AnalizadorLexico.TS.put("@"+id, new TablaSimbolos( 278,tipo, al.numeroLinea() ) );
																repetidos.put(id, 1);
																}
															}
															else
															AnalizadorLexico.TS.put(id, new TablaSimbolos( 278,tipo, al.numeroLinea() ) );
														}
													}

						| listaID error '.'	 		{	//sentenciaDeclarativa: listaID error '.'
														Ventana.Error(" Falta ':' al delcarar variables en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}}
														
						| listaID ':' error '.' 	{	//sentenciaDeclarativa: listaID ':' error '.'
														Ventana.Error("No es una variable en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}}
;

listaID : listaID ',' identificador 				{	//listaID : listaID ',' identificador
														exitos.add("Variable "+ stackId.peek()+ " declarada en al linea "+ al.numeroLinea() );
														listaid.addElement(stackId.peek());
														stackId.pop();}
														
		| identificador 			 				{	//listaID : identificador
														exitos.add("Variable "+ stackId.peek()+ " declarada en al linea "+ al.numeroLinea() );
														listaid.clear();
														listaid.addElement(stackId.peek());
														stackId.pop();}
;

tipoVar : UINT 										{	//tipoVar : UINT
														tipo = true;}
		| ULONG 									{	//tipoVar : ULONG
														tipo= false;}
;

sentenciaNoDeclarativa  : asignacion 				{	//sentenciaNoDeclarativa  : asignacion
														exitos.add("Sentencia No Declarativa reconocida con exito en la linea: "+al.numeroLinea());
														nodoSND = new ArbolSintactico("SENTENCIA", false);
														nodoSND.izq = A; }
														
						| LET asignacion			{	//sentenciaNoDeclarativa  : LET asignacion
														exitos.add("Sentencia No Declarativa reconocida con exito en la linea: "+al.numeroLinea());
														nodoSND = new ArbolSintactico("SENTENCIA", false);
														nodoLET = new ArbolSintactico("LET", false);
														if (!repetidos.containsKey(A.izq.contenido)){
															repetidos.put(A.izq.contenido, 1);
															A.izq.contenido = "@" + A.izq.contenido;
															A.izq.tipo=A.masDer().tipo;
															nodoLET.izq = A;
															nodoSND.izq = nodoLET;
															AnalizadorLexico.TS.put(A.izq.contenido, new TablaSimbolos(278,A.masDer().tipo, al.numeroLinea()));}	
														else {
															int cantidadRepes = repetidos.get(A.izq.contenido);
															String arrobas = "";
															for (int i = 1; i <= cantidadRepes; i++){
																arrobas += "@";
															}
															repetidos.replace(A.izq.contenido, cantidadRepes + 1);
															AnalizadorLexico.TS.put(arrobas + A.izq.contenido, new TablaSimbolos(278, A.masDer().tipo, al.numeroLinea()));
														}
													}
													
						| bloqueIf					{	//sentenciaNoDeclarativa  :  bloqueIf
														exitos.add("Sentencia No Declarativa reconocida con exito en la linea: "+al.numeroLinea());
														nodoSND = new ArbolSintactico("SENTENCIA", false);
														nodoSND.izq = stackIF.pop();}
														
						| bloqueSwitch				{	//sentenciaNoDeclarativa  :  bloqueSwitch
														exitos.add("Sentencia No Declarativa reconocida con exito en la linea: "+al.numeroLinea());
														nodoSND = new ArbolSintactico("SENTENCIA", false);
														nodoSND.izq = stackSWITCH.pop();}
														
						| out						{	//sentenciaNoDeclarativa  :  out
														exitos.add("Sentencia No Declarativa reconocida con exito en la linea: "+al.numeroLinea());
														nodoSND = new ArbolSintactico("SENTENCIA", false);
														nodoSND.izq = O; }
;


conversion  : UI_UL '('expresion ')' 				{ 	//conversion  : UI_UL '('expresion ')'
														exitos.add("Conversion reconocida con exito en la linea: "+al.numeroLinea());
														nodoConversion = new ArbolSintactico("CONV", false);
														nodoConversion.izq = stackExp.pop();}
														
			| UI_UL  error '.' 						{	//conversion  : UI_UL  error '.'
														Ventana.Error("No es una expresion en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}}
;

bloqueIf :    bloqueConElse END_IF					{	//bloqueIf :    bloqueConElse END_IF
														exitos.add("BloqueIF reconocido con exito en la linea: "+al.numeroLinea());
														stackIF.push(nodoIF);}
														
			| bloqueSinElse END_IF					{	//bloqueIf :    bloqueSinElse END_IF	
														exitos.add("BloqueIF reconocido con exito en la linea: "+al.numeroLinea());
														stackIF.push(nodoIF);}
;

bloqueSinElse : IF '(' condicion ')' THEN bloque 	{	//bloqueSinElse : IF '(' condicion ')' THEN bloque
														exitos.add("Bloque Sin Else reconocido con exito en la linea: "+al.numeroLinea());
														nodoIF = new ArbolSintactico("IF", false);
														nodoIF.izq = stackCOND.pop();
														nodoCuerpoIF = new ArbolSintactico("CUERPOIF", false);
														nodoCuerpoIF.izq = nodoBLOQUE;
														nodoIF.der = nodoCuerpoIF;	}
														
			| 	IF '(' condicion ')' THEN sentenciaNoDeclarativa {	//bloqueSinElse : IF '(' condicion ')' THEN sentenciaNoDeclarativa
														exitos.add("Bloque Sin Else reconocido con exito en la linea: "+al.numeroLinea());
														nodoIF = new ArbolSintactico("IF", false);
														nodoIF.izq = stackCOND.pop();
														nodoCuerpoIF = new ArbolSintactico("CUERPOIF", false);
														nodoCuerpoIF.izq = nodoSND;
														nodoIF.der = nodoCuerpoIF;}
														
			| IF error '.' 							{	//bloqueSinElse : IF error '.' 
														Ventana.Error("No es una condicion en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}}
;

bloqueConElse : bloqueSinElse ELSE bloque			{	//bloqueConElse : bloqueSinElse ELSE bloque
														exitos.add("Bloque ELSE reconocido con exito en la linea: "+al.numeroLinea());
														nodoIF.der.der = nodoBLOQUE; }
														
			  | bloqueSinElse ELSE sentenciaNoDeclarativa{	//bloqueConElse : bloqueSinElse ELSE sentenciaNoDeclarativa
														exitos.add("Bloque ELSE reconocido con exito en la linea: "+al.numeroLinea());
														nodoIF.der.der = nodoSND; }
														
			  | bloqueSinElse ELSE error '.' 		{	//bloqueConElse :bloqueSinElse ELSE error '.' 
														Ventana.Error("No es una sentencia o bloque en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}}
;

bloqueSwitch: SWITCH '(' identificador ')'  '{' listaCase '}' '.'{//bloqueSwitch: SWITCH '(' identificador ')'  '{' listaCase '}' '.'	
														exitos.add("Bloque SWITCH reconocido con exito en la linea: "+al.numeroLinea());
														nodoSWITCH = new ArbolSintactico("SWITCH", false);
														String ID = stackId.pop();
														String lexemaNuevo = ID;
														if (repetidos.containsKey(ID)){
															lexemaNuevo = "";
															int cantidadRepes = repetidos.get(ID);
															String arrobas = "";
															for (int i = 1; i <= cantidadRepes; i++){
																arrobas += "@";
																}
															lexemaNuevo = arrobas + ID;
														}
														nodoSWITCH.izq =new ArbolSintactico(lexemaNuevo, true, AnalizadorLexico.TS.get(lexemaNuevo).tipo);
														nodoSWITCH.der = nodoListaCASE;
														stackSWITCH.push(nodoSWITCH);}
														
			|  SWITCH error '.' 					{	//bloqueSwitch: SWITCH error '.'
														Ventana.Error("No es un ID en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}}
;

listaCase : listaCase bloqueCase					{	//listaCase : listaCase bloqueCase
														exitos.add("Lista de Case reconocido con exito en la linea: "+al.numeroLinea());
														nodoListaCASE.addMasDer(nodoCASE); }
														
		  | bloqueCase								{	//listaCase : bloqueCase
														exitos.add("Lista de Case reconocido con exito en la linea: "+al.numeroLinea());
														nodoListaCASE = nodoCASE; }
;

bloqueCase: CASE CTE ':' bloque						{	//bloqueCase: CASE CTE ':' bloque	
														exitos.add("Bloque CASE reconocido con exito en la linea: "+al.numeroLinea());
														nodoCASE = new ArbolSintactico("CASE", false);
														nodoCASE.izq = new ArbolSintactico("CUERPO", false);
														nodoCASE.izq.izq = stackCTE.pop();
														nodoCASE.izq.der = nodoBLOQUE;
														}
														
		  | CASE error '.' 							{	//bloqueCase: CASE error '.' 
														Ventana.Error("No es una Constante en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}}
;

bloque:  begin listaSenteciasNoDeclarativas END'.'	{	//bloque: BEGIN listaSenteciasNoDeclarativas END'.'
														exitos.add("Bloque BEGIN END reconocido con exito en la linea: "+al.numeroLinea());
														nodoBLOQUE = nodoListaSND;
														nodoListaSND=stackBloque.pop();
														}
														
		|BEGIN error '.' 							{	//bloque: BEGIN error '.' 
														Ventana.Error("No es una lista de sentecias no delcarativas o falta END en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}}
;

begin: BEGIN 										{	//begin: BEGIN 	
														stackBloque.push(nodoListaSND);
														nodoListaSND=null;
													}
;
listaSenteciasNoDeclarativas: listaSenteciasNoDeclarativas sentenciaNoDeclarativa {//listaSenteciasNoDeclarativas: listaSenteciasNoDeclarativas sentenciaNoDeclarativa	
														exitos.add("Lista Sent. No Decl. reconocidas con exito en la linea: "+al.numeroLinea());
														nodoListaSND.addMasDer(nodoSND); }
														
							| sentenciaNoDeclarativa{	//listaSenteciasNoDeclarativas:sentenciaNoDeclarativa
														exitos.add("Lista Sent. No Decl. con exito en la linea: "+al.numeroLinea());
														nodoListaSND = nodoSND;}
;

out : OUT '(' cadena ')'  '.'    					{	//out : OUT '(' cadena ')'  '.'
														exitos.add("Sentencia OUT reconocida con exito en la linea: "+al.numeroLinea());
														O = new ArbolSintactico("OUT",false);
														O.izq = new ArbolSintactico(al.lastCAD,false);}
														
		| OUT error '.' 							{	//out :OUT error '.' 
														Ventana.Error("No es una cadena en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}}
;

cadena : CAD										{	//cadena : CAD 	
													}

asignacion :  identificador '=' expresion '.' 		 {	//asignacion :  identificador '=' expresion '.'
														String ID=stackId.pop();
														if (!AnalizadorLexico.TS.containsKey(ID)){
															Ventana.Error("La variable "+ID+ " no esta declarada en la linea: "+al.numeroLinea());
															try {
																this.wait();
															} catch (InterruptedException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
															}}
														else {
															String lexemaNuevo = ID;
															exitos.add("se reconocio ID como factor en la linea: "+al.numeroLinea());
															if (repetidos.containsKey(ID)){
																lexemaNuevo = "";
																int cantidadRepes = repetidos.get(ID);
																String arrobas = "";
																for (int i = 1; i <= cantidadRepes; i++){
																	arrobas += "@";
																}
																lexemaNuevo = arrobas + ID;
															}
															A = new ArbolSintactico("=",false);
															nodoID = new ArbolSintactico(lexemaNuevo,true, AnalizadorLexico.TS.get(lexemaNuevo).tipo);
															A.der = stackExp.pop(); A.izq = nodoID;
														}
													}
													
						| ID error '.' 				{	//asignacion : ID error '.' 		
														Ventana.Error("Error en la asignacion en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}}
;

identificador : ID  								{	//identificador : ID  
														stackId.push(al.lastID());}
;

expresion : expresion '+' termino 					{ 	//expresion : expresion '+' termino
														AUX = new ArbolSintactico("+",false); 
														AUX.izq = stackExp.pop(); 
														AUX.der = stackTermino.pop(); 
														E= AUX;
														stackExp.push(E);
														}
														
          | expresion '-' termino 					{ 	//expresion : expresion '-' termino 
														AUX = new ArbolSintactico("-",false); 
														AUX.izq = stackExp.pop(); 
														AUX.der = stackTermino.pop();  
														E= AUX;
														stackExp.push(E);
														}
														
          | termino									{	//expresion : termino	
														E=stackTermino.pop();
														stackExp.push(E);}
;

termino : termino '*' factor						{	//termino : termino '*' factor	
														AUX = new ArbolSintactico("*",false); 
														AUX.izq = stackTermino.pop();  
														AUX.der = stackFactor.pop();
														T= AUX;
														stackTermino.push(T);}
														
        | termino '/' factor						{	//termino : termino '/' factor
														AUX = new ArbolSintactico("/",false); 
														AUX.izq = stackTermino.pop();  
														AUX.der = stackFactor.pop(); 
														T= AUX;
														stackTermino.push(T);}
														
        | factor									{	//termino : factor
														T=stackFactor.pop();
														stackTermino.push(T);}
;

factor : identificador 								{	//factor : identificador 
														String ID=stackId.pop();
														String lexemaNuevo=ID;
														if (!AnalizadorLexico.TS.containsKey(ID)){
															Ventana.Error("La variable "+ID+ " no esta declarada en la linea: "+al.numeroLinea());
															try {
																this.wait();
															} catch (InterruptedException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
															}
														}
														else {
															exitos.add("se reconocio ID como factor en la linea: "+al.numeroLinea());
															if (repetidos.containsKey(ID)){
																lexemaNuevo="";
																int cantidadRepes = repetidos.get(ID);
																String arrobas = "";
																for (int i = 1; i <= cantidadRepes; i++){
																	arrobas += "@";
																}
																lexemaNuevo = arrobas + ID;
															}	
														}
														F = new ArbolSintactico(lexemaNuevo, true);
														F.contenido = lexemaNuevo;
														F.tipo = AnalizadorLexico.TS.get(lexemaNuevo).tipo;
														stackFactor.push(F);
													}
													
		| CTE 										{	//factor : CTE
														F = stackCTE.pop();
														stackFactor.push(F);}
														
	  	| conversion								{ 	//factor : conversion
														F = nodoConversion;
														stackFactor.push(F);}
;

condicion : expresion comparador expresion 			{	//condicion : expresion comparador expresion 	
														exitos.add("Condicion reconocida con exito en la linea: "+al.numeroLinea());
														nodoCONDICION = new ArbolSintactico(nodoComparador, false); 
														nodoCONDICION.der = stackExp.pop();
														nodoCONDICION.izq = stackExp.pop();
														stackCOND.push(nodoCONDICION);
														}
;

comparador: 			 '<'       					{ 	//comparador:  '<' 
														nodoComparador = "<" ;}
														
						| '>'      					{ 	//comparador:  '>'
														nodoComparador = ">" ;}
														
						| IGUAL   					{ 	//comparador: IGUAL
														nodoComparador = "=" ;}
														
						| MENIGUAL					{ 	//comparador: MENIGUAL
														nodoComparador = "<=" ;}
														
						| MAYIGUAL 					{ 	//comparador: MAYIGUAL
														nodoComparador = ">=" ;}
														
						| DISTINTO					{ 	//comparador: DISTINTO
														nodoComparador = "<>" ;}
;

CTE: 	CTE_ULONG 									{	//CTE: 	CTE_ULONG
														nodoCTE = new ArbolSintactico(al.lastCTE, true, false);
														stackCTE.push(nodoCTE);
													}
													
	|	 CTE_UINT									{ 	//CTE : CTE_UINT
														nodoCTE = new ArbolSintactico(al.lastCTE, true, true);
														stackCTE.push(nodoCTE);
													}
;

%%


public AnalizadorLexico al;
public ArbolSintactico AUX;
public ArbolSintactico A;
public ArbolSintactico E;
public ArbolSintactico E2;
public ArbolSintactico T;
public ArbolSintactico F;
public ArbolSintactico C;
public ArbolSintactico nodoID;
public ArbolSintactico O;
public ArbolSintactico nodoELSE;
public ArbolSintactico nodoIF;
public ArbolSintactico nodoCuerpoIF;
public ArbolSintactico nodoBLOQUE;
public ArbolSintactico nodoCONDICION;
public ArbolSintactico nodoSentencia;
public ArbolSintactico nodoSD;
public ArbolSintactico nodoSND;
public ArbolSintactico nodoListaSND;
public ArbolSintactico nodoConversion;
public ArbolSintactico nodoLET;
public ArbolSintactico nodoListaSentencias;
public ArbolSintactico nodoSWITCH;
public ArbolSintactico nodoCuerpoSwitch;
public ArbolSintactico nodoCuerpoCASE;
public ArbolSintactico nodoCASE;
public ArbolSintactico nodoCTE;
public ArbolSintactico nodoListaCASE;
public ArbolSintactico nodoCTECASE;
public String nodoComparador;
public boolean tipo;
public boolean fueDeclarativa = false;
public boolean esCASE = true;
public Stack<String> stackId=new Stack<String>();
public Stack<ArbolSintactico> stackExp=new Stack<ArbolSintactico>();
public Stack<ArbolSintactico> stackCTE=new Stack<ArbolSintactico>();
public Stack<ArbolSintactico> stackIF=new Stack<ArbolSintactico>();
public Stack<ArbolSintactico> stackSWITCH=new Stack<ArbolSintactico>();
public Stack<ArbolSintactico> stackCOND=new Stack<ArbolSintactico>();
public Stack<ArbolSintactico> stackBloque=new Stack<ArbolSintactico>();
public Stack<ArbolSintactico> stackFactor=new Stack<ArbolSintactico>();
public Stack<ArbolSintactico> stackTermino=new Stack<ArbolSintactico>();

public Parser(String Arch){
		yyval =new ParserVal();
		al=new AnalizadorLexico(Arch,yyval);
}

public int yylex(){
	return al.nextToken();
}

public Vector<String> listaid = new Vector<String>();
public Vector<String> exitos= new Vector<String>();
public Vector<String> errores= new Vector<String>();
public HashMap<String, Integer> repetidos = new HashMap<String, Integer>();

public void ImprimirMensajes(){

	System.out.println("MENSAJES DE CREACION DE ESTRUCTURAS");
	exitos.forEach((s) ->{
		System.out.println(s);
	});
	if (!errores.isEmpty()){
		System.out.println("**********************************");
		System.out.println("MENSAJES DE ERROR");
		errores.forEach((s) ->{
			System.out.println(s);
		});
	}else{
		System.out.println("NO HUBO ERRORES ");
	}
}

public void yyerror(String s){
	Ventana.Error(s+" en la linea: "+al.numeroLinea());
	try {
		this.wait();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
}
