import exceptions.InvalidFilepathException;
import exceptions.InvalidMatrixException;
import exceptions.InvalidMatrixOperationException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class MatrixReader {
    private boolean getReadableFile(String filename) {
        try {
            Path path = Paths.get(filename);
            return Files.exists(path) && !Files.isDirectory(path) && Files.isReadable(path);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private double[] readValues(String line, int dimension) {
        return Arrays.stream(line.split("\\s"))
                .mapToDouble(Double::parseDouble)
                .limit(dimension)
                .toArray();
    }

    private double readFreeValue(String line, int dimension) {
        return Double.parseDouble(line.split("\\s")[dimension]);
    }

    public EquationMatrix readMatrixFromFile(String filename) {
        try {
            if (!getReadableFile(filename)) {
                throw new InvalidFilepathException();
            }
            StringBuilder fileContent = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            String line = br.readLine();
            if (line == null) {
                throw new InvalidMatrixException();
            }
            int dimension = Integer.parseInt(line.strip());
            double[][] values = new double[dimension][dimension];
            double[] freeValues = new double[dimension];
            for (int i = 0; i < dimension; i++) {
                line = br.readLine();
                if (line == null) {
                    throw new InvalidMatrixException();
                }
                values[i] = readValues(line, dimension);
                freeValues[i] = readFreeValue(line, dimension);
            }

            EquationMatrix matrix =  new EquationMatrix(dimension);
            matrix.setValues(values);
            matrix.setFreeValues(freeValues);
            return matrix;
        } catch (IOException | IndexOutOfBoundsException | NumberFormatException e) {
            throw new InvalidMatrixException();
        }
    }
}
