package ntust.huiting.edgecomputing;

import ntust.huiting.edgecomputing.Packet;
import ntust.huiting.random.Generator;

/**
 * ��t�B�������
 * @author �G��߬
 *
 */
public class Emulator {		
	//final static double lambda = 2; // UE�ǰe��Edge���ʥ]��F�v	
	final static double constraint_deadline_time[] = {0.010, 1, 0.05}; //����
	final static int service_number = 3; /// �A�������ƶq //urllc embb mmtc
	final static double lambda[] = {105, 30, 15}; //urllc, embb, mmtc (packets/s)
	//data rate: uRLLC >25kbps, eMBB 2Mbps, mMTC 800kbps
	final static double r_job_size[] = {0.5, 1.5, 0.5}; //urllc,embb,mmtc packet UE2Edge input size (bits)
	final static double w_job_size[] = {11, 1, 0.5}; //urllc,embb,mmtc packet Edge work load (cycles)
	final static double o_job_size[] = {0.5, 1.5, 0.5}; //urllc,embb,mmtc packet Edge2UE output size ((bits)
	//uplink rate 40 Mbps , downlink rate  63 Mbps  �H��Edge �OIntel Xeon CPUs ,	Intel Xeon CPUs �� 3.00 GHz�C
	final static double mu_UE = 4000; // UE�ǰe��Edge���A�Ȳv(�ǿ�v) 40Mbps 
	final static double mu_E = 0.3; // �bEdge���A�Ȳv(�B��v) 178MIps 3.00 GHz (cycles)
	final static double mu_EU = 6300; // Edge�ǰe��UE���A�Ȳv(�ǿ�v) 63Mbps
	final static int number_packet = 10; // �����ʥ]��
	static Packet packet[] = new Packet[number_packet]; // �ʥ]
	
	
	/**
	 * �D�{��
	 * @param args
	 */
	public static void main(String args[]) {
		// ��l�ƫʥ]
		initial_packet();
		
		// UE�ǰe��Edge��M/M/1 queue_fifo
		u2e_m_m_1();
				
		// �bEdge��M/M/1 queue_fifo
		e_m_m_1();
				
		// Edge�ǰe��UE��M/M/1 queue_fifo
		e2u_m_m_1();
		
		// ��ܲέp���G
		summary();
	}
	
	/**
	 * ��l�ƫʥ]
	 * �]�w�C�ӫʥ]����F�ɶ��M�A�Ȯɶ�
	 */
	private static void initial_packet() {
		// �üƲ��;�
		Generator g = new Generator();
		
		// ��U�ɶ��I
		double current_time[] = {0, 0, 0};
		
		//����number_packet�ӫʥ]
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
	 * UE�ǰe��Edge��M/M/1 queue_fifo
	 */
	private static void u2e_m_m_1() {
		double previous_packet_departure_time = 0;
		
		// �B�z�C�@�ӫʥ]
		for(int i = 0; i < number_packet; i++) {
			packet[i].u2e_waiting_queue_length = 0;
			
			// �ݵ���
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
			// ���ݵ���
			else {
				packet[i].u2e_departure_time = packet[i].u2e_arrival_time + packet[i].u2e_service_time;
				packet[i].u2e_waiting_time = 0;				
			}
			// ��s�e�@�ӫʥ]���}�ɶ�
			previous_packet_departure_time = packet[i].u2e_departure_time;
			packet[i].e_arrival_time = packet[i].u2e_departure_time;
		}
	}
	
	/**
	 *  �bEdge��M/M/1 queue_fifo
	 */
	private static void e_m_m_1() {
		double previous_packet_departure_time = 0;
		
		// �B�z�C�@�ӫʥ]
		for(int i = 0; i < number_packet; i++) {
			packet[i].e_waiting_queue_length = 0;
			
			// �ݵ���
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
			// ���ݵ���
			else {
				packet[i].e_departure_time = packet[i].e_arrival_time + packet[i].e_service_time;
				packet[i].e_waiting_time = 0;				
			}
			// ��s�e�@�ӫʥ]���}�ɶ�
			previous_packet_departure_time = packet[i].e_departure_time;
			packet[i].e2u_arrival_time = packet[i].e_departure_time;
		}
	}
	
	/**
	 * Edge�ǰe��UE��M/M/1 queue_fifo
	 */
	private static void e2u_m_m_1() {
		double previous_packet_departure_time = 0;
		
		// �B�z�C�@�ӫʥ]
		for(int i = 0; i < number_packet; i++) {
			packet[i].e2u_waiting_queue_length = 0;
			
			// �ݵ���
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
			// ���ݵ���
			else {
				packet[i].e2u_departure_time = packet[i].e2u_arrival_time + packet[i].e2u_service_time;
				packet[i].e2u_waiting_time = 0;				
			}
			// ��s�e�@�ӫʥ]���}�ɶ�
			previous_packet_departure_time = packet[i].e2u_departure_time;
		}
	}
	
		
	/**
	 * �έp���G
	 */
	private static void summary() {
		double total_u2e_service_time = 0;
		double total_u2e_waiting_time = 0;
		double total_e_service_time = 0;
		double total_e_waiting_time = 0;
		double total_e2u_service_time = 0;
		double total_e2u_waiting_time = 0;
		double total_processing_time = 0;
		double total_delay_packet = 0; //�p��w����ʥ]
		double total_delay_packet_type0 = 0; //service type urllc �W�Ldeadline
		double total_delay_packet_type1 = 0; //service type embb �W�Ldeadline
		double total_delay_packet_type2 = 0; //service type mmtc �W�Ldeadline
		double total_packet_type0 = 0; //service type urllc
		double total_packet_type1 = 0; //service type embb 
		double total_packet_type2 = 0; //service type mmtc
		double simulation_time = packet[number_packet - 1].e2u_departure_time;
		
		// ��ܩM�έp�C�@�ӫʥ]���ɶ�
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
			
			System.out.println("�ʥ](" + (i+1) + "): " + packet[i].info());
		}
		
		// ��ܲέp���G
		System.out.println("UE�ǰe��Edge���A�Ȯɶ�: " + (total_u2e_service_time));
		System.out.println("����UE�ǰe��Edge���A�Ȯɶ�: " + (total_u2e_service_time/number_packet));
		System.out.println("����UE�ǰe��Edge�����Ԯɶ�: " + (total_u2e_waiting_time/number_packet));
		System.out.println("����UE�ǰe��Edge�����Ԫ���: " + (total_u2e_waiting_time/simulation_time));
		System.out.println("�bEdge���A�Ȯɶ�: " + (total_e_service_time));
		System.out.println("�����bEdge���A�Ȯɶ�: " + (total_e_service_time/number_packet));
		System.out.println("�����bEdge�����Ԯɶ�: " + (total_e_waiting_time/number_packet));
		System.out.println("�����bEdge�����Ԫ���: " + (total_e_waiting_time/simulation_time));
		System.out.println("Edge�ǰe��UE���A�Ȯɶ�: " + (total_e2u_service_time));
		System.out.println("����Edge�ǰe��UE���A�Ȯɶ�: " + (total_e2u_service_time/number_packet));
		System.out.println("����Edge�ǰe��UE�����Ԯɶ�: " + (total_e2u_waiting_time/number_packet));
		System.out.println("����Edge�ǰe��UE�����Ԫ���: " + (total_e2u_waiting_time/simulation_time));
		System.out.println("�����C�ӫʥ]��E2E�ɶ�: " + (total_processing_time/number_packet)); //average delay
		System.out.println("�����C�ӫʥ]���W�Ldeadline�����: " + (total_delay_packet/number_packet)); //�W�Ldeadline time
		System.out.println("urllc�W�Ldeadline���ʥ]��: " + (total_delay_packet_type0));System.out.println("urllc�`�ʥ]��: " + (total_packet_type0));
		System.out.println("urllc�ʥ]�W�Ldeadline�����: " + (total_delay_packet_type0/total_packet_type0));
		System.out.println("embb�W�Ldeadline���ʥ]��: " + (total_delay_packet_type1));System.out.println("embb�`�ʥ]��: " + (total_packet_type1));
		System.out.println("embb�ʥ]�W�Ldeadline�����: " + (total_delay_packet_type1/total_packet_type1));
		System.out.println("mmtc�W�Ldeadline���ʥ]��: " + (total_delay_packet_type2));System.out.println("mmtc�`�ʥ]��: " + (total_packet_type2));
		System.out.println("mmtc�ʥ]�W�Ldeadline�����: " + (total_delay_packet_type2/total_packet_type2));
	}
	
}
