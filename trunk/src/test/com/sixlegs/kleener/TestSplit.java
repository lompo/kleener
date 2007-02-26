package com.sixlegs.kleener;

import org.testng.annotations.*;

public class TestSplit
{
    @Test public void testSunExamples() {
        split(":", "boo:and:foo", 2, new String[]{ "boo", "and:foo" });
        split(":", "boo:and:foo", 5, new String[]{ "boo", "and", "foo" });
        split(":", "boo:and:foo", -2, new String[]{ "boo", "and", "foo" });
        split("o", "boo:and:foo", 5, new String[]{ "b", "", ":and:f", "", "" });
        split("o", "boo:and:foo", -2, new String[]{ "b", "", ":and:f", "", "" });
        split("o", "boo:and:foo", 0, new String[]{ "b", "", ":and:foo" });
    }

    private void split(String regex, CharSequence input, int limit, String[] expect) {
        assertTrue(Arrays.deepEquals(Pattern.compile(regex).split(input, limit), expect));
    }
    
    private CharSet range(Integer... range) {
        CharSetBuilder builder = new CharSetBuilder();
        for (int i = 0; i < range.length; i += 2)
            builder.add(range[i], range[i + 1]);
        System.err.println(builder);
        return builder.build();
    }
}

