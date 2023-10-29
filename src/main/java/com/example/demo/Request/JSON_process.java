package com.example.demo.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class JSON_process {
    public String toggleGroupFeature(String groupId) throws JSONException {
        JSONObject data = readJsonFromFile("groups.json");
        try{
            boolean send= data.getBoolean(groupId);
            if(send){
                data.put(groupId,false);
                saveJsonToFile("groups.json", data);
                return "资讯功能关闭!";
            }
            else{
                data.put(groupId,true);
                saveJsonToFile("groups.json", data);
                return "资讯功能开启!";
            }
        }
        catch (Exception e){
            System.out.println(groupId+" firstly initialize.");
            data.put(groupId,true);
        }
        saveJsonToFile("groups.json", data);


        // Save the updated data to the file

        return "Sensei，出错了！";
    }
    public JSONObject readJsonFromFile(String filename) throws JSONException {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                // If the file doesn't exist, create an initial JSON object
                return new JSONObject().put("groups", new JSONArray());
            }

            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();

            String jsonString = new String(data, StandardCharsets.UTF_8);
            return new JSONObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public void saveJsonToFile(String filename, JSONObject jsonObject) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
