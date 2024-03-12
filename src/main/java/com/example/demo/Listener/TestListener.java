package com.example.demo.Listener;

import net.itbaima.robot.event.RobotListener;

import net.itbaima.robot.event.RobotListenerHandler;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

import com.example.demo.Request.JSON_process;
import java.util.regex.*;

import java.io.File;



@RobotListener
public class TestListener {
    private ChatGPT Chat=new ChatGPT();
    private final int maxTokenLength=30000;
    private static int tokenCount;
    private JSON_process j = new JSON_process();
    @RobotListenerHandler
    public void LoginSuccessHandler(BotOnlineEvent event){
        System.out.print(event);
    }
    @RobotListenerHandler
    public void FriendMessageHandler(FriendMessageEvent event){
        //try {
            //String message = event.getMessage().contentToString();
            //Chat.PromptGPT(message);

        // 关闭资源
        //} catch (Exception e) {
            //e.printStackTrace();
        //}

    }
    @RobotListenerHandler
    public void GroupMessageHandler(GroupMessageEvent event){
        try {

            Bot bot = event.getBot();

            String message = event.getMessage().contentToString();
            message = message.replaceAll("\n","");

            Contact group = event.getGroup();
            if(message.equals("爱丽丝")||Pattern.matches("邦邦咔邦.?",message)){

                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\表情3.png"));
                Image image = ExternalResource.uploadAsImage(externalResource, group);
                group.sendMessage(image.plus(new PlainText("邦邦咔邦！")));
                externalResource.close();
            }
            else if(message.equals("操所有人")||Pattern.matches("全部.?飞",message)){

                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\草飞.gif"));
                Image image = ExternalResource.uploadAsImage(externalResource, group);
                group.sendMessage(image);
                externalResource.close();
            }
            else if (Pattern.matches(".*啥比.?",message)||Pattern.matches(".*傻逼.*",message)||Pattern.matches(".*笨蛋.*",message)){

                if(Math.random()>0.5) {
                    ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\表情8.png"));
                    group.sendMessage(ExternalResource.uploadAsImage(externalResource, group));
                    externalResource.close();
                }
                else{
                    ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\挨骂.jpg"));
                    group.sendMessage(ExternalResource.uploadAsImage(externalResource, group));
                    externalResource.close();
                }
            }
            else if (Pattern.matches(".*妹抖",message)||Pattern.matches(".*女仆.*",message)){
                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\表情7.png"));
                Image image = ExternalResource.uploadAsImage(externalResource, group);
                group.sendMessage(image.plus("メイド勇者です！"));
                externalResource.close();
            }
            else if (Pattern.matches("悲",message)||Pattern.matches(".*落泪",message)){
                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\做不到.jpg"));
                Image image = ExternalResource.uploadAsImage(externalResource, group);
                group.sendMessage(image);
                externalResource.close();
            }
            if (Pattern.matches(".*"+bot.getId()+".*",message)){
                //String s=".*"+bot.getId()+".*";
                String m=message.replaceAll("@"+bot.getId()+" ","");
                if(!Chat.is_init()){
                    Chat.promtInit();
                }

                m=Chat.PromptGPT(m,false);

                group.sendMessage(m);
            }
            if (Pattern.matches(".*魔法.?",message)){
                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\l2d_small.png"));
                group.sendMessage(ExternalResource.uploadAsImage(externalResource, group).plus("爱丽丝不觉得基沃托斯没有魔法"));
                externalResource.close();
            }
            if (Pattern.matches(".*启动.?",message)){
                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\启动.png"));
                group.sendMessage(ExternalResource.uploadAsImage(externalResource, group));
                externalResource.close();
            }
            if (Pattern.matches(".*工作时间.?",message)){
                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\ac6.jpg"));
                group.sendMessage(ExternalResource.uploadAsImage(externalResource, group));
                externalResource.close();
            }
            if (Pattern.matches("不要卷啦.?",message)||Pattern.matches("别卷.+",message)){
                if(Math.random()>0.5) {
                    ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\别卷了.jpg"));
                    group.sendMessage(ExternalResource.uploadAsImage(externalResource, group));
                    externalResource.close();
                }
                else{
                    ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\打人了.jpg"));
                    group.sendMessage(ExternalResource.uploadAsImage(externalResource, group));
                    externalResource.close();
                }
            }
            if (Pattern.matches(".*大佬.?",message)){
                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\带带我.jpg"));
                group.sendMessage(ExternalResource.uploadAsImage(externalResource, group));
                externalResource.close();
            }
            if (Pattern.matches(".*色色.?",message)||Pattern.matches(".*涩涩.?",message)||Pattern.matches(".*奈奈.?",message)||Pattern.matches(".*奈子.?",message)||Pattern.matches(".*抽象.?",message)){
                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\思考.jpg"));
                group.sendMessage(ExternalResource.uploadAsImage(externalResource, group));
                externalResource.close();
            }
            if (message.equals("资讯功能")) {
                String groupId = Long.toString(group.getId());
                group.sendMessage(j.toggleGroupFeature(groupId));
            }
            if(message.equals("help")){
                group.sendMessage("爱丽丝的小贴士：\n" +
                        "@我 + 想说的内容可以与我聊天哦\n" +
                        "但是Sensei，爱丽丝没有记忆，所以请一次把话讲完哦。\n" +
                        "发送\"资讯功能\"可以获取碧蓝档案资讯站最新消息\n" +
                        "不要忘了打总力战哦，Sensei");
            }
            //System.out.println("@"+bot.getId());

        }catch(Exception e){
            e.printStackTrace();
        }
    }




}



