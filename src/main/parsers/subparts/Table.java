package parsers.subparts;

import parsers.Regex;

import java.util.*;


public class Table {
    private List<String> fileList;
    private int sectionEndIndex;
    private Map<List<String>, List<List<List<Double>>>> TableMap;
    private int updatedIndex;

    /**
     * This class retrieves the subcell identification table given where it begins
     * Alternatively the beginning could be the regex of the label
     */
    public Table(List<String> fileList, int tableStartIndex, int sectionEndIndex) {
        this.fileList = fileList;
        this.sectionEndIndex = sectionEndIndex;
        updatedIndex = -1;
        TableMap = new HashMap<>();
        buildTable(tableStartIndex);
    }

    @Override
    //TODO: MAKE THIS MATRIX?
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<List<String>, List<List<List<Double>>>> entry : TableMap.entrySet()) {
            sb.append(String.format("Label: %s\n", entry.getKey()).replace("[", "").
                    replace("]", ""));
            List<List<List<Double>>> l = entry.getValue();
            sb.append("Table:\n");
            for (List<List<Double>> s : l) {
                sb.append(String.format("\t%s\n", s));
            }
        }
        return sb.toString();
    }

    public String getLabel() {
        String label = "";
        for (Map.Entry<List<String>, List<List<List<Double>>>> entry : TableMap.entrySet()) {
            label = String.valueOf(entry.getKey());
        }
        return label;
    }

    public List<List<List<Double>>> getParsedTableData() {
        List<List<List<Double>>> parsedTableData = new ArrayList<>();
        for (Map.Entry<List<String>, List<List<List<Double>>>> entry : TableMap.entrySet()) {
            parsedTableData = entry.getValue();
        }
        return parsedTableData;
    }

    public int getUpdatedIndex() {
        return updatedIndex;
    }

    /*Traditionally, there is only 1 line in the beginning of subcell identification that can
     * be interpreted as a label after the label of the subsection,
     * the following non blank line is the beginning of the table */
    private int[] getSubcellIDLabelIndices(int subscectionStartIndex) {
        int[] subcellIndices = new int[2];
        int label_1_index = -1;
        int table_start_index = -1;

        for (int i = subscectionStartIndex; i < sectionEndIndex; i++) {
            String line = fileList.get(i);
            /*Label 1 has not been found, the next non blank line is label 1*/
            if (label_1_index == -1 && !line.matches(Regex.REGEX_BLANK_LINE)) {
                label_1_index = i;
            }
            /*Label 1 has been found, the next non blank line is the beginning of the table, exit loop*/
            else if (table_start_index == -1 && !line.matches(Regex.REGEX_BLANK_LINE)) {
                table_start_index = i;
                break;
            }
        }
        subcellIndices[0] = label_1_index;
        subcellIndices[1] = table_start_index;

        return subcellIndices;
    }

    private Object[] getSubcellTableElements(int subsection_start_index) {
        List<String> tableData = new ArrayList<>();
        String label;
        Object[] initialSubcellTable = new Object[2];

        int[] labelIndices = getSubcellIDLabelIndices(subsection_start_index);
        label = fileList.get(labelIndices[0]);
        int start = labelIndices[1];

        for (int i = start; i < sectionEndIndex; i++) {
            String line = fileList.get(i);
            if (line.matches(Regex.REGEX_BLANK_LINE)) {
                updatedIndex = i;
                break;
            } else {
                tableData.add(line);
            }
        }
        initialSubcellTable[0] = label;
        initialSubcellTable[1] = tableData;
        return initialSubcellTable;
    }

    //TODO: Try merge sort approach, merge left and right of commas
    // Change the regex recognizer to a series of numbers and columns
    /*This only works if all elements, including the comma, are separated by a space
    and the label elements are separated by more than one space*/
    private void buildTable(int subsection_start_index) {
        List<List<List<Double>>> parsedTableData = new ArrayList<>();
        Object[] initialSubcellTable = getSubcellTableElements(subsection_start_index);
        String label = (String) initialSubcellTable[0];
        List<String> tableData = (List<String>) initialSubcellTable[1];
        List<String> labelList = Arrays.asList(label.trim().split("\\s{2,}"));

        for (int i = 0; i < tableData.size(); i++) {
            String line = tableData.get(i).trim();
            List<String> lineArray = Arrays.asList(line.split("\\s+"));
            List<List<Double>> lineElements = new ArrayList<>();

            for (int j = 0; j < lineArray.size(); j++) {
                List<Double> element = new ArrayList<>();
                String e = lineArray.get(j);
                if (!e.matches(",")) {
                    Double d = Double.parseDouble(e);
                    element.add(d);
                    if (!((j + 1 == lineArray.size()) | (j + 2 == lineArray.size()))) {
                        for (int k = j + 1; k < lineArray.size(); k++) {
                            String e2 = lineArray.get(k);
                            if (e2.matches(",")) {
                                if (!(k + 1 >= lineArray.size())) {
                                    String e3 = lineArray.get(k + 1);
                                    Double d2 = Double.parseDouble(e3);
                                    element.add(d2);
                                    k = k + 1;
                                }
                            } else {
                                if (lineArray.get(k - 1).matches(",")) {
                                    j = k;
                                } else {
                                    j = k - 1;
                                }
                                break;
                            }
                        }
                    }
                    lineElements.add(element);
                    element = new ArrayList<>();
                }
            }
            parsedTableData.add(lineElements);
        }
        TableMap.put(labelList, parsedTableData);
    }
}