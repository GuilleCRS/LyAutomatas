package GUI.Recursos;

public class Expresion {
    private String asigna,expresion,postorder;
    private int linea;


    public Expresion(){}

    public Expresion(String asigna, String expresion,String postorder) {
        this.asigna = asigna;
        this.expresion = expresion;
        this.postorder = postorder;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }
    public void setPostorder(String postorder){
        this.postorder=postorder;
    }

    public String getPostorder() {
        return postorder;
    }

    public String getAsigna() {
        return asigna;
    }

    public void setAsigna(String asigna) {
        this.asigna = asigna;
    }

    public String getExpresion() {
        return expresion;
    }

    public void setExpresion(String expresion) {
        this.expresion = expresion;
    }
}
