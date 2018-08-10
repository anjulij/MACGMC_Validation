package parsers;

import parsers.sections.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Section {
    /**
     * A section is a list of lines within a file ending at the start of another section of end of the document
     */
    private Map<String, Object> sectionDataMap;
    private int sectionStartIndex;
    private int sectionEndIndex;
    private List<String> fileList;
    private String sectionName;

    /*If treating Section like individual object separate from Output, use this constructor*/
    public Section(List<String> fileList, String sectionName) {
        this.sectionName = sectionName;
        this.fileList = fileList;

        sectionDataMap = new HashMap<>();
        sectionStartIndex = Integer.MAX_VALUE;
        sectionEndIndex = -1;

        setSectionBoundaries();

        if (getSectionName().matches(Regex.LABEL_SECTION_EFFECTIVE_PROPERTIES)) {
            EffectivePropertyResults e = new EffectivePropertyResults(fileList, getSectionStartIndex(), getSectionEndIndex());
            sectionDataMap.put(sectionName, e.getSectionDataMap());
        } else if (getSectionName().matches(Regex.LABEL_SECTION_TIME_BASED_OUTPUT)) {
            TimeBasedOutput t = new TimeBasedOutput(fileList, getSectionStartIndex(), getSectionEndIndex());
            sectionDataMap.put(sectionName, t.getSectionDataMap());
        }
        /*If there isn't a specified way to parse the data everything will be stored in a List of strings*/
        else{
            List<String> sectionList = new ArrayList<>();
            for (int i = sectionStartIndex; i <= sectionEndIndex ; i++) {
                sectionList.add(fileList.get(i));
            }
            sectionDataMap.put(sectionName, sectionList);
        }
    }

    /*If treating integrating each Section with Output, use this constructor*/
    public Section(List<String> fileList, int sectionStartIndex, int sectionEndIndex) {
        this.fileList = fileList;

        sectionDataMap = new HashMap<>();
        this.sectionStartIndex = sectionStartIndex;
        this.sectionEndIndex = sectionEndIndex;
        this.sectionName = fileList.get(sectionStartIndex).replace("*","").trim();

        if (getSectionName().matches(Regex.LABEL_SECTION_EFFECTIVE_PROPERTIES)) {
            EffectivePropertyResults e = new EffectivePropertyResults(fileList, sectionStartIndex, sectionEndIndex);
            sectionDataMap.put(sectionName, e.getSectionDataMap());
        } else if (getSectionName().matches(Regex.LABEL_SECTION_TIME_BASED_OUTPUT)) {
            TimeBasedOutput t = new TimeBasedOutput(fileList, sectionStartIndex, sectionEndIndex);
            sectionDataMap.put(sectionName, t.getSectionDataMap());
        }
        /*If there isn't a specified way to parse the data everything will be stored in a List of strings*/
        else{
            List<String> sectionList = new ArrayList<>();
            for (int i = sectionStartIndex; i <= sectionEndIndex ; i++) {
                sectionList.add(fileList.get(i));
            }
            sectionDataMap.put(sectionName, sectionList);
        }
    }

    public String getSectionName() {
        return sectionName;
    }

    public Map<String, Object> getSectionDataMap() {
        return sectionDataMap;
    }

    @Override
    public String toString() {
        try {
            return String.format("Section name: %s, Section start index: %s, Section end index: %s \n",
                    getSectionName(), getSectionStartIndex(), getSectionEndIndex());
        }catch(NullPointerException e){
            return "";
        }
    }

    public String toStringDataMap() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : sectionDataMap.entrySet()) {
            sb.append(sectionName).append("\n\n");
            try {
                Map<String, Map<String, Object>> section_map = (Map<String, Map<String, Object>>) entry.getValue();

                for (Map.Entry<String, Map<String, Object>> subsection : section_map.entrySet()) {
                    String subsection_name = subsection.getKey();
                    sb.append(subsection_name).append("\n");
                    Map<String, Object> map_subsection = subsection.getValue();

                    for (Map.Entry<String, Object> object : map_subsection.entrySet()) {
                        String object_label = object.getKey();
                        sb.append(object_label).append("\n");
                        Object object_value = object.getValue();
                        sb.append(object_value.toString()).append("\n");
                        sb.append("\n");
                    }
                }
            }catch (ClassCastException e){
                List<String> l = (ArrayList<String>) entry.getValue();
                //TODO: Minus two is because sectionEndIndex ends on next section. Should have better approach
                for (int i = 2; i < l.size()-2; i++) {
                    if(!l.get(i).trim().isEmpty()) {
                        sb.append(l.get(i).trim()).append("\n");
                    }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int getSectionStartIndex() {
        /*Returns -1 if section start index is not found*/
        if (sectionStartIndex == Integer.MAX_VALUE) {
            return -1;
        } else {
            return sectionStartIndex;
        }
    }

    public int getSectionEndIndex() {
        /*Returns -1 if section start index is not found*/
        return sectionEndIndex;
    }

    static boolean isASection(String line) {
        boolean value = false;
        if (line.matches(Regex.REGEX_SECTION)) {
            value = true;
        }
        return value;
    }

    static boolean isSpecifiedSection(String line, String sectionName) {
        boolean value = false;
        String regex = String.format(".*%s.*", sectionName);
        if (line.matches(regex)) {
            value = true;
        }
        return value;
    }

    private void setSectionBoundaries() {
        for (int i = 0; i < fileList.size(); i++) {
            String line = fileList.get(i);
            if (isSpecifiedSection(line, sectionName)) {
                sectionName = line.replace("*", "").trim();
                sectionStartIndex = i;
            }
            if (i > sectionStartIndex && isASection(line)) {
                sectionEndIndex = i;
                break;
            } else if (line.matches(Regex.REGEX_DOCUMENT_END)) {
                sectionEndIndex = i;
            }
        }
    }
}