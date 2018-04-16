package com.mgrmobi.brandbeat.entity.mappers.base;

import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public abstract class BaseMapper<T, E> {

    @Nullable
    public abstract T mapTo(E entity);

    public List<T> mapTo(@Nullable Collection<E> entities) {
        if (entities == null) {
            return Collections.<T>emptyList();
        }
        List<T> result = new ArrayList<>(entities.size());
        for (E e : entities) {
            T mapped = mapTo(e);
            if (mapped != null) {
                result.add(mapped);
            }
        }
        return result;
    }

    @Nullable
    protected Uri parseUri(String path) {
        if (path == null) {
            return null;
        }

        try {
            return Uri.parse(path);
        } catch (Exception e) {
            return null;
        }
    }

    protected long secondsToMillis(long seconds) {
        return TimeUnit.SECONDS.toMillis(seconds);
    }

    protected static int getAge(long birthdayTimestampInSec) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(new Date(TimeUnit.SECONDS.toMillis(birthdayTimestampInSec)));
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    protected String toCamelCase(String text) {
        if (text == null) {
            return "";
        }
        if (text.isEmpty()) {
            return text;
        }
        StringBuilder result = new StringBuilder();
        String[] parts = text.split(" ");
        for (String s : parts) {
            result.append(s.substring(0, 1).toUpperCase());
            if (s.length() > 1) {
                result.append(s.substring(1));
            }
            result.append(' ');
        }
        return result.toString();
    }
}
