package ntust.huiting.edgecomputing;

//import ntust.huiting.edgecomputing.EmulatorParameters;

/**
 * 封包類別
 * @author hui
 *
 */
public class Packet {
	
	//final private int queue_number = EmulatorParameters.queue_number;
	//final private int constraint_deadline_time = EmulatorParameters.constraint_deadline_time;
	
	public double u2e_arrival_time = 0; // UE傳送到Edge的到達時間
	public double u2e_service_time = 0; //  UE傳送到Edge的服務時間(傳輸時間)
	public double u2e_departure_time = 0; //  UE傳送到Edge的完成時間
	public double u2e_waiting_time = 0; //  UE傳送到Edge的等候時間
	public int u2e_waiting_queue_length = 0; //  UE傳送到Edge的等候長度
	public int u2e_waiting_queue_length_scheduled = 0;//  排程後等候長度
	
	public double e_arrival_time = 0; // 在Edge的到達時間
	public double e_service_time = 0; // 在Edge的服務時間
	public double e_departure_time = 0; // 在Edge的完成時間
	public double e_waiting_time = 0; // 在Edge的等候時間
	public int e_waiting_queue_length = 0; // 在Edge的等候長度
	public int e_waiting_queue_length_scheduled = 0;//  排程後等候長度
	
	public double e2u_arrival_time = 0; // Edge傳送到UE的到達時間
	public double e2u_service_time = 0; // Edge傳送到UE的服務時間(傳輸時間)
	public double e2u_departure_time = 0; // Edge傳送到UE的完成時間
	public double e2u_waiting_time = 0; // Edge傳送到UE的等候時間
	public int e2u_waiting_queue_length = 0; // Edge傳送到UE的等候長度
	public int e2u_waiting_queue_length_scheduled = 0;//  排程後等候長度
	
	public int packet_index = 0; // 封包編號 
	public int service_type = 0; // 服務類型 urllc mmtc embb
	public double constraint_deadline_time = 0; //約束時間urllc=20ms embb=10s mmtc=75ms
	public double packet_uplink_size = 0; //封包大小(UE傳輸)
	public double packet_downlink_size = 0; //封包大小(EU傳輸)
	public double packet_computing_job = 0; //封包大小(Edge運算)
	
	public double deadline_time = 0; // 截止時間 
	public double remaining_time = 0; //  剩餘時間
	
	/**
	 * 建構子
	 * @param packet_index 封包編號
	 * @param service_type 服務類型
	 * @param arrival_time 到達
	 * @param service_time 服務時間
	 * @param u2e_arrival_time UE傳送到Edge的到達時間
	 * @param u2e_service_time UE傳送到Edge的服務時間(傳輸時間)
	 * @param e_service_time 在Edge的服務時間(運算時間)
	 * @param e2u_service_time Edge傳送到UE的服務時間(傳輸時間)
	 */
	public Packet(int packet_index, int service_type, double packet_uplink_size, double packet_downlink_size, double packet_computing_job, double u2e_arrival_time, double u2e_service_time, double e_service_time, double e2u_service_time) {
		this.packet_index = packet_index;
		this.service_type = service_type;
		this.packet_uplink_size = packet_uplink_size;
		this.packet_downlink_size = packet_downlink_size;
		this.packet_computing_job = packet_computing_job;
		
		this.u2e_arrival_time = u2e_arrival_time;
		this.u2e_service_time = u2e_service_time;
		this.u2e_departure_time = 0;
		this.u2e_waiting_queue_length = 0;
		this.u2e_waiting_queue_length_scheduled = 0;
		//this.e_arrival_time = u2e_departure_time;
		this.e_service_time = e_service_time;
		this.e_departure_time = 0;
		this.e_waiting_queue_length = 0;
		this.e_waiting_queue_length_scheduled = 0;
		//this.e2u_arrival_time = e_departure_time;
		this.e2u_service_time = e2u_service_time;
		this.e2u_departure_time = 0;
		this.e2u_waiting_queue_length = 0;
		this.e2u_waiting_queue_length_scheduled = 0;
		
		deadline_time = u2e_arrival_time + constraint_deadline_time; // base on service_type;
		remaining_time = deadline_time;
	}
	
	/**
	 * 取得封包資訊
	 * @return 封包資訊
	 */
	public String info() {
		StringBuffer buffer = new StringBuffer("\n");
		//for(int i = 0; i < queue_number; i++) {
			//buffer.append("---Queue_").append((i+1)).append("---\n");
			buffer.append("---UE2Edge---\n");
			buffer.append("封包編號: ").append(packet_index).append('\n');
			buffer.append("服務類型: ").append(service_type).append('\n');
			buffer.append("r, w, o: ").append(packet_uplink_size +", "+packet_computing_job+", "+packet_downlink_size).append('\n');
			buffer.append("UE傳送到Edge的到達時間: ").append(u2e_arrival_time).append('\n');
			buffer.append("UE傳送到Edge的服務時間(傳輸時間): ").append(u2e_service_time).append('\n');
			buffer.append("UE傳送到Edge的完成時間: ").append(u2e_departure_time).append('\n');
			buffer.append("截止時間: ").append(deadline_time).append('\n');
			buffer.append("剩餘時間: ").append(remaining_time).append('\n');
			buffer.append("UE傳送到Edge的等候時間: ").append(u2e_waiting_time).append('\n');
			buffer.append("UE傳送到Edge的等候長度: ").append(u2e_waiting_queue_length).append('\n');
			buffer.append("UE傳送到Edge的排程後等候長度: ").append(u2e_waiting_queue_length_scheduled).append('\n');
			//buffer.append("---Queue_").append((i+1)).append("---\n");
			buffer.append("---Edge---\n");
			buffer.append("封包編號: ").append(packet_index).append('\n');
			buffer.append("服務類型: ").append(service_type).append('\n');
			buffer.append("在Edge的到達時間: ").append(e_arrival_time).append('\n');
			buffer.append("在Edge的服務時間(運算時間): ").append(e_service_time).append('\n');
			buffer.append("在Edge的完成時間: ").append(e_departure_time).append('\n');
			buffer.append("截止時間: ").append(deadline_time).append('\n');
			buffer.append("剩餘時間: ").append(remaining_time).append('\n');
			buffer.append("在Edge的等候時間: ").append(e_waiting_time).append('\n');
			buffer.append("在Edge的等候長度: ").append(e_waiting_queue_length).append('\n');
			buffer.append("在Edge的排程後等候長度: ").append(e_waiting_queue_length_scheduled).append('\n');
			//buffer.append("---Queue_").append((i+1)).append("---\n");
			buffer.append("---Edge2UE---\n");
			buffer.append("封包編號: ").append(packet_index).append('\n');
			buffer.append("服務類型: ").append(service_type).append('\n');
			buffer.append("Edge傳送到UE的到達時間: ").append(e2u_arrival_time).append('\n');
			buffer.append("Edge傳送到UE的服務時間(傳輸時間): ").append(e2u_service_time).append('\n');
			buffer.append("Edge傳送到UE的完成時間: ").append(e2u_departure_time).append('\n');
			buffer.append("截止時間: ").append(deadline_time).append('\n');
			buffer.append("剩餘時間: ").append(remaining_time).append('\n');
			buffer.append("Edge傳送到UE的等候時間: ").append(e2u_waiting_time).append('\n');
			buffer.append("Edge傳送到UE的等候長度: ").append(e2u_waiting_queue_length).append('\n');
			buffer.append("Edge傳送到UE的排程後等候長度: ").append(e2u_waiting_queue_length_scheduled).append('\n');
		//}
		
		buffer.append("===========\n");
		
		return buffer.toString();
	}
}
