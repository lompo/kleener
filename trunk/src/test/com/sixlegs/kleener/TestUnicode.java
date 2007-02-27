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
        printRanges("block", map);
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

    private static <T> void printRanges(String label, Map<T,BitSet> map) {
        System.err.println("//////////////// " + label + " ////////////////");
        for (T block : map.keySet()) {
            StringBuilder sb = new StringBuilder();
            BitSet bits = map.get(block);
            int prev = -2;
            int start = -1;
            int count = 0;
            sb.append(label).append("Map.put(").append(block.toString()).append(", new CharSetBuilder()");
            for (int i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i + 1)) {
                if (i != prev + 1) {
                    count += rangeHelper(sb, start, prev);
                    start = i;
                }
                prev = i;
            }
            count += rangeHelper(sb, start, prev);
            sb.append(".build());");
            if (count > 25) {
                System.err.println("// " + block + " has " + count + " ranges");
            } else {
                System.err.println(sb);
            }
        }
    }

    private static int rangeHelper(StringBuilder sb, int start, int end) {
        if (start >= 0) {
            sb.append(".add(").append(start).append(", ").append(end).append(")");
            return 1;
        }
        return 0;
    }

}
