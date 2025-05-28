import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FlashcardAppWithFeedback {

    private JFrame frame;
    private JLabel questionLabel;
    private JLabel feedbackLabel;
    private JButton nextButton, previousButton, addCardButton, editCardButton, deleteCardButton;
    private ArrayList<Flashcard> flashcards;
    private ArrayList<JButton> optionButtons;
    private int currentIndex = 0;

    public FlashcardAppWithFeedback() {
        flashcards = new ArrayList<>();
        flashcards.add(new Flashcard("What is the capital of France?", "Paris", new String[]{"London", "Berlin", "Paris", "Madrid"}));
        flashcards.add(new Flashcard("What is 2 + 2?", "4", new String[]{"3", "4", "5", "6"}));
        flashcards.add(new Flashcard("What is the boiling point of water?", "100°C or 212°F", new String[]{"90°C", "100°C or 212°F", "110°C", "120°C"}));

        frame = new JFrame("Flashcard Quiz App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        feedbackLabel = new JLabel("", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        optionButtons = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            JButton optionButton = new JButton();
            optionButtons.add(optionButton);
            optionsPanel.add(optionButton);
            int finalI = i; // Required for lambda expressions
            optionButton.addActionListener(e -> checkAnswer(finalI));
        }

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        nextButton = new JButton("Next");
        previousButton = new JButton("Previous");
        addCardButton = new JButton("Add Card");
        editCardButton = new JButton("Edit Card");
        deleteCardButton = new JButton("Delete Card");

        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(addCardButton);
        buttonPanel.add(editCardButton);
        buttonPanel.add(deleteCardButton);

        frame.add(questionLabel, BorderLayout.NORTH);
        frame.add(optionsPanel, BorderLayout.CENTER);
        frame.add(feedbackLabel, BorderLayout.SOUTH);
        frame.add(buttonPanel, BorderLayout.PAGE_END);

        updateFlashcardDisplay();

        // Add action listeners
        nextButton.addActionListener(e -> nextFlashcard());
        previousButton.addActionListener(e -> previousFlashcard());
        addCardButton.addActionListener(e -> addFlashcard());
        editCardButton.addActionListener(e -> editFlashcard());
        deleteCardButton.addActionListener(e -> deleteFlashcard());

        frame.setVisible(true);
    }

    private void updateFlashcardDisplay() {
        if (!flashcards.isEmpty()) {
            Flashcard currentFlashcard = flashcards.get(currentIndex);
            questionLabel.setText("Q: " + currentFlashcard.getQuestion());
            String[] options = currentFlashcard.getOptions();
            for (int i = 0; i < optionButtons.size(); i++) {
                if (i < options.length) {
                    optionButtons.get(i).setText(options[i]);
                    optionButtons.get(i).setEnabled(true);
                    optionButtons.get(i).setBackground(null);
                } else {
                    optionButtons.get(i).setText("");
                    optionButtons.get(i).setEnabled(false);
                }
            }
            feedbackLabel.setText("");
        } else {
            questionLabel.setText("No flashcards available.");
            feedbackLabel.setText("");
            for (JButton button : optionButtons) {
                button.setText("");
                button.setEnabled(false);
            }
        }
    }

    private void checkAnswer(int selectedIndex) {
        Flashcard currentFlashcard = flashcards.get(currentIndex);
        String selectedOption = optionButtons.get(selectedIndex).getText();
        if (selectedOption.equals(currentFlashcard.getAnswer())) {
            feedbackLabel.setText("Correct!");
            optionButtons.get(selectedIndex).setBackground(Color.GREEN);
        } else {
            feedbackLabel.setText("Incorrect. Correct Answer: " + currentFlashcard.getAnswer());
            optionButtons.get(selectedIndex).setBackground(Color.RED);
            for (int i = 0; i < optionButtons.size(); i++) {
                if (optionButtons.get(i).getText().equals(currentFlashcard.getAnswer())) {
                    optionButtons.get(i).setBackground(Color.GREEN);
                }
            }
        }
        for (JButton button : optionButtons) {
            button.setEnabled(false);
        }
    }

    private void nextFlashcard() {
        if (!flashcards.isEmpty()) {
            currentIndex = (currentIndex + 1) % flashcards.size();
            updateFlashcardDisplay();
        }
    }

    private void previousFlashcard() {
        if (!flashcards.isEmpty()) {
            currentIndex = (currentIndex - 1 + flashcards.size()) % flashcards.size();
            updateFlashcardDisplay();
        }
    }

    private void addFlashcard() {
        String question = JOptionPane.showInputDialog(frame, "Enter the question:");
        String answer = JOptionPane.showInputDialog(frame, "Enter the answer:");
        String optionsStr = JOptionPane.showInputDialog(frame, "Enter options (comma-separated):");
        if (question != null && answer != null && optionsStr != null) {
            String[] options = optionsStr.split(",");
            flashcards.add(new Flashcard(question, answer, options));
        }
    }

    private void editFlashcard() {
        if (!flashcards.isEmpty()) {
            Flashcard currentFlashcard = flashcards.get(currentIndex);
            String question = JOptionPane.showInputDialog(frame, "Edit the question:", currentFlashcard.getQuestion());
            String answer = JOptionPane.showInputDialog(frame, "Edit the answer:", currentFlashcard.getAnswer());
            String optionsStr = JOptionPane.showInputDialog(frame, "Edit options (comma-separated):", String.join(", ", currentFlashcard.getOptions()));
            if (question != null && answer != null && optionsStr != null) {
                currentFlashcard.setQuestion(question);
                currentFlashcard.setAnswer(answer);
                currentFlashcard.setOptions(optionsStr.split(","));
                updateFlashcardDisplay();
            }
        }
    }

    private void deleteFlashcard() {
        if (!flashcards.isEmpty()) {
            flashcards.remove(currentIndex);
            if (flashcards.isEmpty()) {
                currentIndex = 0;
            } else {
                currentIndex = currentIndex % flashcards.size();
            }
            updateFlashcardDisplay();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FlashcardAppWithFeedback::new);
    }

    class Flashcard {
        private String question;
        private String answer;
        private String[] options;

        public Flashcard(String question, String answer, String[] options) {
            this.question = question;
            this.answer = answer;
            this.options = options;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String[] getOptions() {
            return options;
        }

        public void setOptions(String[] options) {
            this.options = options;
        }
    }
}