package Parser;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import model.Flower;

/**
 * Created by ananth on 8/16/2016.
 */
public class XML_Parser {

    public static List<Flower> parseFeed(String content){

        try {
            boolean inDataItemTag = false;
            String currentTagName = "";
            Flower flower = null;
            List<Flower> flowerList = new ArrayList<>();

            XmlPullParserFactory ParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser Parser = ParserFactory.newPullParser();
            Parser.setInput(new StringReader(content));

            int eventType = Parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTagName = Parser.getName();
                        if (currentTagName.equals("product")) {
                            inDataItemTag = true;
                            flower = new Flower();
                            flowerList.add(flower);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (Parser.getName().equals("product")) {
                            inDataItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    case XmlPullParser.TEXT:
                        if (inDataItemTag && flower != null) {
                            switch (currentTagName) {
                                case "productId":
                                    flower.setProductId(Integer.parseInt(Parser.getText()));
                                    break;
                                case "category":
                                    flower.setCategory(Parser.getText());
                                    break;
                                case "name":
                                    flower.setName(Parser.getText());
                                    break;
                                case "instructions":
                                    flower.setInstructions(Parser.getText());
                                    break;
                                case "price":
                                    flower.setPrice(Double.parseDouble(Parser.getText()));
                                    break;
                                case "photo":
                                    flower.setPhoto(Parser.getText());
                                    break;
                                //default:
                                //  ;
                            }
                        }
                        break;
                }
                eventType = Parser.next();
            }
            return  flowerList;

        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return  null;

    }
}
