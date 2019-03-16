package GUI;

import GUI.Recursos.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class Controller implements Initializable {

    private ArrayList<Token> Tokens;
    private boolean Lex = false, ErrLex = false, Sin = false, ErrSin = false, Sem = false, ErrSem = false, ArchivoAbierto = false;
    private HashMap<Integer, String> texto, tipoSintactico;
    private ArrayList<Token> TokensNoAceptados, VarsTkns;
    private ArrayList<Expresion> Expresiones;
    private ArrayList<String> ErroresSintaxis, ErrSemantico, Warnings;
    private ObservableList<Token> t;
    private ObservableList<Token> t2;
    private String Codigo = "", ProgramaSintaxis = "";
    @FXML
    private TableView<Token> table, table2;
    @FXML
    private TableColumn tkn, tpo, vlr, lna, cna;
    @FXML
    private TableColumn tkn2, tpo2, vlr2, lna2;
    @FXML
    private TextArea txtCDG;
    @FXML
    private TextArea txtErr;
    @FXML
    private AnchorPane Ventana;
    private HashMap<String, Token> Variables;
    private boolean Warns = false;
    private double UbicacionY, UbicacionX;

    //Metodo para salir del programa dando click a la x
    public void onExitButtonClicked(MouseEvent mouseEvent) {
        Platform.exit();
        System.exit(0);
    }

    //Metodo para dar reinicio al programa
    public void Restart(MouseEvent mouseEvent) throws IOException {
        Stage Actualstage = (Stage) Ventana.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        Actualstage.close();
        Stage NewStage = new Stage();
        root.setOnMousePressed(event -> {
            UbicacionY = event.getSceneY();
            UbicacionX = event.getSceneX();
        });
        root.setOnMouseDragged(event -> {
            NewStage.setX(event.getScreenX() - UbicacionX);
            NewStage.setY(event.getScreenY() - UbicacionY);
        });
        NewStage.setScene(new Scene(root, 975, 495));
        NewStage.setResizable(false);
        NewStage.initStyle(StageStyle.TRANSPARENT);
        NewStage.show();
    }

    public void AnalizadorLexico() {
        Tokens = new ArrayList<>();
        TokensNoAceptados = new ArrayList<>();
        Lex = true;
        Sin = false;
        ErrSin = false;//Se reinician las banderas en caso de que se vuelva a hacer el analisis lexico
        Sem = false;
        ErrSem = false;

        String palabra;
        //Iteracion for que recorre las lineas del archivo txt
        //Como se maneja un hashmap se recorren las lineas apartir del 1
        //ir al metodo Leer

        for (int i = 1; i <= texto.size(); i++) {
            String lineaActual = texto.get(i);
            //Convertimos la linea en tokens con el tokenizer
            StringTokenizer stkn = new StringTokenizer(lineaActual, " ={[()]}+-/*:;\t\r", true);
            //Creamos una variable para contar la columna tratada
            int colm = 0;
            String aux1;
            Boolean posExpresion = false;//Verificamos si puede ser una posible expresion
            //Mientras existan mas tokens tomamos una variable y le asignamos la palabra(token)
            while (stkn.hasMoreTokens()) {
                colm++;
                palabra = stkn.nextToken();
                boolean Matched = false; //Manejador de errores, si es = true se aceptaron los tokens
                //Si el token detectado es un token en blanco se consume y se va al siguiente token
                if (palabra.trim().isEmpty()) {
                    colm--;
                    continue;
                }
                if (palabra.equals(":")) {
                    aux1 = palabra; //Se asigna el : a palabra
                    palabra = stkn.nextToken();//Asignamos el nuevo token a palabra
                    if (palabra.equals("=")) {//Si palabra es igual a =
                        palabra = aux1 + palabra; //ahora la palabra es igual :=
                    }//Caso contrario solamente pasa el igual y se ignora el : ya que no deberia usarse en la gramatica
                }
                for (Tipo TokenTipo : Tipo.values()) {
                    //Se establece un patron REGEX para comparar
                    Pattern patron = Pattern.compile(TokenTipo.patron);
                    //Se establece lo que queremos comparar ante el patron REGEX
                    Matcher matcher = patron.matcher(palabra);
                    if (matcher.matches()) {
                        //Aqui entraria si hace match y se crea un objeto para guardar el token con su valor
                        Token tk = new Token();
                        tk.setToken(palabra);
                        tk.setTipo(TokenTipo.toString());
                        tk.setLinea(String.valueOf(i));
                        tk.setColumna(String.valueOf(colm));
                        Tokens.add(tk);
                        //System.out.println(tk.getToken()+" "+tk.getTipo());
                        //Matched se convierte en veradero para saltar a la proxima iteracion
                        Matched = true;
                        break;
                    }
                }
                //Caso contrario no hizo match entonces lo agregamos a la lista de errores
                if (!Matched) {
                    System.out.println("Se encontró un error lexico:");
                    //Establecemos que hay errores lexicos
                    ErrLex = true;
                    //Agregamos a un arreglo los errores lexicos encontrados
                    Token tkna = new Token();
                    tkna.setToken(palabra);
                    tkna.setLinea(String.valueOf(i));
                    tkna.setColumna(String.valueOf(colm));
                    TokensNoAceptados.add(tkna);
                }
            }
        }
        //Se agregan todos los elementos del arraylist a una tabla
        t = FXCollections.observableArrayList(Tokens);
        //Se recorren los elementos aceptados en la gramatica y se cargan en la tabla
        CargaLexico();
        /*System.out.println("Los siguientes elementos fueron encontrados en la gramatica");
        Tokens.iterator().forEachRemaining((v)->System.out.println("Token: "+v.getToken()+"\tTipo: "+v.getTipo()+"\tValor: "+v.getValor()
                +"\tLinea: "+v.getLinea()+"\tColumna: "+v.getColumna()));
        System.out.println("\nFin elementos encontrados\n");*/
        ChequeoErrores();
    }

    public void AnalizadorSintactico() {
        ProgramaSintaxis = "";
        if (Lex) {//Verificamos que se haya realizado el analisis lexico
            if (!ErrLex) {//Verificamos que se haya realizado el analisis sintactico
                ErroresSintaxis = new ArrayList<>();
                Expresiones = new ArrayList<>();
                Sin = true;
                CargaTablaDeSintaxis();
                //Se inicializa la variable que contendra la sintaxis general del programa prueba para compararlo con una expresion regular
                //Se comienza a recorrer linea por linea
                for (int i = 1; i <= texto.size(); i++) {
                    String lineaActual = texto.get(i);
                    //Se recorre los tipos de sintaxis que puede haber en cada linea
                    boolean Matched = false;
                    for (Sintaxis sin : Sintaxis.values()) {
                        Pattern patron = Pattern.compile(sin.patronSin);
                        Matcher matcher = patron.matcher(lineaActual);
                        //Si la linea actual hace match con algun patron de expresion regular entra en el if
                        if (matcher.matches()) {
                            //System.out.print("Linea "+i+": "+sin.toString());
                            Matched = true;
                            //Se recorren los tipos de sintaxis posibles ejemplo DECLARACION_CLASE la cual su valor es 0
                            for (int j = 0; j < tipoSintactico.size(); j++) {
                                //Si el nombre del patron en curso es igual a alguno del recorrido en tipo sintaxis entra en el if
                                if (sin.toString().equals(tipoSintactico.get(j))) {
                                    //Si el identificador es 6 se va a validar la forma de una expresion
                                    if (j == 6) {//Se va a validar la forma de la expresion
                                        System.out.println("Se encontro expresion: " + lineaActual);
                                        String Expresion = "";
                                        String Asigna = "";
                                        boolean salto1tkn = false;//bandera para indicar que se salto el primer token (identificador)
                                        //Se separa la expresion en tokens
                                        StringTokenizer stkn = new StringTokenizer(lineaActual, " :={[()]}+-/*;\t\r", true);
                                        //Naturalmente una expresion es el identificador seguido de un := posteriormente la expresion a asignar
                                        while (stkn.hasMoreTokens()) {
                                            String tknActual = stkn.nextToken();
                                            if (tknActual.trim().isEmpty() || tknActual.equals(";") || tknActual.equals(":") || tknActual.equals("=")) {
                                                continue;
                                            } else if (!salto1tkn) {//Se salta el primer token que seria el identificador
                                                Asigna = tknActual;
                                                salto1tkn = true;
                                                continue;
                                            } else {//Se salta, en caso contrario se agrega al string que contiene la expresion
                                                Expresion = Expresion + tknActual;
                                            }
                                        }
                                        //Al terminar ese ciclo tendriamos una expresion y procedemos a evaluar
                                        //System.out.println(expresion);
                                        AnalizadorExpresion ana = new AnalizadorExpresion();
                                        if (!ana.Analisis(Expresion)) { //Si el analisis de expresion da falso , quiere decir que la expresion no tiene buena sintaxis
                                            //System.out.println("Entro: "+lineaActual);
                                            ErroresSintaxis.add("\nSe encontro un error sintactico en la expresion: " + Expresion + " que se encuentra en la linea: " + i + "\n");
                                            ErrSin = true;
                                        } else {//Caso contrario se agrega en un objeto a un arreglo de expresiones
                                            //System.out.println("Expresion");

                                            Expresion exp = new Expresion();//Se crea una nuevo objeto expresion
                                            exp.setAsigna(Asigna);//Variable que se le quiere asignar dicha expresion
                                            exp.setExpresion(ana.aggEspacio(Expresion));//Expresion con espacios
                                            exp.setLinea(i);//Numero de linea
                                            exp.setPostorder(ana.Conversion(Expresion));
                                            System.out.println(ana.aggEspacio(Expresion)+" "+ana.Conversion(Expresion));
                                            Expresiones.add(exp);
                                        }
                                    }
                                    //System.out.println(" ==> "+j);
                                    //Se establece una sintaxis de programa por ejemplo si fuese DECLARACION_CLASE la cual su valor es 0 , repetida 3 veces
                                    //en las primeras 3 lineas, se forma un string que seria " 000 "
                                    //Si solo fuese en las primeras 2 se formaria un string que seria " 00 "
                                    //Si solo fuese en las primeras 2 y posteriormente en la 3er linea un LBRA( { ) el cual su valor es 2
                                    //se formaria un string que seria " 002 " y esa seria la logica, posteriormente se compara el string obtenido
                                    //con otra expresion regular para ver si la sintaxis de el programa completo es correcta
                                    ProgramaSintaxis = ProgramaSintaxis +" "+ j;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    //Como no hace match entra aqui y el error se agrega a un arreglo de errores de sintaxis
                    if (!Matched) {
                        System.out.println("Se encontro un error sintactico");
                        ErroresSintaxis.add("ERROR ==>   " + lineaActual + "    <== Verifique la sintaxis en la linea: # " + i + "\n");
                        ErrSin = true;
                    }
                }
                System.out.println();
                //Verifica si el patron obtenido hace match con el de la sintaxis general y si no existen errores sintacticos por linea
                if (!ErrSin) {
                    if (!Pattern.matches(Sintaxis.SINTAXIS_GENERAL.patronSin, ProgramaSintaxis)) {
                        //System.out.printf("General");
                        String SintaxisToString = "<";
                        ErrSin = true;//Error sintactico se hace true ya que se encontro uno
                        ErroresSintaxis.add("Error ==> El programa puede tener una sintaxis correcta pero tiene una mala estructura," +
                                " organice e intente de nuevo <== ERROR" + "\n");
                        //Este arreglo sirve para recorrer el codigo sintactico y obetener exactamente la estructura exacta del programa, asi facilitar
                        //La manera de encontrar el error
                        for (int kk = 0; kk < ProgramaSintaxis.length(); kk++) {
                            String llave = String.valueOf(ProgramaSintaxis.charAt(kk));
                            SintaxisToString = SintaxisToString + "<" + tipoSintactico.get(Integer.parseInt(llave)) + ">\n";
                        }
                        ErroresSintaxis.add("La sintaxis tiene la siguiente forma:\n" + SintaxisToString + ">");
                    }
                }
                txtErr.setText("");
            } else {//Se encontro un error lexico, no se puede continuar con el analisis sintactico
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Se encontraron errores lexicos, proceda a corregir", ButtonType.OK);
                alert.showAndWait();
            }
        } else {//Se encontro que no se hizo el analisis previo
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Debe realizar el analisis lexico", ButtonType.OK);
            alert.show();
            try {
                sleep(2000);
                alert.close();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Expresiones.forEach(v -> System.out.println(v.getAsigna() + v.getExpresion())); //<<--Se imprime la variable y la expresion que se le quiere asignar
        ChequeoErrores();//Se checan los errores
    }

    public void AnalizadorSemantico() {
        if (Lex & Sin) { //Verificamos que se hayan realizado los analisis previos
            if (!ErrSin) {
                ErrSemantico = new ArrayList<>();
                Variables = new HashMap<>();
                Sem = true;
                //El largo de ProgramaSintaxis siempre va a ser igual al numero de lineas en el
                //Lo recorro desde 1 porque las lineas del texto comienzan desde la 1
                StringTokenizer tokenizer=new StringTokenizer(ProgramaSintaxis," ");
                int i=1;//Se comienza la linea en 1 porque es la primer linea del texto
                while(tokenizer.hasMoreTokens()){
                    //Aqui utilizamos i-1 para comenzar con el 0 porque el largo de la palabra generada comienza desde 0
                    int indice = Integer.parseInt(tokenizer.nextToken());
                    //Si el indice es igual a VARDEC == 3 o VARDECINIT == 5 entrara al if dado que no necesita verificar su existencia previa
                    //System.out.println("Linea: "+i);
                    if (indice == 3 || indice == 5) {
                        //System.out.println("Indice: "+indice+" Linea: "+i);
                        Token tkn = new Token();
                        ArrayList<String> ArregloAyudante = new ArrayList<>();
                        //System.out.println(texto.get(i));
                        //Tokenizamos la linea que naturalmente tendria los dos tipos de sintaxis previamente mencionados
                        StringTokenizer stkn = new StringTokenizer(texto.get(i), " ={[()]}+-/*;\n\t\r", true);
                        //De acuerdo a la sintaxis el primer valor es el tipo de variable
                        //El segundo debe ser el identificador
                        //El tercero debe ser el simbolo = que puede depender su existencia en caso de que se inicialice la variable o solo se declare
                        //El cuarto el valor de la variable que puede depender si solo se declara la variable o tambien se inicializa
                        while (stkn.hasMoreTokens()) {//Recorremos los tokens dentro de la linea
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
                        String regex = "";//Iniciamos un auxiliar para contener la expresion regular
                        String ayudante2 = ArregloAyudante.get(0);//sacamos el tipo exacto del token

                        regex = getTipo(regex, ayudante2);//comparo el tipo con la expresion regular para obtener el codigo de la expresion regular

                        //Vamos a ver que tipo de declaracion es para agregar a la tabla de variables
                        //Tamaño 3 = TIPO + VARIABLE/TOKEN + VALOR
                        if (ArregloAyudante.size() == 3) {
                            if (!Variables.containsKey(ArregloAyudante.get(1))) {
                                if (Pattern.matches(regex, ArregloAyudante.get(2))) { //si el valor de la variable hace match con el tipo se agrega a la tabla
                                    tkn.setTipo(ArregloAyudante.get(0));
                                    tkn.setToken(ArregloAyudante.get(1));
                                    tkn.setValor(ArregloAyudante.get(2));
                                    tkn.setLinea(String.valueOf(i));
                                    Variables.put(ArregloAyudante.get(1), tkn);
                                } else {//caso contrario hay un error semantico
                                    ErrSem = true;
                                    ErrSemantico.add("\nLa variable: ' " + ArregloAyudante.get(1) + "' de tipo: ' " + ayudante2 + " ' no es compatible con el valor: ' " + ArregloAyudante.get(2) + "\n");
                                }
                            } else {
                                ErrSem = true;
                                ErrSemantico.add("\nSe trato de declarar la variable: " + ArregloAyudante.get(1) + " en la lnea: " + i
                                        + " con un valor de: " + ArregloAyudante.get(2) + " la cual ya esta declarada en la linea: " + Variables.get(ArregloAyudante.get(1)).getLinea() + " con el valor: " + Variables.get(ArregloAyudante.get(1)).getValor() + "\n");
                            }
                            //Tamaño 2 = TIPO + VARIABLE
                        }
                        if (ArregloAyudante.size() == 2) {
                            //Si la variable no existe procedemos a a agregarla
                            if (!Variables.containsKey(ArregloAyudante.get(1))) {
                                tkn.setTipo(ArregloAyudante.get(0));
                                tkn.setToken(ArregloAyudante.get(1));
                                tkn.setValor("null");
                                tkn.setLinea(String.valueOf(i));
                                Variables.put(ArregloAyudante.get(1), tkn);
                            } else {
                                //Caso contrario mensaje de que ya ests declarada previemante
                                ErrSem = true;
                                ErrSemantico.add("\nSe trato de declarar nuevamente la variable: " + ArregloAyudante.get(1) + " en la lnea: " + i + " la cual ya esta declarada en la linea: " + Variables.get(ArregloAyudante.get(1)).getLinea());
                            }
                            //Tamaño 4 = TIPO + VAIRABLE + VALOR CON SIGNO NEGATIVO (int x , -45.5)
                        } else if (ArregloAyudante.size() == 4) {
                            String valor = ArregloAyudante.get(2) + ArregloAyudante.get(3); //Tomamos el valor convertimos a negativo agregando su signo al string
                            //Si la variable no esta previamente declarada procedemos a agregarla
                            if (!Variables.containsKey(ArregloAyudante.get(1))) {
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
                                    ErrSemantico.add("\nLa variable: ' " + ArregloAyudante.get(1) + "' de tipo: ' " + ayudante2 + " ' no es compatible con el valor: ' " + valor);
                                }
                            } else {//caso contrario hay un error semantico y se agrega
                                ErrSem = true;
                                ErrSemantico.add("\nSe trato de declarar la variable: " + ArregloAyudante.get(1) + " en la lnea: " + i
                                        + " con un valor de: " + valor + " la cual ya esta declarada en la linea: " + Variables.get(ArregloAyudante.get(1)).getLinea() + " con el valor: " + Variables.get(ArregloAyudante.get(1)).getValor());
                            }
                        }

                    }//indice = 4  x=5 se debe confirmar que exista la variable
                    //Tenemos que ver que la variable esta previamente inicializada para agregar un nuevo valor o inicializarla con cierto valor
                    if (indice == 4) {
                        String variable, valorNuevo;
                        ArrayList<String> ArregloAyudante = new ArrayList<>();//Este arraylist contendra los tokens encontrados
                        StringTokenizer stkn = new StringTokenizer(texto.get(i), " ={[()]}+-/*;\n\t\r", true);
                        //Si entró aquí quiere decir que encontro un cambio de valor de variable o inicializacion tenemos
                        //que confirmar que dicha variable existe antes de hacer algun cambio
                        //y nuestra sintaxis debe ser x = 0;
                        while (stkn.hasMoreTokens()) {
                            String ayudante = stkn.nextToken();
                            //En el proximo if vamos a ver si es un espacio vacio, un = o un ;
                            //de ser esto positivo se pasa al siguiente token;
                            if (ayudante.trim().isEmpty() || ayudante.equals("=") || ayudante.equals(";")) {
                                continue;
                            }
                            //Naturalmente se deberian de agregar siempre y solamente 2 valores , la variable y el nuevo valor
                            //System.out.println(texto.get(i)+" num linea "+i);
                            //System.out.println(ayudante);
                            ArregloAyudante.add(ayudante);
                        }
                        //System.out.println(ArregloAyudante.get(0));
                        variable = ArregloAyudante.get(0);
                        if (Variables.containsKey(variable)) {//Si la variable existe podemos agregar su valor
                            String regex = "";// inciamos auxiliar para contener la expresion regular de cada tipo
                            String ayudante2 = Variables.get(variable).getTipo();//sacamos el tipo exacto del token
                            // System.out.println("Se contiene la variable"+ayudante2);
                            regex = getTipo(regex, ayudante2);
                            //Aqui ya tenemos la expresion regular dependiendo el tipo de la variable y tenemos que ver si el valor es compatible dependiendo el tipo
                            if (Pattern.matches(regex, ArregloAyudante.get(1))) {//Si el patron da match con su valor procedemos a agragarlo a la tabla
                                Variables.get(variable).setValor(ArregloAyudante.get(1));
                            } else {//caso contrario da error semanitco y lo agregamos
                                ErrSem = true;
                                ErrSemantico.add("\nSe trato de inicializar la variable: ' " + variable + " ' de tipo: ' " + Variables.get(variable).getTipo() +
                                        " ' con un valor: ' " + ArregloAyudante.get(1) + " ' y no son compatibles");
                            }
                        } else {//Caso contrario da error semantico y lo agregamos
                            //System.out.println("Se encontro un error semantico");
                            ErrSem = true;
                            ErrSemantico.add("\nSe trató de inicializar o agregar un nuevo valor a la variable: " + variable +
                                    " la cual no ha sido declarada\tLinea: " + i);

                        }
                    }
                    //Si el indicie es = a 6 quiere decir que se encontro una expresion
                    if (indice == 6) {
                        //                       x=x + x-x;
                        //Analizador Sintactico
                    }
                    VarsTkns = new ArrayList<>();
                    Variables.forEach((k, v) -> VarsTkns.add(v));
                    t2 = FXCollections.observableArrayList(VarsTkns);
                    CargaSemantico();//SE CARGAN LAS VARIABLES DEL ANALISIS A LA INTERGAZ
                    i++;
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Se encontraron errores sintacticos, proceda a corregir", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            if (!Expresiones.isEmpty()) {//Checamos si hay elementos en la tabla de expresiones
                Expresiones.forEach(expresion -> {
                    //Recorremos todas las expresiones guardadas durante el analisis semantico
                    if (Variables.containsKey(expresion.getAsigna())) {//Si la variable a la que se le quiere asignar la expresion existe entra
                        String tipo = Variables.get(expresion.getAsigna()).getTipo();//Aqui se guardara el tipo de la variable para asginar
                        StringTokenizer stkn = new StringTokenizer(expresion.getExpresion(), " *+-/()", true);
                        while (stkn.hasMoreTokens()) {
                            String token = stkn.nextToken();
                            if (token.trim().isEmpty()) {
                                continue;
                            }
                            if (Pattern.matches(Tipo.IDENTIFICADORES.patron, token)) {//Se procede a trabajar solo con los identificadores de la expresion
                                if (Variables.containsKey(token)) {//Si existe la variable
                                    if (Variables.get(token).getTipo().equals(tipo)) {//Las variables son del mismo tipo
                                        if (!Variables.get(token).getValor().equals("null")) {//La variable tratada si tiene valor
                                            //Aun no se trabaja con expresiones en forma , solo sintaxis y contexto

                                        } else {//Caso contrario error semantico sin valor
                                            ErrSemantico.add("\nLa variable: " + token + " en la expresion: " + expresion.getAsigna() + ":=" + expresion.getExpresion() + " de tipo: " + Variables.get(token).getTipo() + " tiene un valor nulo");
                                            ErrSem = true;
                                        }
                                    } else {//Caso contrario Error semantico tipos diferentes
                                        ErrSemantico.add("\nLa variable: " + token + " en la expresion: " + expresion.getAsigna() + ":=" + expresion.getExpresion() + " de tipo: " + Variables.get(token).getTipo() + " no es compatible con " +
                                                "la variable principal: " + expresion.getAsigna() + " la cual es de tipo: " + tipo);
                                        ErrSem = true;
                                    }
                                } else {//Caso contrario Error semantico no existe variable
                                    ErrSemantico.add("\nLa variable: " + token + " en la expresion: " + expresion.getAsigna() + ":=" + expresion.getExpresion() + " no ha sido declarada");
                                    ErrSem = true;
                                }
                            }
                        }
                    } else {//caso contrario error semantico no funciona
                        ErrSemantico.add("\nLa variable: " + expresion.getAsigna() + " a la que se le quiere asignar el resultado de la expresion: " +
                                expresion.getExpresion() + " no se encuentra declarada\n");
                        ErrSem = true;
                    }
                });

            }
        } else {//Se encontro que no se han hecho los analisi previos , muesta el error
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Debe realizar los analisis previos", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        RecorreVariables();//SE recorren variables por consola , no necesario
        ChequeoVariablesSinInicializar(); //Se checan las variables cuyo valor es nulo
        ActualizaValoresTablaTokens();//Actualiza la tabla de tokens con los nuevos valores encontrados
        ChequeoErrores();//Checa los errores encontrados y los recarga;

        // System.out.println("Sintaxis: "+ProgramaSintaxis);
    }

    //Metodo que dependiendo el tipo de una variable regresa un expresion regular
    private String getTipo(String regex, String ayudante2) {
        if (ayudante2.equals("int")) {
            regex = Tipo.ENTERO.patron;
        }// el auxiliar se hace el de la expresion regular
        if (ayudante2.equals("double")) {
            regex = Tipo.DOUBLE.patron;
        }
        if (ayudante2.equals("float")) {
            regex = Tipo.DOUBLE.patron;
        }
        if (ayudante2.equals("boolean")) {
            regex = Tipo.BOOLEAN.patron;
        }
        return regex;
    }

    //Metodo para actualizar la tabla de tokens con los nuevos valores
    private void ActualizaValoresTablaTokens() {
        Tokens.forEach(v -> {
            if (Variables.containsKey(v.getToken())) {
                v.setValor(Variables.get(v.getToken()).getValor());
            }
        });
        CargaLexico();//Metodo para recargar el grafico de la tabla
    }

    //Metodo para imprimir por consola todas las variables encontradas
    private void RecorreVariables() {
        if (!Variables.isEmpty()) {
            System.out.println();
            Variables.forEach((k, v) -> System.out.println("Variable: " + k + "  Variable: " + v.getToken() + "  Tipo: " + v.getTipo()
                    + "  Valor: " + v.getValor() + "  Linea: " + v.getLinea()));
            System.out.println();
        }
    }

    //Metodo para leer el texto
    private boolean Leer(String Ruta) throws IOException {
        String Texto;
        int NumLinea = 0;
        texto = new HashMap<>();
        //Se crea la lecutra del fichero y se agrega al buffer para tratar linea por linea
        try {
            java.io.FileReader f = new java.io.FileReader(Ruta);
            java.io.BufferedReader b = new java.io.BufferedReader(f);
            while ((Texto = b.readLine()) != null) {
                NumLinea++;
                ArchivoAbierto = true;
                Codigo = Codigo + NumLinea + "\t" + Texto + "\n";
                //Como es un hashmap la llave la tomamos como el numero de linea por eso se inicia en 1, no hay problema si comienza en 0
                texto.put(NumLinea, Texto);
            }
            b.close();
        } catch (Exception e) {
            System.out.println("No se pudo abrir el archivo:\nError: " + e.toString());
        }
        if (ArchivoAbierto) {
            txtCDG.setText(Codigo);
        }
        return ArchivoAbierto;
    }

    //Carga los diferentes tipos de sintaxis en un mapa id + nombre
    private void CargaTablaDeSintaxis() {
        /*0DECLARACION_CLASE,1DECLARACION_MAIN,2LBRA,3VARDEC
        4VARINIT,5VARDECINIT,6EXPRESSION,7PRINT
        8RBRA,9LNULL,10SINTAXIS_GENERAL*/
        tipoSintactico = new HashMap<>();
        int contador = 0;
        //Se recorre cada tipo y se manda a un hashmap el valor de tipo de sintaxis y el nombre del tipo de sintaxis
        for (Sintaxis v : Sintaxis.values()) {
            tipoSintactico.put(contador, v.toString());
            contador++;
        }
        tipoSintactico.forEach((v, k) -> System.out.println(v.toString() + k));
    }

    //Checa si existe algun error o advertencia en algun analisis
    private void ChequeoErrores() {
        String ayudante = "";
        if (ErrLex | ErrSin | ErrSem | Warns) {
            //Si se encontro algun tipo de error la ventana cambia de tamaño para mostrar la zona de errores
            Ventana.getScene().getWindow().setHeight(700);
            // Ventana.getScene().getWindow().
        }
        if (ErrLex) {
            //Se recorren los elementos no aceptados
            System.out.println("\nLos siguientes elementos no fueron encontrados en la gramatica\n");
            CargaErrores("<Analisis Lexico>\nLos siguientes elementos no fueron encontrados en la gramatica:\n");
            TokensNoAceptados.iterator().forEachRemaining((v) -> {
                CargaErrores("\nToken: " + v.getToken() + " \t\tLinea: " + v.getLinea() + "\t\tColumna: " + v.getColumna() + "\n");
            });
            CargaErrores("\nFin elementos no encontrados\n");
        }
        //Verifica si existen errores Sintacticos
        if (ErrSin) {
            //Se recorren los errores en sintaxis
            CargaErrores("\n\n<Analisis Sintactico>\nSe muestran errores Sintacticos\n");
            ErroresSintaxis.forEach(this::CargaErrores);
            CargaErrores("\nFin errores Sintacticos\n");
        }
        if (ErrSem) {
            CargaErrores("\n\n<Analisis Semanticos>\nSe muestran errores Semanticos\n");
            ErrSemantico.forEach(this::CargaErrores);
            CargaErrores("\nFin errores Semanticos");
        }
        if (Warns) {
            CargaErrores("\n\n<Warnings>\nSe muestran warnings\n");
            Warnings.forEach(this::CargaErrores);
            CargaErrores("\nFin Warnings\n");
        }

    }

    //Carga los errores en el cuadro de texto
    private void CargaErrores(String error) {
        txtErr.setText("" + txtErr.getText() + error);
    }

    //Va a recorrer todas las variables y verificar que tengan valores distintos a nulo
    private void ChequeoVariablesSinInicializar() {
        System.out.println("Entro al metodo");
        Warnings = new ArrayList<>();
        if (!Variables.isEmpty()) {
            Variables.forEach((v, k) -> {
                if (k.getValor().equals("null")) {
                    Warns = true;
                    Warnings.add("Warn: La variable: " + v + " no fue inicializada\t\t Linea--> #" + k.getLinea() + "\n");
                }
            });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Leer("src/GUI/Recursos/Prueba.txt");
        } catch (IOException e) {
            System.out.println("ERRORSILLO");
        }
    }

    //Metodos para cargar tablas graficas
    private void CargaLexico() {
        tkn.setCellValueFactory(new PropertyValueFactory<Token, String>("Token"));
        tpo.setCellValueFactory(new PropertyValueFactory<Token, String>("Tipo"));
        vlr.setCellValueFactory(new PropertyValueFactory<Token, String>("Valor"));
        lna.setCellValueFactory(new PropertyValueFactory<Token, String>("Linea"));
        cna.setCellValueFactory(new PropertyValueFactory<Token, String>("Columna"));
        table.getItems().setAll(t);
    }

    private void CargaSemantico() {
        tkn2.setCellValueFactory(new PropertyValueFactory<Token, String>("Token"));
        tpo2.setCellValueFactory(new PropertyValueFactory<Token, String>("Tipo"));
        vlr2.setCellValueFactory(new PropertyValueFactory<Token, String>("Valor"));
        lna2.setCellValueFactory(new PropertyValueFactory<Token, String>("Linea"));
        table2.getItems().setAll(t2);

    }
}
