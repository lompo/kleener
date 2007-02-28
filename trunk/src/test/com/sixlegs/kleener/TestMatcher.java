package com.sixlegs.kleener;

import java.util.*;
import org.testng.annotations.*;

public class TestMatcher
{
    @Test public void testAppendReplacement() {
        Pattern p = Pattern.compile("cat");
        Matcher m = p.matcher("one cat two cats in the yard");
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            m.appendReplacement(sb, "dog");
        }
        m.appendTail(sb);
        assert "one dog two dogs in the yard".equals(sb.toString());
    }
}

