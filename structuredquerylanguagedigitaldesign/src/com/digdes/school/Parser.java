package com.digdes.school;

import com.digdes.school.Exceptions.ParseObjectsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
    public static List<ProcessTwoObjects> parseWhere(List<Object> objectsList) throws ParseObjectsException {
        List<ProcessTwoObjects> listOfToken = processWhere(objectsList);
        Stack<ProcessTwoObjects> stack = new Stack<>();
        List<ProcessTwoObjects> result = new ArrayList<>();
        for (int i = 0; i < listOfToken.size(); i++) {
            if (listOfToken.get(i).getOperator().getType() == 0) {
                result.add(listOfToken.get(i));
            } else if (listOfToken.get(i).getOperator().getType() < 3) {
                if (stack.isEmpty()) {
                    stack.push(listOfToken.get(i));
                } else if (stack.peek().getOperator().getType() <= listOfToken.get(i).getOperator().getType()) {
                    stack.push(listOfToken.get(i));
                } else {
                    do {
                        result.add(stack.pop());
                    } while (!stack.isEmpty());
                    stack.push(listOfToken.get(i));
                }
            }
        }
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }


    public static List<ProcessTwoObjects> processWhere(List<Object> objectsList) throws ParseObjectsException {
        int index = 0;
        while (index< objectsList.size() && !(objectsList.get(index) instanceof String str && str.equalsIgnoreCase("WHERE"))){
            index++;
        }
        List<ProcessTwoObjects> processesTwoObjects = new ArrayList<>();
        ProcessTwoObjects processTwoObjects = new ProcessTwoObjects();
        for (int i = index+1; i < objectsList.size(); i++){
            if (objectsList.get(i) instanceof Operators operator){
                if (processTwoObjects.getObject1() == null){
                    if (operator.getType() == 0){
                        throw new ParseObjectsException("Invalid operators");
                    }else{
                        processTwoObjects.setOperator(operator);
                        processesTwoObjects.add(processTwoObjects);
                        processTwoObjects = new ProcessTwoObjects();
                    }
                }
                else{
                    processTwoObjects.setOperator(operator);
                }
            }
            else{
                if (processTwoObjects.getObject1() == null){
                    processTwoObjects.setObject1(objectsList.get(i));
                }
                else{
                    processTwoObjects.setObject2(objectsList.get(i));
                    processesTwoObjects.add(processTwoObjects);
                    processTwoObjects = new ProcessTwoObjects();
                }
            }
        }
        return processesTwoObjects;
    }
    public static List<ProcessTwoObjects> parseValues(List<Object> objectsList) throws ParseObjectsException {
        List<ProcessTwoObjects> processesTwoObjects = new ArrayList<>();
        ProcessTwoObjects processTwoObjects = new ProcessTwoObjects();
        for (int i = 2; i < objectsList.size() && !(objectsList.get(i) instanceof String string && string.equalsIgnoreCase("WHERE")); i++) {
            if (objectsList.get(i) instanceof Operators operator) {
                if (processTwoObjects.getOperator() == null) {
                    processTwoObjects.setOperator(operator);

                } else throw new ParseObjectsException("too many operators");

            } else {
                if (processTwoObjects.getObject1() == null) {
                    processTwoObjects.setObject1(objectsList.get(i));
                } else {
                    processTwoObjects.setObject2(objectsList.get(i));
                    processesTwoObjects.add(processTwoObjects);
                    processTwoObjects = new ProcessTwoObjects();
                }
            }
        }
        return processesTwoObjects;
    }
}
