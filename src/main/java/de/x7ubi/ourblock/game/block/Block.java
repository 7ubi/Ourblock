package de.x7ubi.ourblock.game.block;

import de.x7ubi.ourblock.game.texture.TextureUVRecord;
import org.joml.Vector3d;

import java.util.Arrays;
import java.util.List;

public abstract class Block {

    protected TextureUVRecord textureUVRecord;

    protected TextureUVRecord topUVRecord = null;
    protected TextureUVRecord sideUVRecord = null;
    protected TextureUVRecord bottomUVRecord = null;

    public Block(TextureUVRecord textureUVRecord) {
        this.textureUVRecord = textureUVRecord;
    }

    public Block(TextureUVRecord topUVRecord, TextureUVRecord sideUVRecord, TextureUVRecord bottomUVRecord) {
        this.topUVRecord = topUVRecord;
        this.sideUVRecord = sideUVRecord;
        this.bottomUVRecord = bottomUVRecord;
    }

    public void generateMeshData(Vector3d position, List<Faces> facesToDraw, MeshData meshData) {
        generateTopFace(position, facesToDraw, meshData);
        generateBottomFace(position, facesToDraw, meshData);
        generateBackFace(position, facesToDraw, meshData);
        generateFrontFace(position, facesToDraw, meshData);
        generateLeftFace(position, facesToDraw, meshData);
        generateRightFace(position, facesToDraw, meshData);
    }

    private void generateTopFace(Vector3d position, List<Faces> facesToDraw, MeshData meshData) {
        if (facesToDraw.contains(Faces.TOP)) {
            Vector3d topLeft = new Vector3d(position.x - 0.5, position.y + 0.5, position.z + 0.5);
            Vector3d topRight = new Vector3d(position.x + 0.5, position.y + 0.5, position.z + 0.5);
            Vector3d bottomLeft = new Vector3d(position.x - 0.5, position.y + 0.5, position.z - 0.5);
            Vector3d bottomRight = new Vector3d(position.x + 0.5, position.y + 0.5, position.z - 0.5);

            TextureUVRecord uvRecord = topUVRecord != null ? topUVRecord : textureUVRecord;

            generateFaceData(topLeft, topRight, bottomLeft, bottomRight, uvRecord, new Vector3d(0, 1, 0), meshData);
        }
    }

    private void generateBottomFace(Vector3d position, List<Faces> facesToDraw, MeshData meshData) {
        if (facesToDraw.contains(Faces.BOTTOM)) {
            Vector3d topLeft = new Vector3d(position.x - 0.5, position.y - 0.5, position.z - 0.5);
            Vector3d topRight = new Vector3d(position.x + 0.5, position.y - 0.5, position.z - 0.5);
            Vector3d bottomLeft = new Vector3d(position.x - 0.5, position.y - 0.5, position.z + 0.5);
            Vector3d bottomRight = new Vector3d(position.x + 0.5, position.y - 0.5, position.z + 0.5);

            TextureUVRecord uvRecord = bottomUVRecord != null ? bottomUVRecord : textureUVRecord;

            generateFaceData(topLeft, topRight, bottomLeft, bottomRight, uvRecord, new Vector3d(0, -1, 0), meshData);
        }
    }

    private void generateBackFace(Vector3d position, List<Faces> facesToDraw, MeshData meshData) {
        if (facesToDraw.contains(Faces.BACK)) {
            Vector3d topLeft = new Vector3d(position.x - 0.5, position.y + 0.5, position.z - 0.5);
            Vector3d topRight = new Vector3d(position.x + 0.5, position.y + 0.5, position.z - 0.5);
            Vector3d bottomLeft = new Vector3d(position.x - 0.5, position.y - 0.5, position.z - 0.5);
            Vector3d bottomRight = new Vector3d(position.x + 0.5, position.y - 0.5, position.z - 0.5);

            TextureUVRecord uvRecord = sideUVRecord != null ? sideUVRecord : textureUVRecord;

            generateFaceData(topLeft, topRight, bottomLeft, bottomRight, uvRecord, new Vector3d(0, 0, -1), meshData);
        }
    }

    private void generateFrontFace(Vector3d position, List<Faces> facesToDraw, MeshData meshData) {
        if (facesToDraw.contains(Faces.FRONT)) {
            Vector3d topLeft = new Vector3d(position.x + 0.5, position.y + 0.5, position.z + 0.5);
            Vector3d topRight = new Vector3d(position.x - 0.5, position.y + 0.5, position.z + 0.5);
            Vector3d bottomLeft = new Vector3d(position.x + 0.5, position.y - 0.5, position.z + 0.5);
            Vector3d bottomRight = new Vector3d(position.x - 0.5, position.y - 0.5, position.z + 0.5);

            TextureUVRecord uvRecord = sideUVRecord != null ? sideUVRecord : textureUVRecord;

            generateFaceData(topLeft, topRight, bottomLeft, bottomRight, uvRecord, new Vector3d(0, 0, 1), meshData);
        }
    }

    private void generateLeftFace(Vector3d position, List<Faces> facesToDraw, MeshData meshData) {
        if (facesToDraw.contains(Faces.LEFT)) {
            Vector3d topLeft = new Vector3d(position.x - 0.5, position.y + 0.5, position.z + 0.5);
            Vector3d topRight = new Vector3d(position.x - 0.5, position.y + 0.5, position.z - 0.5);
            Vector3d bottomLeft = new Vector3d(position.x - 0.5, position.y - 0.5, position.z + 0.5);
            Vector3d bottomRight = new Vector3d(position.x - 0.5, position.y - 0.5, position.z - 0.5);

            TextureUVRecord uvRecord = sideUVRecord != null ? sideUVRecord : textureUVRecord;

            generateFaceData(topLeft, topRight, bottomLeft, bottomRight, uvRecord, new Vector3d(-1, 0, 0), meshData);
        }
    }

    private void generateRightFace(Vector3d position, List<Faces> facesToDraw, MeshData meshData) {
        if (facesToDraw.contains(Faces.RIGHT)) {
            Vector3d topLeft = new Vector3d(position.x + 0.5, position.y + 0.5, position.z - 0.5);
            Vector3d topRight = new Vector3d(position.x + 0.5, position.y + 0.5, position.z + 0.5);
            Vector3d bottomLeft = new Vector3d(position.x + 0.5, position.y - 0.5, position.z - 0.5);
            Vector3d bottomRight = new Vector3d(position.x + 0.5, position.y - 0.5, position.z + 0.5);

            TextureUVRecord uvRecord = sideUVRecord != null ? sideUVRecord : textureUVRecord;

            generateFaceData(topLeft, topRight, bottomLeft, bottomRight, uvRecord, new Vector3d(1, 0, 0), meshData);
        }
    }

    private void generateFaceData(Vector3d topLeftPoint, Vector3d topRightPoint, Vector3d bottomLeftPoint,
                                  Vector3d bottomRightPoint, TextureUVRecord uvRecord, Vector3d normal, MeshData meshData) {
        meshData.getVertices().addAll(Arrays.asList(
                topLeftPoint.x, topLeftPoint.y, topLeftPoint.z, uvRecord.getTopLeft().x, uvRecord.getTopLeft().y, normal.x, normal.y, normal.z,
                topRightPoint.x, topRightPoint.y, topRightPoint.z, uvRecord.getTopRight().x, uvRecord.getTopRight().y, normal.x, normal.y, normal.z,
                bottomLeftPoint.x, bottomLeftPoint.y, bottomLeftPoint.z, uvRecord.getBottomLeft().x, uvRecord.getBottomLeft().y, normal.x, normal.y, normal.z,
                bottomRightPoint.x, bottomRightPoint.y, bottomRightPoint.z, uvRecord.getBottomRight().x, uvRecord.getBottomRight().y, normal.x, normal.y, normal.z
        ));

        int baseIndex = (meshData.getVertices().size() / MeshData.STRIDE_FLOATS) - 4;

        meshData.getIndices().addAll(List.of(
                baseIndex + 3, baseIndex + 2, baseIndex,
                baseIndex + 1, baseIndex + 3, baseIndex
        ));
    }
}