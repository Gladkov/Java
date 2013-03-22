package diploma;

import java.util.ArrayList;

public class EmployerInfo {
	
	
		
		private int _round;
		private Double _proposal;
		private Double _response;
		private Double _profit;
		
		
		public EmployerInfo(int _round, Double _proposal, Double _response, Double _profit) {
			// TODO Auto-generated constructor stub
			this.set_round(_round);
			this.set_proposal(_proposal);
			this.set_response(_response);
			this.set_profit(_profit);
			
			
		}

		public int get_round() {
			return _round;
		}

		public void set_round(int _round) {
			this._round = _round;
		}

		public Double get_proposal() {
			return _proposal;
		}

		public void set_proposal(Double _proposal) {
			this._proposal = _proposal;
		}

		public Double get_profit() {
			return _profit;
		}

		public void set_profit(Double _profit) {
			this._profit = _profit;
		}

		public Double get_response() {
			return _response;
		}

		public void set_response(Double _response) {
			this._response = _response;
		}
		
		@Override
		public boolean equals(Object obj){
			if (!(obj instanceof EmployerInfo))
		        return false;
			EmployerInfo ei = (EmployerInfo) obj;
			return (_proposal.equals(ei.get_proposal()) && _response.equals(ei.get_response()));
			
			
		}
		
		public static boolean contains(ArrayList<EmployerInfo> _employerInfo, EmployerInfo contains){
			
			for(EmployerInfo ei: _employerInfo)
			{
				if(ei.equals(contains))
					return true;
			}
			
			
			
			return false;
		}

}


