package com.company;

import java.util.*;

public class SelectWhereParser
{
    final static String literalChars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz01234567890_.";
    final static String symbols = "+-/*=<>&|!^()";

    private final LinkedList<String> listOfResults = new LinkedList<>();
    private final Map<String, Object> mapVariables = new HashMap<>();
    private final String stringExpression;

    final static List<String> opers1 = Arrays.asList("(", ")");
    final static List<String> opers2 = Arrays.asList("=", "<", ">", "<=", ">=", "!=", "&", "|", "^");
    final static List<String> opers3 = Arrays.asList("!");
    final static List<String> opers4 = Arrays.asList("+", "-");
    final static List<String> opers5 = Arrays.asList("/", "*");

    SelectWhereParser(String strExpression)
    {
        this.stringExpression = strExpression;
        makeToPolishNotation();
    }

    public boolean isVarUsed(String varName)
    {
        return listOfResults.contains(varName);
    }

    private static boolean tokenIsOperator(String token)
    {
        return (opers1.contains(token) ||
                opers2.contains(token) ||
                opers3.contains(token) ||
                opers4.contains(token) ||
                opers5.contains(token));
    }

    private static int buildPriority(String token)
    {
        if (opers1.contains(token)) return 1;
        if (opers2.contains(token)) return 2;
        if (opers3.contains(token)) return 3;
        if (opers4.contains(token)) return 4;
        if (opers5.contains(token)) return 5;
        return 6;
    }

    public Object calculateRow()
    {
        if (listOfResults.isEmpty())
            makeToPolishNotation();
        return evaluatePolishNotation();
    }

    public void newVarAdd(String key, Object value)
    {
        if (key == null || key.isEmpty() || value == null)
            return;
        if (value instanceof Double)
            mapVariables.put(key, value);
        else if (value instanceof Number)
        {
            Double newVal = (double) ((Integer) value);
            mapVariables.put(key, newVal);
        } else
            mapVariables.put(key, value);
    }

    public void makeToPolishNotation()
    {
        LinkedList<String> queueList = new LinkedList<>();
        LinkedList<String> listOfTokens = new LinkedList<>();
        listOfResults.clear();

        String expression = stringExpression.replaceAll(" ", "");

        int expressionIndex = 0;

        while (expressionIndex < expression.length())
        {
            if (expression.charAt(expressionIndex) == ' ')
            {
                expressionIndex++;
                continue;
            }
            if (literalChars.indexOf(expression.charAt(expressionIndex)) > -1)
            {
                StringBuilder digitObj = new StringBuilder();
                while (expressionIndex < expression.length() &&
                        literalChars.indexOf(expression.charAt(expressionIndex)) > -1)
                {
                    digitObj.append(expression.charAt(expressionIndex));
                    expressionIndex++;
                }
                listOfTokens.add(digitObj.toString());
            } else if (symbols.indexOf(expression.charAt(expressionIndex)) > -1)
            {
                StringBuilder symbolObj = new StringBuilder();
                while (expressionIndex < expression.length() &&
                        symbols.indexOf(expression.charAt(expressionIndex)) > -1)
                {
                    symbolObj.append(expression.charAt(expressionIndex));
                    expressionIndex++;
                }
                if (tokenIsOperator(symbolObj.toString()))
                {
                    listOfTokens.add(symbolObj.toString());
                } else
                {
                    int i0 = 0;
                    int i1 = 1;
                    while (i0 < symbolObj.length())
                    {
                        if (i1 < symbolObj.length() && tokenIsOperator(symbolObj.substring(i0, i1 + 1)))
                        {
                            listOfTokens.add(symbolObj.substring(i0, i1 + 1));
                            i0 += 2;
                        } else
                        {
                            listOfTokens.add(symbolObj.substring(i0, i1));
                            i0++;
                        }
                        i1 = i0 + 1;
                        i1 = Math.min(i1, symbolObj.length());
                    }
                }
            }
        }

        for (String tokenValue : listOfTokens)
        {
            if (tokenIsOperator(tokenValue))
            {
                if (tokenValue.equals("("))
                {
                    queueList.addFirst(tokenValue);
                } else if (tokenValue.equals(")"))
                {
                    String item = queueList.removeFirst();
                    while (!"(".equals(item))
                    {
                        listOfResults.addLast(item);
                        item = queueList.removeFirst();
                    }
                }
                else
                {
                    while (!queueList.isEmpty() &&
                            (buildPriority(tokenValue) <= buildPriority(queueList.peek())))
                    {
                        listOfResults.add(queueList.removeFirst());
                    }
                    queueList.addFirst(tokenValue);
                }
            }
            else
            {
                listOfResults.add(tokenValue);
            }
        }

        while (!queueList.isEmpty())
        {
            listOfResults.add(queueList.removeFirst());
        }

        System.out.println("resultList " + listOfResults);
    }

    public Object evaluatePolishNotation()
    {
        LinkedList<Object> queueList = new LinkedList<>();

        for (String token : listOfResults)
        {

            if (tokenIsOperator(token))
            {
                String rightValue;
                String leftValue;
                Boolean rightBool;
                Boolean leftBool;
                String oRight;
                String oLeft;
                boolean boolResult;

                switch (token)
                {
                    // "+", "-", "*", "/"
                    case "<" ->
                            {
                        rightValue = String.valueOf(queueList.removeFirst());
                        leftValue = String.valueOf(queueList.removeFirst());
                        queueList.addFirst(Integer.parseInt(leftValue) < Integer.parseInt(rightValue));
                    }
                    case ">" ->
                            {
                        rightValue = String.valueOf(queueList.removeFirst());
                        leftValue = String.valueOf(queueList.removeFirst());
                        queueList.addFirst(Integer.parseInt(leftValue) > Integer.parseInt(rightValue));
                    }
                    case "<=" ->
                            {
                        rightValue = String.valueOf(queueList.removeFirst());
                        leftValue = String.valueOf(queueList.removeFirst());
                        queueList.addFirst(Integer.parseInt(leftValue) <= Integer.parseInt(rightValue));
                    }
                    case ">=" ->
                            {
                        rightValue = String.valueOf(queueList.removeFirst());
                        leftValue = String.valueOf(queueList.removeFirst());
                        queueList.addFirst(Integer.parseInt(leftValue) >= Integer.parseInt(rightValue));
                    }
                    case "=" ->
                            {
                        oRight = String.valueOf(queueList.removeFirst());
                        oLeft = String.valueOf(queueList.removeFirst());
                        boolResult = oLeft.equals(oRight);
                        queueList.addFirst(boolResult);
                    }
                    case "!=" ->
                            {
                        oRight = String.valueOf(queueList.removeFirst());
                        oLeft = String.valueOf(queueList.removeFirst());
                        boolResult = oLeft.equals(oRight);
                        queueList.addFirst(!boolResult);
                    }
                    case "&" ->
                            {
                        rightBool = (Boolean) queueList.removeFirst();
                        leftBool = (Boolean) queueList.removeFirst();
                        queueList.addFirst(leftBool && rightBool);
                    }
                    case "|" ->
                            {
                        rightBool = (Boolean) queueList.removeFirst();
                        leftBool = (Boolean) queueList.removeFirst();
                        queueList.addFirst(leftBool || rightBool);
                    }
                }
            }
            else
            {
                if (isNumeric(token))
                {
                    queueList.addFirst(Integer.parseInt(token));
                } else if (mapVariables.containsKey(token) && mapVariables.get(token) != null)
                {
                    queueList.addFirst(mapVariables.get(token));
                }
                else if (token != null && !mapVariables.containsKey(token))
                {
                    queueList.addFirst(token);
                }
            }
        }
        if (queueList.size() > 1)
        {
            throw new ArithmeticException(queueList.toString());
        }
        return queueList.removeFirst();
    }

    public static boolean isNumeric(String string)
    {
        int intValue;

        if (string == null || string.equals(""))
        {
            System.out.println("String cannot be parsed, it is null or empty.");
            return false;
        }

        try
        {
            intValue = Integer.parseInt(string);
            return true;
        }
        catch (NumberFormatException ignored)
        {
        }
        return false;
    }

}
