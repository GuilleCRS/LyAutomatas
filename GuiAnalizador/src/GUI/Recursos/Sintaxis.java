package GUI.Recursos;

public enum Sintaxis {
    //Declaracion de clase mediante una expresion regular
    //space-publicORprivate-space-class-identificador-space
    //IDENTIFICADOR ==>   0
    DECLARACION_CLASE("((\\s)*)?((public|private))?(\\s)*((class))((\\s)*)"+Tipo.IDENTIFICADORES.patron+"((\\s)*)"),

    //Declaracion de main mediante una expresion regular
    //space-public-space-static-space-void-space-identificador-space-parentesis(-spaceOno-)parentesis-spaceOno
    //IDENTIFICADOR ==>   1
    DECLARACION_MAIN("((\\s)*)?(public)(\\s)*(static)(\\s)*(void)(\\s)*("+Tipo.IDENTIFICADORES.patron+")(\\s)*(\\()((\\s)*)(\\))(\\s)*"),

    //Se toman los brackets como posibilidad de sintaxis en linea
    //Abre Bracket
    //IDENTIFICADOR ==>   2
    LBRA("(\\s)*(\\{)(\\s)*"),
    //Declaracion de variable int x ; ,no debe existir la llave x, Hashmap llave=x objeto: (id=x tipo=entero valor=null)
    //No se debe comparar su tipo con el valor ya que aun no se inicializa
    //IDENTIFICADOR ==>   3
    VARDEC("(\\s)*(int|boolean|double|float)(\\s)"+ Tipo.IDENTIFICADORES.patron+"(\\s)*(\\;)(\\s)*"),

    //Inicializacion de variable x=5 ; ,posterior a su creacion ,debe existir la llave x en el hashmap y reemplazar su valor por 5 en este caso
    //Se debe confirmar que el tipo sea compatible con su valor
    //IDENTIFICADOR ==>   4
    VARINIT("(\\s*)"+Tipo.IDENTIFICADORES.patron+"(\\s)*(=)(\\s*)((true|false)|(((\\-)?\\d+(\\.)?\\d*)))(\\s)*(\\;)(\\s)*"),

    //Se inicializa la variable y se le da un valor int x = 5 ; , no debe existir la llave x, Hashmap llave=x objeto: (id=x tipo=entero valor=5)
    //Se debe confirmar que el tipo sea compatible con su valor
    //IDENTIFICADOR ==>   5
    VARDECINIT("(\\s)*(int|boolean|double|float)(\\s)"+Tipo.IDENTIFICADORES.patron+"(\\s)*(=)(\\s*)((true|false)|(((\\-)?\\d+(\\.)?\\d*)))(\\s)*(\\;)(\\s)*"),

    //Con variables ya inicializadas o numeros, se tienen que confirmar tipos compatibles, tipo de expresion (Recomendacion guardar en tabla aparte)
    //Despues del igual debe ir siempre un : sin espacios ejemplo ( x=:->Expresion<-; ) en este caso se llama indicador de expresion (INDIEXP)
    //IDENTIFICADOR ==>   6
   //EXPRESION("(\\s)*(\\w)+(\\s)*"+Tipo.INDIEX.patron+"(\\s)*(((\\w)+)|((\\-)?\\d+(\\.)?\\d*))(\\s)*(([\\+|\\-|\\*|\\/])(\\s)*(((\\w)+)|((\\-)?\\d+(\\.)?\\d*))(\\s)*)+(\\;)(\\s)*"),
    EXPRESSION("(\\s)*"+Tipo.IDENTIFICADORES.patron+"(\\s)*"+Tipo.INDIEX.patron+"(\\s)*(.)*"),
    //Declaracion del print mediante expresion regular (System.out.println ( "Se_imprime_cualquier_string_mientras_no_existan-espacios + "Multiples" )
    //IDENTIFICADOR ==>   7
    PRINT("(\\s)*(System.out.println)(\\s)*(\\()((\\s)*(\\\"((\\s)*((.)*)?(\\s)*\\\"[+]?)*)(\\s)*(\\))(\\s)*(;)(\\s)*)"),

    //Cierra bracket
    //IDENTIFICADOR ==> 8
    RBRA("(\\s)*(\\})(\\s)*"),

    //Linea nula
    //IDENTIFICADOR == 9
    LNULL("(\\s)*"),
    SINTAXIS_GENERAL("(\\s)*(9)*(\\s)*(0)(\\s)*(9)*(\\s)*(2)(\\s)*(9)*(\\s)*(3|5|(\\s))*(\\s)*(9)*(\\s)*(1)(\\s)*(9)*(\\s)*(2)(\\s)*(9)*(\\s)*(3|4|5|6|7|9|(\\s))*(\\s)*(8)(\\s)*(9)*(\\s)*(8)(\\s)*(9)*(\\s)*");

    /*** LOS IDENTIFICADORES SE ASIGNAN EN LA CLASE ANALIZADOR LEXICO ***/




    public final String patronSin;


    Sintaxis(String Regex){
        this.patronSin = Regex;
    }
}