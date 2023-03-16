import lombok.Getter;
import lombok.Setter;

@Getter
public class SquareMatrix {
    private final int cols;
    private final int rows;
    @Setter
    private double[][] values;
    private double[] freeValues;
    private final double EPS = 0.000001;

    public SquareMatrix(int n) {
        this.cols = n;
        this.rows = n;
        values = new double[rows][cols];
        freeValues = new double[rows];
    }

    boolean swapRows(int i, int j) {
        if (i >= rows || j >= rows) {
            return false;
        }
        double[] buf = values[i];
        values[i] = values[j];
        values[j] = buf;
        return true;
    }

    boolean swapCols(int i, int j) {
        if (i >= cols || j >= cols) {
            return false;
        }
        for  (int k = 0; i < rows; i++) {
            double t = values[i][k];
            values[i][k] = values[j][k];
            values[j][k] = t;
        }
        return true;
    }

    void subRowWithMultiplier(int resultRow, int subRow, int colIdx) {
        if (values[subRow][colIdx] < EPS && values[subRow][colIdx] > -EPS) {
            return;
        }
        double multiplier = values[subRow][colIdx] / values[resultRow][colIdx];
        for (int i = 0; i < cols; i++) {
            values[resultRow][i] -= multiplier * values[subRow][i];
            if (values[resultRow][0] < EPS && values[resultRow][0] > -EPS) {
                values[resultRow][i] = 0;
            }
        }
    }

    int[] getMaxElemCoords(int offset) {
        double max = values[offset][offset];
        int[] coords = {offset, offset};
        for (int i = offset; i < rows; i++) {
            for (int j = offset; j < cols; j++) {
                if (values[i][j] - max > EPS) {
                    max = values[i][j];
                    coords[0] = i;
                    coords[1] = j;
                }
            }
        }
        return coords;
    }

    int getMaxColElementRow(int col) {
        if (col >= cols) {
            throw new OutOfMatrixBoundsException();
        }
        double max = values[0][col];
        int row =  0;
        for (int i = 0; i < rows; i++) {
            if (values[i][col] - max > EPS) {
                max = values[i][col];
                row = i;
            }
        }
        return row;
    }

    boolean swapColElements(int col, int row1, int row2) {
        double t = values[row1][col];
        values[row1][col] = values[row2][col];
        values[row2][col] = t;
        return true;
    }

    boolean isUpperTriangular() {
        for (int i = 0; i < rows - 1; i++) {
            for (int j = i; j < cols; j++) {
                if (values[i][j] != 0) {
                    return false;
                }
            }
        }
        return false;
    }

    void gaussianStep(int i) {
        if (i < cols) {
            throw new OutOfMatrixBoundsException();
        }
    }

    int calcDetForUpperTriangular() {
        if (!isUpperTriangular()) {
            //throw
            return -1;
        }
        if (cols != rows) {
            return -1;
        }
        int det = 1;
        for (int i = 0; i < cols; i++) {
            det *= values[i][i];
        }
        return det;
    }
}
