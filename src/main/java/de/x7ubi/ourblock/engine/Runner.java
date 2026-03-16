package de.x7ubi.ourblock.engine;

import de.x7ubi.ourblock.engine.render.Renderer;
import de.x7ubi.ourblock.game.controller.Controller;
import org.lwjgl.opengl.GL;

import java.util.Objects;
import java.util.logging.Logger;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Runner {

    private static final Logger logger = Logger.getLogger(Runner.class.getName());

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
        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);

        renderer.initialize();
        Controller.getInstance().initialize();

        glClearColor(0.2f, 0.3f, 0.8f, 0.0f);

        while (!glfwWindowShouldClose(Window.getInstance().getWindow())) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            renderer.render();

            glfwSwapBuffers(Window.getInstance().getWindow());

            glfwPollEvents();
        }
    }

}
