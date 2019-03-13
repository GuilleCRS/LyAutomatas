package GUI.Recursos;

public class Expresion {
    private String asigna,expresion;
    private int linea;
    public Expresion(){}

    public Expresion(String asigna, String expresion) {
        this.asigna = asigna;
        this.expresion = expresion;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
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
