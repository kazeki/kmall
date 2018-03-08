package com.kzk.kefu.core;

import java.util.HashMap;
import java.util.Map;

public class KefuMeiMeiService {
    private Map<String,String> knowledge = new HashMap<>();

    private String prefix;
    private String suffix;

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setKnowledge(Map<String,String> knowledge){
        this.knowledge = knowledge;
    }

    public String talk(String msg) {
        String reply = "";
        if(knowledge!=null&&knowledge.containsKey(msg)){
            reply = knowledge.get(msg);
        }else{
            reply = "I don't Know! ";
        }
        return prefix + reply + suffix;
    }
}
