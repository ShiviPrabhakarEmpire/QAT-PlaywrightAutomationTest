package com.qat.playwright.parity;

import com.microsoft.playwright.Page;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xmlunit.assertj3.XmlAssert;

public class DomParityService {

    public static void compareDom(Page basePage, Page targetPage) {
        String baseHtml = sanitizeHtml(basePage.content());
        String targetHtml = sanitizeHtml(targetPage.content());

        XmlAssert.assertThat(baseHtml)
                .and(targetHtml)
                .ignoreWhitespace()
                .ignoreComments()
                .areSimilar();
    }

    private static String sanitizeHtml(String rawHtml) {
        Document doc = Jsoup.parse(rawHtml);
        
        // Remove transient or dynamic nodes
        doc.select("script").remove();
        doc.select("style").remove();
        doc.select("meta[name*='csrf']").remove();
        doc.select("input[type='hidden']").remove();
        doc.select("iframe").remove();
        doc.select("noscript").remove();
        
        // Set document output syntax to XML for strict comparison by XMLUnit
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        return doc.outerHtml();
    }
}
