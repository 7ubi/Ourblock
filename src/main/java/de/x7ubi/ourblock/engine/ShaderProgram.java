package de.x7ubi.ourblock.engine;

import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL33.*;

public final class ShaderProgram {

    public final int programId;

    public ShaderProgram(String vertexSrc, String fragmentSrc) {
        String resolvedVertexSrc = resolveShaderSource(vertexSrc);
        String resolvedFragmentSrc = resolveShaderSource(fragmentSrc);

        int vs = compile(GL_VERTEX_SHADER, resolvedVertexSrc);
        int fs = compile(GL_FRAGMENT_SHADER, resolvedFragmentSrc);

        programId = glCreateProgram();
        glAttachShader(programId, vs);
        glAttachShader(programId, fs);
        glLinkProgram(programId);

        int linked = glGetProgrami(programId, GL_LINK_STATUS);
        if (linked == GL_FALSE) {
            String log = glGetProgramInfoLog(programId);
            throw new IllegalStateException("Program link failed:\n" + log);
        }

        glDetachShader(programId, vs);
        glDetachShader(programId, fs);
        glDeleteShader(vs);
        glDeleteShader(fs);
    }

    private static int compile(int type, String src) {
        int id = glCreateShader(type);
        glShaderSource(id, src);
        glCompileShader(id);

        int ok = glGetShaderi(id, GL_COMPILE_STATUS);
        if (ok == GL_FALSE) {
            String log = glGetShaderInfoLog(id);
            throw new IllegalStateException("Shader compile failed:\n" + log + "\nSource:\n" + src);
        }
        return id;
    }

    public void use() {
        glUseProgram(programId);
    }

    public void destroy() {
        glDeleteProgram(programId);
    }

    public void setInt(String name, int v) {
        glUniform1i(glGetUniformLocation(programId, name), v);
    }

    public void setVec3(String name, float x, float y, float z) {
        glUniform3f(glGetUniformLocation(programId, name), x, y, z);
    }

    public void setMat4(String name, org.joml.Matrix4f m) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            m.get(fb);
            glUniformMatrix4fv(glGetUniformLocation(programId, name), false, fb);
        }
    }

    private static String resolveShaderSource(String sourceOrPath) {
        if (sourceOrPath == null || sourceOrPath.isBlank()) {
            throw new IllegalArgumentException("Shader source/path must not be blank");
        }

        if (sourceOrPath.endsWith(".glsl") && !sourceOrPath.contains("\n") && !sourceOrPath.contains("\r")) {
            try (InputStream inputStream = ShaderProgram.class.getClassLoader().getResourceAsStream(sourceOrPath)) {
                if (inputStream == null) {
                    throw new IllegalStateException("Shader resource not found: " + sourceOrPath);
                }
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to read shader resource: " + sourceOrPath, e);
            }
        }

        return sourceOrPath;
    }
}
