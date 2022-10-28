package io.github.Leonardo0013YT.UltraMinions.animator;

public class Animation implements Cloneable {

    private Frame[] frames;
    private int length;
    private boolean interpolate;

    public Animation(Frame[] frames, int length, boolean interpolate) {
        this.frames = frames;
        this.length = length;
        this.interpolate = interpolate;
    }

    public Frame[] getFrames() {
        return frames;
    }

    public int getLength() {
        return length;
    }

    public boolean isInterpolate() {
        return interpolate;
    }

    @Override
    public Animation clone() {
        return new Animation(frames, length, interpolate);
    }

}