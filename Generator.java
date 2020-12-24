package ntust.huiting.random;

import java.util.Random;

/**
 * �üƲ��;�
 * @author �G��߬
 *
 */
public class Generator {
	private Random random = null;
	private long seed = 0;
	
	/**
	 * �غc�l
	 */
	public Generator() {
		random = new Random();
		random.setSeed(seed);
	}
	
	/**
	 * �]�w�üƺؤl
	 * @param seed �üƺؤl
	 */
	public void setSeed(long seed) {
		random.setSeed(seed);
	}
	
	/**
	 * �]�w�������Ƥ��G���ü�
	 * @param mu 1/������
	 * @return �ü�
	 */
	public double getRandomNumber_Exponential(double mu) {
		return -(Math.log(random.nextDouble()) / mu);
	}
	
	/**
	 * �]�w�����y�Q���G���ü�
	 * @param random ������
	 * @return �ü�
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
	 * �]�w����󧡤ä��G���ü�
	 * @param mean ������
	 * @return �ü�
	 */
	public double getRandomNumber_Uniform(double mean) {
		return random.nextDouble() * mean;
	}
	
	/**
	 * �]�w�����`�A���G���ü�
	 * @param mean ������
	 * @return �ü�
	 */
	public double getRandomNumber_Normal(double mean) {
		return random.nextGaussian() * mean;
	}
}
