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

import me.knighthat.interactivedeck.component.input.HexColorTextField;

// START: Subclass

/**
 * General rules:
 * - Static fields/methods/initializer block must be placed before non-static elements.<br>
 * - Order is based on the immunity. Meaning, mutable objects must be placed below immutable objects.<br>
 * - Separate fields from methods by initializer block and/or constructors.<br>
 * - Inner enums/interfaces/classes are located at the bottom with above rules applied.<br>
 * - Overridden methods must be kept in order.<br>
 */
@SuppressWarnings({ "unused", "InnerClassMayBeStatic", "EmptyClassInitializer" })
public abstract class Arrangement {

    // START: Static fields/functions
    /*
     * Static final fields
     */
    public static final    Object PUB_FIN_OBJ = new Object();
    protected static final Object PRO_FIN_OBJ = new Object();
    private static final   Object PRI_FIN_OBJ = new Object();
    static final           Object STA_FIN_OBJ = new Object();

    /*
     * Static non-final fields
     */
    public static    Object PUB_STA_OBJ;
    protected static Object PRO_STA_OBJ;
    private static   Object PRI_STA_OBJ;
    static           Object STA_OBJ;

    /*
     * Static initializer
     */
    static {
    }

    /**
     * Static methods
     */
    static void sta_me() { }

    private static void pri_sta_me() { }

    protected static void pro_sta_me() { }

    public static void pub_sta_me() { }
    // END: Static fields/functions

    /*
     * Final fields
     */
    public final    Object pub_fin_obj = new Object();
    protected final Object pro_fin_obj = new Object();
    private final   Object pri_fin_obj = new Object();
    final           Object fin_obj     = new Object();
    /*
     * Non-final fields
     */
    public          Object pub_obj;
    protected       Object pro_obj;
    private         Object pri_obj;
    Object obj;

    /*
     * Class constructor
     */
    private Arrangement( HexColorTextField textField ) { }

    public Arrangement() { }

    /*
     * Abstract methods
     */
    protected abstract void pri_abs_me();

    public abstract void pub_abs_me();

    /*
     * Methods
     */
    void me() { }

    private void pri_me() { }

    protected void pro_me() { }

    public void pub_me() { }

    /*
     * Overridden methods
     */
    @Override
    public int hashCode() { return super.hashCode(); }

    @Override
    public String toString() { return super.toString(); }

    /*
     * Enums
     */
    public enum PubEnum {}

    protected enum ProEnum {}

    private enum PriEnum {}

    enum Enum {}

    /*
     * Interfaces
     */
    public interface PubInter { }

    protected interface ProInter { }

    private interface PriInter { }

    interface Inter { }

    /*
     * Static classes
     */
    public static class PubStaClass { }

    protected static class ProStaClass { }

    private static class PriStaClass { }

    /*
     * Classes
     */
    public class PubClass { }

    protected class ProClass { }

    private class PriClass { }
}
