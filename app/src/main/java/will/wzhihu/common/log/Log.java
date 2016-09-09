package will.wzhihu.common.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
public class Log {
	final static boolean DEBUG = true;

	public static void v(String tag, String text) {
		if (DEBUG) {
			android.util.Log.v(tag, text);
		}
	}

	public static void d(String tag, String text) {
		if (DEBUG) {
			android.util.Log.d(tag, text);
		}
	}

	public static void i(String tag, String text) {
		if (DEBUG) {
			android.util.Log.i(tag, text);
		}
	}

	public static void w(String tag, String text) {
		if (DEBUG) {
			android.util.Log.w(tag, text);
		}
	}

	public static void e(String tag, String text) {
		if (DEBUG) {
			android.util.Log.e(tag, text);
		}
	}

    public static void e(String tag, String text, Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        e(tag, text + "\n" + writer.toString());
    }

    public static void printStack(String tag, Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        e(tag, writer.toString());
    }

	public static void v(String tag, String format, Object... args) {
		v(tag, String.format(format, args));
	}

	public static void d(String tag, String format, Object... args) {
		d(tag, String.format(format, args));
	}

	public static void i(String tag, String format, Object... args) {
		i(tag, String.format(format, args));
	}

	public static void w(String tag, String format, Object... args) {
		w(tag, String.format(format, args));
	}

	public static void e(String tag, String format, Object... args) {
		e(tag, String.format(format, args));
	}
}