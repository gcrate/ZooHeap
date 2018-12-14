package com.grahamcrate.zooheap.geometery;

import com.grahamcrate.zooheap.util.ArrayShapeUtils;
import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author grahamcrate
 */
public class Shape {

    public final char shapeId;
    private final boolean[][] shape;
    private final boolean[][] shape90;
    private final boolean[][] shape180;
    private final boolean[][] shape270;

    public Shape(char shapeId, boolean[][] shape) {
        this.shapeId = shapeId;
        this.shape = shape;

        this.shape90 = ArrayShapeUtils.copyAndRotate90(shape);
        this.shape180 = ArrayShapeUtils.copyAndRotate180(shape);
        this.shape270 = ArrayShapeUtils.copyAndRotate180(shape90);
    }

    public boolean[][] getArea(Rotation rotation) {
        switch (rotation) {
            case ROTATED_0:
                return shape;
            case ROTATED_90:
                return shape90;
            case ROTATED_180:
                return shape180;
            case ROTATED_270: 
                return shape270;
        }
        return shape;
    }
}
