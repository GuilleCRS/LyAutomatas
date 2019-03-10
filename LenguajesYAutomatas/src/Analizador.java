
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analizador {
    HashMap<Integer,String> texto=new HashMap<Integer,String>();//Hashmap contiene la linea y el texto de respecitva linea
    HashMap<Integer,String> tipoSintactico=new HashMap<>();//Contiene los tipos de sintaxis ejemplo: DECLRACION_CLASE = 0
    HashMap<String,Token> Variables=new HashMap<>();//Contiene las variables que se encuentran durante el analisis semantico
  //  ArrayList<String> ContenedorLlavesVariables=new ArrayList<>();//Contiene las llaves de variables que existen durante
    ArrayList<String> ErrSemantico=new ArrayList<>();//Contiene los errores que se encuentran durante el analisis semantico
    ArrayList<Token> Tokens = new ArrayList<Token>();//Contiene la lista de todos los tokens aceptados durante el analisis lexico con sus respectivos valores
    ArrayList<Token> TokensNoAceptados = new ArrayList<Token>();//Contiene la lista de todos los tokens no aceptados durante el analisis lexico
    ArrayList<String> ErroresSintaxis = new ArrayList<>();//Aqui se encuentran los errores que puedan existir durante el analisis sintacitoc
    String ProgramaSintaxis="";//Variable que contendra la sintaxis de el codigo fuente general
    //Banderas manejadoras de errores en caso de que existan errores lexicos o sintacticos
    boolean ErrLex=false,ErrSin=false,ErrSem=false,ArchivoAbierto=false;
    boolean Lex=false,Sin=false,Sem=false;

    public Analizador() throws IOException {
        int opcionMenu;
        java.util.Scanner nc=new java.util.Scanner(System.in);
        if(Leer("src/prueba.txt")){
            do {
                System.out.println("Que desea hacer?\n" +
                        "1°- Analizador Lexico\n" +
                        "2°- Analizador Sintactico\n" +
                        "3°- Analizador Semantico\n" +
                        "0°- SALIR.\n" +
                        "Digite su opcion:\n");
                         opcionMenu=nc.nextInt();
                switch (opcionMenu){
                    case 1: {
                        System.out.println(" <-- Codigo Fuente --> ");
                        RecorreHM(texto,"","# ");
                        System.out.println("\nSe realizara analisis lexico\n");
                        AnalizadorLexico();
                        ChequeoErrores();
                        break;
                    }
                    case 2:{
                        System.out.println(" <-- Codigo Fuente --> ");
                        RecorreHM(texto,"","# ");
                        if(!ErrLex&&Lex){
                            System.out.println("\nSe realizara analisis sintactico");
                            AnalizadorSintactico();
                            System.out.println(ProgramaSintaxis);
                            ChequeoErrores();
                        }else{
                            System.out.println("Verifique errores en el analisis Lexico");
                        }
                        break;
                    }
                    case 3:{
                        System.out.println(" <-- Codigo Fuente --> ");
                        RecorreHM(texto,"","# ");
                        if(!ErrLex&&Lex&&!ErrSin&&Sin){
                            System.out.println("\nSe realizara analisis semantico");
                            AnalizadorSemantico();
                            ChequeoErrores();
                            //ChequeoCompatibilidadSemantica();
                        }else{
                            System.out.println("Hay errores en alguno de los analisis previos o no se han realizado");
                        }
                        break;
                    }
                }
               /* RecorreHM(texto, "Numero de Linea:", "\t");
                AnalizadorLexico();
                AnalizadorSintactico();
                ChequeoErrores();*/
            }while(opcionMenu!=0);
        }else{
            System.out.println(" Existen errores en el archivo a leer, no se puede proceder");
        }
    }

    private void ChequeoErrores() {
        //Verifica si hay errores lexicos
        if(ErrLex){
            //Se recorren los elementos no aceptados
            System.out.println("\nLos siguientes elementos no fueron encontrados en la gramatica\n");
            TokensNoAceptados.iterator().forEachRemaining((v)-> System.out.println("Token: "+v.getToken()
                    +" Linea: "+v.getLinea()+" Columna: "+v.getColumna()));
            System.out.println("\nFin elementos no encontrados\n");
        }
        //Verifica si existen errores Sintacticos
        if(ErrSin){
            //Se recorren los errores en sintaxis
            System.out.println("\nSe muestran errores Sintacticos\n");
            ErroresSintaxis.forEach(System.out::println);
            System.out.println("\nFin errores Sintacticos\n");
        }
        if(ErrSem){
            System.out.println("\nSe muestran errores Semanticos\n");
            ErrSemantico.forEach(System.out::println);
            System.out.println("\nFin errores Semanticos");
        }

    }

    //Codigo no utilizado
    {
    /*private void AnalizadorLexico() throws IOException {
        boolean bandera=false;
        Leer("src/Hola.txt");
        for(int i=0;i<texto.size();i++){
            String linea= texto.get(i);
            String token="";
            String tipo="";
            for(int j=0;j<linea.length();j++){
                char curChar=linea.charAt(j);
                if(curChar==':'){
                    bandera=true;
                    continue;
                }
                if(!bandera){
                    tipo=tipo+curChar;
                }
                if(bandera){
                    token=token+curChar;
                }
            }
            gramatica.put(token,tipo);
            bandera=false;
            token="";
            tipo="";

        }
        RecorreHM(gramatica,"Token","Tipo");
    }
    */
    }
    private void AnalizadorLexico(){
        Lex=true;
        String palabra="";
        //Iteracion for que recorre las lineas del archivo txt
        //Como se maneja un hashmap se recorren las lineas apartir del 1
        //ir al metodo Leer
        for(int i=1;i<=texto.size();i++){
            String lineaActual=texto.get(i);
            //Convertimos la linea en tokens con el tokenizer
            StringTokenizer stkn=new StringTokenizer(lineaActual," ={[()]}+-/*;\n\t\r",true);
            //Creamos una variable para contar la columna tratada
            int colm=0;
            //Mientras existan mas tokens tomamos una variable y le asignamos la palabra(token)
            while(stkn.hasMoreTokens()){
                colm++;
                palabra=stkn.nextToken();
                Boolean Matched=false; //Manejador de errores, si es = true se aceptaron los tokens
                //Si el token detectado es un token en blanco se consume y se va al siguiente token
                if(palabra.trim().isEmpty()){
                    colm--; //Si hay un esapacio no contamos esa columna
                    continue;
                }
                for(Tipo TokenTipo : Tipo.values()){
                    //Se establece un patron REGEX para comparar
                    Pattern patron= Pattern.compile(TokenTipo.patron);
                    //Se establece lo que queremos comparar ante el patron REGEX
                    Matcher matcher = patron.matcher(palabra);
                    if(matcher.matches()){
                        //Aqui entraria si hace match y se crea un objeto para guardar el token con su valor
                        Token tk=new Token();
                        tk.setToken(palabra);
                        tk.setTipo(TokenTipo.toString());
                        tk.setLinea(String.valueOf(i));
                        tk.setColumna(String.valueOf(colm));
                        Tokens.add(tk);
                        //System.out.println(tk.getToken()+" "+tk.getTipo());
                        //Matched se convierte en veradero para saltar a la proxima iteracion
                        Matched=true;
                        break;
                    }
                }
                //Caso contrario no hizo match entonces lo agregamos a la lista de errores
                if(!Matched){
                    System.out.println("Se encontró un error lexico:");
                    //Establecemos que hay errores lexicos
                    ErrLex=true;
                    //Agregamos a un arreglo los errores lexicos encontrados
                    Token tkna=new Token();
                    tkna.setToken(palabra);
                    tkna.setLinea(String.valueOf(i));
                    tkna.setColumna(String.valueOf(colm));
                    TokensNoAceptados.add(tkna);
                }
            }
        }
        //Se recorren los elementos aceptados en la gramatica
        System.out.println("Los siguientes elementos fueron encontrados en la gramatica");
        Tokens.iterator().forEachRemaining((v)->System.out.println("Token: "+v.getToken()+"\tTipo: "+v.getTipo()+"\tValor: "+v.getValor()
                +"\tLinea: "+v.getLinea()+"\tColumna: "+v.getColumna()));
        System.out.println("\nFin elementos encontrados\n");
    }
    private void AnalizadorSintactico(){
        Sin=true;
        CargaTablaDeSintaxis();
        //Se inicializa la variable que contendra la sintaxis general del programa prueba para compararlo con una expresion regular
        //Se comienza a recorrer linea por linea
        for(int i=1;i<=texto.size();i++){
            String lineaActual=texto.get(i);
            //Se recorre los tipos de sintaxis que puede haber en cada linea
            boolean Matched=false;
            for(Sintaxis sin : Sintaxis.values()){
                Pattern patron=Pattern.compile(sin.patronSin);
                Matcher matcher=patron.matcher(lineaActual);
                //Si la linea actual hace match con algun patron de expresion regular entra en el if
                if(matcher.matches()){
                    //System.out.print("Linea "+i+": "+sin.toString());
                    Matched=true;
                    //Se recorren los tipos de sintaxis posibles ejemplo DECLARACION_CLASE la cual su valor es 0
                    for(int j=0;j<tipoSintactico.size();j++){
                        //Si el nombre del patron en curso es igual a alguno del recorrido en tipo sintaxis entra en el if
                        if (sin.toString().equals(tipoSintactico.get(j))) {
                           //System.out.println(" ==> "+j);
                            //Se establece una sintaxis de programa por ejemplo si fuese DECLARACION_CLASE la cual su valor es 0 , repetida 3 veces
                            //en las primeras 3 lineas, se forma un string que seria " 000 "
                            //Si solo fuese en las primeras 2 se formaria un string que seria " 00 "
                            //Si solo fuese en las primeras 2 y posteriormente en la 3er linea un LBRA( { ) el cual su valor es 2
                            //se formaria un string que seria " 002 " y esa seria la logica, posteriormente se compara el string obtenido
                            //con otra expresion regular para ver si la sintaxis de el programa completo es correcta
                            ProgramaSintaxis=ProgramaSintaxis+j;
                            break;
                        }
                    }
                    break;
                }
            }
            //Como no hace match entra aqui y el error se agrega a un arreglo de errores de sintaxis
            if(!Matched){
                System.out.println("Se encontro un error sintactico");
                ErroresSintaxis.add("ERROR ==> "+lineaActual+"  <== Verifique la sintaxis en la linea: #"+i);
                ErrSin=true;
            }
        }
        System.out.println("");
        //Verifica si el patron obtenido hace match con el de la sintaxis general y si no existen errores sintacticos por linea
        if(!Pattern.matches(Sintaxis.SINTAXIS_GENERAL.patronSin,ProgramaSintaxis)) {
            System.out.printf("General");
            String SintaxisToString="<";
            ErrSin=true;//Error sintactico se hace true ya que se encontro uno
            ErroresSintaxis.add("Error ==> El programa puede tener una sintaxis correcta pero tiene una mala estructura," +
                    " organice e intente de nuevo <== ERROR");
            //Este arreglo sirve para recorrer el codigo sintactico y obetener exactamente la estructura exacta del programa, asi facilitar
            //La manera de encontrar el error
            for(int kk=0;kk<ProgramaSintaxis.length();kk++){
                String llave=String.valueOf(ProgramaSintaxis.charAt(kk));
                SintaxisToString=SintaxisToString+"<"+tipoSintactico.get(Integer.parseInt(llave)) +">";
            }
            ErroresSintaxis.add("La sintaxis tiene la siguiente forma:\n"+SintaxisToString+">");
        }
    }
    private void AnalizadorSemantico(){
        Sem=true;
        //El largo de ProgramaSintaxis siempre va a ser igual al numero de lineas en el
        //Lo recorro desde 1 porque las lineas del texto comienzan desde la 1
        for(int i=1;i<=ProgramaSintaxis.length();i++) {
            //Aqui utilizamos i-1 para comenzar con el 0 porque el largo de la palabra generada comienza desde 0
            int indice = Integer.parseInt(String.valueOf(ProgramaSintaxis.charAt(i-1)));
            //Si el indice es igual a VARDEC == 3 o VARDECINIT == 5 entrara al if dado que no necesita verificar su existencia previa
            //System.out.println("Linea: "+i);
            if(indice==3||indice==5){
                //System.out.println("Indice: "+indice+" Linea: "+i);
                Token tkn=new Token();
                ArrayList<String> ArregloAyudante=new ArrayList<>();
                //System.out.println(texto.get(i));
                //Tokenizamos la linea que naturalmente tendria los dos tipos de sintaxis previamente mencionados
                StringTokenizer stkn=new StringTokenizer(texto.get(i)," ={[()]}+-/*;\n\t\r",true);
                //De acuerdo a la sintaxis el primer valor es el tipo de variable
                //El segundo debe ser el identificador
                //El tercero debe ser el simbolo = que puede depender su existencia en caso de que se inicialice la variable o solo se declare
                //El cuarto el valor de la variable que puede depender si solo se declara la variable o tambien se inicializa
                while(stkn.hasMoreTokens()) {//Recorremos los tokens dentro de la linea
                    //Un ayudante para contener los tokens
                    String ayudante = stkn.nextToken();
                    //Si el token en cuestion es un campo vacio , un signo igual o un punto y coma pasamos al siguiente token
                    //El punto y coma seria el ultimo token posible por lo tanto saldria del while en este punto
                    if (ayudante.trim().isEmpty() || ayudante.equals("=") || ayudante.equals(";")) {
                        continue;
                    }
                 //   System.out.println(ayudante);
                    //Se agrega cada ayudante a un arreglo en caso de no ser vacio , igual o ;
                    //Nuestra sintaxis nos dice que pueden existir un arreglo con tamaño 2 o 3
                    ArregloAyudante.add(ayudante);
                }
                String regex="";//Iniciamos un auxiliar para contener la expresion regular
                String ayudante2=ArregloAyudante.get(0);//sacamos el tipo exacto del token
                if(ayudante2.equals("int")){  regex=Tipo.ENTERO.patron;}//el ayudante se vuelve el valor de la expresion regular dependiendo el tipo
                if(ayudante2.equals("double")){  regex=Tipo.DOUBLE.patron; }
                if(ayudante2.equals("float")){  regex=Tipo.DOUBLE.patron; }
                if(ayudante2.equals("boolean")){  regex=Tipo.BOOLEAN.patron; }
                //Vamos a ver que tipo de declaracion es para agregar a la tabla de variables
                //Tamaño 3 = TIPO + VARIABLE/TOKEN + VALOR
                if(ArregloAyudante.size()==3){
                    if(!Variables.containsKey(ArregloAyudante.get(1))) {
                        if (Pattern.matches(regex, ArregloAyudante.get(2))) { //si el valor de la variable hace match con el tipo se agrega a la tabla
                            tkn.setTipo(ArregloAyudante.get(0));
                            tkn.setToken(ArregloAyudante.get(1));
                            tkn.setValor(ArregloAyudante.get(2));
                            tkn.setLinea(String.valueOf(i));
                            Variables.put(ArregloAyudante.get(1), tkn);
                        } else {//caso contrario hay un error semantico
                            ErrSem = true;
                            ErrSemantico.add("La variable: ' " + ArregloAyudante.get(1) + "' de tipo: ' " + ayudante2 + " ' no es compatible con el valor: ' " + ArregloAyudante.get(2));
                        }
                    }else{
                        ErrSem=true;
                        ErrSemantico.add("Se trato de declarar la variable: "+ArregloAyudante.get(1)+" en la lnea: "+i
                                +" con un valor de: "+ArregloAyudante.get(2)+" la cual ya esta declarada en la linea: "+Variables.get(ArregloAyudante.get(1)).getLinea()+" con el valor: "+Variables.get(ArregloAyudante.get(1)).getValor());
                    }
                    //Tamaño 2 = TIPO + VARIABLE
                }
                if(ArregloAyudante.size()==2){
                    //Si la variable no existe procedemos a a agregarla
                    if(!Variables.containsKey(ArregloAyudante.get(1))) {
                        tkn.setTipo(ArregloAyudante.get(0));
                        tkn.setToken(ArregloAyudante.get(1));
                        tkn.setValor("null");
                        tkn.setLinea(String.valueOf(i));
                        Variables.put(ArregloAyudante.get(1), tkn);
                    }else{
                        //Caso contrario mensaje de que ya ests declarada previemante
                        ErrSem=true;
                        ErrSemantico.add("Se trato de declarar nuevamente la variable: "+ArregloAyudante.get(1)+" en la lnea: "+i+" la cual ya esta declarada en la linea: "+Variables.get(ArregloAyudante.get(1)).getLinea());
                    }
                    //Tamaño 4 = TIPO + VAIRABLE + VALOR CON SIGNO NEGATIVO (int x , -45.5)
                }
                else if(ArregloAyudante.size()==4){
                    String valor = ArregloAyudante.get(2) + ArregloAyudante.get(3); //Tomamos el valor convertimos a negativo agregando su signo al string
                    //Si la variable no esta previamente declarada procedemos a agregarla
                    if(!Variables.containsKey(ArregloAyudante.get(1))) {
                        // System.out.println("Tamaño arreglo = 4");
                        if (Pattern.matches(regex, valor)) { //Si el patron de la variable da match con su valor
                            tkn.setTipo(ArregloAyudante.get(0));
                            tkn.setToken(ArregloAyudante.get(1));
                            //System.out.println("Valor: "+valor);
                            tkn.setValor(valor);
                            tkn.setLinea(String.valueOf(i));
                            Variables.put(ArregloAyudante.get(1), tkn);
                        } else {//caso contrario hay un error semantico y se agrega
                            ErrSem = true;
                            ErrSemantico.add("La variable: ' " + ArregloAyudante.get(1) + "' de tipo: ' " + ayudante2 + " ' no es compatible con el valor: ' " + valor);
                        }
                    }else{//caso contrario hay un error semantico y se agrega
                        ErrSem = true;
                        ErrSemantico.add("Se trato de declarar la variable: "+ArregloAyudante.get(1)+" en la lnea: "+i
                                +" con un valor de: "+valor+" la cual ya esta declarada en la linea: "+Variables.get(ArregloAyudante.get(1)).getLinea()+" con el valor: "+Variables.get(ArregloAyudante.get(1)).getValor());
                    }
                }

            }//indice = 4  x=5 se debe confirmar que exista la variable
            //Tenemos que ver que la variable esta previamente inicializada para agregar un nuevo valor o inicializarla con cierto valor
            if(indice==4){
                String variable,valorNuevo;
                ArrayList<String> ArregloAyudante = new ArrayList<>();//Este arraylist contendra los tokens encontrados
                StringTokenizer stkn=new StringTokenizer(texto.get(i)," ={[()]}+-/*;\n\t\r",true);
                //Si entró aquí quiere decir que encontro un cambio de valor de variable o inicializacion tenemos
                //que confirmar que dicha variable existe antes de hacer algun cambio
                //y nuestra sintaxis debe ser x = 0;
                while(stkn.hasMoreTokens()){
                    String ayudante=stkn.nextToken();
                    //En el proximo if vamos a ver si es un espacio vacio, un = o un ;
                    //de ser esto positivo se pasa al siguiente token;
                    if(ayudante.trim().isEmpty()||ayudante.equals("=")||ayudante.equals(";")){
                        continue;
                    }
                    //Naturalmente se deberian de agregar siempre y solamente 2 valores , la variable y el nuevo valor
                    //System.out.println(texto.get(i)+" num linea "+i);
                    //System.out.println(ayudante);
                    ArregloAyudante.add(ayudante);
                }
                //System.out.println(ArregloAyudante.get(0));
                variable=ArregloAyudante.get(0);
                if(Variables.containsKey(variable)){//Si la variable existe podemos agregar su valor
                    String regex="";// inciamos auxiliar para contener la expresion regular de cada tipo
                    String ayudante2= Variables.get(variable).getTipo();//sacamos el tipo exacto del token
                   // System.out.println("Se contiene la variable"+ayudante2);
                    if(ayudante2.equals("int")){  regex=Tipo.ENTERO.patron; }// el auxiliar se hace el de la expresion regular
                    if(ayudante2.equals("double")){  regex=Tipo.DOUBLE.patron;}
                    if(ayudante2.equals("float")){  regex=Tipo.DOUBLE.patron;}
                    if(ayudante2.equals("boolean")){  regex=Tipo.BOOLEAN.patron; }
                    //Aqui ya tenemos la expresion regular dependiendo el tipo de la variable y tenemos que ver si el valor es compatible dependiendo el tipo
                    if(Pattern.matches(regex,ArregloAyudante.get(1))) {//Si el patron da match con su valor procedemos a agragarlo a la tabla
                            Variables.get(variable).setValor(ArregloAyudante.get(1));
                    }else{//caso contrario da error semanitco y lo agregamos
                        ErrSem=true;
                        ErrSemantico.add("Se trato de inicializar la variable: ' "+variable+" ' de tipo: ' " +Variables.get(variable).getTipo()+
                                " ' con un valor: ' "+ArregloAyudante.get(1)+" ' y no son compatibles");
                    }
                }else{//Caso contrario da error semantico y lo agregamos
                    //System.out.println("Se encontro un error semantico");
                    ErrSem=true;
                    ErrSemantico.add("Se trató de inicializar o agregar un nuevo valor a la variable: "+variable+
                            " la cual no ha sido declarada\tLinea: "+i);

                }
            }
            //Si el indicie es = a 6 quiere decir que se encontro una expresion
            if(indice==6){

            }
        }

       // System.out.println("Sintaxis: "+ProgramaSintaxis);
        RecorreVariables();
    }
    //Metodo para cargar el hashmap de sintaxis y asignar el valor de cada tipo de sintaxis posible, clase Sintaxis para ver el valor correspondiente
    private void CargaTablaDeSintaxis() {
        int contador=0;
        //Se recorre cada tipo y se manda a un hashmap el valor de tipo de sintaxis y el nombre del tipo de sintaxis
        for(Sintaxis v : Sintaxis.values()){
            tipoSintactico.put(contador,v.toString());
            contador++;
        }
        //RecorreHM(tipoSintactico,"Valor","Tipo");
    }
    private void RecorreVariables(){
        System.out.println();
        Variables.forEach((k,v) -> System.out.println("YVariable: "+k+"  Variable: "+v.getToken()+"  Tipo: "+v.getTipo()
                +"  Valor: "+v.getValor()+"  Linea: "+v.getLinea()));
        System.out.println();
    }
    private void ChequeoCompatibilidadSemantica(){
        //Para realizar el chequeo de compatibilidad de valores y tipo es necesario checar si existen variables en la tabla de variables
        if(!Variables.isEmpty()){
            //Recorreremos todos los tokens de la tabla de tokens para ver si dicho token es uno de los que estan en la tabla de variables
            for(int i=0;i<Tokens.size();i++){
                String tipoToken=Tokens.get(i).getTipo();
                String llaveExistencia=Tokens.get(i).getToken();
                //Si el tipo token a tratar es de tipo identificador procedemos a comparar y observar si existe en dicha tabla
                if(tipoToken.equals(Tipo.IDENTIFICADORES.toString())){
                    //System.out.println("Se encontro un identificador: "+tipoToken);
                    //Si el identificador tratado existe en la tabla de variables entra
                    if(Variables.containsKey(llaveExistencia)){
                       // System.out.println("Se econtro la llave: "+llaveExistencia);
                        Token tkn=Variables.get(llaveExistencia);
                        //HACER EN CASE---
                        //Aqui se compara que el tipo sea int, booleano , float, double, o si el valor es nulo no hay necesidad de hacer comparacion
                        //En caso de que no entre en ninguno, se agregara un error a la tabla de errores semanticos
                        if(tkn.getTipo().equals(("int"))){
                            if(Pattern.matches(Tipo.ENTERO.patron,tkn.getValor())){
                                System.out.println("Se encontro un valor entero que da match: "+tkn.getToken()+" Tipo: "+tkn.getTipo()+" Valor: "+tkn.getValor());
                                continue;
                            }
                        }
                        if(tkn.getTipo().equals("boolean")){
                            if(Pattern.matches(Tipo.BOOLEAN.patron,tkn.getValor())){
                                System.out.println("Se encontro un valor boolean que da match: "+tkn.getToken()+" Tipo: "+tkn.getTipo()+" Valor: "+tkn.getValor());
                                continue;
                            }
                        }
                        if(tkn.getTipo().equals("double")){
                            if(Pattern.matches(Tipo.DOUBLE.patron,tkn.getValor())){
                                System.out.println("Se encontro un valor double que da match: "+tkn.getToken()+" Tipo: "+tkn.getTipo()+" Valor: "+tkn.getValor());
                                continue;
                            }
                        }
                        if(tkn.getTipo().equals("float")){
                            if(Pattern.matches(Tipo.DOUBLE.patron,tkn.getValor())){
                                System.out.println("Se encontro un valor float que da match: "+tkn.getToken()+" Tipo: "+tkn.getTipo()+" Valor: "+tkn.getValor());
                                continue;
                            }
                        }
                        if(tkn.getValor().equals("null")){
                            System.out.println("Se encontro un no inicializado: "+tkn.getToken()+" Tipo: "+tkn.getTipo()+" Valor: "+tkn.getValor());
                            continue;
                        }
                        else{
                            System.out.println("Se encontro un valor que NO da match: "+tkn.getToken()+" Tipo: "+tkn.getTipo()+" Valor: "+tkn.getValor());
                        }


                    }
                }
            }

        }
        RecorreVariables();

    }
    //Metodo para recorrer el hashMap, recibe un hashmap, el nombre que va a tener la llave y el nombre que va a tener el valor
    private void RecorreHM(HashMap HM,String llave,String valor) {
        HM.forEach((k,v) -> System.out.println(llave+": "+k+" -> "+valor+" : "+v));
    }
    //Metodo para leer cada linea del programa prueba
    public boolean Leer(String Ruta) throws java.io.FileNotFoundException, IOException {
        String Texto;
        int NumLinea=0;
        //Se crea la lecutra del fichero y se agrega al buffer para tratar linea por linea
        try{
            java.io.FileReader f= new java.io.FileReader(Ruta);
            java.io.BufferedReader b=new java.io.BufferedReader(f);
            while((Texto = b.readLine())!=null){
                ArchivoAbierto=true;
                //Como es un hashmap la llave la tomamos como el numero de linea por eso se inicia en 1, no hay problema si comienza en 0
                NumLinea++;
                texto.put(NumLinea,Texto);
            }
            b.close();
        }catch (Exception e){
            System.out.println("No se pudo abrir el archivo:\nError: "+e.toString());
        }
        return ArchivoAbierto;
    }
    public static void main(String []args) throws IOException {
        Analizador ana=new Analizador();
    }
}
