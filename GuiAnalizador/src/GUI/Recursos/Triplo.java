package GUI.Recursos;

public class Triplo {
    private String id="T",t1,op,t2,resultado;
    private int n=1;
    private String idtriplo;

    public Triplo(String idtriplo, String t1, String op, String t2, String resultado) {
        this.idtriplo=idtriplo;
        this.t1 = t1;
        this.op = op;
        this.t2 = t2;
        this.resultado = resultado;
    }

    public Triplo() {

    }

    public String getIdtriplo() {
        return idtriplo;
    }

    public void setIdtriplo(String idtriplo) {
        this.idtriplo = idtriplo;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getT2() {
        return t2;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return idtriplo+"="+t1+op+t2;
    }
}
