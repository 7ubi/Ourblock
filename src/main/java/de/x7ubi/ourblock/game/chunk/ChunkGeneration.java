package de.x7ubi.ourblock.game.chunk;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.core.api.noisegen.NoiseGenerator;
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator;
import de.x7ubi.ourblock.engine.ShaderProgram;
import de.x7ubi.ourblock.game.controller.Controller;
import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class ChunkGeneration {

    private static final long SEED = 12345;

    private static final int RENDER_DISTANCE = 12;

    @Getter
    private static final ChunkGeneration instance = new ChunkGeneration();

    @Getter
    private final ShaderProgram shaderProgram = new ShaderProgram("shaders/chunk_vertex.glsl", "shaders/chunk_fragment.glsl");

    private final NoiseGenerator noiseGenerator = PerlinNoiseGenerator.newBuilder().setSeed(SEED).setInterpolation(Interpolation.COSINE).build();

    private final ConcurrentHashMap<Vector2i, Chunk> chunks = new ConcurrentHashMap<>();

    private final List<Chunk> newChunks = new ArrayList<>();

    private Vector2i lastPlayerChunkPos = new Vector2i(0, 0);

    private ChunkGeneration() {
        initialize();
    }

    public void initialize() {
        for (int x = -RENDER_DISTANCE; x < RENDER_DISTANCE; x++) {
            for (int z = -RENDER_DISTANCE; z < RENDER_DISTANCE; z++) {
                Chunk chunk = new Chunk(new Vector3d(x * Chunk.CHUNK_SIZE, 0, z * Chunk.CHUNK_SIZE), noiseGenerator);
                chunks.put(new Vector2i(x * Chunk.CHUNK_SIZE, z * Chunk.CHUNK_SIZE), chunk);
                newChunks.add(chunk);
            }
        }

        generateMeshDataForNewChunks();
    }

    public void updateChunks() {
        Vector3d playerPos = Controller.getInstance().getPosition();
        Vector2i playerChunkPos = new Vector2i(
                (int) (Math.floor(-playerPos.x / Chunk.CHUNK_SIZE) * Chunk.CHUNK_SIZE),
                (int) (Math.floor(-playerPos.z / Chunk.CHUNK_SIZE) * Chunk.CHUNK_SIZE)
        );

        if (!playerChunkPos.equals(lastPlayerChunkPos)) {
            for (int x = -RENDER_DISTANCE; x < RENDER_DISTANCE; x++) {
                for (int z = -RENDER_DISTANCE; z < RENDER_DISTANCE; z++) {
                    Vector2i chunkPos = new Vector2i(playerChunkPos.x + x * Chunk.CHUNK_SIZE, playerChunkPos.y + z * Chunk.CHUNK_SIZE);
                    if (!chunks.containsKey(chunkPos)) {
                        Chunk chunk = new Chunk(new Vector3d(chunkPos.x, 0, chunkPos.y), noiseGenerator);
                        chunks.put(chunkPos, chunk);
                        newChunks.add(chunk);
                    }
                }
            }

            for (Vector2i chunkPos : chunks.keySet()) {
                if (Math.abs(chunkPos.x - playerChunkPos.x) > RENDER_DISTANCE * Chunk.CHUNK_SIZE || Math.abs(chunkPos.y - playerChunkPos.y) > RENDER_DISTANCE * Chunk.CHUNK_SIZE) {
                    Chunk removed = chunks.remove(chunkPos);
                    if (removed != null) {
                        removed.cleanup();
                    }
                }
            }

            generateMeshDataForNewChunks();
        }
        lastPlayerChunkPos = playerChunkPos;
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

    private void generateMeshDataForNewChunks() {
        for (Chunk chunk : newChunks) {
            Vector2i pos = new Vector2i((int) chunk.getPosition().x, (int) chunk.getPosition().z);
            Chunk neighborXPos = chunks.get(new Vector2i(pos.x + Chunk.CHUNK_SIZE, pos.y));
            Chunk neighborXNeg = chunks.get(new Vector2i(pos.x - Chunk.CHUNK_SIZE, pos.y));
            Chunk neighborZPos = chunks.get(new Vector2i(pos.x, pos.y + Chunk.CHUNK_SIZE));
            Chunk neighborZNeg = chunks.get(new Vector2i(pos.x, pos.y - Chunk.CHUNK_SIZE));
            chunk.generateMeshData(neighborXPos, neighborXNeg, neighborZPos, neighborZNeg);
        }
        newChunks.clear();
    }

    public void cleanup() {
        for (Chunk chunk : chunks.values()) {
            chunk.cleanup();
        }
        shaderProgram.destroy();
    }
}
