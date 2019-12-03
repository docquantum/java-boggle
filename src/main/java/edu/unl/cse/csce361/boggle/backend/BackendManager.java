package edu.unl.cse.csce361.boggle.backend;

import edu.unl.cse.csce361.boggle.logic.GameManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static edu.unl.cse.csce361.boggle.backend.DictionaryLoader.getFilePathsInDir;
import static edu.unl.cse.csce361.boggle.backend.DictionaryLoader.getLinesInFiles;

public class BackendManager {


    private static BackendManager uniqueInstance = new BackendManager();
    private BackendManager() {}
    public static BackendManager getInstance() {
        return uniqueInstance;
    }


    public List<String> returnDictionary(){

        List<Path> path = new ArrayList<Path>();
        List<String> dic = new ArrayList<String>();

        path = getFilePathsInDir("src/test/data/files");

        dic = getLinesInFiles(path);

        return dic;
    }

}
