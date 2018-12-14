package com.grahamcrate.zooheap.compute;

import com.grahamcrate.zooheap.geometery.PlayArea;
import com.grahamcrate.zooheap.geometery.ShapeRotationCombo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author grahamcrate
 */
public class SolutionState {
    public final List<ShapeRotationCombo> leftToTry;
    public final PlayArea playArea;

    public SolutionState(List<ShapeRotationCombo> leftToTry, PlayArea playArea) {
        this.leftToTry = new ArrayList(leftToTry);
        this.playArea = playArea.clone();
    }
    
    
}
