import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.*;
import javafx.application.Platform;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class TTTGui extends Application
{
    static final int MINW = 500, MINH = 500, DEFW = 700, DEFH = 700,// MAXW = 1000, MAXH = 1000, //Window size
            BTN_W = 60, BTN_H = BTN_W, MAX_SIZE = 15;
    Stage stage;//Stage object
    Scene loadingSc, playSc;
    GridPane loadingGr = new GridPane(), playGr = new GridPane();
    GridPane[] grids = new GridPane[]{loadingGr,playGr};
    Button startx = new Button("Start (X)"), starto = new Button("Start (O)"), startr = new Button("Start (random)"), b00 = new Button(), b01 = new Button(), b02 = new Button(), b10 = new Button(),
            b11 = new Button(), b12 = new Button(), b20 = new Button(), b21 = new Button(), b22 = new Button(), mainmenu = new Button("Main Menu");
    Button[] buttons = new Button[]{startx, starto, startr, mainmenu};
    Button[][] gridButtons = new Button[MAX_SIZE][MAX_SIZE];
    Text msg = new Text(), rltext = new Text("RecursionLimit"), siztext = new Text("Board Size"), difftext = new Text("Difficulty"), imgText = new Text("Background"),timeText = new Text("Time Limit"), colorText = new Text("Solid color");
    TextField timeLimit = new TextField("30"), colorCode = new TextField("0xFFF");
    ChoiceBox<String> diff = new ChoiceBox<>();
    ChoiceBox<Integer> sizeSelector = new ChoiceBox<>(), recLimit = new ChoiceBox<>();
    ChoiceBox<BGHolder> imgList = new ChoiceBox<>();
    CheckBox useColor = new CheckBox();
    volatile boolean isTurn = false;
    volatile TTTHandler handler;
    public static void main(String[] args){
        launch();
    }
    @Override
    public void start(Stage primaryStage){
        stage = primaryStage;
        for(Button b : buttons){
            b.setOnAction(e-> buttonClick(e));
        }
        for(int x = 0; x < MAX_SIZE; ++x)
            for(int y = 0; y < MAX_SIZE; ++y)
            {
                gridButtons[x][y] = new Button();
                gridButtons[x][y].setOnAction(e-> buttonClick(e));
                gridButtons[x][y].setMinSize(BTN_W, BTN_H);
                gridButtons[x][y].setMaxSize(BTN_W, BTN_H);
                gridButtons[x][y].setId("gridb");
            }
        for(GridPane grid : grids){
            grid.setAlignment(Pos.CENTER);
            grid.setPadding(new Insets(20,20,20,20));
        }
        msg.setId("text");
        rltext.setId("text");
        siztext.setId("text");
        difftext.setId("text"); 
        timeText.setId("text");
        imgText.setId("text");
        colorText.setId("text");
        diff.getItems().addAll("Easy","Normal","Hard","Impossible");
        diff.getSelectionModel().selectLast();
        for(int s = 3; s <= MAX_SIZE; ++s)
            sizeSelector.getItems().add(s);
        sizeSelector.getSelectionModel().selectFirst();
        recLimit.getItems().addAll(4,5,6,7,8,9,10);
        recLimit.getSelectionModel().select(5);
        imgList.getItems().addAll(
            new BGHolder(new Background(new BackgroundImage(new Image("BG1.png"),null,null,null,null)), "BG1.png"),
            new BGHolder(new Background(new BackgroundImage(new Image("BG2.png"),null,null,null,null)), "BG2.png"),
            new BGHolder(new Background(new BackgroundImage(new Image("BG3.png"),null,null,null,null)), "BG3.png"),
            new BGHolder(new Background(new BackgroundImage(new Image("BG4.png"),null,null,null,null)), "BG4.png"),
            new BGHolder(new Background(new BackgroundImage(new Image("uhart.jpg"),null,null,null,null)), "uhart.jpg")
        );
        imgList.getSelectionModel().selectFirst();
        imgList.setOnAction(e-> updateBG());
        colorCode.setOnAction(e-> forceUpdateBG());
        useColor.setOnAction(e-> updateBG());
        //Loading scene
        loadingGr.add(siztext, 0, 2);
        GridPane.setConstraints(siztext, 0, 2, 1, 1, HPos.CENTER, VPos.TOP);
        loadingGr.add(sizeSelector, 0, 2);
        GridPane.setConstraints(sizeSelector, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
        loadingGr.add(rltext, 2, 2);
        GridPane.setConstraints(rltext, 2, 2, 1, 1, HPos.CENTER, VPos.TOP);
        loadingGr.add(recLimit, 2, 2);
        GridPane.setConstraints(recLimit, 2, 2, 1, 1, HPos.CENTER, VPos.CENTER);
        loadingGr.add(startx, 1, 0);
        GridPane.setConstraints(startx, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
        loadingGr.add(starto, 1, 1);
        GridPane.setConstraints(starto, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        loadingGr.add(startr, 1, 2);
        GridPane.setConstraints(startr, 1, 2, 1, 1, HPos.CENTER, VPos.CENTER);
        loadingGr.add(diff, 2, 1);
        GridPane.setConstraints(diff, 2, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        loadingGr.add(difftext, 2, 1);
        GridPane.setConstraints(difftext, 2, 1, 1, 1, HPos.CENTER, VPos.TOP);
        loadingGr.add(timeLimit, 0, 1);
        GridPane.setConstraints(timeLimit, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        loadingGr.add(timeText, 0, 1);
        GridPane.setConstraints(timeText, 0, 1, 1, 1, HPos.CENTER, VPos.TOP);
        loadingGr.add(imgList,2,0);
        GridPane.setConstraints(imgList,2,0,1,1,HPos.CENTER,VPos.CENTER);
        loadingGr.add(imgText,2,0);
        GridPane.setConstraints(imgText,2,0,1,1,HPos.CENTER, VPos.TOP);
        loadingGr.add(colorCode, 0, 0);
        GridPane.setConstraints(colorCode,0,0,1,1,HPos.CENTER, VPos.CENTER);
        loadingGr.add(colorText, 0, 0);
        GridPane.setConstraints(colorText,0,0,1,1,HPos.CENTER, VPos.TOP);
        loadingGr.add(useColor, 0, 0);
        GridPane.setConstraints(useColor,0,0,1,1,HPos.CENTER, VPos.BOTTOM);
        updateBG();
        loadingGr.setVgap(25);
        loadingGr.setHgap(25);
        ColumnConstraints c = new ColumnConstraints(100);
        RowConstraints r = new RowConstraints(75);
        loadingGr.getColumnConstraints().addAll(c, c, c);
        loadingGr.getRowConstraints().addAll(r, r, r, r);
        //
        //Call scene
        //Grid needs to be built each time a game is started, to accomodate multiple grid 
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
        //primaryStage.setMaxWidth(MAXW);
        //primaryStage.setMaxHeight(MAXH);
        primaryStage.setScene(loadingSc);
        primaryStage.setResizable(true);
        primaryStage.setOnCloseRequest(e-> {
            if(handler != null) handler.interrupt();
            System.exit(0);
        });
        primaryStage.show();
    }
    public void buttonClick(ActionEvent e){
        if(e.getSource() == startx)
        {
            msg.setText("");
            stage.setScene(playSc);
            openGrid(1, diff.getValue());
        }
        else if(e.getSource() == starto)
        {
            msg.setText("");
            stage.setScene(playSc);
            openGrid(2, diff.getValue());
        }
        else if(e.getSource() == startr)
        {
            msg.setText("");
            stage.setScene(playSc);
            openGrid((int)(Math.random() * 2 + 1), diff.getValue());
        }
        else if(e.getSource() == mainmenu)
        {
            stage.setScene(loadingSc);
            //Clear game
            msg.setText("");
            handler = null;
        }
        else if(isTurn)
            for(int x = 0; x < MAX_SIZE; ++x)
                for(int y = 0; y < MAX_SIZE; ++y)
                    if(e.getSource() == gridButtons[x][y])
                    {
                        handler.addInstruction("place " + x + " " + y);
                    }
    }
    
    public void openGrid(int ID, String diff)
    {
        int size = sizeSelector.getValue();
        playGr.getChildren().removeAll(playGr.getChildren());
        for(int x = 0; x < size; ++x)
            for(int y = 0; y < size; ++y)
            {
                Button b = gridButtons[x][y];
                b.setText("");
                playGr.add(b, x, y);
                GridPane.setConstraints(b, x, y, 1, 1, HPos.CENTER, VPos.CENTER);
            }
        playGr.add(msg, 0, size);
        GridPane.setConstraints(msg, 0, size, size, 1, HPos.CENTER, VPos.CENTER);
        playGr.add(mainmenu, 0, size + 1);
        GridPane.setConstraints(mainmenu, 0, size + 1, size, 1, HPos.CENTER, VPos.CENTER);
        updateBG();
        long time = 30000L;
        try
        {
            time = (long)(Double.parseDouble(timeLimit.getCharacters().toString()) * 1000.0);
            if(TTTBoard.DEBUG) System.err.println("Time limit: " + (time/1000.0) + " seconds");
        }
        catch(NumberFormatException ignored){}
        handler = new TTTHandler(this, ID, diff, size, recLimit.getValue(), time);
        handler.start();
    }
    
    public void turnStart(boolean val)
    {
        isTurn = val;
        setText(val ? "Your Turn!" : "Thinking...");
    }
    
    public void setText(String str)
    {
        Platform.runLater(() -> {
            msg.setText(str);
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
            stage.setScene(loadingSc);
            msg.setText("");
            handler = null;
        });
    }
    
    private void forceUpdateBG()
    {
        String ccode = colorCode.getCharacters().toString();
        if(isHexColor(ccode))
        {
            if(!useColor.isSelected()) useColor.fire();
            else updateBG();
        }
        else
        {
            if(useColor.isSelected()) useColor.fire();
            else updateBG();
        }
    }
    
    private void updateBG()
    {
        if(useColor.isSelected())
        {
            String ccode = colorCode.getCharacters().toString();
            if(isHexColor(ccode))
            {
                Background bg = new Background(new BackgroundFill(Color.web(ccode),null,null));
                loadingGr.setBackground(bg);
                playGr.setBackground(bg);
            }
            else
            {
                useColor.fire();
            }
        }
        else
        {
            Background bg = imgList.getValue().getBG();
            loadingGr.setBackground(bg);
            playGr.setBackground(bg);
        }
    }
    
    public void updateButtons(TTTBoard board)
    {
        Platform.runLater(() -> {
            for(int x = 0; x < MAX_SIZE; ++x)
                for(int y = 0; y < MAX_SIZE; ++y)
                {
                    Button b = gridButtons[x][y];
                    try
                    {
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
                    catch(TTTException e)
                    {
                        if(!e.getMessage().equals("Out of Bounds button!")) throw e;
                        b.setText("");
                    }
                }
        });
    }
    
    private boolean isHexColor(String str)
    {
        try
        {
            Color.web(str);
            return true;
        }
        catch(IllegalArgumentException | NullPointerException e)
        {
            return false;
        }
    }
}