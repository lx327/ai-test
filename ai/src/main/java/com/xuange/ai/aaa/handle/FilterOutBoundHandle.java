package com.xuange.ai.aaa.handle;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//xuange
class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    List<Integer> output = new ArrayList<>();
    TrieNode fail;
    static int dadadadad = 0;

    public TrieNode() {
        fail = null;
    }
}

class AhoCorasick {
    private TrieNode root;

    public AhoCorasick() {
        root = new TrieNode();
    }

    // 插入敏感词
    public void insert(String word, int index) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.output.add(index);
        TrieNode.dadadadad++;
    }

    // 构建失败指针
    public void buildFailPointers() {
        List<TrieNode> queue = new ArrayList<>();
        for (TrieNode child : root.children.values()) {
            child.fail = root;
            queue.add(child);
        }

        while (!queue.isEmpty()) {
            TrieNode current = queue.remove(0);
            for (Map.Entry<Character, TrieNode> entry : current.children.entrySet()) {
                char c = entry.getKey();
                TrieNode child = entry.getValue();
                TrieNode failNode = current.fail;

                while (failNode != null && !failNode.children.containsKey(c)) {
                    failNode = failNode.fail;
                }

                if (failNode == null) {
                    child.fail = root;
                } else {
                    child.fail = failNode.children.get(c);
                    child.output.addAll(child.fail.output);
                }

                queue.add(child);
            }
        }
    }

    // 过滤字符串
    public String filter(String text) {
        StringBuilder result = new StringBuilder();
        TrieNode node = root;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            while (node != null && !node.children.containsKey(c)) {
                node = node.fail;
            }

            if (node == null) {
                node = root;
                result.append(c);
                continue;
            }

            node = node.children.get(c);

            if (!node.output.isEmpty()) {
                result.append("*".repeat(node.output.size())); // 用*替代敏感词
                node = root; // 回到根节点
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}



public class FilterOutBoundHandle extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            CharSequence sequence = byteBuf.readCharSequence(byteBuf.readableBytes(), StandardCharsets.UTF_8);
            String string1 = sequence.toString();
            String filter = this.filter(string1);
            byteBuf = Unpooled.copiedBuffer(filter,   StandardCharsets.UTF_8);
            ctx.write(byteBuf,promise);
        } catch (Exception e){
            ctx.close();
        } finally {

            byteBuf.release();
        }

    }
    private String filter(String s){
        AhoCorasick ac = new AhoCorasick();

        ac.insert("狗日", TrieNode.dadadadad);

        ac.buildFailPointers();
        return ac.filter(s);
    }

}
