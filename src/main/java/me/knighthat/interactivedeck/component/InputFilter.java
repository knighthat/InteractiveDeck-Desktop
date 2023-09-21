/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.component;

import org.jetbrains.annotations.NotNull;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public abstract class InputFilter extends DocumentFilter {

    protected abstract boolean test( @NotNull CharSequence chars );

    private StringBuilder getStringBuilder( @NotNull FilterBypass fb ) throws BadLocationException {
        Document document = fb.getDocument();
        StringBuilder builder = new StringBuilder();
        builder.append( document.getText( 0, document.getLength() ) );
        return builder;
    }

    @Override
    public void insertString( FilterBypass fb, int offset, String string, AttributeSet attr ) throws BadLocationException {
        StringBuilder builder = getStringBuilder( fb );
        builder.insert( offset, string );
        if (test( builder ))
            super.insertString( fb, offset, string, attr );
    }

    @Override
    public void replace( FilterBypass fb, int offset, int length, String text, AttributeSet attrs ) throws BadLocationException {
        StringBuilder builder = getStringBuilder( fb );
        builder.replace( offset, offset + length, text );
        if (test( builder ))
            super.replace( fb, offset, length, text, attrs );
    }
}
