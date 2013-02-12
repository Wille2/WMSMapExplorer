package mapsoftware.wms.domain;

import java.util.Arrays;

/**
 * <p>
 * Container for the location information
 * </p>
 * 
 * @author Ville Ahti
 * 
 */
public class LocationInformation {
    // TODO: Comments
    public enum Operation {
        LEFT, RIGHT, UP, DOWN, ZOOM_IN, ZOOM_OUT;
    }

    /**
     * <p>
     * Constructor for the class. Parameters form two coordinates: (minX, minY)
     * and (maxX, maxY), which are formatted in to a single string.
     * </p>
     * 
     * @param minX
     *            Double representing minimum X coordinate
     * @param minY
     *            Double representing minimum Y coordinate
     * @param maxX
     *            Double representing maximum X coordinate
     * @param maxY
     *            Double representing maximum Y coordinate
     * 
     */
    public LocationInformation(Double minX, Double minY, Double maxX,
            Double maxY) {
        maxCoords = new double[4];
        this.maxCoords[MIN_X] = minX;
        this.maxCoords[MIN_Y] = minY;
        this.maxCoords[MAX_X] = maxX;
        this.maxCoords[MAX_Y] = maxY;
        currentCoords = maxCoords.clone();
        this.angle = (maxY - minY) / (maxX - minX);
        zoomLevel = 9;
        countDistances();
        updateMovement();
    }

    /**
     * <p>
     * Getter for the minimum X coordinate.
     * </p>
     * 
     * @return Double value representing minX coordinate.
     */
    public double getMinX() {
        return this.maxCoords[0];
    }

    /**
     * <p>
     * Getter for the minimum Y coordinate.
     * </p>
     * 
     * @return Double value representing minY coordinate.
     */
    public double getMinY() {
        return this.maxCoords[1];
    }

    /**
     * <p>
     * Getter for the maximum X coordinate.
     * </p>
     * 
     * @return Double value representing maxX coordinate.
     */
    public double getMaxX() {
        return this.maxCoords[2];
    }

    /**
     * <p>
     * Getter for the maximum Y coordinate.
     * </p>
     * 
     * @return Double value representing maxY coordinate.
     */
    public double getMaxY() {
        return this.maxCoords[3];
    }

    /**
     * <p>
     * Getter for the area coordinates.
     * </p>
     * 
     * @return String containing coordinates minX, minY, maxX and maxY.
     */
    public String getCurrentCoordinantsAsString() {
        String coordinants = "";
        for (double coord : currentCoords) {
            coordinants += coord + ",";
        }
        // Removes last comma
        return coordinants.substring(0, coordinants.length() - 1);
    }

    // TODO: mayby 20 level zoom
    /**
     * <p>
     * Changes the coordinates according to which parameter is given.
     * </p>
     * 
     * @param operation
     *            String defining in what direction the coordinates should be
     *            changed.
     */
    public void move(Operation operation) {
    	int lastZoomLevel = zoomLevel;
        updateMovement();
        switch (operation) {
        case LEFT:
            currentCoords[MIN_X] -= longMovement;
            currentCoords[MAX_X] -= longMovement;
            break;
        case RIGHT:
            currentCoords[MIN_X] += longMovement;
            currentCoords[MAX_X] += longMovement;
            break;
        case UP:
            currentCoords[MIN_Y] += latMovement;
            currentCoords[MAX_Y] += latMovement;
            break;
        case DOWN:
            currentCoords[MIN_Y] -= latMovement;
            currentCoords[MAX_Y] -= latMovement;
            break;
        case ZOOM_IN:
        	zoomLevel--;
            doZoom(lastZoomLevel);
            break;
        case ZOOM_OUT:
            zoomLevel++;
            doZoom(lastZoomLevel);
            fixCoordinates();
            break;
        }
    }
    
    @Override
    public String toString() {
        return "LocationInformation [maxCoords=" + Arrays.toString(maxCoords)
                + ", currentCoords=" + Arrays.toString(currentCoords)
                + ", angle=" + angle + "]";
    }

	private void fixCoordinates() {
		// fixes coordinates if over max
		double[] coordsFixes = new double[4];
		if (currentCoords[MIN_X] < maxCoords[MIN_X]) {
		    coordsFixes[MIN_X] = maxCoords[MIN_X] - currentCoords[MIN_X];
		    currentCoords[MIN_X] = maxCoords[MIN_X];
		}
		if (currentCoords[MIN_Y] < maxCoords[MIN_Y]) {
		    coordsFixes[MIN_Y] = maxCoords[MIN_Y] - currentCoords[MIN_Y];
		    currentCoords[MIN_Y] = maxCoords[MIN_Y];
		}

		if (currentCoords[MAX_X] > maxCoords[MAX_X]) {
		    coordsFixes[MAX_X] = maxCoords[MAX_X] - currentCoords[MAX_X];
		    currentCoords[MAX_X] = maxCoords[MAX_X];
		}
		if (currentCoords[MIN_X] > maxCoords[MAX_X]) {
		    coordsFixes[MAX_Y] = maxCoords[MAX_Y] - currentCoords[MAX_Y];
		    currentCoords[MAX_Y] = maxCoords[MAX_Y];
		}

		currentCoords[MIN_X] += coordsFixes[MAX_X];
		currentCoords[MIN_Y] += coordsFixes[MAX_Y];
		currentCoords[MAX_X] += coordsFixes[MIN_X];
		currentCoords[MAX_Y] += coordsFixes[MIN_Y];
	}

	private void doZoom(int lastZoomLevel) {
		// difference between current and becoming distance between points
		// min(x,y) and max(x,y)
		double currentLatZoom = zoomLatDistances[lastZoomLevel]
		        - zoomLatDistances[zoomLevel];
		double currentLongZoom = zoomLongDistances[lastZoomLevel]
		        - zoomLongDistances[zoomLevel];
		// calculating new points (moves both points half the wanted
		// distance closer to each other)
		currentCoords[MAX_X] = currentCoords[MIN_X]
		        + (zoomLongDistances[zoomLevel] - (currentLongZoom / 2));
		currentCoords[MAX_Y] = currentCoords[MIN_Y]
		        + (zoomLatDistances[zoomLevel] - (currentLatZoom / 2));

		currentCoords[MIN_X] += currentLongZoom / 2;
		currentCoords[MIN_Y] += currentLatZoom / 2;
	}

    private void countDistances() {
        longDistance = (maxCoords[MAX_X] - maxCoords[MIN_X]);
        latDistance = (maxCoords[MAX_Y] - maxCoords[MIN_Y]);
        zoomLatDistances = new double[10];
        zoomLongDistances = new double[10];
        for (int i = 0; i < zoomLevels.length; i++) {
            zoomLongDistances[i] = zoomLevels[i] * longDistance;
            zoomLatDistances[i] = zoomLevels[i] * latDistance;
        }
    }

    private void updateMovement() {
        if (zoomLevel != 9) {
            latMovement = (currentCoords[MAX_Y] - currentCoords[MIN_Y]) / 10;
            longMovement = (currentCoords[MAX_X] - currentCoords[MIN_X]) / 10;
        } else {
            latMovement = 0;
            longMovement = 0;
        }
    }
    
    private final double[] maxCoords;
    private final double[] currentCoords;
    private final double angle;
    private static final int MIN_X = 0, MIN_Y = 1, MAX_X = 2, MAX_Y = 3;
    private double latDistance, longDistance, latMovement, longMovement;
    // TODO: do with enums
    private static double[] zoomLevels = { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7,
            0.8, 0.9, 1 };
    private double[] zoomLatDistances, zoomLongDistances;
    private int zoomLevel;
    
}
