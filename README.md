<center>

![](./img/AmarokJsonForJava.png)
![](https://badgen.net/badge/Java/8/blue?icon=java)
[![](https://badgen.net/badge/License/MPL-2.0/yellow)](./LICENSE.txt)

</center>

# Amarok 's Json For Java （AJ4J）

[简体中文](README_CN.md)

## What is AJ4J?
Amarok's Json For Java is a Json parsing library written in Java, which can accurately process Json and internally infer the type obtained by Json.

Processon Json, Json5, and write to the files!

## Why AJ4J?
**Google Gson** ： Gson is great!, but Gson sometimes handles type inference incorrectly, and it's easy to get into the trouble of type agnosticism when you use JsonObject directly. Most of the time this is fine, but I am writing something based on Json parsing that I urgently need to know if the user has entered the correct type and pre-infer the type to throw the correct false positive. Constantly trying to check only increases the performance overhead of the runtime, giving the user the feeling that "this software is stuck", so I think type inference should be done at parsing time.

Doing so may make parsing slower, but it's equivalent. And Gson still doesn't seem to be able to handle Json5.

**Jackson** : Jackson is really my best bet! Not only is it fast, but it can also handle Json5! I also prioritize Jackson in addition to Gson when dealing with some personal projects.

But Jackson's size made my post-package program look bloated. In some of the software that is required to be lighter, I will try to consider other libraries.

**FoxSuma Json for Java**: I packaged it in my Minecraft Mod Lib because the library is cool (it supports Json5 processing!), but it smells a bit personal and just as easy to get into type-agnostic trouble as Gson. Consistently holding a JsonNode's data type is a better option than by internal inference (or relying on the brain).

## Let's go start

### Gradle get it

[Github Package](https://github.com/AmarokIce/AmarokJsonForJava/packages/1929112)

[Jitpack.io](https://jitpack.io/#AmarokIce/AmarokJsonForJava)

Gradle:
```groove
repositories {
    maven {
        url 'http://maven.snowlyicewolf.club/'
        allowInsecureProtocol = true
    }
}

dependencies {
    implementation "club.someoneice.json:amarok-json-for-java:1.4"
}
```

Gradle Kotlin:
```kotlin script
repositories {
    maven {
        url = uri("http://maven.snowlyicewolf.club")
        isAllowInsecureProtocol true
    }
}

dependencies {
    implementation("club.someoneice.json:amarok-json-for-java:1.6")
}
```

### From Json

Set up our Json File, Json String, and then instance the [JSON](src/main/java/club/someoneice/json/JSON.java). Any you can use Json or Json5, but don't use Json Handler to processor Json5.

```java
JSON json = JSON.json;      // For Json, Or
JSON json5 = JSON.json5;    // For Json or Json5
```

Then parse get JsonNode:
```java
// Processor to ArrayNode or MapNode which your Json started。
JsonNode node = json.parse(file);

// Parse to Array or Map (Maybe empty)
ArrayNode arrayNode = json.tryPullArrayOrEmpty(node);
MapNode mapNode = json.tryPullObjectOrEmpty(node);

```

> The default JsonNode type is: <br />
> Null, String, Int, Float, Double, Boolean, Map, Array, Number, Long, Other

JsonNode can fetch the internal content directly, or it can get the type through the internal inference of the node:
```java
JsonNode node = JsonNode.NULL;
System.out.println(node.getType());
// It a null node.
```

### To Json

To Json by method [JsonBuilder](src/main/java/club/someoneice/json/processor/JsonBuilder.java)#asString(JsonNode) :String ,then JsonNode will changed to Json String. And the method `prettyPrint` can make our Json String pretty.

[Json5Bean](src/main/java/club/someoneice/json/processor/Json5Builder.java) allow the use of stream input and annotation input, you need to implement Json5Builder first, and then obtain ArrayBean and MapBean to start importing the task stream.
```java
public class Main {
    public static void main(String[] args) {
        File file2 = new File("./NewJsonFile.json5");
        if (!file2.exists() || !file2.isFile()) file2.createNewFile();
        // Core of builder
        Json5Builder builder = new Json5Builder();
        
        // Array builder
        Json5Builder.ArrayBean arrayBean = builder.getArrayBean();
        
        // Map builder
        Json5Builder.ObjectBean mapBean = builder.getObjectBean();

        arrayBean.add(new StringNode("Test"));
        // Yes! Another line
        arrayBean.enterLine();
        arrayBean.add(new StringNode("This is another Test"));
        
        mapBean.put("test", new StringNode("Test"));
        // And you can make a comment for your Json5.
        mapBean.addNote("This is a test note");
        mapBean.put("newTest", new StringNode("This is another Test"));

        MapNode map = new MapNode();
        map.put("testInMap", new StringNode("mapTest"));
        // Build a HashMap into the Map Builder.
        mapBean.put("nodeMap", map);

        // And put the Map Builder into the Array Builder.
        arrayBean.addBean(mapBean);

        // Finally, push our Builder.
        builder.put(arrayBean);
        // Now! We can get our Json String.
        String data = builder.build();
        
        // Check it out.
        System.out.println(data);

        // Save the text to file.
        OutputStream outputStream = Files.newOutputStream(file2.toPath());
        outputStream.write(data.getBytes());
        outputStream.close();
    }
}
```

# Free Software!
Amarok's Json4J is a Free Software, as defined in the Pineapple License: if you want to sell the software you license, you should embed it in your labor, not use it directly. Even if this isn't defined as Free Software to some extent - the author doesn't care about the minutiae, if you insist on selling it.

You can freely fork or contribute to Amarok's Json4J or even provide a better name to get it up and running on the Community Relay Network faster!