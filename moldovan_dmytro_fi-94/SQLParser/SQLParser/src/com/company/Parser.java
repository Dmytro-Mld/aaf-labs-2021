package com.company;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Parser
{
    static Map<String, List<String>> tablesFields = new HashMap<>();
    static Map<String, List<List<String>>> tablesData = new HashMap<>();
    static Map<String, Map<String, List<String>>> tablesIndexes = new HashMap<>();

    private static final String CREATE = "((C|c)(r|R)(e|E)(a|A)(t|T)(E|e)[ ]*[a-z,A-Z,0-9]*[ ]*[(][a-z,A-Z,0-9, ]*[)]*)";
    private static final String INSERT = "[ ]*(((I|i)(N|n)(S|s)(E|e)(R|r)(T|t)[ ]((I|i)(n|N)(T|t)(O|o)))|((I|i)(N|n)(S|s)(E|e)(R|r)(T|t)))[ ]*[A-Z,a-z,0-9]*[ ]*[(][A-Z,a-z,0-9, \"]*[)]*[ ]*";
    private static final String SELECT = "[ ]*(S|s)(E|e)(L|l)(E|e)(C|c)(T|t)[ ]*[A-Z, a-z, 0-9, * ]*[ ]*(F|f)(R|r)(O|o)(M|m)[ ]*[A-Z, a-z, 0-9]*(([ ]*)|([ ]*((W|w)(H|h)(E|e)(R|r)(E|e))*[ ]*[A-Z, a-z, 0-9,\",=,!,>,<,+,\\-,*,\\/,&,|,.,(,)]*[ ]*))";
    private static final String DELETE = "[ ]*(D|d)(E|e)(L|l)(E|e)(T|t)(E|e)(([ ]*)|([ ]*(F|f)(R|r)(O|o)(M|m)[ ]*))[A-Z, a-z, 0-9]*(([ ]*)|([ ]*((W|w)(H|h)(E|e)(R|r)(E|e))*[ ]*[A-Z, a-z, 0-9,\",=,!,>,<,+,\\-,*,\\/,&,|,.,(,)]*[ ]*))";

    private static final Pattern createPattern = Pattern.compile(CREATE);
    private static final Pattern insertPattern = Pattern.compile(INSERT);
    private static final Pattern selectPattern = Pattern.compile(SELECT);
    private static final Pattern deletePattern = Pattern.compile(DELETE);

    public static void commandHandler(String command)
    {
        System.out.println("-------> : " + command);

        if (createPattern.matcher(command).matches())
        {
            createHandler(command);
        }
        else if (insertPattern.matcher(command).matches())
        {
            insertHandler(command);
        }
        else if (selectPattern.matcher(command).matches())
        {
            selectHandler(command);
        }
        else if (deletePattern.matcher(command).matches())
        {
            deleteHandler(command);
        }
        else
        {
            System.out.println("Error: your command is wrong");
        }
    }

    private static void createHandler(String command)
    {

        String[] queryArrayCreate = command.replaceFirst("( )*[C|c][R|r][E|e][A|a][T|t][E|e]( )*", "")
                .replaceAll("[(|)|,|;|]", "")
                .replaceAll("[ ]*[ ]", " ")
                .split(" ");

        String tableName = queryArrayCreate[0];

        if (tablesFields.containsKey(tableName))
        {
            System.out.println("Error: Table with this name is already created");
            return;
        }

        if (queryArrayCreate.length < 2)
        {
            System.out.println("Error: Columns not found");
            return;
        }

        LinkedList<String> fields = new LinkedList<>();
        for (int i = 1; i < queryArrayCreate.length; i++)
        {
            if (queryArrayCreate[i].equalsIgnoreCase("indexed"))
            {
                if (i == 1)
                {
                    System.out.println("Error: Indexed field not found");
                    return;
                }
                String indexName = tableName + '#' + queryArrayCreate[i - 1];
                tablesIndexes.put(indexName, new HashMap<String, List<String>>());
            } else
            {
                fields.add(queryArrayCreate[i]);
            }
        }

        tablesFields.put(tableName, fields);
        tablesData.put(tableName, new LinkedList<>());
        System.out.println("Table '" + tableName + "' has been created");
    }

    private static void insertHandler(String command)
    {
        String[] queryArray = command.replaceFirst("[ ]*(((I|i)(N|n)(S|s)(E|e)(R|r)(T|t)[ ]((I|i)(n|N)(T|t)(O|o)))|((I|i)(N|n)(S|s)(E|e)(R|r)(T|t)))[ ]*", "")
                .replaceAll("[(|)|,|;|]", "")
                .replaceAll("[ ]*[ ]", " ")
                .split(" ");


        String tableName = queryArray[0];

        if (!tablesFields.containsKey(tableName))
        {
            System.out.println("Error: Table with this name doesn't exist");
            return;
        }

        if (tablesFields.get(tableName).size() != (queryArray.length - 1))
        {
            System.out.println("Error: The amount of data doesn't match the number of columns");
            return;
        }

        LinkedList<String> fields = new LinkedList<>();
        for (int i = 1; i < queryArray.length; i++)
        {
            String value = queryArray[i].replaceAll("\"", "");
            fields.add(value);

            String indexName = tableName + '#' + tablesFields.get(tableName).get(i - 1);
            if (tablesIndexes.containsKey(indexName))
            {
                tablesIndexes.get(indexName).put(value, fields);
            }
        }
        tablesData.get(tableName).add(fields);
        System.out.println("1 row has been inserted into '" + tableName + "' table");
    }

    private static void selectHandler(String command)
    {
        String[] queryArray1 = command.replaceFirst("[ ]*(S|s)(E|e)(L|l)(E|e)(C|c)(T|t)*[ ]*[A-Z, a-z, 0-9, *]*[ ]*(F|f)(R|r)(O|o)(M|m)[ ]*", "")
                .replaceAll("[(|)|,|;|]", "")
                .replaceAll("[ ]*[ ]", " ").replaceAll("\"", "")
                .split(" ");

        String tableName = queryArray1[0];

        if (!tablesFields.containsKey(tableName))
        {
            System.out.println("Error: Table with this name doesn't exist");
            return;
        }

        int indexOfWhere = command.toLowerCase().indexOf("where");
        String cmd;
        if (indexOfWhere > -1)
        {
            cmd = command.substring(0, indexOfWhere);
        }
        else
            cmd = command;

        String[] array = cmd.replaceFirst("[ ]*(S|s)(E|e)(L|l)(E|e)(C|c)(T|t)[ ]*", "")
                .replaceAll("[ ]*(F|f)(R|r)(O|o)(M|m)*[A-Z, a-z, 0-9]*[ ]*", "")
                .replaceAll("[(|)|,|;|]", "")
                .replaceAll("[ ]*[ ]", " ")
                .split(" ");


        List<String> allFields = tablesFields.get(tableName);
        if (array.length == 1 && array[0].equals("*"))
        {
            array = allFields.toArray(new String[0]);
        } else {
            int counter = 0;
            for (String s : array) {
                if (!allFields.contains(s)) {
                    System.out.println("Error: List of fields is incorrect for the given table - " + s);
                    return;
                }
            }
        }


        SelectWhereParser expression = null;
        boolean indexedCalculation = false;
        String indexFieldName = "";
        Map<String, List<String>> indexedTableValues = Collections.emptyMap();
        if (indexOfWhere > -1) {
            cmd = command.replaceAll("\"", "").substring(indexOfWhere + "where".length());
            System.out.println("cmd " + cmd);
            expression = new SelectWhereParser(cmd);


            List<String> usedFields = new ArrayList<>();
            for (String item : allFields)
            {
                if (expression.isVarUsed(item))
                    usedFields.add(item);
            }
            if (usedFields.size() == 1)
            {
                String indexName = tableName + '#' + usedFields.get(0);
                if (tablesIndexes.containsKey(indexName))
                {
                    indexedCalculation = true;
                    indexedTableValues = tablesIndexes.get(indexName);
                    indexFieldName = usedFields.get(0);
                }
            }
        }


        for (int i = 0; i < array.length; i++)
            System.out.print("----------------|");
        System.out.println();

        for (String s : array)
        {
            System.out.printf("%15s |", s);
        }
        System.out.println();

        for (int i = 0; i < array.length; i++)
            System.out.print("----------------|");
        System.out.println();

        if (indexedCalculation)
        {
            SelectWhereParser finalExpression = expression;
            String finalIndexFieldName = indexFieldName;
            List<String> filteredKeys = indexedTableValues.keySet().stream().filter(obj ->
            {
                finalExpression.newVarAdd(finalIndexFieldName, obj);
                return (Boolean) finalExpression.calculateRow();
            }).collect(Collectors.toList());

            for(String item: filteredKeys)
            {
                List<String> list = indexedTableValues.get(item);
                for (String s : array)
                {
                    int index = allFields.indexOf(s);
                    System.out.printf("%15s |", list.get(index));
                }
                System.out.println();
                for (int i = 0; i < array.length; i++)
                    System.out.print("----------------|");
                System.out.println();
            }
        }
        else
        {
            List<List<String>> allFieldsData = tablesData.get(tableName);
            for (List<String> allFieldsDatum : allFieldsData)
            {

                if (expression != null)
                    for (int i = 0; i < allFields.size(); i++)
                    {
                        expression.newVarAdd(allFields.get(i), allFieldsDatum.get(i));
                    }

                if (expression == null || (Boolean) expression.calculateRow())
                {
                    for (String s : array)
                    {
                        int index = allFields.indexOf(s);
                        System.out.printf("%15s |", allFieldsDatum.get(index));
                    }
                    System.out.println();
                    for (int i = 0; i < array.length; i++)
                        System.out.print("----------------|");
                    System.out.println();
                }
            }
        }
        System.out.println();
    }

    private static void deleteHandler(String command)
    {
        String[] array = command.replaceFirst("[ ]*(D|d)(E|e)(L|l)(E|e)(T|t)(E|e)*[ ]*", "")
                .replaceAll("[ ]*(F|f)(R|r)(O|o)(M|m)[ ]*", "")
                .replaceAll("[(|)|,|;|]", "")
                .replaceAll("[ ]*[ ]", " ")
                .split(" ");


        String tableName = array[0];
        if (!tablesFields.containsKey(tableName))
        {
            System.out.println("Error: Table with this name doesn't exist");
            return;
        }

        int indexOfWhere = command.toLowerCase().indexOf("where");
        if (indexOfWhere == -1)
        {
            int count = tablesData.get(tableName).size();
            tablesData.put(tableName, new LinkedList<>());


            for (String indexName : tablesIndexes.keySet())
            {
                if (indexName.startsWith(tableName + '#'))
                {
                    tablesIndexes.get(indexName).clear();
                }
            }

            System.out.println(count + " rows have been deleted from the '" + tableName + "' table");
        }
        else
        {

            String cmd = command.replaceAll("\"", "").substring(indexOfWhere + "where".length());
            SelectWhereParser expression = new SelectWhereParser(cmd);

            int counter = 0;
            List<String> allFields = tablesFields.get(tableName);
            List<List<String>> allFieldsData = tablesData.get(tableName);

            for (int k = 0; k < allFieldsData.size(); k++)
            {

                for (int i = 0; i < allFields.size(); i++)
                {
                    expression.newVarAdd(allFields.get(i), allFieldsData.get(k).get(i));
                }

                if ((Boolean) expression.calculateRow())
                {


                    for (int i = 0; i < allFields.size(); i++)
                    {
                        String fieldName = allFields.get(i);
                        String indexName = tableName + '#' + fieldName;
                        if (tablesIndexes.containsKey(indexName))
                        {
                            tablesIndexes.get(indexName).remove(allFieldsData.get(k).get(i));
                        }
                    }

                    allFieldsData.remove(k);
                    k--;
                    counter++;
                }
            }
            System.out.println(counter + " rows have been deleted from the '" + tableName + "' table");
        }
    }
}
