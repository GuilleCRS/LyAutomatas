package GUI.Recursos;


import java.util.Stack;
import java.util.StringTokenizer;

public class Conversor {

    private boolean esOperador(char c) {

        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^'
                || c == '(' || c == ')';

    }


    private boolean esEspacio(char c) {

        return (c == ' ');

    }


    private boolean valorJerarquico(char op1, char op2) {
        switch (op1) {

            case '+':
            case '-':
                return !(op2 == '+' || op2 == '-');

            case '*':
            case '/':
                return op2 == '^' || op2 == '(';

            case '^':
                return op2 == '(';

            case '(':
                return true;

            default:
                return false;
        }

    }


    public String aPostorder(String infija) {

        Stack operadores = new Stack();

        char c;

        StringTokenizer stkn = new StringTokenizer(infija, "+-*/^() ", true);

        StringBuilder postfix = new StringBuilder();
        while (stkn.hasMoreTokens()) {

            String token = stkn.nextToken();
            c = token.charAt(0);

            if ((token.length() == 1) && esOperador(c)) {

                while (!operadores.empty() && !valorJerarquico(((String) operadores.peek()).charAt(0), c))
                    postfix.append(" ").append((String) operadores.pop());

                if (c == ')') {
                    String operator = (String) operadores.pop();
                    while (operator.charAt(0) != '(') {
                        postfix.append(" ").append(operator);
                        operator = (String) operadores.pop();
                    }
                } else
                    operadores.push(token);

            } else if ((token.length() == 1) && esEspacio(c)) {
            } else {
                postfix.append(" ").append(token);
            }

        }
        while (!operadores.empty())
            postfix.append(" ").append((String) operadores.pop());


        return postfix.toString();


    }

}