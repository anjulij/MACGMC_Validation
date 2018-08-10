package comparators.subpart_comparators;

import comparators.Tolerance;
import parsers.subparts.MatrixObjects.Matrix;

public class MatrixComparator {
    private Matrix m1;
    private Matrix m2;
    private Object[] failure;

    public MatrixComparator(Matrix m1, Matrix m2){
        failure = new Object[2];
        this.m1 = m1;
        this.m2 = m2;
    }

    public Object[] getFailure(){
        /*if failure is empty there hasn't been failure*/
        return compareMatrices();
    }

    private Object[] compareMatrices() {
        boolean bool = true;
        Double[][] matrix1 = m1.getMatrixArray();
        Double[][] matrix2 = m2.getMatrixArray();

        /*Check that matrices are the same size*/
        if (!((m1.getRows() == m2.getRows()) && (m1.getColumns() == m2.getColumns()))) {
            bool = false;
            failure = new Object[2];
            failure[0] = bool;
            failure[1] = "matrices are not the same size";
        }
        /*Check that matrices contain the same elements*/
        else {
            for (int i = 0; i < m1.getRows(); i++) {
                for (int j = 0; j < m1.getColumns(); j++) {
                    double diff = Math.abs(matrix1[i][j] - matrix2[i][j]);
                    Tolerance tolerance = new Tolerance(matrix1[i][j]);
                    double epsilon = tolerance.getEpsilon();
                    if (!(diff <= epsilon)) {
                        bool = false;
                        failure = new Object[2];
                        failure[0] = bool;
                        failure[1] = String.format("matrices do not have the same values: %f != %f", matrix1[i][j], matrix2[i][j]);
                        break;
                    }
                }
                if(!bool){
                    break;
                }
            }
        }
        return failure;
    }
}