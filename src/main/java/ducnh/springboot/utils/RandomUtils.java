package ducnh.springboot.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class RandomUtils {

	public static int randomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	public String randCheckinCode() {
		int rand = randomInt(0,9999);
		return String.format("%04d", rand);
	}
}
