package com.cloud.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HtmlParser {

    public static String parseHtml(String fileName) throws IOException {
        Path path = Paths.get(HtmlParser.class.getClassLoader().getResource(fileName).getPath());
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
