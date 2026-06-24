package com.dogstore.dogsellingapp.controller;

import com.dogstore.dogsellingapp.service.ChatService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping(value = "/chat/ui", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String chatUi() {
        return """
                <div id="chat-widget" style="position:fixed;bottom:90px;right:20px;width:320px;background:white;border-radius:12px;box-shadow:0 4px 20px rgba(0,0,0,0.15);z-index:1000;">
                  <div style="background:#e07b1a;color:white;padding:12px 16px;border-radius:12px 12px 0 0;font-weight:600;">
                    AussiePaw Assistant
                    <span onclick="document.getElementById('chat-widget').remove()" style="float:right;cursor:pointer;">✕</span>
                  </div>
                  <div id="chat-messages" style="height:250px;overflow-y:auto;padding:12px;font-size:14px;"></div>
                  <div style="padding:10px;border-top:1px solid #eee;display:flex;gap:8px;">
                    <input id="chat-input" type="text" placeholder="Ask about breeds..." style="flex:1;padding:8px;border:1px solid #ddd;border-radius:6px;font-size:13px;"/>
                    <button onclick="sendChat()" style="background:#e07b1a;color:white;border:none;padding:8px 12px;border-radius:6px;cursor:pointer;">Send</button>
                  </div>
                </div>
                <script>
                function sendChat() {
                  const input = document.getElementById('chat-input');
                  const messages = document.getElementById('chat-messages');
                  const text = input.value.trim();
                  if (!text) return;
                  messages.innerHTML += '<div style="margin:6px 0;text-align:right;"><span style="background:#e07b1a;color:white;padding:4px 10px;border-radius:12px;font-size:13px;">'+text+'</span></div>';
                  input.value = '';
                  fetch('/api/chat', {method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify({text})})
                    .then(r=>r.json())
                    .then(d=>{messages.innerHTML+='<div style="margin:6px 0;"><span style="background:#f0f0f0;padding:4px 10px;border-radius:12px;font-size:13px;">'+d.text+'</span></div>';messages.scrollTop=messages.scrollHeight;});
                }
                document.getElementById('chat-input').addEventListener('keypress', e=>{ if(e.key==='Enter') sendChat(); });
                </script>
                """;
    }

    @PostMapping(path = "/api/chat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ChatReply> chat(@RequestBody ChatMessage message) {
        String reply = chatService.reply(message.getText());
        ChatReply response = new ChatReply();
        response.setText(reply);
        response.setTimestamp(Instant.now().toString());
        return ResponseEntity.ok(response);
    }

    @Data
    public static class ChatMessage { private String text; }

    @Data
    public static class ChatReply { private String text; private String timestamp; }
}