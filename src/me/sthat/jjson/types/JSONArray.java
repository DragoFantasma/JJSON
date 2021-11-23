package me.sthat.jjson.types;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Stream;

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
 * JSONArray holds a list of JSONIElement
 *
 * @since 1.0.12
 * @author sthat
 */
public class JSONArray implements JSONIContainer<List<JSONIElement>>, Iterable<JSONIElement> {

    private final List<JSONIElement> list = new ArrayList<>();

    public JSONArray(JSONIElement... elements) {
        list.addAll(Arrays.asList(elements));
    }

    public JSONArray append(JSONIElement element) {
        list.add(element);
        return this;
    }

    public JSONArray append(JSONIElement... elements) {
        list.addAll(Arrays.asList(elements));
        return this;
    }

    public long size() {
        return list.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void clear() {
        list.clear();
    }

    public List<JSONIElement> getValue() {
        return list;
    }

    public JSONIElement get(int index) {
        return list.get(index);
    }

    public void stringify(FileWriter writer, int indentation, int currIndentation) throws IOException {
        writer.write('[');
        if (indentation >= 0)
            writer.write('\n');
        AtomicLong index = new AtomicLong();
        for (JSONIElement element : list) {
            for (int i = 0; i < indentation * (currIndentation + 1); ++i) {
                writer.write(' ');
            }
            element.stringify(writer, indentation, currIndentation + 1);
            if (index.incrementAndGet() < list.size()) {
                writer.write(',');
            }
            if (indentation >= 0)
                writer.write('\n');
        }
        for (int i = 0; i < indentation * currIndentation; ++i) {
            writer.write(' ');
        }
        writer.write(']');
    }

    public void print(int indentation, int currIndentation) {
        System.out.print("[");
        if (indentation >= 0)
            System.out.println();
        AtomicLong index = new AtomicLong();
        list.forEach(e -> {
            for (int i = 0; i < indentation * (currIndentation + 1); ++i) {
                System.out.print(" ");
            }
            e.print(indentation, currIndentation + 1);
            if (index.incrementAndGet() < list.size()) {
                System.out.print(",");
            }
            if (indentation >= 0)
                System.out.println();
        });
        for (int i = 0; i < indentation * currIndentation; ++i) {
            System.out.print(" ");
        }
        System.out.print("]");
    }

    public Iterator<JSONIElement> iterator() {
        return list.iterator();
    }

    public Stream<JSONIElement> stream() {
        return list.stream();
    }

    public String getType() {
        return "Array";
    }

    public String toString() {
        return String.valueOf(list);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JSONArray other = (JSONArray) o;
        return list.equals(other.list);
    }

    public JSONType getElementType() {
        return JSONType.ARRAY;
    }

    public int hashCode() {
        return Objects.hash(list);
    }
}
