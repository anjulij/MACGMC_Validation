import comparators.OutputComparator;
import parsers.Output;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //TODO: Documentation: if you wanted to add this type of output, how would you go about it
        long tStart = System.currentTimeMillis();

        /*Add your initial files here*/
        //Sample file
        Output dummydoc = new parsers.Output("files/sampleFile/dummydoc.out");

        /*Add files that you want to compare here*/
        //Sample file
        Output dummydocTest = new parsers.Output("newFiles/sampleFile/dummydoc.out");

        /*Add sections you want to compare here*/
        dummydoc.addSectionEfficiently("Section II:");
        dummydoc.addSectionEfficiently("Section III:");
        dummydocTest.addSectionEfficiently("Section III:");
        dummydocTest.addSectionEfficiently("Section II:");

        System.out.println(dummydoc.toString());
        //System.out.println(dummydoc.toStringDataMap());
        
        /*Compare output here*/
        OutputComparator ocTest = new OutputComparator(dummydoc, dummydocTest);


        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        if (tDelta >= 1000) {
            double time = (double) tDelta / 1000;
            System.out.printf("Time elapsed: %.4f s\n", time);
        } else {
            System.out.printf("Time elapsed: %d ms\n", tDelta);
        }
    }
}