package comparators;

import comparators.subpart_comparators.*;
import parsers.Output;
import parsers.Section;
import parsers.subparts.subsectionSpecificFeatures.SubcellFailure;
import parsers.subparts.MathObjects.Equations;
import parsers.subparts.MathObjects.Vectors;
import parsers.subparts.MatrixObjects.Matrix;
import parsers.subparts.Table;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

//TODO: add line number to parsing so line number can be isolated
public class OutputComparator {
    private Output output_1;
    private Output output_2;

    public OutputComparator(Output output_1, Output output_2) throws FileNotFoundException {
        this.output_1 = output_1;
        this.output_2 = output_2;
        writeToFileResults();
    }

    public void writeToFileResults() throws FileNotFoundException {
        String fileName = String.format("%s_%s_Results.txt", output_1.getFileName().replace("/", "").replace(".", ""),
                output_2.getFileName().replace("/", "").replace(".", ""));
        //File results = new File(fileName);
        PrintWriter pw = new PrintWriter(fileName);

        pw.println(String.format("Comparison between %s and %s\n", output_1.getFileName(), output_2.getFileName()));
        pw.println("1. Number of sections and section names check");
        pw.println("\t" + Arrays.toString(isEqualSectionNames()).replace("[", "").replace("]", ""));

        pw.println("\n2. Number of subsections check");
        List<Object[]> failureSubsectionCardinality = compareSubsectionCardinality();
        for (int i = 0; i < failureSubsectionCardinality.size(); i++) {
            Object[] l2 = failureSubsectionCardinality.get(i);
            if (i > 0) {
                pw.println("");
            }
            for (Object o : l2) {
                pw.println("\t" + o);
            }
        }

        pw.println("\n3. Subsection names check");
        List<Object[]> failureSubsectionNames = compareSubsectionNames();
        if (failureSubsectionNames.isEmpty()) {
            pw.println("Check 2. did not pass so this check was not evaluated");
        } else {
            for (int i = 0; i < failureSubsectionNames.size(); i++) {
                Object[] l2 = failureSubsectionNames.get(i);
                if (i > 0) {
                    pw.println("");
                }
                for (Object o : l2) {
                    pw.println("\t" + o);
                }
            }
        }

        pw.println("\n4. Subsection elements check");
        List<Object[]> failureSubsectionElements = compareSubsectionElements();
        for (int i = 0; i < failureSubsectionElements.size(); i++) {
            Object[] l2 = failureSubsectionElements.get(i);
            if (i > 0) {
                pw.println("");
            }
            for (Object o : l2) {
                pw.println("\t" + o);
            }
        }
        pw.close();
    }

    public Object[] isEqualSectionNames() {
        /*True iff the lists contain the same sections with the same cardinality, both list must not be null*/
        List<String> list_1 = output_1.getSectionNames();
        List<String> list_2 = output_2.getSectionNames();
        boolean bool;
        Object[] failure = new Object[2];
        if (list_1.isEmpty()) {
            failure[0] = false;
            failure[1] = "output 1 is empty";
            return failure;
        } else if (list_2.isEmpty()) {
            failure[0] = false;
            failure[1] = "output 2 is empty";
            return failure;
        } else {
            boolean list1_isASubsetOf_List2 = true;
            for (String s : list_1) {
                if (!list_2.contains(s)) {
                    list1_isASubsetOf_List2 = false;
                }
            }
            boolean list2_isASubsetOf_List1 = true;
            for (String s : list_2) {
                if (!list_1.contains(s)) {
                    list2_isASubsetOf_List1 = false;
                }
            }
            bool = (list1_isASubsetOf_List2 && list2_isASubsetOf_List1);
            failure[0] = bool;
            if (bool) {
                failure[1] = "section names and number of sections match in each output file";
            } else {
                failure[1] = "section names and number of sections do not match in each output file";

            }
            return failure;
        }
    }

    public List<Object[]> compareSubsectionCardinality() {
        List<Object[]> failures = new ArrayList<>();
        Object[] failure;
        List<Section> sectionList_1 = output_1.getSections();
        List<Section> sectionList_2 = output_2.getSections();

        /*For each section in each output file compare the set of all subsection names*/
        for (Section s1 : sectionList_1) {
            for (Section s2 : sectionList_2) {
                if (s1.getSectionName().equals(s2.getSectionName())) {
                    Map<String, Object> s1_map = s1.getSectionDataMap();
                    Map<String, Object> s2_map = s2.getSectionDataMap();

                    Set<String> subsection_name_set1 = new HashSet<>();
                    Set<String> subsection_name_set2 = new HashSet<>();

                    try {
                        for (Map.Entry<String, Object> entry1 : s1_map.entrySet()) {
                            Map<String, Map<String, Object>> section_map1 = (Map<String, Map<String, Object>>) entry1.getValue();
                            subsection_name_set1 = section_map1.keySet();
                        }
                        for (Map.Entry<String, Object> entry2 : s2_map.entrySet()) {
                            Map<String, Map<String, Object>> section_map2 = (Map<String, Map<String, Object>>) entry2.getValue();
                            subsection_name_set2 = section_map2.keySet();
                        }
                        if (!(subsection_name_set1.size() == subsection_name_set2.size())) {
                            //TODO: if you get more than x number of errors suppress error messages
                            /*PSA: this becomes VERY long if there are multiple subsections that do not match.
                            If you just want to know that there exist subsections that don't match, not their specific names, comment out the following
                            if...else if sequence*/
                            String extraSubsection = "";
                            String failedFile = "";
                            if (subsection_name_set1.size() > subsection_name_set2.size()) {
                                Set<String> temp1 = new HashSet<>();
                                temp1.addAll(subsection_name_set1);
                                Set<String> temp2 = new HashSet<>();
                                temp2.addAll(subsection_name_set2);
                                temp1.removeAll(temp2);
                                failedFile = output_2.getFileName();
                                extraSubsection = String.valueOf(temp1);
                            } else if (subsection_name_set2.size() > subsection_name_set1.size()) {
                                Set<String> temp1 = new HashSet<>();
                                temp1.addAll(subsection_name_set1);
                                Set<String> temp2 = new HashSet<>();
                                temp2.addAll(subsection_name_set2);
                                temp2.removeAll(temp1);
                                extraSubsection = String.valueOf(temp2);
                                failedFile = output_1.getFileName();
                            }
                            failure = new Object[5];
                            failure[0] = false;
                            failure[1] = s1.getSectionName();
                            failure[2] = failedFile;
                            failure[3] = "subsection cardinality does not match in output files";
                            failure[4] = extraSubsection;
                            failures.add(failure);
                        } else {
                            failure = new Object[3];
                            failure[0] = true;
                            failure[1] = s1.getSectionName();
                            failure[2] = "both output files contain matching subsection cardinality";
                            failures.add(failure);
                        }
                    }
                    /*If it is a list there are no subsections*/ catch (ClassCastException e) {
                        failure = new Object[3];
                        failure[0] = false;
                        failure[1] = s1.getSectionName();
                        failure[2] = "section does not contain subsections in both output files";
                        failures.add(failure);
                    }
                }
            }
        }
        return failures;
    }

    public List<Object[]> compareSubsectionNames() {
        //TODO: add specific for non matching names
        List<Object[]> failures = new ArrayList<>();
        Object[] failure;
        /*iterate through compareSubsectionCardinality failure list elements that passed and compare subsection names*/
        List<Object[]> subsectionCardinalityFailures = compareSubsectionCardinality();
        for (Object[] subsectionCardinalityFailure : subsectionCardinalityFailures) {
            Boolean val = (Boolean) subsectionCardinalityFailure[0];
            if (val) {
                String sectionName = (String) subsectionCardinalityFailure[1];
                Section s1 = output_1.getSection(sectionName);
                Section s2 = output_2.getSection(sectionName);

                Map<String, Object> s1_map = s1.getSectionDataMap();
                Map<String, Object> s2_map = s2.getSectionDataMap();

                Set<String> subsection_name_set1 = new HashSet<>();
                Set<String> subsection_name_set2 = new HashSet<>();

                for (Map.Entry<String, Object> entry1 : s1_map.entrySet()) {
                    Map<String, Map<String, Object>> section_map1 = (Map<String, Map<String, Object>>) entry1.getValue();
                    subsection_name_set1 = section_map1.keySet();
                }
                for (Map.Entry<String, Object> entry2 : s2_map.entrySet()) {
                    Map<String, Map<String, Object>> section_map2 = (Map<String, Map<String, Object>>) entry2.getValue();
                    subsection_name_set2 = section_map2.keySet();
                }
                if (subsection_name_set1.containsAll(subsection_name_set2)) {
                    failure = new Object[3];
                    failure[0] = true;
                    failure[1] = s1.getSectionName();
                    failure[2] = "subsection names match in both output files";
                    failures.add(failure);
                } else {
                    failure = new Object[3];
                    failure[0] = false;
                    failure[1] = s1.getSectionName();
                    failure[2] = "subsection names do not match in both output files";
                    failures.add(failure);
                }
            }
        }
        return failures;
    }

    public List<Object[]> compareSubsectionElements() {
        List<Object[]> failures = new ArrayList<>();
        Object[] failure;

        List<Section> sectionList_1 = output_1.getSections();
        List<Section> sectionList_2 = output_2.getSections();

        for (Section s1 : sectionList_1) {
            for (Section s2 : sectionList_2) {
                if (s1.getSectionName().equals(s2.getSectionName())) {

                    String sectionName = s1.getSectionName();
                    try {
                        Map<String, Object> s1_map = s1.getSectionDataMap();
                        Map<String, Object> s2_map = s2.getSectionDataMap();

                        Map<String, Map<String, Object>> s1_data_map = new HashMap<>();
                        Map<String, Map<String, Object>> s2_data_map = new HashMap<>();

                        for (Map.Entry<String, Object> s1_data : s1_map.entrySet()) {
                            s1_data_map = (Map<String, Map<String, Object>>) s1_data.getValue();
                        }
                        for (Map.Entry<String, Object> s2_data : s2_map.entrySet()) {
                            s2_data_map = (Map<String, Map<String, Object>>) s2_data.getValue();
                        }

                        /*Retrieve subsections*/
                        for (Map.Entry<String, Map<String, Object>> subsection1 : s1_data_map.entrySet()) {
                            String subsection_name1 = subsection1.getKey();

                            for (Map.Entry<String, Map<String, Object>> subsection2 : s2_data_map.entrySet()) {
                                String subsection_name2 = subsection2.getKey();

                                /*If subsections have matching names, compare elements in subsection*/
                                if (subsection_name1.equals(subsection_name2)) {
                                    Map<String, Object> value1 = subsection1.getValue();
                                    Map<String, Object> value2 = subsection2.getValue();

                                    /*Retrieve subsection elements*/
                                    for (Map.Entry object1 : value1.entrySet()) {
                                        for (Map.Entry object2 : value2.entrySet()) {

                                             /*PSA: this becomes VERY long if there are multiple subsection elements that do not match.
                                              If you just want to know that there exist subsection elements that don't match, not the specific elements
                                              break the for loop when an error is found like in subpart_comparators.MatrixComparator*/

                                            /*Compare subsection elements based on type*/
                                            if (object1.getKey().equals(object2.getKey())) {
                                                boolean object1IsMatrix = object1.getValue().getClass().getName().equals("parsers.subparts.MatrixObjects.Matrix");
                                                boolean object2IsMatrix = object2.getValue().getClass().getName().equals("parsers.subparts.MatrixObjects.Matrix");
                                                if (object1IsMatrix && object2IsMatrix) {
                                                    Matrix m1 = (Matrix) object1.getValue();
                                                    Matrix m2 = (Matrix) object2.getValue();
                                                    MatrixComparator matrixComparator = new MatrixComparator(m1, m2);
                                                    failure = new Object[4];
                                                    Object[] f = matrixComparator.getFailure();
                                                    if (!(f[0] == null)) {
                                                        failure[0] = f[0];
                                                        failure[1] = sectionName;
                                                        failure[2] = String.format("Subsection name: " + subsection_name1);
                                                        failure[3] = f[1];
                                                        failures.add(failure);
                                                    }
                                                }
                                                boolean object1IsSubcellFailure = object1.getValue().getClass().getName().equals("parsers.sections.subsections.subsectionSpecificFeatures.SubcellFailure");
                                                boolean object2IsSubcellFailure = object2.getValue().getClass().getName().equals("parsers.sections.subsections.subsectionSpecificFeatures.SubcellFailurex");
                                                if (object1IsSubcellFailure && object2IsSubcellFailure) {
                                                    SubcellFailure subcell1 = (SubcellFailure) object1.getValue();
                                                    SubcellFailure subcell2 = (SubcellFailure) object2.getValue();
                                                    SubcellFailureComparator subcellFailureComparator = new SubcellFailureComparator(subcell1, subcell2);
                                                    failure = new Object[4];
                                                    Object[] f = subcellFailureComparator.getFailure();
                                                    if (!(f[0] == null)) {
                                                        failure[0] = f[0];
                                                        failure[1] = sectionName;
                                                        failure[2] = String.format("Subsection name: " + subsection_name1);
                                                        failure[3] = f[1];
                                                        failures.add(failure);
                                                    }
                                                }
                                                boolean object1IsTable = object1.getValue().getClass().getName().equals("parsers.subparts.Table");
                                                boolean object2IsTable = object2.getValue().getClass().getName().equals("parsers.subparts.Table");
                                                if (object1IsTable && object2IsTable) {
                                                    Table t1 = (Table) object1.getValue();
                                                    Table t2 = (Table) object2.getValue();
                                                    TableComparator tableComparator = new TableComparator(t1, t2);
                                                    failure = new Object[4];
                                                    Object[] f = tableComparator.getFailure();
                                                    if (!(f[0] == null)) {
                                                        failure[0] = f[0];
                                                        failure[1] = sectionName;
                                                        failure[2] = String.format("Subsection name: " + subsection_name1);
                                                        failure[3] = f[1];
                                                        failures.add(failure);
                                                    }
                                                }

                                                boolean object1IsVector = object1.getValue().getClass().getName().equals("parsers.subparts.MathObjects.Vectors");
                                                boolean object2IsVector = object2.getValue().getClass().getName().equals("parsers.subparts.MathObjects.Vectors");
                                                if (object1IsVector && object2IsVector) {
                                                    Vectors v1 = (Vectors) object1.getValue();
                                                    Vectors v2 = (Vectors) object2.getValue();
                                                    VectorComparator vectorComparator = new VectorComparator(v1, v2);
                                                    failure = new Object[4];
                                                    Object[] f = vectorComparator.getFailure();
                                                    if (!(f[0] == null)) {
                                                        failure[0] = f[0];
                                                        failure[1] = sectionName;
                                                        failure[2] = String.format("Subsection name: " + subsection_name1);
                                                        failure[3] = f[1];
                                                        failures.add(failure);
                                                    }
                                                }

                                                boolean object1IsEquation = object1.getValue().getClass().getName().equals("parsers.subparts.MathObjects.Equations");
                                                boolean object2IsEquation = object2.getValue().getClass().getName().equals("parsers.subparts.MathObjects.Equations");
                                                if (object1IsEquation && object2IsEquation) {
                                                    Equations e1 = (Equations) object1.getValue();
                                                    Equations e2 = (Equations) object2.getValue();
                                                    EquationComparator equationComparator = new EquationComparator(e1, e2);
                                                    failure = new Object[4];
                                                    Object[] f = equationComparator.getFailure();
                                                    if (!(f[0] == null)) {
                                                        failure[0] = f[0];
                                                        failure[1] = sectionName;
                                                        failure[2] = String.format("Subsection name: " + subsection_name1);
                                                        failure[3] = f[1];
                                                        failures.add(failure);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    /*If it is a list there are no subsections*/ catch (ClassCastException e) {
                        failure = new Object[3];
                        failure[0] = false;
                        failure[1] = s1.getSectionName();
                        failure[2] = "section does not contain subsections in both output files";
                        failures.add(failure);
                    }
                }
            }
        }
        if (failures.isEmpty()) {
            failure = new Object[2];
            failure[0] = true;
            failure[1] = "subsection elements match in both output files";
            failures.add(failure);
        }
        return failures;
    }
}