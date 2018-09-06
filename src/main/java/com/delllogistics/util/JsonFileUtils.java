package com.delllogistics.util;

import com.alibaba.fastjson.*;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class JsonFileUtils {

/*    *//**
     * JSONObject排序
     *
     * @param obj
     * @return
     *//*
    @SuppressWarnings("all")
    public static Map sortJsonObject(JSONObject obj) {
        Map map;
        map = new TreeMap();
        Iterator<String> it = obj.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            Object value = obj.get(key);
            if (value instanceof JSONObject) {
                // System.out.println(value + " is JSONObject");
                map.put(key, sortJsonObject(JSONObject.parseObject(value)));
            } else if (value instanceof JSONArray) {
                // System.out.println(value + " is JSONArray");
                map.put(key, sortJsonArray(JSONArray.parseObject(value)));
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    *//**
     * JSONArray排序
     *
     * @param array
     * @return
     *//*
    @SuppressWarnings("all")
    public static JSONArray sortJsonArray(JSONArray array) {
        List list = new ArrayList();
        int size = array.size();
        for (int i = 0; i < size; i++) {
            Object obj = array.get(i);
            if (obj instanceof JSONObject) {
                list.add(sortJsonObject(JSONObject.parseObject(obj)));
            } else if (obj instanceof JSONArray) {
                list.add(sortJsonArray(JSONArray.parseObject(obj)));
            } else {
                list.add(obj);
            }
        }

        list.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));
        return JSONArray.parseObject(list);
    }*/
    /**
     * @param args
     */
    public static void main(String[] args) {
        // 读取原始json文件并进行操作和输出
        try {

            File jsonfile = ResourceUtils.getFile("classpath:areaJson/area35.json");
            String fileName = jsonfile.getAbsolutePath();
            BufferedReader br = new BufferedReader(new FileReader(fileName));// 读取原始json文件
            String s, ws = "";
            // System.out.println(s);
            while ((s = br.readLine()) != null) try {
                ws += s;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            br.close();




            JSONObject jsonObject = JSON.parseObject(ws);




            List<Map> jsonMaps = new ArrayList<Map>();

            // Map  lev2 = JSONObject.parseObject( lev2json.toString(), new TypeReference<Map>(){});
            for (String lev : jsonObject.keySet()) {
                //System.out.println("map.get(" + lev + ") = " + jsonObject.get(lev).toString());
                String strClassSimpleName = jsonObject.get(lev).getClass().getSimpleName();//JSONArray
                if (strClassSimpleName.equals("JSONArray")) {
                    JSONArray levArray = JSON.parseArray(jsonObject.get(lev).toString());
                    for (Object obj : levArray) {
                        strClassSimpleName = obj.getClass().getSimpleName();//JSONArray
                        if (strClassSimpleName.equals("String")) {
                            String lev2 = obj.toString();
                            System.out.println("===" + lev2.toString() + "(" + lev + ")");
                        }
                        if (strClassSimpleName.equals("JSONObject")) {
                            JSONObject lev3Object = JSON.parseObject(obj.toString());


                            for (String lev3 : lev3Object.keySet()) {
                                String strClassSimpleNamelev3 = lev3Object.get(lev3).getClass().getSimpleName();//JSONArray


                                if (strClassSimpleNamelev3.equals("JSONArray")) {
                                    JSONArray lev3Array = JSON.parseArray(lev3Object.get(lev3).toString());
                                    for (Object obj3 : lev3Array) {
                                       String strClassSimpleName3 = obj3.getClass().getSimpleName();//JSONArray
                                        if (strClassSimpleName3.equals("String")) {
                                            System.out.println("======" + obj3.toString() + "(" + lev3 + ")");
                                        }
                                        if (strClassSimpleName3.equals("JSONObject")) {
                                                JSONObject lev4Object = JSON.parseObject(obj3.toString());
                                                for (String lev4 : lev4Object.keySet()) {
                                                    String strClassSimpleName4= lev4Object.get(lev4).getClass().getSimpleName();//JSONArray
                                                    System.out.println("=========" + lev4Object.get(lev4).toString() + "(" + lev4 + ")");
                                                }
                                        }

                                    }
                                }
                                if (strClassSimpleNamelev3.equals("String")) {
                                        System.out.println("======" + lev3Object.get(lev3).toString() + "(" + lev3 + ")");
                                }

                            }
                        }
                    }

                }
                if (strClassSimpleName.equals("String")) {
                    System.out.println("======（3）" + jsonObject.get(lev).toString() + "(" + lev + ")=========");
                }
            }


        } catch (   IOException e)

        {

            e.printStackTrace();
        }

    }

}
