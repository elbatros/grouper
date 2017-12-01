package com.yhuang.grouper;

/**
 * Created by yhuang on 11/30/17.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.util.Map;

public class GrouperMain {
    public final static String SPACE = " ";

    public GrouperMain() {
    }

    private List<DateValue>  readData() {
        List<DateValue> rows  = new ArrayList<>();

        try {
/*
            //For debug convenience
            String fileName = "input_file";
            System.setIn(new FileInputStream(fileName));
*/
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(System.in));

            // skip header row
            bufReader.readLine();

            String readLine;
            // read the following rows
            while ((readLine = bufReader.readLine()) != null) {
                System.out.println(readLine);
                String[] splitStr = readLine.trim().split("\\t");
                int size = splitStr.length;

                String y = SPACE;
                String m = SPACE;
                String d = SPACE;
                // if only value item exist, y, m, d is a space
                if (size > 1) {
                    y = splitStr[0];
                    m = splitStr[1];
                    d = splitStr[2];
                }

                int value = Integer.valueOf(splitStr[size - 1]);

                DateValue dataValue = new DateValue(y, m, d, value);
                rows.add(dataValue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Finished reading");
        return rows;
    }

    private void processInputFile(String[] columns) {
        List<DateValue> rows = readData();

        Grouper grouper = new Grouper(rows);

        // step 1:
        grouper.signRows(columns);
        // step 2:
        grouper.sortSignedRows();
        // step 3:
        //grouper.squashRows();
        List<Map.Entry<String,Integer>> pairList = grouper.squashRowsDirectly();

        grouper.outputSquashedRows(pairList, columns);

    }

    public static void printUsage() {
        System.out.println("Usage:");
        System.out.println("./grouper.sh y m d < input_file");
        System.out.println("./grouper.sh y m  < input_file");
        System.out.println("./grouper.sh y  < input_file");
        System.out.println("./grouper.sh < input_file");

        System.out.println("./grouper.sh y m d");
        System.out.println("./grouper.sh y m");
        System.out.println("./grouper.sh y");

        System.out.println("./grouper.sh");

        System.out.println("./grouper.sh h");
        System.exit(0);
    }

    // only allow 'y', 'm', 'd'
    enum Arguments {
        y, m, d;
        public static boolean contains(String s)
        {
            for(Arguments Arguments:values())
                if (Arguments.name().equals(s))
                    return true;
            return false;
        }
    };

    public static void validateArguments(String[] args) {
        if (args.length > 0) {
            if(args[0].equalsIgnoreCase("h")) {
                printUsage();
            }
            for (String arg : args) {
                if (!Arguments.contains(arg)) {
                    System.out.println("\nWrong input argument: " + arg + "\n");
                    printUsage();
                }
            }
        }
    }

    public static void main(String[] args) {
        // y m d < file
        // y m   < file
        // y     < file
        //       < file
        //
        validateArguments(args);

        // scan input
        //     2016 2 1 123
        //     2016 3 1 200
        GrouperMain starter = new GrouperMain();
        starter.processInputFile(args);
    }
}
