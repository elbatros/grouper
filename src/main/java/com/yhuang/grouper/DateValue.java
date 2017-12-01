package com.yhuang.grouper;

/**
 * Created by yhuang on 11/30/17.
 */
import java.util.Properties;

public class DateValue implements Comparable<DateValue> {
    private int _value;
    private Properties _kvp;
    private String _signedColumnsValue;

    public DateValue(String y, String m, String d, int value) {
        _value = value;
        _kvp = new Properties();
        _kvp.put("y", y);
        _kvp.put("m", m);
        _kvp.put("d", d);
    }

    public int getValue() {
        return _value;
    }

    public String getSignedColumnsValue() {
        return _signedColumnsValue;
    }

    /**
     * sign row by input column name + its value:
     * such as: ('y' + y_value) + ('m' + m_value) + ('d' + d_value)
     *     or:  ('y' + y_value) + ('m' + m_value)
     *
     *
     * @param 'signedColumns
     */
    public void signWithColumns(String[] signedColumns) {
        StringBuilder columnsValue = new StringBuilder();
        for (String key : signedColumns) {
            // also append column key name (y/m/d) to distinguish:
            //   2016 12     value
            //   2016    12  value
            columnsValue.append(key).append(_kvp.getProperty(key));
        }
        _signedColumnsValue = columnsValue.toString();
    }


    public int compareTo(DateValue anotherDateValue) {
        return this._signedColumnsValue.compareTo(anotherDateValue._signedColumnsValue);
    }
}
