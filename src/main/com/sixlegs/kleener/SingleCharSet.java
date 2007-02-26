package com.sixlegs.kleener;

final public class SingleCharSet extends CharSet
{
    private final int c;

    public SingleCharSet(int c) {
        this.c = c;
    }
    
    public boolean contains(int c) {
        return c == this.c;
    }

    public int nextChar(int c) {
        return (c <= this.c) ? this.c : -1;
    }
    
    public boolean isEmpty() {
        return false;
    }

    public int cardinality() {
        return 1;
    }

    public CharSet intersect(CharSet cset) {
        return cset.contains(c) ? this : EmptyCharSet.INSTANCE;
    }

    public CharSet subtract(CharSet cset) {
        return cset.contains(c) ? EmptyCharSet.INSTANCE : this;
    }
}
