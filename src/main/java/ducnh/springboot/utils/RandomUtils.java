package ducnh.springboot.utils;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class RandomUtils {

	public static int randomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	public String randCheckinCode() {
		StringBuilder code = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			code.append(randomInt(0, 9));
		}
		return code.toString();
	}
}
