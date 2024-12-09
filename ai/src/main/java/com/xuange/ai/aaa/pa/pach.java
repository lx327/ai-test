package com.xuange.ai.aaa.pa;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
public class pach {
    public static void main(String[] args) {
        String url = "";
        try {
            // 连接到网页并获取文档
            Document document = Jsoup.connect(url).get();

            // 选择特定的元素，例如所有的链接
            Elements links = document.select("a[href]");

            // 输出所有链接
            for (Element link : links) {
                System.out.println("链接: " + link.attr("href"));
                System.out.println("文本: " + link.text());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
