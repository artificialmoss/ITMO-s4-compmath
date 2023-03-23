import exceptions.NonSolvableEquationException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SLESolver {
    private final EquationMatrix matrix;

    void findVariableStep(int step) {
        double sum = matrix.getFreeValues()[step];
        for (int i = matrix.getCols() - 1; i > step; i--) {
            sum -= matrix.getVariables()[i] * matrix.getValues()[step][i];
        }
        double value = sum / matrix.getValues()[step][step];
        matrix.setVariable(step, value);
    }

    void solve() {
        if (matrix.isASolvableEquation()) {
            for (int i = matrix.getCols() - 1; i >= 0; i--) {
                findVariableStep(i);
            }
        } else {
            throw new NonSolvableEquationException();
        }
    }

    double[] calculateError() {
        double[] errors = new double[matrix.getRows()];
        for (int i = 0; i < matrix.getRows(); i++) {
            double sumLeft = 0;
            for (int j = 0; j < matrix.getCols(); j++) {
                sumLeft += matrix.getVariables()[j] * matrix.getValues()[i][j];
            }
            errors[i] = matrix.getFreeValues()[i] - sumLeft;
        }
        return errors;
    }
}
