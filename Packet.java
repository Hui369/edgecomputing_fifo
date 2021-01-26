package ntust.huiting.edgecomputing.v2;

/**
 * 封包類別
 * @author hui
 *
 */
public class Packet {
	final private int queue_number = EmulatorParameters.queue_number;	
	public double arrival_time[] = new double[queue_number]; // 到達時間
	public double service_time[] = new double[queue_number]; //  服務時間
	public double departure_time[] = new double[queue_number]; //  完成時間
	public double waiting_time[] = new double[queue_number]; //  等候時間
	public int waiting_queue_length[] = new int[queue_number]; //  等候長度
	
	/**
	 * 建構子
	 * @param arrival_time 到達時間
	 * @param service_time 服務時間
	 */
	public Packet(double arrival_time_gen, double service_time_gen[]) {
		for(int i = 0; i < queue_number; i++) {
			arrival_time[i] = 0;
			service_time[i] = service_time_gen[i];
			departure_time[i] = 0;
			waiting_time[i] = 0;
			waiting_queue_length[i] = 0;
		}
		arrival_time[0] = arrival_time_gen;
	}
	
	/**
	 * 取得封包資訊
	 * @return 封包資訊
	 */
	public String info() {
		StringBuffer buffer = new StringBuffer("\n");
		for(int i = 0; i < queue_number; i++) {
			buffer.append("---Queue_").append((i+1)).append("---\n");
			buffer.append("到達時間: ").append(arrival_time[i]).append('\n');
			buffer.append("服務時間: ").append(service_time[i]).append('\n');
			buffer.append("完成時間: ").append(departure_time[i]).append('\n');
			buffer.append("等候時間: ").append(waiting_time[i]).append('\n');
			buffer.append("等候長度: ").append(waiting_queue_length[i]).append('\n');
		}
		buffer.append("===========\n");
		
		return buffer.toString();
	}
}
