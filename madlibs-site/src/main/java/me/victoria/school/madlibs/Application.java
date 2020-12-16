package me.victoria.school.madlibs;

import com.vaadin.flow.component.page.AppShellConfigurator;

import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.shared.communication.PushMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.vaadin.artur.helpers.LaunchUtil;

import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the * and some desktop browsers.
 *
 */
@SpringBootApplication
@Push(PushMode.MANUAL)
@PWA(name = "MadLibs", shortName = "MadLibs")
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    public static void main(String[] args) {
        for (int i = 0; i < MadLib.madlibs.length(); i++) {
            MadLib.madLibs.add(new MadLib(MadLib.madlibs.getString(i)));
        }
        Collections.sort(MadLib.madLibs);
        ProfanityCheck.load();
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ActivityTracker.checkForInactivity();
            }
        }, 30000L, 10000L);
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(Application.class, args));
    }

}
