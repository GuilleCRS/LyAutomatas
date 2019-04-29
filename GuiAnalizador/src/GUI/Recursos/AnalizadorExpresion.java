package GUI.Recursos;
/*Programa hecho por Guillermo Cruz para la materia Estructura de Datos con la profesora: Ekaterine Peralta*/
import java.util.Scanner;
import java.util.Stack;

public class AnalizadorExpresion {
    public AnalizadorExpresion(){}

    Pila pila = new Pila();


    public boolean Analisis(String exp) {
        for (int i = 0; i < exp.length(); i++) {
            char caract = exp.charAt(i);
            if ((caract == '(') || (caract == '{') || (caract == '[')) {
                pila.Inserta(caract);
                //System.out.println(pila.MostrarT());
            }
            if ((caract == '}') || (caract == ']') || (caract == ')')) {
                if (!pila.Vacia()) {
                    char caract2 = (char) pila.MostrarT();
                    char cart1 = ' ';
                    if (caract2 == '(') {
                        cart1 = ')';
                    }
                    if (caract2 == '[') {
                        cart1 = ']';
                    }
                    if (caract2 == '{') {
                        cart1 = '}';
                    }
                    if (pila.Vacia() == false) {
                        if (cart1 == caract) {
                            pila.Retira();
                           // System.out.println(caract);
                        }
                    } else if (pila.Vacia()) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        if (pila.Vacia()) {
            return true;
        } else if (pila.Vacia() == false) {//SI la pila no esta vacia es por que hubo un error
            return false;
        }
        return false;
    }
    public String Conversion(String exp) {
        Stack S = new Stack();
        String salida = "";
        String aux="";
        boolean decimal=false;
        for (int i = 0; i < exp.length(); i++) {
            char caract = exp.charAt(i);
            if (caract == '.') {
                salida=salida+caract;
                decimal=true;
                continue;
            }
            if(decimal){
                if(('0' <= caract && caract <= '9')){
                    salida=salida+caract;
                    decimal=false;
                    continue;
                }
            }
            if ((caract >= 'a' && caract <= 'z') || (caract >= 'A' && caract <= 'Z') ) {
                salida = salida  +caract;
            }else if( ('0' <= caract && caract <= '9')) salida+=" "+caract+" ";
            if ((caract == '+') || (caract == '*') || (caract == '/') || (caract == '-')) {
                S.push(caract);
            }
            if ((caract == '}') || (caract == ']') || (caract == ')')) {
                if (!S.empty()) {
                    salida = salida +"  "+ S.pop()+"  ";
                }
            }
        }
        while (!S.empty()) {
            salida = salida +" "+ S.pop();
        }
       // System.out.println("Expresion infija:  " + exp);
       return salida;
    }
}
