package io.github.Leonardo0013YT.UltraMinions.animator;

import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public class Animator {

    private ArmorStand armorStand;
    private int length;
    private Frame[] frames;
    private boolean paused = false;
    private int currentFrame, executes = 0;
    private boolean interpolate;
    private EulerAngle ea = new EulerAngle(19.2, 0.0, 0);

    public Animator(Animation animation, ArmorStand stand) {
        Animation ani = animation.clone();
        this.frames = ani.getFrames();
        this.length = ani.getLength();
        this.interpolate = ani.isInterpolate();
        this.armorStand = stand;
    }

    public void update() {
        if (armorStand == null) {
            executes = length - 1;
            return;
        }
        if (!paused) {
            if (currentFrame >= (length - 1) || currentFrame < 0) {
                currentFrame = 0;
                paused = true;
                return;
            }

            Frame f = frames[currentFrame];
            if (interpolate) {
                if (f == null) {
                    f = interpolate(currentFrame);
                }
            }
            if (f != null) {
                armorStand.setBodyPose(f.middle);
                armorStand.setLeftLegPose(f.leftLeg);
                armorStand.setRightLegPose(f.rightLeg);
                armorStand.setLeftArmPose(f.leftArm);
                armorStand.setRightArmPose(f.rightArm);
                armorStand.setHeadPose(ea);
            }
            currentFrame++;
        }
    }

    private Frame interpolate(int frameID) {
        Frame minFrame = null;
        for (int i = frameID; i >= 0; i--) {
            if (frames[i] != null) {
                minFrame = frames[i];
                break;
            }
        }
        Frame maxFrame = null;
        for (int i = frameID; i < frames.length; i++) {
            if (frames[i] != null) {
                maxFrame = frames[i];
                break;
            }
        }
        Frame res;
        if (maxFrame == null || minFrame == null) {
            if (maxFrame == null && minFrame != null) {
                return minFrame;
            }
            if (maxFrame != null) {
                return maxFrame;
            }
            res = new Frame();
            res.frameID = frameID;
            return res;
        }
        res = new Frame();
        res.frameID = frameID;
        float Dmin = frameID - minFrame.frameID;
        float D = maxFrame.frameID - minFrame.frameID;
        float D0 = Dmin / D;
        res = minFrame.mult(1 - D0, frameID).add(maxFrame.mult(D0, frameID), frameID);
        return res;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public void addExecute() {
        executes++;
    }

    public int getExecutes() {
        return executes;
    }

    public int getLength() {
        return length;
    }
}