package parsers.subparts.MathObjects;

import parsers.Regex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class parses mathObjects on a single line. It contains Vectors and Equations
 */

class MathObject {
    private String line;
    private Map<String, List<Double>> mathObjects;
    private String math_object_name;

    public MathObject(String line) {
        this.line = line;
        mathObjects = new HashMap<>();
    }
    public String getMath_object_name(){
        return math_object_name;
    }
    Map<String, List<Double>> getMathObject() {
        String[] line_array = line.trim().split("\\s+");

        for (int i = 0; i < line_array.length; i++) {
            math_object_name = "";
            List<Double> math_object_elements = new ArrayList<>();
            StringBuilder sb = new StringBuilder();

            //retrieve MathObject name
            for (int j = i; j < line_array.length; j++) {
                if (line_array[j].matches(Regex.REGEX_FLOAT) || line_array[j].matches("\\d+")) {
                    math_object_name = sb.toString().replace("=", "")
                            .replace(":", " ").trim();
                    i = j;
                    break;
                } else {
                    if (sb.toString().isEmpty()) {
                        sb.append(line_array[j]);
                    } else {
                        sb.append(String.format(" %s", line_array[j]));
                    }
                }
            }

            //retrieve math object elements
            if (!math_object_name.isEmpty()) {
                for (int j = i; j < line_array.length; j++) {
                    if (line_array[j].matches(Regex.REGEX_FLOAT) || line_array[j].matches("\\d+")) {
                        Double val = Double.valueOf(line_array[j].replace("D","E"));
                        math_object_elements.add(val);
                        //math_object_elements.add(line_array[j]);
                    } else {
                        i = j - 1;
                        break;
                    }
                }
                mathObjects.put(math_object_name, math_object_elements);
            }
        }
        return mathObjects;
    }
}