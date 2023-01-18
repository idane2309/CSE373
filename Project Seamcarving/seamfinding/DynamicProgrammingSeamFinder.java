package seamcarving.seamfinding;

import seamcarving.Picture;
import seamcarving.SeamCarver;
import seamcarving.energy.EnergyFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 * @see SeamCarver
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {


    @Override
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        double[][] pixelArray = new double[picture.width()][picture.height()];
        for(int i = 0; i < picture.height(); i++) {                     // Fills out the first collumn with energy values
            pixelArray[0][i] = f.apply(picture, 0, i);
        }
        buildPixelArray(picture, f, pixelArray);                        // Builds the rest of the pixel array accordingly
        return minCostPre(picture, pixelArray);
        //throw new UnsupportedOperationException("Not implemented yet");
    }

    // Method builds the 2D array with its corresponding energy values
    private void buildPixelArray(Picture picture, EnergyFunction f, double[][] pixelArray) {
        for (int x = 1; x < picture.width(); x++) {
            for (int y = 0; y < picture.height(); y++) {
                if (y == picture.height() - 1) {       // Top Case
                    pixelArray[x][y] = f.apply(picture, x, y) + Math.min(pixelArray[x - 1][y], pixelArray[x - 1][y - 1]);
                } else if (y == 0) {      // Bottom Case
                    pixelArray[x][y] = f.apply(picture, x, y) + Math.min(pixelArray[x - 1][y], pixelArray[x - 1][y + 1]);
                } else {            // Normal Case
                    pixelArray[x][y] = f.apply(picture, x, y) + Math.min(pixelArray[x - 1][y], Math.min(pixelArray[x - 1][y - 1], pixelArray[x - 1][y + 1]));
                }
            }
        }
    }

    private List<Integer> minCostPre(Picture picture, double[][] pixelArray) {
        List<Integer> result = new ArrayList<>();
        int y = picture.height() - 1;
        for (int j = picture.height() - 1; j >= 0; j--) {                   // Get starting value of y on right edge
            if(pixelArray[picture.width()-1][j] < pixelArray[picture.width()-1][y]) {
                y = j;
            }
        }
        result.add(y);                                                 //add starting y point to our list
        for (int x = picture.width() - 1; x > 0; x--) {
            if (y == picture.height() - 1) {    // Top Case
                if (Math.min(pixelArray[x - 1][y], pixelArray[x - 1][y - 1]) == pixelArray[x - 1][y]) {
                    result.add(y);
                } else {
                    result.add(y - 1);
                    y = y - 1;
                }
            } else if (y == 0) {      // Bottom Case
                if (Math.min(pixelArray[x - 1][y], pixelArray[x - 1][y + 1]) == pixelArray[x - 1][y]) {
                    result.add(y);
                } else {
                    result.add(y + 1);
                    y = y + 1;
                }
            } else {            // Normal Case
                if (Math.min(pixelArray[x - 1][y], Math.min(pixelArray[x - 1][y - 1], pixelArray[x - 1][y + 1])) == pixelArray[x - 1][y]) {
                    result.add(y);
                } else if (Math.min(pixelArray[x - 1][y], Math.min(pixelArray[x - 1][y - 1], pixelArray[x - 1][y + 1])) == pixelArray[x - 1][y - 1]) {
                    result.add(y - 1);
                    y = y - 1;
                } else {
                    result.add(y + 1);
                    y = y + 1;
                }
            }
        }
        Collections.reverse(result);
        return result;
    }
}
