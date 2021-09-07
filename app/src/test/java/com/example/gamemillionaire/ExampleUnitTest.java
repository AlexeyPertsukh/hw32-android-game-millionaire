package com.example.gamemillionaire;

import com.example.gamemillionaire.question.Question;
import com.example.gamemillionaire.question.QuestionException;
import com.example.gamemillionaire.question.QuestionFabric;
import com.example.gamemillionaire.question.QuestionFabricException;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    class TestList {
        public ArrayList<String> list;
        public boolean correct;

        public TestList(ArrayList<String> list, boolean correct) {
            this.list = list;
            this.correct = correct;
        }
    }

    @Test
    public void testQuestionFabric_createFromCsv() {

        ArrayList<String> listErr = new ArrayList<>();
        listErr.add("1;2;3;4");
        ArrayList<String> listOk = new ArrayList<>();
        listOk.add("1;2;3;4;5");

        ArrayList<String> listErr1 = new ArrayList<>();
        listErr1.add("1,2;3;4;5");

        List<TestList> listTest = new ArrayList<>();
        listTest.add(new TestList(listErr, false));
        listTest.add(new TestList(listOk, true));
        listTest.add(new TestList(listErr1, false));

        for (int i = 0; i < listTest.size(); i++) {
            TestList item =  listTest.get(i);
            boolean correct;
            try {
                ArrayList<Question> listQuestion = QuestionFabric.createFromCsv(item.list);
                correct = true;
                System.out.println(i + ". " + "convert ok");
            } catch (QuestionFabricException ex) {
                System.out.println(i + ". " + ex.getMessage());
                correct = false;
            }
            assertEquals(item.correct, correct);
        }
    }


    @Test
    public void testQuestionFabric_createFromJson() {

        ArrayList<String> listErr = new ArrayList<>();
        listErr.add("{error}");
        ArrayList<String> listOk = new ArrayList<>();
        listOk.add("{\"strQuestion\":\"Как называется традиционная народная русская забава \\\"Взятие снежного... \\\"?\",\"correctAnswer\":\"Городка\",\"wrongAnswers\":[\"Дворца\",\"Дома\",\"Человека\"]}");

        ArrayList<String> listErr1 = new ArrayList<>();
        listErr1.add("{\"strQuestion\":\"Как называется традиционная народная русская забава \\\"Взятие снежного... \\\"?\",\"correctAnswer\":\"Городка\",\"wrongAnswers\":[\"Дворца\",\"Дома\"]}");

        List<TestList> listTest = new ArrayList<>();
        listTest.add(new TestList(listErr, false));
        listTest.add(new TestList(listOk, true));
        listTest.add(new TestList(listErr1, false));

        for (int i = 0; i < listTest.size(); i++) {
            TestList item =  listTest.get(i);
            boolean correct;
            try {
                ArrayList<Question> listQuestion = QuestionFabric.createFromJson(item.list);
                correct = true;
                System.out.println(i + ". " + "convert ok");
            } catch (QuestionFabricException ex) {
                System.out.println(i + ". " + ex.getMessage());
                correct = false;
            }
            assertEquals(item.correct, correct);
        }
    }


    class TestStorageQuestion {
        String[] stringsWrongAnswer;
        boolean correct;

        public TestStorageQuestion(String[] stringsWrongAnswer, boolean err) {
            this.stringsWrongAnswer = stringsWrongAnswer;
            this.correct = err;
        }
    }


    @Test
    public void testQuestion() {

        List<TestStorageQuestion> listTest = new ArrayList<>();

        listTest.add( new TestStorageQuestion(new String[]{"1","2"}, false));
        listTest.add( new TestStorageQuestion(new String[]{"1","2","3"}, true));
        listTest.add( new TestStorageQuestion(new String[]{"1","2","3","4","5"}, false));
        listTest.add( new TestStorageQuestion(new String[]{"1"}, false));
        boolean correct;

        for (int i = 0; i < listTest.size(); i++) {
            TestStorageQuestion testStorageQuestion = listTest.get(i);

            try {
                Question question = new Question("","",testStorageQuestion.stringsWrongAnswer);
                System.out.println(i + ". create question ok");
                correct = true;
            } catch (QuestionException ex) {
                System.out.println(i + ". " + ex.getMessage());
                correct = false;
            }
            assertEquals(testStorageQuestion.correct, correct);
        }

    }


}