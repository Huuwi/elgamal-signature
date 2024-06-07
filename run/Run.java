package run;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import controller.Controller;
import model.Model;
import view.View;

public class Run {
	public static String validInput = "";

	public boolean valid(Model m, View view, Controller c) {
		boolean checkLog = false;
		try {
			m.p = Long.valueOf(view.pField.getText());
			m.alpha = Long.valueOf(view.alphaField.getText());
			m.a = Long.valueOf(view.aField.getText());
			m.beta = Long.valueOf(view.betaField.getText());
			m.k = Long.valueOf(view.kField.getText());
			m.y = Long.valueOf(view.yField.getText());
		} catch (Exception er) {
			JOptionPane.showMessageDialog(view, "đầu vào tạo khóa không hợp lệ!");
			Run.validInput = "!!";
			return false;
		}
		if (!c.isPrime(m.p)) {
			JOptionPane.showMessageDialog(view, "p không phải số nguyên tố!!");
			Run.validInput = "!!";
		}
		if (!c.isPrimitiveRoot(m.alpha, m.p)) {
			JOptionPane.showMessageDialog(view, "alpha không phải căn nguyên thủy của p");
			Run.validInput = "!!";
		}

		if (m.a > m.p - 1 || m.a == m.p - 1) {
			if (Run.validInput == "") {
				JOptionPane.showMessageDialog(view,
						"a phải nhỏ hơn p - 1 ! Chương trình đã tự chọn 1 số nguyên a hợp lệ");
				checkLog = true;
				Run.validInput = "!!";
			}
			m.a = m.p - 2;
		}
		if (m.k > m.p - 1 || m.k < 0 || c.gcd(m.k, m.p - 1) != 1) {
			JOptionPane.showMessageDialog(view,
					"k phải : 0 <= k <= p - 1 và nguyên tố cùng nhau với p - 1 ! Chương trình đã tự chọn 1 số nguyên k hợp lệ");
			checkLog = true;
			m.k = c.generateCoprimePrime(m.p - 1);
			Run.validInput = "!!";
		}
		if (!c.validMod(m.beta, m.alpha, m.a, m.p)) {
			JOptionPane.showMessageDialog(view, "giá trị mod beta không hợp lệ , chương trình đã tự động sửa lại!");
			Run.validInput = "!!";
		}
		m.beta = c.mod(m.alpha, m.a, m.p);
		if (!c.validMod(m.y, m.alpha, m.k, m.p)) {
			JOptionPane.showMessageDialog(view, "giá trị mod y không hợp lệ , chương trình đã tự động sửa lại!");
			Run.validInput = "!!";
		}
		m.y = c.mod(m.alpha, m.k, m.p);
		view.betaField.setText(String.valueOf(m.beta));
		view.yField.setText(String.valueOf(m.y));
		view.kField.setText(String.valueOf(m.k));
		view.aField.setText(String.valueOf(m.a));
		if (Run.validInput == "") {
			// JOptionPane.showMessageDialog(view, "đầu vào tạo khóa hợp lệ");
			return true;
		} else {
			if (checkLog) {
				JOptionPane.showMessageDialog(view, "vui lòng click lại thêm 1 lần nữa");
			}
			Run.validInput = "";
			return false;
		}
	}

	public static void main(String[] args) {
		View view = new View();
		Model m = new Model();
		Controller c = new Controller();
		Run r = new Run();

		view.generateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.pField.setText(String.valueOf(c.generateRandomPrime()));
				view.alphaField
						.setText(String.valueOf(c.generateRandomPrimitiveRoot(Integer.valueOf(view.pField.getText()))));
				view.aField.setText(String.valueOf(Integer.valueOf(view.pField.getText()) - 3));
				view.kField.setText(String.valueOf(c.generateCoprimePrime(Integer.valueOf(view.pField.getText()))));
				view.betaField.setText(String.valueOf(c.mod(Integer.valueOf(view.alphaField.getText()),
						Integer.valueOf(view.aField.getText()), Integer.valueOf(view.pField.getText()))));
				view.yField.setText(String.valueOf(c.mod(Integer.valueOf(view.alphaField.getText()),
						Integer.valueOf(view.kField.getText()), Integer.valueOf(view.pField.getText()))));
			}

		});

		view.chooseFileToSig.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog(view.chooseFileToSig);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					Model.filePathToSig = selectedFile.toPath().toString();
					view.textContentArea.setText(c.readFileByPath(Model.filePathToSig));
					m.text = view.textContentArea.getText();
				}
			}

		});

		view.signatureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!r.valid(m, view, c)) {
					return;
				}
				if (view.textContentArea.getText().toString().length() == 0) {
					JOptionPane.showMessageDialog(view,
							"vui lòng nhập vào nội dung cần ký");
					return;
				}
				r.valid(m, view, c);
				m.S1 = m.y;
				String contentFileHash = c.hashString(view.textContentArea.getText(),
						view.hashComboBox.getSelectedItem().toString());
				BigInteger hashed = new BigInteger(contentFileHash, 16);//
				long t1 = c.modInverse(m.k, m.p - 1);// chuan
				BigInteger temp = hashed.subtract(new BigInteger(String.valueOf(m.a * m.S1)));
				long t2 = Long.valueOf(temp.mod(new BigInteger(String.valueOf(m.p - 1))).toString(10));
				m.S2 = (t1 * t2) % (m.p - 1);
				view.signatureResultArea
						.setText("(S1 , S2) = " + "(" + m.S1 + " , " + m.S2 + ")" + " hashText : " + hashed);
			}
		});

		view.send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.text = view.textContentArea.getText();
				view.verifyTextContentArea.setText("(" + m.S1 + " , " + m.S2 + ")" + "-" + m.text);
			}
		});

		view.verifyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!r.valid(m, view, c)) {
					return;
				}
				String regex = "\\(\\d+,\\d+\\)-(?s).+";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern
						.matcher(view.verifyTextContentArea.getText().replaceAll(" ", ""));
				if (!matcher.matches()) {
					JOptionPane.showMessageDialog(view,
							"đầu vào phải có định dạng (S1,S2)-Văn bản cần xác nhận chữ ký");
					return;
				}

				String text = view.verifyTextContentArea.getText();
				String content = text.substring(text.indexOf("-") + 1);
				text = text.replaceAll(" ", "");
				m.S1 = Long.valueOf(text.substring(text.indexOf("(") + 1, text.indexOf(",")));
				m.S2 = Long.valueOf(text.substring(text.indexOf(",") + 1, text.indexOf(")")));
				long t1 = c.mod(m.beta, m.S1, m.p);
				long t2 = c.mod(m.S1, m.S2, m.p);
				m.V2 = (t1 * t2) % m.p;
				String hashContent = c.hashString(content, view.verifyHashComboBox.getSelectedItem().toString());
				BigInteger hashed = new BigInteger(hashContent, 16);

				m.V1 = Long.valueOf(new BigInteger(String.valueOf(m.alpha)).modPow(hashed,
						new BigInteger(String.valueOf(m.p))).toString(10));
				if (m.V1 == m.V2) {
					JOptionPane.showMessageDialog(view, "chữ ký hợp lệ");
				} else {
					JOptionPane.showMessageDialog(view, "chữ ký không hợp lệ");
				}
			}
		});

	}
}
