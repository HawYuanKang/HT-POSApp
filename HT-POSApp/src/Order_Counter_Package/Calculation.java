package Order_Counter_Package;

import java.math.RoundingMode;
import java.text.DecimalFormat;

//Author of this class : ARIEF MUIZZUDDIN BIN KHALID , B032110508

public class Calculation 
{
	// This class is used for price calculation.
	
	public Double calculate_subtotal (int quantity, Double price)
	{
		// Calculate subtotal of items
		double subtotal = 0;
		subtotal = quantity*price;
		return subtotal;
	}
	
	public Double rounding_total (double raw_total)
	{
		// Round off the subtotal.
		double factor = (double) Math.pow(10, 1);
	    double price = raw_total * factor;
	    double tmp = Math.round(price);
		double answer=tmp/factor;
		double difference=answer-raw_total;
		DecimalFormat df = new DecimalFormat("#.###");
		df.setRoundingMode(RoundingMode.CEILING);
		double round= Double.parseDouble(df.format(difference));
		return round;
	}
}
