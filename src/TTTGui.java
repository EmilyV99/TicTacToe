import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.*;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;

public class TTTGui extends Application
{
    static final int MINW = 300, MINH = 300, DEFW = 350, DEFH = 350, MAXW = 400, MAXH = 400, //Window size
            BTN_W = 60, BTN_H = BTN_W;
    Stage stage;//Stage object
    Scene loadingSc, playSc;
    GridPane loadingGr = new GridPane(), playGr = new GridPane();
    GridPane[] grids = new GridPane[]{loadingGr,playGr};
    Button startx = new Button("Start (X)"), starto = new Button("Start (O)"), startr = new Button("Start (random)"), b00 = new Button(), b01 = new Button(), b02 = new Button(), b10 = new Button(),
            b11 = new Button(), b12 = new Button(), b20 = new Button(), b21 = new Button(), b22 = new Button(), mainmenu = new Button("Main Menu");
    Button[] buttons = new Button[]{startx, starto, startr, mainmenu};
    Button[][] gridButtons = new Button[][]{{b00, b01, b02},{b10, b11, b12},{b20, b21, b22}};
    Text msg = new Text();
    ChoiceBox<String> diff = new ChoiceBox<>();
    volatile boolean isTurn = false;
    volatile TTTHandler handler;
    Background background = new Background(new BackgroundImage(new Image("BG1.png"),null,null,null,null));
    public static void main(String[] args){
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
                b.setMinSize(BTN_W, BTN_H);
                b.setMaxSize(BTN_W, BTN_H);
                b.setId("gridb");
            }
        for(GridPane grid : grids){
            grid.setAlignment(Pos.CENTER);
            grid.setPadding(new Insets(20,20,20,20));
        }
        diff.getItems().addAll("Easy","Normal","Hard","Impossible");
        diff.getSelectionModel().selectLast();
        //Loading scene
        loadingGr.setBackground(background);
        loadingGr.add(startx, 0, 1);
        GridPane.setConstraints(startx, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        loadingGr.add(starto, 0, 2);
        GridPane.setConstraints(starto, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
        loadingGr.add(startr, 0, 3);
        GridPane.setConstraints(startr, 0, 3, 1, 1, HPos.CENTER, VPos.CENTER);
        loadingGr.add(diff, 1, 2);
        GridPane.setConstraints(diff, 1, 2, 1, 1, HPos.CENTER, VPos.CENTER);
        loadingGr.setVgap(25);
        loadingGr.setHgap(25);
        //
        //Call scene
        playGr.setBackground(background);
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
        playGr.add(msg, 0, 3);
        GridPane.setConstraints(msg, 0, 3, 3, 1, HPos.CENTER, VPos.CENTER);
        playGr.add(mainmenu, 0, 4);
        GridPane.setConstraints(mainmenu, 0, 4, 3, 5, HPos.CENTER, VPos.CENTER);
        //
        loadingSc = new Scene(loadingGr);
        loadingSc.getStylesheets().add("TTTGui.css");
        playSc = new Scene(playGr);
        playSc.getStylesheets().add("TTTGui.css");
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
        handler = new TTTHandler(this);
        handler.start();
        primaryStage.setOnCloseRequest(e-> {
            handler.interrupt();
            System.exit(0);
        });
        primaryStage.show();
    }
    public void buttonClick(ActionEvent e){
        if(e.getSource() == startx)
        {
            msg.setText("");
            stage.setScene(playSc);
            handler.addInstruction("start 1 " + diff.getValue());
        }
        else if(e.getSource() == starto)
        {
            msg.setText("");
            stage.setScene(playSc);
            handler.addInstruction("start 2 " + diff.getValue());
        }
        else if(e.getSource() == startr)
        {
            msg.setText("");
            stage.setScene(playSc);
            handler.addInstruction("start " + (int)(Math.random() * 2 + 1) + " " + diff.getValue()); //Random side
        }
        else if(e.getSource() == mainmenu)
        {
            stage.setScene(loadingSc);
            //Clear game
            msg.setText("");
            for(Button[] barr : gridButtons)
                for(Button b : barr)
                    b.setText("");
            handler.addInstruction("menu");
        }
        else if(isTurn)
            for(int x = 0; x < 3; ++x)
                for(int y = 0; y < 3; ++y)
                    if(e.getSource() == gridButtons[x][y])
                    {
                        handler.addInstruction("place " + x + " " + y);
                    }
    }
    public void turnStart(boolean val){
        isTurn = val;
        Platform.runLater(() -> {
            msg.setText(val ? "Your Turn!" : "Their Turn!");
        });
    }
    public void endGame(int winner)
    {
        String str = (winner == handler.ID) ? "You Won!" : (winner < 0 ? "It's a Tie!" : "You Lost!");
        Platform.runLater(() -> {
            msg.setText(str);
        });
    }
    public void handleCrash()
    {
        Platform.runLater(() -> {
            msg.setText("ERROR!");
        });
        handler = new TTTHandler(this);
    }
    public void updateButtons(TTTBoard board)
    {
        Platform.runLater(() -> {
            for(int x = 0; x < 3; ++x)
                for(int y = 0; y < 3; ++y)
                {
                    Button b = gridButtons[x][y];
                    switch(board.getSpace(x,y))
                    {
                        case 0:
                            b.setText("");
                            break;
                        case 1:
                            b.setText("X");
                            break;
                        case 2:
                            b.setText("O");
                            break;
                    }
                }
        });
    }
}