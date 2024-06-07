package view;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {
	public JTextField pField = new JTextField();
	public JTextField alphaField = new JTextField();
	public JTextField aField = new JTextField();
	public JTextField betaField = new JTextField();
	public JTextField kField = new JTextField();
	public JTextField yField = new JTextField();
	public JButton generateBtn = new JButton("sinh khoá ngẫu nhiên");
	public JTextArea textContentArea = new JTextArea();
	String[] hashAlgorithms = { "UNICODE", "MD5", "SHA-1", "SHA-256", "SHA-384", "SHA-512" };
	public JComboBox<String> hashComboBox = new JComboBox<>(hashAlgorithms); // select type hash signature
	public JTextArea signatureResultArea = new JTextArea(5, 20); // result after signature
	public JButton chooseFileToSig = new JButton("Chọn file");// choose file to signature
	public JButton signatureBtn = new JButton("Ký");// signature
	public JButton send = new JButton("Chuyển tiếp kết quả");
	public JTextArea verifyTextContentArea = new JTextArea();// input to verify
	public JComboBox<String> verifyHashComboBox = new JComboBox<>(hashAlgorithms); // select type verify signature
	public JButton verifyBtn = new JButton("Tiến hành kiểm tra chữ ký"); // verify button

	public View() {
		setTitle("Chữ ký số điện tử - ElGamal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 600);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new GridLayout(1, 3));

		JPanel createKeyPanel = new JPanel(new GridLayout(10, 2, 5, 5));
		createKeyPanel.setBorder(BorderFactory.createTitledBorder("TẠO KHÓA"));

		createKeyPanel.add(new JLabel("Số nguyên tố p:"));
		createKeyPanel.add(pField);

		createKeyPanel.add(new JLabel("Số (alpha) α:"));
		createKeyPanel.add(alphaField);

		createKeyPanel.add(
				new JLabel("Số β = α^a mod p: "));
		createKeyPanel.add(betaField);

		createKeyPanel.add(new JLabel("Số nguyên a ="));
		createKeyPanel.add(aField);

		createKeyPanel.add(new JLabel("Số ngẫu nhiên k ="));
		createKeyPanel.add(kField);

		createKeyPanel.add(new JLabel("Số y = α^k mod p = "));
		createKeyPanel.add(yField);

		createKeyPanel.add(generateBtn);

		mainPanel.add(createKeyPanel);

		JPanel generateSignaturePanel = new JPanel(new BorderLayout(5, 5));
		generateSignaturePanel.setBorder(BorderFactory.createTitledBorder("PHÁT SINH CHỮ KÝ"));

		JPanel topPanel = new JPanel(new GridLayout(3, 1, 5, 5));

		topPanel.add(new JLabel("Nội dung văn bản:"));
		topPanel.add(new JScrollPane(textContentArea));

		topPanel.add(new JLabel("Hàm băm:"));
		topPanel.add(hashComboBox);

		generateSignaturePanel.add(topPanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));

		JLabel signatureResultLabel = new JLabel("Kết quả sau ký:");
		signatureResultArea.setLineWrap(true);
		signatureResultArea.setWrapStyleWord(true);
		signatureResultArea.setEditable(false);
		JScrollPane signatureResultScrollPane = new JScrollPane(signatureResultArea);

		bottomPanel.add(signatureResultLabel, BorderLayout.NORTH);
		bottomPanel.add(signatureResultScrollPane, BorderLayout.CENTER);

		JPanel bottomButtonsPanel = new JPanel();
		bottomButtonsPanel.add(chooseFileToSig);
		bottomButtonsPanel.add(signatureBtn);
		bottomButtonsPanel.add(send);
		bottomPanel.add(bottomButtonsPanel, BorderLayout.SOUTH);

		generateSignaturePanel.add(bottomPanel, BorderLayout.SOUTH);

		mainPanel.add(generateSignaturePanel);

		JPanel verifySignaturePanel = new JPanel(new BorderLayout(5, 5));
		verifySignaturePanel.setBorder(BorderFactory.createTitledBorder("KIỂM TRA CHỮ KÝ"));

		JPanel verifyTopPanel = new JPanel(new GridLayout(3, 1, 5, 5));
		verifyTopPanel.add(new JLabel("Điền (S1,S2)-plain text :"));
		verifyTopPanel.add(new JScrollPane(verifyTextContentArea));

		verifyTopPanel.add(new JLabel("Hàm băm:"));
		verifyTopPanel.add(verifyHashComboBox);

		verifySignaturePanel.add(verifyTopPanel, BorderLayout.CENTER);

		JPanel verifyBottomPanel = new JPanel();
		verifyBottomPanel.add(verifyBtn);

		verifySignaturePanel.add(verifyBottomPanel, BorderLayout.SOUTH);

		mainPanel.add(verifySignaturePanel);

		add(mainPanel);

		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new View());
	}
}
