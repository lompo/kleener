package com.sixlegs.kleener;

import static com.sixlegs.kleener.Expression.*;
import static com.sixlegs.kleener.Pattern.CompileType.*;
import java.util.*;
import java.util.regex.MatchResult;

public class Main
{
    // sample from wikipedia
    // "(public|private|protected|)\s*(\w+)\s+(\w+)\s*\("
    
    public static void main(String[] args) throws Exception {            
        Expression e =
            paren(concat(repeat(paren(or(literal("public"),
                                         literal("private"),
                                         literal("protected")), 1), 0, 1),
                         repeat(literal(PosixCharSet.Space), 0, 0),
                         paren(repeat(literal(PosixCharSet.Alnum), 1, 0), 2),
                         repeat(literal(PosixCharSet.Space), 1, 0),
                         paren(repeat(literal(PosixCharSet.Alnum), 1, 0), 3),
                         repeat(literal(PosixCharSet.Space), 0, 0),
                         literal("(")), 0);

        String str1 = "public  int main(";
        String str2 = " private void foo (";
        
        test(e, str1, NFA, 10000);
        test(e, str2, NFA, 1);
        test(e, str1, DFA, 10000);
        test(e, str2, DFA, 1);
        test(buildCrazy(29), repeatString("a", 29), NFA, 1);
        test(buildCrazy(29), repeatString("a", 29), DFA, 1);
        test(buildCrazy(100), repeatString("a", 100), NFA, 1);
        test(buildCrazy(100), repeatString("a", 100), DFA, 1);

        List<Long> times = new ArrayList<Long>();
        for (int n = 1; n <= 100; n++) {
            Pattern p = buildCrazy(n).compile(null, NFA);
            String str = repeatString("a", n);
            long t = System.nanoTime();
            if (!p.matcher(str).matches())
                throw new IllegalStateException("expected match");
            t = System.nanoTime() - t;
            times.add(t / 1000);
        }
        System.err.println(times);
    }

    private static Expression buildCrazy(int n) {
        Expression[] array = new Expression[n + 1];
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
        Pattern p = e.compile(null, type);
        long t1 = System.currentTimeMillis();
        Matcher matcher = p.matcher(str);
        if (!matcher.matches())
            throw new IllegalStateException("expected match");
        for (int i = 1; i < count; i++)
            matcher.matches();
        long t2 = System.currentTimeMillis();
        
        for (int group = 0; group <= matcher.groupCount(); group++)
            System.err.println("group " + group + ": >>>" + matcher.group(group) + "<<<");
        System.err.println(type + " compile=" + (t1 - t0) + " run=" + (t2 - t1) + " (" + count + " iterations)");
    }
}
