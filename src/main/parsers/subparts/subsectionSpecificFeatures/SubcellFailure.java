package parsers.subparts.subsectionSpecificFeatures;

import parsers.Regex;
import parsers.subparts.MathObjects.Equations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubcellFailure {

    private List<String> fileList;
    private int sectionEndIndex;
    private int start;
    private int updatedIndex;
    private Map<String, List<Object>> subcellFailureMap;
    private List<Object> subcellFailureList;
    private String name;

    /**
     * subcell failure starts with the subcell failure regex
     * subcell failure ends on a blank line
     * after each retrieval, end index should be updated
     */
    public SubcellFailure(List<String> fileList, int sectionEndIndex, int start) {
        this.fileList = fileList;
        this.sectionEndIndex = sectionEndIndex;
        this.start = start;
        name = "";
        subcellFailureMap = new HashMap<>();
        subcellFailureList = new ArrayList<>();
        updatedIndex = -1;
        setSubcellFailure();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (Object entry : subcellFailureList) {
            sb.append(entry.toString().trim());
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getName() {
        return name.trim();
    }

    public int getUpdatedIndex() {
        return updatedIndex;
    }

    public Map<String, List<Object>> getSubcellFailureMap() {
        return subcellFailureMap;
    }

    public List<Object> getSubcellFailureList() {
        return subcellFailureList;
    }

    private void setSubcellFailure() {
        name = fileList.get(start + 1);
        for (int i = start; i < sectionEndIndex; i++) {
            String line = fileList.get(i);
            if (line.matches(Regex.REGEX_BLANK_LINE)) {
                updatedIndex = i;
                break;
            } else {
                if (line.matches(Regex.REGEX_EQN)) {
                    Equations equations = new Equations(line);
                    subcellFailureList.add(equations);
                } else if (!line.matches(Regex.REGEX_SUBCELL_FAILURE)) {
                    subcellFailureList.add(line);
                }
            }
        }
        subcellFailureMap.put(name, subcellFailureList);
    }
}