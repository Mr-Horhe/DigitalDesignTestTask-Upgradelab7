package com.digdes.school;

public class Main {
    public static void main(String[]args){
        JavaSchoolStarter javaSchoolStarter = new JavaSchoolStarter();
        javaSchoolStarter.execute("insert vaLuEs 'lastName' = 'Попов' , 'id'=2, 'age'=18, 'active'=true");
        javaSchoolStarter.execute("INSERT VALUES 'lastName' = 'Fedorov' , 'id'=3, 'age'=40, 'active'=true");
        javaSchoolStarter.execute("INSERT values 'lastName' = 'Voev' , 'id'=3, 'age'=40, 'active'=true");
        System.out.println(javaSchoolStarter.execute("INSERT VALUES 'lastName' = 'Morozov' , 'id'=4, 'age'=21, 'cost' = 5.5,'active'=true WHERE 'lastName' < 228"));
        System.out.println();
        System.out.println(javaSchoolStarter.execute("select where 'lastName' = 'Fedorov'"));
        System.out.println();
        System.out.println(javaSchoolStarter.execute("update values 'lastName' = 'dyachenko' WHERE 'lastName' = 'Fedorov'"));
        System.out.println();
        System.out.println(javaSchoolStarter.execute("select where 'lastName' ilike 'поп%'"));
        System.out.println();
        System.out.println(javaSchoolStarter.getTable());
        System.out.println(javaSchoolStarter.execute("SELECT WHERE 'age' = 21"));
        //System.out.println(javaSchoolStarter.execute("UPDATE WHERE 'cost' > 10.0"));
        //System.out.println(javaSchoolStarter.execute("UPDATE VALUES 'cost' = 5.5"));
    }
}
