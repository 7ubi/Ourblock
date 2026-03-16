package de.x7ubi.ourblock.engine;

import lombok.Getter;
import org.joml.Vector2d;
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
    private static Window instance = new Window();

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

        window = glfwCreateWindow(vidmode.width(), vidmode.height(), "Ourcraft", glfwGetPrimaryMonitor(), NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // For fullscreen, positioning is unnecessary (can remove), but harmless:
        glfwSetWindowPos(window, 0, 0);

        // Make the OpenGL context current BEFORE any GL calls
        glfwMakeContextCurrent(window);

        // Create OpenGL capabilities (required in LWJGL)
        GL.createCapabilities();

        glfwSwapInterval(1);

        // Set initial viewport using framebuffer size
        try (MemoryStack stack = stackPush()) {
            IntBuffer fbw = stack.mallocInt(1);
            IntBuffer fbh = stack.mallocInt(1);
            glfwGetFramebufferSize(window, fbw, fbh);
            glViewport(0, 0, fbw.get(0), fbh.get(0));

            updateProjection(fbw.get(0), fbh.get(0));
        }

        // Keep viewport in sync on resize / DPI changes
        glfwSetFramebufferSizeCallback(window, (win, width, height) -> {
            glViewport(0, 0, width, height);
            updateProjection(width, height);
        });

        glfwShowWindow(window);
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

    public Vector2d getWindowSize() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);
            return new Vector2d(pWidth.get(0), pHeight.get(0));
        }
    }
}