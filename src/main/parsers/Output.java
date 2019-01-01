package parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static parsers.Section.isASection;

public class Output {
    /**
     * Output is a list of Sections which contain data
     */
    private List<Section> sections;
    private List<String> fileList;
    private String fileName;

    //TODO:  have option to output as [json] or csv add csv print to file for everything
    public Output(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        sections = new ArrayList<>();
        fileList = new ArrayList<>();
        while (scanner.hasNextLine()) {
            fileList.add(scanner.nextLine());
        }
        scanner.close();

    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Section s : sections) {
            sb.append(s.toString());
        }
        return sb.toString();
    }
    //TODO: complete toJson for output
    /*public String toJson(){
        StringBuilder sb = new StringBuilder();
        for (Section s : sections) {
            sb.append(s.toJson());
        }
        return sb.toString();
    }*/
    public String toStringDataMap() {
        StringBuilder sb = new StringBuilder();
        for (Section s : sections) {
            sb.append(s.toStringDataMap());
        }
        return sb.toString();
    }

    public List<String> getFileList() {
        return fileList;
    }

    public List<Section> getSections() {
        return sections;
    }

    /*This method retrieves every section name in the section list*/
    public List<String> getSectionNames() {
        List<String> sectionNames = new ArrayList<>();
        for (int i = 0; i < sections.size(); i++) {
            sectionNames.add(sections.get(i).getSectionName());
        }
        return sectionNames;
    }

    /*This method retrieves a section in the section list given the section name*/
    public Section getSection(String section_name) {
        for (Section section : sections) {
            if (section.getSectionName().matches(String.format(".*%s.*", section_name))) {
                return section;
            }
        }
        return null;
    }
    /*This method iterates through the list of section given a section name. It returns true if the section has been
    added, false if it has not been added*/
    public boolean contains(String section_name) {
        for (Section section : sections) {
            if (section.getSectionName().matches(String.format(".*%s.*", section_name))) {
                return true;
            }
        }
        return false;
    }

    /*This method retrieves a section given a section name which is passes to the Section class*/
    public void addSection(String section_name) {
        if (!contains(section_name)) {
            Section s = new Section(fileList, section_name);
            if (!(s.getSectionStartIndex() == -1 || s.getSectionEndIndex() == -1)) {
                sections.add(s);
            }
        }
    }

    /*This method retrieves a section given a section name and retrieves the section boundaries efficiently
    before passing this information the Section class*/
    public void addSectionEfficiently(String section_name) {
        if (!contains(section_name)) {
            int[] indices = setSectionBoundaries(section_name);
            if (!(indices[0] == Integer.MAX_VALUE || indices[1] == -1)) {
                Section s = new Section(fileList, indices[0], indices[1]);
                sections.add(s);
            }
        }
    }

    /*This method retrieves all possible sections in the document*/
    public void addAllSectionsInDocument() {
        List<String> allSectionNames = getAllSectionNamesInDocument();
        for (String section_name : allSectionNames) {
            sections.add(new Section(fileList, section_name));
        }
    }

    /*This method retrieves all possible section names in the document*/
    private List<String> getAllSectionNamesInDocument() {
        List<String> sectionNames = new ArrayList<>();
        for (String sectionLine : fileList) {
            if (isASection(sectionLine)) {
                String sectionName = sectionLine.replaceAll("\\*", "").trim();
                sectionNames.add(sectionName);
            }
        }
        return sectionNames;
    }

    /*This method integrates output and and section to improve efficiency*/
    private int[] setSectionBoundaries(String sectionName) {
        int sectionStartIndex = Integer.MAX_VALUE;
        int sectionEndIndex = -1;

        for (int i = 0; i < fileList.size(); i++) {
            String line = fileList.get(i);

            try {
                for (int j = 0; j < sections.size(); j++) {
                    Section s = sections.get(j);
                    if (line.replace("*", "").trim().matches(s.getSectionName())) {
                        i = s.getSectionEndIndex() - 1;
                    }
                }
            } catch (NullPointerException e) {
                System.out.println("Section List does not exist");
            }
            /*Retrieves the first index of the section*/
            if (Section.isSpecifiedSection(line, sectionName)) {
                sectionName = line.replace("*", "").trim();
                sectionStartIndex = i;
            }
            /*Retrieves the last index of the section*/
            if (i > sectionStartIndex && isASection(line)) {
                sectionEndIndex = i;
                break;
            } else if (line.matches(Regex.REGEX_DOCUMENT_END)) {
                sectionEndIndex = i;
            }
        }
        int[] sectionIndices = {sectionStartIndex, sectionEndIndex};
        return sectionIndices;
    }
}