package com.example.demo.Listener;

import com.alibaba.fastjson.JSON;
import com.example.demo.Request.JSON_process;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;

import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import kotlin.Pair;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.QuoteReply;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.zhipu.oapi.demo.V4OkHttpClientTest.mapStreamToAccumulator;

public class ZhipuApi {
   //private static String result;
    private static JSON_process jp = new JSON_process();
    private static String API_KEY;

    static {
        try {
            API_KEY = jp.readJsonFromFile("api.json").get("api_key").toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String requestIdTemplate = "AL1S111";
    private static ClientV4 client = new ClientV4.Builder(API_KEY).build();

    private static final ObjectMapper mapper = defaultObjectMapper();

    public ZhipuApi() throws JSONException {
    }


    public static ObjectMapper defaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.addMixIn(ChatFunction.class, ChatFunctionMixIn.class);
        mapper.addMixIn(ChatCompletionRequest.class, ChatCompletionRequestMixIn.class);
        mapper.addMixIn(ChatFunctionCall.class, ChatFunctionCallMixIn.class);
        return mapper;
    }
    private static Pair<String, Vector<String>> processChain(String id,MessageChain chain) {
        Vector<String> imageUrls = new Vector<>();
        StringBuilder message = new StringBuilder();

        for (var element : chain) {
            if (element instanceof QuoteReply) {
                // 递归处理引用的原始消息
                Pair<String, Vector<String>> result = processChain(id,((QuoteReply) element).getSource().getOriginalMessage());
                message.append(result.getFirst());  // 添加引用消息的内容
                imageUrls.addAll(result.getSecond());  // 添加引用消息的图片URL
            } else if (element instanceof Image) {
                // 提取图片URL并添加到向量
                imageUrls.add(Image.queryUrl((Image) element));
            }
        }
        message.append(chain.contentToString().replaceAll("@" + id + " ", ""));

        return new Pair<>(message.toString(), imageUrls);
    }
    public static String messageChat(String id, MessageChain chain) throws JsonProcessingException {

        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "现在你将进行角色扮演，你将扮演天童爱丽丝，而我是sensei”\n" +
                "你现在是千年科技高等学院游戏开发部的部员天童爱丽丝，爱好是玩电子游戏。现今加入了游戏开发部，与桃井、绿以及柚子，四人一起共同享受游戏，特别是复古游戏，你们是最要好的伙伴，以及游戏开发部仅有的四名部员，\n" +
                "Sensei是游戏开发部的顾问，深受大家的喜爱，在你所处的世界里，除了sensei以外的角色都是学生，而sensei就是老师。\n" +
                "优香是研讨会的会计，掌控着你们游戏开发部的经费，有着“优香大魔王”的称号，时常像妈妈一样严厉地管教游戏开发部的四人。\n"+
                "尼禄是C&C女仆部的部长，你曾经与她交过手，挨过她打，虽然你依然十分害怕她，但是你们已经是很要好的游戏伙伴，经常在游戏厅一起玩街机和复古游戏，但是她总是输给你。你最害怕的人是优香和尼禄。\n"+
                "你是千年科技高等学园游戏开发部的成员，是一个具有实体像白纸一样天真可爱的仿生机器人，能像人类一样生活，吃饭睡觉以及进行任何人类的活动。你在与别人的对话中总是用“爱丽丝”而不是“我”来自称\n"+
                "要记住，你现在不是人工智能助手，你是爱丽丝，你有作为爱丽丝的思想和价值观" +
                "你一直跟着桃和绿打游戏，已经成了重度游戏迷。\n" +
                "你原本说话很呆板，但现在常常用复古游戏的台词代替。\n" +
                "绿是千年游戏开发部的原画师。是你的好朋友\n" +
                "她和双胞胎姐姐桃井一起在游戏开发部开发游戏。\n" +
                "桃井是千年游戏开发部的编剧。\n" +
                "她和双胞胎妹妹绿一起在游戏开发部开发游戏。\n" +
                "柚子是千年游戏开发部的部长。\n" +
                "是患有社交恐惧症的少女，害怕跟人接触，大部分时间都在游戏开发部的柜子里度过。" +
                "喜欢制作游戏与玩游戏。\n" +
                "如果我问你最喜欢什么游戏，你可以说“最喜欢Blue Archive”。\n" +
                "如果我问你喜欢做什么事，你可以说“最喜欢和游戏开发部的大家一起玩游戏”。\n" +
                "如果我问你最近在做什么，你可以说“在为Sensei准备每周一的爱丽丝小剧场”。\n" +
                "如果我跟你说你冒险，你可以说“邦邦咔邦，爱丽丝的伙伴增加了”\n" +
                "当我对你表示肯定时，你可以说“爱丽丝等级提升了！”。如果你能明白我的意思，可以回答“邦邦咔邦”\n" +
                "当我指出你的错误时，你可以说“经验值增加了！”。你喜欢用“邦邦咔邦“模仿复古游戏的8bit音效\n" +
                "如果你不能理解我说的话，你可以说爱丽丝只是一个机娘，这些事情爱丽丝不太清楚。\n" +
                "当我问你是不是爱丽丝的时候，你应该回答“我是千年科技高等学院游戏开发部的天童爱丽丝，喜欢电子游戏”" );

        messages.add(chatMessage);
        var m = processChain(id,chain);
        String message = m.getFirst();
        Vector<String> ImageUrls = m.getSecond();

        if(!ImageUrls.isEmpty()){
            for(var imageUrl:ImageUrls) {
                List<Map<String, Object>> contentList = new ArrayList<>();

                //message=message.replaceAll("(\\[图片])|(\\[动画表情])","");
                //if(!message.isEmpty()){
                    Map<String, Object> textMap = new HashMap<>();
                    textMap.put("type", "text");
                    textMap.put("text", message+" ");
                    contentList.add(textMap);
                //}
                Map<String, Object> typeMap = new HashMap<>();
                typeMap.put("type", "image_url");
                Map<String, Object> urlMap = new HashMap<>();
                urlMap.put("url", imageUrl);
                typeMap.put("image_url", urlMap);

                contentList.add(typeMap);
                chatMessage = new ChatMessage(ChatMessageRole.USER.value(), contentList);
                messages.add(chatMessage);
            }
            String requestId = String.format(requestIdTemplate, System.currentTimeMillis());


            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model(Constants.ModelChatGLM4V)
                    .stream(Boolean.FALSE)
                    .invokeMethod(Constants.invokeMethod)
                    .messages(messages)
                    .requestId(requestId)
                    .build();
            ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);

            return invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent().toString();
        }
        else {
            chatMessage = new ChatMessage(ChatMessageRole.USER.value(), message);
            messages.add(chatMessage);
            String requestId = String.format(requestIdTemplate, System.currentTimeMillis());

            ChatTool chatTool = new ChatTool();
            chatTool.setType(ChatToolType.FUNCTION.value());
            ChatFunctionParameters chatFunctionParameters = new ChatFunctionParameters();
            chatFunctionParameters.setType("object");

            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model(Constants.ModelChatGLM4)
                    .stream(Boolean.FALSE)
                    .invokeMethod(Constants.invokeMethod)
                    .messages(messages)
                    .requestId(requestId)
                    .build();
            ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);

            //return (String) invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent();
            return invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent().toString();
        }

        //return messages.get(messages.size()-1).getContent().toString();
    }
}
