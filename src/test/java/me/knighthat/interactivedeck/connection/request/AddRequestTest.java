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
import me.knighthat.interactivedeck.logging.Log;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

class AddRequestTest {

    private static final @NotNull List<Profile> PROFILES = new ArrayList<>( 10 );

    private static Request request;

    @BeforeAll
    static void setUp() {
        for (int i = 0 ; i < 10 ; i++)
            PROFILES.add( Profiles.create( "Profile No." + i ) );

        request = new AddRequest( pArray -> PROFILES.forEach( p -> pArray.add( p.serialize() ) ) );
    }

    @Test
    void serialize() {
        JsonArray rawContent = request.content.getAsJsonArray();
        JsonArray compressedContent = request.serialize().get( "content" ).getAsJsonArray();

        byte[] compressedBytes = new byte[compressedContent.size()];
        for (int i = 0 ; i < compressedBytes.length ; i++)
            compressedBytes[i] = compressedContent.get( i ).getAsByte();

        byte[] inflated = inflate( compressedBytes );
        if (inflated.length == 0)
            Assertions.fail();

        String inflatedData = new String( inflated );
        JsonArray inflatedArray = JsonParser.parseString( inflatedData ).getAsJsonArray();

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
            Log.exc( "", e, true );
        }

        return inflated;
    }
}