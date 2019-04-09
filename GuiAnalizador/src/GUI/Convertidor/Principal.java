package GUI.Convertidor;

import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {

        Conversor conver=new Conversor(50);

        System.out.println("Introduzca operacion inorder\n");
        String expresion = new Scanner(System.in).nextLine();
        String prefija = conver.prefija(expresion);
        String postfija = conver.postfija(expresion);
        System.out.println("Expresion Infija: " + expresion);
        System.out.println("Expresion postfija: " + postfija);
        System.out.println("Expresion prefija: " + prefija);


    }
}