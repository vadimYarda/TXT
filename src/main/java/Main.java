import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static BlockingQueue<String> maxA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> maxB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> maxC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {

        new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                try {
                    maxA.put(generateText("abc", 100_000));
                    maxB.put(generateText("abc", 100_000));
                    maxC.put(generateText("abc", 100_000));
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        new Thread(() -> {
            MaxTextABC<String, Integer> maxTextABC;
            try {
                maxTextABC = getMax(maxA, 'a');
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Текст с максимальным количеством букв a (" + maxTextABC.getCount() + "):\n" + maxTextABC.getText() + "\n");
        }).start();

        new Thread(() -> {
            MaxTextABC<String, Integer> maxTextABC;
            try {
                maxTextABC = getMax(maxB, 'b');
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Текст с максимальным количеством букв b (" + maxTextABC.getCount() + "):\n" + maxTextABC.getText() + "\n");
        }).start();

        new Thread(() -> {
            MaxTextABC<String, Integer> maxTextABC;
            try {
                maxTextABC = getMax(maxC, 'c');
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Текст с максимальным количеством букв c (" + maxTextABC.getCount() + "):\n" + maxTextABC.getText() + "\n");
        }).start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static MaxTextABC<String, Integer> getMax(BlockingQueue<String> maxABC, char abc) throws InterruptedException {
        MaxTextABC<String, Integer> maxTextABC = new MaxTextABC<>();
        maxTextABC.setCount(0);
        for (int i = 0; i < 10_000; i++) {
            int maxInText = 0;
            String textABC = maxABC.take();
            char[] text = textABC.toCharArray();
            for (char s : text) {
                if (s == abc) {
                    maxInText++;
                }
            }
            if (maxInText > maxTextABC.getCount()) {
                maxTextABC.setCount(maxInText);
                maxTextABC.setText(textABC);
            }
        }
        return maxTextABC;
    }
}