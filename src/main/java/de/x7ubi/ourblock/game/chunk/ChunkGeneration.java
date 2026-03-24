package de.x7ubi.ourblock.game.chunk;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.core.api.noisegen.NoiseGenerator;
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator;
import de.x7ubi.ourblock.engine.ShaderProgram;
import de.x7ubi.ourblock.game.controller.Controller;
import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3d;

import java.util.HashMap;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class ChunkGeneration {

    @Getter
    private static final ChunkGeneration instance = new ChunkGeneration();

    @Getter
    private final ShaderProgram shaderProgram = new ShaderProgram("shaders/chunk_vertex.glsl", "shaders/chunk_fragment.glsl");

    private static final long SEED = 12345;

    private final NoiseGenerator noiseGenerator = PerlinNoiseGenerator.newBuilder().setSeed(SEED).setInterpolation(Interpolation.COSINE).build();

    private final HashMap<Vector2d, Chunk> chunks = new HashMap<>();

    private ChunkGeneration() {
        initialize();
    }

    public void initialize() {
        for (int x = -4; x < 4; x++) {
            for (int z = -4; z < 4; z++) {
                chunks.put(new Vector2d(x * Chunk.CHUNK_SIZE, z * Chunk.CHUNK_SIZE),
                        new Chunk(new Vector3d(x * Chunk.CHUNK_SIZE, 0, z * Chunk.CHUNK_SIZE), noiseGenerator));
            }
        }


        for (Vector2d chunkPos : chunks.keySet()) {
            Chunk chunk = chunks.get(chunkPos);
            Chunk neighborXPos = chunks.get(new Vector2d(chunkPos.x + Chunk.CHUNK_SIZE, chunkPos.y));
            Chunk neighborXNeg = chunks.get(new Vector2d(chunkPos.x - Chunk.CHUNK_SIZE, chunkPos.y));
            Chunk neighborZPos = chunks.get(new Vector2d(chunkPos.x, chunkPos.y + Chunk.CHUNK_SIZE));
            Chunk neighborZNeg = chunks.get(new Vector2d(chunkPos.x, chunkPos.y - Chunk.CHUNK_SIZE));
            chunk.generateMeshData(neighborXPos, neighborXNeg, neighborZPos, neighborZNeg);
        }
    }

    public void render() {
        shaderProgram.use();

        glActiveTexture(GL_TEXTURE0);
        shaderProgram.setInt("uAtlas", 0);
        shaderProgram.setVec3("uLightDirWS", -0.3f, -1.0f, -0.2f);

        Matrix4f projection = new Matrix4f().perspective((float) Math.toRadians(70.0), 16.0f / 9.0f, 0.1f, 1000.0f);
        shaderProgram.setMat4("uProj", projection);
        shaderProgram.setMat4("uView", Controller.getInstance().getViewMatrix());

        for (Chunk chunk : chunks.values()) {
            chunk.render(shaderProgram);
        }
    }

    public void cleanup() {
        for (Chunk chunk : chunks.values()) {
            chunk.cleanup();
        }
        shaderProgram.destroy();
    }
}
