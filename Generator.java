package ntust.huiting.random;

import java.util.Random;

/**
 * 亂數產生器
 * @author 鄭蕙葶
 *
 */
public class Generator {
	private Random random = null;
	private long seed = 0;
	
	/**
	 * 建構子
	 */
	public Generator() {
		random = new Random();
		random.setSeed(seed);
	}
	
	/**
	 * 設定亂數種子
	 * @param seed 亂數種子
	 */
	public void setSeed(long seed) {
		random.setSeed(seed);
	}
	
	/**
	 * 設定為基於指數分佈的亂數
	 * @param mu 1/平均值
	 * @return 亂數
	 */
	public double getRandomNumber_Exponential(double mu) {
		return -(Math.log(random.nextDouble()) / mu);
	}
	
	/**
	 * 設定為基於泊松分佈的亂數
	 * @param random 平均值
	 * @return 亂數
	 */
	public double getRandomNumber_Poisson(double lambda) {
		double L = Math.exp(-lambda);
        int k = 0;
        double p = 1.0;
        do {
            k++;
            p = p * random.nextDouble();
        } while (p > L);

        return k - 1;
	}
	
	/**
	 * 設定為基於均勻分佈的亂數
	 * @param mean 平均值
	 * @return 亂數
	 */
	public double getRandomNumber_Uniform(double mean) {
		return random.nextDouble() * mean;
	}
	
	/**
	 * 設定為基於常態分佈的亂數
	 * @param mean 平均值
	 * @return 亂數
	 */
	public double getRandomNumber_Normal(double mean) {
		return Math.abs(random.nextGaussian() * mean);
	}
}
