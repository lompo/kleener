package com.sixlegs.kleener;

import java.util.*;
import java.util.regex.MatchResult;
import org.testng.annotations.*;
import static com.sixlegs.kleener.Expression.*;
import static com.sixlegs.kleener.Pattern.CompileType.*;

public class TestPerf
{
    @Test(groups = { "perf" })
    public void testWikipedia() {
        // "(public|private|protected|)\s*(\w+)\s+(\w+)\s*\("
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
    }

    @Test(groups = { "perf" })
    public void testRussCox() {
        test(buildCrazy(29), repeatString("a", 29), NFA, 1);
        test(buildCrazy(29), repeatString("a", 29), DFA, 1);
        test(buildCrazy(100), repeatString("a", 100), NFA, 1);
        test(buildCrazy(100), repeatString("a", 100), DFA, 1);
    }

    @Test(groups = { "perf" })
    public void testRussCoxGraph() {
        List<Long> times1 = new ArrayList<Long>();
        for (int n = 1; n <= 100; n++) {
            Pattern p = buildCrazy(n).compile(null, NFA);
            String str = repeatString("a", n);
            long t = System.nanoTime();
            if (!p.matcher(str).matches())
                throw new IllegalStateException("expected match");
            t = System.nanoTime() - t;
            times1.add(t / 1000);
        }

        List<Long> times2 = new ArrayList<Long>();
        for (int n = 1; n <= 23; n++) {
            java.util.regex.Pattern p =
                java.util.regex.Pattern.compile(repeatString("a?", n) + repeatString("a", n));
            String str = repeatString("a", n);
            long t = System.nanoTime();
            if (!p.matcher(str).matches())
                throw new IllegalStateException("expected match");
            t = System.nanoTime() - t;
            times2.add(t / 1000);
        }
        
        System.err.println(times1);
        System.err.println(times2);
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
    
    @Test(groups = { "perf" })
    public void testTimBray() {
        // (<[^/]([^>]*[^/>])?>)|(</[^>]*>)|(<[^>]*/>)|((\p{Lu}|\p{Ll}|\p{Lt}|\p{Nd}|\p{Nl}|\p{No}|[\u4e00-\u9fa5]|\u3007|[\u3021-\u3029])((\p{Lu}|\p{Ll}|\p{Lt}|\p{Nd}|\p{Nl}|\p{No}|[-._:']|[\u4e00-\u9fa5]|\u3007|[\u3021-\u3029])*(\p{Lu}|\p{Ll}|\p{Lt}|\p{Nd}|\p{Nl}|\p{No}|[\u4e00-\u9fa5]|\u3007|[\u3021-\u3029]))?)

        CharSet c1 =
            UnionCharSet.union(Arrays.asList(UnicodeCategoryCharSet.get(Character.UPPERCASE_LETTER),
                                             UnicodeCategoryCharSet.get(Character.LOWERCASE_LETTER),
                                             UnicodeCategoryCharSet.get(Character.TITLECASE_LETTER),
                                             UnicodeCategoryCharSet.get(Character.DECIMAL_DIGIT_NUMBER),
                                             UnicodeCategoryCharSet.get(Character.LETTER_NUMBER),
                                             UnicodeCategoryCharSet.get(Character.OTHER_NUMBER),
                                             new CharSetBuilder().add(0x4300, 0x9fa5).add(0x3007).add(0x3021, 0x3029).build()));

        CharSet c2 =
            UnionCharSet.union(Arrays.asList(UnicodeCategoryCharSet.get(Character.UPPERCASE_LETTER),
                                             UnicodeCategoryCharSet.get(Character.LOWERCASE_LETTER),
                                             UnicodeCategoryCharSet.get(Character.TITLECASE_LETTER),
                                             UnicodeCategoryCharSet.get(Character.DECIMAL_DIGIT_NUMBER),
                                             UnicodeCategoryCharSet.get(Character.LETTER_NUMBER),
                                             UnicodeCategoryCharSet.get(Character.OTHER_NUMBER),
                                             new CharSetBuilder().add(0x4300, 0x9fa5).add(0x3007).add(0x3021, 0x3029).build(),
                                             new CharSetBuilder().add("-._:'").build()));

        // TODO: add rest of parens
        Expression e =
            paren(or(concat(literal("<"),
                            literal(new SingleCharSet('/'), true),
                            repeat(concat(repeat(literal(new SingleCharSet('>'), true), 0, 0),
                                          literal(new CharSetBuilder().add("/>").build(), true)), 0, 1),
                            literal(">")),
                     concat(literal("</"),
                            repeat(literal(new SingleCharSet('>'), true), 0, 0),
                            literal(">")),
                     concat(literal("<"),
                            repeat(literal(new SingleCharSet('>'), true), 0, 0),
                            literal("/>")),
                     concat(literal(c1),
                            repeat(concat(repeat(literal(c2), 0, 0),
                                          literal(c1)), 0, 1))), 0);

        Pattern p = e.compile(null, DFA);
        // TODO: run it!
    }

    @Test(groups = { "perf" })
    public void testTusker() {
        // "^(([^:]+)://)?([^:/]+)(:([0-9]+))?(/.*)"
        // "(([^:]+)://)?([^:/]+)(:([0-9]+))?(/.*)"
        // "usd [+-]?[0-9]+.[0-9][0-9]"
        // "\\b(\\w+)(\\s+\\1)+\\b"
        // "\\{(\\d+):(([^}](?!-} ))*)"
    }
}
