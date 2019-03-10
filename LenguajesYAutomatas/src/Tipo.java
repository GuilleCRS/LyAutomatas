public enum Tipo {
    RESERVADAS_DEL_SISTEMA("(class|public|static|void|extends|return|if|while|"
            + "System.out.println|lenght|case|break|this|do|package|"
            + "new|main|int|boolean|String|float)"),
    STRING("\"((.)+)?\""),
    ENTERO("^(\\-)?\\d+$"),
    DOUBLE("(\\-)?((\\d)+)?(\\.)(\\d)+"),
    BOOLEAN("(true|false)"),
    IDENTIFICADORES("([A-Z]+)?[a-z]+([0-9]+)?"),
    SIMBOLOS("\\p{Punct}*");


    public final String patron;

    Tipo(String s) {
        this.patron = s;

    }
}