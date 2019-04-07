package com.wjyup.coolq.util;

import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.wjyup.coolq.exception.PredicateException;
import com.wjyup.coolq.exception.UnexpectedException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Predicates {
    private static final Pattern IP_PATTERN = Pattern
            .compile("^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$");

    /**
     * 检查数值范围
     *
     * @param number 数值
     * @param min 最小值
     * @param max 最大值
     * @param msg 消息
     * @throws PredicateException
     */
    public static void ensureNumberRange(int number, int min, int max, String msg) throws PredicateException {
        if (number < min || number > max) {
            throw new PredicateException(msg);
        }
    }
    public static void ensureNumberRange(int number, int min, int max, Supplier<String> msg) throws PredicateException {
        ensureNumberRange(number, min, max, msg.get());
    }

    public static void ensureNumberRange(long number, long min, long max, String msg) throws PredicateException {
        if (number < min || number > max) {
            throw new PredicateException(msg);
        }
    }
    public static void ensureNumberRange(long number, long min, long max, Supplier<String> msg) throws PredicateException {
        ensureNumberRange(number, min, max, msg.get());
    }

    public static void ensureTrue(boolean isTrue, String msg) throws PredicateException {
        if(!isTrue) {
            throw new PredicateException(msg);
        }
    }
    public static void ensureTrue(boolean isTrue, Supplier<String> msg) throws PredicateException {
        ensureTrue(isTrue, msg.get());
    }

    /**
     * 验证IP地址格式
     *
     * @param ip IP
     * @param msg 消息
     * @throws PredicateException
     */
    public static void ensureIP(String ip, String msg) throws PredicateException {
        if (!IP_PATTERN.matcher(ip).matches()) {
            throw new PredicateException(msg);
        }
    }
    public static void ensureIP(String ip, Supplier<String> msg) throws PredicateException {
        ensureIP(ip, msg.get());
    }

    /**
     * 判断给定的指是否不在在数组中出现
     * @param value 数值
     * @param predicates
     * @throws PredicateException
     */
    public static <T> void ensureNotIn(T value, Iterable<T> predicates, String msg) throws PredicateException {
        for(T p : predicates) {
            if(p.equals(value)) {
                throw new PredicateException(msg);
            }
        }
    }
    public static <T> void ensureNotIn(T value, Iterable<T> predicates, Supplier<String> msg) throws PredicateException {
        ensureNotIn(value, predicates, msg.get());
    }

    /**
     * 判断给定的指是否不在在数组中出现
     * @param value
     * @param predicates
     * @throws PredicateException
     */
    public static <T> void ensureNotIn(T value, T[] predicates, String msg) throws PredicateException {
        for(T p : predicates) {
            if(p.equals(value)) {
                throw new PredicateException(msg);
            }
        }
    }
    public static <T> void ensureNotIn(T value, T[] predicates, Supplier<String> msg) throws PredicateException {
        ensureNotIn(value, predicates, msg.get());
    }

    /**
     * 判断给定的指是否在数组中出现
     * @param value
     * @param predicates
     * @throws PredicateException
     */
    public static <T> void ensureIn(T value, Iterable<T> predicates, String msg) throws PredicateException {
        for(T p : predicates) {
            if(p.equals(value)) {
                return;
            }
        }
        throw new PredicateException(msg);
    }
    public static <T> void ensureIn(T value, Iterable<T> predicates, Supplier<String> msg) throws PredicateException {
        ensureIn(value, predicates, msg.get());
    }

    /**
     * 判断给定的指是否在数组中出现
     * @param value
     * @param predicates
     * @throws PredicateException
     */
    public static <T> void ensureIn(T value, T[] predicates, String msg) throws PredicateException {
        for(T p : predicates) {
            if(p.equals(value)) {
                return;
            }
        }
        throw new PredicateException(msg);
    }
    public static <T> void ensureIn(T value, T[] predicates, Supplier<String> msg) throws PredicateException {
        ensureIn(value, predicates, msg.get());
    }

    private static int compare(Number x, Number y) {
        if(isSpecial(x) || isSpecial(y)) {
            return Double.compare(x.doubleValue(), y.doubleValue());
        } else {
            return toBigDecimal(x).compareTo(toBigDecimal(y));
        }
    }

    private static boolean isSpecial(Number x) {
        boolean specialDouble = x instanceof Double && (Double.isNaN((Double) x) || Double.isInfinite((Double) x));
        boolean specialFloat = x instanceof Float && (Float.isNaN((Float)x) || Float.isInfinite((Float)x));
        return specialDouble || specialFloat;
    }

    private static BigDecimal toBigDecimal(Number number) {
        if(number instanceof BigDecimal) {
            return (BigDecimal) number;
        } else if(number instanceof BigInteger) {
            return new BigDecimal((BigInteger) number);
        } else if(number instanceof Byte || number instanceof Short || number instanceof Integer || number instanceof Long) {
            return new BigDecimal(number.longValue());
        } else if(number instanceof Float || number instanceof Double) {
            return new BigDecimal(number.doubleValue());
        } else {
            try {
                return new BigDecimal(number.toString());
            } catch(final NumberFormatException e) {
                throw new UnexpectedException(String.format("数值%s(类型%s)无法转换为合法的字符串", number.toString(), number.getClass().getName()), e);
            }
        }
    }

    private static boolean isEquals(Object lhs, Object rhs) {
        if(lhs == null && rhs == null) {
            return true;
        } else if(lhs == null || rhs == null) {
            return false;
        } else if(lhs.getClass().equals(rhs.getClass()) && !lhs.equals(rhs)) {
            return false;
        } else if(lhs instanceof Number && rhs instanceof Number && compare((Number)lhs, (Number)rhs) == 0) {
            return true;
        } else if(lhs.equals(rhs)) {
            return true;
        } else {
            return true;
        }
    }

    public static void ensureEquals(Object lhs, Object rhs, String msg) throws PredicateException {
        if(!isEquals(lhs, rhs)) {
            throw new PredicateException(msg);
        }
    }

    public static void ensureNotEquals(Object lhs, Object rhs, String msg) throws PredicateException {
        if(isEquals(lhs, rhs)) {
            throw new PredicateException(msg);
        }
    }
    public static void ensureNotEquals(Object lhs, Object rhs, Supplier<String> msg) throws PredicateException {
        ensureNotEquals(lhs, rhs, msg);
    }

    public static void ensureEqualsIgnoreCase(String lhs, String rhs, String msg) {
        if(lhs == null && rhs == null) {
            return;
        } else if(lhs == null || rhs == null || !lhs.equalsIgnoreCase(rhs)) {
            throw new PredicateException(msg);
        }
    }
    public static void ensureEqualsIgnoreCase(String lhs, String rhs, Supplier<String> msg) {
        ensureEqualsIgnoreCase(lhs, rhs, msg.get());
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 判断给定的指是否在数组中出现
     * @param value
     * @param predicates
     * @throws PredicateException
     */
    public static void ensureIn(String value, Iterable<String> predicates, String msg) throws PredicateException {
        for(String p : predicates) {
            if(p.equals(value)) {
                return;
            }
        }
        throw new PredicateException(msg);
    }
    public static void ensureIn(String value, Iterable<String> predicates, Supplier<String> msg) throws PredicateException {
        ensureIn(value, predicates, msg.get());
    }

    /**
     * 验证子符长度
     *
     * @param min
     * @param max
     * @return
     * @throws PredicateException
     */
    public static void ensureLength(String name, boolean isNull, int min, int max, String msg) throws PredicateException {

        if (isNull) {
            if (name == null) {
                return;
            }
        } else {
            if (name == null) {
                throw new PredicateException(msg);
            }
        }

        int length = name.length();
        if (length < min || length > max) {
            throw new PredicateException(msg);
        }
    }
    public static void ensureLength(String name, boolean isNull, int min, int max, Supplier<String> msg) throws PredicateException {
        ensureLength(name, isNull, min, max, msg.get());
    }

    /**
     * 检查参数不为空
     * @param param
     * @param msg
     * @throws PredicateException
     */
    public static void ensureNotNull(Object param, String msg) {
        if (param == null) {
            throw new PredicateException(msg);
        }

        if(param instanceof String){
            if(Strings.isNullOrEmpty((String) param)){
                throw new PredicateException(msg);
            }
        }
    }
    public static void ensureNotNull(Object param, Supplier<String> msg) {
        ensureNotNull(param, msg.get());
    }

    public static <T> void ensureContainsNoNull(Collection<T> col, String msg) {
        for(T item: col) {
            if(item == null) {
                throw new PredicateException(msg);
            }
        }
    }
    public static <T> void ensureContainsNoNull(Collection<T> col, Supplier<String> msg) {
        ensureContainsNoNull(col, msg.get());
    }

    /**
     * 检查字符串不为空
     * @param str
     * @param msg
     * @throws PredicateException
     */
    public static void ensureNotEmpty(String str, String msg) throws PredicateException {

        if (Strings.isNullOrEmpty(str)) {
            throw new PredicateException(msg);
        }
    }
    public static void ensureNotEmpty(String str, Supplier<String> msg) throws PredicateException {
        ensureNotEmpty(str, msg.get());
    }

    public static <T> void ensureNotEmpty(Collection<T> col, String msg) throws PredicateException {
        if(col == null || col.isEmpty()) {
            throw new PredicateException(msg);
        }
    }
    public static <T> void ensureNotEmpty(Collection<T> col, Supplier<String> msg) throws PredicateException {
        ensureNotEmpty(col, msg);
    }

    public static <T> void ensureNotEmpty(Iterable<T> iter, String msg) throws PredicateException {
        if(iter == null || Iterables.isEmpty(iter)) {
            throw new PredicateException(msg);
        }
    }
    public static <T> void ensureNotEmpty(Iterable<T> iter, Supplier<String> msg) throws PredicateException {
        ensureNotEmpty(iter, msg.get());
    }
}
