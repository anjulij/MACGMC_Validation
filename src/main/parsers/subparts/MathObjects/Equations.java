package parsers.subparts.MathObjects;

import parsers.Regex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Equations extends MathObject {
    private Map<String, List<Double>> equations;
    private String line;

    public Equations(String line) {
        super(line);
        this.line = line;
        equations = new HashMap<>();
        getMathEquations();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!equations.isEmpty()) {
            Iterator i = equations.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry entry = (Map.Entry) i.next();
                sb.append(String.format("%s = ", entry.getKey()));
                if (i.hasNext()) {
                    sb.append(String.format("%s, ", entry.getValue()));
                } else {
                    sb.append(String.format("%s ", entry.getValue()));
                }
                i.remove();
            }
        }
        return sb.toString();
    }

    private Boolean isMathEquation() {
        return line.matches(String.format(".*%s.*", Regex.REGEX_EQN));
    }

    public Map<String, List<Double>> getMathEquations() {
        if (isMathEquation()) {
            equations = getMathObject();
            return equations;
        } else {
            return null;
        }
    }
}
