package me.sthat.jjson.types;

import me.sthat.jjson.exceptions.JSONInvalidPath;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

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
 * JSONString holds a map of String-JSONIElement
 *
 * @since 1.0.12
 * @author sthat
 */
public class JSONObject implements JSONIContainer<Map<String, JSONIElement>>, Iterable<String> {

    private final Map<String, JSONIElement> map;

    public JSONObject() {
         this.map = new HashMap<>();
    }

    public JSONObject(Map<String, JSONIElement> map) {
        this.map = map;
    }

    public JSONObject append(String key, JSONIElement element) {
        map.put(key, element);
        return this;
    }

    public JSONObject append(String key, String value) {
        map.put(key, new JSONString(value));
        return this;
    }

    public JSONObject append(String key, double value) {
        map.put(key, new JSONDouble(value));
        return this;
    }

    public JSONObject append(String key, int value) {
        map.put(key, new JSONInteger(value));
        return this;
    }

    public JSONObject append(String key, boolean value) {
        map.put(key, new JSONBoolean(value));
        return this;
    }

    public JSONObject appendNull(String key) {
        map.put(key, new JSONNull());
        return this;
    }

    public JSONObject append(String key, JSONIElement... elements) {
        map.put(key, new JSONArray(elements));
        return this;
    }

    public JSONObject append(String key, Map<String, JSONIElement> map) {
        map.put(key, new JSONObject(map));
        return this;
    }

    public long length() {
        return map.size();
    }

    public boolean isEmpty() {
        return length() == 0;
    }

    public void clear() {
        map.clear();
    }

    public Map<String, JSONIElement> getValue() {
        return map;
    }

    /**
     * Get a key of an object.
     * You can also specify a path.
     * Example:
     *      "world.entities.2.name" <br>
     *      Will assume "world" is a key inside the current object, and entities an array inside the object<br>
     *      pointed to by "world". "2" is the index to look up and "name" is the property to read<br>
     *      Please remember that indexes goes from 0 to N-1.
     * @param key
     *      The path to retrieve
     * @return
     *      The element at the specified path or null if it couldn't be found.
     */
    public JSONIElement get(String key) {
        String[] path = key.split("\\.");
        JSONIElement element = this;
        for (String str : path) {
            if (element instanceof JSONObject) {
                element = ((JSONObject)element).map.get(str);
            } else if (element instanceof JSONArray) {
                element = ((JSONArray)element).get(Integer.parseInt(str));
            } else {
                throw new JSONInvalidPath("Cannot get property " + str + " of " + element.getType());
            }

            if (element == null) {
                return null;
            }
        }

        return element;
    }

    public void stringify(FileWriter writer, int indentation, int currIndentation) throws IOException {
        writer.write('{');
        if (indentation >= 0)
            writer.write('\n');
        AtomicLong index = new AtomicLong(0);
        for (String key : map.keySet()) {
            for (int i = 0; i < indentation * (currIndentation + 1); ++i) {
                writer.write(' ');
            }
            writer.write("\"" + key + "\": ");
            map.get(key).stringify(writer, indentation, currIndentation + 1);
            if (index.incrementAndGet() < map.size()) {
                writer.write(',');
            }
            if (indentation >= 0)
                writer.write('\n');
        }
        for (int i = 0; i < indentation * currIndentation; ++i) {
            writer.write(' ');
        }
        writer.write('}');
    }

    public void print(int indentation, int currIndentation) {
        System.out.print("{");
        if (indentation >= 0)
            System.out.println();
        AtomicLong index = new AtomicLong(0);
        for (String key : map.keySet()) {
            for (int i = 0; i < indentation * (currIndentation + 1); ++i) {
                System.out.print(" ");
            }
            System.out.print("\"" + key + "\": ");
            map.get(key).print(indentation, currIndentation + 1);
            if (index.incrementAndGet() < map.size()) {
                System.out.print(",");
            }
            if (indentation >= 0)
                System.out.println();
        }
        for (int i = 0; i < indentation * currIndentation; ++i) {
            System.out.print(" ");
        }
        System.out.print("}");
    }

    public Iterator<String> iterator() {
        return map.keySet().iterator();
    }

    public String getType() {
        return "Object";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JSONObject other = (JSONObject) o;
        return map.equals(other.map);
    }

    public int hashCode() {
        return Objects.hash(map);
    }
}
