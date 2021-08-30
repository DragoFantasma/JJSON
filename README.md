# JJSON
Guides
* [Guides](https://github.com/DragoFantasma/JJSON#guides)
* [Code examples](https://github.com/DragoFantasma/JJSON#code-examples)


A simple library to load JSON document written in Java.<br>
I wrote this also to learn, so I don't say this is the best.<br>
I hope it could be useful to you!<br>

### Code examples
The class `Launcher` contains some tips to getting started with jsonreaser.<br>
Anyway, let's dive into some code.<br>

#Important
### Remember to check the type before casting
<br>

#### **Create and load a json document**
```java
public static void main(String... args) {
    String filepath = ...;
    
    /* You can specify if the first argument is the filepath,
     * or the json as string by changing the source
     */
    JSONIElement element = JSON.load(filepath, JSON.Source.FILEPATH);
    
    /* Check the type */
    if (element instanceof JSONObject) {
        JSONIElement value = element.asObject().get("name");
        if (value == null) {
            /* No value was found */
        } else {
            System.out.println(value.asString());
        }
    }
    
    
}
```


### Available type are
- JSONObject
- JSONArray
- JSONString
- JSONInteger
- JSONDouble
- JSONBoolean
- JSONNull

Each type has its own `as<Type>()` method (asString(), asArray(), asBoolean(), etc...)

### Prints the document to a file

```java
public static void main(String... args) {
    JSONIElement element = ...;
    
    /* The path of the file were to prints the output */
    String filepath = ...;
    
    /* The indentation to use. 0 to disable, -1 to prints to a single line */
    int indentation = 4;
    
    JSON.stringify(json, filepath, indentation);
}
```

### Builds a new json

In the example below, we create a JSONObject first.<br>
And then we append some value to it.<br>
As you can see, `append` returns the object itself, so you
can chain together as many calls as you want.<br>
`JSONArray` accepts elements directly into the constructor

```java
public static void main(String... args) {
        JSONObject json = new JSONObject();
        json.append("name", new JSONString("Alex"))
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
}
```


### Iterates through containers

Both `JSONArray` and `JSONObject` allows for-each loops.
```java
public static void main(String... args) {
    JSONArray array = ...;
    for (JSONIElement element : array) {
        /* Do stuff */
    }
    
    JSONObject object = ...;
    for (String key : object) {
        JSONIElement element = object.get(key);
        /* Do stuff */
    }
}
```

#### I guess that's it for now<br>Hope it's useful to you and happy coding!
