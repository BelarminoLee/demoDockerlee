package mz.co.attendance.control.components.utils;

import java.io.Serializable;
import java.util.List;

public interface CustomValueChangeListener<T> extends Serializable {

    default void getValue(String value, String actionType) {
    }

    default void getListValue(List<T> value, String actionType) {
    }

    default void getObjectValue(Object value, String actionType) {
    }

    default boolean isValid(Object value){
        return true;
    }
}
