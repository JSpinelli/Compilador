
public class TablaSimbolos {
	
	public int numLinea;
	public int valorToken;
	public boolean tipo;
	public String valorS=null;
	
	
	
	public TablaSimbolos(int valor){
		valorToken=valor;
		numLinea=-1;
	}
	
	public TablaSimbolos(int valor,int numLinea){
		valorToken=valor;
		this.numLinea=numLinea;
	}
	
	public TablaSimbolos( int valor,int numLinea,String valorS){
		valorToken=valor;
		this.numLinea=numLinea;
		this.valorS=valorS;
	}
	
	public TablaSimbolos(int valor, boolean tipo, int numLinea){
		valorToken=valor;
		this.numLinea=numLinea;
		this.tipo=tipo;
	}
	
	public String print(){
		if (numLinea!=-1)
			if (valorS!=null)
				return "Contenido: "+valorS+" Token: " + valorToken+"	Tipo: "+tipo+ "	Numero de linea:	"+numLinea;
			else
				return "Token: " + valorToken+"	Tipo: "+tipo+ "	Numero de linea:	"+numLinea;
		else
			return "";
	}
}
