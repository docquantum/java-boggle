package edu.unl.cse.csce361.boggle.backend;

import edu.unl.cse.csce361.boggle.logic.GameManager;

import java.util.List;

public class BackendManager {
    public DictionaryLoader dic = new DictionaryLoader();


    private static BackendManager uniqueInstance = new BackendManager();
    private BackendManager() {}
    public static BackendManager getInstance() {
        return uniqueInstance;
    }


    List<String> returnDictionary(){




    }

}
