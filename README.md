# osuDbParser

A silly attempt to make a thing that can read the `osu!.db` file.

I used https://osu.ppy.sh/wiki/Db_(file_format)#osu.21.db, which is unfortunately not always correct.... Tried to do my best to be creative and make it work. It works on my 90gb of beatmaps, so I'm asuming it works :smile:

## use

Just add the following to your `pom.xml`

```xml
<repositories>
    ...
    <repository>
        <id>omk2-releases</id>
        <url>http://repo.omkserver.nl/content/repositories/releases/</url>
    </repository>
    ...
</repositories>
...
<dependencies>
    ...
    <dependency>
        <groupId>com.github.omkelderman</groupId>
        <artifactId>osu-db-parser</artifactId>
        <version>...</version>
    </dependency>
    ...
</dependencies>
```
