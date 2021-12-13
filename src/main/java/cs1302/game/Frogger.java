package cs1302.game;

import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.effect.Lighting;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Frogger Game class.
 */
public class Frogger extends Stage {
    StackPane sp = new StackPane();
    StackPane bigOnTurtle = new StackPane();
    Pane turtlePane = new Pane();
    Pane pn = new Pane();
    Pane p = new Pane();
    Image largeCar = new Image("file:resources/BigCar.png");
    Image smallCar = new Image("file:resources/SmallCar.png");
    Image bc = new Image("file:resources/big the cat.png");
    ImageView big = new ImageView(bc);
    Scene scene = new Scene(sp);
    AnimationTimer timer;
    List<List<Rectangle>> logs = new ArrayList<>();
    List<List<ImageView>> cars = new ArrayList<>();
    List<ImageView> bigLives = new ArrayList<>();
    List<Rectangle> allRec = new ArrayList<>();
    Image turtle = new Image("file:resources/Frogger_turtle.png");
    List<ImageView> turtleList = new ArrayList<>();
    Stage gameOver = new Stage();
    Stage instructions = new Stage();
    Stage wonGame = new Stage();
    boolean isMoving = false;
    boolean bigDies = false;
    boolean crosses = false;
    Stage winWindow = new Stage();
    int score = 0;
    int winCount = 0;
    int deathCount = 0;
    int lives = 5;
    double velocity = 0.5;
    boolean toLevelUp = false;
    Text scoreDisp = new Text("Score: " + score);
    Stage initials = new Stage();
    String loc = "Scores.txt";
    File configFile = new File(loc);

    /**
     * Instantiates the object Frogger.
     */
    public Frogger() {
        super();
        intializeStage();
        sp.requestFocus();
        initializeWinWindow();
        initializeGameOver();
        initializeWinGame();
        runNow(() -> initializeScene());
        initializeInstructions();

        runNow(() -> moveBig());


        //Animation Timer
        timer = new AnimationTimer() {
                @Override
                /** @inheritDoc */
                public void handle(long now) {
                    animation();
                } //handle
            }; //AnimationTimer
        timer.start();
    } //GameFrogger


    /**
     * The animation of the game.
     */
    public void animation() {
        logsMotion();
        checkBigOnLogs();
        checkBigInWater();
        carsMotion();
        checkBigHitByCar();
        if (deathCount == lives) {
            gameOver.show();
            timer.stop();
        } else if (winCount == 3) {
            wonGame.show();
            timer.stop();
        } else if (bigDies) {
            bigDies();
        } else if (bigCrosses()) {
            levelWon();
        }
        if (toLevelUp) {
            timer.stop();
            levelUp();
            toLevelUp = false;
            timer.start();
        } //if
    }

    /**
     * If the user has won the game, the winWindow will show up.
     */
    public void levelWon() {
        winWindow.show();
        crosses = false;
        toLevelUp = true;
        big.setTranslateX(10);
        big.setTranslateY(195);
        winCount += 1;
    } //levelWon

    /**
     * This method initializes all the necessary characteristics
     * of the scene such as Big's lives, adding logs to the pane,
     * adding cars to the pane, initializing turles and big himself,
     * and much more.
     */
    public void initializeScene() {
        //Intializing Big's lives
        Platform.runLater(() -> initializeBigLives());
        //Creating 5 rows of logs
        initializeRowsOfLogs();
        //Creating 5 rows of cars
        intializeRowsOfCars();
        //Adding logs to the pane
        Platform.runLater(() -> addLogsToPane());
        //Adding logs to list of all rectangle objs
        addToAllLogs();
        //Adding cars to the pane
        Platform.runLater(() -> addCarsToPane());
        //Adding the pane to the stack pane
        Platform.runLater(() -> this.sp.getChildren().add(pn));
        //Initializing Big
        initializeBig();
        //Intialize Turtles
        Platform.runLater(() -> initializeTurtles());
        Platform.runLater(() -> this.sp.getChildren().add(big));
        intializeScore();
        Platform.runLater(() -> this.pn.getChildren().add(scoreDisp));
    } //initializeScene

    /**
     * Initializes the main stage of the game.
     */
    public void intializeStage() {
        setBackground();
        this.setScene(scene);
        this.setMinWidth(500);
        this.setMaxWidth(550);
        this.setMinHeight(500);
        this.setMaxHeight(550);
    } //intializeStage

    /**
     * Inititizes the stage if you won the whole game.
     */
    public void initializeWinGame() {
        wonGame.setTitle("You won!");
        VBox vBox = new VBox();
        Scene s = new Scene(vBox);
        Text text = new Text("GGEZ! Exit to start over!");
        vBox.getChildren().add(text);
        wonGame.setScene(s);
        wonGame.hide();
    } //intializeWinGame

    /**
     * Initializes the score displayed.
     */
    public void intializeScore() {
        scoreDisp.setFill(Color.GREEN);
        scoreDisp.setY(40);
        scoreDisp.setX(200);
    } //intializeScore

    /**
     * Creates the alert for the instructions.
     */
    public void initializeInstructions() {
        String s1 = "Welcome to Big the Cat: Frogger!\n";
        String s2 = "Big the Cat is a cat who loves his dear frog, Froggy.\n";
        String s3 = "The goal of the game is to get to the other side\n";
        String s4 = "Try not to touch the river or hit the cars, or Big will die!";
        Alert alert = new Alert(Alert.AlertType.NONE, s1 + s2 + s3 + s4
                               , ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    } //intializeInstructions

    /**
     * Initializes the gameOver stage that says that you failed the game.
     */
    public void initializeGameOver() {
        gameOver.setTitle("Game Over");
        VBox vBox = new VBox();
        Scene s = new Scene(vBox);
        Text text = new Text("RIP F in chat! \n To start over just exit out and start over! \n"
                             + "Thanks for playing!");
        vBox.getChildren().add(text);
        gameOver.setScene(s);
        gameOver.hide();
    } //intializeGameOver

    /**
     * Big dies and loses a life RIP. He is also teleported to the start.
     */
    public void bigDies() {
        big.setTranslateX(10);
        big.setTranslateY(200);
        deathCount += 1;
        bigLives.get(bigLives.size() - deathCount).setImage(null);
        bigDies = false;
    } //bigDies

    /**
     * Increases the velocity by .5 and increases the score.
     */
    public void levelUp() {
        velocity += 0.5;
        score += 100;
        scoreDisp.setText("Score: " + score);
    } //levelUp

    /**
     * Initializes the stage that pops up if a level is won.
     */
    public void initializeWinWindow() {
        winWindow.setTitle("You Won!!");
        VBox vBox = new VBox();
        Scene s = new Scene(vBox);
        Text text = new Text("YOU WON!! Moving on to the next level!");
        vBox.getChildren().add(text);
        winWindow.setScene(s);
        winWindow.hide();
    } //initializeWinWindow

    /**
     * Checks if big's bounds intersect with any of the turtle bounds.
     *
     * @return a boolean denoting if Big has successfully crossed
     */
    public boolean bigCrosses() {
        for (int i = 0; i < this.turtleList.size(); i++) {
            if (big.getBoundsInParent().intersects(turtleList.get(i).getBoundsInParent())) {
                crosses = true;
                break;
            } //if
        } //for
        return crosses;
    } //bigCrosses


    /**
     * Checks the intersection of Big the cat with the car.
     */
    public void checkBigHitByCar() {
        for (int i = 0; i < cars.size(); i++) {
            List<ImageView> carsL = cars.get(i);
            if (isMoving) {
                isMoving = false;
                continue;
            } //if
            for (int j = 0; j < carsL.size(); j++) {
                if (big.getTranslateY() >= 8 && big.getTranslateY() <= 169) {
                    if (big.getBoundsInParent().intersects(carsL.get(j).getBoundsInParent())) {
                        bigDies = true;
                    } //if
                } //if
            } //for
        } //for
    } //checkBigHitByCar()

    /**
     * "Animates" or moves the cars on its own.
     */
    public void carsMotion() {
        for (int i = 0; i < cars.size(); i++) {
            List<ImageView> carsL = cars.get(i);
            for (int j = 0; j < carsL.size(); j++) {
                if (i % 2 == 0) {
                    if (carsL.get(j).getTranslateX() < 10000) {
                        carsL.get(j).setTranslateX(carsL.get(j).getTranslateX() + velocity);
                    } else {
                        carsL.get(j).setTranslateX(-10000);
                    } //else
                } else {
                    if (carsL.get(j).getTranslateX() > -10000) {
                        carsL.get(j).setTranslateX(carsL.get(j).getTranslateX() - velocity);
                    } else {
                        carsL.get(j).setTranslateX(10000);
                    } //else
                } //else
            } //for
        } //for
    } //moveCars

    /**
     * Checks to see if Big is in the water. if he is then oof.
     */
    public void checkBigInWater() {
        boolean nm = allRec.stream().noneMatch(p -> big.getBoundsInParent()
                            .intersects(p.getBoundsInParent()));
        if (nm && big.getTranslateY() >= -152 && big.getTranslateY() <= -17) {
            bigDies = true;
        }
    } //checkFrogInWater

    /**
     * Checks whether Big and the Logs are intersecting.
     */
    public void checkBigOnLogs() {
        for (int i = 0; i < logs.size(); i++) {
            List<Rectangle> rects = logs.get(i);
            for (int j = 0; j < rects.size(); j++) {
                if (isMoving) {
                    isMoving = false;
                    continue;
                } //if
                if (big.getTranslateY() >= -152 && big.getTranslateY() <= -17) {
                    if (big.getBoundsInParent().intersects(rects.get(j).getBoundsInParent())) {
                        if (big.getTranslateX() < 230 && big.getTranslateX() > -230) {
                            if (i % 2 == 0) {
                                big.setTranslateX(big.getTranslateX() + velocity);
                            } else {
                                big.setTranslateX(big.getTranslateX() - velocity);
                            } //else
                        } else {
                            bigDies = true;
                        } //else
                    }
                } //if
            } //for
        } //for
    } //checkFrogOnLogs

    /**
     * Creates the "animation" or movement for the logs in the game.
     */
    public void logsMotion() {
        for (int i = 0; i < logs.size(); i++) {
            List<Rectangle> rects = logs.get(i);
            for (int j = 0; j < rects.size(); j++) {
                if (i % 2 == 0) { //Forwards movement
                    if (rects.get(j).getTranslateX() < 1000) {
                        rects.get(j).setTranslateX(rects.get(j).getTranslateX() + velocity);
                    } else {
                        rects.get(j).setTranslateX(-1000);
                    } //else
                } else {
                    if (rects.get(j).getTranslateX() > -1000) {
                        rects.get(j).setTranslateX(rects.get(j).getTranslateX() - velocity);
                    } else {
                        rects.get(j).setTranslateX(1000);
                    } //else
                } //else
            } //for
        } //for
    } //moveLogs

    /**
     * Initializes Big the Cat object.
     */
    public void initializeBig() {
        big.setFitHeight(25);
        big.setFitWidth(25);
        big.setTranslateY(200);
        big.setX(225);
    } //initializeFrog

    /**
     * Adds the cars to the Pane.
     */
    public void addCarsToPane() {
        //adding the cars to the pane
        for (int i = 0; i < this.cars.size(); i++) {
            List<ImageView> carsL = this.cars.get(i);
            for (int j = 0; j < carsL.size(); j++) {
                this.pn.getChildren().add(carsL.get(j));
            } //for
        } //for
    } //addCarsToPane

    /**
     * Adds the Rectangle to all the logs array.
     */
    public void addToAllLogs() {
        for (int i = 0; i < this.logs.size(); i++) {
            List<Rectangle> rects = logs.get(i);
            for (int j = 0; j < rects.size(); j++) {
                allRec.add(rects.get(j));
            } //for
        } //for
    } //addToAllLogs

    /**
     * Adds the rectangle objects to the game.
     */
    public void addLogsToPane() {
        for (int i = 0; i < this.logs.size(); i++) {
            List<Rectangle> rects = this.logs.get(i);
            for (int j = 0; j < rects.size(); j++) {
                this.pn.getChildren().add(rects.get(j));
            } //for
        } //for
    } //addLogsToPane

    /**
     * Creates five rows of Cars to be used in the game.
     */
    public void intializeRowsOfCars() {
        int count = 0;
        double y = 283;
        double x = -80900;
        while (count < 5) {
            if (count % 2 == 0) {
                initializeBigCars(x, y);
                y += 31;
                x = -80900;
            } else {
                initializeSmallCars(x, y);
                x = -55000;
                y += 31;
            } //else
            count += 1;
        } //while
    } //initializeRowsOfCars

    /**
     * Creates five rows of Rectangle objects to be used as Logs.
     */
    public void initializeRowsOfLogs() {
        int count = 0;
        double y = 103;
        double x = -80900;
        while (count < 5) {
            initializeLogs(x, y);
            y += 30;
            if (count % 2 == 0) {
                x = -80900;
            } else {
                x = -55000;
            } //else
            count += 1;
        } //while
    } //intializeRowsOfLogs

    /**
     * Creates a List that handles Big the Cat's lives.
     */
    public void initializeBigLives() {
        double x = 10;
        for (int i = 0; i < lives; i++) {
            bigLives.add(new ImageView(big.getImage()));
            bigLives.get(i).setFitHeight(20);
            bigLives.get(i).setFitWidth(20);
            bigLives.get(i).setX(x);
            x += 20;
            bigLives.get(i).setY(10);
            this.pn.getChildren().add(bigLives.get(i));
        } //for
    } //intializeFrogLives

    /**
     * Creates a List that initializes the turtles that Big will get.
     */
    public void initializeTurtles() {
        double x = 40;
        for (int i = 0; i < 5; i++) {
            turtleList.add(new ImageView(turtle));
            turtleList.get(i).setFitHeight(20);
            turtleList.get(i).setFitWidth(20);
            turtleList.get(i).setX(x);
            x += 95;
            turtleList.get(i).setY(70);
            this.turtlePane.getChildren().add(turtleList.get(i));
        } //for
        this.pn.getChildren().add(turtlePane);
    } //intializeTurtles

    /**
     * Creates a List that is composed of brown rectangles (logs) that will be used in the game.
     *
     * @param x a double x value
     * @param y a double y value
     */
    public void initializeLogs(double x, double y) {
        List<Rectangle> rects = new ArrayList<Rectangle>();
        for (int i = 0; i < 1000; i++) {
            if (i % 3 == 0) {
                rects.add(new Rectangle(75, 15, Color.BROWN));
            } else if (i % 2 == 0) {
                rects.add(new Rectangle(35, 15, Color.BROWN));
            } else {
                rects.add(new Rectangle(50, 15, Color.BROWN));
            } //else
            rects.get(i).setX(x);
            rects.get(i).setY(y);
            x += 150;
        } //for
        logs.add(rects);
    } //createLogs

    /**
     * Creates an array that initializes all the smaller cars in the game.
     *
     * @param xProp is the prop for the x axis
     * @param yProp is the prop for the y axis
     */
    public void initializeSmallCars(double xProp, double yProp) {
        List<ImageView> carsL = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            carsL.add(new ImageView(smallCar));
            carsL.get(i).setFitHeight(25);
            carsL.get(i).setFitWidth(25);
            carsL.get(i).setX(xProp);
            carsL.get(i).setY(yProp);
            if (i % 2 == 0) {
                xProp += 160;
            } else {
                xProp += 130;
            } //else
        } //for
        this.cars.add(carsL);
    } //createSmallCars

    /**
     * Creates an array that initializes all the large cars in the game.
     *
     * @param xProp is the prop for the x axis
     * @param yProp is the prop for the y axis.
     */
    public void initializeBigCars(double xProp, double yProp) {
        List<ImageView> carsL = new ArrayList<ImageView>();
        for (int i = 0; i < 1000; i++) {
            carsL.add(new ImageView(largeCar));
            carsL.get(i).setFitHeight(25);
            carsL.get(i).setFitWidth(25);
            carsL.get(i).setX(xProp);
            carsL.get(i).setY(yProp);
            if (i % 2 == 0) {
                xProp += 95;
            } else {
                xProp += 125;
            } //else
        } //for
        this.cars.add(carsL);
    } //createBigCars


    /**
     * Creates an Background image from the imageview and centers it.
     */
    public void setBackground() {
        Image frogger = new Image("file:resources/FroggerBackground.png");
        ImageView frgr = new ImageView(frogger);
        frgr.setFitHeight(500);
        frgr.setFitWidth(500);
        BackgroundSize bs = new BackgroundSize(500, 500, false, false, false, true);
        BackgroundImage bg = new BackgroundImage(frgr.getImage(), BackgroundRepeat.NO_REPEAT,
                                                 BackgroundRepeat.NO_REPEAT,
                                                 BackgroundPosition.DEFAULT, bs);
        sp.setBackground(new Background(bg));
    } //setBackground

    /**
     * Moves Big upward.
     */
    public void bigMovesUp() {
        big.setTranslateY(big.getTranslateY() - 31);
    } //bigMovesUp

    /**
     * Moves Big downward.
     */
    public void bigMovesDown() {
        big.setTranslateY(big.getTranslateY() + 31);
    } //bigMovesDown

    /**
     * Moves Big to the left.
     */
    public void bigMovesLeft() {
        big.setTranslateX(big.getTranslateX() - 30);
    } //bigMovesLeft

    /**
     * Moves Big to the right.
     */
    public void bigMovesRight() {
        big.setTranslateX(big.getTranslateX() + 30);
    } //bigMovesRight

    /**
     * Creates an {@code EventHandler} that handles how Big the Cat moves.
     */
    public void moveBig() {
        EventHandler<KeyEvent> moveBig = (e -> {
            isMoving = true;
            if (e.getCode() == KeyCode.UP) {
                Platform.runLater(() -> bigMovesUp());
            } else if (e.getCode() == KeyCode.DOWN) {
                Platform.runLater(() -> bigMovesDown());
            } else if (e.getCode() == KeyCode.LEFT) {
                Platform.runLater(() -> bigMovesLeft());
            } else if (e.getCode() == KeyCode.RIGHT) {
                Platform.runLater(() -> bigMovesRight());
            } //if
        }); //moveBig
        scene.setOnKeyPressed(moveBig);
    } //moveBig

     /**
      * Creates and immediately starts a new daemon thread that excecutes
      * {@code target.run()}. This method, which may be called from any
      * thread will return immediately its the caller.
      * @param target the object whose {@code run} method is invoked when this
      *               thread is started
      */
    public static void runNow(Runnable target) {
        Thread t = new Thread(target);
        t.setDaemon(true);
        t.start();
    } //runNow
} //Frogger
