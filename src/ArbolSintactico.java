import java.io.IOException;
import java.io.OutputStreamWriter;

public class ArbolSintactico {
	
		public ArbolSintactico izq=null;
		public ArbolSintactico der=null;
		public boolean esHoja=false;
		public String contenido=null;
		public boolean esReg=false;
		public boolean tipo;
		public int numLinea;
		
	    public ArbolSintactico(String contenido,boolean eshoja) {
	    	this.contenido=contenido;	
	    	this.esHoja=eshoja;
	    }
	    
	    public ArbolSintactico(String contenido,boolean eshoja,boolean tipo) {
	    	this.contenido=contenido;	
	    	this.esHoja=eshoja;
	    	this.tipo=tipo;
	    }
	    
	    public void addIzq (ArbolSintactico izq){
	    	this.izq=izq;
	    }
	   
	    public void addDer (ArbolSintactico der){
	    	this.der=der;
	    }
	    
	    public void addMasDer(ArbolSintactico derecha){
	    	if (this.der==null){
	    		this.der=derecha;
	    	}else{
	    		ArbolSintactico aux=this.der;
	    		while(aux.der!=null){
	    			aux=aux.der;
	    		}
	    		aux.der=derecha;
	    	}	
	    }
	    
	    public ArbolSintactico masDer(){
	    	if (this.der==null){
	    		return this;
	    	}else{
	    		ArbolSintactico aux=this.der;
	    		while(aux.der!=null){
	    			aux=aux.der;
	    		}
	    		return aux;
	    	}	
	    }
	    
	    public void saveArbol(String path){
	    	
	    }
	    
	    //Fuente de la que se obtuvo el metodo de impresion del arbol
	    //https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
	    
	    public void printTree(OutputStreamWriter out) throws IOException {
	        if (der != null) {
	        	der.printTree(out, true, "");
	        }
	        printNodeValue(out);
	        if (izq != null) {
	        	izq.printTree(out, false, "");
	        }
	    }
	    private void printNodeValue(OutputStreamWriter out) throws IOException {
	        if (contenido == null) {
	            out.write("<null>");
	        } else {
	            out.write(contenido);
	        }
	        out.write('\n');
	    }
	    
	    // use string and not stringbuffer on purpose as we need to change the indent at each recursion
	    private void printTree(OutputStreamWriter out, boolean isRight, String indent) throws IOException {
	        if (der != null) {
	        	der.printTree(out, true, indent + (isRight ? "        " : " |      "));
	        }
	        out.write(indent);
	        if (isRight) {
	            out.write(" /");
	        } else {
	            out.write(" \\");
	        }
	        out.write("----- ");
	        printNodeValue(out);
	        if (izq != null) {
	        	izq.printTree(out, false, indent + (isRight ? " |      " : "        "));
	        }
	    }
	    

}
