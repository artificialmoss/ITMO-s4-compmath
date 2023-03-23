import exceptions.*;
import lombok.AllArgsConstructor;

import java.text.DecimalFormat;
import java.util.Arrays;

@AllArgsConstructor
public class AppRunner implements Runnable {
    private final String MATRIX_FILEPATH;

    @Override
    public void run() {
        MatrixReader matrixReader = new MatrixReader();
        long start = System.currentTimeMillis();
        try {
            EquationMatrix matrix = matrixReader.readMatrixFromFile(MATRIX_FILEPATH);

            System.out.println("Original matrix:");
            System.out.println(matrix.toString());

            EquationMatrixTransformer transformer = new EquationMatrixTransformer(matrix);
            transformer.transformToUpperTriangular();

            System.out.println("Transformed matrix:");
            System.out.println(transformer.getMatrix().toString());

            double det = transformer.calcDet();

            System.out.println("Matrix determinant = " + det);
            System.out.println();

            SLESolver solver = new SLESolver(transformer.getMatrix());
            solver.solve();

            System.out.println("Variables:");
            System.out.println(Arrays.toString(solver.getMatrix().getVariables()));
            System.out.println();

            matrix.setVariables(solver.getMatrix().getVariables());
            double[] errors = matrix.calculateError();

            System.out.println("Errors:");
            System.out.println(Arrays.toString(errors));
            System.out.println();
        } catch (InvalidFilepathException e) {
            System.out.println("The specified filepath does not refer to an existing readable file.");
        } catch (InvalidMatrixException e) {
            System.out.println("The specified filepath does not contain a valid equation matrix.");
        } catch (OutOfMatrixBoundsException e) {
            System.out.println("Internal error. The program tried to refer to an invalid matrix index.");
        } catch (InvalidMatrixOperationException e) {
            System.out.println("Internal error. The program tried to execute an invalid matrix operation.");
        } catch (NonSolvableEquationException e) {
            System.out.println("This equation has either multiple solutions or no solution " +
                    "and can't be solved by this program.");
        }

        long end = System.currentTimeMillis();

        System.out.println("Execution time: " + (end - start) + " ms");
    }
}
