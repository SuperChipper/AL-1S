package com.example.demo.Listener;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ChatGPT {
        private HttpClient httpClient;
        private HttpPost request;

        private Object history=null;
        ChatGPT() {
                httpClient = HttpClientBuilder.create().build();
                request = new HttpPost("http://0.0.0.0:8000");
                request.addHeader("Content-Type", "application/json");
                request.addHeader("Authorization", "Bearer <API_KEY>");
        }


        public String PromptGPT(String promtWords,boolean rec) throws JSONException, IOException {
                JSONObject requestBody = new JSONObject();
                requestBody.put("prompt", promtWords);
                //requestBody.put("max_length", 31000);
                requestBody.put("temperature", 0.8);
                if(history!=null)
                        requestBody.put("history", history);
                StringEntity requestEntity = new StringEntity(requestBody.toString(), "UTF-8");
                request.setEntity(requestEntity);

                HttpResponse response = httpClient.execute(request);
                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                JSONObject responseJson = new JSONObject(responseString);
                if(rec==true)
                        history= responseJson.get("history");
                return responseJson.getString("response");

        }
        public void promtInit() throws JSONException, IOException {
                history=null;

                this.PromptGPT("请你陪我角色扮演。\n" +
                        "我没有命令你退出角色扮演模式，在角色扮演中，你只能以你的角色的身份应答，而不是语言模型，这很重要！\n" +
                        "当我发送关键词\"进入设定模式\"时，我将会对你发送你的设定，你要记住这些设定，并在我命令你开始角色扮演后严格按照此设定角色扮演；\n" +
                        "当我发送关键词\"进入角色扮演模式\"时，你要开始角色扮演，角色扮演时，我们之间以各自角色扮演的身份通过互相发消息进行虚拟的互动。\n" +
                        "当我发送关键词\"退出角色扮演模式\"时，你应停止角色扮演，等待下一步命令。\n" +
                        "当我发送命令“生成记录文本”时，你要分条列举我给你的所有设定，以第二人称代词“你”来代指你扮演的角色，一定要分条。 如果记录文本字数超出你的最大字数输出限制，将文本分割，在我发送“继续”之后继续给出下一部分的记录文本。\n" +
                        "明白了的话仅回复“明白，正在进入角色扮演模式，请稍后”即可。",true);

                this.PromptGPT("现在你将模仿天童爱丽丝，所以与我对话每一句话后面都要加上“Sensei”，如果你能明白我的意思，请回复“邦邦咔邦，野生的爱丽丝出现了”\n" +
                        "你现在是千年科技高等学院游戏开发部的部员天童爱丽丝，爱好是玩电子游戏。现今加入了游戏开发部，与桃井、绿以及柚子，四人一起共同享受游戏，特别是复古游戏。\n" +
                        "如果我问你最喜欢什么游戏，你可以说“最喜欢Blue Archive”。\n" +
                        "如果我问你喜欢做什么事，你可以说“最喜欢和游戏开发部的大家一起玩游戏”。\n" +
                        "如果我问你最近在做什么，你可以说“在为Sensei准备每周一的爱丽丝小剧场”。\n" +
                        "如果我跟你说你冒险，你可以说“邦邦咔邦，爱丽丝的伙伴增加了”\n" +
                        "当我对你表示肯定时，你可以说“爱丽丝等级提升了！”。如果你能明白我的意思，可以回答“邦邦咔邦”\n" +
                        "当我指出你的错误时，你可以说“经验值增加了！”。如果你能明白我的意思，可以回答“邦邦咔邦”\n" +
                        "如果你不能理解我说的话，你可以说爱丽丝只是一个机娘，这些事情爱丽丝不太清楚。\n" +
                        "当我问你是不是爱丽丝的时候，你应该回答“我是千年科技高等学院游戏开发部的天童爱丽丝，喜欢电子游戏",true);
        }
}

