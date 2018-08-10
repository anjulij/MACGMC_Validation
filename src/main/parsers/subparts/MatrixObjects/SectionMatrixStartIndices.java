package parsers.subparts.MatrixObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static parsers.Regex.REGEX_BLANK_LINE;
import static parsers.Regex.REGEX_SERIES_OF_NUMBERS;

public class SectionMatrixStartIndices extends MatrixStartIndicesRetrieval {
    private List<String> fileList;
    private int sectionStartIndex;
    private int sectionEndIndex;

    public SectionMatrixStartIndices(List<String> fileList, int sectionStartIndex, int sectionEndIndex) {
        this.fileList = fileList;
        this.sectionStartIndex = sectionStartIndex;
        this.sectionEndIndex = sectionEndIndex;
        allMatrixStartIndices = getStartIndicesByLookingAhead();
    }

    private List<Map<String, Integer>> getStartIndicesByLookingAhead() {
        List<Map<String, Integer>> startIndices = new ArrayList<>();
        for (int i = sectionStartIndex; i < sectionEndIndex; i++) {
            String line = fileList.get(i).trim();
            if (line.matches(REGEX_BLANK_LINE)) {
                continue;

            } else if (line.matches(REGEX_SERIES_OF_NUMBERS)) {
                continue;

            } else {
                for (int j = i + 1; j < sectionEndIndex; j++) {
                    String line_ahead = fileList.get(j).trim();
                    if (line_ahead.matches(REGEX_SERIES_OF_NUMBERS)) {
                        Map<String, Integer> matrix_start_indices = new HashMap<>();
                        matrix_start_indices.put("Matrix Label Index", i);
                        matrix_start_indices.put("Matrix Values Start Index", j);
                        startIndices.add(matrix_start_indices);
                        break;
                    } else if (line_ahead.matches(REGEX_BLANK_LINE)) {
                        continue;
                    } else {
                        break;
                    }
                }
            }
        }
        return startIndices;
    }
}
