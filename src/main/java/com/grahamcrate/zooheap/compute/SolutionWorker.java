package com.grahamcrate.zooheap.compute;

import com.grahamcrate.zooheap.ZooHeapApp;
import com.grahamcrate.zooheap.geometery.PlayArea;
import com.grahamcrate.zooheap.geometery.ShapeRotationCombo;
import com.grahamcrate.zooheap.util.ArrayShapeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 *
 * @author grahamcrate
 */
public class SolutionWorker implements Runnable {

    private List<ShapeRotationCombo> combosToTry;
    private PlayArea playArea;

    private Stack<SolutionState> previousStates = new Stack<>();

    public boolean run = true;

    /**
     *
     * @param playArea - Play area with one shape in already?
     */
    public SolutionWorker(PlayArea playArea) {
        this.playArea = playArea;
    }

    @Override
    public void run() {
        combosToTry = new ArrayList(ZooHeapApp.combos);
        combosToTry = ZooHeapApp.combos
                        .stream()
                        .filter(x -> !playArea.getUsedShapes().contains(x.shape))
                        .collect(Collectors.toList());
        
        while (!tryAddingShapes() && !ZooHeapApp.isSolutionFound()) {
            if (!previousStates.isEmpty()) {
                restorePreviousState();
            } else {
                return;
            }
        }
    }

    public boolean tryAddingShapes() {
        while (!combosToTry.isEmpty()) {
            ShapeRotationCombo combo = combosToTry.get(0);
            combosToTry.remove(combo);
            if (playArea.addShapeTransactional(combo)) {
                saveState();
                playArea.commit();
                if (playArea.isFull()) {
                    ZooHeapApp.solutionFound(
                            playArea.getUsedShapes().size(),
                            ArrayShapeUtils.arrayToString(playArea.getArea()));
                    return true;
                }

                combosToTry = ZooHeapApp.combos
                        .stream()
                        .filter(x -> !playArea.getUsedShapes().contains(x.shape))
                        .collect(Collectors.toList());
            }

        }
        return false;
    }

    private void restorePreviousState() {
        SolutionState prevState = previousStates.pop();
        this.combosToTry = prevState.leftToTry;
        this.playArea = prevState.playArea;
    }

    private void saveState() {
        previousStates.push(new SolutionState(combosToTry, playArea));
    }

}
