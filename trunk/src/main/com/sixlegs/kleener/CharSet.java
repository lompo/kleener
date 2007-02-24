package com.sixlegs.kleener;

// immutable
abstract public class CharSet
{
    abstract public boolean contains(char c);

    // TODO: use iterator instead? (better for charset builder)
    abstract public int nextChar(int c);

    abstract public CharSet intersect(CharSet cset);
    abstract public CharSet subtract(CharSet cset);

    public boolean isEmpty() {
        return nextChar(0) < 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int c = nextChar(0); c >= 0; c = nextChar(c + 1))
            sb.append((char)c);
        return Misc.escapeStringLiteral(sb.toString());
    }
}
