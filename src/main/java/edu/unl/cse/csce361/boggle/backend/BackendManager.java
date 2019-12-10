package edu.unl.cse.csce361.boggle.backend;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static edu.unl.cse.csce361.boggle.backend.DictionaryLoader.getFilePathsInDir;
import static edu.unl.cse.csce361.boggle.backend.DictionaryLoader.getLinesInFiles;

public class BackendManager {

    public final Set<String> dictionary;

    private static BackendManager uniqueInstance = null;
    private BackendManager() {
        this.dictionary = loadDictionary();
    }

    public static BackendManager getInstance() {
        if(uniqueInstance == null){
            uniqueInstance = new BackendManager();
        }
        return uniqueInstance;
    }



    public Set<String> getDictionary() {
        return dictionary;
    }

    public Set<String> loadDictionary(){

        List<Path> path;
        Set<String> dic;
        path = getFilePathsInDir("src/main/resources/words/dicts");

        dic = getLinesInFiles(path);

        return dic;
    }

    public void setDictionary(String path){


    }

}
