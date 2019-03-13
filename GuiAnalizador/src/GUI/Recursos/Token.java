package GUI.Recursos;

public class Token {
    String token,tipo,linea,valor,columna;

    public Token(String token, String tipo, String linea, String columna, String valor) {
        this.token = token;
        this.tipo = tipo;
        this.linea = linea;
        this.valor = valor;
        this.columna = columna;
    }
    public Token(){
    }

    public String getColumna() {
        return columna;
    }

    public void setColumna(String columna) {
        this.columna = columna;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
