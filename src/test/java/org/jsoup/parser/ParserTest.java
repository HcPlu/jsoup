package org.jsoup.parser;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 Tests for the Parser

 @author Jonathan Hedley, jonathan@hedley.net */
public class ParserTest {

    @Test public void testParsesSimpleDocument() {
        TokenStream tokenStream = TokenStream.create("<html><head><title>First!</title></head><body><p>First post! <img src=\"foo.png\" /></p></body></html>");
        Parser parser = new Parser(tokenStream);
        Document doc = parser.parse();
        // need a better way to verify these:
        Element p = doc.child(1).child(0);
        assertEquals("p", p.tagName());
        Element img = p.child(0);
        assertEquals("foo.png", img.attr("src"));
        assertEquals("img", img.tagName());
    }

    @Test public void testParsesRoughAttributes() {
        TokenStream tokenStream = TokenStream.create("<html><head><title>First!</title></head><body><p class=\"foo > bar\">First post! <img src=\"foo.png\" /></p></body></html>");
        Parser parser = new Parser(tokenStream);
        Document doc = parser.parse();
        // need a better way to verify these:
        Element p = doc.child(1).child(0);
        assertEquals("p", p.tagName());
        assertEquals("foo > bar", p.attr("class"));
    }

    @Test public void testParsesComments() {
        TokenStream ts = TokenStream.create("<html><head></head><body><!-- <table><tr><td></table> --><p>Hello</p></body></html>");
        Document doc = new Parser(ts).parse();
        Element body = doc.child(1);
        Comment comment = (Comment) body.childNode(0);
        assertEquals("<table><tr><td></table>", comment.getData());
        Element p = body.child(0);
        TextNode text = (TextNode) p.childNode(0);
        assertEquals("Hello", text.getWholeText());
    }
}