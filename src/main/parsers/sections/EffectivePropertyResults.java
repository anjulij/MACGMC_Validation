package parsers.sections;

import parsers.Regex;
import parsers.sections.subsections.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EffectivePropertyResults {
    private List<String> fileList;
    private int sectionStartIndex;
    private int sectionEndIndex;
    private Map<String, Map<String, Object>> sectionDataMap;

    /**
     * This class retrieves the subcell identification tables in a single section
     */
    public EffectivePropertyResults(List<String> fileList, int sectionStartIndex, int sectionEndIndex) {
        this.fileList = fileList;
        this.sectionStartIndex = sectionStartIndex;
        this.sectionEndIndex = sectionEndIndex;
        sectionDataMap = new HashMap<>();
        identifyEffectivePropertyResult();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Map<String, Object>> entry : sectionDataMap.entrySet()) {
            String name = entry.getKey().replace("-", "");
            sb.append(name).append("\n");
            Map<String, Object> m = entry.getValue();
            for (Map.Entry<String, Object> e : m.entrySet()) {
                sb.append(String.format("%s: %s \n", e.getKey(), e.getValue().toString()));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public Map<String, Map<String, Object>> getSectionDataMap() {
        return sectionDataMap;
    }

    private void identifyEffectivePropertyResult() {
        for (int i = sectionStartIndex + 1; i < sectionEndIndex; i++) {
            String line = fileList.get(i);
            if (line.matches(Regex.REGEX_EFFECTIVE_PROPERTIES)) {
                int temp = i;
                i = getEffectivePropertyResults(temp);
            }
            if (line.matches(Regex.REGEX_DOCUMENT_END)) {
                break;
            } else if (line.matches(Regex.REGEX_SECTION)) {
                break;
            }
        }
    }

    private int getEffectivePropertyResults(int start) {
        int updatedIndex = -1;
        for (int i = start; i < sectionEndIndex; i++) {
            String line = fileList.get(i);
            if (line.matches(Regex.REGEX_DOCUMENT_END)) {
                updatedIndex = i;
                break;
            } else {
                if (line.matches(Regex.REGEX_SUBCELL_IDENTIFICATION)) {
                    int temp = i;
                    EffectivePropertiesSubpart subcellIdentification = new EffectivePropertiesSubpart(fileList, temp, sectionStartIndex, sectionEndIndex);
                    i = subcellIdentification.getSubsectionEndIndex() - 1;
                    updatedIndex = i;
                    sectionDataMap.put(subcellIdentification.getKey(), subcellIdentification.getValue());

                } else if (line.matches(Regex.REGEX_EFFECTIVE_PROPERTIES_AT)) {
                    int temp = i;
                    EffectivePropertiesSubpart effectivePropertiesAtTemp = new EffectivePropertiesSubpart(fileList, temp, sectionStartIndex, sectionEndIndex);
                    i = effectivePropertiesAtTemp.getSubsectionEndIndex() - 1;
                    updatedIndex = i;
                    sectionDataMap.put(effectivePropertiesAtTemp.getKey(), effectivePropertiesAtTemp.getValue());

                } else if (line.matches(Regex.REGEX_LAMINATE_RESULTS_AT)) {
                    int temp = i;
                    EffectivePropertiesSubpart laminateResultsAtTemp = new EffectivePropertiesSubpart(fileList, temp, sectionStartIndex, sectionEndIndex);
                    i = laminateResultsAtTemp.getSubsectionEndIndex() - 1;
                    updatedIndex = i;
                    sectionDataMap.put(laminateResultsAtTemp.getKey(), laminateResultsAtTemp.getValue());

                }
            }
        }
        return updatedIndex;
    }
}