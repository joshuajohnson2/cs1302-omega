package cs1302.omega;

import cs1302.game.Frogger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.*;
import javafx.animation.AnimationTimer;

/**
 * Using the idea of Frogger, I decided to replace the frog with
 * Big the Cat. As a Sonic the Hedgehog fan, I love Big the Cat
 * who is the most underrated character in the game. The mechanics
 * are pretty much the same as Frogger but the intentions are different.
 * Big must save his frog who is his best friend.
 */
public class OmegaApp extends Application {

    Image froggerLogo = new Image("file:resources/bigfrogger.png");
    ImageView imgViewFroggerLogo = new ImageView(froggerLogo);
    Image arcadeLogo = new Image("file:resources/changjun.png");
    ImageView imgViewArcadeLogo = new ImageView(arcadeLogo);
    ImageView animation = new ImageView(arcadeLogo);
    AnimationTimer timer;
    VBox vBox = new VBox();
    Scene scene = new Scene(vBox);
    double ogX = 0;
    double originalX = 0;

    /**
     * Constructs an {@code OmegaApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public OmegaApp() {}

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        stage.setMinHeight(650);
        stage.setMaxHeight(700);
        stage.setMaxWidth(550);
        stage.setMinWidth(500);
        stage.setScene(scene);

        stage.setTitle("cs1302-omega");

        initializeArcadeLogo();
        initializeGameFroggerButton();
        stage.sizeToScene();
        stage.show();

        //Animation Timer
        timer = new AnimationTimer() {
                @Override
                /**
                 * @inheritDoc
                 */
                public void handle(long now) {
                    animation();
                } //handle
            }; //AnimationTimer
        this.timer.start();
    } // start

    /**
     * Sets action for animation of the logos going back and forth.
     */
    public void animation() {
        animation.setTranslateX(animation.getTranslateX() + 10);
        imgViewArcadeLogo.setTranslateX(imgViewArcadeLogo.getTranslateX() - 10);
        if (animation.getTranslateX() > 200) {
            animation.setTranslateX(originalX);
        } //if
        if (imgViewArcadeLogo.getTranslateX() < -200) {
            imgViewArcadeLogo.setTranslateX(ogX);
        }
    } //animation

    /**
     * Intializes top arcade logo in the {@code VBox}.
     */
    public void initializeArcadeLogo() {
        double ogX = imgViewArcadeLogo.getTranslateX();
        animation.setFitHeight(150);
        animation.setFitWidth(500);
        double originalX = animation.getTranslateX();
        imgViewArcadeLogo.setFitHeight(150);
        imgViewArcadeLogo.setFitWidth(500);
        vBox.getChildren().add(imgViewArcadeLogo);
    } //intializeArcadeLogo

    /**
     * Intializes frogger {@code Button} in the {@code VBox}.
     */
    public void initializeGameFroggerButton() {
        imgViewFroggerLogo.setFitHeight(180);
        imgViewFroggerLogo.setFitWidth(480);
        Button froggerButton = new Button("" , imgViewFroggerLogo);
        vBox.getChildren().add(froggerButton);
        EventHandler<ActionEvent> playFrogger = ((e) -> new Frogger().show());
        froggerButton.setOnAction(playFrogger);
    } //intializeGameLogoButtons

} // OmegaApp
