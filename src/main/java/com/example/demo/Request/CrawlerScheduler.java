package com.example.demo.Request;

import jakarta.annotation.Resource;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class CrawlerScheduler {
    @Resource
    private Bot bot;
    private JSON_process j = new JSON_process();
    private final String PYTHON_SERVICE_URL = "http://localhost:5000/crawl";
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 10000) // 10 seconds
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
            List<String> imagesStr = (List<String>) data.get("pictures");
            System.out.println("Received text: " + textData);
            for (String imageUrl : imagesStr) {
                System.out.println("Received image URL: " + imageUrl);
                // TODO: Process or save the image URLs as required
            }
            ContactList<Group> groups=bot.getGroups();
            JSONObject g = j.readJsonFromFile("groups.json");
            for(Group group:groups){
                try{
                    g.getBoolean(Long.toString(group.getId()));
                }
                catch (Exception e){

                }
            }

        }

        // Extract and save image data

    }
}


