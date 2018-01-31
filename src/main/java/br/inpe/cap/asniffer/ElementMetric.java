package br.inpe.cap.asniffer;

import java.util.HashMap;
import java.util.Map;

public class ElementMetric {
	
	private Map<String,Integer> metricValue;

	public ElementMetric() {
		metricValue = new HashMap<>();
	}
	
	public int getMetricValue(String metric) {
		if(metricValue.containsKey(metric))
			return metricValue.get(metric);
		else
			return -1;
	}

	public void addMetricValue(String metricName, int metricValue) {
		this.metricValue.put(metricName, metricValue);
	}

}
