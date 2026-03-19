package de.x7ubi.ourblock.game.block;

import de.x7ubi.ourblock.game.texture.TextureUVRecord;
import org.joml.Vector2d;
import org.joml.Vector3d;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public abstract class Block {

    protected TextureUVRecord textureUVRecord;

    public Block(TextureUVRecord textureUVRecord) {
        this.textureUVRecord = textureUVRecord;
    }

    public void render(Vector3d position, List<Faces> facesToDraw) {

        glPushMatrix();

        glTranslated(position.x, position.y, position.z);

        glBegin(GL_TRIANGLES);
        renderFrontFace(facesToDraw);
        renderBackFace(facesToDraw);
        renderLeftFace(facesToDraw);
        renderRightFace(facesToDraw);
        renderTopFace(facesToDraw);
        renderBottomFace(facesToDraw);
        glEnd();

        glPopMatrix();
    }

    private void renderFrontFace(List<Faces> facesToDraw) {
        if (facesToDraw.contains(Faces.FRONT)) {
            face(new Vector3d(-0.5, -0.5, 0.5), new Vector3d(0.5, -0.5, 0.5), new Vector3d(-0.5, 0.5, 0.5), new Vector3d(0.5, 0.5, 0.5), textureUVRecord);
        }
    }

    private void renderBackFace(List<Faces> facesToDraw) {
        if (facesToDraw.contains(Faces.BACK)) {
            face(new Vector3d(0.5, -0.5, -0.5), new Vector3d(-0.5, -0.5, -0.5), new Vector3d(0.5, 0.5, -0.5), new Vector3d(-0.5, 0.5, -0.5), textureUVRecord);
        }
    }

    private void renderLeftFace(List<Faces> facesToDraw) {
        if (facesToDraw.contains(Faces.LEFT)) {
            face(new Vector3d(-0.5, -0.5, -0.5), new Vector3d(-0.5, -0.5, 0.5), new Vector3d(-0.5, 0.5, -0.5), new Vector3d(-0.5, 0.5, 0.5), textureUVRecord);
        }
    }

    private void renderRightFace(List<Faces> facesToDraw) {
        if (facesToDraw.contains(Faces.RIGHT)) {
            face(new Vector3d(0.5, -0.5, 0.5), new Vector3d(0.5, -0.5, -0.5), new Vector3d(0.5, 0.5, 0.5), new Vector3d(0.5, 0.5, -0.5), textureUVRecord);
        }
    }

    private void renderTopFace(List<Faces> facesToDraw) {
        if (facesToDraw.contains(Faces.TOP)) {
            face(new Vector3d(-0.5, 0.5, -0.5), new Vector3d(-0.5, 0.5, 0.5), new Vector3d(0.5, 0.5, -0.5), new Vector3d(0.5, 0.5, 0.5), textureUVRecord);
        }
    }

    private void renderBottomFace(List<Faces> facesToDraw) {
        if (facesToDraw.contains(Faces.BOTTOM)) {
            face(new Vector3d(-0.5, -0.5, -0.5), new Vector3d(0.5, -0.5, -0.5), new Vector3d(-0.5, -0.5, 0.5), new Vector3d(0.5, -0.5, 0.5), textureUVRecord);
        }
    }

    private void face(Vector3d topLeftPoint, Vector3d topRightPoint, Vector3d bottomLeftPoint,
                      Vector3d bottomRightPoint, TextureUVRecord textureUVRecord) {

        vertex(bottomRightPoint, textureUVRecord.getBottomRight());
        vertex(bottomLeftPoint, textureUVRecord.getBottomLeft());
        vertex(topLeftPoint, textureUVRecord.getTopLeft());

        vertex(topRightPoint, textureUVRecord.getTopRight());
        vertex(bottomRightPoint, textureUVRecord.getBottomRight());
        vertex(topLeftPoint, textureUVRecord.getTopLeft());
    }

    private void vertex(Vector3d point, Vector2d uv) {
        glTexCoord2d(uv.x, uv.y);
        glVertex3d(point.x, point.y, point.z);
    }
}
