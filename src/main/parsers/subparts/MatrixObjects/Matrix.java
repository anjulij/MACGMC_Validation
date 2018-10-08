package parsers.subparts.MatrixObjects;

import parsers.Regex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static parsers.Regex.REGEX_SERIES_OF_NUMBERS;

/**
 * This class retrieves a singular rectangular given where it begins
 */
public class Matrix {
    private List<String> fileList;
    private int sectionEndIndex;
    private Map<String, Integer> startIndices;
    private int columns;
    private int rows;

    public Matrix(List<String> fileList, int sectionEndIndex, Map<String, Integer> startIndices) {
        this.fileList = fileList;
        this.sectionEndIndex = sectionEndIndex;
        this.startIndices = startIndices;
        getMatrixInformation();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        //sb.append(String.format("%s \n", getMatrixTitle()));
        sb.append("\n");
        Double [][] m = getMatrixArray();

        for (Double[] a : m) {
            for (int i = 0; i < a.length; i++) {
                if (i == a.length - 1) {
                    sb.append(String.format("%s \n", a[i]));
                } else {
                    sb.append(String.format("%s ", a[i]));
                }
            }
        }
        return sb.toString();
    }

    public String toJson(){
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append(String.format("\"%s\":[",getMatrixTitle()));

        Double [][] m = getMatrixArray();
        for (int row = 0; row < m.length; row++) {
            for (int col = 0; col < m[row].length; col++){
                double value = m[row][col];
                if(col == 0){
                    sb.append(String.format("[%f,", value));
                }
                else if(col == m[row].length-1){
                    if(row == m.length -1 ){
                        sb.append(String.format("%f]]", value));
                    }
                    else {
                        sb.append(String.format("%f],", value));
                    }
                }
                else{
                    sb.append(String.format("%f,", value));
                }

            }
        }
        sb.append("\n}");
        return sb.toString();
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public String getMatrixTitle() {
        return ((String) getMatrixInformation().get("Matrix Title")).replace(":", "");
    }

    public int getMatrixStartIndex() {
        return (int) getMatrixInformation().get("Matrix Start Index");
    }

    public int getMatrixEndIndex() {
        return (int) getMatrixInformation().get("Matrix End Index");
    }

    public Double[][] getMatrixArray() {
        return (Double [][]) getMatrixInformation().get("Matrix");
    }

    private Map<String, Object> getMatrixInformation() {
        int matrixLabelIndex = startIndices.get("Matrix Label Index");
        int matrixStart = startIndices.get("Matrix Values Start Index");
        String label = fileList.get(matrixLabelIndex);
        String matrixTitle;
        if(label.matches(Regex.REGEX_SECTION) || label.matches(Regex.REGEX_BLANK_LINE)){
            matrixTitle = "No Matrix Title Found";
        }else{
           matrixTitle = fileList.get(matrixLabelIndex).trim();
        }

        int matrixEnd = 0;
        rows = 0;
        Map<String, Object> matrixInformation = new HashMap<>();

        //Get number of columns
        columns = fileList.get(matrixStart).trim().split("\\s+").length;

        //Get end line and number of rows
        for (int i = matrixStart; i < sectionEndIndex; i++) {
            /*After the matrix begins, the next blank line signifies the matrix end*/
            String line = fileList.get(i).trim();
            if (!line.matches(REGEX_SERIES_OF_NUMBERS)) {
                matrixEnd = i - 1;
                rows = (matrixEnd - matrixStart) + 1;
                break;
            }
        }
        Double [][] matrix = getMatrix(matrixStart, matrixEnd, rows, columns);
        matrixInformation.put("Matrix Title", matrixTitle);
        matrixInformation.put("Matrix Start Index", matrixStart);
        matrixInformation.put("Matrix End Index", matrixEnd);
        matrixInformation.put("Matrix", matrix);

        return matrixInformation;
    }

    private Double [][] getMatrix(int matrixStart, int matrixEnd, int rows, int columns) {
        Double [][] matrix = new Double[rows][columns];
        int j = 0;
        for (int i = matrixStart; i <= matrixEnd; i++) {
            String data[] = fileList.get(i).trim().split("\\s+");
            for (int k = 0; k < columns; k++) {
                Double val = Double.valueOf(data[k].trim());
                matrix[j][k] = val;
                //matrix[j][k] = data[k].trim();
            }
            j++;
        }
        return matrix;
    }
}
