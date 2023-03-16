import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class SquareMatrixTransformer {
    private final SquareMatrix matrix;
    @Setter
    private int detMultiplier = 1;

    void swapRows(int i, int j) {
        if (!matrix.swapRows(i, j)) {
            // throw;
            return;
        }
        detMultiplier *= -1;
    }

    void swapCols(int i, int j) {
        if (!matrix.swapCols(i, j)) {
            // throw;
            return;
        }
        detMultiplier *= -1;
    }

    void transformToUpperTrianglular() {
        for (int i = 0; i < matrix.getCols(); i++) {
            if (matrix.isUpperTriangular()) {
                return;
            }

        }
    }
}
