import javax.swing.*;
import java.awt.*;

public class GUI {
    private JFrame window;
    private JPanel questionPanel, buttonPanel, topPanel;
    private JLabel scoreLabel, logoLabel;
    private JTextArea questionText;
    private JButton trueButton, falseButton;
    private QuizBrain quiz;
    private final Color THEME_COLOR = new Color(55, 83, 98); // #375362

    // Add constants
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 650;
    private static final int FEEDBACK_DELAY = 1000; // milliseconds

    public GUI(QuizBrain quizBrain) {
        this.quiz = quizBrain;

        // Window setup
        this.window = new JFrame("Quizzy");
        this.window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.setLayout(new BorderLayout());
        this.window.getContentPane().setBackground(THEME_COLOR);
        this.window.setResizable(false);

        // Top panel for logo and score
        this.topPanel = new JPanel();
        this.topPanel.setLayout(new BorderLayout());
        this.topPanel.setBackground(THEME_COLOR);
        this.window.add(this.topPanel, BorderLayout.NORTH);

        // Logo
        this.logoLabel = new JLabel();
        this.logoLabel.setIcon(loadImage("/Users/mac/Documents/JAVA/Quiz/images/logo.png", WINDOW_WIDTH, WINDOW_WIDTH/2));
        this.logoLabel.setHorizontalAlignment(JLabel.CENTER);
        this.topPanel.add(this.logoLabel, BorderLayout.NORTH);

        // Score label below logo with padding
        this.scoreLabel = new JLabel("Score: 0", JLabel.CENTER);
        this.scoreLabel.setForeground(Color.WHITE);
        this.scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        this.scoreLabel.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        this.topPanel.add(this.scoreLabel, BorderLayout.SOUTH);

        // Question panel
        this.questionPanel = new JPanel();
        this.questionPanel.setBackground(Color.WHITE);
        this.questionPanel.setLayout(new BorderLayout());
        this.questionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.window.add(this.questionPanel, BorderLayout.CENTER);

        this.questionText = new JTextArea("Some Question Text");
        this.questionText.setLineWrap(true);
        this.questionText.setWrapStyleWord(true);
        this.questionText.setFont(new Font("Arial", Font.ITALIC, 20));
        this.questionText.setEditable(false);
        this.questionText.setBackground(Color.WHITE);
        this.questionText.setForeground(THEME_COLOR);
        this.questionText.setMargin(new Insets(20, 10, 20, 10));
        this.questionText.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.questionPanel.add(this.questionText, BorderLayout.CENTER);

        // Button panel
        this.buttonPanel = new JPanel();
        this.buttonPanel.setLayout(new GridLayout(1, 2, 20, 0));
        this.buttonPanel.setBackground(Color.decode("#0097b2"));
        this.buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // padding around buttons

        this.trueButton = new JButton(loadImage("/Users/mac/Documents/JAVA/Quiz/images/true.png", 80, 80));
        this.trueButton.setBorderPainted(false); // removes border
        this.trueButton.setContentAreaFilled(false); // makes button background transparent
        this.buttonPanel.add(this.trueButton);

        this.falseButton = new JButton(loadImage("/Users/mac/Documents/JAVA/Quiz/images/false.png", 80, 80));
        this.falseButton.setBorderPainted(false); // removes border
        this.falseButton.setContentAreaFilled(false); // makes button background transparent
        this.buttonPanel.add(this.falseButton);
        this.window.add(this.buttonPanel, BorderLayout.SOUTH);

        // Button actions
        this.trueButton.addActionListener(e -> {
            boolean isRight = quiz.check_answer("True");
            giveFeedback(isRight);
        });

        this.falseButton.addActionListener(e -> {
            boolean isRight = quiz.check_answer("False");
            giveFeedback(isRight);
        });

        // Show first question
        getNewQuestion();

        this.window.setVisible(true);
    }

    // method to show final score
    private void showFinalScore() {
        String finalMessage = String.format(
            "Quiz Complete!\nYour final score: %d/%d", 
            quiz.getScore(), 
            quiz.getTotalQuestions()
        );
        questionText.setText(finalMessage);
        
        // Add restart button
        JButton restartButton = new JButton("Restart Quiz");
        restartButton.addActionListener(e -> restartQuiz());
        buttonPanel.removeAll();
        buttonPanel.add(restartButton);
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    // Add method to restart quiz
    private void restartQuiz() {
        quiz = new QuizBrain();
        setupButtons();
        getNewQuestion();
    }

    private void setupButtons() {
        buttonPanel.removeAll();
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 0));
        
        // Recreate true button
        this.trueButton = new JButton(loadImage("/Users/mac/Documents/JAVA/Quiz/images/true.png", 80, 80));
        this.trueButton.setBorderPainted(false); // removes border
        this.trueButton.setContentAreaFilled(false);
        this.trueButton.addActionListener(e -> {
            boolean isRight = quiz.check_answer("True");
            giveFeedback(isRight);
        });
        
        // Recreate false button
        this.falseButton = new JButton(loadImage("/Users/mac/Documents/JAVA/Quiz/images/false.png", 80, 80));
        this.falseButton.setBorderPainted(false); // removes border
        this.falseButton.setContentAreaFilled(false); // makes button background transparent
        this.falseButton.addActionListener(e -> {
            boolean isRight = quiz.check_answer("False");
            giveFeedback(isRight);
        });
        
        buttonPanel.add(trueButton);
        buttonPanel.add(falseButton);
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    // Modify getNewQuestion method
    private void getNewQuestion() {
        this.questionPanel.setBackground(Color.WHITE);
        this.scoreLabel.setText("Score: " + this.quiz.getScore());

        if (this.quiz.stillHasQuestion()) {
            String qText = this.quiz.nextQuestion();
            this.questionText.setText(qText);
        } else {
            showFinalScore();
        }
    }

    private void giveFeedback(boolean isRight) {
        if (isRight) {
            this.questionPanel.setBackground(Color.GREEN);
        } else {
            this.questionPanel.setBackground(Color.RED);
        }
        Timer timer = new Timer(FEEDBACK_DELAY, e -> getNewQuestion());
        timer.setRepeats(false);
        timer.start();
    }

    private ImageIcon loadImage(String path, int width, int height) {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
    }
}
