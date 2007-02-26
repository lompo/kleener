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

    protected boolean updateMatch() {
        return matched = match[0] != null && match[0].sp >= 0;
    }
    
    public boolean matches() {
        reset();
        return matched = match(0) && end() == regionEnd;
    }

    public boolean find() {
        int p = matched ? match[0].ep : regionStart;
        // TODO
        return match(p);
    }

    public boolean find(int start) {
        reset();
        return find();
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
        requireMatch();
        return 0;
    }

    public int end() {
        requireMatch();
        return input.length();
    }

    public int start(int group) {
        requireMatch();
        return match[group].sp;
    }

    public int end(int group) {
        requireMatch();
        return match[group].ep;
    }

    abstract protected boolean match(int p);
}
