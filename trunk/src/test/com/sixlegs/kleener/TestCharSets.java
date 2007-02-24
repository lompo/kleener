package com.sixlegs.kleener;

import org.testng.annotations.*;

public class TestCharSets
{
    @Test public void testMe() {
        System.err.println("hello world");
    }
    
    private CharSet range(Integer... range) {
        CharSetBuilder builder = new CharSetBuilder();
        for (int i = 0; i < range.length; i += 2)
            builder.add(range[i], range[i + 1]);
        System.err.println(builder);
        return builder.build();
    }
}

