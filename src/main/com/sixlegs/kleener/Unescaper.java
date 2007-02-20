package com.sixlegs.kleener;

import java.io.*;
import java.util.*;

class Unescaper extends FilterReader
{
    private PushbackReader push;
    private boolean escaped = false;

    public Unescaper(Reader r) {
        super(r);
        push = new PushbackReader(r, 3);
    }

    public boolean wasEscaped() {
        return escaped;
    }

    public boolean markSupported() {
        return false;
    }

    public int read() throws IOException {
        int ch = push.read();
        try {
            escaped = false;
            if (ch != '\\')
                return ch;
            escaped = true;
            ch = push.read();
            switch ((char)ch) {
            case '0':
            case '1':
            case '2':
            case '3':
                int ch2 = push.read();
                int ch3 = push.read();
                int build = ch * 64 - 06660;
                if (ch2 >= '0' && ch2 <= '7') {
                    build += ch2 * 8;
                    if (ch3 >= '0' && ch3 <= '7') {
                        build += ch3;
                    } else {
                        push.unread(ch3);
                    }
                } else {
                    push.unread(ch2);
                    push.unread(ch3);
                }
                return build;
            case 'n': return '\n';
            case 'r': return '\r';
            case 'b': return '\b';
            case 't': return '\t';
            case 'f': return '\f';
            case 'x':
                return Integer.parseInt("" + (char)push.read() + (char)push.read(), 16);
            case 'u': 
                return Integer.parseInt("" + 
                                        (char)push.read() + (char)push.read() + 
                                        (char)push.read() + (char)push.read(),
                                        16);
            default:
                return ch;
            }
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Illegal \\" + ch + " escape sequence");
        }
    }

    /*
    // used for quoted strings
    public static Expression parse(String str)
    throws IOException
    {
        Expression build = null;
        StringBuffer sb = new StringBuffer(str.length());
        Unescaper unesc = new Unescaper(new StringReader(str), false);
        int c;
        for (;;) {
            c = unesc.read();
            switch (c) {
            case -1: 
            case 10:
                Expression newexp = new StringExp(sb.toString());
                if (c == 10) {
                    newexp = new CatExp(newexp, Expression.endl);
                }
                if (build != null) {
                    build = new CatExp(build, newexp);
                } else {
                    build = newexp;
                }
                sb.setLength(0);
                if (c == -1) {
                    return build;
                }
                break;
            default:
                sb.append((char)c);
            }
        }
    }

    // used for token strings
    public static String convert(String str)
    throws IOException
    {
        StringBuffer sb = new StringBuffer(str.length());
        Unescaper unesc = new Unescaper(new StringReader(str), false);
        int c;
        while ((c = unesc.read()) != -1) {
            sb.append((char)c);
        }
        return sb.toString();
    }
    */
}
