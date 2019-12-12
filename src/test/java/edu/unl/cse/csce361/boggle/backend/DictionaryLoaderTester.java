package edu.unl.cse.csce361.boggle.backend;

import org.junit.Test;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class DictionaryLoaderTester {

    @Test
    public void getFilePathsInDirTest(){
        String directory = "src/test/data/files/dicLoadTest";

        List<Path> expectedPaths= new ArrayList<>();
        expectedPaths.add(Path.of(directory + "/file1.txt"));
        expectedPaths.add(Path.of(directory + "/file2"));

        List<Path> actualPaths = DictionaryLoader.getFilePathsInDir(directory);

        assertTrue(expectedPaths.containsAll(actualPaths));
    }

    @Test
    public void getLinesInFilesTest(){
        String directory = "src/test/data/files";
        List<Path> filePaths = DictionaryLoader.getFilePathsInDir(directory);

        Set<String> expectedLines = new HashSet<>();

        expectedLines.addAll(Arrays.stream(new String[]{"This", "Has", "some", "words", "one", "too", "Duplicate"})
                .map(String::toLowerCase).collect(Collectors.toSet()));

        Set<String> actualLines = DictionaryLoader.getLinesInFiles(filePaths);

        assertTrue(expectedLines.containsAll(actualLines));

    }
}
