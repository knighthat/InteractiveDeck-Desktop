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

import java.io.*;

/*
 * This is how the code should be formatted:
 * - Wrap at 200th character
 * - Wrap on typing (optional)
 * - Use spaces as tab (fixed at four spaces per tab)
 *
 * - Class with extenstion and multiple interfaces
 *   must be kept on one line.
 */
@SuppressWarnings("all")
public class JavaCodeWrappingFormat extends Error implements Cloneable, Serializable {

    // START: Static fields/functions
    /*
     * Fields must have its name and value aligned to other fields.
     * Except for when it does not have modifier list.
     */
    static        boolean yes  = true;
    // END: Static fields/functions
    private final int     var2 = 0;
    String var1 = "";

    /*
     * - Opening & closing one the same line.
     * - Should have no space inside braces.
     */
    void emptyFunction() { }

    /*
     * - Any declaration with braces '{}' and does not contain
     *   any code should be groupped to one line.
     */
    void emptyDeclaration() {
        Runnable runnable = () -> { };

        Object obj = new Object() { };
    }

    /*
     * - Similar to function declaration. When a method required
     *   multiple parameters, consider placing each individual on
     *   separate line with closing parenthese on the next line.
     *
     * - When multiple methods are called one the same line.
     *   Consider chopping it down and align then with the first dot
     */
    void callLongMethod() {
        SubClass subClass = new SubClass();

        subClass.returnTheClass()
                .returnTheClass()
                .returnTheClass()
                .returnTheClass()
                .returnTheClass()
                .aVeryLongFunctionWithMultipleParametersThatCanExceedTheMaximumCharactersAllowedOnOneLineWhich(
                        10000000,
                        20000000,
                        30000000,
                        40000000,
                        50000000,
                        60000000,
                        70000000,
                        80000000,
                        90000000,
                        100000000,
                        110000000
                );
    }

    void commenting() {
        // Comment indicator (//, /**/) should be indented according to
        // the line it is placed. Ideally, to the line it is intended to
        // comment on.


        // This comment is for explaining the 'if' statement
        if ( true ) {
            // This comment is intended to explain the code below
            int i = 0;
        }
    }

    /*
     * Avoid one line statement because it makes the code hard to understand.
     */
    void oneLineStatment() {
        // Avoid 'if (condition) code'
        if ( true ) {
            return;
        }
    }

    /*
     * Any function, declaration, lambda function, class should have its
     * opening brace at the end of its declaration regardless the length.
     */
    void bracePlacements() {
        Runnable runnable = () -> { };

        Object obj = new Object() { };

        if ( true ) {
        }
    }

    /*
     * A function with multiple throws is required to keep everything on one line.
     * If there are more than five, consider making a more general exception class.
     */
    void functionWithThrowKeyword() throws IllegalArgumentException, IllegalStateException, InvalidClassException { }

    /*
     * This rule is not enfored, but take it into your consideration
     *
     * - One line statement is not required to have braces
     * - Multiple if-else statements if link together should have braces
     *   event when they only contain one line each.
     *
     */
    void multipleLinesIfElse() {
        int a = 1;

        if ( a < 1 )
            return;

        for ( int i = 0 ; i < a ; i++ )
            continue;

        while ( true )
            break;

        if ( a == 0 ) {
            return;
        } else if ( a == 1 ) {
            return;
        } else if ( a == 2 ) {
            return;
        }
    }

    /*
     * - Each case must be on a separted line.
     * - Except when they are grouped togther.
     * - 'case' must be aligned with other 'case' and 'default'
     */
    void switchCaseAlignment() {
        int a = 0;

        switch ( a ) {
            case 0 -> {
            }
            case 1 -> {
            }
            case 2 -> {
            }
            case 3 -> {
            }
            case 4 -> {
            }
            default -> {
            }
        }
    }

    /*
     * - Try-with-resource is required to have multiple line of resources
     *   if the number of resources is more than one.
     * - 'catch' and 'finally' are on the same line as closing parenthese.
     * - Multiple catch types must be on one line.
     */
    void tryCatchStatement() {
        File file = new File("afile.txt");

        try ( FileInputStream inStream = new FileInputStream(file) ) {

        } catch ( IOException | IllegalStateException | NumberFormatException ignored ) {
        } finally {
        }

        try (
                FileInputStream inStream = new FileInputStream(file) ;
                FileOutputStream fileOutStream = new FileOutputStream(file) ;
                ByteArrayOutputStream outStream = new ByteArrayOutputStream()
        ) {
            return;
        } catch ( IOException ignored ) {
        }
    }

    /*
     * If operator is long and has multiple groups,
     * it is allowed to have multiple line;
     *
     * Operator sign must be palced at the end of the line.
     */
    void binaryOperators() {
        int a = (1 + 2 + 3) +
                (4 + 5 + 6) +
                7 + 8 + 9 +
                0x0101001;

        int b = a <= 1 ?
                2 :
                3;
    }

    void oneLiner() { Class<Void> v = Void.TYPE; }

    /*
     * Enum class must have its values on each line instead of one line.
     */
    enum Day {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY;
    }

    /*
     * A class or method or field that has multiple annotation
     * must have only one annotation on one line.
     */
    @Deprecated
    @FunctionalInterface
    interface AnnotatedClass {

        @Deprecated
        @Contract
        void doSomething();
    }

    /*
     * Multiple parameters records should have each parameter
     * on each line with the closing parenthese on new line.
     */
    record RecordFile(
            String var1,
            int var2,
            boolean var3,
            long var4
    ) { }

    static class SubClass {
        /*
         * - Method with exceeded parameters should have each parameter
         *   on separate line.
         * - Closing parenthese should be on new line with opening brace.
         * - Closing parenthese must be aligned with the the of function declaration.
         */
        void aVeryLongFunctionWithMultipleParametersThatCanExceedTheMaximumCharactersAllowedOnOneLineWhich(
                int var1,
                int var2,
                int var3,
                int var4,
                int var5,
                int var6,
                int var7,
                int var8,
                int var9,
                int var10,
                int var11
        ) { }

        SubClass returnTheClass() { return this; }
    }
}
