package com.example.demo.Listener;

import jakarta.annotation.Resource;
import net.itbaima.robot.service.RobotService;
import net.mamoe.mirai.Bot;
import java.io.File;

import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class BotOnlineMessageSender implements ApplicationRunner {
    @Resource
    private Bot bot;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {


            ContactList<Group> groups = bot.getGroups();
            ExternalResource externalResource = ExternalResource.create(new File(".\\al1s\\立绘.png"));
            boolean send =false;
            if(send) {
                for (Group group : groups) {
                    Image image;
                    image = ExternalResource.uploadAsImage(externalResource, group);
                    group.sendMessage(image.plus("HP回复完成！"));

                }
            }
            externalResource.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



