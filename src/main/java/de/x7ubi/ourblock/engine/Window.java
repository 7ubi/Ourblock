package de.x7ubi.ourblock.engine;

import lombok.Getter;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

@Getter
public class Window {

    @Getter
    private static final Window instance = new Window();

    private long window;

    private Window() {
    }

    public void initializeWindow() {

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwWindowHint(GLFW_RED_BITS, vidmode.redBits());
        glfwWindowHint(GLFW_GREEN_BITS, vidmode.greenBits());
        glfwWindowHint(GLFW_BLUE_BITS, vidmode.blueBits());
        glfwWindowHint(GLFW_REFRESH_RATE, vidmode.refreshRate());

        window = glfwCreateWindow(vidmode.width(), vidmode.height(), "Ourblock", glfwGetPrimaryMonitor(), NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetWindowPos(window, 0, 0);

        glfwMakeContextCurrent(window);

        GL.createCapabilities();

        glfwSwapInterval(1);

        try (MemoryStack stack = stackPush()) {
            IntBuffer fbw = stack.mallocInt(1);
            IntBuffer fbh = stack.mallocInt(1);
            glfwGetFramebufferSize(window, fbw, fbh);
            glViewport(0, 0, fbw.get(0), fbh.get(0));

            updateProjection(fbw.get(0), fbh.get(0));
        }

        glfwSetFramebufferSizeCallback(window, (win, width, height) -> {
            glViewport(0, 0, width, height);
            updateProjection(width, height);
        });

        glfwShowWindow(window);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
    }

    private void updateProjection(int fbWidth, int fbHeight) {
        float aspect = (float) fbWidth / (float) fbHeight;

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        float near = 0.1f;
        float far = 100.0f;

        float top = 0.05625f;
        float bottom = -top;

        float right = top * aspect;
        float left = -right;

        glFrustum(left, right, bottom, top, near, far);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }
}