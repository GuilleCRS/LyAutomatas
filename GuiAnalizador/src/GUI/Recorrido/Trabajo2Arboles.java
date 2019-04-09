package GUI.Recorrido;

import java.util.Scanner;

/**
 * Created by guill on 02/05/2017.
 */
public class Trabajo2Arboles {
    private final Scanner nc=new Scanner(System.in);
    private final ArbolBB<String> abb=new ArbolBB<>();
    public static void main(String[]Args){
        Trabajo2Arboles t2=new Trabajo2Arboles();
        int op=0;
        do{
            System.out.println("\n1°- Agregar Elemento");
            System.out.println("2°- Imprime InOrden");
            System.out.println("3°- Imprime PosOrden");
            System.out.println("4°- Imprime PreOrden");
            System.out.println("5°- Buscar elementos");
            System.out.println("6°- Buscar elementos no recursivo");
            System.out.println("7°- Cuenta elementos");
            System.out.println("8°- Altura del arbol");
            System.out.println("9°- Salir");
            System.out.println("Digita tu opcion");
            try{ op=new Scanner(System.in).nextInt();
            }catch (Exception e){ System.out.println("Verifique su opcion e intente nuevamente");}

            switch(op){
                case 1:
                    t2.Agrega();
                    break;
                case 2:
                    t2.Inorden();
                    break;
                case 3:
                    t2.Posorden();
                    break;
                case 4:
                    t2.Preorden();
                    break;
                case 5:
                    t2.Busca();
                    break;
                case 6:
                    t2.Buscanr();
                    break;
                case 7:
                    t2.CuentaEle();
                    break;
                case 8:
                    t2.Altura();
                    break;
            }
        }while(op!=9);
    }
    private void Altura() {
        System.out.println("La altura es : "+abb.Altura(abb.DameRaiz(),0));

    }
    private void Agrega() {
        System.out.println("Inserta el dato a agregar");
        String dato=nc.nextLine();
        if(!dato.equals("")){
            abb.Inserta(dato);
        }
    }
    private void Inorden() {
        abb.inorder(abb.DameRaiz());
    }
    private void Posorden() {
        abb.posorder(abb.DameRaiz());

    }
    private void Preorden() {
        abb.preorder(abb.DameRaiz());

    }
    private void Busca() {
        System.out.println("Dame dato a buscar ");
        String dato=nc.nextLine();
        if(!dato.equals("")) {
            if (abb.Busca(abb.DameRaiz(), dato)) {
                System.out.println("Dato encontrado");
            }
        }
    }
    private void Buscanr() {
        System.out.println("Dame dato a buscar ");
        String dato = nc.nextLine();
        if (!dato.equals("")) {
            if (abb.buscar(dato)) {
                System.out.println("Dato encontrado");
            }
        }
    }
    private void CuentaEle(){
        System.out.println("El numero de elementos en el arbol es: "+abb.cuenta(abb.DameRaiz()));

    }

}
