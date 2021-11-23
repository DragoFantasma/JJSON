package me.sthat.jjson.types;

import java.io.FileWriter;
import java.io.IOException;

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
 * JSONIElement represents each element in the document, no matter the type.
 * This has no generic types, so it can be used without knowing what type it is holding.
 * @see me.sthat.benchmark.Launcher for some example
 *
 * @since 1.0.12
 * @author sthat
 */
public interface JSONIElement {

    void stringify(FileWriter writer, int indentation, int currIndentation) throws IOException;
    void print(int indentation, int currIndentation);

    @Deprecated
    String getType();
    JSONType getElementType();

    String asString();
    long asLong();
    double asDouble();
    boolean asBoolean();
    JSONArray asArray();
    JSONObject asObject();

    boolean isNull();

}
