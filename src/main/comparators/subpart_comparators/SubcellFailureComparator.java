package comparators.subpart_comparators;

import parsers.subparts.subsectionSpecificFeatures.SubcellFailure;
import parsers.subparts.MathObjects.Equations;

import java.util.List;

public class SubcellFailureComparator {
    private Object[] failure;
    private SubcellFailure s1;
    private SubcellFailure s2;

    public SubcellFailureComparator(SubcellFailure s1, SubcellFailure s2) {
        failure = new Object[2];
        this.s1 = s1;
        this.s2 = s2;
        compareSubcells();
    }

    public Object[] getFailure() {
        /*if failure is empty there hasn't been failure*/
        return compareSubcells();
    }

    private Object[] compareSubcells() {
        boolean bool = true;
        List<Object> s1_list = s1.getSubcellFailureList();
        List<Object> s2_list = s2.getSubcellFailureList();

        if (s1_list.size() != s2_list.size()) {
            failure[0] = "elements do not contain the same number of lines";
            failure[1] = false;
        }
        for (int i = 0; i < s1_list.size(); i++) {
            Object o1 = s1_list.get(i);
            Object o2 = s2_list.get(i);

            boolean object1IsString = o1.getClass().getName().equals("java.lang.String");
            boolean object2IsString = o2.getClass().getName().equals("java.lang.String");
            boolean object1IsEquation = o1.getClass().getName().equals("parsers.subparts.MathObjects.Equations");
            boolean object2IsEquation = o2.getClass().getName().equals("parsers.subparts.MathObjects.Equations");

            if (object1IsString && object2IsString) {
                if (!o1.equals(o2)) {
                    failure[0] = "subcell failure lines don't match";
                    failure[1] = false;
                    break;
                }
            } else if (object1IsEquation && object2IsEquation) {
                Equations e1 = (Equations) o1;
                Equations e2 = (Equations) o2;
                if (e1.getMathEquations().keySet().containsAll(e2.getMathEquations().keySet())) {
                    EquationComparator equationComparator = new EquationComparator(e1, e2);
                    failure = equationComparator.getFailure();
                    break;
                }
            }
        }
        return failure;
    }
}
