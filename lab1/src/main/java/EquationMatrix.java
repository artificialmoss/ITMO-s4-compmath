import exceptions.InvalidMatrixException;
import exceptions.InvalidMatrixOperationException;
import exceptions.NonSolvableEquationException;
import exceptions.OutOfMatrixBoundsException;
import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.abs;

@Getter
@Setter
public class EquationMatrix {
    private final int cols;
    private final int rows;
    private double[][] values;
    private double[] freeValues;
    private double[] variables;
    private final double EPS = 0.00000000000000001;

    public EquationMatrix(int n) {
        this.cols = n;
        this.rows = n;
        values = new double[rows][cols];
        freeValues = new double[rows];
        variables = new double[cols];
    }

    boolean isValidRow(int row) {
        return row >= 0 && row < rows;
    }

    boolean isValidCol(int col) {
        return col >= 0 && col < cols;
    }

    void setVariable(int idx, double value) {
        variables[idx] = value;
    }

    void swapRows(int i, int j) {
        swapValuesRows(i, j);
        swapVariableRows(i, j);
    }

    void swapValuesRows(int i, int j) {
        if (!isValidRow(i) || !isValidRow(j)) {
            throw new OutOfMatrixBoundsException();
        }
        double[] buf = values[i];
        values[i] = values[j];
        values[j] = buf;
    }

    void swapVariableRows(int i, int j) {
        if (!isValidRow(i) || !isValidRow(j)) {
            throw new OutOfMatrixBoundsException();
        }
        double tmp = variables[i];
        variables[i] = variables[j];
        variables[j] = tmp;
    }

    double roundIfZero(double value) {
       if (value < EPS && value > -EPS) {
           return 0;
       }
       return value;
    }

    void subRowWithMultiplier(int resultRow, int subRow, int colIdx) {
        if (!isValidRow(subRow) || !isValidRow(resultRow) || !isValidCol(colIdx)) {
            throw new OutOfMatrixBoundsException();
        }
        if (resultRow == subRow) {
            throw new InvalidMatrixOperationException();
        }
        if (roundIfZero(values[resultRow][colIdx]) == 0) {
            return;
        }
        double multiplier = values[resultRow][colIdx] / values[subRow][colIdx];
        for (int i = 0; i < cols; i++) {
            values[resultRow][i] -= multiplier * values[subRow][i];
            values[resultRow][i] = roundIfZero(values[resultRow][i]);
        }
        freeValues[resultRow] -= multiplier * freeValues[subRow];
        freeValues[resultRow] = roundIfZero(freeValues[resultRow]);
    }

    int getMaxColElementRow(int col, int startRow) {
        if (!isValidCol(col) || !isValidRow(startRow)) {
            throw new OutOfMatrixBoundsException();
        }
        double max = abs(values[startRow][col]);
        int row =  startRow;
        for (int i = startRow; i < rows; i++) {
            if (abs(values[i][col]) - max > EPS) {
                max = values[i][col];
                row = i;
            }
        }
        return row;
    }

    boolean isUpperTriangular() {
        for (int i = 0; i < rows - 1; i++) {
            if (roundIfZero(values[i][i]) == 0) {
                return false;
            }
            for (int j = i - 1; j >= 0; j--) {
                if (roundIfZero(values[i][j]) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    double calcDetForUpperTriangular() {
        if (!isUpperTriangular()) {
            throw new InvalidMatrixOperationException();
        }
        if (cols != rows) {
            throw new InvalidMatrixOperationException();
        }
        double det = 1;
        for (int i = 0; i < cols; i++) {
            det *= values[i][i];
        }
        return det;
    }

    boolean isASolvableEquation() {
        try {
            double det = calcDetForUpperTriangular();
            return roundIfZero(det) != 0.0;
        } catch (InvalidMatrixOperationException e) {
            return false;
        }
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.append(values[i][j]).append("  ");
            }
            res.append("|  ").append(freeValues[i]).append("\n");
        }
        return res.toString();
    }

    double[] calculateError() {
        double[] errors = new double[rows];
        for (int i = 0; i < rows; i++) {
            double sumLeft = 0;
            for (int j = 0; j < cols; j++) {
                sumLeft += variables[j] * values[i][j];
            }
            errors[i] = freeValues[i] - sumLeft;
        }
        return errors;
    }
}
