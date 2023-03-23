import exceptions.NonSolvableEquationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EquationMatrixTransformer {
    private final EquationMatrix matrix;
    int detMultiplier = 1;

    void toUpperTriangularStep(int step) {
        int rowIdx = matrix.getMaxColElementRow(step, step);
        if (rowIdx != step) {
            matrix.swapRows(step, rowIdx);
            detMultiplier *= -1;
        }
        for (int j = step + 1; j < matrix.getRows(); j++) {
            matrix.subRowWithMultiplier(j, step, step);
        }
    }

    void transformToUpperTriangular() {
        for (int i = 0; i < matrix.getCols(); i++) {
            toUpperTriangularStep(i);
        }
    }

    double calcDet() {
        if (!matrix.isUpperTriangular()) {
            transformToUpperTriangular();
            if (!matrix.isUpperTriangular()) {
                throw new NonSolvableEquationException();
            }
        }
        return matrix.calcDetForUpperTriangular() * detMultiplier;
    }
}
