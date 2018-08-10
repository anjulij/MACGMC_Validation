package comparators.subpart_comparators;

import comparators.Tolerance;
import parsers.subparts.MathObjects.Vectors;

import java.util.List;
import java.util.Map;

public class VectorComparator {
    private Object[] failure;
    private Vectors v1;
    private Vectors v2;

    public VectorComparator(Vectors v1, Vectors v2){
        failure = new Object[2];
        this.v1 = v1;
        this.v2 = v2;
    }

    public Object[] getFailure(){
        /*if failure is empty there hasn't been failure*/
        return compareVectors();
    }

    private Object[] compareVectors() {
        boolean bool = true;
        Map<String, List<Double>> vector1 = v1.getVectors();
        Map<String, List<Double>> vector2 = v2.getVectors();

        /*Check if vectors have same labels*/
        if (vector1.keySet().size() != vector2.keySet().size()) {
            bool = false;
            failure[0] = "vector cardinality does not match";
            failure[1] = bool;
        } else {
            if (!vector1.keySet().containsAll(vector2.keySet())) {
                bool = false;
                failure[0] = "vector names do not match";
                failure[1] = bool;
            } else {
                /*Check if vectors have same size and values*/
                for (Map.Entry<String, List<Double>> object1 : vector1.entrySet()) {
                    String vector_name1 = object1.getKey();
                    List<Double> values1 = object1.getValue();
                    for (Map.Entry<String, List<Double>> object2 : vector2.entrySet()) {
                        String vector_name2 = object2.getKey();
                        List<Double> values2 = object2.getValue();
                        /*Check if vectors are same size*/
                        if (vector_name1.equals(vector_name2)) {
                            if (values1.size() != values2.size()) {
                                bool = false;
                                failure = new Object[2];
                                failure[0] = "vector value size does not match";
                                failure[1] = bool;
                            }
                            /*If the vectors are the same size check if equations have same values*/
                            else {
                                for (int i = 0; i < values1.size(); i++) {
                                    for (int j = 0; j < values1.size(); j++) {
                                        Double element1 = values1.get(i);
                                        Double element2 = values2.get(i);
                                        double diff = Math.abs(element1 - element2);
                                        Tolerance tolerance = new Tolerance(element1);
                                        double epsilon = tolerance.getEpsilon();
                                        if (!(diff <= epsilon)) {
                                            bool = false;
                                            failure = new Object[2];
                                            failure[0] = String.format("vector element values are not equal to each other: %f != %f", element1,element2);
                                            failure[1] = bool;
                                            break;
                                        }
                                    }
                                    if (!bool) {
                                        break;
                                    }
                                }
                            }
                        }
                        if (!bool) {
                            break;
                        }
                    }
                    if (!bool) {
                        break;
                    }
                }
            }
        }
        return failure;
    }
}