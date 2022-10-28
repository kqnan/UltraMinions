package io.github.Leonardo0013YT.UltraMinions.managers;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.animator.Animation;
import io.github.Leonardo0013YT.UltraMinions.animator.Animator;
import io.github.Leonardo0013YT.UltraMinions.animator.Frame;
import io.github.Leonardo0013YT.UltraMinions.interfaces.BlockAnimation;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnimationManager {

    private Map<String, Animation> animCache = new HashMap<>();
    private ArrayList<Animator> animators = new ArrayList<>();
    private ArrayList<BlockAnimation> blocks = new ArrayList<>();
    private boolean blockAnimation = false;
    private BukkitTask task;
    private Main plugin;

    public AnimationManager(Main plugin) {
        this.plugin = plugin;
        reload();
    }

    public void addBlockAnimation(BlockAnimation block) {
        blocks.add(block);
    }

    public void reload() {
        animators.clear();
        if (task != null) {
            task.cancel();
        }
        task = new BukkitRunnable() {
            @Override
            public void run() {
                blockAnimation = !blockAnimation;
                if (blockAnimation && !blocks.isEmpty()) {
                    ArrayList<BlockAnimation> to = new ArrayList<>();
                    for (BlockAnimation ani : blocks) {
                        ani.update();
                        if (ani.isFinished()) {
                            to.add(ani);
                        }
                    }
                    blocks.removeAll(to);
                }
                if (animators.isEmpty()) return;
                ArrayList<Animator> to = null;
                for (Animator ani : animators) {
                    ani.addExecute();
                    ani.update();
                    if (ani.getExecutes() >= ani.getLength() - 1) {
                        if (to == null) {
                            to = new ArrayList<>();
                        }
                        ani.getArmorStand().setHeadPose(new EulerAngle(0, 0, 0));
                        to.add(ani);
                    }
                }
                if (to != null) {
                    animators.removeAll(to);
                }
            }
        }.runTaskTimer(Main.get(), 0, 1);
    }

    private void loadAnimator(File aniFile) {
        Frame[] frames = new Frame[0];
        int length = 0;
        boolean interpolate = false;
        try (BufferedReader br = new BufferedReader(new FileReader(aniFile))) {
            String line = "";
            Frame currentFrame = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("length")) {
                    length = (int) Float.parseFloat(line.split(" ")[1]);
                    frames = new Frame[length];
                } else if (line.startsWith("frame")) {
                    if (currentFrame != null) {
                        frames[currentFrame.frameID] = currentFrame;
                    }
                    int frameID = Integer.parseInt(line.split(" ")[1]);
                    currentFrame = new Frame();
                    currentFrame.frameID = frameID;
                } else if (line.contains("interpolate")) {
                    interpolate = true;
                } else if (line.contains("Armorstand_Position")) {
                    if (currentFrame == null) {
                        return;
                    }
                    currentFrame.x = Float.parseFloat(line.split(" ")[1]);
                    currentFrame.y = Float.parseFloat(line.split(" ")[2]);
                    currentFrame.z = Float.parseFloat(line.split(" ")[3]);
                    currentFrame.r = Float.parseFloat(line.split(" ")[4]);
                } else if (line.contains("Armorstand_Middle")) {
                    if (currentFrame == null) {
                        return;
                    }
                    float x = (float) Math.toRadians(Float.parseFloat(line.split(" ")[1]));
                    float y = (float) Math.toRadians(Float.parseFloat(line.split(" ")[2]));
                    float z = (float) Math.toRadians(Float.parseFloat(line.split(" ")[3]));
                    currentFrame.middle = new EulerAngle(x, y, z);
                } else if (line.contains("Armorstand_Right_Leg")) {
                    if (currentFrame == null) {
                        return;
                    }
                    float x = (float) Math.toRadians(Float.parseFloat(line.split(" ")[1]));
                    float y = (float) Math.toRadians(Float.parseFloat(line.split(" ")[2]));
                    float z = (float) Math.toRadians(Float.parseFloat(line.split(" ")[3]));
                    currentFrame.rightLeg = new EulerAngle(x, y, z);
                } else if (line.contains("Armorstand_Left_Leg")) {
                    if (currentFrame == null) {
                        return;
                    }
                    float x = (float) Math.toRadians(Float.parseFloat(line.split(" ")[1]));
                    float y = (float) Math.toRadians(Float.parseFloat(line.split(" ")[2]));
                    float z = (float) Math.toRadians(Float.parseFloat(line.split(" ")[3]));
                    currentFrame.leftLeg = new EulerAngle(x, y, z);
                } else if (line.contains("Armorstand_Left_Arm")) {
                    if (currentFrame == null) {
                        return;
                    }
                    float x = (float) Math.toRadians(Float.parseFloat(line.split(" ")[1]));
                    float y = (float) Math.toRadians(Float.parseFloat(line.split(" ")[2]));
                    float z = (float) Math.toRadians(Float.parseFloat(line.split(" ")[3]));
                    currentFrame.leftArm = new EulerAngle(x, y, z);
                } else if (line.contains("Armorstand_Right_Arm")) {
                    if (currentFrame == null) {
                        return;
                    }
                    float x = (float) Math.toRadians(Float.parseFloat(line.split(" ")[1]));
                    float y = (float) Math.toRadians(Float.parseFloat(line.split(" ")[2]));
                    float z = (float) Math.toRadians(Float.parseFloat(line.split(" ")[3]));
                    currentFrame.rightArm = new EulerAngle(x, y, z);
                } else if (line.contains("Armorstand_Head")) {
                    if (currentFrame == null) {
                        return;
                    }
                    float x = (float) Math.toRadians(Float.parseFloat(line.split(" ")[1]));
                    float y = (float) Math.toRadians(Float.parseFloat(line.split(" ")[2]));
                    float z = (float) Math.toRadians(Float.parseFloat(line.split(" ")[3]));
                    currentFrame.head = new EulerAngle(x, y, z);
                }
            }
            if (currentFrame != null) {
                frames[currentFrame.frameID] = currentFrame;
            }
        } catch (Exception ignored) {
        }
        animCache.put(aniFile.getAbsolutePath(), new Animation(frames, length, interpolate));
    }

    public void execute(String animation, ArmorStand stand) {
        File file = new File(plugin.getDataFolder(), animation);
        if (!animCache.containsKey(file.getAbsolutePath())) {
            loadAnimator(file);
        }
        Animator ani = new Animator(animCache.get(file.getAbsolutePath()), stand);
        animators.add(ani);
    }

}