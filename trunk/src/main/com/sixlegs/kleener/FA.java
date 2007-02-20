package com.sixlegs.kleener;

import java.util.*;
import java.lang.reflect.*;

abstract public class FA
{
    private final State start;

    protected FA(State start) {
        this.start = start;
    }

    State getStart() {
        return start;
    }    
}



