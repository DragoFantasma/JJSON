package me.sthat.jjson.utils;

import me.sthat.jjson.exceptions.JSONBadFormat;
import me.sthat.jjson.types.*;

import java.io.*;

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
 * JSON is the main class that handles each I/O operation
 *
 * @since 1.0.12
 * @author sthat
 */

public class JSON {

    /**
     * Loads a JSON file
     * You can load from a buffer or a file, specifying the path
     *
     * @param string
     *              The json source.
     *              @see Source
     * @param source Tells how to use @string.<br>
     *               If @source is set to <b>FILEPATH</b>, it will open the file at @string and load the document from its content.<br>
     *               If @source is set to <b>BUFFER</b>, the json will be loaded reading directly from @string
     * @return
     *              The JSON document just loaded.<br>
     *              It may an instance of <b>JSONObject</b>, <b>JSONArray</b>, <b>JSONBoolean</b>, <b>JSONInteger</b>, <b>JSONDouble</b>, <b>JSONNull</b> or <b>JSONString</b>.<br>
     *              It's always better to check the type before using the value.
     */
    public static JSONIElement load(String string, Source source) {
        String jsonString;
        if (source.equals(Source.FILEPATH)) {
            try {
                jsonString = loadFileTo(string);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else if (source.equals(Source.BUFFER)) {
            jsonString = string;
        } else {
            throw new IllegalArgumentException("No valid source were specified to load " + string);
        }

        return jsonString != null ? JSONParser.jsonFromString(jsonString) : null;
    }

    /**
     * Prints a JSON to a file
     *
     * @param element
     *              The object to print.<br>
     *              If an instance of <b>JSONArray</b> or <b>JSONObject</b> is passed, each element is printed recursively.
     * @param filepath
     *              The path of the file were to print.
     *              If it doesn't exist, it gets created first.
     * @param indentation
     *              The indentation to use.<br>
     *              - Set 0 to disable.<br>
     *              - Set -1 to print each element to a single line.
     * @throws IOException
     *              If the file couldn't be created
     */
    public static void stringify(JSONIElement element, String filepath, int indentation) throws IOException {
        File file = new File(filepath);
        if (!file.exists() && !file.createNewFile())
            throw new IOException("Cannot create file " + filepath);

        FileWriter writer = new FileWriter(file);
        element.stringify(writer, indentation, 0);
        writer.close();
    }

    private static String loadFileTo(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));

        boolean isString = false;
        while (reader.ready()) {
            char c = (char) reader.read();
            if (c == '"') {
                isString = !isString;
            }
            if ((c != ' ' && c != '\t' && c != '\n') || isString)
                builder.append(c);
        }

        reader.close();

        if (isString) {
            throw new JSONBadFormat("Missing closing quote");
        }

        return builder.toString();
    }


    public enum Source {
        FILEPATH,
        BUFFER
    }

}
