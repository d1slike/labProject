package ru.stankin.test.holders;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import ru.stankin.test.TestController;
import ru.stankin.enums.AnswerType;
import ru.stankin.test.model.Answer;
import ru.stankin.test.model.Question;
import ru.stankin.utils.Rnd;

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
            File file = new File("resources/tFrame.xml");
            if(!file.exists())
                throw new FileNotFoundException("source file not found");
            SAXReader reader = new SAXReader();
            reader.setIgnoreComments(true);
            Document document = reader.read(file);
            Element element = document.getRootElement();
            if(!element.getName().equals("qlist"))
                throw new InvalidObjectException("source file is bad");
            for (Element question : element.elements("question")) {
                int qId = parseIntValue(question, "id");
                int correctAnswer = parseIntValue(question, "correctAnswerId");
                TIntObjectMap<Answer> answers = new TIntObjectHashMap<>();
                for (Element answer : question.elements("answer")) {
                    int aId = parseIntValue(answer, "id");
                    AnswerType type = AnswerType.valueOf(answer.attributeValue("type"));
                    if(type == null)
                        throw new InvalidObjectException("bad answer type in " + qId + " answerId " + aId);
                    answers.put(aId, new Answer(aId, type, answer.attributeValue("source")));
                }
                questions.put(qId, new Question(qId, question.attributeValue("text"), correctAnswer, answers));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
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

    public List<QuestionToShow> getRndListOfQuestion() {
        List<Question> copy = new LinkedList<>(questions.valueCollection());
        List<QuestionToShow> toShows = new ArrayList<>(TestController.MAX_QUESTIONS);
        while (!copy.isEmpty() && toShows.size() < TestController.MAX_QUESTIONS) {
            int size = copy.size();
            Question question = size == 1 ? copy.remove(0) : copy.remove(Rnd.get(size));
            toShows.add(new QuestionToShow(question));
        }
        return toShows;
    }

    public static class QuestionToShow {
        public final int correctAnswer;
        public final String text;
        public final List<Answer> answers;

        public QuestionToShow(Question question) {
            this.correctAnswer = question.getCorrectAnswerId();
            this.text = question.getText();
            this.answers = question.shakeAnswers();
        }
    }


}
