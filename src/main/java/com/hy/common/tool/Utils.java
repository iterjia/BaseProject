/**
 * Copyright (c) 2018-2028, DreamLu 卢春梦 (qq596392912@gmail.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hy.common.tool;

import com.hy.core.secure.entity.TokenStub;
import com.hy.core.secure.utils.TokenUtil;
import org.springframework.beans.BeansException;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具包集合，只做简单的调用，不删除原有工具类
 */
public class Utils {

    /**
     * Checks that the specified object reference is not {@code null}. This
     * method is designed primarily for doing parameter validation in methods
     * and constructors, as demonstrated below:
     * <blockquote><pre>
     * public Foo(Bar bar) {
     *     this.bar = $.requireNotNull(bar);
     * }
     * </pre></blockquote>
     *
     * @param obj the object reference to check for nullity
     * @param <T> the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    public static <T> T requireNotNull(T obj) {
        return Objects.requireNonNull(obj);
    }

    /**
     * Checks that the specified object reference is not {@code null} and
     * throws a customized {@link NullPointerException} if it is. This method
     * is designed primarily for doing parameter validation in methods and
     * constructors with multiple parameters, as demonstrated below:
     * <blockquote><pre>
     * public Foo(Bar bar, Baz baz) {
     *     this.bar = $.requireNotNull(bar, "bar must not be null");
     *     this.baz = $.requireNotNull(baz, "baz must not be null");
     * }
     * </pre></blockquote>
     *
     * @param obj     the object reference to check for nullity
     * @param message detail message to be used in the event that a {@code
     *                NullPointerException} is thrown
     * @param <T>     the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    public static <T> T requireNotNull(T obj, String message) {
        return Objects.requireNonNull(obj, message);
    }

    /**
     * Checks that the specified object reference is not {@code null} and
     * throws a customized {@link NullPointerException} if it is.
     *
     * <p>Unlike the method {@link #requireNotNull(Object, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param obj             the object reference to check for nullity
     * @param messageSupplier supplier of the detail message to be
     *                        used in the event that a {@code NullPointerException} is thrown
     * @param <T>             the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     * @since 1.8
     */
    public static <T> T requireNotNull(T obj, Supplier<String> messageSupplier) {
        return Objects.requireNonNull(obj, messageSupplier);
    }

    /**
     * Returns {@code true} if the provided reference is {@code null} otherwise
     * returns {@code false}.
     * <p>
     * This method exists to be used as a
     * {@link java.util.function.Predicate}, {@code filter($::isNull)}
     *
     * @param obj a reference to be checked against {@code null}
     * @return {@code true} if the provided reference is {@code null} otherwise
     * {@code false}
     * @see java.util.function.Predicate
     * @since 1.8
     */
    public static boolean isNull(@Nullable Object obj) {
        return Objects.isNull(obj);
    }

    /**
     * Returns {@code true} if the provided reference is non-{@code null}
     * otherwise returns {@code false}.
     * <p>
     * This method exists to be used as a
     * {@link java.util.function.Predicate}, {@code filter($::notNull)}
     *
     * @param obj a reference to be checked against {@code null}
     * @return {@code true} if the provided reference is non-{@code null}
     * otherwise {@code false}
     * @see java.util.function.Predicate
     * @since 1.8
     */
    public static boolean notNull(@Nullable Object obj) {
        return Objects.nonNull(obj);
    }

    /**
     * 首字母变小写
     *
     * @param str 字符串
     * @return {String}
     */
    public static String firstCharToLower(String str) {
        return StringUtil.lowerFirst(str);
    }

    /**
     * 首字母变大写
     *
     * @param str 字符串
     * @return {String}
     */
    public static String firstCharToUpper(String str) {
        return StringUtil.upperFirst(str);
    }

    /**
     * 下划线变驼峰
     *
     * @param str 字符串
     * @return {String}
     */
    public static String underLine2Camel(String str) {
        Pattern pattern = Pattern.compile("_(\\w)");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        int matchCount = 0;
        while (matcher.find()) {
            matchCount++;
//            System.out.println(matcher.group(1) + "-" + matcher.start(1) + "-" + matcher.end(1));
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }

        if (matchCount > 0) {
            matcher.appendTail(sb);
            return sb.toString();
        } else {
            return str;
        }
    }

    /**
     * Check whether the given {@code CharSequence} contains actual <em>text</em>.
     * <p>More specifically, this method returns {@code true} if the
     * {@code CharSequence} is not {@code null}, its length is greater than
     * 0, and it contains at least one non-whitespace character.
     * <pre class="code">
     * $.isBlank(null)		= true
     * $.isBlank("")		= true
     * $.isBlank(" ")		= true
     * $.isBlank("12345")	= false
     * $.isBlank(" 12345 ")	= false
     * </pre>
     *
     * @param cs the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null},
     * its length is greater than 0, and it does not contain whitespace only
     * @see Character#isWhitespace
     */
    public static boolean isBlank(@Nullable final CharSequence cs) {
        return StringUtil.isBlank(cs);
    }

    /**
     * <p>Checks if a CharSequence is not empty (""), not null and not whitespace only.</p>
     * <pre>
     * $.isNotBlank(null)	= false
     * $.isNotBlank("")		= false
     * $.isNotBlank(" ")	= false
     * $.isNotBlank("bob")	= true
     * $.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is
     * not empty and not null and not whitespace
     * @see Character#isWhitespace
     */
    public static boolean isNotBlank(@Nullable final CharSequence cs) {
        return StringUtil.isNotBlank(cs);
    }

    /**
     * 有 任意 一个 Blank
     *
     * @param css CharSequence
     * @return boolean
     */
    public static boolean isAnyBlank(final CharSequence... css) {
        return StringUtil.isAnyBlank(css);
    }

    /**
     * 是否全非 Blank
     *
     * @param css CharSequence
     * @return boolean
     */
    public static boolean isNoneBlank(final CharSequence... css) {
        return StringUtil.isNoneBlank(css);
    }

    /**
     * Determine whether the given object is an array:
     * either an Object array or a primitive array.
     *
     * @param obj the object to check
     * @return 是否数组
     */
    public static boolean isArray(@Nullable Object obj) {
        return ObjectUtil.isArray(obj);
    }

    /**
     * Determine whether the given object is empty:
     * i.e. {@code null} or of zero length.
     *
     * @param obj the object to check
     * @return 数组是否为空
     */
    public static boolean isEmpty(@Nullable Object obj) {
        return ObjectUtil.isEmpty(obj);
    }

    /**
     * Determine whether the given object is not empty:
     * i.e. {@code null} or of zero length.
     *
     * @param obj the object to check
     * @return 是否不为空
     */
    public static boolean isNotEmpty(@Nullable Object obj) {
        return !ObjectUtil.isEmpty(obj);
    }

    /**
     * Determine whether the given array is empty:
     * i.e. {@code null} or of zero length.
     *
     * @param array the array to check
     * @return 数组是否为空
     */
    public static boolean isEmpty(@Nullable Object[] array) {
        return ObjectUtil.isEmpty(array);
    }

    /**
     * 判断数组不为空
     *
     * @param array 数组
     * @return 数组是否不为空
     */
    public static boolean isNotEmpty(@Nullable Object[] array) {
        return ObjectUtil.isNotEmpty(array);
    }

    /**
     * 对象组中是否存在 Empty Object
     *
     * @param os 对象组
     * @return boolean
     */
    public static boolean hasEmpty(Object... os) {
        for (Object o : os) {
            if (isEmpty(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对象组中是否全是 Empty Object
     *
     * @param os 对象组
     * @return boolean
     */
    public static boolean allEmpty(Object... os) {
        for (Object o : os) {
            if (!isEmpty(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 比较两个对象是否相等。<br>
     * 相同的条件有两个，满足其一即可：<br>
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否相等
     */
    public static boolean equals(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    /**
     * Determine if the given objects are equal, returning {@code true} if
     * both are {@code null} or {@code false} if only one is {@code null}.
     * <p>Compares arrays with {@code Arrays.equals}, performing an equality
     * check based on the array elements rather than the array reference.
     *
     * @param o1 first Object to compare
     * @param o2 second Object to compare
     * @return whether the given objects are equal
     * @see Object#equals(Object)
     * @see Arrays#equals
     */
    public static boolean equalsSafe(@Nullable Object o1, @Nullable Object o2) {
        return ObjectUtil.nullSafeEquals(o1, o2);
    }

    /**
     * Check whether the given Array contains the given element.
     *
     * @param array   the Array to check
     * @param element the element to look for
     * @param <T>     The generic tag
     * @return {@code true} if found, {@code false} else
     */
    public static <T> boolean contains(@Nullable T[] array, final T element) {
        return CollectionUtil.contains(array, element);
    }

    /**
     * Check whether the given Iterator contains the given element.
     *
     * @param iterator the Iterator to check
     * @param element  the element to look for
     * @return {@code true} if found, {@code false} otherwise
     */
    public static boolean contains(@Nullable Iterator<?> iterator, Object element) {
        return CollectionUtil.contains(iterator, element);
    }

    /**
     * Check whether the given Enumeration contains the given element.
     *
     * @param enumeration the Enumeration to check
     * @param element     the element to look for
     * @return {@code true} if found, {@code false} otherwise
     */
    public static boolean contains(@Nullable Enumeration<?> enumeration, Object element) {
        return CollectionUtil.contains(enumeration, element);
    }

    /**
     * 强转string,并去掉多余空格
     *
     * @param str 字符串
     * @return String
     */
    public static String toStr(Object str) {
        return toStr(str, "");
    }

    /**
     * 强转string,并去掉多余空格
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return String
     */
    public static String toStr(Object str, String defaultValue) {
        if (null == str) {
            return defaultValue;
        }
        return String.valueOf(str);
    }

    /**
     * 判断一个字符串是否是数字
     *
     * @param cs the CharSequence to check, may be null
     * @return {boolean}
     */
    public static boolean isNumeric(final CharSequence cs) {
        return StringUtil.isNumeric(cs);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>int</code>, returning
     * <code>zero</code> if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
     *
     * <pre>
     *   $.toInt(null) = 0
     *   $.toInt("")   = 0
     *   $.toInt("1")  = 1
     * </pre>
     *
     * @param value the string to convert, may be null
     * @return the int represented by the string, or <code>zero</code> if
     * conversion fails
     */
    public static int toInt(final Object value) {
        return NumberUtil.toInt(String.valueOf(value));
    }

    /**
     * <p>Convert a <code>String</code> to an <code>int</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     *
     * <pre>
     *   $.toInt(null, 1) = 1
     *   $.toInt("", 1)   = 1
     *   $.toInt("1", 0)  = 1
     * </pre>
     *
     * @param value        the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion fails
     */
    public static int toInt(final Object value, final int defaultValue) {
        return NumberUtil.toInt(String.valueOf(value), defaultValue);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>long</code>, returning
     * <code>zero</code> if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
     *
     * <pre>
     *   $.toLong(null) = 0L
     *   $.toLong("")   = 0L
     *   $.toLong("1")  = 1L
     * </pre>
     *
     * @param value the string to convert, may be null
     * @return the long represented by the string, or <code>0</code> if
     * conversion fails
     */
    public static long toLong(final Object value) {
        return NumberUtil.toLong(String.valueOf(value));
    }

    /**
     * <p>Convert a <code>String</code> to a <code>long</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     *
     * <pre>
     *   $.toLong(null, 1L) = 1L
     *   $.toLong("", 1L)   = 1L
     *   $.toLong("1", 0L)  = 1L
     * </pre>
     *
     * @param value        the string to convert, may be null
     * @param defaultValue the default value
     * @return the long represented by the string, or the default if conversion fails
     */
    public static long toLong(final Object value, final long defaultValue) {
        return NumberUtil.toLong(String.valueOf(value), defaultValue);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>Double</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     *
     * <pre>
     *   $.toDouble(null, 1) = 1.0
     *   $.toDouble("", 1)   = 1.0
     *   $.toDouble("1", 0)  = 1.0
     * </pre>
     *
     * @param value the string to convert, may be null
     * @return the int represented by the string, or the default if conversion fails
     */
    public static Double toDouble(Object value) {
        return toDouble(String.valueOf(value), -1.00);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>Double</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     *
     * <pre>
     *   $.toDouble(null, 1) = 1.0
     *   $.toDouble("", 1)   = 1.0
     *   $.toDouble("1", 0)  = 1.0
     * </pre>
     *
     * @param value        the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion fails
     */
    public static Double toDouble(Object value, Double defaultValue) {
        return NumberUtil.toDouble(String.valueOf(value), defaultValue);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>Float</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     *
     * <pre>
     *   $.toFloat(null, 1) = 1.00f
     *   $.toFloat("", 1)   = 1.00f
     *   $.toFloat("1", 0)  = 1.00f
     * </pre>
     *
     * @param value the string to convert, may be null
     * @return the int represented by the string, or the default if conversion fails
     */
    public static Float toFloat(Object value) {
        return toFloat(String.valueOf(value), -1.0f);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>Float</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     *
     * <pre>
     *   $.toFloat(null, 1) = 1.00f
     *   $.toFloat("", 1)   = 1.00f
     *   $.toFloat("1", 0)  = 1.00f
     * </pre>
     *
     * @param value        the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion fails
     */
    public static Float toFloat(Object value, Float defaultValue) {
        return NumberUtil.toFloat(String.valueOf(value), defaultValue);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>Boolean</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     *
     * <pre>
     *   $.toBoolean("true", true)  = true
     *   $.toBoolean("false")   	= false
     *   $.toBoolean("", false)  	= false
     * </pre>
     *
     * @param value the string to convert, may be null
     * @return the int represented by the string, or the default if conversion fails
     */
    public static Boolean toBoolean(Object value) {
        return toBoolean(value, null);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>Boolean</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     *
     * <pre>
     *   $.toBoolean("true", true)  = true
     *   $.toBoolean("false")   	= false
     *   $.toBoolean("", false)  	= false
     * </pre>
     *
     * @param value        the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion fails
     */
    public static Boolean toBoolean(Object value, Boolean defaultValue) {
        if (value != null) {
            String val = String.valueOf(value);
            val = val.toLowerCase().trim();
            return Boolean.parseBoolean(val);
        }
        return defaultValue;
    }

    /**
     * 转换为Integer数组<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Integer[] toIntArray(String str) {
        return toIntArray(",", str);
    }

    /**
     * 转换为Integer数组<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static Integer[] toIntArray(String split, String str) {
        if (StringUtil.isEmpty(str)) {
            return new Integer[]{};
        }
        String[] arr = str.split(split);
        final Integer[] ints = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            final Integer v = toInt(arr[i], 0);
            ints[i] = v;
        }
        return ints;
    }

    /**
     * 转换为Integer集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<Integer> toIntList(String str) {
        return Arrays.asList(toIntArray(str));
    }

    /**
     * 转换为Integer集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<Integer> toIntList(String split, String str) {
        return Arrays.asList(toIntArray(split, str));
    }

    /**
     * 转换为String数组<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static String[] toStrArray(String str) {
        return toStrArray(",", str);
    }

    /**
     * 转换为String数组<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static String[] toStrArray(String split, String str) {
        if (isBlank(str)) {
            return new String[]{};
        }
        return str.split(split);
    }

    /**
     * 转换为String集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<String> toStrList(String str) {
        return Arrays.asList(toStrArray(str));
    }

    /**
     * 转换为String集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<String> toStrList(String split, String str) {
        return Arrays.asList(toStrArray(split, str));
    }

    /**
     * 将 long 转短字符串 为 62 进制
     *
     * @param num 数字
     * @return 短字符串
     */
    public static String to62String(long num) {
        return NumberUtil.to62String(num);
    }

    /**
     * Convert a {@code Collection} into a delimited {@code String} (e.g., CSV).
     * <p>Useful for {@code toString()} implementations.
     *
     * @param coll the {@code Collection} to convert
     * @return the delimited {@code String}
     */
    public static String join(Collection<?> coll) {
        return StringUtil.join(coll);
    }

    /**
     * Convert a {@code Collection} into a delimited {@code String} (e.g. CSV).
     * <p>Useful for {@code toString()} implementations.
     *
     * @param coll  the {@code Collection} to convert
     * @param delim the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     */
    public static String join(Collection<?> coll, String delim) {
        return StringUtil.join(coll, delim);
    }

    /**
     * Convert a {@code String} array into a comma delimited {@code String}
     * (i.e., CSV).
     * <p>Useful for {@code toString()} implementations.
     *
     * @param arr the array to display
     * @return the delimited {@code String}
     */
    public static String join(Object[] arr) {
        return StringUtil.join(arr);
    }

    /**
     * Convert a {@code String} array into a delimited {@code String} (e.g. CSV).
     * <p>Useful for {@code toString()} implementations.
     *
     * @param arr   the array to display
     * @param delim the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     */
    public static String join(Object[] arr, String delim) {
        return StringUtil.join(arr, delim);
    }

    /**
     * 生成uuid
     *
     * @return UUID
     */
    public static String randomUUID() {
        return StringUtil.randomUUID();
    }

    /**
     * 转义HTML用于安全过滤
     *
     * @param html html
     * @return {String}
     */
    public static String escapeHtml(String html) {
        return StringUtil.escapeHtml(html);
    }

    /**
     * closeQuietly
     *
     * @param closeable 自动关闭
     */
    public static void closeQuietly(@Nullable Closeable closeable) {
        IoUtil.closeQuietly(closeable);
    }

    /**
     * InputStream to String utf-8
     *
     * @param input the <code>InputStream</code> to read from
     * @return the requested String
     * @throws NullPointerException if the input is null
     */
    public static String toString(InputStream input) {
        return IoUtil.toString(input);
    }

    /**
     * InputStream to String
     *
     * @param input   the <code>InputStream</code> to read from
     * @param charset the <code>Charsets</code>
     * @return the requested String
     * @throws NullPointerException if the input is null
     */
    public static String toString(@Nullable InputStream input, Charset charset) {
        return IoUtil.toString(input, charset);
    }

    public static byte[] toByteArray(@Nullable InputStream input) {
        return IoUtil.toByteArray(input);
    }

    /**
     * Decode the given encoded URI component.
     * <p>See {@link StringUtils#uriDecode(String, Charset)} for the decoding rules.
     *
     * @param source  the encoded String
     * @param charset the character encoding to use
     * @return the decoded value
     * @throws IllegalArgumentException when the given source contains invalid encoded sequences
     * @see StringUtils#uriDecode(String, Charset)
     * @see java.net.URLDecoder#decode(String, String)
     */
    public static String decode(String source, Charset charset) {
        return StringUtils.uriDecode(source, charset);
    }

    /**
     * 时间比较
     *
     * @param startInclusive the start instant, inclusive, not null
     * @param endExclusive   the end instant, exclusive, not null
     * @return a {@code Duration}, not null
     */
    public static Duration between(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive);
    }

    /**
     * 获取方法参数信息
     *
     * @param constructor    构造器
     * @param parameterIndex 参数序号
     * @return {MethodParameter}
     */
    public static MethodParameter getMethodParameter(Constructor<?> constructor, int parameterIndex) {
        return ClassUtil.getMethodParameter(constructor, parameterIndex);
    }

    /**
     * 获取方法参数信息
     *
     * @param method         方法
     * @param parameterIndex 参数序号
     * @return {MethodParameter}
     */
    public static MethodParameter getMethodParameter(Method method, int parameterIndex) {
        return ClassUtil.getMethodParameter(method, parameterIndex);
    }

    /**
     * 获取Annotation
     *
     * @param annotatedElement AnnotatedElement
     * @param annotationType   注解类
     * @param <A>              泛型标记
     * @return {Annotation}
     */
    @Nullable
    public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
        return AnnotatedElementUtils.findMergedAnnotation(annotatedElement, annotationType);
    }

    /**
     * 获取Annotation
     *
     * @param method         Method
     * @param annotationType 注解类
     * @param <A>            泛型标记
     * @return {Annotation}
     */
    @Nullable
    public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
        return ClassUtil.getAnnotation(method, annotationType);
    }

    /**
     * 获取Annotation
     *
     * @param handlerMethod  HandlerMethod
     * @param annotationType 注解类
     * @param <A>            泛型标记
     * @return {Annotation}
     */
    @Nullable
    public static <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
        return ClassUtil.getAnnotation(handlerMethod, annotationType);
    }

    /**
     * 实例化对象
     *
     * @param clazz 类
     * @param <T>   泛型标记
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz) {
        return (T) BeanUtil.instantiateClass(clazz);
    }

    /**
     * 实例化对象
     *
     * @param clazzStr 类名
     * @param <T>      泛型标记
     * @return 对象
     */
    public static <T> T newInstance(String clazzStr) {
        return BeanUtil.newInstance(clazzStr);
    }

    /**
     * 获取Bean的属性
     *
     * @param bean         bean
     * @param propertyName 属性名
     * @return 属性值
     */
    public static Object getProperty(Object bean, String propertyName) {
        return BeanUtil.getProperty(bean, propertyName);
    }

    /**
     * 设置Bean属性
     *
     * @param bean         bean
     * @param propertyName 属性名
     * @param value        属性值
     */
    public static void setProperty(Object bean, String propertyName, Object value) {
        BeanUtil.setProperty(bean, propertyName, value);
    }

    /**
     * Copy the property values of the given source bean into the target class.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     *
     * @param source the source bean
     * @param clazz  the target bean class
     * @param <T>    泛型标记
     * @return T
     * @throws BeansException if the copying failed
     */
    public static <T> T copyProperties(Object source, Class<T> clazz) throws BeansException {
        return BeanUtil.copyProperties(source, clazz);
    }

    /**
     * 将对象装成map形式
     *
     * @param bean 源对象
     * @return {Map}
     */
    public static Map<String, Object> toMap(Object bean) {
        return BeanUtil.toMap(bean);
    }

    /**
     * 将map 转为 bean
     *
     * @param beanMap   map
     * @param valueType 对象类型
     * @param <T>       泛型标记
     * @return {T}
     */
    public static <T> T toBean(Map<String, Object> beanMap, Class<T> valueType) {
        return BeanUtil.toBean(beanMap, valueType);
    }

    /**
     * 将对象序列化成json字符串
     *
     * @param value javaBean
     * @param <T>   T 泛型标记
     * @return jsonString json字符串
     */
    public static <T> String toJson(T value) {
        return JsonUtil.toJson(value);
    }

    public static boolean retBool(Integer result) {
        return null != result && result >= 1;
    }

    public static String toMd5Hex(String src) {
        return SecureUtil.toMd5Hex(src);
    }

    public static byte[] toMd5Bytes(byte[] data) {
        return SecureUtil.toMd5Bytes(data);
    }

    public static String toBase64(byte[] bytes) {
        return SecureUtil.toBase64(bytes);
    }

    public static String toBase64(String src) {
        return SecureUtil.toBase64(src);
    }

    public static String toBase64(String src, String charsetName) {
        return SecureUtil.toBase64(src, charsetName);
    }

    public static byte[] decodeBase64B2B(byte[] bytes) {
        return SecureUtil.decodeBase64B2B(bytes);
    }

    public static byte[] decodeBase64ToBytes(String src) {
        return SecureUtil.decodeBase64ToBytes(src);
    }

    public static byte[] decodeBase64S2B(String src, String charsetName) {
        return SecureUtil.decodeBase64S2B(src, charsetName);
    }

    public static String decodeBase64ToString(String src) {
        return SecureUtil.decodeBase64ToString(src);
    }

    public static String decodeBase64S2S(String src, String charsetName) {
        return SecureUtil.decodeBase64S2S(src, charsetName);
    }

    public static String genRandomString(int length) {
        return SecureUtil.genRandomString(length);
    }

    public static String saltPassword(String pwd) {
        return SecureUtil.saltPassword(pwd);
    }

    public static boolean verifyPassword(String clearTextPwd, String encryptedPwd) {
        return SecureUtil.verifyPassword(clearTextPwd, encryptedPwd);
    }

    public static String encryptWithAes(String src) {
        return SecureUtil.encryptWithAes(src);
    }

    public static String decryptWithAes(String src) {
        return SecureUtil.decryptWithAes(src);
    }

    public static String ensureDirExist(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    public static Integer getLoginUserId() {
        TokenStub stub = TokenUtil.extractTokenStub();
        if (stub == null) {
            return null;
        }
        return stub.getUserId();
    }

    public static HttpServletRequest getRequest() {
        return WebUtil.getRequest();
    }
}
