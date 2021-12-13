package cs1302.game;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the Bird class. The Bird class creates the Bird object
 * and runs sprite animation.
 */
public class Bird {
    private Sprite bird;
    private ArrayList<Sprite> flight = new ArrayList<>();
    private int cbird = 0;
    private double xLocation = 70;
    private double yLocation = 200;
    private int BIRD_WIDTH = 50;
    private int BIRD_HEIGHT = 45;

    /**
     * Creates the Bird object and default instantiation.
     */
    public Bird() {
        bird = new Sprite();
        bird.resizeImage("file:resources/images/bradbird1.png", BIRD_WIDTH, BIRD_HEIGHT);
        bird.setPositionXY(xLocation, yLocation);
        setFlightAnimation();
    }

    /**
     * Creates the spirtes for needed animation and adds them to the
     * ArrayList.
     */
    public void setFlightAnimation() {
        Sprite b2 = new Sprite();
        b2.resizeImage("file:resources/images/bradbird2.png", BIRD_WIDTH, BIRD_HEIGHT);
        b2.setPositionXY(xLocation, yLocation);

        Sprite b3 = new Sprite();
        b3.resizeImage("file:resources/images/bradbird1.png", BIRD_WIDTH, BIRD_HEIGHT);
        b3.setPositionXY(xLocation, yLocation);

        Sprite b4 = new Sprite();
        bird4.resizeImage("file:resources/images/bradbird3.png", BIRD_WIDTH, BIRD_HEIGHT);
        bird4.setPositionXY(xLocation, yLocation);

        flight.addAll(Arrays.asList(bird, b2, b3, b4));
    } //SetFlightAnimation

    /**
     * Gets the bird sprite.
     */
    public Sprite getBird() {
        return bird;
    }

    /**
     * Animates the spirtes from the list by returning the current
     * sprite number.
     * @return the sprite
     */
    public Sprite animate() {
        if (cBird == flight.size() - 1) {
            currentBird = 0;
        }
        return flight.get(cBird++);
    }
} //Bird
