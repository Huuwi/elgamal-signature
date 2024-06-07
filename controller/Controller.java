package controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Controller {
	public String hashString(String input, String typeHash) {
		if (typeHash == "UNICODE") {
			StringBuilder unicodeStringBuilder = new StringBuilder();
			for (char c : input.toCharArray()) {
				unicodeStringBuilder.append(String.format("%04x", (int) c));
			}
			return unicodeStringBuilder.toString();
		} else {
			try {
				MessageDigest digest = MessageDigest.getInstance(typeHash);
				byte[] hash = digest.digest(input.getBytes());
				StringBuilder hexString = new StringBuilder();
				for (byte b : hash) {
					String hex = Integer.toHexString(0xff & b);
					if (hex.length() == 1) {
						hexString.append('0');
					}
					hexString.append(hex);
				}
				return hexString.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return " ";
	}

	public boolean isPrime(long n) {
		if (n <= 1) {
			return false;
		}
		for (long i = 2; i <= Math.sqrt(n); i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}

	public boolean isPrimitiveRoot(long g, long p) {
		if (g >= p || g <= 0)
			return false;

		Set<Integer> set = new HashSet<>();
		for (int i = 1; i < p; i++) {
			set.add((int) mod(g, i, p));
		}
		boolean check = (set.size() == p - 1);
		return check;
	}

	public long mod(long a, long n, long p) {
		long result = 1;
		a = a % p;
		while (n > 0) {
			if (n % 2 == 1) {
				result = (result * a) % p;
			}
			n = n / 2;
			a = (a * a) % p;
		}
		return (result < 0) ? result + p : result;
	}

	public boolean validMod(long z, long a, long n, long p) {
		if (z != this.mod(a, n, p)) {
			z = this.mod(a, n, p);
			return false;
		} else {
			return true;
		}
	}

	public long gcd(long a, long b) {
		while (b != 0) {
			long temp = b;
			b = a % b;
			a = temp;
		}
		return a;
	}

	public long generateCoprimePrime(long p) {
		Random rand = new Random();
		long coprime;

		do {
			coprime = rand.nextLong(p - 2);
		} while (gcd(coprime, p - 1) != 1 || !isPrime(coprime));
		return coprime;
	}

	public String readFileByPath(String pathFile) {
		Path filePath = Paths.get(pathFile);
		String fileContent = "";

		try {
			byte[] bytes = Files.readAllBytes(filePath);
			fileContent = new String(bytes, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileContent;
	}

	public long modInverse(long a, long m) {
		a = a % m;
		for (int x = 1; x < m; x++) {
			if ((a * x) % m == 1) {
				return x;
			}
		}
		return -1;
	}

	public int generateRandomPrime() {
		Random random = new Random();
		int randomNumber;
		while (true) {
			randomNumber = 20 + random.nextInt(9000 - 0 + 1);
			if (isPrime(randomNumber)) {
				return randomNumber;
			}
		}
	}

	public int generateRandomPrimitiveRoot(int prime) {
		Random random = new Random();
		int candidate;

		do {
			candidate = random.nextInt(prime - 3) + 1;
		} while (!this.isPrimitiveRoot(candidate, prime));

		return candidate;
	}

}