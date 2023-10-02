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

package me.knighthat.interactivedeck.connection.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.knighthat.interactivedeck.file.Profile;
import me.knighthat.interactivedeck.file.Profiles;
import me.knighthat.interactivedeck.json.Json;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

class AddRequestTest {

    private static final @NotNull List<Profile> PROFILES = new ArrayList<>( 10 );

    private static Request request;

    @BeforeAll
    static void setUp() {
        for (int i = 0 ; i < 10 ; i++) {
            Profile profile = Profiles.create( "Profile No." + i );
            PROFILES.add( profile );
            println( "Profile " + profile.displayName() + " is created!" );
        }

        request = new AddRequest( pArray -> PROFILES.forEach( p -> pArray.add( p.serialize() ) ) );
        println( request.toString() );
    }

    private static void println( @NotNull Object obj ) {
        System.out.println( obj );
    }

    private static void printErr( @NotNull Exception e ) {
        if (e.getCause() == null)
            System.err.println( e.getMessage() );
        else
            System.err.println( e.getCause().getMessage() );
    }

    @Test
    void serialize() {
        JsonArray rawContent = request.content.getAsJsonArray();
        println( Json.toString( rawContent ) );
        JsonArray compressedContent = request.serialize().get( "content" ).getAsJsonArray();
        println( compressedContent.toString() );

        byte[] compressedBytes = new byte[compressedContent.size()];
        for (int i = 0 ; i < compressedBytes.length ; i++)
            compressedBytes[i] = compressedContent.get( i ).getAsByte();
        println( "Raw bytes count: " + Json.toString( rawContent ).getBytes().length );
        println( "Compressed bytes count: " + compressedBytes.length );
        println( "Compressed bytes:" + Arrays.toString( compressedBytes ) );

        byte[] inflated = inflate( compressedBytes );
        if (inflated.length == 0)
            Assertions.fail();
        println( "Inflated bytes: " + Arrays.toString( inflated ) );

        String inflatedData = new String( inflated );
        println( "Inflated data: " + inflatedData );
        JsonArray inflatedArray = JsonParser.parseString( inflatedData ).getAsJsonArray();
        println( "Inflated array: " + Json.toString( inflatedArray ) );

        Assertions.assertEquals( rawContent, inflatedArray );
    }

    private byte[] inflate( byte[] deflated ) {
        byte[] inflated = new byte[0];

        try (ByteArrayInputStream bais = new ByteArrayInputStream( deflated ) ;
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            GZIPInputStream gzip = new GZIPInputStream( bais );
            byte[] buffer = new byte[1024];
            int bytesRead;
            while (( bytesRead = gzip.read( buffer ) ) != -1)
                baos.write( buffer, 0, bytesRead );
            gzip.close();

            inflated = baos.toByteArray();
        } catch (IOException e) {
            printErr( e );
        }

        return inflated;
    }
}