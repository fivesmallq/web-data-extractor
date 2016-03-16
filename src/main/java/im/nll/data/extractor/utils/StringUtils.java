package im.nll.data.extractor.utils;

import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static final Logger logger = Logs.get();
    /**
     * The empty String <code>""</code>.
     *
     * @since 2.0
     */
    public static final String EMPTY = "";
    /**
     * An empty immutable {@code String} array.
     */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    // 日期正则
    private final static String strictDatePattern = "\\d{2,4}[-年/\\s]*\\d{1,2}[-月/\\s]*\\d{1,2}[\\s日/]*(\\d{1,2}:\\d{1,2}(:\\d{1,2})?)?";
    private static final int INDEX_NOT_FOUND = -1;

    private static Pattern strictDateRegexPattern = Pattern.compile(
            strictDatePattern, Pattern.CANON_EQ | Pattern.DOTALL
                    | Pattern.CASE_INSENSITIVE);
    private static final Set<String> IRRELEVANT_PARAMETERS = new HashSet<String>(
            3);

    static {
        IRRELEVANT_PARAMETERS.add("jsessionid");
        IRRELEVANT_PARAMETERS.add("phpsessid");
        IRRELEVANT_PARAMETERS.add("aspsessionid");
    }

    public static boolean isUrl(String url) {
        if (url == null || url.length() == 0) {
            return false;
        }
        // NOTE:为了支持参数化的url，故暂时不用正则的方式来严格的校验
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public static boolean isNullOrEmpty(String content) {
        return content == null || content.length() == 0
                || "null".equals(content);
    }

    public static boolean isNotNullOrEmpty(String content) {
        return (content != null) && (content.length() > 0);
    }

    public static String defaultIfNullOrEmpty(String str, String defaultStr) {
        return StringUtils.isNullOrEmpty(str) ? defaultStr : str;
    }

    /**
     * 获取数字字符.
     *
     * @param str
     * @return
     */
    public static String getNumStr(String str) {
        if (isNullOrEmpty(str)) {
            return "";
        }
        Pattern numPattern = Pattern.compile("\\d");
        Matcher matcher = numPattern.matcher(str);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            builder.append(matcher.group());
        }
        return builder.toString();
    }


    /**
     * 获取最后一个字符,如果为空返回"";
     *
     * @param str
     * @return
     */
    public static String last(String str) {
        if (StringUtils.isNotNullOrEmpty(str)) {
            return str.substring(str.length() - 1, str.length());
        } else {
            return "";
        }
    }

    // Empty checks
    // -----------------------------------------------------------------------

    /**
     * <p>
     * Checks if a CharSequence is empty ("") or null.
     * </p>
     * <p>
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     * <p>
     * <p>
     * NOTE: This method changed in Lang version 2.0. It no longer trims the
     * CharSequence. That functionality is available in isBlank().
     * </p>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is empty or null
     * @since 3.0 Changed signature from isEmpty(String) to
     * isEmpty(CharSequence)
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * <p>
     * Checks if a String is whitespace, empty ("") or null.
     * </p>
     * <p>
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     * @since 2.0
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Capitalizes a String changing the first letter to title case as per
     * {@link Character#toTitleCase(char)}. No other letters are changed.
     * </p>
     * <p>
     * <p>
     * <code>null</code> input String returns <code>null</code>.
     * </p>
     * <p>
     * <pre>
     * StringUtils.capitalize(null)  = null
     * StringUtils.capitalize("")    = ""
     * StringUtils.capitalize("cat") = "Cat"
     * StringUtils.capitalize("cAt") = "CAt"
     * </pre>
     *
     * @param str the String to capitalize, may be null
     * @return the capitalized String, <code>null</code> if null String input
     * @since 2.0
     */
    public static String capitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuffer(strLen)
                .append(Character.toTitleCase(str.charAt(0)))
                .append(str.substring(1)).toString();
    }

    // SubStringAfter/SubStringBefore
    // -----------------------------------------------------------------------

    /**
     * <p>
     * Gets the substring before the first occurrence of a separator. The
     * separator is not returned.
     * </p>
     * <p>
     * <p>
     * A {@code null} string input will return {@code null}. An empty ("")
     * string input will return the empty string. A {@code null} separator will
     * return the input string.
     * </p>
     * <p>
     * <p>
     * If nothing is found, the string input is returned.
     * </p>
     * <p>
     * <pre>
     * StringUtils.substringBefore(null, *)      = null
     * StringUtils.substringBefore("", *)        = ""
     * StringUtils.substringBefore("abc", "a")   = ""
     * StringUtils.substringBefore("abcba", "b") = "a"
     * StringUtils.substringBefore("abc", "c")   = "ab"
     * StringUtils.substringBefore("abc", "d")   = "abc"
     * StringUtils.substringBefore("abc", "")    = ""
     * StringUtils.substringBefore("abc", null)  = "abc"
     * </pre>
     *
     * @param str       the String to get a substring from, may be null
     * @param separator the String to search for, may be null
     * @return the substring before the first occurrence of the separator,
     * {@code null} if null String input
     * @since 2.0
     */
    public static String substringBefore(String str, String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.length() == 0) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 截取指定字符的前一个字符.
     *
     * @param str
     * @param separator
     * @return
     */
    public static String substringBeforeLastChar(String str, String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        if (pos == 0) {
            return "";
        }
        return str.substring(pos - 1, pos);
    }

    /**
     * <p>
     * Gets the substring after the first occurrence of a separator. The
     * separator is not returned.
     * </p>
     * <p>
     * <p>
     * A {@code null} string input will return {@code null}. An empty ("")
     * string input will return the empty string. A {@code null} separator will
     * return the empty string if the input string is not {@code null}.
     * </p>
     * <p>
     * <p>
     * If nothing is found, the empty string is returned.
     * </p>
     * <p>
     * <pre>
     * StringUtils.substringAfter(null, *)      = null
     * StringUtils.substringAfter("", *)        = ""
     * StringUtils.substringAfter(*, null)      = ""
     * StringUtils.substringAfter("abc", "a")   = "bc"
     * StringUtils.substringAfter("abcba", "b") = "cba"
     * StringUtils.substringAfter("abc", "c")   = ""
     * StringUtils.substringAfter("abc", "d")   = ""
     * StringUtils.substringAfter("abc", "")    = "abc"
     * </pre>
     *
     * @param str       the String to get a substring from, may be null
     * @param separator the String to search for, may be null
     * @return the substring after the first occurrence of the separator,
     * {@code null} if null String input
     * @since 2.0
     */
    public static String substringAfter(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * <p>
     * Gets the substring before the last occurrence of a separator. The
     * separator is not returned.
     * </p>
     * <p>
     * <p>
     * A {@code null} string input will return {@code null}. An empty ("")
     * string input will return the empty string. An empty or {@code null}
     * separator will return the input string.
     * </p>
     * <p>
     * <p>
     * If nothing is found, the string input is returned.
     * </p>
     * <p>
     * <pre>
     * StringUtils.substringBeforeLast(null, *)      = null
     * StringUtils.substringBeforeLast("", *)        = ""
     * StringUtils.substringBeforeLast("abcba", "b") = "abc"
     * StringUtils.substringBeforeLast("abc", "c")   = "ab"
     * StringUtils.substringBeforeLast("a", "a")     = ""
     * StringUtils.substringBeforeLast("a", "z")     = "a"
     * StringUtils.substringBeforeLast("a", null)    = "a"
     * StringUtils.substringBeforeLast("a", "")      = "a"
     * </pre>
     *
     * @param str       the String to get a substring from, may be null
     * @param separator the String to search for, may be null
     * @return the substring before the last occurrence of the separator,
     * {@code null} if null String input
     * @since 2.0
     */
    public static String substringBeforeLast(String str, String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * <p>
     * Gets the substring after the last occurrence of a separator. The
     * separator is not returned.
     * </p>
     * <p>
     * <p>
     * A {@code null} string input will return {@code null}. An empty ("")
     * string input will return the empty string. An empty or {@code null}
     * separator will return the empty string if the input string is not
     * {@code null}.
     * </p>
     * <p>
     * <p>
     * If nothing is found, the empty string is returned.
     * </p>
     * <p>
     * <pre>
     * StringUtils.substringAfterLast(null, *)      = null
     * StringUtils.substringAfterLast("", *)        = ""
     * StringUtils.substringAfterLast(*, "")        = ""
     * StringUtils.substringAfterLast(*, null)      = ""
     * StringUtils.substringAfterLast("abc", "a")   = "bc"
     * StringUtils.substringAfterLast("abcba", "b") = "a"
     * StringUtils.substringAfterLast("abc", "c")   = ""
     * StringUtils.substringAfterLast("a", "a")     = ""
     * StringUtils.substringAfterLast("a", "z")     = ""
     * </pre>
     *
     * @param str       the String to get a substring from, may be null
     * @param separator the String to search for, may be null
     * @return the substring after the last occurrence of the separator,
     * {@code null} if null String input
     * @since 2.0
     */
    public static String substringAfterLast(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (isEmpty(separator)) {
            return EMPTY;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND || pos == str.length() - separator.length()) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * <p>
     * Gets the String that is nested in between two Strings. Only the first
     * match is returned.
     * </p>
     * <p>
     * <p>
     * A <code>null</code> input String returns <code>null</code>. A
     * <code>null</code> open/close returns <code>null</code> (no match). An
     * empty ("") open and close returns an empty string.
     * </p>
     * <p>
     * <pre>
     * StringUtils.substringBetween("wx[b]yz", "[", "]") = "b"
     * StringUtils.substringBetween(null, *, *)          = null
     * StringUtils.substringBetween(*, null, *)          = null
     * StringUtils.substringBetween(*, *, null)          = null
     * StringUtils.substringBetween("", "", "")          = ""
     * StringUtils.substringBetween("", "", "]")         = null
     * StringUtils.substringBetween("", "[", "]")        = null
     * StringUtils.substringBetween("yabcz", "", "")     = ""
     * StringUtils.substringBetween("yabcz", "y", "z")   = "abc"
     * StringUtils.substringBetween("yabczyabcz", "y", "z")   = "abc"
     * </pre>
     *
     * @param str   the String containing the substring, may be null
     * @param open  the String before the substring, may be null
     * @param close the String after the substring, may be null
     * @return the substring, <code>null</code> if no match
     * @since 2.0
     */
    public static String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != INDEX_NOT_FOUND) {
            int end = str.indexOf(close, start + open.length());
            if (end != INDEX_NOT_FOUND) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    /**
     * 置空字符串
     *
     * @param content 判断的字符串
     * @return 如果字符串为空，返回"",否则返回原文
     */
    public static String empty(String content) {
        return StringUtils.isNullOrEmpty(content) ? "" : content;
    }

    /**
     * 支持的格式串：yyyy,yy,MM,M,dd,d ,其中:
     * yyyy（四位年），yy（两位年），MM（两位月，如果不足两位，前导0补齐），M（月，不补齐），dd(两位日，如果不足两位，前导0补齐)，d(日，
     * 不补齐)
     *
     * @param oldUrl
     * @return
     */
    public static String preProcessUrl(String oldUrl) {
        //
        if (oldUrl.indexOf('{') >= 0) {
            String tempUrl = oldUrl.replace("{", "{0,date,");
            return MessageFormat.format(tempUrl, new Date());
        }
        return oldUrl;
    }

    public static Date normalizeStr2Date(String before) throws ParseException

    {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String afterDateString = normalizeDate(before);
        return formatter.parse(afterDateString);
    }

    /**
     * normalization date string,<br>
     * not support two-digit year,not support time-only string
     *
     * @return normalized date string,format is:yyyy-MM-dd HH:mm:ss
     */
    public static String normalizeDate(String before) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNullOrEmpty(before)) {
            return formatter.format(new java.util.Date());
        }
        int[] calendarField = new int[]{Calendar.YEAR, Calendar.MONTH,
                Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE,
                Calendar.SECOND};
        Calendar calendar = Calendar.getInstance();
        try {
            Matcher matcher = strictDateRegexPattern.matcher(before);
            if (matcher.find()) {
                before = matcher.group();
            }

            String[] tmp = before.replaceAll("\\D", " ").split("\\s+");
            int i = 0, j = 0;
            if (tmp != null && tmp.length > 0 && "".equals(tmp[0])) {
                i = 1;
            }
            if (Integer.parseInt(tmp[i]) < 13) {
                j = 1;
            }
            for (; i < tmp.length; i++, j++) {
                if (j == 1) {
                    calendar.set(calendarField[j], Integer.parseInt(tmp[i]) - 1);
                } else {
                    calendar.set(calendarField[j], Integer.parseInt(tmp[i]));
                }
            }
            for (; j < calendarField.length; j++) {
                calendar.set(calendarField[j], 0);
            }
            return formatter.format(calendar.getTime());
        } catch (Exception e) {
            logger.error("日期转换错误,内容:(" + before.length() + ")" + before);
            return formatter.format(new Date());
        }
    }

    /**
     * 转换为int型，如果不能转换，则返回-1
     *
     * @param value
     * @return
     */
    public static int tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 转换为int型，如果不能转换，则返回defaultValue
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static int tryParseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为Date型，如果不能转换，则返回now
     *
     * @param value
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Date tryParseDate(String value) {
        try {
            return new Date(Date.parse(value));
        } catch (Exception e) {
            return new Date();
        }
    }

    /**
     * 转换为Date型，如果不能转换，则返回defaultValue
     *
     * @param value
     * @param defaultValue
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Date tryParseDate(String value, Date defaultValue) {
        try {
            return new Date(Date.parse(value));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 解析http头中包含的日期
     *
     * @param value
     * @return
     */
    public static Date parseHttpDate(String value) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "EEE, dd MMM yyyy HH:mm:ss", Locale.US);
            Date expires = simpleDateFormat.parse(value);
            return expires;
        } catch (Exception e) {
            return tryParseDate(value);
        }
    }

    /**
     * 批量格式化url，通过相对url获取绝对url
     *
     * @param baseUrl
     * @param urlList
     * @return
     */
    public static List<String> normalizeUrl(String baseUrl, List<String> urlList) {
        List<String> newUrlList = new ArrayList<>();
        for (String url : urlList) {
            newUrlList.add(normalizeUrl(baseUrl, url));
        }
        return newUrlList;
    }

    /**
     * 批量格式化url，通过相对url获取绝对url
     *
     * @param baseUrl
     * @param urlList
     * @param urlSuffix url后缀
     * @return
     */
    public static List<String> normalizeUrl(String baseUrl,
                                            List<String> urlList, String urlSuffix) {
        List<String> newUrlList = new ArrayList<>();
        for (String url : urlList) {
            newUrlList.add(normalizeUrl(baseUrl, url) + urlSuffix);
        }
        return newUrlList;
    }

    /**
     * 格式化url，通过相对url获取绝对url
     *
     * @param baseUrl   相对地址所在地址
     * @param urlString 相对地址
     * @return
     */
    public static String normalizeUrl(String baseUrl, String urlString) {
        URL base;
        try {
            try {
                base = new URL(baseUrl);
            } catch (MalformedURLException e) {
                // the base is unsuitable, but the attribute may be abs on its
                // own, so try that
                URL abs = new URL(urlString);
                return abs.toExternalForm();
            }
            // workaround: java resolves '//path/file + ?foo' to '//path/?foo',
            // not '//path/file?foo' as desired
            if (urlString.startsWith("?"))
                urlString = base.getPath() + urlString;
            URL abs = new URL(base, urlString);
            return abs.toExternalForm();
        } catch (MalformedURLException e) {
            return "";
        }
    }


    /**
     * Takes a query string, separates the constituent name-value pairs, and
     * stores them in a SortedMap ordered by lexicographical order.
     *
     * @param queryString the query string
     * @return Null if there is no query string.
     */
    private static SortedMap<String, String> createParameterMap(
            final String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return null;
        }

        final String[] pairs = queryString.split("&");
        final SortedMap<String, String> params = new TreeMap<String, String>();

        for (final String pair : pairs) {
            if (pair.length() == 0) {
                continue;
            }

            String[] tokens = pair.split("=", 2);
            switch (tokens.length) {
                case 1:
                    if (pair.charAt(0) == '=') {
                        params.put("", tokens[0]);
                    } else {
                        params.put(tokens[0], "");
                    }
                    break;
                case 2:
                    params.put(tokens[0], tokens[1]);
                    break;
            }
        }
        return params;
    }

    /**
     * Canonicalize the query string.
     *
     * @param sortedParamMap Parameter name-value pairs in lexicographical order.
     * @return Canonical form of query string.
     */
    private static String canonicalize(
            final SortedMap<String, String> sortedParamMap) {
        if (sortedParamMap == null || sortedParamMap.isEmpty()) {
            return "";
        }

        final StringBuilder sb = new StringBuilder(100);
        for (Map.Entry<String, String> pair : sortedParamMap.entrySet()) {
            final String key = pair.getKey().toLowerCase();
            // Ignore irrelevant parameters
            if (IRRELEVANT_PARAMETERS.contains(key) || key.startsWith("utm_")) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append('&');
            }
            sb.append(percentEncodeRfc3986(pair.getKey()));
            if (!pair.getValue().isEmpty()) {
                sb.append('=');
                sb.append(percentEncodeRfc3986(pair.getValue()));
            }
        }
        return sb.toString();
    }

    /**
     * Percent-encode values according the RFC 3986. The built-in Java
     * URLEncoder does not encode according to the RFC, so we make the extra
     * replacements.
     *
     * @param string Decoded string.
     * @return Encoded string per RFC 3986.
     */
    private static String percentEncodeRfc3986(String string) {
        try {
            string = string.replace("+", "%2B");
            string = URLDecoder.decode(string, "UTF-8");
            string = URLEncoder.encode(string, "UTF-8");
            return string.replace("+", "%20").replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (Exception e) {
            return string;
        }
    }

    /**
     * Normalize path.
     *
     * @param path the path
     * @return the string
     */
    private static String normalizePath(final String path) {
        return path.replace("%7E", "~").replace(" ", "%20");
    }

    /**
     * <p>
     * Joins the elements of the provided {@code Iterable} into a single String
     * containing the provided elements.
     * </p>
     * <p>
     * <p>
     * No delimiter is added before or after the list. Null objects or empty
     * strings within the iteration are represented by empty strings.
     * </p>
     * <p>
     * <p>
     * See the examples here: {@link #join(Object[], char)}.
     * </p>
     *
     * @param iterable  the {@code Iterable} providing the values to join together,
     *                  may be null
     * @param separator the separator character to use
     * @return the joined String, {@code null} if null iterator input
     * @since 2.3
     */
    public static String join(Iterable<?> iterable, char separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    /**
     * <p>
     * Joins the elements of the provided {@code Iterable} into a single String
     * containing the provided elements.
     * </p>
     * <p>
     * <p>
     * No delimiter is added before or after the list. A {@code null} separator
     * is the same as an empty String ("").
     * </p>
     * <p>
     * <p>
     * See the examples here: {@link #join(Object[], String)}.
     * </p>
     *
     * @param iterable  the {@code Iterable} providing the values to join together,
     *                  may be null
     * @param separator the separator character to use, null treated as ""
     * @return the joined String, {@code null} if null iterator input
     * @since 2.3
     */
    public static String join(Iterable<?> iterable, String separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    /**
     * <p>
     * Joins the elements of the provided array into a single String containing
     * the provided list of elements.
     * </p>
     * <p>
     * <p>
     * No delimiter is added before or after the list. Null objects or empty
     * strings within the array are represented by empty strings.
     * </p>
     * <p>
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([null], *)             = ""
     * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
     * StringUtils.join(["a", "b", "c"], null) = "abc"
     * StringUtils.join([null, "", "a"], ';')  = ";;a"
     * </pre>
     *
     * @param array     the array of values to join together, may be null
     * @param separator the separator character to use
     * @return the joined String, {@code null} if null array input
     * @since 2.0
     */
    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }

        return join(array, separator, 0, array.length);
    }

    /**
     * <p>
     * Joins the elements of the provided array into a single String containing
     * the provided list of elements.
     * </p>
     * <p>
     * <p>
     * No delimiter is added before or after the list. Null objects or empty
     * strings within the array are represented by empty strings.
     * </p>
     * <p>
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([null], *)             = ""
     * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
     * StringUtils.join(["a", "b", "c"], null) = "abc"
     * StringUtils.join([null, "", "a"], ';')  = ";;a"
     * </pre>
     *
     * @param array      the array of values to join together, may be null
     * @param separator  the separator character to use
     * @param startIndex the first index to start joining from. It is an error to pass
     *                   in an end index past the end of the array
     * @param endIndex   the index to stop joining from (exclusive). It is an error to
     *                   pass in an end index past the end of the array
     * @return the joined String, {@code null} if null array input
     * @since 2.0
     */
    public static String join(Object[] array, char separator, int startIndex,
                              int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }

        StringBuilder buf = new StringBuilder(noOfItems * 16);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * <p>
     * Joins the elements of the provided array into a single String containing
     * the provided list of elements.
     * </p>
     * <p>
     * <p>
     * No delimiter is added before or after the list. A {@code null} separator
     * is the same as an empty String (""). Null objects or empty strings within
     * the array are represented by empty strings.
     * </p>
     * <p>
     * <pre>
     * StringUtils.join(null, *)                = null
     * StringUtils.join([], *)                  = ""
     * StringUtils.join([null], *)              = ""
     * StringUtils.join(["a", "b", "c"], "--")  = "a--b--c"
     * StringUtils.join(["a", "b", "c"], null)  = "abc"
     * StringUtils.join(["a", "b", "c"], "")    = "abc"
     * StringUtils.join([null, "", "a"], ',')   = ",,a"
     * </pre>
     *
     * @param array     the array of values to join together, may be null
     * @param separator the separator character to use, null treated as ""
     * @return the joined String, {@code null} if null array input
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    /**
     * <p>
     * Joins the elements of the provided array into a single String containing
     * the provided list of elements.
     * </p>
     * <p>
     * <p>
     * No delimiter is added before or after the list. A {@code null} separator
     * is the same as an empty String (""). Null objects or empty strings within
     * the array are represented by empty strings.
     * </p>
     * <p>
     * <pre>
     * StringUtils.join(null, *)                = null
     * StringUtils.join([], *)                  = ""
     * StringUtils.join([null], *)              = ""
     * StringUtils.join(["a", "b", "c"], "--")  = "a--b--c"
     * StringUtils.join(["a", "b", "c"], null)  = "abc"
     * StringUtils.join(["a", "b", "c"], "")    = "abc"
     * StringUtils.join([null, "", "a"], ',')   = ",,a"
     * </pre>
     *
     * @param array      the array of values to join together, may be null
     * @param separator  the separator character to use, null treated as ""
     * @param startIndex the first index to start joining from. It is an error to pass
     *                   in an end index past the end of the array
     * @param endIndex   the index to stop joining from (exclusive). It is an error to
     *                   pass in an end index past the end of the array
     * @return the joined String, {@code null} if null array input
     */
    public static String join(Object[] array, String separator, int startIndex,
                              int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }

        // endIndex - startIndex > 0: Len = NofStrings *(len(firstString) +
        // len(separator))
        // (Assuming that all Strings are roughly equally long)
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }

        StringBuilder buf = new StringBuilder(noOfItems * 16);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    // Joining
    // -----------------------------------------------------------------------

    /**
     * <p>
     * Joins the elements of the provided array into a single String containing
     * the provided list of elements.
     * </p>
     * <p>
     * <p>
     * No separator is added to the joined String. Null objects or empty strings
     * within the array are represented by empty strings.
     * </p>
     * <p>
     * <pre>
     * StringUtils.join(null)            = null
     * StringUtils.join([])              = ""
     * StringUtils.join([null])          = ""
     * StringUtils.join(["a", "b", "c"]) = "abc"
     * StringUtils.join([null, "", "a"]) = "a"
     * </pre>
     *
     * @param <T>      the specific type of values to join together
     * @param elements the values to join together, may be null
     * @return the joined String, {@code null} if null array input
     * @since 3.0 Changed signature to use varargs
     */
    @SuppressWarnings("unchecked")
    public static <T> String join(T... elements) {
        return join(elements, null);
    }

    // Nested extraction
    // -----------------------------------------------------------------------

    // Splitting
    // -----------------------------------------------------------------------

    /**
     * <p>
     * Splits the provided text into an array, using whitespace as the
     * separator. Whitespace is defined by {@link Character#isWhitespace(char)}.
     * </p>
     * <p>
     * <p>
     * The separator is not included in the returned String array. Adjacent
     * separators are treated as one separator. For more control over the split
     * use the StrTokenizer class.
     * </p>
     * <p>
     * <p>
     * A {@code null} input String returns {@code null}.
     * </p>
     * <p>
     * <pre>
     * StringUtils.split(null)       = null
     * StringUtils.split("")         = []
     * StringUtils.split("abc def")  = ["abc", "def"]
     * StringUtils.split("abc  def") = ["abc", "def"]
     * StringUtils.split(" abc ")    = ["abc"]
     * </pre>
     *
     * @param str the String to parse, may be null
     * @return an array of parsed Strings, {@code null} if null String input
     */
    public static String[] split(final String str) {
        return split(str, null, -1);
    }

    /**
     * <p>
     * Splits the provided text into an array, separator specified. This is an
     * alternative to using StringTokenizer.
     * </p>
     * <p>
     * <p>
     * The separator is not included in the returned String array. Adjacent
     * separators are treated as one separator. For more control over the split
     * use the StrTokenizer class.
     * </p>
     * <p>
     * <p>
     * A {@code null} input String returns {@code null}.
     * </p>
     * <p>
     * <pre>
     * StringUtils.split(null, *)         = null
     * StringUtils.split("", *)           = []
     * StringUtils.split("a.b.c", '.')    = ["a", "b", "c"]
     * StringUtils.split("a..b.c", '.')   = ["a", "b", "c"]
     * StringUtils.split("a:b:c", '.')    = ["a:b:c"]
     * StringUtils.split("a b c", ' ')    = ["a", "b", "c"]
     * </pre>
     *
     * @param str           the String to parse, may be null
     * @param separatorChar the character used as the delimiter
     * @return an array of parsed Strings, {@code null} if null String input
     * @since 2.0
     */
    public static String[] split(final String str, final char separatorChar) {
        return splitWorker(str, separatorChar, false);
    }

    /**
     * <p>
     * Splits the provided text into an array, separators specified. This is an
     * alternative to using StringTokenizer.
     * </p>
     * <p>
     * <p>
     * The separator is not included in the returned String array. Adjacent
     * separators are treated as one separator. For more control over the split
     * use the StrTokenizer class.
     * </p>
     * <p>
     * <p>
     * A {@code null} input String returns {@code null}. A {@code null}
     * separatorChars splits on whitespace.
     * </p>
     * <p>
     * <pre>
     * StringUtils.split(null, *)         = null
     * StringUtils.split("", *)           = []
     * StringUtils.split("abc def", null) = ["abc", "def"]
     * StringUtils.split("abc def", " ")  = ["abc", "def"]
     * StringUtils.split("abc  def", " ") = ["abc", "def"]
     * StringUtils.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
     * </pre>
     *
     * @param str            the String to parse, may be null
     * @param separatorChars the characters used as the delimiters, {@code null} splits on
     *                       whitespace
     * @return an array of parsed Strings, {@code null} if null String input
     */
    public static String[] split(final String str, final String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    /**
     * <p>
     * Splits the provided text into an array with a maximum length, separators
     * specified.
     * </p>
     * <p>
     * <p>
     * The separator is not included in the returned String array. Adjacent
     * separators are treated as one separator.
     * </p>
     * <p>
     * <p>
     * A {@code null} input String returns {@code null}. A {@code null}
     * separatorChars splits on whitespace.
     * </p>
     * <p>
     * <p>
     * If more than {@code max} delimited substrings are found, the last
     * returned string includes all characters after the first {@code max - 1}
     * returned strings (including separator characters).
     * </p>
     * <p>
     * <pre>
     * StringUtils.split(null, *, *)            = null
     * StringUtils.split("", *, *)              = []
     * StringUtils.split("ab cd ef", null, 0)   = ["ab", "cd", "ef"]
     * StringUtils.split("ab   cd ef", null, 0) = ["ab", "cd", "ef"]
     * StringUtils.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
     * StringUtils.split("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
     * </pre>
     *
     * @param str            the String to parse, may be null
     * @param separatorChars the characters used as the delimiters, {@code null} splits on
     *                       whitespace
     * @param max            the maximum number of elements to include in the array. A zero
     *                       or negative value implies no limit
     * @return an array of parsed Strings, {@code null} if null String input
     */
    public static String[] split(final String str, final String separatorChars,
                                 final int max) {
        return splitWorker(str, separatorChars, max, false);
    }

    /**
     * <p>
     * Splits the provided text into an array, separator string specified.
     * </p>
     * <p>
     * <p>
     * The separator(s) will not be included in the returned String array.
     * Adjacent separators are treated as one separator.
     * </p>
     * <p>
     * <p>
     * A {@code null} input String returns {@code null}. A {@code null}
     * separator splits on whitespace.
     * </p>
     * <p>
     * <pre>
     * StringUtils.splitByWholeSeparator(null, *)               = null
     * StringUtils.splitByWholeSeparator("", *)                 = []
     * StringUtils.splitByWholeSeparator("ab de fg", null)      = ["ab", "de", "fg"]
     * StringUtils.splitByWholeSeparator("ab   de fg", null)    = ["ab", "de", "fg"]
     * StringUtils.splitByWholeSeparator("ab:cd:ef", ":")       = ["ab", "cd", "ef"]
     * StringUtils.splitByWholeSeparator("ab-!-cd-!-ef", "-!-") = ["ab", "cd", "ef"]
     * </pre>
     *
     * @param str       the String to parse, may be null
     * @param separator String containing the String to be used as a delimiter,
     *                  {@code null} splits on whitespace
     * @return an array of parsed Strings, {@code null} if null String was input
     */
    public static String[] splitByWholeSeparator(final String str,
                                                 final String separator) {
        return splitByWholeSeparatorWorker(str, separator, -1, false);
    }

    /**
     * <p>
     * Splits the provided text into an array, separator string specified.
     * Returns a maximum of {@code max} substrings.
     * </p>
     * <p>
     * <p>
     * The separator(s) will not be included in the returned String array.
     * Adjacent separators are treated as one separator.
     * </p>
     * <p>
     * <p>
     * A {@code null} input String returns {@code null}. A {@code null}
     * separator splits on whitespace.
     * </p>
     * <p>
     * <pre>
     * StringUtils.splitByWholeSeparator(null, *, *)               = null
     * StringUtils.splitByWholeSeparator("", *, *)                 = []
     * StringUtils.splitByWholeSeparator("ab de fg", null, 0)      = ["ab", "de", "fg"]
     * StringUtils.splitByWholeSeparator("ab   de fg", null, 0)    = ["ab", "de", "fg"]
     * StringUtils.splitByWholeSeparator("ab:cd:ef", ":", 2)       = ["ab", "cd:ef"]
     * StringUtils.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 5) = ["ab", "cd", "ef"]
     * StringUtils.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 2) = ["ab", "cd-!-ef"]
     * </pre>
     *
     * @param str       the String to parse, may be null
     * @param separator String containing the String to be used as a delimiter,
     *                  {@code null} splits on whitespace
     * @param max       the maximum number of elements to include in the returned
     *                  array. A zero or negative value implies no limit.
     * @return an array of parsed Strings, {@code null} if null String was input
     */
    public static String[] splitByWholeSeparator(final String str,
                                                 final String separator, final int max) {
        return splitByWholeSeparatorWorker(str, separator, max, false);
    }

    /**
     * <p>
     * Splits the provided text into an array, separator string specified.
     * </p>
     * <p>
     * <p>
     * The separator is not included in the returned String array. Adjacent
     * separators are treated as separators for empty tokens. For more control
     * over the split use the StrTokenizer class.
     * </p>
     * <p>
     * <p>
     * A {@code null} input String returns {@code null}. A {@code null}
     * separator splits on whitespace.
     * </p>
     * <p>
     * <pre>
     * StringUtils.splitByWholeSeparatorPreserveAllTokens(null, *)               = null
     * StringUtils.splitByWholeSeparatorPreserveAllTokens("", *)                 = []
     * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab de fg", null)      = ["ab", "de", "fg"]
     * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab   de fg", null)    = ["ab", "", "", "de", "fg"]
     * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab:cd:ef", ":")       = ["ab", "cd", "ef"]
     * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-") = ["ab", "cd", "ef"]
     * </pre>
     *
     * @param str       the String to parse, may be null
     * @param separator String containing the String to be used as a delimiter,
     *                  {@code null} splits on whitespace
     * @return an array of parsed Strings, {@code null} if null String was input
     * @since 2.4
     */
    public static String[] splitByWholeSeparatorPreserveAllTokens(
            final String str, final String separator) {
        return splitByWholeSeparatorWorker(str, separator, -1, true);
    }

    /**
     * <p>
     * Splits the provided text into an array, separator string specified.
     * Returns a maximum of {@code max} substrings.
     * </p>
     * <p>
     * <p>
     * The separator is not included in the returned String array. Adjacent
     * separators are treated as separators for empty tokens. For more control
     * over the split use the StrTokenizer class.
     * </p>
     * <p>
     * <p>
     * A {@code null} input String returns {@code null}. A {@code null}
     * separator splits on whitespace.
     * </p>
     * <p>
     * <pre>
     * StringUtils.splitByWholeSeparatorPreserveAllTokens(null, *, *)               = null
     * StringUtils.splitByWholeSeparatorPreserveAllTokens("", *, *)                 = []
     * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab de fg", null, 0)      = ["ab", "de", "fg"]
     * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab   de fg", null, 0)    = ["ab", "", "", "de", "fg"]
     * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab:cd:ef", ":", 2)       = ["ab", "cd:ef"]
     * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-", 5) = ["ab", "cd", "ef"]
     * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-", 2) = ["ab", "cd-!-ef"]
     * </pre>
     *
     * @param str       the String to parse, may be null
     * @param separator String containing the String to be used as a delimiter,
     *                  {@code null} splits on whitespace
     * @param max       the maximum number of elements to include in the returned
     *                  array. A zero or negative value implies no limit.
     * @return an array of parsed Strings, {@code null} if null String was input
     * @since 2.4
     */
    public static String[] splitByWholeSeparatorPreserveAllTokens(
            final String str, final String separator, final int max) {
        return splitByWholeSeparatorWorker(str, separator, max, true);
    }

    /**
     * Performs the logic for the {@code splitByWholeSeparatorPreserveAllTokens}
     * methods.
     *
     * @param str               the String to parse, may be {@code null}
     * @param separator         String containing the String to be used as a delimiter,
     *                          {@code null} splits on whitespace
     * @param max               the maximum number of elements to include in the returned
     *                          array. A zero or negative value implies no limit.
     * @param preserveAllTokens if {@code true}, adjacent separators are treated as empty
     *                          token separators; if {@code false}, adjacent separators are
     *                          treated as one separator.
     * @return an array of parsed Strings, {@code null} if null String input
     * @since 2.4
     */
    private static String[] splitByWholeSeparatorWorker(final String str,
                                                        final String separator, final int max,
                                                        final boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }

        final int len = str.length();

        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }

        if (separator == null || EMPTY.equals(separator)) {
            // Split on whitespace.
            return splitWorker(str, null, max, preserveAllTokens);
        }

        final int separatorLength = separator.length();

        final ArrayList<String> substrings = new ArrayList<String>();
        int numberOfSubstrings = 0;
        int beg = 0;
        int end = 0;
        while (end < len) {
            end = str.indexOf(separator, beg);

            if (end > -1) {
                if (end > beg) {
                    numberOfSubstrings += 1;

                    if (numberOfSubstrings == max) {
                        end = len;
                        substrings.add(str.substring(beg));
                    } else {
                        // The following is OK, because String.substring( beg,
                        // end ) excludes
                        // the character at the position 'end'.
                        substrings.add(str.substring(beg, end));

                        // Set the starting point for the next search.
                        // The following is equivalent to beg = end +
                        // (separatorLength - 1) + 1,
                        // which is the right calculation:
                        beg = end + separatorLength;
                    }
                } else {
                    // We found a consecutive occurrence of the separator, so
                    // skip it.
                    if (preserveAllTokens) {
                        numberOfSubstrings += 1;
                        if (numberOfSubstrings == max) {
                            end = len;
                            substrings.add(str.substring(beg));
                        } else {
                            substrings.add(EMPTY);
                        }
                    }
                    beg = end + separatorLength;
                }
            } else {
                // String.substring( beg ) goes from 'beg' to the end of the
                // String.
                substrings.add(str.substring(beg));
                end = len;
            }
        }

        return substrings.toArray(new String[substrings.size()]);
    }

    // -----------------------------------------------------------------------

    /**
     * <p>
     * Splits the provided text into an array, using whitespace as the
     * separator, preserving all tokens, including empty tokens created by
     * adjacent separators. This is an alternative to using StringTokenizer.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     * </p>
     * <p>
     * <p>
     * The separator is not included in the returned String array. Adjacent
     * separators are treated as separators for empty tokens. For more control
     * over the split use the StrTokenizer class.
     * </p>
     * <p>
     * <p>
     * A {@code null} input String returns {@code null}.
     * </p>
     * <p>
     * <pre>
     * StringUtils.splitPreserveAllTokens(null)       = null
     * StringUtils.splitPreserveAllTokens("")         = []
     * StringUtils.splitPreserveAllTokens("abc def")  = ["abc", "def"]
     * StringUtils.splitPreserveAllTokens("abc  def") = ["abc", "", "def"]
     * StringUtils.splitPreserveAllTokens(" abc ")    = ["", "abc", ""]
     * </pre>
     *
     * @param str the String to parse, may be {@code null}
     * @return an array of parsed Strings, {@code null} if null String input
     * @since 2.1
     */
    public static String[] splitPreserveAllTokens(final String str) {
        return splitWorker(str, null, -1, true);
    }

    /**
     * <p>
     * Splits the provided text into an array, separator specified, preserving
     * all tokens, including empty tokens created by adjacent separators. This
     * is an alternative to using StringTokenizer.
     * </p>
     * <p>
     * <p>
     * The separator is not included in the returned String array. Adjacent
     * separators are treated as separators for empty tokens. For more control
     * over the split use the StrTokenizer class.
     * </p>
     * <p>
     * <p>
     * A {@code null} input String returns {@code null}.
     * </p>
     * <p>
     * <pre>
     * StringUtils.splitPreserveAllTokens(null, *)         = null
     * StringUtils.splitPreserveAllTokens("", *)           = []
     * StringUtils.splitPreserveAllTokens("a.b.c", '.')    = ["a", "b", "c"]
     * StringUtils.splitPreserveAllTokens("a..b.c", '.')   = ["a", "", "b", "c"]
     * StringUtils.splitPreserveAllTokens("a:b:c", '.')    = ["a:b:c"]
     * StringUtils.splitPreserveAllTokens("a\tb\nc", null) = ["a", "b", "c"]
     * StringUtils.splitPreserveAllTokens("a b c", ' ')    = ["a", "b", "c"]
     * StringUtils.splitPreserveAllTokens("a b c ", ' ')   = ["a", "b", "c", ""]
     * StringUtils.splitPreserveAllTokens("a b c  ", ' ')   = ["a", "b", "c", "", ""]
     * StringUtils.splitPreserveAllTokens(" a b c", ' ')   = ["", a", "b", "c"]
     * StringUtils.splitPreserveAllTokens("  a b c", ' ')  = ["", "", a", "b", "c"]
     * StringUtils.splitPreserveAllTokens(" a b c ", ' ')  = ["", a", "b", "c", ""]
     * </pre>
     *
     * @param str           the String to parse, may be {@code null}
     * @param separatorChar the character used as the delimiter, {@code null} splits on
     *                      whitespace
     * @return an array of parsed Strings, {@code null} if null String input
     * @since 2.1
     */
    public static String[] splitPreserveAllTokens(final String str,
                                                  final char separatorChar) {
        return splitWorker(str, separatorChar, true);
    }

    /**
     * Performs the logic for the {@code split} and
     * {@code splitPreserveAllTokens} methods that do not return a maximum array
     * length.
     *
     * @param str               the String to parse, may be {@code null}
     * @param separatorChar     the separate character
     * @param preserveAllTokens if {@code true}, adjacent separators are treated as empty
     *                          token separators; if {@code false}, adjacent separators are
     *                          treated as one separator.
     * @return an array of parsed Strings, {@code null} if null String input
     */
    private static String[] splitWorker(final String str,
                                        final char separatorChar, final boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        final List<String> list = new ArrayList<String>();
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
                continue;
            }
            lastMatch = false;
            match = true;
            i++;
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i));
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * <p>
     * Splits the provided text into an array, separators specified, preserving
     * all tokens, including empty tokens created by adjacent separators. This
     * is an alternative to using StringTokenizer.
     * </p>
     * <p>
     * <p>
     * The separator is not included in the returned String array. Adjacent
     * separators are treated as separators for empty tokens. For more control
     * over the split use the StrTokenizer class.
     * </p>
     * <p>
     * <p>
     * A {@code null} input String returns {@code null}. A {@code null}
     * separatorChars splits on whitespace.
     * </p>
     * <p>
     * <pre>
     * StringUtils.splitPreserveAllTokens(null, *)           = null
     * StringUtils.splitPreserveAllTokens("", *)             = []
     * StringUtils.splitPreserveAllTokens("abc def", null)   = ["abc", "def"]
     * StringUtils.splitPreserveAllTokens("abc def", " ")    = ["abc", "def"]
     * StringUtils.splitPreserveAllTokens("abc  def", " ")   = ["abc", "", def"]
     * StringUtils.splitPreserveAllTokens("ab:cd:ef", ":")   = ["ab", "cd", "ef"]
     * StringUtils.splitPreserveAllTokens("ab:cd:ef:", ":")  = ["ab", "cd", "ef", ""]
     * StringUtils.splitPreserveAllTokens("ab:cd:ef::", ":") = ["ab", "cd", "ef", "", ""]
     * StringUtils.splitPreserveAllTokens("ab::cd:ef", ":")  = ["ab", "", cd", "ef"]
     * StringUtils.splitPreserveAllTokens(":cd:ef", ":")     = ["", cd", "ef"]
     * StringUtils.splitPreserveAllTokens("::cd:ef", ":")    = ["", "", cd", "ef"]
     * StringUtils.splitPreserveAllTokens(":cd:ef:", ":")    = ["", cd", "ef", ""]
     * </pre>
     *
     * @param str            the String to parse, may be {@code null}
     * @param separatorChars the characters used as the delimiters, {@code null} splits on
     *                       whitespace
     * @return an array of parsed Strings, {@code null} if null String input
     * @since 2.1
     */
    public static String[] splitPreserveAllTokens(final String str,
                                                  final String separatorChars) {
        return splitWorker(str, separatorChars, -1, true);
    }

    /**
     * <p>
     * Splits the provided text into an array with a maximum length, separators
     * specified, preserving all tokens, including empty tokens created by
     * adjacent separators.
     * </p>
     * <p>
     * <p>
     * The separator is not included in the returned String array. Adjacent
     * separators are treated as separators for empty tokens. Adjacent
     * separators are treated as one separator.
     * </p>
     * <p>
     * <p>
     * A {@code null} input String returns {@code null}. A {@code null}
     * separatorChars splits on whitespace.
     * </p>
     * <p>
     * <p>
     * If more than {@code max} delimited substrings are found, the last
     * returned string includes all characters after the first {@code max - 1}
     * returned strings (including separator characters).
     * </p>
     * <p>
     * <pre>
     * StringUtils.splitPreserveAllTokens(null, *, *)            = null
     * StringUtils.splitPreserveAllTokens("", *, *)              = []
     * StringUtils.splitPreserveAllTokens("ab de fg", null, 0)   = ["ab", "cd", "ef"]
     * StringUtils.splitPreserveAllTokens("ab   de fg", null, 0) = ["ab", "cd", "ef"]
     * StringUtils.splitPreserveAllTokens("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
     * StringUtils.splitPreserveAllTokens("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
     * StringUtils.splitPreserveAllTokens("ab   de fg", null, 2) = ["ab", "  de fg"]
     * StringUtils.splitPreserveAllTokens("ab   de fg", null, 3) = ["ab", "", " de fg"]
     * StringUtils.splitPreserveAllTokens("ab   de fg", null, 4) = ["ab", "", "", "de fg"]
     * </pre>
     *
     * @param str            the String to parse, may be {@code null}
     * @param separatorChars the characters used as the delimiters, {@code null} splits on
     *                       whitespace
     * @param max            the maximum number of elements to include in the array. A zero
     *                       or negative value implies no limit
     * @return an array of parsed Strings, {@code null} if null String input
     * @since 2.1
     */
    public static String[] splitPreserveAllTokens(final String str,
                                                  final String separatorChars, final int max) {
        return splitWorker(str, separatorChars, max, true);
    }

    /**
     * Performs the logic for the {@code split} and
     * {@code splitPreserveAllTokens} methods that return a maximum array
     * length.
     *
     * @param str               the String to parse, may be {@code null}
     * @param separatorChars    the separate character
     * @param max               the maximum number of elements to include in the array. A zero
     *                          or negative value implies no limit.
     * @param preserveAllTokens if {@code true}, adjacent separators are treated as empty
     *                          token separators; if {@code false}, adjacent separators are
     *                          treated as one separator.
     * @return an array of parsed Strings, {@code null} if null String input
     */
    private static String[] splitWorker(final String str,
                                        final String separatorChars, final int max,
                                        final boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()

        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        final List<String> list = new ArrayList<String>();
        int sizePlus1 = 1;
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            final char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i));
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * <p>
     * Splits a String by Character type as returned by
     * {@code java.lang.Character.getType(char)}. Groups of contiguous
     * characters of the same type are returned as complete tokens.
     * <p>
     * <pre>
     * StringUtils.splitByCharacterType(null)         = null
     * StringUtils.splitByCharacterType("")           = []
     * StringUtils.splitByCharacterType("ab de fg")   = ["ab", " ", "de", " ", "fg"]
     * StringUtils.splitByCharacterType("ab   de fg") = ["ab", "   ", "de", " ", "fg"]
     * StringUtils.splitByCharacterType("ab:cd:ef")   = ["ab", ":", "cd", ":", "ef"]
     * StringUtils.splitByCharacterType("number5")    = ["number", "5"]
     * StringUtils.splitByCharacterType("fooBar")     = ["foo", "B", "ar"]
     * StringUtils.splitByCharacterType("foo200Bar")  = ["foo", "200", "B", "ar"]
     * StringUtils.splitByCharacterType("ASFRules")   = ["ASFR", "ules"]
     * </pre>
     *
     * @param str the String to split, may be {@code null}
     * @return an array of parsed Strings, {@code null} if null String input
     * @since 2.4
     */
    public static String[] splitByCharacterType(final String str) {
        return splitByCharacterType(str, false);
    }

    /**
     * <p>
     * Splits a String by Character type as returned by
     * {@code java.lang.Character.getType(char)}. Groups of contiguous
     * characters of the same type are returned as complete tokens, with the
     * following exception: the character of type
     * {@code Character.UPPERCASE_LETTER}, if any, immediately preceding a token
     * of type {@code Character.LOWERCASE_LETTER} will belong to the following
     * token rather than to the preceding, if any,
     * {@code Character.UPPERCASE_LETTER} token.
     * <p>
     * <pre>
     * StringUtils.splitByCharacterTypeCamelCase(null)         = null
     * StringUtils.splitByCharacterTypeCamelCase("")           = []
     * StringUtils.splitByCharacterTypeCamelCase("ab de fg")   = ["ab", " ", "de", " ", "fg"]
     * StringUtils.splitByCharacterTypeCamelCase("ab   de fg") = ["ab", "   ", "de", " ", "fg"]
     * StringUtils.splitByCharacterTypeCamelCase("ab:cd:ef")   = ["ab", ":", "cd", ":", "ef"]
     * StringUtils.splitByCharacterTypeCamelCase("number5")    = ["number", "5"]
     * StringUtils.splitByCharacterTypeCamelCase("fooBar")     = ["foo", "Bar"]
     * StringUtils.splitByCharacterTypeCamelCase("foo200Bar")  = ["foo", "200", "Bar"]
     * StringUtils.splitByCharacterTypeCamelCase("ASFRules")   = ["ASF", "Rules"]
     * </pre>
     *
     * @param str the String to split, may be {@code null}
     * @return an array of parsed Strings, {@code null} if null String input
     * @since 2.4
     */
    public static String[] splitByCharacterTypeCamelCase(final String str) {
        return splitByCharacterType(str, true);
    }

    /**
     * <p>
     * Splits a String by Character type as returned by
     * {@code java.lang.Character.getType(char)}. Groups of contiguous
     * characters of the same type are returned as complete tokens, with the
     * following exception: if {@code camelCase} is {@code true}, the character
     * of type {@code Character.UPPERCASE_LETTER}, if any, immediately preceding
     * a token of type {@code Character.LOWERCASE_LETTER} will belong to the
     * following token rather than to the preceding, if any,
     * {@code Character.UPPERCASE_LETTER} token.
     *
     * @param str       the String to split, may be {@code null}
     * @param camelCase whether to use so-called "camel-case" for letter types
     * @return an array of parsed Strings, {@code null} if null String input
     * @since 2.4
     */
    private static String[] splitByCharacterType(final String str,
                                                 final boolean camelCase) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty()) {
            return EMPTY_STRING_ARRAY;
        }
        final char[] c = str.toCharArray();
        final List<String> list = new ArrayList<String>();
        int tokenStart = 0;
        int currentType = Character.getType(c[tokenStart]);
        for (int pos = tokenStart + 1; pos < c.length; pos++) {
            final int type = Character.getType(c[pos]);
            if (type == currentType) {
                continue;
            }
            if (camelCase && type == Character.LOWERCASE_LETTER
                    && currentType == Character.UPPERCASE_LETTER) {
                final int newTokenStart = pos - 1;
                if (newTokenStart != tokenStart) {
                    list.add(new String(c, tokenStart, newTokenStart
                            - tokenStart));
                    tokenStart = newTokenStart;
                }
            } else {
                list.add(new String(c, tokenStart, pos - tokenStart));
                tokenStart = pos;
            }
            currentType = type;
        }
        list.add(new String(c, tokenStart, c.length - tokenStart));
        return list.toArray(new String[list.size()]);
    }

    /**
     * 获取html源代码中的body部分.
     * <p>
     * 如果没有body标签，那么返回全部
     *
     * @param html
     * @return
     */
    public static String body(String html) {
        int start_index = html.toLowerCase().indexOf("<body");
        int end_index = html.toLowerCase().lastIndexOf("</body>");
        if (start_index < 0) {
            start_index = 0;
        }
        if (end_index < 0) {
            end_index = html.length();
        }
        return html.substring(start_index, end_index);
    }

    /**
     * <p>
     * Find the Levenshtein distance between two Strings.
     * </p>
     * <p>
     * <p>
     * This is the number of changes needed to change one String into another,
     * where each change is a single character modification (deletion, insertion
     * or substitution).
     * </p>
     * <p>
     * <p>
     * The previous implementation of the Levenshtein distance algorithm was
     * from <a
     * href="http://www.merriampark.com/ld.htm">http://www.merriampark.com
     * /ld.htm</a>
     * </p>
     * <p>
     * <p>
     * Chas Emerick has written an implementation in Java, which avoids an
     * OutOfMemoryError which can occur when my Java implementation is used with
     * very large strings.<br>
     * This implementation of the Levenshtein distance algorithm is from <a
     * href="http://www.merriampark.com/ldjava.htm">http://www.merriampark.com/
     * ldjava.htm</a>
     * </p>
     * <p>
     * <pre>
     * StringUtils.getLevenshteinDistance(null, *)             = IllegalArgumentException
     * StringUtils.getLevenshteinDistance(*, null)             = IllegalArgumentException
     * StringUtils.getLevenshteinDistance("","")               = 0
     * StringUtils.getLevenshteinDistance("","a")              = 1
     * StringUtils.getLevenshteinDistance("aaapppp", "")       = 7
     * StringUtils.getLevenshteinDistance("frog", "fog")       = 1
     * StringUtils.getLevenshteinDistance("fly", "ant")        = 3
     * StringUtils.getLevenshteinDistance("elephant", "hippo") = 7
     * StringUtils.getLevenshteinDistance("hippo", "elephant") = 7
     * StringUtils.getLevenshteinDistance("hippo", "zzzzzzzz") = 8
     * StringUtils.getLevenshteinDistance("hello", "hallo")    = 1
     * </pre>
     *
     * @param s the first String, must not be null
     * @param t the second String, must not be null
     * @return result distance
     * @throws IllegalArgumentException if either String input {@code null}
     * @since 3.0 Changed signature from getLevenshteinDistance(String, String)
     * to getLevenshteinDistance(CharSequence, CharSequence)
     */
    public static int getLevenshteinDistance(CharSequence s, CharSequence t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

		/*
         * The difference between this impl. and the previous is that, rather
		 * than creating and retaining a matrix of size s.length() + 1 by
		 * t.length() + 1, we maintain two single-dimensional arrays of length
		 * s.length() + 1. The first, d, is the 'current working' distance array
		 * that maintains the newest distance cost counts as we iterate through
		 * the characters of String s. Each time we increment the index of
		 * String t we are comparing, d is copied to p, the second int[]. Doing
		 * so allows us to retain the previous cost counts as required by the
		 * algorithm (taking the minimum of the cost count to the left, up one,
		 * and diagonally up and to the left of the current cost count being
		 * calculated). (Note that the arrays aren't really copied anymore, just
		 * switched...this is clearly much better than cloning an array or doing
		 * a System.arraycopy() each time through the outer loop.)
		 * 
		 * Effectively, the difference between the two implementations is this
		 * one does not cause an out of memory condition when calculating the LD
		 * over two very large strings.
		 */

        int n = s.length(); // length of s
        int m = t.length(); // length of t

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        if (n > m) {
            // swap the input strings to consume less memory
            CharSequence tmp = s;
            s = t;
            t = tmp;
            n = m;
            m = t.length();
        }

        int p[] = new int[n + 1]; // 'previous' cost array, horizontally
        int d[] = new int[n + 1]; // cost array, horizontally
        int _d[]; // placeholder to assist in swapping p and d

        // indexes into strings s and t
        int i; // iterates through s
        int j; // iterates through t

        char t_j; // jth character of t

        int cost; // cost

        for (i = 0; i <= n; i++) {
            p[i] = i;
        }

        for (j = 1; j <= m; j++) {
            t_j = t.charAt(j - 1);
            d[0] = j;

            for (i = 1; i <= n; i++) {
                cost = s.charAt(i - 1) == t_j ? 0 : 1;
                // minimum of cell to the left+1, to the top+1, diagonally left
                // and up +cost
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1]
                        + cost);
            }

            // copy current distance counts to 'previous row' distance counts
            _d = p;
            p = d;
            d = _d;
        }

        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return p[n];
    }

    /**
     * 转换为String数组
     *
     * @param array
     * @return
     */
    public static String[] toArray(List<String> array) {
        return array.toArray(new String[]{});
    }

    public static String[] toStringArray(Object... objects) {
        String[] stringArray = Arrays.asList(objects).toArray(new String[objects.length]);
        return stringArray;
    }

    /**
     * <p>Searches a String for substrings delimited by a start and end tag,
     * returning all matching substrings in an array.</p>
     * <p>
     * <p>A {@code null} input String returns {@code null}.
     * A {@code null} open/close returns {@code null} (no match).
     * An empty ("") open/close returns {@code null} (no match).</p>
     * <p>
     * <pre>
     * StringUtils.substringsBetween("[a][b][c]", "[", "]") = ["a","b","c"]
     * StringUtils.substringsBetween(null, *, *)            = null
     * StringUtils.substringsBetween(*, null, *)            = null
     * StringUtils.substringsBetween(*, *, null)            = null
     * StringUtils.substringsBetween("", "[", "]")          = []
     * </pre>
     *
     * @param str   the String containing the substrings, null returns null, empty returns empty
     * @param open  the String identifying the start of the substring, empty returns null
     * @param close the String identifying the end of the substring, empty returns null
     * @return a String Array of substrings, or {@code null} if no match
     * @since 2.3
     */
    public static List<String> substringsBetween(final String str, final String open, final String close, boolean tokenReservedFlag) {
        if (str == null || isEmpty(open) || isEmpty(close)) {
            return null;
        }
        final int strLen = str.length();
        if (strLen == 0) {
            return new ArrayList<>();
        }
        final int closeLen = close.length();
        final int openLen = open.length();
        final List<String> list = new ArrayList<String>();
        int pos = 0;
        while (pos < strLen - closeLen) {
            int start = str.indexOf(open, pos);
            if (start < 0) {
                break;
            }
            start += openLen;
            final int end = str.indexOf(close, start);
            if (end < 0) {
                break;
            }
            if (tokenReservedFlag) {
                list.add(str.substring(start - openLen, end + closeLen));
            } else {
                list.add(str.substring(start, end));
            }
            pos = end + closeLen;
        }
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }
}
