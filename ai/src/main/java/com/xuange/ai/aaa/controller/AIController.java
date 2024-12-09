package com.xuange.ai.aaa.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.*;

import static com.xuange.ai.aaa.common.Tread.getTreadPool;
//xuange
@RestController
@CrossOrigin
@Component
@RequestMapping("ChatResponse")
public class AIController {
    @Autowired
    private  OpenAiChatModel chatModel;
    @Autowired
    private ChatClient chatClient;


     @PostMapping("/chatWithGPT-3.5-TURBO")
     public String chatResponse(@RequestBody String input) {
         ThreadPoolExecutor treadPool = getTreadPool
                 (10, 20, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
         CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
             ChatResponse call = chatModel
                     .call(new Prompt(
                             input,
                             OpenAiChatOptions.builder()
                                     .build()
                     ));
             return call.getResult().getOutput().getContent();
         }, treadPool);
         try {
             return stringCompletableFuture.get();
         } catch (InterruptedException | ExecutionException e) {
             throw new RuntimeException(e);
         }

     }


}
//                 .stream(new Prompt(
//                 input,
//                 OpenAiChatOptions.builder()
//
//
//                         .build()));
//                        .withModel("gpt-3.5")
//                        .withTemperature(0.4f)
//        return  this.chatClient.prompt()
//                .user(message)
//                .call()
//                .content();