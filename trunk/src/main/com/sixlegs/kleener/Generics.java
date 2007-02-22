package com.sixlegs.kleener;

import java.util.*;

public class Generics
{
    public static <T> List<T> newArrayList() { return new ArrayList<T>(); }
    public static <T> Set<T> newHashSet() { return new HashSet<T>(); }
    public static <T,S> Map<T,S> newHashMap() { return new HashMap<T,S>(); }
}

