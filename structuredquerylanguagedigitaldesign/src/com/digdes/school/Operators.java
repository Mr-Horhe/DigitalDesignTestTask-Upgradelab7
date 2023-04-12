package com.digdes.school;

import java.util.regex.Pattern;

public enum Operators implements CompareObjects{
    EQUALS("=", 0){
        @Override
        public boolean comparition(Object value1, Object value2){
            return value1.equals(value2);
        }
    },
    NOTEQUALS("!=", 0){
        @Override
        public boolean comparition(Object value1, Object value2){
            return !value1.equals(value2);
        }
    },
    HIGHEREQUALS(">=", 0){
        @Override
        public boolean comparition(Object value1, Object value2) {
            if (value1 instanceof Double double1 && value2 instanceof Double double2){
                return double1 >= double2;
            }
            if (value1 instanceof Double double1 && value2 instanceof Long long2){
                return double1>=long2;
            }
            if (value1 instanceof Long long1 && value2 instanceof Long long2){
                return long1 >= long2;
            }
            if (value1 instanceof Long long1 && value2 instanceof Double double2){
                return long1>= double2;
            }
            throw new IllegalArgumentException("Invalid query");
        }
    },
    LESSEQUALS("<=", 0){
        @Override
        public boolean comparition(Object value1, Object value2){
            if (value1 instanceof Double double1 && value2 instanceof Double double2){
                return double1 <= double2;
            }
            if (value1 instanceof Double double1 && value2 instanceof Long long2){
                return double1<=long2;
            }
            if (value1 instanceof Long long1 && value2 instanceof Long long2){
                return long1 <= long2;
            }
            if (value1 instanceof Long long1 && value2 instanceof Double double2){
                return long1 <= double2;
            }
            throw new IllegalArgumentException("Invalid query");
        }
    },
    HIGHER(">", 0){
        @Override
        public boolean comparition(Object value1, Object value2){
            if (value1 instanceof Double double1 && value2 instanceof Double double2){
                return double1 > double2;
            }
            if (value1 instanceof Double double1 && value2 instanceof Long long2){
                return double1 > long2;
            }
            if (value1 instanceof Long long1 && value2 instanceof Long long2){
                return long1 > long2;
            }
            if (value1 instanceof Long long1 && value2 instanceof Double double2){
                return long1 > double2;
            }
            throw new IllegalArgumentException("Invalid query");
        }
    },
    LESS("<", 0){
        @Override
        public boolean comparition(Object value1, Object value2){
            if (value1 instanceof Double double1 && value2 instanceof Double double2){
                return double1 < double2;
            }
            if (value1 instanceof Double double1 && value2 instanceof Long long2){
                return double1<long2;
            }
            if (value1 instanceof Long long1 && value2 instanceof Long long2){
                return long1 < long2;
            }
            if (value1 instanceof Long long1 && value2 instanceof Double double2){
                return long1 < double2;
            }
            throw new IllegalArgumentException("Invalid query");
        }
    },
    AND("AND", 2){
        @Override
        public boolean comparition(Object value1, Object value2) {
            if (value1 instanceof Boolean boolean1 && value2 instanceof Boolean boolean2){
                return boolean1 && boolean2;
            }
            throw new IllegalArgumentException("Invalid query");
        }
    },
    OR("OR", 1){
        @Override
        public boolean comparition(Object value1, Object value2)  {
            if (value1 instanceof Boolean boolean1 && value2 instanceof Boolean boolean2){
                return boolean1 || boolean2;
            }
            throw new IllegalArgumentException("Invalid query");
        }
    },
    LIKE("LIKE", 0){
        @Override
        public boolean comparition(Object value1, Object value2){
            if (value1 instanceof String string1 && value2 instanceof String string2){
               return Pattern.compile(string2.replaceAll("%", ".*")).matcher(string1).matches();
            }
            throw new IllegalArgumentException("Invalid query");
        }
    },
    ILIKE("ILIKE", 0){
        @Override
        public boolean comparition(Object value1, Object value2) {
            if (value1 instanceof String string1 && value2 instanceof String string2){
                return Pattern.compile(string2.replaceAll("%", ".*"), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(string1).matches();
            }
            throw new IllegalArgumentException("Invalid query");
        }
    };

    private int type;
    private String operator;

    public int getType() {
        return type;
    }

    Operators(String operator, int type){
        this.type = type;
        this.operator=operator;
    }


    public static Operators findOperator(String string){
        for (Operators op : Operators.values()){
            if (op.operator.equals(string.toUpperCase())){
                return op;
            }
        }
        throw new IllegalArgumentException("Invalid operator");
    }


}
