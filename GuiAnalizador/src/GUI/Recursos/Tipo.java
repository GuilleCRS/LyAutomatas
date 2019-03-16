package GUI.Recursos;

public enum Tipo {
    RESERVADAS_DEL_SISTEMA("(class|public|static|void|if|while|"
            + "System.out.println|"
            + "main|int|boolean|String|float)"),
    STRING("\"((.)+)?\""),
    ENTERO("^(\\-)?\\d+$"),
    DOUBLE("(\\-)?((\\d)+)?(\\.)(\\d)+"),
    BOOLEAN("(true|false)"),
    IDENTIFICADORES("([A-Z]+)?[a-z]+([0-9]+)?"), //TODOS EN MINUSCULA O MAYUSCULA SEGUIDO DE UNA MINUSCULA Y NUMEROS OPCIONALES
    INDIEX("((\\:)(\\=))"),
    SIMBOLOS("\\p{Punct}*");


    public final String patron;

    Tipo(String s) {
        this.patron = s;

    }
}