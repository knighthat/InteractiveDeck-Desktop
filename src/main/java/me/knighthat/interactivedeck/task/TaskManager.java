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

package me.knighthat.interactivedeck.task;

import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.StringJoiner;

public class TaskManager {

    @Contract( "_, null -> null" )
    public static <T extends Task> @Nullable Task create( @NotNull Class<T> clazz, @Nullable Object... params ) {
        StringJoiner builder = new StringJoiner( ",", "[", "]" );

        Class<?>[] paramTypes = new Class[params.length];
        for (int i = 0 ; i < paramTypes.length ; i++) {
            if (params[i] == null)
                return null;

            paramTypes[i] = params[i].getClass();
            builder.add( paramTypes[i].getName() );
        }

        try {
            Log.deb( "Looking for a constructor requires %s in %s".formatted( builder, clazz.getName() ) );
            Constructor<T> constructor = clazz.getConstructor( paramTypes );

            return constructor.newInstance( params );
        } catch (InvocationTargetException e) {
            if (e.getCause() != null)
                Log.warn( e.getCause().getMessage() );
        } catch (Exception e) {
            Log.exc( "Failed to create a task with " + builder, e, true );
            if (e.getCause() != null)
                Log.err( e.getCause().getMessage() );
            Log.reportBug();
        }

        return null;
    }
}
