//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 1 "GramaticaFinal2.y"
import java.util.Vector;
import java.util.Stack;
import java.util.HashMap;
//#line 21 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IF=257;
public final static short THEN=258;
public final static short ELSE=259;
public final static short END_IF=260;
public final static short BEGIN=261;
public final static short END=262;
public final static short SWITCH=263;
public final static short CASE=264;
public final static short UINT=265;
public final static short ULONG=266;
public final static short CAD=267;
public final static short CADMULTI=268;
public final static short LET=269;
public final static short IGUAL=270;
public final static short MENIGUAL=271;
public final static short MAYIGUAL=272;
public final static short DISTINTO=273;
public final static short CTE_ULONG=274;
public final static short CTE_UINT=275;
public final static short OUT=276;
public final static short UI_UL=277;
public final static short ID=278;
public final static short CTE_FUERARANGO=279;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    3,    3,    3,    5,    5,
    6,    6,    4,    4,    4,    4,    4,   12,   12,    9,
    9,   15,   15,   15,   14,   14,   14,   10,   10,   18,
   18,   19,   19,   17,   17,   21,   22,   22,   11,   11,
   23,    8,    8,    7,   13,   13,   13,   24,   24,   24,
   25,   25,   25,   16,   26,   26,   26,   26,   26,   26,
   20,   20,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    1,    4,    3,    4,    3,    1,
    1,    1,    1,    2,    1,    1,    1,    4,    3,    2,
    2,    6,    6,    3,    3,    3,    4,    8,    3,    2,
    1,    4,    3,    4,    3,    1,    2,    1,    5,    3,
    1,    4,    3,    1,    3,    3,    1,    3,    3,    1,
    1,    1,    1,    3,    1,    1,    1,    1,    1,    1,
    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    2,    4,    5,
    0,    0,   13,   15,   16,   17,    0,    0,    0,    0,
    0,    0,    0,   14,    0,    0,    0,    3,    0,    0,
    0,    0,   20,    0,   21,   24,   61,   62,    0,   44,
   51,   53,    0,    0,   52,    0,   50,   29,    0,   40,
   41,    0,   43,    7,    0,   11,   12,    0,    9,    0,
    0,    0,   26,   25,    0,    0,    0,   57,   58,   59,
   60,    0,    0,   55,   56,    0,    0,    0,    0,    0,
    0,    8,    6,   42,   27,    0,   38,    0,   19,    0,
    0,    0,    0,    0,   48,   49,    0,   39,   35,    0,
   37,   18,   23,   22,    0,    0,   31,   34,    0,    0,
    0,   30,   33,    0,   28,   32,
};
final static short yydgoto[] = {                          6,
    7,    8,    9,   10,   11,   58,   41,   13,   14,   15,
   16,   42,   43,   17,   18,   44,   64,  106,  107,   45,
   65,   88,   52,   46,   47,   76,
};
final static short yysindex[] = {                      -191,
  -39,  -37, -246,  -33, -217,    0, -191,    0,    0,    0,
  -22,  -14,    0,    0,    0,    0, -209, -163,   25, -184,
   64, -166,  -14,    0,   67, -153,   69,    0,   70, -183,
 -166, -184,    0, -213,    0,    0,    0,    0,  -31,    0,
    0,    0,  -25,   77,    0,   42,    0,    0,   78,    0,
    0,   79,    0,    0,   75,    0,    0,   76,    0,   -5,
   80, -133,    0,    0, -191,   81, -184,    0,    0,    0,
    0, -184, -184,    0,    0, -184, -134, -184, -184,    2,
   82,    0,    0,    0,    0,   83,    0, -202,    0,   59,
   42,   42,   60, -199,    0,    0, -132,    0,    0,   84,
    0,    0,    0,    0, -222, -112,    0,    0,   85,   86,
   87,    0,    0, -127,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,   37,    0,  135,    0,    0,    0,
    0,  -16,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -41,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -177,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -35,  -29,   95,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  130,    0,  -19,    0,    0,   23,  136,    0,    0,
    0,    0,   -8,    0,    0,    0,  -65,    0,   32,   35,
    0,    0,    0,   34,   30,    0,
};
final static int YYTABLESIZE=248;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         47,
   20,   47,   22,   47,   47,   45,   26,   45,   67,   45,
   45,   46,  111,   46,   63,   46,   46,   72,   47,   73,
   47,   31,   12,   60,   45,   23,   45,   10,  104,   12,
   46,    5,   46,  109,   74,   30,   75,   72,   27,   73,
   84,   10,   61,    1,   49,   87,   32,   62,  116,    2,
   33,   37,   38,   59,    1,    3,   23,    1,   90,  100,
    2,   62,    4,    2,    5,    1,    3,   93,  101,    3,
   36,    2,   55,    4,  103,    5,    4,    3,    5,   36,
   44,   56,   57,   78,    4,   36,    5,   23,   79,   37,
   38,   36,   39,   40,   44,   34,   35,   44,   36,  102,
   36,   72,   72,   73,   73,   91,   92,   95,   96,   48,
   23,   40,   50,   51,   53,   54,   23,   77,   80,   81,
   82,   83,   86,   94,   97,   85,   89,   98,   99,  108,
  113,  105,  115,   62,    1,   54,   28,  112,   24,  110,
    0,    0,    0,  114,    0,    0,    0,    0,    0,    0,
    0,  105,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   19,    0,   21,    0,
    0,    0,   25,    0,   66,    0,    0,    0,   47,   47,
   47,   47,    0,   29,   45,   45,   45,   45,    0,   10,
   46,   46,   46,   46,   68,   69,   70,   71,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   40,   43,   40,   45,   46,   41,   40,   43,   40,   45,
   46,   41,  125,   43,   34,   45,   46,   43,   60,   45,
   62,   44,    0,   32,   60,    3,   62,   44,   94,    7,
   60,  278,   62,  256,   60,   58,   62,   43,  256,   45,
   46,   58,  256,  257,   22,   65,   61,  261,  114,  263,
  260,  274,  275,   31,  257,  269,   34,  257,   67,  262,
  263,  261,  276,  263,  278,  257,  269,   76,   88,  269,
   46,  263,  256,  276,   94,  278,  276,  269,  278,  257,
   44,  265,  266,   42,  276,  263,  278,   65,   47,  274,
  275,  269,  277,  278,   58,  259,  260,   61,  276,   41,
  278,   43,   43,   45,   45,   72,   73,   78,   79,   46,
   88,  278,   46,  267,   46,   46,   94,   41,   41,   41,
   46,   46,  256,  258,  123,   46,   46,   46,   46,   46,
   46,  264,   46,  261,    0,   41,    7,  106,    3,  105,
   -1,   -1,   -1,   58,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  264,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  256,   -1,  256,   -1,
   -1,   -1,  256,   -1,  256,   -1,   -1,   -1,  270,  271,
  272,  273,   -1,  256,  270,  271,  272,  273,   -1,  256,
  270,  271,  272,  273,  270,  271,  272,  273,
};
}
final static short YYFINAL=6;
final static short YYMAXTOKEN=279;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'",null,
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"IF","THEN","ELSE","END_IF","BEGIN","END",
"SWITCH","CASE","UINT","ULONG","CAD","CADMULTI","LET","IGUAL","MENIGUAL",
"MAYIGUAL","DISTINTO","CTE_ULONG","CTE_UINT","OUT","UI_UL","ID",
"CTE_FUERARANGO",
};
final static String yyrule[] = {
"$accept : programa",
"programa : listaSentencias",
"listaSentencias : sentencia",
"listaSentencias : listaSentencias sentencia",
"sentencia : sentenciaDeclarativa",
"sentencia : sentenciaNoDeclarativa",
"sentenciaDeclarativa : listaID ':' tipoVar '.'",
"sentenciaDeclarativa : listaID error '.'",
"sentenciaDeclarativa : listaID ':' error '.'",
"listaID : listaID ',' identificador",
"listaID : identificador",
"tipoVar : UINT",
"tipoVar : ULONG",
"sentenciaNoDeclarativa : asignacion",
"sentenciaNoDeclarativa : LET asignacion",
"sentenciaNoDeclarativa : bloqueIf",
"sentenciaNoDeclarativa : bloqueSwitch",
"sentenciaNoDeclarativa : out",
"conversion : UI_UL '(' expresion ')'",
"conversion : UI_UL error '.'",
"bloqueIf : bloqueConElse END_IF",
"bloqueIf : bloqueSinElse END_IF",
"bloqueSinElse : IF '(' condicion ')' THEN bloque",
"bloqueSinElse : IF '(' condicion ')' THEN sentenciaNoDeclarativa",
"bloqueSinElse : IF error '.'",
"bloqueConElse : bloqueSinElse ELSE bloque",
"bloqueConElse : bloqueSinElse ELSE sentenciaNoDeclarativa",
"bloqueConElse : bloqueSinElse ELSE error '.'",
"bloqueSwitch : SWITCH '(' identificador ')' '{' listaCase '}' '.'",
"bloqueSwitch : SWITCH error '.'",
"listaCase : listaCase bloqueCase",
"listaCase : bloqueCase",
"bloqueCase : CASE CTE ':' bloque",
"bloqueCase : CASE error '.'",
"bloque : begin listaSenteciasNoDeclarativas END '.'",
"bloque : BEGIN error '.'",
"begin : BEGIN",
"listaSenteciasNoDeclarativas : listaSenteciasNoDeclarativas sentenciaNoDeclarativa",
"listaSenteciasNoDeclarativas : sentenciaNoDeclarativa",
"out : OUT '(' cadena ')' '.'",
"out : OUT error '.'",
"cadena : CAD",
"asignacion : identificador '=' expresion '.'",
"asignacion : ID error '.'",
"identificador : ID",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : identificador",
"factor : CTE",
"factor : conversion",
"condicion : expresion comparador expresion",
"comparador : '<'",
"comparador : '>'",
"comparador : IGUAL",
"comparador : MENIGUAL",
"comparador : MAYIGUAL",
"comparador : DISTINTO",
"CTE : CTE_ULONG",
"CTE : CTE_UINT",
};

//#line 478 "GramaticaFinal2.y"


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
//#line 423 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 8 "GramaticaFinal2.y"
{	/*programa : listaSentencias*/
														exitos.add("The compiler goes skrra");}
break;
case 2:
//#line 12 "GramaticaFinal2.y"
{	/*listaSentencias : sentencia*/
														exitos.add("Lista de Sentencias reconocidas con exito en la linea: "+al.numeroLinea());
														if (!fueDeclarativa) {
															nodoListaSentencias = nodoSentencia;
														}
														fueDeclarativa = false;}
break;
case 3:
//#line 19 "GramaticaFinal2.y"
{	/*listaSentencias : listaSentencias sentencia*/
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
break;
case 4:
//#line 33 "GramaticaFinal2.y"
{	/*sentencia : sentenciaDeclarativa*/
														exitos.add("Sentencia reconocida con exito en la linea: "+al.numeroLinea());
														fueDeclarativa = true;}
break;
case 5:
//#line 37 "GramaticaFinal2.y"
{	/*sentencia : sentenciaNoDeclarativa*/
														exitos.add("Sentencia reconocida con exito en la linea: "+al.numeroLinea());
														esCASE = true;
														nodoSentencia = nodoSND;}
break;
case 6:
//#line 43 "GramaticaFinal2.y"
{	/*sentenciaDeclarativa: 	listaID ':' tipoVar '.'*/
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
break;
case 7:
//#line 65 "GramaticaFinal2.y"
{	/*sentenciaDeclarativa: listaID error '.'*/
														Ventana.Error(" Falta ':' al delcarar variables en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															/* TODO Auto-generated catch block*/
															e.printStackTrace();
														}}
break;
case 8:
//#line 74 "GramaticaFinal2.y"
{	/*sentenciaDeclarativa: listaID ':' error '.'*/
														Ventana.Error("No es una variable en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															/* TODO Auto-generated catch block*/
															e.printStackTrace();
														}}
break;
case 9:
//#line 84 "GramaticaFinal2.y"
{	/*listaID : listaID ',' identificador*/
														exitos.add("Variable "+ stackId.peek()+ " declarada en al linea "+ al.numeroLinea() );
														listaid.addElement(stackId.peek());
														stackId.pop();}
break;
case 10:
//#line 89 "GramaticaFinal2.y"
{	/*listaID : identificador*/
														exitos.add("Variable "+ stackId.peek()+ " declarada en al linea "+ al.numeroLinea() );
														listaid.clear();
														listaid.addElement(stackId.peek());
														stackId.pop();}
break;
case 11:
//#line 96 "GramaticaFinal2.y"
{	/*tipoVar : UINT*/
														tipo = true;}
break;
case 12:
//#line 98 "GramaticaFinal2.y"
{	/*tipoVar : ULONG*/
														tipo= false;}
break;
case 13:
//#line 102 "GramaticaFinal2.y"
{	/*sentenciaNoDeclarativa  : asignacion*/
														exitos.add("Sentencia No Declarativa reconocida con exito en la linea: "+al.numeroLinea());
														nodoSND = new ArbolSintactico("SENTENCIA", false);
														nodoSND.izq = A; }
break;
case 14:
//#line 107 "GramaticaFinal2.y"
{	/*sentenciaNoDeclarativa  : LET asignacion*/
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
break;
case 15:
//#line 129 "GramaticaFinal2.y"
{	/*sentenciaNoDeclarativa  :  bloqueIf*/
														exitos.add("Sentencia No Declarativa reconocida con exito en la linea: "+al.numeroLinea());
														nodoSND = new ArbolSintactico("SENTENCIA", false);
														nodoSND.izq = stackIF.pop();}
break;
case 16:
//#line 134 "GramaticaFinal2.y"
{	/*sentenciaNoDeclarativa  :  bloqueSwitch*/
														exitos.add("Sentencia No Declarativa reconocida con exito en la linea: "+al.numeroLinea());
														nodoSND = new ArbolSintactico("SENTENCIA", false);
														nodoSND.izq = stackSWITCH.pop();}
break;
case 17:
//#line 139 "GramaticaFinal2.y"
{	/*sentenciaNoDeclarativa  :  out*/
														exitos.add("Sentencia No Declarativa reconocida con exito en la linea: "+al.numeroLinea());
														nodoSND = new ArbolSintactico("SENTENCIA", false);
														nodoSND.izq = O; }
break;
case 18:
//#line 146 "GramaticaFinal2.y"
{ 	/*conversion  : UI_UL '('expresion ')'*/
														exitos.add("Conversion reconocida con exito en la linea: "+al.numeroLinea());
														nodoConversion = new ArbolSintactico("CONV", false);
														nodoConversion.izq = stackExp.pop();}
break;
case 19:
//#line 151 "GramaticaFinal2.y"
{	/*conversion  : UI_UL  error '.'*/
														Ventana.Error("No es una expresion en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															/* TODO Auto-generated catch block*/
															e.printStackTrace();
														}}
break;
case 20:
//#line 161 "GramaticaFinal2.y"
{	/*bloqueIf :    bloqueConElse END_IF*/
														exitos.add("BloqueIF reconocido con exito en la linea: "+al.numeroLinea());
														stackIF.push(nodoIF);}
break;
case 21:
//#line 165 "GramaticaFinal2.y"
{	/*bloqueIf :    bloqueSinElse END_IF	*/
														exitos.add("BloqueIF reconocido con exito en la linea: "+al.numeroLinea());
														stackIF.push(nodoIF);}
break;
case 22:
//#line 170 "GramaticaFinal2.y"
{	/*bloqueSinElse : IF '(' condicion ')' THEN bloque*/
														exitos.add("Bloque Sin Else reconocido con exito en la linea: "+al.numeroLinea());
														nodoIF = new ArbolSintactico("IF", false);
														nodoIF.izq = stackCOND.pop();
														nodoCuerpoIF = new ArbolSintactico("CUERPOIF", false);
														nodoCuerpoIF.izq = nodoBLOQUE;
														nodoIF.der = nodoCuerpoIF;	}
break;
case 23:
//#line 178 "GramaticaFinal2.y"
{	/*bloqueSinElse : IF '(' condicion ')' THEN sentenciaNoDeclarativa*/
														exitos.add("Bloque Sin Else reconocido con exito en la linea: "+al.numeroLinea());
														nodoIF = new ArbolSintactico("IF", false);
														nodoIF.izq = stackCOND.pop();
														nodoCuerpoIF = new ArbolSintactico("CUERPOIF", false);
														nodoCuerpoIF.izq = nodoSND;
														nodoIF.der = nodoCuerpoIF;}
break;
case 24:
//#line 186 "GramaticaFinal2.y"
{	/*bloqueSinElse : IF error '.' */
														Ventana.Error("No es una condicion en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															/* TODO Auto-generated catch block*/
															e.printStackTrace();
														}}
break;
case 25:
//#line 196 "GramaticaFinal2.y"
{	/*bloqueConElse : bloqueSinElse ELSE bloque*/
														exitos.add("Bloque ELSE reconocido con exito en la linea: "+al.numeroLinea());
														nodoIF.der.der = nodoBLOQUE; }
break;
case 26:
//#line 200 "GramaticaFinal2.y"
{	/*bloqueConElse : bloqueSinElse ELSE sentenciaNoDeclarativa*/
														exitos.add("Bloque ELSE reconocido con exito en la linea: "+al.numeroLinea());
														nodoIF.der.der = nodoSND; }
break;
case 27:
//#line 204 "GramaticaFinal2.y"
{	/*bloqueConElse :bloqueSinElse ELSE error '.' */
														Ventana.Error("No es una sentencia o bloque en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															/* TODO Auto-generated catch block*/
															e.printStackTrace();
														}}
break;
case 28:
//#line 214 "GramaticaFinal2.y"
{/*bloqueSwitch: SWITCH '(' identificador ')'  '{' listaCase '}' '.'	*/
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
break;
case 29:
//#line 232 "GramaticaFinal2.y"
{	/*bloqueSwitch: SWITCH error '.'*/
														Ventana.Error("No es un ID en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															/* TODO Auto-generated catch block*/
															e.printStackTrace();
														}}
break;
case 30:
//#line 242 "GramaticaFinal2.y"
{	/*listaCase : listaCase bloqueCase*/
														exitos.add("Lista de Case reconocido con exito en la linea: "+al.numeroLinea());
														nodoListaCASE.addMasDer(nodoCASE); }
break;
case 31:
//#line 246 "GramaticaFinal2.y"
{	/*listaCase : bloqueCase*/
														exitos.add("Lista de Case reconocido con exito en la linea: "+al.numeroLinea());
														nodoListaCASE = nodoCASE; }
break;
case 32:
//#line 251 "GramaticaFinal2.y"
{	/*bloqueCase: CASE CTE ':' bloque	*/
														exitos.add("Bloque CASE reconocido con exito en la linea: "+al.numeroLinea());
														nodoCASE = new ArbolSintactico("CASE", false);
														nodoCASE.izq = new ArbolSintactico("CUERPO", false);
														nodoCASE.izq.izq = stackCTE.pop();
														nodoCASE.izq.der = nodoBLOQUE;
														}
break;
case 33:
//#line 259 "GramaticaFinal2.y"
{	/*bloqueCase: CASE error '.' */
														Ventana.Error("No es una Constante en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															/* TODO Auto-generated catch block*/
															e.printStackTrace();
														}}
break;
case 34:
//#line 269 "GramaticaFinal2.y"
{	/*bloque: BEGIN listaSenteciasNoDeclarativas END'.'*/
														exitos.add("Bloque BEGIN END reconocido con exito en la linea: "+al.numeroLinea());
														nodoBLOQUE = nodoListaSND;
														nodoListaSND=stackBloque.pop();
														}
break;
case 35:
//#line 275 "GramaticaFinal2.y"
{	/*bloque: BEGIN error '.' */
														Ventana.Error("No es una lista de sentecias no delcarativas o falta END en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															/* TODO Auto-generated catch block*/
															e.printStackTrace();
														}}
break;
case 36:
//#line 285 "GramaticaFinal2.y"
{	/*begin: BEGIN 	*/
														stackBloque.push(nodoListaSND);
														nodoListaSND=null;
													}
break;
case 37:
//#line 290 "GramaticaFinal2.y"
{/*listaSenteciasNoDeclarativas: listaSenteciasNoDeclarativas sentenciaNoDeclarativa	*/
														exitos.add("Lista Sent. No Decl. reconocidas con exito en la linea: "+al.numeroLinea());
														nodoListaSND.addMasDer(nodoSND); }
break;
case 38:
//#line 294 "GramaticaFinal2.y"
{	/*listaSenteciasNoDeclarativas:sentenciaNoDeclarativa*/
														exitos.add("Lista Sent. No Decl. con exito en la linea: "+al.numeroLinea());
														nodoListaSND = nodoSND;}
break;
case 39:
//#line 299 "GramaticaFinal2.y"
{	/*out : OUT '(' cadena ')'  '.'*/
														exitos.add("Sentencia OUT reconocida con exito en la linea: "+al.numeroLinea());
														O = new ArbolSintactico("OUT",false);
														O.izq = new ArbolSintactico(al.lastCAD,false);}
break;
case 40:
//#line 304 "GramaticaFinal2.y"
{	/*out :OUT error '.' */
														Ventana.Error("No es una cadena en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															/* TODO Auto-generated catch block*/
															e.printStackTrace();
														}}
break;
case 41:
//#line 314 "GramaticaFinal2.y"
{	/*cadena : CAD 	*/
													}
break;
case 42:
//#line 317 "GramaticaFinal2.y"
{	/*asignacion :  identificador '=' expresion '.'*/
														String ID=stackId.pop();
														if (!AnalizadorLexico.TS.containsKey(ID)){
															Ventana.Error("La variable "+ID+ " no esta declarada en la linea: "+al.numeroLinea());
															try {
																this.wait();
															} catch (InterruptedException e) {
																/* TODO Auto-generated catch block*/
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
break;
case 43:
//#line 345 "GramaticaFinal2.y"
{	/*asignacion : ID error '.' 		*/
														Ventana.Error("Error en la asignacion en la linea: "+al.numeroLinea());
														try {
															this.wait();
														} catch (InterruptedException e) {
															/* TODO Auto-generated catch block*/
															e.printStackTrace();
														}}
break;
case 44:
//#line 355 "GramaticaFinal2.y"
{	/*identificador : ID  */
														stackId.push(al.lastID());}
break;
case 45:
//#line 359 "GramaticaFinal2.y"
{ 	/*expresion : expresion '+' termino*/
														AUX = new ArbolSintactico("+",false); 
														AUX.izq = stackExp.pop(); 
														AUX.der = stackTermino.pop(); 
														E= AUX;
														stackExp.push(E);
														}
break;
case 46:
//#line 367 "GramaticaFinal2.y"
{ 	/*expresion : expresion '-' termino */
														AUX = new ArbolSintactico("-",false); 
														AUX.izq = stackExp.pop(); 
														AUX.der = stackTermino.pop();  
														E= AUX;
														stackExp.push(E);
														}
break;
case 47:
//#line 375 "GramaticaFinal2.y"
{	/*expresion : termino	*/
														E=stackTermino.pop();
														stackExp.push(E);}
break;
case 48:
//#line 380 "GramaticaFinal2.y"
{	/*termino : termino '*' factor	*/
														AUX = new ArbolSintactico("*",false); 
														AUX.izq = stackTermino.pop();  
														AUX.der = stackFactor.pop();
														T= AUX;
														stackTermino.push(T);}
break;
case 49:
//#line 387 "GramaticaFinal2.y"
{	/*termino : termino '/' factor*/
														AUX = new ArbolSintactico("/",false); 
														AUX.izq = stackTermino.pop();  
														AUX.der = stackFactor.pop(); 
														T= AUX;
														stackTermino.push(T);}
break;
case 50:
//#line 394 "GramaticaFinal2.y"
{	/*termino : factor*/
														T=stackFactor.pop();
														stackTermino.push(T);}
break;
case 51:
//#line 399 "GramaticaFinal2.y"
{	/*factor : identificador */
														String ID=stackId.pop();
														String lexemaNuevo=ID;
														if (!AnalizadorLexico.TS.containsKey(ID)){
															Ventana.Error("La variable "+ID+ " no esta declarada en la linea: "+al.numeroLinea());
															try {
																this.wait();
															} catch (InterruptedException e) {
																/* TODO Auto-generated catch block*/
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
break;
case 52:
//#line 429 "GramaticaFinal2.y"
{	/*factor : CTE*/
														F = stackCTE.pop();
														stackFactor.push(F);}
break;
case 53:
//#line 433 "GramaticaFinal2.y"
{ 	/*factor : conversion*/
														F = nodoConversion;
														stackFactor.push(F);}
break;
case 54:
//#line 438 "GramaticaFinal2.y"
{	/*condicion : expresion comparador expresion 	*/
														exitos.add("Condicion reconocida con exito en la linea: "+al.numeroLinea());
														nodoCONDICION = new ArbolSintactico(nodoComparador, false); 
														nodoCONDICION.der = stackExp.pop();
														nodoCONDICION.izq = stackExp.pop();
														stackCOND.push(nodoCONDICION);
														}
break;
case 55:
//#line 447 "GramaticaFinal2.y"
{ 	/*comparador:  '<' */
														nodoComparador = "<" ;}
break;
case 56:
//#line 450 "GramaticaFinal2.y"
{ 	/*comparador:  '>'*/
														nodoComparador = ">" ;}
break;
case 57:
//#line 453 "GramaticaFinal2.y"
{ 	/*comparador: IGUAL*/
														nodoComparador = "=" ;}
break;
case 58:
//#line 456 "GramaticaFinal2.y"
{ 	/*comparador: MENIGUAL*/
														nodoComparador = "<=" ;}
break;
case 59:
//#line 459 "GramaticaFinal2.y"
{ 	/*comparador: MAYIGUAL*/
														nodoComparador = ">=" ;}
break;
case 60:
//#line 462 "GramaticaFinal2.y"
{ 	/*comparador: DISTINTO*/
														nodoComparador = "<>" ;}
break;
case 61:
//#line 466 "GramaticaFinal2.y"
{	/*CTE: 	CTE_ULONG*/
														nodoCTE = new ArbolSintactico(al.lastCTE, true, false);
														stackCTE.push(nodoCTE);
													}
break;
case 62:
//#line 471 "GramaticaFinal2.y"
{ 	/*CTE : CTE_UINT*/
														nodoCTE = new ArbolSintactico(al.lastCTE, true, true);
														stackCTE.push(nodoCTE);
													}
break;
//#line 1139 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
