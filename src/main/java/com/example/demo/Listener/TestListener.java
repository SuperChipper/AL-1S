package com.example.demo.Listener;

import net.itbaima.robot.event.RobotListener;

import net.itbaima.robot.event.RobotListenerHandler;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

import java.util.regex.*;

import java.io.File;
import net.mamoe.mirai.message.data.MessageChain;

@RobotListener
public class TestListener {
    @RobotListenerHandler
    public void LoginSuccessHandler(BotOnlineEvent event){
        System.out.print(event);
    }
    @RobotListenerHandler
    public void FriendMessageHandler(FriendMessageEvent event){
        try {
            String message = event.getMessage().contentToString();
            if(message.equals("爱丽丝")) {
                Contact friend = event.getFriend();
                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\立绘.png"));
                Image image = ExternalResource.uploadAsImage(externalResource, friend);
                friend.sendMessage(image.plus(new PlainText("1")));
                externalResource.close();
            }
        // 关闭资源
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @RobotListenerHandler
    public void GroupMessageHandler(GroupMessageEvent event){
        try {
            Bot bot = event.getBot();
            MessageChain messageChain = event.getMessage();
            String message = event.getMessage().contentToString();
            Contact group = event.getGroup();
            if(message.equals("爱丽丝")||Pattern.matches("邦邦咔邦.+",message)){

                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\表情3.png"));
                Image image = ExternalResource.uploadAsImage(externalResource, group);
                group.sendMessage(image.plus(new PlainText("邦邦咔邦！")));
                externalResource.close();
            }
            if (Pattern.matches(".*啥比",message)||Pattern.matches(".*傻逼.*",message)){
                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\表情8.png"));
                Image image = ExternalResource.uploadAsImage(externalResource, group);
                group.sendMessage(image);
                externalResource.close();
            }
            if (Pattern.matches(".*妹抖",message)||Pattern.matches(".*女仆.*",message)){
                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\表情7.png"));
                Image image = ExternalResource.uploadAsImage(externalResource, group);
                group.sendMessage(image.plus("メイド勇者です！"));
                externalResource.close();
            }
            if (message.equals("@"+bot.getId())){
                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\表情2.png"));
                Image image = ExternalResource.uploadAsImage(externalResource, group);
                group.sendMessage(image);
                externalResource.close();
            }
            if (Pattern.matches(".*魔法.*",message)){
                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\l2d_small.png"));
                group.sendMessage(ExternalResource.uploadAsImage(externalResource, group).plus("爱丽丝不觉得基沃托斯没有魔法"));
                externalResource.close();
            }
            if (Pattern.matches(".*启动.+",message)){
                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\启动.png"));
                group.sendMessage(ExternalResource.uploadAsImage(externalResource, group));
                externalResource.close();
            }
            if (Pattern.matches(".*工作时间.+",message)){
                ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\ac6.jpg"));
                group.sendMessage(ExternalResource.uploadAsImage(externalResource, group));
                externalResource.close();
            }
            //System.out.println("@"+bot.getId());

        }catch(Exception e){
            e.printStackTrace();
        }
    }


}


