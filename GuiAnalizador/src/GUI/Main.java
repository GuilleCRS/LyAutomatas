package GUI;

import GUI.Recursos.Token;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {
private double UbicacionY,UbicacionX;
private HashMap<Integer,String> texto;
private ArrayList<Token> Tokens,TokensNoAceptados;
private boolean ArchivoAbierto=false,Lex=false,ErrLex=false;
private ObservableList<Token> t=null;
    Parent root;
    Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        stage=primaryStage;
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                UbicacionY = event.getSceneY();
                UbicacionX=event.getSceneX();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX()-UbicacionX);
                primaryStage.setY(event.getScreenY()-UbicacionY);
            }
        });
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Compilador");
        primaryStage.setScene(new Scene(root, 975, 500));
        primaryStage.setResizable(false);
        primaryStage.show();


//        table.setItems(t);
  //      table.getColumns().addAll(tkn,tpo,vlr,lna,cna);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
