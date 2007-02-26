package com.sixlegs.kleener;

import java.util.*;
import org.testng.annotations.*;

public class TestSplit
{
    @Test public void testSunExamples() {
        split(":", "boo:herby:foo", 2, new String[]{ "boo", "herby:foo" });
        split(":", "boo:herby:foo", 5, new String[]{ "boo", "herby", "foo" });
        split(":", "boo:herby:foo", -2, new String[]{ "boo", "herby", "foo" });
        split("o", "boo:herby:foo", 5, new String[]{ "b", "", ":herby:f", "", "" });
        split("o", "boo:herby:foo", -2, new String[]{ "b", "", ":herby:f", "", "" });
        split("o", "boo:herby:foo", 0, new String[]{ "b", "", ":herby:f" });
    }

    private void split(String regex, CharSequence input, int limit, String[] expect) {
        String[] result = Pattern.compile(regex).split(input, limit);
        assert Arrays.deepEquals(result, expect) :
            "Expected " + Arrays.deepToString(expect) + ", got " + Arrays.deepToString(result);
    }
    
    private CharSet range(Integer... range) {
        CharSetBuilder builder = new CharSetBuilder();
        for (int i = 0; i < range.length; i += 2)
            builder.add(range[i], range[i + 1]);
        System.err.println(builder);
        return builder.build();
    }
}

