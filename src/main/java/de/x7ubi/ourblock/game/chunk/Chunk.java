package de.x7ubi.ourblock.game.chunk;

import de.x7ubi.ourblock.game.block.BlockDictionary;
import de.x7ubi.ourblock.game.block.Faces;
import lombok.Getter;
import org.joml.Vector3d;

import java.util.List;

import static org.lwjgl.opengl.GL11.glTranslated;

@Getter
public class Chunk {

    public static final int CHUNK_SIZE = 16;

    private final Vector3d position;

    private byte[] blocks = new byte[CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE];

    public Chunk(Vector3d position) {
        this.position = position;

        generateBlocks();
    }

    private void generateBlocks() {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    int index = x + CHUNK_SIZE * (y + CHUNK_SIZE * z);
                    if (y < CHUNK_SIZE / 2) {
                        blocks[index] = 1;
                        if (y < CHUNK_SIZE / 4) {
                            blocks[index] = 2;
                        }
                    } else {
                        blocks[index] = 0;
                    }
                }
            }
        }
    }

    public void render() {
        glTranslated(position.x, position.y, position.z);

        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    int index = x + CHUNK_SIZE * (y + CHUNK_SIZE * z);
                    if (blocks[index] != 0) {
                        List<Faces> facesToDraw = getFacesToDraw(x, y, z);

                        BlockDictionary.getBlockById(blocks[index]).render(new Vector3d(x, y, z), facesToDraw);
                    }
                }
            }
        }
    }

    private List<Faces> getFacesToDraw(int x, int y, int z) {
        List<Faces> facesToDraw = new java.util.ArrayList<>();

        if (y == 0 || blocks[x + CHUNK_SIZE * ((y - 1) + CHUNK_SIZE * z)] == 0) {
            facesToDraw.add(Faces.BOTTOM);
        }
        if (y == CHUNK_SIZE - 1 || blocks[x + CHUNK_SIZE * ((y + 1) + CHUNK_SIZE * z)] == 0) {
            facesToDraw.add(Faces.TOP);
        }
        if (x == 0 || blocks[(x - 1) + CHUNK_SIZE * (y + CHUNK_SIZE * z)] == 0) {
            facesToDraw.add(Faces.LEFT);
        }
        if (x == CHUNK_SIZE - 1 || blocks[(x + 1) + CHUNK_SIZE * (y + CHUNK_SIZE * z)] == 0) {
            facesToDraw.add(Faces.RIGHT);
        }
        if (z == CHUNK_SIZE - 1 || blocks[x + CHUNK_SIZE * (y + CHUNK_SIZE * (z + 1))] == 0) {
            facesToDraw.add(Faces.FRONT);
        }
        if (z == 0 || blocks[x + CHUNK_SIZE * (y + CHUNK_SIZE * (z - 1))] == 0) {
            facesToDraw.add(Faces.BACK);
        }

        return facesToDraw;
    }
}
