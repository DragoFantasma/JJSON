package me.sthat.jjson.utils;

import me.sthat.jjson.exceptions.JSONUnexpectedToken;
import me.sthat.jjson.types.*;

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
 * JSONParser parses the document to a JSON type
 *
 * @since 1.0.12
 * @author sthat
 */
public class JSONParser {

    public static JSONIElement jsonFromString(String buffer) {
        return jsonParseNextItem(new JSONSourceString(buffer));
    }


    public static JSONIElement jsonParseNextItem(JSONSourceString source) {
        switch (source.getChar()) {
            case '{': {
                return jsonParseObject(source);
            }
            case '[': {
                return jsonParseArray(source);
            }
            case '"': {
                return jsonParseString(source);
            }
            case 't':
            case 'f': {
                return jsonParseBoolean(source);
            }
            case 'n': {
                return jsonParseNull(source);
            }
            default: {
                char c;
                source.resetVirOffset();
                boolean dotSeen = false;
                boolean isNegative = false;
                boolean signSeen = false;

                /*
                 * Let Integer.parseInt and Double.parseDouble handles scientific notation and hex numbers
                 * Just find out if this is an integer or a double
                 */

                c = source.nextCharVir();
                if (c != '0' || source.nextCharVir() != 'x') {
                    /*
                     * If the number starts with '0x', exclude the prefix from being parsed
                     * Handling 'x' in the loop below would result in an exception since it not a valid hex digit
                     */
                    source.resetVirOffset();
                }

                while ((c = source.getVir()) != ',' && c != ']' && c != '}') {
                    if (c == '.') {
                        if (dotSeen) {
                            throw new JSONUnexpectedToken("Double dot in a number is not allowed");
                        }
                        dotSeen = true;
                    }
                    else if (c == '-') {
                        if (isNegative) {
                            throw new JSONUnexpectedToken("Found double '-' while expecting a number");
                        }
                        isNegative = true;
                        signSeen = true;
                    }
                    else if (c == '+') {
                        if (signSeen) {
                            throw new JSONUnexpectedToken("Found double '+' while expecting a number");
                        }

                        signSeen = true;
                    }
                    else if (!isHexDigit(c)) {
                        throw new JSONUnexpectedToken("Character " + c + " is not a valid decimal or hex digit");
                    }
                    if (!source.end()) {
                        source.nextVir();
                    }
                }

                source.disableVirOffset();
                return dotSeen ? jsonParseDouble(source) : jsonParseInteger(source);
            }
        }
    }

    public static JSONObject jsonParseObject(JSONSourceString source) {
        if (!source.assertNextCharIs('{')) {
            throw new JSONUnexpectedToken("JSON Object must starts with a '{'.");
        }

        JSONObject object = new JSONObject();

        /* Looking for empty objects */
        if (source.getChar() == '}') {
            return object;
        }

        do {
            if (source.getChar() != '"') {
                throw new JSONUnexpectedToken("Expected '\"', found " + source.getChar() + " instead.");
            }
            JSONString key = jsonParseString(source);

            if (source.getChar() != ':') {
                throw new JSONUnexpectedToken("Expected ':' after key declaration, found " + source.getChar() + " instead");
            }

            source.next();
            JSONIElement element = jsonParseNextItem(source);
            object.append(key.getValue(), element);

            if (source.getChar() != ',') {
                break;
            }

            source.next();
        } while (!source.end() && source.getChar() == '"');

        if (source.getChar() != '}') {
            throw new JSONUnexpectedToken("Missing closing bracket or comma after value declaration, found " + source.getChar() + " instead. Missing quote?");
        }

        source.next();

        return object;
    }

    public static JSONArray jsonParseArray(JSONSourceString source) {
        if (!source.assertNextCharIs('[')) {
            throw new JSONUnexpectedToken("JSON Object must starts with a '{'.");
        }

        JSONArray array = new JSONArray();

        if (source.getChar() == ']') {
            source.next();
            return array;
        }

        char ch = source.getChar();
        do {
            if (ch == ',' && source.getChar() == ']') {
                throw new JSONUnexpectedToken("Unexpected comma before ']'.");
            }
            JSONIElement element = jsonParseNextItem(source);
            array.append(element);
        } while ((ch = source.nextChar()) == ',');

        if (ch != ']') {
            System.out.println("Found char " + ch);
            throw new JSONUnexpectedToken("Missing closing square bracket.");
        }

        return array;
    }

    public static JSONString jsonParseString(JSONSourceString source) {
        if (!source.assertNextCharIs('"')) {
            throw new JSONUnexpectedToken("JSON Object must starts with a '{'.");
        }

        StringBuilder builder = new StringBuilder();
        char c;
        boolean escape = false;
        while ((c = source.nextChar()) != '"' || escape) {
            if (c == '\\') {
                escape = !escape;
            }
            builder.append(c);
        }


        return new JSONString(builder.toString());
    }

    public static JSONDouble jsonParseDouble(JSONSourceString source) {
        StringBuilder builder = new StringBuilder();
        char c;
        while ((c=source.getChar()) != ',' && c != '}' && c != ']') {
            builder.append(c);
            source.next();
        }

        return new JSONDouble(Double.parseDouble(builder.toString()));
    }

    public static JSONInteger jsonParseInteger(JSONSourceString source) {
        StringBuilder builder = new StringBuilder();
        char c;
        while ((c=source.getChar()) != ',' && c != '}' && c != ']') {
            builder.append(c);
            source.next();
        }

        return new JSONInteger(Integer.parseInt(builder.toString()));
    }

    public static JSONBoolean jsonParseBoolean(JSONSourceString source) {
        StringBuilder builder = new StringBuilder();
        char c;
        while ((c=source.getChar()) != ',' && c != '}' && c != ']') {
            builder.append(c);
            source.next();
        }

        return new JSONBoolean(Boolean.parseBoolean(builder.toString()));
    }

    public static JSONNull jsonParseNull(JSONSourceString source) {
        StringBuilder builder = new StringBuilder();
        char c;
        while ((c=source.getChar()) != ',' && c != '}' && c != ']') {
            builder.append(c);
            source.next();
        }

        String str = builder.toString();

        if (!str.equals("null"))
            throw new JSONUnexpectedToken("Invalid token \"" + str + "\".");

        return new JSONNull();
    }

    public static boolean isHexDigit(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }
}
