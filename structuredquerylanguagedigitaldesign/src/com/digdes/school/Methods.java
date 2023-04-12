package com.digdes.school;

public enum Methods {
    SELECT("SELECT"),
    UPDATE("UPDATE"),
    INSERT("INSERT"),
    DELETE("DELETE");
    private final String type;

    Methods(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static Methods getInputType(String str){
        for (Methods method : Methods.values()){
            if (method.getType().equalsIgnoreCase(str)){
                return method;
            }
        }
        return null;
    }
}
