package parsers.subparts.MatrixObjects;

import parsers.Regex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatrixStartIndex extends MatrixStartIndicesRetrieval {
    private List<String> fileList;
    private int sectionStartIndex;

    public MatrixStartIndex(List<String> fileList, int sectionStartIndex, int series_of_numbers_index) {
        this.fileList = fileList;
        this.sectionStartIndex = sectionStartIndex;
        allMatrixStartIndices = getStartIndicesByLookingBehind(series_of_numbers_index);
    }

    public Map<String, Integer> getValue() {
        return allMatrixStartIndices.get(0);
    }

    private List<Map<String, Integer>> getStartIndicesByLookingBehind(int index_series_of_numbers) {
        Map<String, Integer> matrix_start_indices = new HashMap<>();
        List<Map<String, Integer>> startIndices = new ArrayList<>();
        for (int j = index_series_of_numbers - 1; j >= sectionStartIndex; j--) {
            String line_previous = fileList.get(j).trim();
            if (!line_previous.matches(Regex.REGEX_BLANK_LINE)) {
                matrix_start_indices.put("Matrix Label Index", j);
                matrix_start_indices.put("Matrix Values Start Index", index_series_of_numbers);
                startIndices.add(matrix_start_indices);
                break;
            }
        }
        return startIndices;
    }
}
