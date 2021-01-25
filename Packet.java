package ntust.huiting.edgecomputing.v2;

/**
 * packet (task)
 * @author hui
 */
public class Packet {
	final private int queue_number = EmulatorParameters.queue_number;
	//final private double constant_deadline_time = EmulatorParameters.constant_deadline_time;
	final private int service_number = EmulatorParameters.service_number;
	
	
	public double arrival_time[] = new double[queue_number]; // arrival_time
	public double service_time[] = new double[queue_number]; //  service_time
	public double departure_time[] = new double[queue_number]; //  departure_time
	public double waiting_time[] = new double[queue_number]; //  waiting_time
	public int waiting_queue_length[] = new int[queue_number]; //  waiting_queue_length
	public int waiting_queue_length_scheduled[] = new int[queue_number]; //  waiting_queue_length after scheduling
	public int packet_index = 0; // packet_index
	public int service_type = 0; // service_type
	public double r_task_size = 0;// 任務大小(UE傳輸)
	public double w_task_size = 0; //封包大小(Edge運算)
	public double o_task_size = 0; //封包大小(EU傳輸)
	public double deadline_time = 0; //  deadline_time
	public double constant_deadline_time[] = new double[service_number];//  deadline_time
	
	/**
	 * Constructor
	 * @param packet_index 
	 * @param service_type 
	 * @param arrival_time 
	 * @param service_time 
	 */
	public Packet(int packet_index, int service_type, double r_task_size,double w_task_size,double o_task_size, double arrival_time_gen, double service_time_gen[], double deadline_time_gen) {
		this.packet_index = packet_index;
		this.service_type = service_type;
		for(int k = 0; k < queue_number; k++) {
			arrival_time[k] = 0;
			service_time[k] = service_time_gen[k];
			departure_time[k] = 0;
			waiting_time[k] = 0;
			waiting_queue_length[k] = 0;
			waiting_queue_length_scheduled[k] = 0;
		}
		constant_deadline_time[service_type] = 0;
		arrival_time[0] = arrival_time_gen;
		deadline_time = deadline_time_gen;
		this.r_task_size = r_task_size;
		this.w_task_size = w_task_size;
		this.o_task_size = o_task_size;
	}
	
	public Packet() {		
	}
	
	/**
	 * Get packet information
	 * @return packet information
	 */
	public String info() {
		StringBuffer buffer = new StringBuffer("\n");
		for(int i = 0; i < queue_number; i++) {
			buffer.append("---Queue_").append((i+1)).append("---\n");
			buffer.append("封包編號: ").append(packet_index).append('\n');
			buffer.append("服務類型: ").append(service_type).append('\n');
			buffer.append("r, w, o: ").append(r_task_size +", "+w_task_size+", "+o_task_size).append('\n');
			buffer.append("到達時間: ").append(arrival_time[i]).append('\n');
			buffer.append("服務時間: ").append(service_time[i]).append('\n');
			buffer.append("完成時間: ").append(departure_time[i]).append('\n');
			buffer.append("截止時間: ").append(deadline_time).append('\n');
			buffer.append("等候時間: ").append(waiting_time[i]).append('\n');
			buffer.append("等候長度: ").append(waiting_queue_length[i]).append('\n');
			buffer.append("排程後等候長度: ").append(waiting_queue_length_scheduled[i]).append('\n');
		}
		buffer.append("===========\n");
		
		return buffer.toString();
	}
}
