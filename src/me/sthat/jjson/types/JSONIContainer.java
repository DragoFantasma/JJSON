package me.sthat.jjson.types;

import me.sthat.jjson.exceptions.JSONTypeMismatch;

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
 * JSONIContainer represents values that can hold other values, like arrays and objects.
 *
 * @since 1.0.12
 * @author sthat
 */
public interface JSONIContainer<T> extends JSONIValue<T> {

    long length();
    boolean isEmpty();
    void clear();

    default String asString() {
        throw new JSONTypeMismatch(getType() + " cannot be cast to String");
    }

    default long asLong() {
        throw new JSONTypeMismatch(getType() + " cannot be cast to Long");
    }

    default boolean asBoolean() {
        throw new JSONTypeMismatch(getType() + " cannot be cast to Boolean");
    }

    default double asDouble() {
        throw new JSONTypeMismatch(getType() + " cannot be cast to Double");
    }

    default JSONObject asObject() {
        if (!(this instanceof JSONObject)) {
            throw new JSONTypeMismatch(getType() + " cannot be cast to Object");
        }

        return (JSONObject)this;
    }

    default JSONArray asArray() {
        if (!(this instanceof JSONArray)) {
            throw new JSONTypeMismatch(getType() + " cannot be cast to Array");
        }

        return (JSONArray)this;
    }

    default boolean isNull() {
        return false;
    }
}
