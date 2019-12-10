package edu.unl.cse.csce361.boggle.backend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DictionaryLoader {

    /**
     * Given a path to a directory, will return a list of files in that directory.
     * @param directoryPath
     * @return
     * @throws IOException
     */
    public static List<Path> getFilePathsInDir(String directoryPath) {
        Path dirPath;
        try{
            dirPath = Paths.get(directoryPath);
        } catch (InvalidPathException e){
            System.err.println("Directory not found: " + e.getLocalizedMessage());
            return null;
        }

        List<Path> pathList;
        try{
            pathList = Files.walk(dirPath)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e){
            System.err.println("IO Exception: " + e.getLocalizedMessage());
            return null;
        }
        return pathList;
    }

    /**
     * Given a list of file paths, reads in every line
     * and saves it to a list. Sorts it and makes sure
     * to remove duplicates and empty strings.
     * @param fileList
     * @return
     */
    public static  Set<String> getLinesInFiles(List<Path> fileList){
        Set<String> lines = new HashSet<>();
        for (Path filePath : fileList) {
            Set<String> fileLines;
            try {
                fileLines = Files.lines(filePath)
                        .map(String::strip)
                        .filter(str -> str.length() > 2)
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet());
            } catch (IOException e){
                System.err.println("IO Failure: " + e.getLocalizedMessage());
                return null;
            }
            lines.addAll(fileLines);
        }

        return lines;
    }

}
