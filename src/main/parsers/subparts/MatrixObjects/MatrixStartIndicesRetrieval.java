package parsers.subparts.MatrixObjects;

import java.util.List;
import java.util.Map;

class MatrixStartIndicesRetrieval {
    static List<Map<String, Integer>> allMatrixStartIndices;

    MatrixStartIndicesRetrieval() {
    }

    public Map<String, Integer> get(int i) {
        return allMatrixStartIndices.get(i);
    }

    public int size() {
        return allMatrixStartIndices.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map<String, Integer> map : allMatrixStartIndices) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                sb.append(String.format("%s: %d\n", entry.getKey(), entry.getValue()));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

