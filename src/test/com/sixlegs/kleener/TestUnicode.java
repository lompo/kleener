package com.sixlegs.kleener;

import java.lang.reflect.*;
import java.util.*;
import org.testng.annotations.*;
import static java.lang.Character.UnicodeBlock.*;

public class TestUnicode
{
    private static final String[] javaNames = {
        "ISOControl",
        "Digit",
        "JavaIdentifierPart",
        "JavaIdentifierStart",
        "IdentifierIgnorable",
        "Letter",
        "LetterOrDigit",
        "LowerCase",
        "Mirrored",
        "SpaceChar",
        "TitleCase",
        "UnicodeIdentifierPart",
        "UnicodeIdentifierStart",
        "UpperCase",
        "Whitespace",
    };

    @Test(groups = { "unicode" })
    public void generateBlockRanges() throws Exception {
        Map<Character.UnicodeBlock,BitSet> map = Generics.newLinkedHashMap();
        for (int i = 0; i < Character.MAX_CODE_POINT; i++) {
            Character.UnicodeBlock block = Character.UnicodeBlock.of(i);
            if (block != null) {
                BitSet bits = map.get(block);
                if (bits == null)
                    map.put(block, bits = new BitSet());
                bits.set(i);
            }
        }
        System.err.println("///////// categories ////////");
        Map<Character.UnicodeBlock,List<Range>> rangeMap = getRanges(map);
        for (Character.UnicodeBlock block : map.keySet()) {
            List<Range> ranges = rangeMap.get(block);
            if (ranges.size() > 1)
                throw new IllegalStateException("block cannot have more than one range");
            Range r = ranges.get(0);
            System.err.println("0x" + Integer.toHexString(r.start) + ", " +
                               "0x" + Integer.toHexString(r.end));
        }
    }

    @Test(groups = { "unicode" })
    public void generateJavaRanges() throws Exception {
        Map<String,BitSet> map = Generics.newTreeMap();
        for (String name : javaNames) {
            Method m = Character.class.getMethod("is" + name, int.class);
            BitSet bits = new BitSet();
            map.put(name, bits);
            for (int i = 0; i < Character.MAX_CODE_POINT; i++) {
                if (((Boolean)m.invoke(null, i)).booleanValue())
                    bits.set(i);
            }
        }
        printRanges("java", map);
    }

    @Test(groups = { "unicode" })
    public void generateCategoryRanges() throws Exception {
        Map<Integer,BitSet> map = Generics.newTreeMap();
        for (int i = 0; i < Character.MAX_CODE_POINT; i++) {
            int type = Character.getType(i);
            if (type != Character.UNASSIGNED) {
                BitSet bits = map.get(type);
                if (bits == null)
                    map.put(type, bits = new BitSet());
                bits.set(i);
            }
        }
        printRanges("category", map);
    }

    private static class Range {
        final int start;
        final int end;

        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    private static <T> Map<T,List<Range>> getRanges(Map<T,BitSet> map) {
        Map<T,List<Range>> rangeMap = Generics.newHashMap();
        for (T block : map.keySet()) {
            List<Range> ranges = Generics.newArrayList();
            rangeMap.put(block, ranges);
            BitSet bits = map.get(block);
            int prev = -2;
            int start = -1;
            int count = 0;
            for (int i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i + 1)) {
                if (i != prev + 1) {
                    rangeHelper(ranges, start, prev);
                    start = i;
                }
                prev = i;
            }
            rangeHelper(ranges, start, prev);
        }
        return rangeMap;
    }

    private static void rangeHelper(List<Range> ranges, int start, int end) {
        if (start >= 0)
            ranges.add(new Range(start, end));
    }

    private static <T> void printRanges(String label, Map<T,BitSet> map) {
        Map<T,List<Range>> rangeMap = getRanges(map);
        System.err.println("//////////////// " + label + " ////////////////");
        for (T block : map.keySet()) {
            BitSet bits = map.get(block);
            List<Range> ranges = rangeMap.get(block);
            int count = ranges.size();
             if (count <= 25 || count * 2 > bits.cardinality()) {
                 StringBuilder sb = new StringBuilder();
                 sb.append(label).append("Map.put(").append(block.toString()).append(", new CharSetBuilder()");
                 for (Range r : ranges) {
                     sb.append(".add(").append(r.start);
                     if (r.start != r.end)
                         sb.append(", ").append(r.end);
                     sb.append(")");
                 }
                 sb.append(".build());");
                 System.err.println(sb.toString());
             } else {
                 System.err.println("// " + block + " has " + count + " ranges, " + bits.cardinality() + " codepoints, last is " + (bits.length() - 1));
             }
        }
    }
}
