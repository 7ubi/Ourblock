package de.x7ubi.ourblock.game.controller;

import de.x7ubi.ourblock.engine.Window;
import lombok.Getter;
import org.joml.Vector2d;
import org.joml.Vector3d;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTranslated;

@Getter
public class Controller {

    private static final double MOUSE_SENSITIVITY = 0.1f;

    private static final double MOVEMENT_SPEED = 7f;

    private static final double RUNNING_MULTIPLIER = 2f;

    @Getter
    private static final Controller instance = new Controller();

    private double yaw = 0f;

    private double pitch = 0f;

    private Vector2d lastMousePosition;

    private final Vector3d position = new Vector3d(0, 70, 0);

    private boolean moveForward = false;
    private boolean moveBackward = false;
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean isRunning = false;

    private Controller() {

    }

    public void initialize() {
        glfwSetInputMode(Window.getInstance().getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        glfwSetCursorPosCallback(Window.getInstance().getWindow(), this::updateRotation);

        glfwSetKeyCallback(Window.getInstance().getWindow(), this::updatePosition);
    }

    private void updatePosition(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_W) {
            moveForward = action == GLFW_PRESS || (action != GLFW_RELEASE && moveForward);
        }
        if (key == GLFW_KEY_S) {
            moveBackward = action == GLFW_PRESS || (action != GLFW_RELEASE && moveBackward);
        }
        if (key == GLFW_KEY_A) {
            moveLeft = action == GLFW_PRESS || (action != GLFW_RELEASE && moveLeft);
        }
        if (key == GLFW_KEY_D) {
            moveRight = action == GLFW_PRESS || (action != GLFW_RELEASE && moveRight);
        }
        if (key == GLFW_KEY_SPACE) {
            moveUp = action == GLFW_PRESS || (action != GLFW_RELEASE && moveUp);
        }
        if (key == GLFW_KEY_LEFT_CONTROL) {
            moveDown = action == GLFW_PRESS || (action != GLFW_RELEASE && moveDown);
        }
        if (key == GLFW_KEY_LEFT_SHIFT) {
            isRunning = action == GLFW_PRESS || (action != GLFW_RELEASE && isRunning);
        }


        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
            glfwSetWindowShouldClose(window, true);
    }


    private void updateRotation(long window, double xpos, double ypos) {
        if (lastMousePosition == null) {
            lastMousePosition = new Vector2d(xpos, ypos);
            return;
        }

        Vector2d currentMousePosition = new Vector2d(xpos, ypos);

        Vector2d offset = new Vector2d(currentMousePosition).sub(lastMousePosition);
        offset.mul(MOUSE_SENSITIVITY);

        lastMousePosition = currentMousePosition;

        yaw += offset.x;
        pitch += offset.y;

        pitch = Math.max(-89f, Math.min(89f, pitch));
    }

    public void update(double deltaTime) {
        glRotated(pitch, 1, 0, 0);
        glRotated(yaw, 0, 1, 0);

        move(deltaTime);
    }

    private void move(double deltaTime) {
        Vector3d forwardDirection = new Vector3d(Math.cos(Math.toRadians(yaw + 90)), 0, Math.sin(Math.toRadians(yaw + 90))).normalize();
        Vector3d leftDirection = new Vector3d(Math.cos(Math.toRadians(yaw)), 0, Math.sin(Math.toRadians(yaw))).normalize();

        double actualMovementSpeed = MOVEMENT_SPEED * deltaTime;
        if (isRunning) {
            actualMovementSpeed *= RUNNING_MULTIPLIER;
        }

        if (moveForward) {
            position.add(new Vector3d(forwardDirection).mul(actualMovementSpeed));
        }
        if (moveBackward) {
            position.sub(new Vector3d(forwardDirection).mul(actualMovementSpeed));
        }
        if (moveLeft) {
            position.add(new Vector3d(leftDirection).mul(actualMovementSpeed));
        }
        if (moveRight) {
            position.sub(new Vector3d(leftDirection).mul(actualMovementSpeed));
        }
        if (moveUp) {
            position.y += actualMovementSpeed;
        }
        if (moveDown) {
            position.y -= actualMovementSpeed;
        }

        glTranslated(position.x, -position.y, position.z);
    }
}
