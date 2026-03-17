package de.x7ubi.ourblock.engine;

import de.x7ubi.ourblock.engine.render.Renderer;
import de.x7ubi.ourblock.game.controller.Controller;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Runner {

    Renderer renderer;

    public Runner() {
        renderer = new Renderer();
    }


    public void run() {
        Window.getInstance().initializeWindow();

        loop();

        renderer.cleanup();
        glfwFreeCallbacks(Window.getInstance().getWindow());
        glfwDestroyWindow(Window.getInstance().getWindow());

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }


    private void loop() {
        renderer.initialize();
        Controller.getInstance().initialize();

        glClearColor(0.2f, 0.3f, 0.8f, 0.0f);

        double lastTime = glfwGetTime();

        while (!glfwWindowShouldClose(Window.getInstance().getWindow())) {
            double now = glfwGetTime();
            double deltaTime = now - lastTime;
            lastTime = now;

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            renderer.render(deltaTime);

            glfwSwapBuffers(Window.getInstance().getWindow());

            glfwPollEvents();
        }
    }

}
