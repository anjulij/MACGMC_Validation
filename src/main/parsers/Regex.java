package parsers;

/**
 * This class contains all of the regular expressions used to parse output sectionDataMap
 */
public class Regex {
    public final static String REGEX_WORD;
    public final static String REGEX_BLANK_LINE;
    public final static String REGEX_FLOAT;
    private final static String REGEX_ROMAN_NUMERAL;
    private final static String GREEK_LETTERS;

    public final static String REGEX_SERIES_OF_DIGITS;
    public final static String REGEX_SERIES_OF_NUMBERS;

    private final static String REGEX_LABEL;
    public final static String REGEX_EQN;
    public final static String REGEX_VECTOR;

    /**Labels*/
    /*Regular expressions for effective property labels*/
    private final static String LABEL_SUBCELL;
    private final static String LABEL_EFFECTIVE_PROPERTIES_AT_TEMP;
    private final static String LABEL_LAMINATE_RESULTS_AT_TEMP;
    /*Time based output labels*/
    private final static String LABEL_SUBCELL_FAILURE;
    /*Section labels*/
    public final static String LABEL_SECTION_EFFECTIVE_PROPERTIES;
    public final static String LABEL_SECTION_TIME_BASED_OUTPUT;
    private final static String LABEL_DOCUMENT_END_1;
    private static final String LABEL_DOCUMENT_END_2;
    private final static String LABEL_SECTION_INDICATOR;
    /*Subcell table lable*/
    private final static String GREEK_SET;
    private final static String LABEL_SUBCELL_TABLE;

    /**Titles*/
    public final static String REGEX_SECTION;
    /*Regular expressions for effective property results*/
    public final static String REGEX_EFFECTIVE_PROPERTIES;
    public final static String REGEX_SUBCELL_IDENTIFICATION;
    public final static String REGEX_EFFECTIVE_PROPERTIES_AT;
    public final static String REGEX_LAMINATE_RESULTS_AT;
    /*Regular expression for subcell table*/
    public final static String REGEX_SUBCELL_TABLE;
    /*Regular expressions for time based output*/
    public final static String REGEX_SUBCELL_FAILURE;
    public final static String REGEX_TIME_BASED_OUTPUT;
    /*Regular expressions for end symbols*/
    public final static String REGEX_DOCUMENT_END;
    public final static String REGEX_SUBSECTION_END;

    static {
        REGEX_WORD = "[a-zA-Z]+";
        REGEX_BLANK_LINE = "^\\s*$";
        REGEX_FLOAT = "\\s*[-+]?\\d*\\.?\\d+([eEdD][-+]?\\d+)?";
        REGEX_ROMAN_NUMERAL = "M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})";
        GREEK_LETTERS = "(ALPHA)|(BETA)|(GAMMA)|(DELTA)|(EPSILON)|(ZETA)|(ETA)|(THETA)|(IOTA)|(KAPPA)|(LAMBDA)|(MU)|(NU)|(XI)|(OMICRON)|(PI)|(RHO)|(SIGMA)|(TAU)|(UPSILON)|(PHI)|(CHI)|(PSI)|(OMEGA)";


        REGEX_SERIES_OF_DIGITS = "(\\d+\\s+){2,}\\d\\s*";
        REGEX_SERIES_OF_NUMBERS = String.format("(%s\\s+){2,}%s\\s*", REGEX_FLOAT, REGEX_FLOAT);

        REGEX_LABEL = String.format("((%s)\\.)|((%s):)|(%s)", REGEX_WORD, REGEX_WORD, REGEX_WORD);
        REGEX_EQN = String.format(".*(%s|(\\d))+\\s+=\\s+\\d+.*", REGEX_LABEL);
        REGEX_VECTOR = String.format("((%s:)|%s:)(%s|\\d|%s)", REGEX_WORD, REGEX_FLOAT, REGEX_SERIES_OF_NUMBERS, REGEX_FLOAT);

        /**Labels*/
        /*Regular expressions for effective property labels*/
        LABEL_SUBCELL = "(SUBCELL)\\s+(IDENTIFICATION)\\s*(,)\\s+(VOLUME)\\s*(,)\\s+(AND)\\s+(MATERIAL)\\s+(ARRANGEMENT)\\s+(FOR)\\s+(LAYER)";
        LABEL_EFFECTIVE_PROPERTIES_AT_TEMP = "(EFFECTIVE)\\s+(PROPERTIES)\\s+(AT)\\s+(TEMPERATURE)";
        LABEL_LAMINATE_RESULTS_AT_TEMP = "(LAMINATE)\\s+(RESULTS)\\s+(AT)\\s+(TEMPERATURE)";
        /*Time based output labels*/
        LABEL_SUBCELL_FAILURE = "(SUBCELL)\\s+(FAILURE)";
        /*Section labels*/
        LABEL_SECTION_INDICATOR = "Section";
        LABEL_SECTION_EFFECTIVE_PROPERTIES = String.format(".*(\\s*(Effective)\\s*(Property)\\s*(Results)\\s*).*");
        LABEL_SECTION_TIME_BASED_OUTPUT = ".*((Time)\\s*(-*)\\s*((b|B)ased)\\s*(Output)).*";
        LABEL_DOCUMENT_END_1 = "MAIN: TIME EXCEEDED";
        LABEL_DOCUMENT_END_2 = "STOPPING:";
        /*Subcell table label*/
        GREEK_SET = String.format("((\\()*)(%s)((\\s*(,)\\s*(%s))*)((\\))*)",GREEK_LETTERS,GREEK_LETTERS);
        LABEL_SUBCELL_TABLE = String.format("((SUBCELL)\\s*(#))(\\s*(SUBCELL)\\s*(MATERIAL))(\\s*(SUBCELL)\\s*(VOLUME))");

        /**Titles*/
        REGEX_SECTION = String.format("\\*{5}\\s+%s\\s+(%s):\\s+([\\w\\s+]|[\\w\\-]){1,}\\*{5}",
                LABEL_SECTION_INDICATOR, REGEX_ROMAN_NUMERAL);
        /*Regular expressions for effective property results*/
        //TODO: make effective properties support spaces
        REGEX_EFFECTIVE_PROPERTIES = String.format("\\s*(-{2,})\\s*(((%s\\s*(,*))\\s*)+)(((=)\\s*(%s))*)\\s*(((%s\\s*)+\\d)*)\\s*(-{2,})\\s*", REGEX_WORD,REGEX_FLOAT,REGEX_WORD);
        REGEX_SUBCELL_IDENTIFICATION = String.format("(-{2,})\\s*(%s)\\s*\\d(-{2,})", LABEL_SUBCELL);
        REGEX_EFFECTIVE_PROPERTIES_AT = String.format("(-{2,})\\s*(%s)\\s*(=)\\s*(%s)\\s*(((FOR)\\s*(LAYER)\\s*\\d)*)(-{2,})",
                LABEL_EFFECTIVE_PROPERTIES_AT_TEMP, REGEX_FLOAT);
        REGEX_LAMINATE_RESULTS_AT = String.format("(-{2,})\\s*(%s)\\s*(=)\\s*(%s)\\s*(-{2,})",
                LABEL_LAMINATE_RESULTS_AT_TEMP, REGEX_FLOAT);
        /*Regular expression for subcell table*/
        REGEX_SUBCELL_TABLE = String.format("\\s*(%s)\\s*(%s)\\s*",GREEK_SET,LABEL_SUBCELL_TABLE);
        /*Regular expressions for time based output*/
        REGEX_SUBCELL_FAILURE = String.format("\\s*\\*{5,}\\s+(%s)\\s+\\*{5,}\\s*", LABEL_SUBCELL_FAILURE);
        REGEX_TIME_BASED_OUTPUT = String.format("\\s*\\d+\\s*((\\w+:\\s*%s+\\s*){3})", REGEX_FLOAT);
        /*Regular expressions for end symbols*/
        REGEX_DOCUMENT_END = String.format(".*(%s|%s).*", LABEL_DOCUMENT_END_1, LABEL_DOCUMENT_END_2);
        REGEX_SUBSECTION_END = "-{5,}";
    }
}