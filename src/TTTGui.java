import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.*;
import javafx.collections.*;
import java.io.*;
import java.net.*;
import javafx.application.Platform;

public class TTTGui extends Application
{
    static final int MINW = 750, MINH = 750, DEFW = 900, DEFH = 900, MAXW = 1000, MAXH = 1000; //Window size
    Stage stage;//Stage object
    Scene loadingSc, playSc;
    GridPane loadingGr = new GridPane(), playGr = new GridPane();
    GridPane[] grids = new GridPane[]{loadingGr,playGr};
    Button startx = new Button("Start (X)"), starto = new Button("Start (O)"), startr = new Button("Start (random)"), b00 = new Button(), b01 = new Button(), b02 = new Button(), b10 = new Button(),
            b11 = new Button(), b12 = new Button(), b20 = new Button(), b21 = new Button(), b22 = new Button(), mainmenu = new Button("Main Menu");
    Button[] buttons = new Button[]{startx, starto, startr, mainmenu};
    Button[][] gridButtons = new Button[][]{{b00, b01, b02},{b10, b11, b12},{b20, b21, b22}};
    Text msg = new Text();
    volatile boolean isTurn = false;
    public static void main(){
        launch();
    }
    @Override
    public void start(Stage primaryStage){
        stage = primaryStage;
        for(Button b : buttons){
            b.setOnAction(e-> buttonClick(e));
        }
        for(Button[] barr : gridButtons)
            for(Button b : barr)
            {
                b.setOnAction(e-> buttonClick(e));
            }
        for(GridPane grid : grids){
            //grid.setVgap(15);
            //grid.setHgap(15);
            grid.setAlignment(Pos.CENTER);
            grid.setPadding(new Insets(20,20,20,20));
            /*ColumnConstraints a = new ColumnConstraints();
            a.setPercentWidth(20);
            RowConstraints b = new RowConstraints();
            b.setPercentHeight(20);
            grid.getColumnConstraints().addAll(a,a,a,a,a);
            grid.getRowConstraints().addAll(b,b,b,b,b);*/
            //grid.setGridLinesVisible(true); //Debugging
        }
        //Loading scene
        loadingGr.add(startx, 1, 1);
        GridPane.setConstraints(startx, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        loadingGr.add(starto, 1, 2);
        GridPane.setConstraints(starto, 1, 2, 1, 1, HPos.CENTER, VPos.CENTER);
        loadingGr.add(startr, 1, 3);
        GridPane.setConstraints(startr, 1, 3, 1, 1, HPos.CENTER, VPos.CENTER);
        //
        //Call scene
        playGr.add(b00, 0, 0);
        GridPane.setConstraints(b00, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
        playGr.add(b01, 0, 1);
        GridPane.setConstraints(b01, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        playGr.add(b02, 0, 2);
        GridPane.setConstraints(b02, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
        playGr.add(b10, 1, 0);
        GridPane.setConstraints(b10, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
        playGr.add(b11, 1, 1);
        GridPane.setConstraints(b11, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        playGr.add(b12, 1, 2);
        GridPane.setConstraints(b12, 1, 2, 1, 1, HPos.CENTER, VPos.CENTER);
        playGr.add(b20, 2, 0);
        GridPane.setConstraints(b20, 2, 0, 1, 1, HPos.CENTER, VPos.CENTER);
        playGr.add(b21, 2, 1);
        GridPane.setConstraints(b21, 2, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        playGr.add(b22, 2, 2);
        GridPane.setConstraints(b22, 2, 2, 1, 1, HPos.CENTER, VPos.CENTER);
        playGr.add(msg, 2, 3);
        GridPane.setConstraints(msg, 2, 3, 1, 1, HPos.CENTER, VPos.CENTER);
        playGr.add(mainmenu, 2, 4);
        GridPane.setConstraints(mainmenu, 2, 4, 1, 1, HPos.CENTER, VPos.CENTER);
        //
        loadingSc = new Scene(loadingGr);
        playSc = new Scene(playGr);
        //
        //primaryStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.F11));
        primaryStage.setMinWidth(MINW);
        primaryStage.setMinHeight(MINH);
        primaryStage.setWidth(DEFH);
        primaryStage.setHeight(DEFW);
        primaryStage.setMaxWidth(MAXW);
        primaryStage.setMaxHeight(MAXH);
        primaryStage.setScene(loadingSc);
        primaryStage.setResizable(true);
        primaryStage.show();
    }
    public void buttonClick(ActionEvent e){
        if(e.getSource() == startx)
        {
            
        }
        else if(e.getSource() == starto)
        {
            
        }
        else if(e.getSource() == startr)
        {
            
        }
        else
            for(int x = 0; x < 3; ++x)
                for(int y = 0; y < 3; ++y)
                    if(e.getSource() == gridButtons[x][y])
                    {
                        
                    }
    }
    public void turnStart(boolean val){
        Platform.runLater(() -> {
            msg.setText(val ? "Your Turn!" : "Their Turn!");
        });
    }
    public void endGame(int winner)
    {
    
    }
}