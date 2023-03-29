package ru.JSilver.asterisk.reports.data;

import java.util.HashMap;
import java.util.Map;

public class QueueMap {
    private final Map<String,String> qMap;

    public QueueMap() {
        this.qMap = new HashMap<>();
        qMap.put("1100","1100");
        qMap.put("1111","1304");
        qMap.put("1112","2124,3137,3138,3139,3140,3141,3142,5002,5003,5004");
        qMap.put("1113","1305");
        qMap.put("1114","3209,3253,3254,3247,5026");
        qMap.put("1115","5119,2118,3165");
        qMap.put("1116","1301");
        qMap.put("1117","1302");
        qMap.put("1118","3177");
        qMap.put("1119","3200");
        qMap.put("1120","5002,5003,5004");
        qMap.put("1121","3137,3138,3139,3140,3141,3142,5002,5003,5004");
        qMap.put("1122","4335,4105");
    }

    public String getQueue(String queueSet, String number) {
        if (qMap.get(queueSet) != null && qMap.get(queueSet).contains(number)) {
            return queueSet;
        }
        for (Map.Entry<String, String> pairs : qMap.entrySet()) {
            if (pairs.getKey().equals(number))
                return pairs.getKey();
             else if (pairs.getValue().contains(number)) {
                return prioritize(pairs.getKey());
            }
        }
        return number;
    }

    private String prioritize(String key) {
        String similarQueues = "1112,1120,1121";
        if (similarQueues.contains(key)) {
            return "1112";
        }
        return key;
    }
}
