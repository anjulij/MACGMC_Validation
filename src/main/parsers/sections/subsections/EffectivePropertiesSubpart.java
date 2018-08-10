package parsers.sections.subsections;

import parsers.Regex;
import parsers.subparts.MathObjects.Equations;
import parsers.subparts.MathObjects.Vectors;
import parsers.subparts.MatrixObjects.Matrix;
import parsers.subparts.MatrixObjects.MatrixStartIndex;
import parsers.subparts.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EffectivePropertiesSubpart {
    private List<String> fileList;
    private int sectionStartIndex;
    private int sectionEndIndex;
    private Map<String, Map<String, Object>> subsectionDataMap;
    private int subsectionEndIndex;

    /**
     * This iterates through a effective properties subsection and retrieves the values inside of it
     */
    public EffectivePropertiesSubpart(List<String> fileList, int subsectionStartIndex, int sectionStartIndex, int sectionEndIndex) {
        this.fileList = fileList;
        this.sectionStartIndex = sectionStartIndex;
        this.sectionEndIndex = sectionEndIndex;
        subsectionDataMap = new HashMap<>();
        getEffectivePropertiesSubpart(subsectionStartIndex);
        subsectionEndIndex = getSubsectionEndIndex();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Map<String, Object>> entry : subsectionDataMap.entrySet()) {
            sb.append(entry.getKey()).append("\n");
            Map<String, Object> e = entry.getValue();
            for (Map.Entry<String, Object> item : e.entrySet()) {
                sb.append(item.getKey()).append("\n");
                sb.append(item.getValue().toString()).append("\n").append("\n");
            }
        }
        return sb.toString();
    }
    public String getKey(){
        String key = "";
        for (Map.Entry<String, Map<String, Object>> entry : subsectionDataMap.entrySet()) {
          key = entry.getKey();
        }
        return key;
    }
    public Map<String, Object> getValue(){
        Map<String, Object> value = new HashMap<>();
        for (Map.Entry<String, Map<String, Object>> entry : subsectionDataMap.entrySet()) {
            value = entry.getValue();
        }
        return value;
    }

    public int getSubsectionEndIndex() {
        return subsectionEndIndex;
    }

    public Map<String, Map<String, Object>> getSubsectionDataMap() {
        return subsectionDataMap;
    }

    private void getEffectivePropertiesSubpart(int start) {
        Map<String, Object> effectivePropertiesMap = new HashMap<>();
        String name = fileList.get(start).replace("-","").trim();
        subsectionEndIndex = -1;

        for (int i = start + 1; i < sectionEndIndex; i++) {
            String line = fileList.get(i);
            if (line.matches(Regex.REGEX_EFFECTIVE_PROPERTIES)) {
                subsectionEndIndex = i;
                break;
            } else if (line.matches(Regex.REGEX_DOCUMENT_END)) {
                subsectionEndIndex = i;
                break;
            } else {
                /*Retrieve Subcell Table*/
                if (line.matches(Regex.REGEX_SUBCELL_TABLE)) {
                    int temp = i;
                    Table subcellTable = new Table(fileList, temp, sectionEndIndex);
                    i = subcellTable.getUpdatedIndex();
                    subsectionEndIndex = i;
                    effectivePropertiesMap.put(subcellTable.getLabel(), subcellTable);
                }
                /*Retrieve Matrices*/
                else if (line.matches(Regex.REGEX_SERIES_OF_NUMBERS)) {
                    MatrixStartIndex startIndex = new MatrixStartIndex(fileList, sectionStartIndex, i);
                    Matrix m = new Matrix(fileList, sectionEndIndex, startIndex.getValue());
                    i = m.getMatrixEndIndex() + 1;
                    subsectionEndIndex = i;
                    effectivePropertiesMap.put(m.getMatrixTitle(), m);
                }
                /*Retrieve vectors*/
                else if (line.matches(String.format(".*%s.*", Regex.REGEX_VECTOR))) {
                    Vectors vector = new Vectors(line);
                    //String label = String.format("Vectors at line %d", i);
                    //String label = vector.getMath_object_name();
                    for (Map.Entry<String,List<Double>> v : vector.getVectors().entrySet()) {
                        String l = String.format("%s = %s",v.getKey(),v.getValue()).replace(",","").replace("[","").replace("]","");
                        Vectors v1 = new Vectors(l);
                        String label = v.getKey();
                        effectivePropertiesMap.put(label, v1);
                    }
                    //effectivePropertiesMap.put(label, vector);
                    subsectionEndIndex = i;
                }
                /*Retrieve equations*/
                else if (line.matches(String.format(".*%s.*", Regex.REGEX_EQN))) {
                    Equations equation = new Equations(line);
                    //String label = String.format("Equation at line %d", i);
                    //String label = equation.getMath_object_name();
                    /*Separates equations that are on the same line*/
                    for (Map.Entry<String,List<Double>> e :equation.getMathEquations().entrySet()) {
                        String l = String.format("%s = %s",e.getKey(),e.getValue()).replace(",","").replace("[","").replace("]","");
                        Equations e1 = new Equations(l);
                        String label = e.getKey();
                        effectivePropertiesMap.put(label, e1);
                    }
                    //effectivePropertiesMap.put(label, equation);
                    subsectionEndIndex = i;
                }
            }
        }
        subsectionDataMap.put(name, effectivePropertiesMap);
    }
}