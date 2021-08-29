package me.sthat.benchmark;

import me.sthat.jjson.types.*;
import me.sthat.jjson.utils.JSON;

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
 * This is not part of the library
 * It is made just to test it
 *
 * @since 1.0.12
 * @author sthat
 */

public class Launcher {

    public static void printToFile(String file) {
        /* Create a new JSON and add some values to it */
        JSONObject json = new JSONObject();
        json
                .append("name", new JSONString("Alex"))
                .append("age", new JSONInteger(20))
                .append("friends", new JSONArray(
                        new JSONString("Lucas"),
                        new JSONString("John"),
                        new JSONString("Albert")
                ))
                .append("stats", new JSONObject()
                        .append("power", new JSONDouble(1.5))
                        .append("level", new JSONInteger(30))
                );

        /* Prints the document to System.out */
        json.print(4, 0);

        /* Prints the document to @file */
        try {
            JSON.stringify(json, file, 4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final String path = "path/to/the/document";

        /* Measures the time took */
        long start = System.nanoTime();

        /* Loads a document from a file */
        JSONIElement element = JSON.load(path, JSON.Source.FILEPATH);

        /* Converts ns to ms and prints the result */
        long end = System.nanoTime();
        long elap = end - start;
        System.out.println("Time elapsed: " + ((double) elap / 1e6) + "ms");


        /* Check type before casting */
        if (element instanceof JSONObject) {
            JSONIElement value = element.asObject().get("ground.3.0");
            if (value == null) {
                System.out.println("No value found");
                return;
            }
            if (value instanceof JSONString) {
                System.out.println("Value = " + value.asString());
            } else if (value instanceof JSONInteger) {
                System.out.println("Value = " + value.asLong());
            }
        }
    }
}
