package com.grahamcrate.zooheap.geometery;

import com.grahamcrate.zooheap.util.ArrayShapeUtils;
import com.grahamcrate.zooheap.exception.DoesntFitException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author grahamcrate
 */
public class PlayArea {
    private final Set<Shape> usedShapes;
    private boolean[][] area;
    
    private boolean[][] updatedArea;
    private Shape updateShape;
    
    private Coordinates firstEmptySpace;
    

    public PlayArea(int xLen, int yLen) {
        area = new boolean[xLen][yLen];
        usedShapes = new HashSet<>();
        calcFirstEmptySpace();
    }
    
    public PlayArea(boolean[][] area) {
        this.area = area;
        usedShapes = new HashSet<>();
        calcFirstEmptySpace();
    }

    public Set<Shape> getUsedShapes() {
        return usedShapes;
    }

    public boolean addShapeTransactional(ShapeRotationCombo src) {
        try {
            addShape(firstEmptySpace, src.shape, src.rotation);
        } catch(DoesntFitException ex) {
            return false;
        }
        return true;
    }
    
    public void commit() {
        area = updatedArea;
        usedShapes.add(updateShape);
        calcFirstEmptySpace();
    }
    
    private void addShape(Coordinates addAt, Shape shape, Rotation rotation) throws DoesntFitException {
        updatedArea = ArrayShapeUtils.makeCopy(area);
        updateShape = shape;
        //this.usedShapes.add(shape);
        
        boolean[][] shapeArea = shape.getArea(rotation);
        
        //find first point on first row
        boolean[] shapeFirstRow = shapeArea[0];
        int offsetCol = 0;
        while (!shapeFirstRow[offsetCol]) {
            offsetCol++;
        }
        
        boolean first = true;
        int colZero = addAt.col - offsetCol;
        int rowZero = addAt.row;
        for (int row = 0; row < shapeArea.length; row++) {
            for (int col = first ? offsetCol : 0; col < shapeFirstRow.length; col++) {
                if (shapeArea[row][col]) {
                    // find where it goes
                    try {
                        if(updatedArea[row + rowZero][col + colZero]) {
                            throw new DoesntFitException();
                        }
                        updatedArea[row + rowZero][col + colZero] = true;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new DoesntFitException();
                    }
                }
            }
            first = false;
        }
        
        //ArrayShapeUtils.print(updatedArea);
        
    }

    public boolean[][] getArea() {
        return area;
    }
    
    public boolean isFull() {
        return firstEmptySpace == null;
    }
    
    
    private void calcFirstEmptySpace() {
        for (int y = 0; y < area.length; y++) {
            boolean[] row = area[y];
            for (int x = 0; x < row.length; x++) {
                if(!row[x]) {
                    firstEmptySpace = new Coordinates(y, x);
                    return;
                }
            }
        }
        firstEmptySpace = null;
    }

    public PlayArea clone() {
        PlayArea clone = new PlayArea(area);
        clone.getUsedShapes().addAll(this.usedShapes);
        clone.firstEmptySpace = this.firstEmptySpace;
        return clone;
    }
    
    
}
