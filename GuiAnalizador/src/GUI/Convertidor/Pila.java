package GUI.Convertidor;

public class Pila {
    private char[] pila;
    private char dato;
    private int tope = -1,max = 0;
    private boolean res;

    Pila(int max) {//el constructor pide el tama�o de la pila
        this.max = max;
        pila = new char[max];//se crea un arreglo del tamano pasado ateriormente
    }
    public boolean llena() {
        res = tope == (max - 1);

        return res;
    }

    public boolean vacia() {
        res = tope == -1;

        return res;
    }

    public boolean push(char dato) {
        if (llena()) {
            System.err.print("Error: Pila llena");
        } else {
            tope++;
            pila[tope] = dato;
            res = true;
        }
        return res;
    }

    public char pop() {
        if (vacia()) {
            System.err.print("Sub-Desbordamiento: Pila vacia");
        } else {
            dato = pila[tope];
            tope--;
        }
        return dato;
    }

    public char getTope() {
        char top = 0;
        if (!vacia()) {
            top = pila[tope];
        }
        return top;
    }

}