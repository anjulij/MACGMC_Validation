import comparators.OutputComparator;
import parsers.Output;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //TODO: Documentation: if you wanted to add this type of output, how would you go about it
        long tStart = System.currentTimeMillis();

        /*Add your initial files here*/
        Output output1a = new parsers.Output("files/example_1a/example_1a.out");
        Output output1b = new parsers.Output("files/example_1b/example_1b.out");
        Output output1c = new parsers.Output("files/example_1c/example_1c.out");
        Output output1d = new parsers.Output("files/example_1d/example_1d.out");
        Output output2a = new parsers.Output("files/example_2a/example_2a.out");
        Output output2c = new parsers.Output("files/example_2c/example_2c.out");
        Output output3a = new parsers.Output("files/example_3a/example_3a.out");
        Output output4a = new parsers.Output("files/example_4a/example_4a.out");
        Output output5b = new parsers.Output("files/example_5b/example_5b.out");
        Output output6a = new parsers.Output("files/example_6a/example_6a.out");
        Output output7b = new parsers.Output("files/example_7b/example_7b.out");

        //Sample file
        Output dummydoc = new parsers.Output("files/sampleFile/dummydoc.out");

        /*Add files that you want to compare here*/
        //Sample file
        Output dummydocTest = new parsers.Output("newFiles/sampleFile/dummydoc.out");

        /*Add sections you want to compare here*/
        output1a.addSectionEfficiently("Section I:");
        output1a.addSectionEfficiently("Section II:");

        output1b.addSectionEfficiently("Section I:");
        output1b.addSectionEfficiently("Section II:");
        output1b.addSectionEfficiently("Section III:");

        output1c.addSectionEfficiently("Section I:");
        output1b.addSectionEfficiently("Section II:");
        output1b.addSectionEfficiently("Section III:");

        output1d.addSectionEfficiently("Section I:");
        output1d.addSectionEfficiently("Section II:");
        output1d.addSectionEfficiently("Section III:");

        output2a.addSectionEfficiently("Section I:");
        output2a.addSectionEfficiently("Section II:");
        output2a.addSectionEfficiently("Section III:");

        output2c.addSectionEfficiently("Section I:");
        output2c.addSectionEfficiently("Section II:");
        output2c.addSectionEfficiently("Section II:");

        output3a.addSectionEfficiently("Section I:");
        output3a.addSectionEfficiently("Section II:");
        output3a.addSectionEfficiently("Section II:");

        output4a.addSectionEfficiently("Section I:");
        output4a.addSectionEfficiently("Section II:");
        output4a.addSectionEfficiently("Section III:");

        output5b.addSectionEfficiently("Section I:");
        output5b.addSectionEfficiently("Section II:");
        output5b.addSectionEfficiently("Section III:");


        output6a.addSectionEfficiently("Section I:");
        output6a.addSectionEfficiently("Section II:");
        output6a.addSectionEfficiently("Section III:");

        output7b.addSectionEfficiently("Section I:");
        output7b.addSectionEfficiently("Section II:");
        output7b.addSectionEfficiently("Section III:");

        dummydoc.addSectionEfficiently("Section II:");
        dummydoc.addSectionEfficiently("Section III:");
        dummydocTest.addSectionEfficiently("Section III:");
        dummydocTest.addSectionEfficiently("Section II:");
        System.out.println(dummydoc.toString());
        System.out.println(dummydoc.toStringDataMap());

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