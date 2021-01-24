package ntust.huiting.edgecomputing;

import ntust.huiting.edgecomputing.Packet;
import ntust.huiting.random.Generator;

/**
 * 邊緣運算模擬器
 * @author hui
 *
 */
public class Emulator {		
	//final static double lambda = 2; // UE傳送到Edge的封包到達率	
	final static double constraint_deadline_time[] = {0.010, 1, 0.05}; //約束
	final static int service_number = 3; /// 服務類型數量 //urllc embb mmtc
	final static double lambda[] = {105, 30, 15}; //urllc, embb, mmtc (packets/s)
	//data rate: uRLLC >25kbps, eMBB 2Mbps, mMTC 800kbps
	final static double r_job_size[] = {0.5, 1.5, 0.5}; //urllc,embb,mmtc packet UE2Edge input size (bits)
	final static double w_job_size[] = {0.11, 1, 0.5}; //urllc,embb,mmtc packet Edge work load (cycles)
	final static double o_job_size[] = {0.5, 1.5, 0.5}; //urllc,embb,mmtc packet Edge2UE output size ((bits)
	//uplink rate 40 Mbps , downlink rate  63 Mbps  以及Edge 是Intel Xeon CPUs ,	Intel Xeon CPUs 約 3.00 GHz。
	final static double mu_UE = 4000; // UE傳送到Edge的服務率(傳輸率) 40Mbps 
	final static double mu_E = 0.3; // 在Edge的服務率(運算率) 178MIps 3.00 GHz (cycles)
	final static double mu_EU = 6300; // Edge傳送到UE的服務率(傳輸率) 63Mbps
	final static int number_packet = 10; // 模擬封包數
	static Packet packet[] = new Packet[number_packet]; // 封包
	
	
	/**
	 * 主程式
	 * @param args
	 */
	public static void main(String args[]) {
		// 初始化封包
		initial_packet();
		
		// UE傳送到Edge的M/M/1 queue_fifo
		u2e_m_m_1();
				
		// 在Edge的M/M/1 queue_fifo
		e_m_m_1();
				
		// Edge傳送到UE的M/M/1 queue_fifo
		e2u_m_m_1();
		
		// 顯示統計結果
		summary();
	}
	
	/**
	 * 初始化封包
	 * 設定每個封包的到達時間和服務時間
	 */
	private static void initial_packet() {
		// 亂數產生器
		Generator g = new Generator();
		
		// 當下時間點
		double current_time[] = {0, 0, 0};
		
		//產生number_packet個封包
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
			double deadline_time = arrival_time + constraint_deadline_time[service_type_gen];
			double remaining_time = deadline_time - current_time_gen;
			
			double packet_uplink_size = g.getRandomNumber_Normal(r_job_size[service_type_gen]);
			double packet_computing_job = g.getRandomNumber_Normal(w_job_size[service_type_gen]);
			double packet_downlink_size = g.getRandomNumber_Normal(o_job_size[service_type_gen]);
			
			double u2e_service_time = (packet_uplink_size/mu_UE);//g.getRandomNumber_Exponential(mu_UE);
			double e_service_time = (packet_computing_job/mu_UE);//g.getRandomNumber_Exponential(mu_E);
			double e2u_service_time = (packet_downlink_size/mu_UE);//g.getRandomNumber_Exponential(mu_EU);
			packet[i] = new Packet(i + 1, service_type_gen, packet_uplink_size, packet_downlink_size, packet_computing_job, arrival_time, u2e_service_time, e_service_time, e2u_service_time);
		}
	}
	
	/**
	 * UE傳送到Edge的M/M/1 queue_fifo
	 */
	private static void u2e_m_m_1() {
		double previous_packet_departure_time = 0;
		
		// 處理每一個封包
		for(int i = 0; i < number_packet; i++) {
			packet[i].u2e_waiting_queue_length = 0;
			
			// 需等候
			if(packet[i].u2e_arrival_time < previous_packet_departure_time) {
				packet[i].u2e_departure_time = previous_packet_departure_time + packet[i].u2e_service_time;
				packet[i].u2e_waiting_time = previous_packet_departure_time - packet[i].u2e_arrival_time;
				
				for(int j = i - 1; j >= 0; j--) {
					if(packet[i].u2e_arrival_time < packet[j].u2e_departure_time) {
						packet[i].u2e_waiting_queue_length++;
						packet[i].remaining_time = packet[i].deadline_time - previous_packet_departure_time;
					}
					else break;
				}
			}
			// 不需等候
			else {
				packet[i].u2e_departure_time = packet[i].u2e_arrival_time + packet[i].u2e_service_time;
				packet[i].u2e_waiting_time = 0;				
			}
			// 更新前一個封包離開時間
			previous_packet_departure_time = packet[i].u2e_departure_time;
			packet[i].e_arrival_time = packet[i].u2e_departure_time;
		}
	}
	
	/**
	 *  在Edge的M/M/1 queue_fifo
	 */
	private static void e_m_m_1() {
		double previous_packet_departure_time = 0;
		
		// 處理每一個封包
		for(int i = 0; i < number_packet; i++) {
			packet[i].e_waiting_queue_length = 0;
			
			// 需等候
			if(packet[i].e_arrival_time < previous_packet_departure_time) {
				packet[i].e_departure_time = previous_packet_departure_time + packet[i].e_service_time;
				packet[i].e_waiting_time = previous_packet_departure_time - packet[i].e_arrival_time;
				
				for(int j = i - 1; j >= 0; j--) {
					if(packet[i].e_arrival_time < packet[j].e_departure_time) {
						packet[i].e_waiting_queue_length++;
						packet[i].remaining_time = packet[i].deadline_time - previous_packet_departure_time;
					}
					else break;
				}
			}
			// 不需等候
			else {
				packet[i].e_departure_time = packet[i].e_arrival_time + packet[i].e_service_time;
				packet[i].e_waiting_time = 0;				
			}
			// 更新前一個封包離開時間
			previous_packet_departure_time = packet[i].e_departure_time;
			packet[i].e2u_arrival_time = packet[i].e_departure_time;
		}
	}
	
	/**
	 * Edge傳送到UE的M/M/1 queue_fifo
	 */
	private static void e2u_m_m_1() {
		double previous_packet_departure_time = 0;
		
		// 處理每一個封包
		for(int i = 0; i < number_packet; i++) {
			packet[i].e2u_waiting_queue_length = 0;
			
			// 需等候
			if(packet[i].e2u_arrival_time < previous_packet_departure_time) {
				packet[i].e2u_departure_time = previous_packet_departure_time + packet[i].e2u_service_time;
				packet[i].e2u_waiting_time = previous_packet_departure_time - packet[i].e2u_arrival_time;
				
				for(int j = i - 1; j >= 0; j--) {
					if(packet[i].e2u_arrival_time < packet[j].e2u_departure_time) {
						packet[i].e2u_waiting_queue_length++;
						packet[i].remaining_time = packet[i].deadline_time - previous_packet_departure_time;
					}
					else break;
				}
			}
			// 不需等候
			else {
				packet[i].e2u_departure_time = packet[i].e2u_arrival_time + packet[i].e2u_service_time;
				packet[i].e2u_waiting_time = 0;				
			}
			// 更新前一個封包離開時間
			previous_packet_departure_time = packet[i].e2u_departure_time;
		}
	}
	
		
	/**
	 * 統計結果
	 */
	private static void summary() {
		double total_u2e_service_time = 0;
		double total_u2e_waiting_time = 0;
		double total_e_service_time = 0;
		double total_e_waiting_time = 0;
		double total_e2u_service_time = 0;
		double total_e2u_waiting_time = 0;
		double total_processing_time = 0;
		double total_delay_packet = 0; //計算已延遲封包
		double total_delay_packet_type0 = 0; //service type urllc 超過deadline
		double total_delay_packet_type1 = 0; //service type embb 超過deadline
		double total_delay_packet_type2 = 0; //service type mmtc 超過deadline
		double total_packet_type0 = 0; //service type urllc
		double total_packet_type1 = 0; //service type embb 
		double total_packet_type2 = 0; //service type mmtc
		double simulation_time = packet[number_packet - 1].e2u_departure_time;
		
		// 顯示和統計每一個封包的時間
		for(int i = 0; i < number_packet; i++) {
			if(packet[i].service_type == 0) total_packet_type0++; //urllc
			if(packet[i].service_type == 1) total_packet_type1++; //embb
			if(packet[i].service_type == 2) total_packet_type2++; //mmtc
			
			if(packet[i].remaining_time < 0) {
				total_delay_packet++;
				if(packet[i].service_type == 0) total_delay_packet_type0++;
				if(packet[i].service_type == 1) total_delay_packet_type1++;
				if(packet[i].service_type == 2) total_delay_packet_type2++;
			}
			
			total_u2e_service_time += packet[i].u2e_service_time;
			total_u2e_waiting_time += packet[i].u2e_waiting_time;
			total_e_service_time += packet[i].e_service_time;
			total_e_waiting_time += packet[i].e_waiting_time;
			total_e2u_service_time += packet[i].e2u_service_time;
			total_e2u_waiting_time += packet[i].e2u_waiting_time;
			total_processing_time += packet[i].e2u_departure_time - packet[i].u2e_arrival_time;
			
			System.out.println("封包(" + (i+1) + "): " + packet[i].info());
		}
		
		// 顯示統計結果
		System.out.println("UE傳送到Edge的服務時間: " + (total_u2e_service_time));
		System.out.println("平均UE傳送到Edge的服務時間: " + (total_u2e_service_time/number_packet));
		System.out.println("平均UE傳送到Edge的等候時間: " + (total_u2e_waiting_time/number_packet));
		System.out.println("平均UE傳送到Edge的等候長度: " + (total_u2e_waiting_time/simulation_time));
		System.out.println("在Edge的服務時間: " + (total_e_service_time));
		System.out.println("平均在Edge的服務時間: " + (total_e_service_time/number_packet));
		System.out.println("平均在Edge的等候時間: " + (total_e_waiting_time/number_packet));
		System.out.println("平均在Edge的等候長度: " + (total_e_waiting_time/simulation_time));
		System.out.println("Edge傳送到UE的服務時間: " + (total_e2u_service_time));
		System.out.println("平均Edge傳送到UE的服務時間: " + (total_e2u_service_time/number_packet));
		System.out.println("平均Edge傳送到UE的等候時間: " + (total_e2u_waiting_time/number_packet));
		System.out.println("平均Edge傳送到UE的等候長度: " + (total_e2u_waiting_time/simulation_time));
		System.out.println("平均每個封包的E2E時間: " + (total_processing_time/number_packet)); //average delay
		System.out.println("超過deadline的比例: " + (total_delay_packet/number_packet)); //超過deadline time
		System.out.println("urllc超過deadline的封包數: " + (total_delay_packet_type0));System.out.println("urllc總封包數: " + (total_packet_type0));
		System.out.println("urllc封包超過deadline的比例: " + (total_delay_packet_type0/total_packet_type0));
		System.out.println("embb超過deadline的封包數: " + (total_delay_packet_type1));System.out.println("embb總封包數: " + (total_packet_type1));
		System.out.println("embb封包超過deadline的比例: " + (total_delay_packet_type1/total_packet_type1));
		System.out.println("mmtc超過deadline的封包數: " + (total_delay_packet_type2));System.out.println("mmtc總封包數: " + (total_packet_type2));
		System.out.println("mmtc封包超過deadline的比例: " + (total_delay_packet_type2/total_packet_type2));
	}
	
}
