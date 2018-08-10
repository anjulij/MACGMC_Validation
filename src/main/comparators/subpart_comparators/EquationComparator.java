package comparators.subpart_comparators;

import comparators.Tolerance;
import parsers.subparts.MathObjects.Equations;

import java.util.List;
import java.util.Map;

public class EquationComparator {
    private Object[] failure;
    private Equations e1;
    private Equations e2;

    public EquationComparator(Equations e1, Equations e2){
        failure = new Object[2];
        this.e1 = e1;
        this.e2 = e2;
    }
    public Object[] getFailure(){
        /*if failure is empty there hasn't been failure*/
        return compareEquations();
    }
    private Object[] compareEquations() {
        boolean bool = true;
        Map<String, List<Double>> equation1 = e1.getMathEquations();
        Map<String, List<Double>> equation2 = e2.getMathEquations();

        /*Check if equations have same labels*/
        try {
            if (equation1.keySet().size() != equation2.keySet().size()) {
                bool = false;
                failure[0] = bool;
                failure[1] = "equation cardinality does not match";
            } else {
                if (!equation1.keySet().containsAll(equation2.keySet())) {
                    bool = false;
                    failure[0] = bool;
                    failure[1] = "equation names do not match";
                } else {
                    /*Check if equations have same size and values*/
                    for (Map.Entry<String, List<Double>> object1 : equation1.entrySet()) {
                        String equation_name1 = object1.getKey();
                        List<Double> values1 = object1.getValue();
                        for (Map.Entry<String, List<Double>> object2 : equation2.entrySet()) {
                            String equation_name2 = object2.getKey();
                            List<Double> values2 = object2.getValue();

                            /*Check if equation are same size*/
                            if (equation_name1.equals(equation_name2)) {
                                if (values1.size() != values2.size()) {
                                    bool = false;
                                    failure = new Object[2];
                                    failure[0] = bool;
                                    failure[1] = "equation value size does not match";
                                }
                                /*If the equations are the same size check if equations have same values*/
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
                                                failure[0] = bool;
                                                failure[1] = String.format("equation element values are not equal to each other: %f != %f", element1, element2);
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
        }catch (NullPointerException e){

        }
        return failure;
    }
}