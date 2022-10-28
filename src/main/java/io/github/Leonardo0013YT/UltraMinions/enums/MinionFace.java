package io.github.Leonardo0013YT.UltraMinions.enums;

public enum MinionFace {

    NORTH(0),
    NORTHEAST(45),
    EAST(90),
    SOUTHEAST(135),
    SOUTH(180),
    SOUTHWEST(225),
    WEST(270),
    NORTHWEST(315);

    private float yaw;

    MinionFace(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }
}