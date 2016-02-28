package ru.stankin.test.holders;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import ru.stankin.test.model.Answer;
import ru.stankin.test.model.Question;
import ru.stankin.test.model.Test;
import ru.stankin.utils.Rnd;
import ru.stankin.utils.Util;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by DisDev on 05.02.2016.
 */
public class QuestionsHolder {
    private static QuestionsHolder ourInstance = new QuestionsHolder();

    public static QuestionsHolder getInstance() {
        return ourInstance;
    }

    private final TIntObjectMap<Question> questions;

    private QuestionsHolder() {
        questions = new TIntObjectHashMap<>();
        load();
    }

    private void load() {
        try {
            File file = new File("resources/ex_data.bin");
            if(!file.exists())
                throw new FileNotFoundException("Data file is not found!");
            SAXReader reader = new SAXReader();
            reader.setIgnoreComments(true);
            SecretKeySpec keySpec = new SecretKeySpec("jcnhjdcrjuj18".getBytes("UTF-8"), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            InputStream inputStream = new CipherInputStream(new FileInputStream(file), cipher);
            Document document = reader.read(inputStream);
            Element element = document.getRootElement();
            if(!element.getName().equals("qlist"))
                throw new InvalidObjectException("source file is bad");
            for (Element question : element.elements("question")) {
                int qId = parseIntValue(question, "id");
                int correctAnswer = parseIntValue(question, "correctAnswerId");
                TIntObjectMap<Answer> answers = new TIntObjectHashMap<>();
                for (Element answer : question.elements("answer")) {
                    int aId = parseIntValue(answer, "id");
                    answers.put(aId, new Answer(aId, answer.attributeValue("imgSource"), answer.attributeValue("text")));
                }
                questions.put(qId, new Question(qId, question.attributeValue("imgSource"), question.attributeValue("text"), correctAnswer, answers));
            }

        } catch (Exception ex) {
            Util.showProgramsFilesSpoiled();
        }
    }

    private int parseIntValue(Element element, String attributeName) {
        int value = 0;
        try {
            value = Integer.parseInt(element.attributeValue(attributeName));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public List<Question> getRndListOfQuestion() {
        List<Question> copy = new LinkedList<>(questions.valueCollection());
        List<Question> toShows = new ArrayList<>(Test.MAX_QUESTIONS);
        while (!copy.isEmpty() && toShows.size() < Test.MAX_QUESTIONS) {
            int size = copy.size();
            Question question = size == 1 ? copy.remove(0) : copy.remove(Rnd.get(size));
            toShows.add(question);
        }
        return toShows;
    }


}
