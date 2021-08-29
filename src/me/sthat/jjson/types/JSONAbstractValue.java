package me.sthat.jjson.types;

import me.sthat.jjson.exceptions.JSONTypeMismatch;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

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
 * JSONAbstractValue is the base class of each value type.
 * It defines some method to be used by his subclasses.
 *
 * @since 1.0.12
 * @author sthat
 */

public abstract class JSONAbstractValue<T> implements JSONIValue<T> {

    private final T value;

    public JSONAbstractValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void print(int indentation, int currIndentation) {
        System.out.print(this);
    }

    public void stringify(FileWriter writer, int indentation, int currIndentation) throws IOException {
        writer.write(toString());
    }

    public String toString() {
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        return String.valueOf(value);
    }

    public long asLong() {
        if (!(this instanceof JSONInteger)) {
            throw new JSONTypeMismatch(getType() + " cannot be cast to long");
        }
        return ((JSONInteger)this).getValue();
    }

    public String asString() {
        if (!(this instanceof JSONString)) {
            throw new JSONTypeMismatch(getType() + " cannot be cast to String");
        }

        return ((JSONString)this).getValue();
    }

    public boolean asBoolean() {
        if (!(this instanceof JSONBoolean)) {
            throw new JSONTypeMismatch(getType() + " cannot be cast to boolean");
        }

        return ((JSONBoolean)this).getValue();
    }

    public double asDouble() {
        if (!(this instanceof JSONDouble)) {
            throw new JSONTypeMismatch(getType() + " cannot be cast to double");
        }

        return ((JSONDouble)this).getValue();
    }

    public JSONArray asArray() {
        return null;
    }

    public JSONObject asObject() {
        return null;
    }

    public boolean isNull() {
        return this instanceof JSONNull;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JSONAbstractValue<?> other = (JSONAbstractValue<?>) o;
        return value.equals(other.value);
    }

    public int hashCode() {
        return Objects.hash(value);
    }
}
