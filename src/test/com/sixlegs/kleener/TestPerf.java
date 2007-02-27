package com.sixlegs.kleener;

import static com.sixlegs.kleener.Expression.*;
import java.util.*;
import org.testng.annotations.*;

public class TestPerf
{
    @Test(groups = { "perf" })
    public void testComparePerformance() {
        // (<[^/]([^>]*[^/>])?>)|(</[^>]*>)|(<[^>]*/>)|((\p{Lu}|\p{Ll}|\p{Lt}|\p{Nd}|\p{Nl}|\p{No}|[\u4e00-\u9fa5]|\u3007|[\u3021-\u3029])((\p{Lu}|\p{Ll}|\p{Lt}|\p{Nd}|\p{Nl}|\p{No}|[-._:']|[\u4e00-\u9fa5]|\u3007|[\u3021-\u3029])*(\p{Lu}|\p{Ll}|\p{Lt}|\p{Nd}|\p{Nl}|\p{No}|[\u4e00-\u9fa5]|\u3007|[\u3021-\u3029]))?)

        CharSet c1 =
            UnionCharSet.union(Arrays.asList(UnicodeCategoryCharSet.get(Character.UPPERCASE_LETTER),
                                             UnicodeCategoryCharSet.get(Character.LOWERCASE_LETTER),
                                             UnicodeCategoryCharSet.get(Character.TITLECASE_LETTER),
                                             UnicodeCategoryCharSet.get(Character.DECIMAL_DIGIT_NUMBER),
                                             UnicodeCategoryCharSet.get(Character.LETTER_NUMBER),
                                             UnicodeCategoryCharSet.get(Character.OTHER_NUMBER),
                                             new CharSetBuilder().add(0x4300, 0x9fa5).add(0x3007).add(0x3021, 0x3029).build()));

        CharSet c2 =
            UnionCharSet.union(Arrays.asList(UnicodeCategoryCharSet.get(Character.UPPERCASE_LETTER),
                                             UnicodeCategoryCharSet.get(Character.LOWERCASE_LETTER),
                                             UnicodeCategoryCharSet.get(Character.TITLECASE_LETTER),
                                             UnicodeCategoryCharSet.get(Character.DECIMAL_DIGIT_NUMBER),
                                             UnicodeCategoryCharSet.get(Character.LETTER_NUMBER),
                                             UnicodeCategoryCharSet.get(Character.OTHER_NUMBER),
                                             new CharSetBuilder().add(0x4300, 0x9fa5).add(0x3007).add(0x3021, 0x3029).build(),
                                             new CharSetBuilder().add("-._:'").build()));

        // TODO: add rest of parens
        Expression e =
            paren(or(concat(literal("<"),
                            literal(new SingleCharSet('/'), true),
                            repeat(concat(repeat(literal(new SingleCharSet('>'), true), 0, 0),
                                          literal(new CharSetBuilder().add("/>").build(), true)), 0, 1),
                            literal(">")),
                     concat(literal("</"),
                            repeat(literal(new SingleCharSet('>'), true), 0, 0),
                            literal(">")),
                     concat(literal("<"),
                            repeat(literal(new SingleCharSet('>'), true), 0, 0),
                            literal("/>")),
                     concat(literal(c1),
                            repeat(concat(repeat(literal(c2), 0, 0),
                                          literal(c1)), 0, 1))), 0);

        Pattern p = e.compile(null, Pattern.CompileType.DFA);
        // TODO: run it!
    }
}
