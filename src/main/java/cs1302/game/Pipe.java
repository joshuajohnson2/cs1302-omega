package cs1302.game;

/**
 * The pipe class (the main obstacle of Flappy Bird).
 */
public class Pipe {
    private Sprite pipe;
    private double xLocation;
    private double yLocation;
    private double height;
    private double width;

    /**
     * In flappy Bird, there are two types of pipes, the up pipe and
     * the down pipe. In this, we are creating a new Pipe object with
     * an isUp boolean and a height parameters.
     *
     * @param isUp the boolean to determine whether the pipe is up or down
     * @param height the height of the pipe
     */
    public Pipe(boolean isUp, int height) {
        this.pipe = new Sprite();
        this.pipe.resizeImage(isUp ? "file:resources/images/up_pipe.png" :
                             "file:resources/images/down_pipe.png", 70, height);
        this.width = 70;
        this.height = height;
        this.xLocation = 400;
        this.yLocation = isUp? 600 - height : 0;
        this.pipe.setPositionXY(xLocation, yLocation);
    }

    /**
     * The getter for the pipe sprite.
     *
     * @return the pipe sprite
     */
    public Sprite getPipe() {
        return pipe;
    }
}
