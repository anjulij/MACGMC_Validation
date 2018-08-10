package comparators.subpart_comparators;

import comparators.Tolerance;
import parsers.subparts.Table;

import java.util.List;

public class TableComparator {
    private Object[] failure;
    private Table t1;
    private Table t2;

    public TableComparator(Table t1, Table t2){
        failure = new Object[2];
        this.t1 = t1;
        this.t2 = t2;
    }

    public Object[] getFailure(){
        /*if failure is empty there hasn't been failure*/
        return compareTables();
    }

    private Object[] compareTables() {
        boolean bool = true;
        List<List<List<Double>>> t1_data = t1.getParsedTableData();
        List<List<List<Double>>> t2_data = t2.getParsedTableData();

        /*See if tables have the same number of lines*/
        if (t1_data.size() != t2_data.size()) {
            bool = false;
            failure = new Object[2];
            failure[0] = "tables do not have the same number on lines";
            failure[1] = bool;
        } else {
            /*Compare if each line has the same number of elements*/
            for (int i = 0; i < t1_data.size(); i++) {
                List<List<Double>> t1_line = t1_data.get(i);
                List<List<Double>> t2_line = t2_data.get(i);
                if (t1_line.size() != t2_line.size()) {
                    bool = false;
                    failure = new Object[2];
                    failure[0] = "table lines do not have same number elements";
                    failure[1] = bool;
                    break;
                }
            }
            /*If compareTables still hasn't failed check elements on each line*/
            if (bool) {
                for (int i = 0; i < t1_data.size(); i++) {
                    List<List<Double>> t1_line = t1_data.get(i);
                    List<List<Double>> t2_line = t2_data.get(i);
                    for (int j = 0; j < t1_line.size(); j++) {
                        List<Double> t1_elements = t1_line.get(j);
                        List<Double> t2_elements = t2_line.get(j);
                        if (t1_elements.size() != t2_elements.size()) {
                            bool = false;
                            failure = new Object[2];
                            failure[0] = "table elements do not have same number of elements";
                            failure[1] = bool;
                            break;
                        }
                    }
                }
            }
            /*If compareTables still hasn't failed at this point check the value of each element on each line*/
            if (bool) {
                for (int i = 0; i < t1_data.size(); i++) {
                    List<List<Double>> t1_line = t1_data.get(i);
                    List<List<Double>> t2_line = t2_data.get(i);
                    for (int j = 0; j < t1_line.size(); j++) {
                        List<Double> t1_elements = t1_line.get(j);
                        List<Double> t2_elements = t2_line.get(j);
                        for (int k = 0; k < t1_elements.size(); k++) {
                            Double e1 = t1_elements.get(k);
                            Double e2 = t2_elements.get(k);
                            double diff = Math.abs(e1-e2);
                            Tolerance tolerance = new Tolerance(e1);
                            double epsilon = tolerance.getEpsilon();
                            if (!(diff <= epsilon)) {
                                bool = false;
                                failure = new Object[2];
                                failure[0] = String.format("Tables do not contain the same values: %f != %f", e1, e2);
                                failure[1] = bool;
                                break;
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