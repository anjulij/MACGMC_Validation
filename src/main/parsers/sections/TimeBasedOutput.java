package parsers.sections;

import parsers.Regex;
import parsers.subparts.subsectionSpecificFeatures.*;
import parsers.subparts.MathObjects.*;
import parsers.subparts.MatrixObjects.Matrix;
import parsers.subparts.MatrixObjects.MatrixStartIndex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This section retrieves all of the time-based output in a file including subcell failure
 */
public class TimeBasedOutput {
    private List<String> fileList;
    private int sectionStartIndex;
    private int sectionEndIndex;
    private Map<String, Map<String, Object>> sectionDataMap;

    public TimeBasedOutput(List<String> fileList, int sectionStartIndex, int sectionEndIndex) {
        this.fileList = fileList;
        this.sectionStartIndex = sectionStartIndex;
        this.sectionEndIndex = sectionEndIndex;
        sectionDataMap = new HashMap<>();
        identifyTimeBasedOutput();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Map<String, Object>> entry : sectionDataMap.entrySet()) {
            String name = entry.getKey();
            sb.append(name).append("\n");
            Map<String, Object> m = entry.getValue();
            for (Map.Entry<String, Object> e : m.entrySet()) {
                sb.append(String.format("%s: %s \n", e.getKey(), e.getValue().toString()));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    public Map<String, Map<String, Object>> getSectionDataMap() {
        return sectionDataMap;
    }


    private void identifyTimeBasedOutput() {
        for (int i = sectionStartIndex; i < sectionEndIndex; i++) {
            String line = fileList.get(i);
            if (line.matches(Regex.REGEX_TIME_BASED_OUTPUT)) {
                int temp = i;
                i = getTimeStep(temp);
            }
        }
    }

    private int getTimeStep(int start) {
        /*Starts with REGEX_TIME_BASED_OUTPUT
          Ends new time step
          or MAIN: TIME EXCEEDED (alternatively: ENDENDEND...)**/
        Map<String, Object> timeBasedOutputMap = new HashMap<>();
        String name = fileList.get(start).trim();

        int updatedIndex = -1;

        for (int i = start + 1; i < sectionEndIndex; i++) {
            String line = fileList.get(i);
            if (line.matches(Regex.REGEX_TIME_BASED_OUTPUT)) {
                updatedIndex = i;
                break;
            } else if (line.matches(Regex.REGEX_DOCUMENT_END)) {
                updatedIndex = i;
                break;
            } else {
                /*Retrieve Subcell Failure*/
                if (line.matches(Regex.REGEX_SUBCELL_FAILURE)) {
                    int temp = i;
                    SubcellFailure subcellFailure = new SubcellFailure(fileList, sectionEndIndex, temp);
                    i = subcellFailure.getUpdatedIndex();
                    updatedIndex = i;
                    timeBasedOutputMap.put(subcellFailure.getName(), subcellFailure);
                }
                /*Retrieve Matrices*/
                else if (line.matches(Regex.REGEX_SERIES_OF_NUMBERS)) {
                    MatrixStartIndex startIndex = new MatrixStartIndex(fileList, sectionStartIndex, i);
                    Matrix m = new Matrix(fileList, sectionEndIndex, startIndex.getValue());
                    i = m.getMatrixEndIndex() + 1;
                    updatedIndex = i;
                    timeBasedOutputMap.put(m.getMatrixTitle(), m);
                }
                /*Retrieve vectors that are not in subcell failure*/
               else if (line.matches(String.format(".*%s.*", Regex.REGEX_VECTOR))) {
                    Vectors vector = new Vectors(line);
                    //String label = String.format("Vectors at line %d", i);
                    //String label = vector.getMath_object_name();
                    /*Separates vectors that are on the same line, if you don't want to do that uncomment the commented out lines*/
                    for (Map.Entry<String,List<Double>> v : vector.getVectors().entrySet()) {
                        String l = String.format("%s: %s",v.getKey(),v.getValue()).replace(",","").replace("[","").replace("]","");
                        Vectors v1 = new Vectors(l);
                        String label = v.getKey();
                        timeBasedOutputMap.put(label, v1);
                    }
                    //effectivePropertiesMap.put(label, vector);
                    updatedIndex = i;
                }
                /*Retrieve equations that are not in subcell failure*/
                else if (line.matches(String.format(".*%s.*", Regex.REGEX_EQN))) {
                    Equations equation = new Equations(line);
                    //String label = String.format("Equation at line %d", i);
                    //String label = equation.getMath_object_name();
                    /*Separates equations that are on the same line, if you don't want to do that uncomment the commented out lines*/
                    for (Map.Entry<String,List<Double>> e :equation.getMathEquations().entrySet()) {
                        String l = String.format("%s = %s",e.getKey(),e.getValue()).replace(",","").replace("[","").replace("]","");
                        Equations e1 = new Equations(l);
                        String label = e.getKey();
                        timeBasedOutputMap.put(label, e1);
                    }
                    //effectivePropertiesMap.put(label, equation);
                    updatedIndex = i;
                }
            }
        }
        sectionDataMap.put(name, timeBasedOutputMap);

        return updatedIndex - 1;
    }

}