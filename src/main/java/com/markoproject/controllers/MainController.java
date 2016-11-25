package com.markoproject.controllers;

import com.markoproject.objects.SearchPropertiesWrapper;
import com.markoproject.service.ThreadManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Marko on 22.11.2016.
 */
@Controller
@SessionAttributes({"manager", "isPaused"})
public class MainController {
//Controller for work with ajax functions.


//This method invok by timer in succes 0of start controller
    @RequestMapping(value = "/updateDatas", method = RequestMethod.PUT)
    public
    @ResponseBody
    String updateDatas(HttpSession session, Map<String, Object> model) {
        ThreadManager threadManager = (ThreadManager) session.getAttribute("manager");//get manager from session
String result=threadManager.getResults().toString().replaceAll("[=_]", "_");
        if((Boolean)session.getAttribute("isPaused")){//if user pres pause send to controller message for stopping timer
            return "{\"msg\":\"paused \"}";
        }
       else if(((ThreadPoolExecutor) threadManager.getFixedPool()).getActiveCount() == 0&&threadManager.getFixedPool().isShutdown()){//if user pres pause send to controller message for stopping timer and final results
            return "{\"msg\":\"ok\",\"result\":\""+result+"\",\"loaded\":\"" + threadManager.getLoaded() + "\"}";
        } else{//sent to ajax result and loader factor
            return "{\"msg\":\""+result+"\",\"loaded\":\"" + threadManager.getLoaded() + "\"}";
        }

    }
//if user press pause
    @RequestMapping(value = "/pause", method = RequestMethod.PUT)
    public
    @ResponseBody
    String pause(HttpSession session, Map<String, Object> model) {
        ThreadManager threadManager = (ThreadManager) session.getAttribute("manager");
        if (threadManager != null && !threadManager.getFixedPool().isShutdown()) {//if threads are scanning
            threadManager.getFixedPool().shutdown();//scanners whose are scanning -scan to the and
            Boolean isPaused = true;
            model.put("isPaused", isPaused);
        }
        return "{\"msg\":\"OK\"}";
    }
//if user press "stop" - stop fixedPool immediately
    @RequestMapping(value = "/stop", method = RequestMethod.PUT)
    public
    @ResponseBody
    String stop(HttpSession session) {
        ThreadManager threadManager = (ThreadManager) session.getAttribute("manager");
        if (threadManager != null && !threadManager.getFixedPool().isShutdown()) {
            threadManager.getFixedPool().shutdownNow();

        }
        return "{\"msg\":\"OK\"}";
    }


//if user press start
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public
    @ResponseBody
    String start(@RequestBody SearchPropertiesWrapper searchPropertiesWrapper, Map<String, Object> model, HttpSession session) {
        Boolean isPaused = (Boolean) session.getAttribute("isPaused");//get from session flag
        ThreadManager threadManager = (ThreadManager) session.getAttribute("manager");//get from session threadManager
        if (threadManager == null ) {//if its a first action
            threadManager = new ThreadManager();
            isPaused = false;
            model.put("isPaused", isPaused);
            model.put("manager", threadManager);
            threadManager.startScanning(searchPropertiesWrapper);
            return "{\"msg\":\"start scanning\"}";
        } else if (isPaused) {//if its action afte pause
            threadManager.continueScanning();
            isPaused = false;
            model.put("isPaused", isPaused);
            return "{\"msg\":\"continue scanning\"}";
        } else   {//if its action duaring scanning
            if((!threadManager.getFixedPool().isShutdown())){
                threadManager.getFixedPool().shutdownNow();
            }
            threadManager = new ThreadManager();
            model.put("manager", threadManager);
            threadManager.startScanning(searchPropertiesWrapper);
            return "{\"msg\":\"start scanning\"}";
        }

    }
}
