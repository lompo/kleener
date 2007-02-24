package com.sixlegs.kleener;

final public class EmptyCharSet extends CharSet
{
    public static final EmptyCharSet INSTANCE = new EmptyCharSet();

    public boolean contains(int c) {
        return false;
    }

    public int nextChar(int c) {
        return -1;
    }

    @Override public boolean isEmpty() {
        return true;
    }

    public CharSet intersect(CharSet cset) {
        return this;
    }

    public CharSet subtract(CharSet cset) {
        return this;
    }
}
