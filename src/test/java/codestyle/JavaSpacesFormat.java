/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package codestyle;

import org.jetbrains.annotations.Contract;

import java.util.function.Function;

@SuppressWarnings("all")
public class JavaSpacesFormat {

    private final String var1 = "";

    /*
     * Rules on functions decorations:
     * - No space before opening parentheses.
     * - Space inside opening & closing parentheses
     * - No space before coma (if applicable).
     * - Space after coma (if applicable).
     * - Space before left brace.
     */
    void functionDecoration( String var1, int var2, boolean var3 ) {}

    /*
     * Rules on statements decorations:
     * - Space before opening parentheses.
     * - Space inside opening & closing parentheses
     * - For loop:
     *   * Spaces around semicolon (if applicable).
     *   * Spaces around colon (if applicable).
     * - Space before left brace (if applicable).
     * - Space before keywords: else, while, catch, finally
     * - Switch-case:
     *   * No space after deconstruction (highly discourage to use this)
     *   * No space inside deconstruction's parentheses
     */
    void statementDecoration() {
        int aNumber = 10;

        if ( false )
            System.out.println();

        for ( int i = 0 ; i < aNumber ; i++ ) {
        }

        while ( true ) {
            break;
        }

        switch ( aNumber ) {
            case 1 -> System.out.println(1);
            case 2 -> System.out.println(2);
            case 10 -> System.out.println(10);
            default -> throw new IllegalStateException("Unexpected value: " + aNumber);
        }

        try {
            Float intToFloat = (float) aNumber;
        } catch ( NumberFormatException ignored ) {
        } finally {
        }

        synchronized ( var1 ) {
            // Execute some code
        }
    }

    /*
     * Rules on operators' decorations:
     * - TL;DR spaces before & after the operator, that includes:
     *   * Assignment:             =, +=, -=, *=, /=, ^=
     *   * Additive:               +, -;
     *   * Multiplicative:         *, /, %
     *   * Bitwise:                &, |, ^
     *   * Logical:                &&, ||
     *   * Bit shifting:           <<, >>, >>>
     *   * Equality:               ==, !=
     *   * Relational:             <, >, <=, >=
     *   * Lambda:                 ->
     * - Exceptions:
     *   * Unary:                  !, +, -, ++, --
     *   * Method reference colon: ::
     */
    void operatorDecoration() {
        int y = 1;

        y += y;
        y -= 1;
        y *= 2;
        y /= 2;
        y ^= 0x123;

        y = y + 1;
        y = y - 1;

        y = y * 2;
        y = y / 2;
        y = y % 3;

        int AND = y & 2;
        int OR = y | 2;
        int XOR = y ^ 3;

        int left = y << 2;
        int right = y >> 1;
        int u_right = y >>> 1;

        if ( true ) {
        }

        if ( y == 1 || y != 100 ) {
        }

        if ( y > 0 && y <= 10 ) {
        }

        Runnable runnable = () -> {};
    }

    /*
     * Rules on annotation' decorations:
     * - No space before & after opening parentheses.
     * - No space before closing parentheses.
     * - Space inside braces (if applicable).
     * - No space inside parentheses.
     * - Spaae around '=' in value pair
     */
    @SuppressWarnings({ "unchecked", "unused" })
    @Contract(pure = true)
    @Deprecated
    void annotationDecoration() {
    }

    /*
     * - Array:
     *   * No space inside brackets
     *   * Space inside curly braces
     *   * Space before opening curly brackets
     *   * No space before coma
     *   * Space after coma
     *   * No space in empty braces
     *   * No space inside parenthese of a groupped operations
     * - No space in empty parentheses.
     * - No space in method call.
     * - No space in empty method call.
     * - No space inside type cast parentheses.
     * - Space after type cast closing parentheses.
     * - No space inside angle brackets
     * - Spaces around question mark (?).
     * - Spaces around colon (:).
     */
    void inCodeDecoration() {
        int[] intArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        int fistE = intArray[0];

        int[] empty = {};

        double grouping = (double) 1 + 2 * (3 - 4);

        Runnable runnable = () -> {
        };

        System.out.println("No space before/after quote");
        System.out.println();

        Function<Object, Object> function = obj -> obj;

        int aNumber = 1 < 2 ? 3 : 4;
    }

    /*
     * Rules on classes' decoration:
     * - Space before left brace
     */
    private static class ClassDecoration {}

    /*
     * Rules on records' decoration:
     * - Space inside parentheses
     */
    private record Record( String var1, String var2 ) {}
}
