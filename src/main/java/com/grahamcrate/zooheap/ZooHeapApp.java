package com.grahamcrate.zooheap;

import com.grahamcrate.zooheap.compute.SolutionWorker;
import com.grahamcrate.zooheap.geometery.PlayArea;
import com.grahamcrate.zooheap.geometery.Rotation;
import com.grahamcrate.zooheap.geometery.Shape;
import com.grahamcrate.zooheap.geometery.ShapeRotationCombo;
import com.grahamcrate.zooheap.util.ArrayShapeUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author grahamcrate
 */
public class ZooHeapApp {

    private final static List<Shape> shapes = new ArrayList<>();
    public static final Set<ShapeRotationCombo> combos = new HashSet<>();
    private static char[][] playAreaShape;

    private static boolean solutionFound = false;
    private static int shapesUsed = 0;
    
    private static String solution;
    
    private static boolean showSolution = false;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        //read in files
        if (args.length < 2) {
            System.out.println("usage: java -jar ZooHeap.jar [shapesFilename] [playareaFilename]");
            System.exit(1);
        }

        File shapeFile = new File(args[0]);
        File areaFile = new File(args[1]);
        
        if (args.length == 3) {
            if (args[2].equalsIgnoreCase("showsolution")) {
                showSolution = true;
            }
        }

        loadShapesFromFile(shapeFile);
        loadAreaFromFile(areaFile);

        for (Shape shape : shapes) {
            combos.add(new ShapeRotationCombo(shape, Rotation.ROTATED_0));
            combos.add(new ShapeRotationCombo(shape, Rotation.ROTATED_90));
            combos.add(new ShapeRotationCombo(shape, Rotation.ROTATED_180));
            combos.add(new ShapeRotationCombo(shape, Rotation.ROTATED_270));
        }

        ExecutorService executorSvc = Executors.newFixedThreadPool(shapes.size() < 5 ? shapes.size() : 5);
        for (ShapeRotationCombo combo : combos) {
            PlayArea pa = new PlayArea(playAreaShape);
            if (pa.addShapeTransactional(combo)) {
                pa.commit();
                SolutionWorker solutionWorker = new SolutionWorker(pa);
                executorSvc.execute(solutionWorker);
            }
        }

        executorSvc.shutdown();

        executorSvc.awaitTermination(10, TimeUnit.MINUTES);

        System.out.println("Success:" + isSolutionFound());
        if (isSolutionFound()) {
            System.out.println(shapesUsed + " shapes used");
            if(showSolution) {
                System.out.println(solution);
            }
        }
    }

    public static void solutionFound(int numOfShapes, String sol) {
        solutionFound = true;
        shapesUsed = numOfShapes;
        solution = sol;
    }

    public static boolean isSolutionFound() {
        return solutionFound;
    }

    public static List<Shape> getShapes() {
        return shapes;
    }

    private static void loadAreaFromFile(File areaFile) throws IOException {
        List<String> areaData = FileUtils.readLines(areaFile, Charset.defaultCharset());
        List<char[]> area = new ArrayList<>();

        for (String areaDataRow : areaData) {
            // TODO: validate all rows are the same size
            char[] areaRow = new char[areaDataRow.length()];
            for (int i = 0; i < areaDataRow.length(); i++) {
                areaRow[i] = areaDataRow.charAt(i) == 'X' ? 'X' : '\u0000';
            }
            area.add(areaRow);
        }

        playAreaShape = new char[area.size()][area.get(0).length];
        
        area.toArray(playAreaShape);
        
        ArrayShapeUtils.print(playAreaShape);
    }

    private static void loadShapesFromFile(File shapeFile) throws IOException {
        List<String> shapesData = FileUtils.readLines(shapeFile, Charset.defaultCharset());
        List<boolean[]> currentShape = new ArrayList<>();

        int shapeRowLength = 0;
        //setup first shape array
        int idCnt = 1;
        for (String dataLine : shapesData) {
            if (dataLine.equals("")) {
                boolean[][] shapeArray = new boolean[currentShape.size()][shapeRowLength];
                shapes.add(new Shape(getCharForNumber(idCnt++), currentShape.toArray(shapeArray)));
                currentShape = new ArrayList<>();
            } else {
                shapeRowLength = dataLine.length();
                boolean[] shapeRow = new boolean[shapeRowLength];
                for (int i = 0; i < dataLine.length(); i++) {
                    shapeRow[i] = dataLine.charAt(i) == 'X';
                }
                currentShape.add(shapeRow);
            }
        }
    }

    private static char getCharForNumber(int i) {
        return i > 0 && i < 27 ? (char) (i + 64) : null;
    }
}
