package ntust.huiting.edgecomputing.v2;

import ntust.huiting.random.Generator;

/**
 * 邊緣運算模擬器
 * @author hui
 *
 */
public class Emulator {
	final static int queue_number = EmulatorParameters.queue_number;
	
	final static double constant_deadline_time[] = {0.01, 1, 0.05};// 約束時間
	
	//final static double lambda = 2; // UE傳送到Edge的封包到達率	
	final static int service_number = 3; // 服務類型數量
	final static double lambda[] = {210, 60, 30}; // UE傳送到Edge的封包到達率
	final static double r[] = {0.5, 1.5, 0.5}; //urllc,embb,mmtc packet UE2Edge input size (bits)
	final static double w[] = {0.11, 1, 0.5}; //urllc,embb,mmtc packet Edge work load (cycles)
	final static double o[] = {0.5, 1.5, 0.5}; //urllc,embb,mmtc packet Edge2UE output size ((bits)
	final static double mu_UE = 4000; // UE傳送到Edge的服務率(傳輸率)
	final static double mu_E = 0.3; // 在Edge的服務率(傳輸率)
	final static double mu_EU = 6300; // Edge傳送到UE的服務率(傳輸率)
	final static int number_packet = 1000; // 模擬封包數	
	static Packet packet[] = new Packet[number_packet]; // 封包
	
	/**
	 * 主程式
	 * @param args
	 */
	public static void main(String args[]) {
		// 初始化封包
		initial_packet();
		
		for(int k = 0; k < queue_number; k++) {
			// M/M/1 queue
			m_m_1(k);// FIFO
		}
		
		// 顯示統計結果
		summary();
	}
	
	/**
	 * 初始化封包
	 * 設定每個封包的到達時間和服務時間
	 */
	private static void initial_packet() {
		// 亂數產生器// Random number generator
		Generator g = new Generator();
		
		// 當下時間點// Current time
		double current_time[] = {0, 0, 0};
		
		//產生number_packet個封包//Generate number_packet's packets
		for(int i = 0; i < number_packet; i++) {
			int service_type_gen = 0;
			double current_time_gen = current_time[service_type_gen];
			for(int j = 0; j < service_number; j++) {
				if(current_time_gen > current_time[j]) {
					service_type_gen = j;
					current_time_gen = current_time[service_type_gen];
				}
			}
			double inter_arrival_time = g.getRandomNumber_Poisson(lambda[service_type_gen]);
			double arrival_time = current_time[service_type_gen] += inter_arrival_time;
			
			double r_task_size = g.getRandomNumber_Normal(r[service_type_gen]);
			double w_task_size = g.getRandomNumber_Normal(w[service_type_gen]);
			double o_task_size = g.getRandomNumber_Normal(o[service_type_gen]);
			
			double service_time[] = new double[queue_number];
			service_time[0] = (r_task_size / (mu_UE));
			service_time[1] = (w_task_size / (mu_UE));
			service_time[2] = (o_task_size / (mu_UE));
			
			double deadline_time = arrival_time + constant_deadline_time[service_type_gen];
			
			packet[i] = new Packet(i + 1, service_type_gen, r_task_size, w_task_size, o_task_size, arrival_time, service_time, deadline_time);
		}
	}
	
	/**
	 * M/M/1 queue
	 */
	private static void m_m_1(int k) {
		boolean isNotLast = (k != queue_number - 1);
		double previous_packet_departure_time = 0;
		
		// 處理每一個封包
		for(int i = 0; i < number_packet; i++) {
			packet[i].waiting_queue_length[k] = 0;
			
			// 需等候
			if(packet[i].arrival_time[k] < previous_packet_departure_time) {
				packet[i].departure_time[k] = previous_packet_departure_time + packet[i].service_time[k];
				packet[i].waiting_time[k] = previous_packet_departure_time - packet[i].arrival_time[k];
				
				for(int j = i - 1; j >= 0; j--) {
					if(packet[i].arrival_time[k] < packet[j].departure_time[k]) {
						packet[i].waiting_queue_length[k]++;
					}
					else break;
				}
			}
			// 不需等候
			else {
				packet[i].departure_time[k] = packet[i].arrival_time[k] + packet[i].service_time[k];
				packet[i].waiting_time[k] = 0;				
			}
			// 更新前一個封包離開時間
			previous_packet_departure_time = packet[i].departure_time[k];			
			if(isNotLast) packet[i].arrival_time[k+1] = packet[i].departure_time[k];
		}
	}
	
	/**
	 * 統計結果
	 */
	private static void summary() {
		double total_service_time[] = new double[queue_number];
		double total_waiting_time[] = new double[queue_number];
		double total_processing_time = 0;
		double simulation_time = packet[number_packet - 1].departure_time[queue_number - 1];
		double QV = 0;//計算已延遲封包(超過deadline_time)
		
		// 顯示和統計每一個封包的時間
		for(int i = 0; i < number_packet; i++) {
			
			if(packet[i].deadline_time - packet[i].departure_time[queue_number - 1]< 0) {
				QV++;
			}
			
			for(int k = 0; k < queue_number; k++) {
				total_service_time[k] += packet[i].service_time[k];
				total_waiting_time[k] += packet[i].waiting_time[k];
				
			}
			total_processing_time += packet[i].departure_time[queue_number - 1] - packet[i].arrival_time[0];
			
			System.out.println("封包(" + (i+1) + "): " + packet[i].info());
		}
		
		// 顯示統計結果
		for(int k = 0; k < queue_number; k++) {
			System.out.println("Queue_" + (k+1) + "服務時間: " + (total_service_time[k]/number_packet));
			System.out.println("Queue_" + (k+1) + "等候時間: " + (total_waiting_time[k]/number_packet));
			System.out.println("Queue_" + (k+1) + "等候長度: " + (total_waiting_time[k]/simulation_time));
		}
		System.out.println("平均每個封包的處理時間: " + (total_processing_time/number_packet));
		System.out.println("超過deadline的比例: " + (QV/number_packet));
	}
}
