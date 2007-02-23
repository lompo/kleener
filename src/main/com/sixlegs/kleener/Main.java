package com.sixlegs.kleener;

import static com.sixlegs.kleener.Expression.*;
import static com.sixlegs.kleener.Pattern.Type.*;

public class Main
{
    // sample from wikipedia
    // "(public|private|protected|)\s*(\w+)\s+(\w+)\s*\("

    public static void main(String[] args) throws Exception {
        Expression e =
            concat(repeat(or(literal("public"),
                             literal("private"),
                             literal("protected")), 0, 1),
                   repeat(space(), 0, 0),
                   repeat(alnum(), 1, 0),
                   repeat(space(), 1, 0),
                   repeat(alnum(), 1, 0),
                   repeat(space(), 0, 0),
                   literal("("));

        String str = "public  int main(";
        test(e, str, NFA);
        test(e, str, DFA);

        int n = 29;
        e = buildCrazy(n);
        str = repeatString("a", 29);
        test(e, str, NFA);
        test(e, str, DFA);
    }

    private static Expression buildCrazy(int n) {
        Expression[] array = new Expression[n + 1];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++)
            array[i] = repeat(literal("a"), 0, 1);
        array[n] = literal(repeatString("a", n));
        return concat(array);
    }

    private static String repeatString(String s, int n) {
        StringBuilder sb = new StringBuilder(s.length() * n);
        for (int i = 0; i < n; i++)
            sb.append(s);
        return sb.toString();
    }

    private static Expression space() {
        return literal(new CharSet(" \t\n\r\f\n"));
    }
    
    private static Expression alnum() {
        return literal(new CharSet("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"));
    }

    private static final int COUNT = 1000;
    private static void test(Expression e, String str, Pattern.Type type) {
        long t0 = System.currentTimeMillis();
        Pattern p = e.compile(type);
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++)
            p.matches(str);
        long t2 = System.currentTimeMillis();
        System.err.println(type + " compile=" + (t1 - t0) + " run=" + (t2 - t1) + " (" + COUNT + " iterations)");
    }
}
