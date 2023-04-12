package com.digdes.school;

import com.digdes.school.Exceptions.ColumnExistException;
import com.digdes.school.Exceptions.ParseObjectsException;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JavaSchoolStarter {
    private List<Map<String, Object>> table;
    JavaSchoolStarter() {
        table = new ArrayList<>();
    }

    public List<Map<String, Object>> execute(String query) {
        Pattern p = Pattern.compile("(=|!=|>=|<=|>|<|\\b(SELECT|UPDATE|INSERT|DELETE|VALUES|WHERE|LIKE|ILIKE|AND|OR|true|false)\\b|\\d+\\.?\\d*|'[\\wА-я]+'|'%?[\\wА-я]+%?')", Pattern.CASE_INSENSITIVE);
        List<String> splittedQuery = p.matcher(query).results().map(MatchResult::group).collect(Collectors.toList());
        if (splittedQuery.get(0).equalsIgnoreCase("SELECT") ||
                splittedQuery.get(0).equalsIgnoreCase("UPDATE") ||
                splittedQuery.get(0).equalsIgnoreCase("INSERT") ||
                splittedQuery.get(0).equalsIgnoreCase("DELETE")) {

            for (int i = 1; i < splittedQuery.size(); i++){
                if (splittedQuery.get(i).equalsIgnoreCase("SELECT") ||
                        splittedQuery.get(i).equalsIgnoreCase("UPDATE") ||
                        splittedQuery.get(i).equalsIgnoreCase("INSERT") ||
                        splittedQuery.get(i).equalsIgnoreCase("DELETE")){
                    throw new IllegalArgumentException("Invalid query");
                }
            }
            try {
                List<Object> objectsList = new ArrayList<>();
                for (int j = 0; j < splittedQuery.size(); j++) {
                    objectsList.add(convertToObj(splittedQuery.get(j)));
                }

                return caseMethods(Methods.getInputType(splittedQuery.get(0)), objectsList);
            }
            catch (Exception exception){
                throw new IllegalArgumentException("Invalid query");
            }
        }
        return null;
    }

    public List<Map<String, Object>> getTable() {
        return table;
    }

    private List<ProcessTwoObjects> parse(String type, List<Object> objectList) throws ParseObjectsException {
        switch (type) {
            case "VALUES" -> {
                return Parser.parseValues(objectList);
            }
            case "WHERE" -> {
                return Parser.parseWhere(objectList);
            }
            default -> throw new RuntimeException();
        }
    }

    private Map<String, List<ProcessTwoObjects>> getSplittedMap(List<Object> objectList) throws ParseObjectsException {
        Map<String, List<ProcessTwoObjects>> map = new HashMap<>();
        for (int i = 1; i < objectList.size(); i++) {
            if (objectList.get(i) instanceof String string && (string.equalsIgnoreCase("VALUES") || string.equalsIgnoreCase("WHERE"))) {
                map.put(string.toUpperCase(), parse(string.toUpperCase(), objectList));
            }
        }
        return map;
    }

    public List<Map<String, Object>> caseMethods(Methods method, List<Object> objectList) throws Exception {
        Map<String, Object> outMap = new HashMap<>();
        List<Map<String, Object>> outList = new ArrayList<>();
        List<ProcessTwoObjects> listValues = new ArrayList<>();
        List<ProcessTwoObjects> listWheres = new ArrayList<>();
        switch (method) {
            case SELECT -> {
                if (getSplittedMap(objectList).containsKey("WHERE")) {
                    for (Map<String, Object> stringObjectMap : table) {
                        if (extractionFinalWhere(Parser.parseWhere(objectList), stringObjectMap)) {
                            outList.add(stringObjectMap);
                        }
                    }
                    return outList;
                } else {
                    return table;
                }
            }
            case INSERT -> {
                if (getSplittedMap(objectList).containsKey("VALUES")) {
                    extractionFinalValues(Parser.parseValues(objectList), outMap);
                    table.add(outMap);
                    return table;
                } else {
                    throw new NoSuchElementException("operator VALUES is not exist in query");
                }
            }
            case DELETE -> {
                if (getSplittedMap(objectList).containsKey("WHERE")) {
                    listWheres = getSplittedMap(objectList).get("WHERE");
                } else {
                    List<Map<String, Object>> list;
                    list = table;
                    table = new ArrayList<>();
                    return list;
                }
                for (int i = 0; i < table.size(); i++) {
                    if (extractionFinalWhere(listWheres, table.get(i))) {
                        table.remove(i);
                        i--;
                    }
                }
                return table;
            }
            case UPDATE -> {
                if (getSplittedMap(objectList).containsKey("VALUES")) {
                    listValues = getSplittedMap(objectList).get("VALUES");
                }
                if (getSplittedMap(objectList).containsKey("WHERE")) {
                    listWheres = getSplittedMap(objectList).get("WHERE");
                }
                for (Map<String, Object> column : table) {
                    if (!listWheres.isEmpty()) {
                        if (extractionFinalWhere(listWheres, column)) {
                            extractionFinalValues(listValues, column);
                            outList.add(column);
                        }
                    } else {
                        extractionFinalValues(listValues, column);
                        outList.add(column);
                    }
                }
                return outList;
            }
            default -> throw new IllegalArgumentException("Invalid method");
        }
    }

    public static Object convertToObj(String newObject) {
        if (Pattern.compile("^(true | false)$").matcher(newObject).matches()) {
            return Boolean.valueOf(newObject);
        } else if (Pattern.compile("^\\d+$").matcher(newObject).matches()) {
            return Long.valueOf(newObject);
        } else if (Pattern.compile("^\\d+\\.\\d*").matcher(newObject).matches()) {
            return Double.valueOf(newObject);
        } else {
            try {
                return Operators.findOperator(newObject.toUpperCase());
            } catch (Exception e) {
                return newObject;
            }
        }
    }

    public boolean extractionFinalWhere(List<ProcessTwoObjects> processesTwoObjects, Map<String, Object> outMap) throws Exception {
        Stack<Boolean> stack = new Stack<>();
        for (ProcessTwoObjects processTwoObjects : processesTwoObjects) {
            if (processTwoObjects.getObject1() == null) {
                if (processTwoObjects.getOperator() != null) {
                    stack.push(processTwoObjects.getOperator().comparition(stack.pop(), stack.pop()));
                }
            } else if (processTwoObjects.getObject1() instanceof String string) {
                if (outMap.containsKey(string)) {
                    stack.push(processTwoObjects.getOperator().comparition(outMap.get(string), processTwoObjects.getObject2()));
                } else throw new ColumnExistException("Column does not exist");
            } else {
                stack.push(processTwoObjects.getOperator().comparition(processTwoObjects.getObject1(), processTwoObjects.getObject2()));
            }
        }
        if (stack.size() != 1) {
            throw new ColumnExistException("No such operator");
        } else {
            return stack.pop();
        }
    }

    public void extractionFinalValues(List<ProcessTwoObjects> processesTwoObjects, Map<String, Object> outMap) {
        for (ProcessTwoObjects p : processesTwoObjects) {
            if (p.getOperator().equals(Operators.EQUALS)) {
                if (p.getObject1() instanceof String key) {
                    if (outMap.containsKey(key)) {
                        if (((p.getObject2() instanceof Long || p.getObject2() instanceof Double) && (outMap.get(key) instanceof Long || outMap.get(key) instanceof Double)) ||
                                (p.getObject2() instanceof Boolean && outMap.get(key) instanceof Boolean) || (outMap.get(key) instanceof String && p.getObject2() instanceof String str && !str.equalsIgnoreCase("null"))) {
                            outMap.put(key, p.getObject2());
                        } else {
                            if (p.getObject2() instanceof String && outMap.get(key) instanceof String) {
                                outMap.put(key, null);
                            } else {
                                throw new IllegalArgumentException("Invalid values types");
                            }
                        }
                    } else {
                        outMap.put(key, p.getObject2());
                    }
                } else {
                    throw new IllegalArgumentException("Invalid column name");
                }
            } else {
                throw new IllegalArgumentException("Invalid operator");
            }
        }
    }
}