package com.sixlegs.kleener;

import java.util.regex.MatchResult;

abstract public class Matcher implements MatchResult
{
    protected final PatternHelper pattern;
    protected final Sub[] match;

    protected CharSequence input;
    protected int regionStart;
    protected int regionEnd;
    protected int appendPosition;
    protected boolean matched;

    public Matcher(PatternHelper pattern) {
        this.pattern = pattern;
        match = new Sub[pattern.parenCount];
    }

    public Matcher reset() {
        regionStart = 0;
        regionEnd = input.length();
        appendPosition = 0;
        matched = false;
        return this;
    }

    public Matcher reset(CharSequence input) {
        this.input = input;
        return reset();
    }

    public boolean matches() {
        reset();
        matched = matchHelper(0) && end() == regionEnd;
        return matched;
    }

    public boolean find() {
        return matchHelper(matched ? match[0].ep : regionStart);
    }

    public boolean find(int start) {
        reset();
        return matchHelper(start);
    }

    private boolean matchHelper(int p) {
        matched = false;
        java.util.Arrays.fill(match, null);
        match(p);
        matched = match[0] != null && match[0].sp >= 0;
        return matched;
    }
    
    private void requireMatch() {
        if (!matched)
            throw new IllegalStateException("no current match");
    }

    public String group() {
        requireMatch();
        return group(0);
    }

    public int groupCount() {
        requireMatch();
        return match.length - 1;
    }

    public String group(int group) {
        requireMatch();
        return input.subSequence(match[group].sp, match[group].ep).toString();
    }

    public int start() {
        return start(0);
    }

    public int end() {
        return end(0);
    }

    public int start(int group) {
        requireMatch();
        return match[group].sp;
    }

    public int end(int group) {
        requireMatch();
        return match[group].ep;
    }

    abstract protected void match(int p);
}
