package ru.stankin.test.holders;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import ru.stankin.Configs;
import ru.stankin.test.model.Answer;
import ru.stankin.test.model.Question;
import ru.stankin.test.model.Test;
import ru.stankin.utils.Rnd;
import ru.stankin.utils.Util;
import ru.stankin.utils.files.CipherFileStreamFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by DisDev on 05.02.2016.
 */
public class QuestionsHolder {
    private static QuestionsHolder ourInstance = new QuestionsHolder();
    private final TIntObjectMap<Question> questions;

    private QuestionsHolder() {
        questions = new TIntObjectHashMap<>();
        load();
    }

    public static QuestionsHolder getInstance() {
        return ourInstance;
    }

    public List<Question> getRndListOfQuestion() {
        List<Question> copy = new LinkedList<>(questions.valueCollection());
        final int maXQuestions = Configs.Test.maxQuestions();
        List<Question> toShows = new ArrayList<>(maXQuestions);
        while (!copy.isEmpty() && toShows.size() < maXQuestions) {
            int size = copy.size();
            Question question = size == 1 ? copy.remove(0) : copy.remove(Rnd.get(size));
            toShows.add(question);
        }
        return toShows;
    }

    private void load() {
        try {
            File file = new File("resources/ex_data.bin");
            if(!file.exists())
                throw new FileNotFoundException("Data file is not found!");
            SAXReader reader = new SAXReader();
            reader.setIgnoreComments(true);

            Document document = reader.read(CipherFileStreamFactory.getInstance().getSafeFileInputStream(file));
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


}
