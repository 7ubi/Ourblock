package de.x7ubi.ourblock.game.chunk;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.core.api.noisegen.NoiseGenerator;
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator;
import lombok.Getter;
import org.joml.Vector2d;
import org.joml.Vector3d;

import java.util.HashMap;

public class ChunkGeneration {

    @Getter
    private static final ChunkGeneration instance = new ChunkGeneration();

    private static final long SEED = 12345;

    private final NoiseGenerator noiseGenerator = PerlinNoiseGenerator.newBuilder().setSeed(SEED).setInterpolation(Interpolation.COSINE).build();

    private final HashMap<Vector2d, Chunk> chunks = new HashMap<>();

    private ChunkGeneration() {
        initialize();
    }

    public void initialize() {
        for (int x = 0; x < 4; x++) {
            for (int z = 0; z < 4; z++) {
                chunks.put(new Vector2d(x * Chunk.CHUNK_SIZE, z * Chunk.CHUNK_SIZE),
                        new Chunk(new Vector3d(x * Chunk.CHUNK_SIZE, 0, z * Chunk.CHUNK_SIZE), noiseGenerator));
            }
        }
    }

    public void render() {
        for (Vector2d chunkPos : chunks.keySet()) {
            Chunk chunk = chunks.get(chunkPos);
            Chunk neighborXPos = chunks.get(new Vector2d(chunkPos.x + Chunk.CHUNK_SIZE, chunkPos.y));
            Chunk neighborXNeg = chunks.get(new Vector2d(chunkPos.x - Chunk.CHUNK_SIZE, chunkPos.y));
            Chunk neighborZPos = chunks.get(new Vector2d(chunkPos.x, chunkPos.y + Chunk.CHUNK_SIZE));
            Chunk neighborZNeg = chunks.get(new Vector2d(chunkPos.x, chunkPos.y - Chunk.CHUNK_SIZE));
            chunk.render(neighborXPos, neighborXNeg, neighborZPos, neighborZNeg);
        }
    }
}
