package net.kojira.util;

public class L {
    public static final boolean DEBUG = true;

    private static StringBuilder builder = new StringBuilder();

    public static void v(String msg) {
        if (DEBUG && msg != null) {
            String log = getTracedText(msg);
            android.util.Log.v(getTag(), log);
        }
    }

    public static void d(String msg) {
        if (DEBUG && msg != null) {
            String log = getTracedText(msg);
            android.util.Log.d(getTag(), log);
        }
    }

    public static void i(String msg) {
        if (DEBUG && msg != null) {
            String log = getTracedText(msg);
            android.util.Log.i(getTag(), log);
        }
    }

    public static void w(String msg) {
        if (DEBUG && msg != null) {
            String log = getTracedText(msg);
            android.util.Log.w(getTag(), log);
        }
    }

    public static void e(String msg) {
        if (DEBUG && msg != null) {
            String log = getTracedText(msg);
            android.util.Log.e(getTag(), log);
        }
    }

    public static void e(Exception e) {
        if (DEBUG) {
            String log;
            if (null != e) {
                log = getTracedText(e.getMessage());
            } else {
                log = "null";
            }
            android.util.Log.e(getTag(), log, e);
        }
    }

    private static String getTracedText(String text) {
        if (DEBUG) {
            StackTraceElement[] ste = (new Throwable()).getStackTrace();
            builder.setLength(0);
            builder.append(ste[2].getMethodName());
            builder.append("(");
            builder.append(ste[2].getFileName());
            builder.append(":");
            builder.append(ste[2].getLineNumber());
            builder.append(") ");
            if (null != text) {
                builder.append(text);
            } else {
                builder.append("null");
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    private static String getTag() {
        StackTraceElement[] ste = (new Throwable()).getStackTrace();
        String className = ste[2].getClassName();
        return className.substring(className.lastIndexOf("."));
    }

}