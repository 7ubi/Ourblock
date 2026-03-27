package de.x7ubi.ourblock.game.chunk;

import de.articdive.jnoise.core.api.noisegen.NoiseGenerator;
import de.x7ubi.ourblock.engine.ShaderProgram;
import de.x7ubi.ourblock.game.block.BlockDictionary;
import de.x7ubi.ourblock.game.block.Faces;
import de.x7ubi.ourblock.game.block.MeshData;
import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector3d;

import java.util.List;

@Getter
public class Chunk {

    public static final int CHUNK_SIZE = 16;

    public static final int MAX_CHUNK_HEIGHT = 256;

    private static final int MAX_GENERATION_HEIGHT = 80;

    private static final int MIN_CHUNK_HEIGHT = 60;

    private static final double frequency = 0.03;

    @Getter
    private final byte[] blocks = new byte[CHUNK_SIZE * CHUNK_SIZE * MAX_CHUNK_HEIGHT];

    private final Vector3d position;

    private final NoiseGenerator noiseGenerator;

    private final MeshData meshData = new MeshData();

    private final ChunkMesh chunkMesh = new ChunkMesh();

    public Chunk(Vector3d position, NoiseGenerator noiseGenerator) {
        this.position = position;
        this.noiseGenerator = noiseGenerator;

        generateBlocks();
    }

    private void generateBlocks() {

        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                double noiseValue = noiseGenerator.evaluateNoise((position.x + x) * frequency, (position.z + z) * frequency);
                noiseValue = (noiseValue + 1) / 2;
                int dirtHeight = (int) (MIN_CHUNK_HEIGHT + noiseValue * (MAX_GENERATION_HEIGHT - MIN_CHUNK_HEIGHT));
                for (int y = 0; y < MAX_CHUNK_HEIGHT; y++) {
                    int index = getBlockIndex(x, y, z);

                    if (y < dirtHeight) {
                        blocks[index] = y < dirtHeight - 4 ? (byte) 2 : 1;

                    } else {
                        blocks[index] = 0;
                    }
                }
            }
        }
    }

    public void generateMeshData(Chunk neighborXPos, Chunk neighborXNeg, Chunk neighborZPos, Chunk neighborZNeg) {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < MAX_CHUNK_HEIGHT; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    byte block = blocks[getBlockIndex(x, y, z)];
                    if (block != 0) {
                        List<Faces> facesToDraw = getFacesToDraw(x, y, z, neighborXPos, neighborXNeg, neighborZPos, neighborZNeg);

                        if (!facesToDraw.isEmpty()) {
                            Vector3d relativePos = new Vector3d(x, y, z).add(position);
                            BlockDictionary.getBlockById(block).generateMeshData(relativePos, facesToDraw, meshData);
                        }
                    }
                }
            }
        }


        chunkMesh.upload(meshData.getVerticesAsFloatBuffer(), meshData.getIndicesAsIntBuffer());
    }

    public void render(ShaderProgram shaderProgram) {
        shaderProgram.setMat4("uModel", new Matrix4f().identity());
        chunkMesh.draw();
    }

    private List<Faces> getFacesToDraw(int x, int y, int z, Chunk neighborXPos, Chunk neighborXNeg, Chunk neighborZPos, Chunk neighborZNeg) {
        List<Faces> facesToDraw = new java.util.ArrayList<>();

        if (y == 0 || blocks[getBlockIndex(x, y - 1, z)] == 0) {
            facesToDraw.add(Faces.BOTTOM);
        }
        if (y == MAX_CHUNK_HEIGHT - 1 || blocks[getBlockIndex(x, y + 1, z)] == 0) {
            facesToDraw.add(Faces.TOP);
        }

        if (x == 0) {
            if (neighborXNeg == null || neighborXNeg.blocks[getBlockIndex(CHUNK_SIZE - 1, y, z)] == 0) {
                facesToDraw.add(Faces.LEFT);
            }
        } else if (blocks[getBlockIndex(x - 1, y, z)] == 0) {
            facesToDraw.add(Faces.LEFT);
        }

        if (x == CHUNK_SIZE - 1) {
            if (neighborXPos == null || neighborXPos.blocks[getBlockIndex(0, y, z)] == 0) {
                facesToDraw.add(Faces.RIGHT);
            }
        } else if (blocks[getBlockIndex(x + 1, y, z)] == 0) {
            facesToDraw.add(Faces.RIGHT);
        }

        if (z == CHUNK_SIZE - 1) {
            if (neighborZPos == null || neighborZPos.blocks[getBlockIndex(x, y, 0)] == 0) {
                facesToDraw.add(Faces.FRONT);
            }
        } else if (blocks[getBlockIndex(x, y, z + 1)] == 0) {
            facesToDraw.add(Faces.FRONT);
        }

        if (z == 0) {
            if (neighborZNeg == null || neighborZNeg.blocks[getBlockIndex(x, y, CHUNK_SIZE - 1)] == 0) {
                facesToDraw.add(Faces.BACK);
            }
        } else if (blocks[getBlockIndex(x, y, z - 1)] == 0) {
            facesToDraw.add(Faces.BACK);
        }

        return facesToDraw;
    }

    private int getBlockIndex(int x, int y, int z) {
        return (z * CHUNK_SIZE * MAX_CHUNK_HEIGHT) + (y * CHUNK_SIZE) + x;
    }

    public void clearMeshData() {
        meshData.getVertices().clear();
        meshData.getIndices().clear();
    }

    public void cleanup() {
        chunkMesh.cleanup();
    }
}
