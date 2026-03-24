package de.x7ubi.ourblock.game.block;

import lombok.Getter;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MeshData {

    public static final int STRIDE_FLOATS = 8;

    private final List<Double> vertices = new ArrayList<>();

    private final List<Integer> indices = new ArrayList<>();

    public FloatBuffer getVerticesAsFloatBuffer() {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.size());
        for (Double vertex : vertices) {
            buffer.put(vertex.floatValue());
        }
        buffer.flip();
        return buffer;
    }

    public IntBuffer getIndicesAsIntBuffer() {
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.size());
        for (Integer index : indices) {
            buffer.put(index);
        }
        buffer.flip();
        return buffer;
    }
}
