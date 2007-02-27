package com.sixlegs.kleener;

import java.lang.reflect.Method;

abstract class JavaCharSet extends GenericCharSet
{
    public static final CharSet ISOControl =
      new CharSetBuilder().add('\u0000', '\u001F').add('\u007F', '\u009F').build();

    // cardinality/last data are valid for Unicode 4.0
    public static final CharSet Digit = new ReflectiveJavaCharSet("Digit", 268, 120831);
    public static final CharSet JavaIdentifierPart = new ReflectiveJavaCharSet("JavaIdentifierPart", 92040, 917999);
    public static final CharSet JavaIdentifierStart = new ReflectiveJavaCharSet("JavaIdentifierStart", 90648, 195101);
    public static final CharSet IdentifierIgnorable = new ReflectiveJavaCharSet("IdentifierIgnorable", 193, 917631);
    public static final CharSet Letter = new ReflectiveJavaCharSet("Letter", 90547, 195101);
    public static final CharSet LetterOrDigit = new ReflectiveJavaCharSet("LetterOrDigit", 90815, 195101);
    public static final CharSet LowerCase = new ReflectiveJavaCharSet("LowerCase", 1415, 120777);
    public static final CharSet Mirrored = new ReflectiveJavaCharSet("Mirrored", 492, 65379);
    public static final CharSet SpaceChar = new ReflectiveJavaCharSet("SpaceChar", 21, 12288);
    public static final CharSet TitleCase = new ReflectiveJavaCharSet("TitleCase", 31, 8188);
    public static final CharSet UnicodeIdentifierPart = new ReflectiveJavaCharSet("UnicodeIdentifierPart", 92004, 917999);
    public static final CharSet UnicodeIdentifierStart = new ReflectiveJavaCharSet("UnicodeIdentifierStart", 90600, 195101);
    public static final CharSet UpperCase = new ReflectiveJavaCharSet("UpperCase", 1190, 120744);
    public static final CharSet Whitespace = new ReflectiveJavaCharSet("Whitespace", 27, 12288);

    // TODO: provide non-reflective versions for commonly used classes
//     public static final CharSet Digit = new JavaCharSet() {
//         public boolean contains(int c) { return Character.isDigit(c); }
//     };

    private final int last;
    private final int cardinality;

    private JavaCharSet(int cardinality, int last) {
        this.cardinality = cardinality;
        this.last = last;
    }

    @Override public int cardinality() {
        return cardinality;
    }

    public int nextChar(int c) {
        for (int i = c; i <= last; i++)
            if (contains(i))
                return i;
        return -1;
    }

    private static class ReflectiveJavaCharSet extends JavaCharSet
    {
        private final Method m;
        
        public ReflectiveJavaCharSet(String name, int cardinality, int last) {
            super(cardinality, last);
            try {
                m = Character.class.getDeclaredMethod("is" + name, int.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean contains(int c) {
            try {
                return (Boolean)m.invoke(null, c);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
