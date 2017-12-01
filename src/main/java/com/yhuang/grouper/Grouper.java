package com.yhuang.grouper;

/**
 * Created by yhuang on 11/30/17.
 */
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

public class Grouper {
    private List<DateValue> _rows;
    private Map<String, Integer> _signColumnsValueKVP;

    public Grouper(List<DateValue> rows) {
        _rows = rows;
    }

    /**
     * use the specified columns plus theirs values to sign a row
     * For example:
     * 1. sign value by "y_yValue + m_mValue + d_dValue"
     * 2. sign value by "y_yValue"
     * @param coloums
     */
    public void signRows(String[] coloums) {
        for (DateValue dateValue : _rows) {
            dateValue.signWithColumns(coloums);
        }
    }

    /**
     * use "signed columns" to sort the rows
     */
    public void sortSignedRows() {
        Collections.sort(_rows);
    }

    /*
     *  Note: we should NOT use a hashmap to store key of "signed columns" with its value
     *        because two different "signed columns" might have the same hash value.
     *        Now we use a pair list to store instance of signedColumn + value
     *
     *  this method iterates the rows and merge same signed_columns rows to accumulate its value
     *
     * @param pairList
     * @param coloums
     */
    public List<Entry<String,Integer>> squashRowsDirectly() {
        _signColumnsValueKVP = new HashMap<>();

        List<Entry<String,Integer>> pairList = new ArrayList<>();
        String oldSig = _rows.get(0).getSignedColumnsValue();
        int count = 0;

        Entry<String,Integer> oldPair = new SimpleEntry<>(oldSig, count);
        for (DateValue dateValue : _rows) {
            String sig = dateValue.getSignedColumnsValue();
            int value = dateValue.getValue();

            if (sig.equalsIgnoreCase(oldSig)) {
                count = oldPair.getValue() + value;
                oldPair.setValue(count);
            } else {
                pairList.add(oldPair);
                oldPair = new SimpleEntry<>(sig, value);;
            }
            oldSig = sig;
        }

        // when the very end pair is not the same as the one before it,
        // add the very end pair into pairList
        if (!pairList.isEmpty()) {
            int size = pairList.size();
            Entry<String, Integer> lastPair = pairList.get(size - 1);
            String key = lastPair.getKey();
            if (!key.equalsIgnoreCase(oldPair.getKey())) {
                pairList.add(oldPair);
            }
        }

        return pairList;
    }

    public void outputSquashedRows(List<Entry<String,Integer>> pairList, String[] coloums) {
        boolean hasSingleValue = false;
        int value = 0;
        StringBuilder emptyKeyBuilder = new StringBuilder();
        for (String column : coloums) {
            emptyKeyBuilder.append(column).append(GrouperMain.SPACE);
        }
        String emptyKey = emptyKeyBuilder.toString();

        System.out.print("\n\nGrouped by: " + emptyKey + "\n");
        for (Entry<String,Integer> pair : pairList) {
            String key = pair.getKey();
            value = pair.getValue();
            if (key.equalsIgnoreCase(emptyKey)) {
                hasSingleValue = true;
            } else {
                System.out.println(key.replace('y', ' ')
                        .replace('m', '\t').replace('d', '\t') + "\t" + value);
            }
        }

        if (hasSingleValue) {
            System.out.println("\t\t\t" + value);
        }
    }
}
