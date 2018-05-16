import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class GeneradorCod {
	
	String CodFinal="";
	int contLabel=2;
	BufferedWriter writer= null;
	final static int MUL=2;
	final static int DIV=1;
	final static int OTRO=0;
	final static int BITS_8=0;
	final static int BITS_16=1;
	
	//Los de 8 bits
	final static String AL="AL";
	final static String BL="BL";
	final static String CL="CL";
	final static String DL="DL";
	final static String DH="DH";
	
	//Los de 16 bits
	final static String AX="AX";
	final static String BX="BX";
	final static String CX="CX";
	final static String DX="DX";
	boolean ax=false;
	boolean bx=false;
	boolean cx=false;
	boolean dx=false;
	ArbolSintactico pax;
	ArbolSintactico pbx;
	ArbolSintactico pcx;
	ArbolSintactico pdx;
	
	//Los de 32 bits
	final static String EAX="EAX";
	final static String EBX="EBX";
	final static String ECX="ECX";
	final static String EDX="EDX";
	boolean eax=false;
	boolean ebx=false;
	boolean ecx=false;
	boolean edx=false;
	ArbolSintactico peax;
	ArbolSintactico pebx;
	ArbolSintactico pecx;
	ArbolSintactico pedx;
	
	static File Codigo;
	
	public Stack<Integer> Labels=new Stack<Integer>();
	
	public GeneradorCod(String path, ArbolSintactico t) throws IOException{
		Codigo = new File("./"+path+".asm");
		try {
			writer = new BufferedWriter(new FileWriter(Codigo));
		} catch (IOException e2) {
			System.err.println("No se pudo crear archivo Codigo");
		}
		writer.write(".386");
		writer.newLine();
		writer.write(".model flat, stdcall");
		writer.newLine();
		writer.write("option casemap :none");
		writer.newLine();
		writer.write("include \\masm32\\include\\windows.inc");
		writer.newLine();
		writer.write("include \\masm32\\include\\kernel32.inc");
		writer.newLine();
		writer.write("include \\masm32\\include\\user32.inc");
		writer.newLine();
		writer.write("includelib \\masm32\\lib\\kernel32.lib");
		writer.newLine();
		writer.write("includelib \\masm32\\lib\\user32.lib");
		writer.newLine();
		writer.write(".data");
		writer.newLine();
		if (!(AnalizadorLexico.TS==null)){
		AnalizadorLexico.TS.forEach((String s , TablaSimbolos ts)->{
			try {
				if (ts.tipo && ts.valorToken==278){
					writer.write(s+" DW 0,0");
					writer.newLine();
					}else{
						if ((!ts.tipo) && ts.valorToken==278){
							writer.write(s+" DD 0,0");
							writer.newLine();	
						}else{
							if (ts.valorToken==267){
								writer.write(s+" DB "+ts.valorS+",0");
								writer.newLine();
							}
						}
					}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		}
		writer.write("TITULO"+" DB \"Error de Compilacion \",0");
		writer.newLine();
		writer.write("TERMINO"+" DB \"Termino la ejecucion \",0");
		writer.newLine();
		writer.write("SALIDA"+" DB \"Salida por pantalla \",0");
		writer.newLine();
		writer.write("RESTA_NEGATIVA"+" DB \"El resultado de una resta da negativo\",0");
		writer.newLine();
		writer.write("OVERFLOW_MULT"+" DB \"Hubo un overflow en una multiplicacion\",0");
		writer.newLine();
		writer.write("DIVISOR_CERO"+" DB \"Se quizo hacer una division por 0\",0");
		writer.newLine();
		writer.write(".code");
		writer.newLine();
		writer.write("start:");
		writer.newLine();
		this.generar(t);
		this.genSubError();
		writer.write("end start");
		writer.newLine();
		writer.flush();
		writer.close();
	}
	
	public void generar(ArbolSintactico t) throws IOException{
		switch (t.contenido){
			case "IF" : 		{this.genIF(t); break;}
			case "SWITCH" : 	{this.genSWITCH(t); break;}
			case "+" :			{this.genSUMA(t); break;}
			case "-" :			{this.genRESTA(t); break;}
			case "/" :			{this.genDIV(t); break;}
			case "*" :			{this.genMUL(t); break;}
			case "=" :			{this.genASG(t); break;}
			case "SENTENCIA" :	{this.genSENTENCIA(t); break;}
			case "CONV" :	    {this.genConver(t); break;}
			case "LET" :		{this.genLET(t); break;}
			case "OUT" :		{this.genOUT(t); break;}
			default :			{System.out.println("Algo se rompio, no reconoce el nodo: "+ t.contenido); break;}
		}
	}
	
	public String getReg32(int cod , ArbolSintactico t){
		
			if (!ebx){
				pebx=t;
				ebx=true;
				return EBX;
			}else{
				if (!ecx){
					pecx=t;
					ecx=true;
					return ECX;
				}else{
					if (!edx && cod!=MUL && cod!=DIV){
						pedx=t;
						edx=true;
						return EDX;
					}else{
						if(!eax){
							peax=t;
							eax=true;
							return EAX;
						}
					}
				}
			}
		return ";ACA HAY UN ERROR DE SEGUIMIENTO"; //NOS QUEDAMOS SIN REGISTROS
	}
	
	public String getReg32_Conv(int cod,String reg , ArbolSintactico t){
		

		if (!ebx && (!reg.equals(BX))){
			pebx=t;
			ebx=true;
			return EBX;
		}else{
			if (!ecx && (!reg.equals(CX))){
				pecx=t;
				ecx=true;
				return ECX;
			}else{
				if ((!edx) && (cod!=MUL)  &&(!reg.equals(DX))){
					pedx=t;
					edx=true;
					return EDX;
				}else{
					if(!eax&&(!reg.equals(AX))){
						peax=t;
						eax=true;
						return EAX;
					}
				}
			}
		}
	return ";ACA HAY UN ERROR DE SEGUIMIENTO"; //NOS QUEDAMOS SIN REGISTROS
}
	
	public String getReg16(int cod, ArbolSintactico t){
		
			if (!bx){
				pbx=t;
				bx=true;
				return BX;
			}else{
				if (!cx){
					pcx=t;
					cx=true;
					return CX;
				}else{
					if (!dx && cod!=MUL  && cod!=DIV){
						pdx=t;
						dx=true;
						return DX;
					}else{
						if(!ax){
							pax=t;
							ax=true;
							return AX;
						}
					}
				}
			}
		return ";ACA HAY UN ERROR DE SEGUIMIENTO"; //NOS QUEDAMOS SIN REGISTROS
	}
	
	public void pasarAx(ArbolSintactico t) throws IOException{
		if (!t.contenido.equals(AX)){
			if (ax){
				String r =this.getReg16(0, pax);
				writer.write("MOV "+ r +"," + AX );
				writer.newLine();
				writer.write("MOV "+ AX +"," + t.contenido);
				writer.newLine();
				pax=t;
				this.vaciar(t.contenido);
			}else{
				writer.write("MOV "+ AX +"," + t.contenido);
				writer.newLine();
				pax=t;
				this.vaciar(t.contenido);
			}
		}
		if (dx){
			String r =this.getReg32(0, pdx);
			writer.write("MOV "+ r +"," + DX);
			writer.newLine();
			writer.write("MOV "+ DX +"," + 0);
			writer.newLine();
			pdx=null;
			dx=false;
		}
	}
	
	public void pasarEAX(ArbolSintactico t) throws IOException{
		if (!t.contenido.equals(EAX)){
			if (eax){
				String r =this.getReg32(0, peax);
				writer.write("MOV "+ r +"," + EAX);
				writer.newLine();
				writer.write("MOV "+ EAX +"," + t.contenido);
				writer.newLine();
				peax=t;
				this.vaciar(t.contenido);
			}else{
				writer.write("MOV "+ EAX +"," + t.contenido);
				writer.newLine();
				peax=t;
				this.vaciar(t.contenido);
			}
		}
		if (edx){
			String r =this.getReg32(0, pedx);
			writer.write("MOV "+ r +"," + EDX);
			writer.newLine();
			writer.write("MOV "+ EDX +"," + 0);
			writer.newLine();
			pedx=null;
			edx=false;
		}
	}
	
	public void cleardx() throws IOException{
		if (dx){
			String r =this.getReg16(0, pdx);
			writer.write("MOV "+ r +"," + DX);
			writer.newLine();
			writer.write("MOV "+ DX +"," + 0);
			writer.newLine();
		}
	}
	
	public void clearedx() throws IOException{
		if (edx){
			String r =this.getReg32(0, pedx);
			writer.write("MOV "+ r +"," + EDX);
			writer.newLine();
			writer.write("MOV "+ EDX +"," + 0);
			writer.newLine();
		}
	}
	
	public void vaciar(String reg){
		switch (reg){
			case EAX:{
				eax=false; 
				}
			case EBX:{
				ebx=false;
				}
			case ECX:{
				ecx=false; 
				}
			case EDX:{
				edx=false;
				}
			case AX:{
				ax=false; 
				}
			case BX:{
				bx=false;
				}
			case CX:{
				cx=false; 
				}
			case DX:{
				dx=false;
				}
			}
	}
	
	public void vaciarTodo(){
		eax=false;
		ebx=false;
		ecx=false;
		edx=false;
		ax=false;
		bx=false;
		cx=false;
		dx=false;
	}
	
	public void addPila(int contLabel){
		Labels.push(new Integer(contLabel));
	}
	
	public int getPila(){
		return Labels.pop();
	}
	
	public void genSubError() throws IOException{
		writer.write("invoke MessageBox, NULL, addr TERMINO, addr TITULO , MB_OK ");
		writer.newLine();
		writer.write("EXIT:");
		writer.newLine();
		writer.write("invoke ExitProcess, 0");
		writer.newLine();
		writer.write("Label0:");
		writer.newLine();
		writer.write("invoke MessageBox, NULL, addr DIVISOR_CERO, addr TITULO , MB_OK ");
		writer.newLine();
		writer.write("JMP EXIT");
		writer.newLine();
		writer.write("Label1:");
		writer.newLine();
		writer.write("invoke MessageBox, NULL, addr RESTA_NEGATIVA, addr TITULO , MB_OK " );
		writer.newLine();
		writer.write("JMP EXIT");
		writer.newLine();
		writer.write("Label2:");
		writer.newLine();
		writer.write("invoke MessageBox, NULL, addr OVERFLOW_MULT, addr TITULO , MB_OK ");
		writer.newLine();
		writer.write("JMP EXIT");
		writer.newLine();
	}
	
	private void genSENTENCIA(ArbolSintactico t) throws IOException {
		this.generar(t.izq);
		this.vaciarTodo();
		if (t.der!=null){
			this.generar(t.der);
		}
	}
	
	public void genLET(ArbolSintactico t) throws IOException{ //Verificar Asig
		this.generar(t.izq);
		t.tipo=t.izq.tipo;
	}

	public void genIF(ArbolSintactico t) throws IOException{
		this.genCOMP(t.izq);
		if(t.der.der!=null){ 					//TIENE SECCION ELSE
			this.genCUERPO_ConElse(t.der.izq);
			this.genELSE(t.der.der);
		}else{									//NO TIENE SECCION ELSE
			 this.genCUERPO_SinElse(t.der.izq);
		}	
	}
	
	public void genCUERPO_ConElse(ArbolSintactico t) throws IOException{
		writer.write(";CUERPO CON ELSE");
		writer.newLine();
		this.generar(t);
		this.vaciar(t.contenido);
		contLabel++;
		writer.write("JMP "+ "Label"+contLabel);
		writer.newLine();
		writer.write("Label"+this.getPila()+":");
		writer.newLine();
		this.addPila(contLabel);
	}
	
	public void genCUERPO_SinElse(ArbolSintactico t) throws IOException{
		writer.write(";CUERPO SIN ELSE");
		writer.newLine();
		this.generar(t);
		this.vaciar(t.contenido);
		writer.write("Label"+this.getPila()+":");
		writer.newLine();
	}
	
	public void genELSE(ArbolSintactico t) throws IOException{
		writer.write(";ELSE");
		writer.newLine();
		this.generar(t);
		writer.write("Label"+this.getPila()+":");
		writer.newLine();
	}
	
	public void genSWITCH(ArbolSintactico t) throws IOException{
		String variable = t.izq.contenido;
		writer.write(";PRINCIPIO SWITCH");
		writer.newLine();
		this.genCASE(t.der,variable);
		writer.write(";FIN SWITCH");
		writer.newLine();
	}
	
	public void genCASE(ArbolSintactico t,String variable) throws IOException{
		//HACER IF
		contLabel++;
		this.addPila(contLabel);
		writer.write(";PRINCIPIO CASE");
		writer.newLine();
		writer.write("CMP "+ variable +", " + t.izq.izq.contenido);
		writer.newLine();
		writer.write("JNE "+ "Label"+contLabel); //VERIFICAR COMO QUEDA EL ARBOL
		writer.newLine();
		if(t.der!=null){ 					//TIENE OTRO CASE
			this.genCUERPO_ConElse(t.izq.der);
			this.genCASE(t.der,variable);
			writer.write("Label"+this.getPila()+":");
			writer.newLine();
		}else{									//NO TIENE OTRO CASE
			 this.genCUERPO_SinElse(t.izq.der);
		}

	}
	
	public void genCOMP(ArbolSintactico t) throws IOException{
		
		if (!t.izq.esHoja){
			this.generar(t.izq);
		}
		if (!t.der.esHoja){
			this.generar(t.der);
		}
		
		if (t.izq.tipo==t.der.tipo){
			contLabel++;
			this.addPila(contLabel);
			writer.write(";COMPARACION");
			writer.newLine();
			
			String r="";
			if (!t.izq.esReg){
				if (t.izq.tipo){
					r=this.getReg16(OTRO, t.der);
				}else{
					r=this.getReg32(OTRO, t.der);
				}
			}
			
			writer.write("MOV "+ r +" , "+t.izq.contenido);
			writer.newLine();
			t.izq.contenido=r;
			
			switch (t.contenido){
				case "<":	{
					writer.write("CMP "+ t.izq.contenido +", " + t.der.contenido);
					writer.newLine();
					writer.write("JAE "+ "Label"+contLabel); 
					writer.newLine();
					break;
					}
				case "<>":	{
					writer.write("CMP "+ t.izq.contenido +", " + t.der.contenido);
					writer.newLine();
					writer.write("JE " + "Label"+contLabel); 
					writer.newLine();
					break;
					}
				case ">=":	{
					writer.write("CMP "+ t.izq.contenido +", " + t.der.contenido); 
					writer.newLine();
					writer.write("JB " + "Label"+contLabel); 
					writer.newLine();
					break;
					}
				case "<=":	{
					writer.write("CMP "+ t.izq.contenido +", " + t.der.contenido);
					writer.newLine();
					writer.write("JA " + "Label"+contLabel); 
					writer.newLine();
					break;
					}
				case "=":	{
					writer.write("CMP "+ t.izq.contenido +", " + t.der.contenido); 
					writer.newLine();
					writer.write("JNE "+ "Label"+contLabel); 
					writer.newLine();
					break;
					}
				case ">":	{
					writer.write("CMP "+ t.izq.contenido +", " + t.der.contenido); 
					writer.newLine();
					writer.write("JBE "+ "Label"+contLabel); 
					writer.newLine();
					break;
					}
			
			}
		}else{
			Ventana.Error("Se quizo realizar una comparacion de distinto tipo");
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.vaciar(t.izq.contenido);
	}
	
	public void genSUMA(ArbolSintactico t) throws IOException{
		if (!t.izq.esHoja){
			this.generar(t.izq);
		}
		if (!t.der.esHoja){
			this.generar(t.der);
		}
		boolean tipo;
		tipo=t.izq.tipo;
		if (tipo==t.der.tipo){
			writer.write(";PRINCIPIO SUMA");
			writer.newLine();
			if (t.der.esReg && t.izq.esReg) {
				writer.write("ADD "+ t.izq.contenido +"," + t.der.contenido);
				writer.newLine();
				t.contenido=t.izq.contenido;
				this.vaciar(t.der.contenido);
				}
			else{
				if (t.izq.esReg){
					writer.write("ADD "+ t.izq.contenido +"," + t.der.contenido);
					writer.newLine();
					t.contenido=t.izq.contenido;
					}
				else{
					if (t.der.esReg){
						String reg;
						if (tipo)
							reg = this.getReg16( 0 , t );
						else
							reg = this.getReg32(0 , t );
						writer.write("MOV "+ reg +"," + t.izq.contenido);
						writer.newLine();
						writer.write("ADD "+ reg +"," + t.der.contenido);
						writer.newLine();
						t.contenido=reg;
						this.vaciar(t.der.contenido);
					}
					else{
						String reg;
						if (tipo)
							reg = this.getReg16(0,t);
						else
							reg = this.getReg32(0,t);
						writer.write("MOV "+ reg +", " + t.izq.contenido);
						writer.newLine();
						writer.write("ADD "+ reg +", " + t.der.contenido);
						writer.newLine();
						t.contenido=reg;
					}
				}
			}
			t.tipo=tipo;
			t.esReg=true;
		}
		else{
			Ventana.Error("Se quizo realizar una suma de distinto tipo");
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		writer.write(";Chequeo OVERFLOW");
		writer.newLine();
		writer.write("JO Label2");
		writer.newLine();
		
		writer.write(";TERMINO SUMA");
		writer.newLine();
	}
	
	public void genRESTA(ArbolSintactico t) throws IOException{
		if (!t.izq.esHoja){
			this.generar(t.izq);
		}
		if (!t.der.esHoja){
			this.generar(t.der);
		}
		boolean tipo=t.izq.tipo;
		
		if (tipo==t.der.tipo){
			writer.write(";PRINCIPIO RESTA");
			writer.newLine();
			if (t.der.esReg && t.izq.esReg) {
				writer.write("SUB "+ t.izq.contenido +"," + t.der.contenido);
				writer.newLine();
				t.contenido=t.izq.contenido;
				this.vaciar(t.der.contenido);
				}
			else{
				if (t.izq.esReg){
					writer.write("SUB "+ t.izq.contenido +"," + t.der.contenido);
					writer.newLine();
					t.contenido=t.izq.contenido;
					}
				else{
					if (t.der.esReg){
						String reg;
						if (tipo)
							reg = this.getReg16(0,t);
						else
							reg = this.getReg32(0,t);
						writer.write("MOV "+ reg +"," + t.izq.contenido);
						writer.newLine();
						writer.write("SUB "+ reg +"," + t.der.contenido);
						writer.newLine();
						t.contenido=reg;
						this.vaciar(t.der.contenido);
					}
					else{
						String reg;
						if (tipo)
							reg = this.getReg16(0,t);
						else
							reg = this.getReg32(0,t);
						writer.write("MOV "+ reg +", " + t.izq.contenido);
						writer.newLine();
						writer.write("SUB "+ reg +", " + t.der.contenido);
						writer.newLine();
						t.contenido=reg;
					}
				}
			}
			
			//ERROR RESTA DIO NEGATIVO
			writer.write(";Chequeo numero negativo");
			writer.newLine();
			writer.write("CMP "+ t.contenido +", " + 0 );
			writer.newLine();
			writer.write("JS Label1 ");
			writer.newLine();
			t.tipo=tipo;
			t.esReg=true;
		}
		else{
			Ventana.Error("Se quizo realizar una resta con operandos de distinto tipo");
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void genDIV(ArbolSintactico t) throws IOException{
		if (!t.izq.esHoja){
			this.generar(t.izq);
		}
		if (!t.der.esHoja){
			this.generar(t.der);
		}
		boolean tipo=t.izq.tipo;
		
		if (tipo==t.der.tipo){
			writer.write(";PRINCIPIO DIV");
			writer.newLine();
			
			String r=t.der.contenido;
			if (!t.der.esReg){
				if (t.der.tipo){
					r=this.getReg16(DIV, t.der);
					writer.write("MOV "+ DX +" , 0");
					writer.newLine();
				}else{
					r=this.getReg32(DIV, t.der);
					writer.write("MOV "+ EDX +" , 0");
					writer.newLine();
				}
			}
			
			writer.write("MOV "+ r +" , "+t.der.contenido);
			writer.newLine();
			t.der.contenido=r;
			
			//COMPARAR POR DIVISOR IGUAL A 0
			writer.write("CMP "+ t.der.contenido +", " + 0);
			writer.newLine();
			writer.write("JE "+ "Label0");
			writer.newLine();
			
			if (tipo){
				this.pasarAx(t.izq);
				t.contenido=AX;
			}else{
				this.pasarEAX(t.izq);
				t.contenido=EAX;
			}
			writer.write("DIV "+ t.der.contenido);
			writer.newLine();
			this.vaciar(t.der.contenido);

			t.tipo=tipo;
			t.esReg=true;}
		
		else{
			Ventana.Error("Se quizo realizar una division de distinto tipo");
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void genMUL(ArbolSintactico t) throws IOException{
		if (!t.izq.esHoja){
			this.generar(t.izq);
		}
		if (!t.der.esHoja){
			this.generar(t.der);
		}
		boolean tipo=t.izq.tipo;
		
		if (tipo==t.der.tipo){
			writer.write(";PRINCIPIO MUL");
			writer.newLine();
			
			String r="";
			if (!t.der.esReg){
				if (t.der.tipo){
					r=this.getReg16(OTRO, t.der);
				}else{
					r=this.getReg32(OTRO, t.der);
				}
			}
			
			writer.write("MOV "+ r +" , "+t.der.contenido);
			writer.newLine();
			t.der.contenido=r;
			
			if (tipo){
				this.pasarAx(t.izq);
				t.contenido=AX;
			}else{
				this.pasarEAX(t.izq);
				t.contenido=EAX;
			}
			writer.write("MUL "+ t.der.contenido);
			writer.newLine();
			this.vaciar(t.der.contenido);

			t.tipo=tipo;
			t.esReg=true;}
		
		else{
			Ventana.Error("Se quizo realizar una multiplicacion de distinto tipo");
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		writer.write(";Chequeo OVERFLOW");
		writer.newLine();
		writer.write("JO Label2");
		writer.newLine();
	}
	
	public void genMUL2(ArbolSintactico t) throws IOException{
		if (!t.izq.esHoja){
			this.generar(t.izq);
		}
		if (!t.der.esHoja){
			this.generar(t.der);
		}
		boolean tipo=t.izq.tipo;
		if (tipo==t.der.tipo){
			
			writer.write(";PRINCIPIO MUL");
			writer.newLine();
			
			if (tipo){
				this.cleardx();
			}else{
				this.clearedx();
			}
			
			if (t.der.esReg && t.izq.esReg) {
				writer.write("MUL "+ t.izq.contenido +"," + t.der.contenido);
				writer.newLine();
				t.contenido=t.izq.contenido;
				this.vaciar(t.der.contenido);
				}
			else{
				if (t.izq.esReg){
					writer.write("MUL "+ t.izq.contenido +"," + t.der.contenido);
					writer.newLine();
					t.contenido=t.izq.contenido;
					}
				else{
					if (t.der.esReg){
						String reg;
						if (tipo)
							reg = this.getReg16(2,t);
						else
							reg = this.getReg32(2,t);
						
						writer.write("MOV "+ reg +"," + t.izq.contenido);
						writer.newLine();
						writer.write("MUL "+ reg +"," + t.der.contenido);
						writer.newLine();
						t.contenido=reg;
						this.vaciar(t.der.contenido);
					}
					else{
						String reg;
						if (tipo)
							reg = this.getReg16(2,t);
						else
							reg = this.getReg32(2,t);
						
						writer.write("MOV "+ reg +", "+ t.izq.contenido);
						writer.newLine();
						writer.write("MUL "+ reg +", "+ t.der.contenido);
						writer.newLine();
						t.contenido=reg;
					}
				}
			}
			
			writer.write(";Chequeo OVERFLOW");
			writer.newLine();
			writer.write("JO Label2");
			writer.newLine();

			t.tipo=tipo;
			t.esReg=true;}
		else{
			Ventana.Error("Se quizo realizar una multiplicacion de distinto tipo");
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void genASG(ArbolSintactico t) throws IOException{
		if (!t.izq.esHoja){
			Ventana.Error("Se quizo asignar un valor a una constante");
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!t.der.esHoja){
			this.generar(t.der);
		}
		
		if (t.izq.tipo==t.der.tipo){
			writer.write(";ASIGNACION");
			writer.newLine();
			writer.write("MOV " + t.izq.contenido +", " + t.der.contenido);
			writer.newLine();
			this.vaciar(t.der.contenido);

		}else{
			Ventana.Error("Se quizo realizar una asignacion de distinto tipo");
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void genConver(ArbolSintactico t) throws IOException{
		
		if (!t.izq.esHoja){
			this.generar(t.izq);
		}
		String r = this.getReg32_Conv(OTRO,t.izq.contenido, t);
		writer.write(";CONVERSION");
		writer.newLine();
		writer.write("MOV " + r +", " + 0);
		writer.newLine();
		writer.write("MOV " + r.substring(1) +", " + t.izq.contenido);
		writer.newLine();
		t.contenido=r;
		t.esReg=true;
		t.tipo=false;
		this.vaciar(t.izq.contenido);
	}
	
	public void genOUT(ArbolSintactico t) throws IOException{
		writer.write("invoke MessageBox, NULL, addr " + t.izq.contenido + ", addr SALIDA , MB_OK");
		writer.newLine();
	}
	
	public static void maine(String[] args) throws IOException{ //Main que se uso para probar la generacion de codigo  a partir de un Arbol
		ArbolSintactico t = new ArbolSintactico("SENTENCIA", false);
			t.izq=new ArbolSintactico("IF", false);
				t.izq.izq=new ArbolSintactico( "<" , false,false);
					t.izq.izq.izq=new ArbolSintactico( "6", true,false);
					t.izq.izq.der=new ArbolSintactico( "6", true,false);
				t.izq.der=new ArbolSintactico( "CUERPO", true,false);
					t.izq.der.izq=new ArbolSintactico("SENTENCIA", false);
						t.izq.der.izq.izq=new ArbolSintactico("/", false);
							t.izq.der.izq.izq.der=new ArbolSintactico( "50", true,true);
							t.izq.der.izq.izq.izq=new ArbolSintactico( "150555", true,true);
					t.izq.der.der=new ArbolSintactico("SENTENCIA", false);
						t.izq.der.der.izq=new ArbolSintactico("*", false);
							t.izq.der.der.izq.der=new ArbolSintactico( "30000", true,true);
							t.izq.der.der.izq.izq=new ArbolSintactico( "30000", true,true);
			t.der=new ArbolSintactico("SENTENCIA", false);
				t.der.izq=new ArbolSintactico("-", false);
					t.der.izq.der=new ArbolSintactico("/", false);
						t.der.izq.der.izq=new ArbolSintactico( "50", true,false);
						t.der.izq.der.der=new ArbolSintactico( "50", true,false);
					t.der.izq.izq=new ArbolSintactico("+", false);
						t.der.izq.izq.izq=new ArbolSintactico( "50", true,false);
						t.der.izq.izq.der=new ArbolSintactico( "150555", true,false);
					
				t.der.der=new ArbolSintactico("SENTENCIA", false);
					t.der.der.izq=new ArbolSintactico("*", false);
						t.der.der.izq.izq=new ArbolSintactico( "30000", true,true);
						t.der.der.izq.der=new ArbolSintactico( "30000", true,true);
					t.der.der.der=null;
					
			//t.der.der=null;
						
	}
}

