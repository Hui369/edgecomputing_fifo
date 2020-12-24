package ntust.huiting.edgecomputing;

//import ntust.huiting.edgecomputing.EmulatorParameters;

/**
 * �ʥ]���O
 * @author �G��߬
 *
 */
public class Packet {
	
	//final private int queue_number = EmulatorParameters.queue_number;
	//final private int constraint_deadline_time = EmulatorParameters.constraint_deadline_time;
	
	public double u2e_arrival_time = 0; // UE�ǰe��Edge����F�ɶ�
	public double u2e_service_time = 0; //  UE�ǰe��Edge���A�Ȯɶ�(�ǿ�ɶ�)
	public double u2e_departure_time = 0; //  UE�ǰe��Edge�������ɶ�
	public double u2e_waiting_time = 0; //  UE�ǰe��Edge�����Ԯɶ�
	public int u2e_waiting_queue_length = 0; //  UE�ǰe��Edge�����Ԫ���
	public int u2e_waiting_queue_length_scheduled = 0;//  �Ƶ{�ᵥ�Ԫ���
	
	public double e_arrival_time = 0; // �bEdge����F�ɶ�
	public double e_service_time = 0; // �bEdge���A�Ȯɶ�
	public double e_departure_time = 0; // �bEdge�������ɶ�
	public double e_waiting_time = 0; // �bEdge�����Ԯɶ�
	public int e_waiting_queue_length = 0; // �bEdge�����Ԫ���
	public int e_waiting_queue_length_scheduled = 0;//  �Ƶ{�ᵥ�Ԫ���
	
	public double e2u_arrival_time = 0; // Edge�ǰe��UE����F�ɶ�
	public double e2u_service_time = 0; // Edge�ǰe��UE���A�Ȯɶ�(�ǿ�ɶ�)
	public double e2u_departure_time = 0; // Edge�ǰe��UE�������ɶ�
	public double e2u_waiting_time = 0; // Edge�ǰe��UE�����Ԯɶ�
	public int e2u_waiting_queue_length = 0; // Edge�ǰe��UE�����Ԫ���
	public int e2u_waiting_queue_length_scheduled = 0;//  �Ƶ{�ᵥ�Ԫ���
	
	public int packet_index = 0; // �ʥ]�s�� 
	public int service_type = 0; // �A������ urllc mmtc embb
	public double constraint_deadline_time = 0; //�����ɶ�urllc=20ms embb=10s mmtc=75ms
	public double packet_uplink_size = 0; //�ʥ]�j�p(UE�ǿ�)
	public double packet_downlink_size = 0; //�ʥ]�j�p(EU�ǿ�)
	public double packet_computing_job = 0; //�ʥ]�j�p(Edge�B��)
	
	public double deadline_time = 0; // �I��ɶ� 
	public double remaining_time = 0; //  �Ѿl�ɶ�
	
	/**
	 * �غc�l
	 * @param packet_index �ʥ]�s��
	 * @param service_type �A������
	 * @param arrival_time ��F
	 * @param service_time �A�Ȯɶ�
	 * @param u2e_arrival_time UE�ǰe��Edge����F�ɶ�
	 * @param u2e_service_time UE�ǰe��Edge���A�Ȯɶ�(�ǿ�ɶ�)
	 * @param e_service_time �bEdge���A�Ȯɶ�(�B��ɶ�)
	 * @param e2u_service_time Edge�ǰe��UE���A�Ȯɶ�(�ǿ�ɶ�)
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
	 * ���o�ʥ]��T
	 * @return �ʥ]��T
	 */
	public String info() {
		StringBuffer buffer = new StringBuffer("\n");
		//for(int i = 0; i < queue_number; i++) {
			//buffer.append("---Queue_").append((i+1)).append("---\n");
			buffer.append("---UE2Edge---\n");
			buffer.append("�ʥ]�s��: ").append(packet_index).append('\n');
			buffer.append("�A������: ").append(service_type).append('\n');
			buffer.append("r, w, o: ").append(packet_uplink_size +", "+packet_computing_job+", "+packet_downlink_size).append('\n');
			buffer.append("UE�ǰe��Edge����F�ɶ�: ").append(u2e_arrival_time).append('\n');
			buffer.append("UE�ǰe��Edge���A�Ȯɶ�(�ǿ�ɶ�): ").append(u2e_service_time).append('\n');
			buffer.append("UE�ǰe��Edge�������ɶ�: ").append(u2e_departure_time).append('\n');
			buffer.append("�I��ɶ�: ").append(deadline_time).append('\n');
			buffer.append("�Ѿl�ɶ�: ").append(remaining_time).append('\n');
			buffer.append("UE�ǰe��Edge�����Ԯɶ�: ").append(u2e_waiting_time).append('\n');
			buffer.append("UE�ǰe��Edge�����Ԫ���: ").append(u2e_waiting_queue_length).append('\n');
			buffer.append("UE�ǰe��Edge���Ƶ{�ᵥ�Ԫ���: ").append(u2e_waiting_queue_length_scheduled).append('\n');
			//buffer.append("---Queue_").append((i+1)).append("---\n");
			buffer.append("---Edge---\n");
			buffer.append("�ʥ]�s��: ").append(packet_index).append('\n');
			buffer.append("�A������: ").append(service_type).append('\n');
			buffer.append("�bEdge����F�ɶ�: ").append(e_arrival_time).append('\n');
			buffer.append("�bEdge���A�Ȯɶ�(�B��ɶ�): ").append(e_service_time).append('\n');
			buffer.append("�bEdge�������ɶ�: ").append(e_departure_time).append('\n');
			buffer.append("�I��ɶ�: ").append(deadline_time).append('\n');
			buffer.append("�Ѿl�ɶ�: ").append(remaining_time).append('\n');
			buffer.append("�bEdge�����Ԯɶ�: ").append(e_waiting_time).append('\n');
			buffer.append("�bEdge�����Ԫ���: ").append(e_waiting_queue_length).append('\n');
			buffer.append("�bEdge���Ƶ{�ᵥ�Ԫ���: ").append(e_waiting_queue_length_scheduled).append('\n');
			//buffer.append("---Queue_").append((i+1)).append("---\n");
			buffer.append("---Edge2UE---\n");
			buffer.append("�ʥ]�s��: ").append(packet_index).append('\n');
			buffer.append("�A������: ").append(service_type).append('\n');
			buffer.append("Edge�ǰe��UE����F�ɶ�: ").append(e2u_arrival_time).append('\n');
			buffer.append("Edge�ǰe��UE���A�Ȯɶ�(�ǿ�ɶ�): ").append(e2u_service_time).append('\n');
			buffer.append("Edge�ǰe��UE�������ɶ�: ").append(e2u_departure_time).append('\n');
			buffer.append("�I��ɶ�: ").append(deadline_time).append('\n');
			buffer.append("�Ѿl�ɶ�: ").append(remaining_time).append('\n');
			buffer.append("Edge�ǰe��UE�����Ԯɶ�: ").append(e2u_waiting_time).append('\n');
			buffer.append("Edge�ǰe��UE�����Ԫ���: ").append(e2u_waiting_queue_length).append('\n');
			buffer.append("Edge�ǰe��UE���Ƶ{�ᵥ�Ԫ���: ").append(e2u_waiting_queue_length_scheduled).append('\n');
		//}
		
		buffer.append("===========\n");
		
		return buffer.toString();
	}
}
