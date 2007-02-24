package com.sixlegs.kleener;

import static com.sixlegs.kleener.Expression.*;
import static com.sixlegs.kleener.Pattern.Type.*;
import java.util.*;

public class Main
{
    // sample from wikipedia
    // "(public|private|protected|)\s*(\w+)\s+(\w+)\s*\("
    
    private static CharSet SPACE = new CharSetBuilder().add(" \t\n\r\f\n").build();
    private static CharSet ALNUM = new CharSetBuilder().add('a', 'z').add('A', 'Z').add('0', '9').build();

    public static void main(String[] args) throws Exception {
        Expression e =
            concat(repeat(or(literal("public"),
                             literal("private"),
                             literal("protected")), 0, 1),
                   repeat(literal(SPACE), 0, 0),
                   repeat(literal(ALNUM), 1, 0),
                   repeat(literal(SPACE), 1, 0),
                   repeat(literal(ALNUM), 1, 0),
                   repeat(literal(SPACE), 0, 0),
                   literal("("));

        String str = "public  int main(";
        test(e, str, NFA);
        test(e, str, DFA);

        List<Long> times = new ArrayList<Long>();
        for (int n = 1; n <= 100; n++) {
            Pattern p = buildCrazy(n).compile(DFA);
            str = repeatString("a", 29);
            long t = System.nanoTime();
            p.matches(str);
            t = System.nanoTime() - t;
            times.add(t / 1000);
        }
        System.err.println(times);
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

    private static final int COUNT = 10000;
    private static void test(Expression e, String str, Pattern.Type type) {
        long t0 = System.currentTimeMillis();
        Pattern p = e.compile(type);
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++)
            if (!p.matches(str))
                throw new IllegalArgumentException("does not match");
        long t2 = System.currentTimeMillis();
        System.err.println(type + " compile=" + (t1 - t0) + " run=" + (t2 - t1) + " (" + COUNT + " iterations)");
    }
}
