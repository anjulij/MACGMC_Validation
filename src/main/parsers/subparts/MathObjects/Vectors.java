package parsers.subparts.MathObjects;

import parsers.Regex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Vectors extends MathObject {
    private String line;
    private Map<String, List<Double>> vectors;

    public Vectors(String line) {
        super(line);
        this.line = line;
        vectors = new HashMap<>();
        getVectors();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!vectors.isEmpty()) {
            for (Map.Entry<String, List<Double>> entry : vectors.entrySet()) {
                sb.append(String.format("%s: ", entry.getKey()));
                sb.append(String.format("%s ", entry.getValue()));
            }
        }
        return sb.toString();
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        if (!vectors.isEmpty()) {
            for (Map.Entry<String, List<Double>> entry : vectors.entrySet()) {
                sb.append("{\n");
                sb.append(String.format("\t\"%s\":", entry.getKey()));
                sb.append(String.format("%s", entry.getValue()));
                sb.append("\n}");
            }
        }
        return sb.toString();
    }

    private Boolean isVector() {
        return line.matches(String.format(".*%s.*", Regex.REGEX_VECTOR));
    }

    public Map<String, List<Double>> getVectors() {
        if (isVector()) {
            vectors = getMathObject();
            return vectors;
        } else {
            return null;
        }
    }
}
