package exceptions;

public class OutOfMatrixBoundsException extends IndexOutOfBoundsException{
    public OutOfMatrixBoundsException() {
        super();
    }

    public OutOfMatrixBoundsException(String message) {
        super(message);
    }
}
