package GUI.Convertidor;

class Conversor {
    private int max;
    Conversor(int max){
        this.max=max;
    }
    String postfija(String expresion) {
        StringBuilder ExpPostfija = new StringBuilder();
        Pila pila = new Pila(max);

        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            if (esOperador(c)) {
                if (c == ')') {

                    while (pila.getTope() != '(')
                        ExpPostfija.append(pila.pop());
                    if (pila.getTope() == '(')
                        pila.pop();
                }

                if (pila.vacia()) {
                    if (c != ')')
                        pila.push(c);
                } else {
                    if (c != ')') {
                        push(ExpPostfija, pila, c);
                    }
                }
            } else
                ExpPostfija.append(c);
        }
        while (!pila.vacia()) {
            ExpPostfija.append(pila.pop());
        }
        return ExpPostfija.toString();
    }
    private static void push(StringBuilder postfija, Pila pila, char letra) {
        int pe = prioridadExp(letra);
        int pp = prioridadPila(pila.getTope());
        if (pe > pp) {
            pila.push(letra);
        } else {
            postfija.append(pila.pop());
            pila.push(letra);
        }
    }

    String prefija(String expresion) {
        StringBuilder ExprPrefija = new StringBuilder();
        StringBuilder ExpInvertida = new StringBuilder();
        Pila pila = new Pila(max);

        for (int i = expresion.length() - 1; i > -1; i--) {
            char c = expresion.charAt(i);
            if (esOperador(c)) {
                if (c == '(') {

                    while (pila.getTope() != ')')
                        ExprPrefija.append(pila.pop());
                    if (pila.getTope() == ')')
                        pila.pop();
                }

                if (pila.vacia()) {
                    if (c != '(')
                        pila.push(c);
                } else {
                    if (c != '(') {
                        push(ExprPrefija, pila, c);
                    }

                }

            } else
                ExprPrefija.append(c);

        }

        while (!pila.vacia()) {
            ExprPrefija.append(pila.pop());
        }

        for (int a = ExprPrefija.length() - 1; a >= 0; a--)
            ExpInvertida.append(ExprPrefija.charAt(a));
        return ExpInvertida.toString();
    }

    private static int prioridadExp(char x) {
        if (x == '^') return 4;
        if (x == '*' || x == '/') return 2;
        if (x == '+' || x == '-') return 1;
        if (x == '(') return 5;
        if (x == ')') return 5;
        return 0;
    }

    private static int prioridadPila(char x) {
        if (x == '^') return 3;
        if (x == '*' || x == '/') return 2;
        if (x == '+' || x == '-') return 1;
        if (x == '(') return 0;
        if (x == ')') return 0;
        return 0;
    }

    private static boolean esOperador(char letra) {
        return letra == '*' || letra == '/' || letra == '+' || letra == '-' || letra == '^' || letra == '(' || letra == ')';
    }
}