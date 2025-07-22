package com.tidal.utils.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.tidal.utils.exceptions.DataResolverException;
import com.tidal.utils.propertieshandler.PropertiesFinder;
import com.tidal.utils.scenario.ScenarioInfo;
import com.tidal.utils.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("All")
public class CsvData {
    private final Logger logger = LoggerFactory.getLogger(CsvData.class);
    private String defaultDataFilePath = getDataFilePath();

    public void setCSVFolderAsDataFilePath(){
        defaultDataFilePath = Helper.getAbsoluteFromRelativePath("src/test/resources/csv") + "/";
    }

    public synchronized void updateDataTo(String dataPoolName, String key, String value) {
        String testCaseName = ScenarioInfo.getScenarioName();
        updateDataTo(dataPoolName, key, value, testCaseName);
    }

    public synchronized void updateDataTo(String dataPoolName, String key, String value, String testCaseName) {
        HashMap<String, String> map = new HashMap<>();
        map.put(key, value);
        updateDataTo(dataPoolName, map, testCaseName);
    }

    public synchronized void updateDataTo(String dataPoolName, Map<String, String> map) {
        String testCaseName = ScenarioInfo.getScenarioName();
        updateData(map, dataPoolName, testCaseName);
    }

    public synchronized void updateDataTo(String dataPoolName, Map<String, String> map, String testCaseName) {
        updateData(map, dataPoolName, testCaseName);
    }

    public synchronized String readDataFrom(String dataPoolName, String key) {
        String testCaseName = ScenarioInfo.getScenarioName();
        return readDataFrom(dataPoolName, key, testCaseName);
    }

    public synchronized String readDataFrom(String dataPoolName, String key, String testcaseName) {
        return readData(dataPoolName, testcaseName).get(key);
    }

    public synchronized LinkedHashMap<String, String> readData(String dataPoolName) {
        String testCaseName = ScenarioInfo.getScenarioName();
        return readData(dataPoolName, testCaseName);
    }

    private synchronized void updateData(Map<String, String> dataMap, String dataPoolName, String testCaseName) {

        String[] nextLine;
        List<String[]> arrayList = new ArrayList<>();
        Map<String, String> newData;
        newData = dataMap;
        CsvData csvData = new CsvData();
        HashMap<String, String> existingDataMap = csvData.readData(dataPoolName, testCaseName);
        HashMap<String, String> newDataMap = existingDataMap;

        for (Map.Entry<String, String> existingDataMapEntry : existingDataMap.entrySet()) {
            for (Map.Entry<String, String> mapEntry : newData.entrySet()) {
                String existingDataMapKey = existingDataMapEntry.getKey().trim().replace(" ", "");
                String newDataMapKey = mapEntry.getKey().trim().replace(" ", "");
                if (existingDataMapKey.equalsIgnoreCase(newDataMapKey)) {
                    newDataMap.put(existingDataMapEntry.getKey(), mapEntry.getValue());
                }
            }
        }

        int index = 0;
        String[] values = new String[existingDataMap.size()];
        for (Map.Entry<String, String> mapEntry : newDataMap.entrySet()) {
            values[index] = mapEntry.getValue();
            index++;
        }

        try (CSVReader reader = new CSVReader(new FileReader( defaultDataFilePath+ dataPoolName + ".csv"))) {

            CSVWriter writer = null;
            try {
                while ((nextLine = reader.readNext()) != null) {
                    if (nextLine[0].trim().equals(testCaseName)) {
                        arrayList.add(values);
                    } else {
                        arrayList.add(nextLine);
                    }
                }
                writer = new CSVWriter(new FileWriter((defaultDataFilePath + dataPoolName + ".csv")));
            } catch (IOException | CsvValidationException e) {
                e.printStackTrace();
            } finally {
                if (null != writer) {
                    writer.writeAll(arrayList);
                    writer.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized String getDataFilePath() {
        String csvFilePath = Helper.getAbsoluteFromRelativePath("src/test/resources/csv");
        String env = PropertiesFinder.getEnvironment();
        String filePath = csvFilePath + "/" + env + "/";
        return filePath;
    }

    protected synchronized LinkedHashMap<String, String> readData(String dataPoolName, String testCaseName) {
        String[] columnHeader = null;
        String[] nextLine = null;
        int lineNumber = 0;
        String[] dataRow = null;

        //Linked HashMap sort the entries in order
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        logger.info("Reading data from: " + defaultDataFilePath + dataPoolName + ".csv");

        try (FileReader fileReader = new FileReader(defaultDataFilePath + dataPoolName + ".csv")) {
            try (CSVReader reader = new CSVReader(fileReader)) {

                while ((nextLine = reader.readNext()) != null) {
                    if (lineNumber == 0) {
                        columnHeader = nextLine;
                    }
                    lineNumber++;

                    if (nextLine[0].trim().equalsIgnoreCase(testCaseName.trim())) {
                        dataRow = nextLine;
                        logger.info(String.format("Found data from row %s for test case: '%s'", lineNumber, testCaseName));
                        break;
                    }
                }

                if (dataRow == null) {
                    throw new DataResolverException();
                }

                for (int index = 0; index < columnHeader.length; index++) {
                    hashMap.put(columnHeader[index].strip(), dataRow[index].strip());
                }
            } catch (Exception e) {
                if (e instanceof DataResolverException) {
                    throw new DataResolverException();
                } else {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            if (e instanceof DataResolverException) {
                String message = String.format("The test case '%s' was not found from file " +
                        "'%s.csv'", testCaseName, dataPoolName);
                logger.info(message);
            } else {
                e.printStackTrace();
            }
        }
        return hashMap;
    }

    protected synchronized List<Map<String, String>> readDataAsMapList(String dataPoolName, String testCaseName) {
        String[] columnHeader = null;
        String[] nextLine = null;
        int lineNumber = 0;
        String[] dataRow = null;

        List<Map<String, String>> dataCollectionList = new ArrayList<>();


        logger.info("Reading data from: " + defaultDataFilePath + dataPoolName + ".csv");

        try (FileReader fileReader = new FileReader(defaultDataFilePath + dataPoolName + ".csv")) {
            try (CSVReader reader = new CSVReader(fileReader)) {

                while ((nextLine = reader.readNext()) != null) {
                    if (lineNumber == 0) {
                        columnHeader = nextLine;
                    }
                    lineNumber++;

                    if (nextLine[0].trim().equalsIgnoreCase(testCaseName.trim())) {

                        //Linked HashMap sort the entries in order
                        //The hashmap needs to intialized for each new entry.
                        // Otherwise adding the same hashmap to the list, would result in copying the same into all list entries already stored
                        HashMap<String, String> hashMap = new LinkedHashMap<>();

                        dataRow = nextLine;
                        logger.info(String.format("Found data from row %s for test case %s", lineNumber, testCaseName));
                        for (int index = 0; index < columnHeader.length; index++) {
                            hashMap.put(columnHeader[index], dataRow[index].trim());
                        }
                        dataCollectionList.add(hashMap);
                    }
                }

                if (dataRow == null) {
                    throw new DataResolverException();
                }


            } catch (Exception e) {
                if (e instanceof DataResolverException) {
                    throw new DataResolverException();
                } else {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            if (e instanceof DataResolverException) {
                String message = String.format("The test case '%s' was not found from file " +
                        "'%s.csv'", testCaseName, dataPoolName);
                logger.info(message);
            } else {
                e.printStackTrace();
            }
        }
        return dataCollectionList;
    }

    protected synchronized List<Map<String, String>> readFullDataAsMapList(String dataPoolName) {
        String[] columnHeader = null;
        String[] nextLine = null;
        int lineNumber = 0;
        String[] dataRow = null;

        List<Map<String, String>> dataCollectionList = new ArrayList<>();


        logger.info("Reading data from: " + defaultDataFilePath + dataPoolName + ".csv");

        try (FileReader fileReader = new FileReader(defaultDataFilePath + dataPoolName + ".csv")) {
            try (CSVReader reader = new CSVReader(fileReader)) {

                while ((nextLine = reader.readNext()) != null) {
                    if (lineNumber == 0) {
                        columnHeader = nextLine;
                    }
                    lineNumber++;
                        //Linked HashMap sort the entries in order
                        //The map needs to intialized for each new entry.
                        // Otherwise adding the same hashmap to the list, would result in copying the same into all list entries already stored
                        Map<String, String> map = new LinkedHashMap<>();

                        dataRow = nextLine;
                        for (int index = 0; index < columnHeader.length; index++) {
                            map.put(columnHeader[index], dataRow[index].trim());
                        }
                        dataCollectionList.add(map);
                }

                if (dataRow == null) {
                    throw new DataResolverException();
                }


            } catch (Exception e) {
                if (e instanceof DataResolverException) {
                    throw new DataResolverException();
                } else {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            if (e instanceof DataResolverException) {
                logger.info(e.getMessage());
            } else {
                e.printStackTrace();
            }
        }
        return dataCollectionList;
    }
}
