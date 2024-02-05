package com.example.demo.Request;

import jakarta.annotation.Resource;
import net.mamoe.mirai.Bot;

import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@Component
public class CrawlerScheduler {
    @Resource
    private Bot bot;
    private JSON_process j = new JSON_process();
    private final String PYTHON_SERVICE_URL = "http://localhost:8081/crawl";
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 600000) // 600 seconds
    public void fetchDataFromPythonService() throws JSONException {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                PYTHON_SERVICE_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}




        );

        Map<String, Object> data = response.getBody();
        if((boolean)data.get("update")) {
            // Extract text data
            String textData = (String) data.get("text");

            //System.out.println("Received text: " + textData);

            List<String> imagesStr = (List<String>) data.get("pictures");




            ContactList<Group> groups=bot.getGroups();
            JSONObject g = j.readJsonFromFile("groups.json");

            Vector<byte[]> images = image_from_url(imagesStr);
            for(Group group:groups){
                try{

                    if(g.getBoolean(Long.toString(group.getId()))){

                        MessageChain messageChain = build_with_img(textData,images,group);  // 构建消息链
                        group.sendMessage(messageChain);  // 发送包含文本和图片的消息链

                    }
                }
                catch (Exception e){
                    //e.printStackTrace();
                }
            }
        }
        // Extract and save image data

    }
    private Vector<byte[]> image_from_url(List<String> imagesStr){
        Vector<byte[]> images=new Vector<byte[]>();
        for (String imageUrl : imagesStr) {
            //System.out.println("Received image URL: " + imageUrl);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(imageUrl)
                    .build();


            try (Response response1 = client.newCall(request).execute()) {

                if (response1.isSuccessful()) {
                    byte[] imageBytes = response1.body().bytes();
                    images.add(imageBytes);
                } else {
                    System.out.println("Failed to download image from URL: " + imageUrl);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error processing image URL: " + imageUrl);
            }
        }
        return images;
    }
    private MessageChain build_with_img(String text, Vector<byte[]> images , Group group) throws IOException {
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        OkHttpClient client = new OkHttpClient();
        messageChainBuilder.add(text);  // 添加文本数据到消息链
        for (byte[] imageByte : images) {
            ExternalResource externalResource = ExternalResource.create(imageByte);
            Image image = group.uploadImage(externalResource);
            messageChainBuilder.add(image);  // 将图片添加到消息链中
            externalResource.close();  // 释放资源

        }
        return messageChainBuilder.build();
    }
}


