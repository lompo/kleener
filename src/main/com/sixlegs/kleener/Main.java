package com.sixlegs.kleener;

import static com.sixlegs.kleener.Expression.*;
import static com.sixlegs.kleener.Pattern.CompileType.*;
import java.util.*;

public class Main
{
    // sample from wikipedia
    // "(public|private|protected|)\s*(\w+)\s+(\w+)\s*\("
    
    private static CharSet SPACE = new CharSetBuilder().add(" \t\n\r\f\n").build();
    private static CharSet ALNUM = new CharSetBuilder().add('a', 'z').add('A', 'Z').add('0', '9').build();

    public static void main(String[] args) throws Exception {
        Expression e =
            paren(concat(repeat(paren(or(literal("public"),
                                         literal("private"),
                                         literal("protected")), 1), 0, 1),
                         repeat(literal(SPACE), 0, 0),
                         paren(repeat(literal(ALNUM), 1, 0), 2),
                         repeat(literal(SPACE), 1, 0),
                         paren(repeat(literal(ALNUM), 1, 0), 3),
                         repeat(literal(SPACE), 0, 0),
                         literal("(")), 0);

        String str = "public  int main(";
        test(e, str, NFA, 10000);
        test(e, str, DFA, 10000);
        test(buildCrazy(29), repeatString("a", 29), NFA, 1);
        test(buildCrazy(29), repeatString("a", 29), DFA, 1);
        test(buildCrazy(100), repeatString("a", 100), NFA, 1);
        test(buildCrazy(100), repeatString("a", 100), DFA, 1);
        
        /*
        List<Long> times = new ArrayList<Long>();
        for (int n = 1; n <= 100; n++) {
            Pattern p = buildCrazy(n).compile(NFA);
            str = repeatString("a", 29);
            long t = System.nanoTime();
            p.matches(str);
            t = System.nanoTime() - t;
            times.add(t / 1000);
        }
        System.err.println(times);
        */
    }

    private static Expression buildCrazy(int n) {
        Expression[] array = new Expression[n + 1];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++)
            array[i] = repeat(literal("a"), 0, 1);
        array[n] = literal(repeatString("a", n));
        return paren(concat(array), 0);
    }

    private static String repeatString(String s, int n) {
        StringBuilder sb = new StringBuilder(s.length() * n);
        for (int i = 0; i < n; i++)
            sb.append(s);
        return sb.toString();
    }

    private static void test(Expression e, String str, Pattern.CompileType type, int count) {
        long t0 = System.currentTimeMillis();
        Pattern p = e.compile(type);
        long t1 = System.currentTimeMillis();
        MatchResult result = p.matches(str);
        if (result == null)
            throw new IllegalArgumentException("does not match");
        for (int i = 1; i < count; i++)
            p.matches(str);        
        long t2 = System.currentTimeMillis();
        
        for (int group = 0; group <= result.groupCount(); group++)
            System.err.println("group " + group + ": >>>" + result.group(group) + "<<<");
        System.err.println(type + " compile=" + (t1 - t0) + " run=" + (t2 - t1) + " (" + count + " iterations)");
    }
}
