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

public class JavadocFormat {

    /*
     * To make a perfect, east to read comment.
     * Here are some rules to follow:
     * - Empty lines are filled with '<p>'
     * - Description, parameters, return, throws must be
     *   separated by one empty line
     * - Parameter and return should have it definition
     */


    /**
     * This comment should describe what the block does,
     * please keep the comment short but concise.
     * <p>
     * Additionally, the method should only do one job.
     * Or a method that calls other methods to complete an algorithm.
     *
     * @param var1                   What is var1
     * @param var2                   What is var2
     * @param var3                   what is var3
     * @param aParameterWithLongName Other parameters' description should align to this description
     *
     * @return What caller should expect to get after the method is done processing
     *
     * @throws IllegalArgumentException why is this exception thrown?
     */
    String aBlockThatHasComment( String var1, int var2, boolean var3, float aParameterWithLongName ) throws IllegalArgumentException {
        return "";
    }
}
