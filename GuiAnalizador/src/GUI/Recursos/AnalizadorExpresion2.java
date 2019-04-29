package GUI.Recursos;

import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

public class AnalizadorExpresion2 {
    public AnalizadorExpresion2(){}
    Pila pila=new Pila();
    Stack tripletero=new Stack();
    ArrayList<Triplo> triplos=new ArrayList<>();

    public boolean Analisis(String expresion){
        StringTokenizer tokenizer=new StringTokenizer(expresion," +-*/()^[]{}",true);
        while(tokenizer.hasMoreTokens()){
            String token=tokenizer.nextToken();
            if ((token.equals("(")) || (token.equals("{")) || (token.equals("["))) {
                pila.Inserta(token);
                //System.out.println(pila.MostrarT());
            }
            if ((token.equals("}")) || (token.equals("]")) || (token.equals(")"))) {
                if (!pila.Vacia()) {
                    String caract2 = (String) pila.MostrarT();
                    String cart1 = " ";
                    if (caract2.equals("(")) {
                        cart1 = ")";
                    }
                    if (caract2.equals("[")) {
                        cart1 = "]";
                    }
                    if (caract2.equals("{")) {
                        cart1 = "}";
                    }
                    if (!pila.Vacia()) {
                        if (cart1.equals(token)) {
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
        } else if (!pila.Vacia()) {
            return false;
        }
        return false;
    }

    public String Conversion(String expresion){
        Stack S =new Stack();
        String salida="";
        StringTokenizer tokenizer=new StringTokenizer(expresion, " +-/*^(){}[]",true);
        while(tokenizer.hasMoreTokens()){
            String token=tokenizer.nextToken();
            if(token.matches("([A-Z]+)?[a-z]+([0-9]+)?")){
                salida+= token+" ";
                tripletero.push(token);
            }
            if(token.matches(Tipo.DOUBLE.patron)||token.matches(Tipo.ENTERO.patron)){
                salida+=token+" ";
                tripletero.push(token);
            }
            if(token.matches("[+*\\-^/]")){
                S.push(token);
            }if(token.matches("[}\\])]")){
                if(!S.empty()){
                    String aux=S.pop().toString();
                    salida+=aux+" ";
                    tripletero.push(aux);
                }
            }
        }
        while (!S.empty()) {
            String aux=S.pop().toString();
            salida += aux +" ";
            tripletero.push(aux);
        }
        return salida;
    }

    public ArrayList<Triplo> tripletear(String expresion){
        ArrayList<Triplo> arreglo = new ArrayList<>();
        Triplo tr=new Triplo();
        int n=0;
        StringTokenizer tokenizer = new StringTokenizer(expresion," +-/*^",true);
        while(tokenizer.hasMoreTokens()){
            String token=tokenizer.nextToken();
            if(token.trim().isEmpty()){
                continue;
            }
            if(!token.matches("[+-/*^]")){
                tripletero.push(token);
            }else{
                n++;
                String id="t"+n;
                String t2=tripletero.pop().toString();
                String t1=tripletero.pop().toString();
                tripletero.push(id);
                System.out.println(id+" "+t1+" "+token+" "+t2);
                arreglo.add(new Triplo(id,t1,token,t2,null));
            }
        }
        return arreglo;
    }

}
