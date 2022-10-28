package io.github.Leonardo0013YT.UltraMinions.animator;

import org.bukkit.util.EulerAngle;

public class Frame {

    public int frameID;
    public float x, y, z, r;
    public EulerAngle middle;
    public EulerAngle rightLeg;
    public EulerAngle leftLeg;
    public EulerAngle rightArm;
    public EulerAngle leftArm;
    public EulerAngle head;

    public Frame mult(float a, int frameID) {
        Frame f = new Frame();
        f.frameID = frameID;
        f.x = f.x * a;
        f.y = f.y * a;
        f.z = f.z * a;
        f.r = f.r * a;
        f.middle = new EulerAngle(middle.getX() * a, middle.getY() * a, middle.getZ() * a);
        f.rightLeg = new EulerAngle(rightLeg.getX() * a, rightLeg.getY() * a, rightLeg.getZ() * a);
        f.leftLeg = new EulerAngle(leftLeg.getX() * a, leftLeg.getY() * a, leftLeg.getZ() * a);
        f.rightArm = new EulerAngle(rightArm.getX() * a, rightArm.getY() * a, rightArm.getZ() * a);
        f.leftArm = new EulerAngle(leftArm.getX() * a, leftArm.getY() * a, leftArm.getZ() * a);
        f.head = new EulerAngle(head.getX() * a, head.getY() * a, head.getZ() * a);
        return f;
    }

    public Frame add(Frame a, int frameID) {
        Frame f = new Frame();
        f.frameID = frameID;
        f.x = f.x + a.x;
        f.y = f.y + a.y;
        f.z = f.z + a.z;
        f.r = f.r + a.r;
        f.middle = new EulerAngle(middle.getX() + a.middle.getX(), middle.getY() + a.middle.getY(), middle.getZ() + a.middle.getZ());
        f.rightLeg = new EulerAngle(rightLeg.getX() + a.rightLeg.getX(), rightLeg.getY() + a.rightLeg.getY(), rightLeg.getZ() + a.rightLeg.getZ());
        f.leftLeg = new EulerAngle(leftLeg.getX() + a.leftLeg.getX(), leftLeg.getY() + a.leftLeg.getY(), leftLeg.getZ() + a.leftLeg.getZ());
        f.rightArm = new EulerAngle(rightArm.getX() + a.rightArm.getX(), rightArm.getY() + a.rightArm.getY(), rightArm.getZ() + a.rightArm.getZ());
        f.leftArm = new EulerAngle(leftArm.getX() + a.leftArm.getX(), leftArm.getY() + a.leftArm.getY(), leftArm.getZ() + a.leftArm.getZ());
        f.head = new EulerAngle(head.getX() + a.head.getX(), head.getY() + a.head.getY(), head.getZ() + a.head.getZ());
        return f;
    }

}