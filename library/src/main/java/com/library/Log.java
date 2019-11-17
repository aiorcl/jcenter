package com.library;

import android.text.TextUtils;

import java.util.Locale;


/**
 * Log
 *
 * @ProjectName HuiLife_Android
 * @Author John
 * @CreateTime 2018/10/28 15:11:21
 * @Email 820858929@qq.com
* @Version V01.00.01.20181028
 */
@SuppressWarnings("all")
public final class Log {
    public static final int DEFAULT_VALUE = Integer.MAX_VALUE;
    private static TagType mTagType = TagType.OUTER;
    private static int mLevel = android.util.Log.VERBOSE;
    private static boolean mOutput = true;

    /**
     * <h1>TAG Type（ALL【完整类名】, OUTER【如果有包含内部类，使用最外部的类名作为TAG。】, INNER【如果有包含内部类，使用内部类名作为TAG。】）</h1>
     */
    public enum TagType {
        ALL, OUTER, INNER
    }

    /**
     * <h1>Log Level（VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT）</h1>
     */
    public enum Level {
        VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT
    }

    private Log() {
    }

    /**
     * Setting Log output parameter(Need to output for true,Level and TagType will work.).
     *
     * @param output  Is print
     * @param level   Level
     * @param tagType Output Tag and method name type
     * @return Setting Level
     */
    public static int setLogOutputParameter(final boolean output, final TagType tagType, final Level level) {
        if (null == level || null == tagType) {
            throw new IllegalArgumentException("Level or TagType is error.");
        }
        mOutput = output;
        mTagType = tagType;
        switch (level) {
            case VERBOSE:
                mLevel = android.util.Log.VERBOSE;
                break;
            case DEBUG:
                mLevel = android.util.Log.DEBUG;
                break;
            case INFO:
                mLevel = android.util.Log.INFO;
                break;
            case WARN:
                mLevel = android.util.Log.WARN;
                break;
            case ERROR:
                mLevel = android.util.Log.ERROR;
                break;
            default:
                mLevel = android.util.Log.ASSERT;
        }
        return mLevel;
    }

    //TODO Log.v
    public static int v(Object... msg) {
        final String[] classAndMethodName = classAndMethodName(mTagType);
        return v(classAndMethodName[0], String.format(Locale.getDefault(), "%s # %s", classAndMethodName[1], buildMsg(msg)));
    }

    public static int v(String tag, String msg) {
        return v(tag, msg, null);
    }

    public static int v(String tag, String msg, Throwable tr) {
        return outputPrint(android.util.Log.VERBOSE, tag, msg, tr);
    }

    //TODO log.d
    public static int d(Object... msg) {
        final String[] classAndMethodName = classAndMethodName(mTagType);
        return d(classAndMethodName[0], String.format(Locale.getDefault(), "%s # %s", classAndMethodName[1], buildMsg(msg)));
    }

    public static int d(String tag, String msg) {
        return d(tag, msg, null);
    }

    public static int d(String tag, String msg, Throwable tr) {
        return outputPrint(android.util.Log.DEBUG, tag, msg, tr);
    }

    //TODO log.i
    public static int i(Object... msg) {
        final String[] classAndMethodName = classAndMethodName(mTagType);
        return i(classAndMethodName[0], String.format(Locale.getDefault(), "%s # %s", classAndMethodName[1], buildMsg(msg)));
    }

    public static int i(String tag, String msg) {
        return i(tag, msg, null);
    }

    public static int i(String tag, String msg, Throwable tr) {
        return outputPrint(android.util.Log.INFO, tag, msg, tr);
    }

    //TODO log.w
    public static int w(Object... msg) {
        final String[] classAndMethodName = classAndMethodName(mTagType);
        return w(classAndMethodName[0], String.format(Locale.getDefault(), "%s # %s", classAndMethodName[1], buildMsg(msg)));
    }

    public static int w(String tag, String msg) {
        return w(tag, msg, null);
    }

    public static int w(String tag, Throwable tr) {
        return w(tag, null, tr);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return outputPrint(android.util.Log.WARN, tag, msg, tr);
    }

    //TODO log.e
    public static int e(Object... msg) {
        final String[] classAndMethodName = classAndMethodName(mTagType);
        return e(classAndMethodName[0], String.format(Locale.getDefault(), "%s # %s", classAndMethodName[1], buildMsg(msg)));
    }

    public static int e(String tag, String msg) {
        return e(tag, msg, null);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return outputPrint(android.util.Log.ERROR, tag, msg, tr);
    }

    //TODO log.wtf
    public static int wtf(Object... msg) {
        final String[] classAndMethodName = classAndMethodName(mTagType);
        return wtf(classAndMethodName[0], String.format(Locale.getDefault(), "%s # %s", classAndMethodName[1], buildMsg(msg)));
    }

    public static int wtf(String tag, String msg) {
        return wtf(tag, msg, null);
    }

    public static int wtf(String tag, Throwable tr) {
        return wtf(tag, null, tr);
    }

    public static int wtf(String tag, String msg, Throwable tr) {
        return outputPrint(android.util.Log.ASSERT, tag, msg, tr);
    }

    //TODO Gets the default TAG and Method name
    private static String[] classAndMethodName(final TagType tagType) {
        if (null == tagType) {
            throw new IllegalArgumentException("TagType is error");
        }
        final String[] classAndMethodName = {Log.class.getSimpleName(), ""};
        final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        if (null != stackTraceElement) {
            String className = stackTraceElement.getClassName();
            className = className.substring(className.lastIndexOf(".") + 1);
            if (className.contains("$")) {
                switch (tagType) {
                    case INNER:
                        className = className.substring(className.lastIndexOf("$") + 1);
                        break;
                    case OUTER:
                        className = className.substring(0, className.indexOf("$"));
                        break;
                    default:
                        //Default ALL
                        break;
                }
            }
            classAndMethodName[0] = className;
            classAndMethodName[1] = stackTraceElement.getMethodName() + "(" + stackTraceElement.getLineNumber() + ")";
        }
        return classAndMethodName;
    }

    //TODO Output
    private static boolean isOutputPrint(final int level, final String msg, final Throwable tr) {
        return mOutput && level >= mLevel && !(null == tr && TextUtils.isEmpty(msg));
    }

    private static final int outputPrint(final int level, final String tag, final String msg, final Throwable tr) {
        if (!isOutputPrint(level, msg, tr)) {
            return DEFAULT_VALUE;
        }
        final int result;
        switch (level) {
            case android.util.Log.VERBOSE:
                result = android.util.Log.v(tag, msg, tr);
                break;
            case android.util.Log.DEBUG:
                result = android.util.Log.d(tag, msg, tr);
                break;
            case android.util.Log.INFO:
                result = android.util.Log.i(tag, msg, tr);
                break;
            case android.util.Log.WARN:
                result = android.util.Log.w(tag, msg, tr);
                break;
            case android.util.Log.ERROR:
                result = android.util.Log.e(tag, msg, tr);
                break;
            case android.util.Log.ASSERT:
                result = android.util.Log.wtf(tag, msg, tr);
                break;
            default:
                result = DEFAULT_VALUE;
        }
        return result;
    }

    private static final String buildMsg(final Object... msg) {
        final String separator = " -> ";
        final StringBuilder builder = new StringBuilder();
        for (final Object obj : msg) {
            try {
                builder.append(isArray(obj) ? array((Object[]) obj) : obj);
            } catch (final Exception e) {
                builder.append(obj);
            }
            builder.append(separator);
        }
        if (0 < builder.length()) {
            builder.setLength(builder.length() - separator.length());
        }
        return builder.toString();
    }

    private static boolean isArray(final Object obj) {
        return null != obj && obj.getClass().isArray();
    }

    private static final String array(final Object[] objs) {
        final StringBuilder builder = new StringBuilder();
        if (null != objs && 0 < objs.length) {
            final String separator = ", ";
            for (final Object obj : objs) {
                builder.append(obj);
                builder.append(separator);
            }
            if (0 < builder.length()) {
                builder.setLength(builder.length() - separator.length());
            }
        }
        return String.format("[%s]", builder.toString());
    }
}