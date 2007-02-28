package com.sixlegs.kleener;

import java.util.*;
import org.testng.annotations.*;
import static com.sixlegs.kleener.Expression.*;

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

    @Test public void testReplaceFirst() {
        Pattern p = Pattern.compile("dog");
        Matcher m = p.matcher("zzzdogzzzdogzzz");
        assert "zzzcatzzzdogzzz".equals(m.replaceFirst("cat"));
    }

    @Test public void testReplaceAll() {
        Pattern p = new DFA(paren(concat(repeat(literal("a"), 0, 0), literal("b")), 0), "a*b", 0);
        Matcher m = p.matcher("aabfooaabfooabfoob");
        assert "-foo-foo-foo-".equals(m.replaceAll("-"));
        assert "-foo-foo-foo-".equals(m.replaceAll("-")); // tests dfa caching
    }
}

