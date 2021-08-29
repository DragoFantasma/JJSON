package me.sthat.jjson.utils;

/*
 * MIT License
 *
 * Copyright (c) 2021 sthat
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


/**
 * JSONSourceString manages the string to read from
 *
 * @since 1.0.12
 * @author sthat
 */
public class JSONSourceString {

    String buffer;
    int offset = 0, virOffset = -1;

    public JSONSourceString(String buffer) {
        this.buffer = buffer;
    }

    public char nextCharVir() {
        if (virOffset == -1) {
            virOffset = offset;
        }

        if (virOffset >= buffer.length()) {
            return 0;
        }

        return buffer.charAt(virOffset++);
    }

    public void disableVirOffset() {
        virOffset = -1;
    }

    public void resetVirOffset() {
        virOffset = offset;
    }

    public boolean assertNextChar(char c) {
        return nextChar() == c;
    }

    public char getChar() {
        if (end()) {
            return 0;
        }
        return buffer.charAt(offset);
    }

    public char getVir() {
        if (end()) {
            return 0;
        }
        return buffer.charAt(virOffset);
    }

    public char nextChar() {
        if (end()) {
            return 0;
        }
        return buffer.charAt(offset++);
    }

    public void next() {
        ++offset;
    }

    public void nextVir() {
        ++virOffset;
    }

    public boolean end() {
        return offset >= buffer.length();
    }


}