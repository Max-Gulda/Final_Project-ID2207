package com.sep.system.user;

import java.util.ArrayList;
import java.util.List;

public class SubTeam {
    private String name;
    private List<SimpleUser> members;
    private ProductionServiceManager manager;

    public SubTeam(String name, ProductionServiceManager manager) {
        this.name = name;
        this.members = new ArrayList<>();
        this.manager = manager;
    }

    public ProductionServiceManager getManager(){
        return manager;
    }

    public String getName() {
        return name;
    }

    public List<SimpleUser> getMembers() {
        return members;
    }

    public void addMember(SimpleUser user) {
        members.add(user);
    }
}
