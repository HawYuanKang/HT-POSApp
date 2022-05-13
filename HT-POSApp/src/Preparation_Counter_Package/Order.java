package Preparation_Counter_Package;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import Order_Counter_Package.OrderItem;

//Author of this class : ARIEF MUIZZUDDIN BIN KHALID , B032110508

public class Order 
{
	private int orderId;
	private int orderNumber;
	private Date transactionDate;
	private List<OrderItem> orderedItems;
	private int totalOrderItem;
	private double subTotal;
	private double serviceTax;
	private double rounding;
	private double grandTotal;
	private double tenderedCash;
	private double change;
	
	public void setOrderId(int orderId)
	{
		this.orderId = orderId;
	}

	public int getOrderId()
	{
		return this.orderId;
	}
	
	public void setOrderNumber(int orderNumber)
	{
		this.orderNumber = orderNumber;
	}

	public int getOrderNumber()
	{
		return this.orderNumber;
	}
	
	public void setTransactionDate(Date transactionDate)
	{
		this.transactionDate = transactionDate;
	}

	public Date getTransactionDate()
	{
		return this.transactionDate;
	}
	
	public void setOrderItems(List<OrderItem> orderedItems)
	{
		this.orderedItems = orderedItems;
	}

	public List<OrderItem> getOrderItems()
	{
		return this.orderedItems;
	}
	
	public void setTotalOrderItem(int totalOrderItem)
	{
		this.totalOrderItem = totalOrderItem;
	}

	public int getTotalOrderItem()
	{
		return this.totalOrderItem;
	}
	
	public void setSubTOtal(double subTotal)
	{
		this.subTotal = subTotal;
	}

	public double getSubTOtal()
	{
		return this.subTotal;
	}
	
	public void setServiceTax(double serviceTax)
	{
		this.serviceTax = serviceTax;
	}

	public double getServiceTax()
	{
		return this.serviceTax;
	}
	
	public void setRounding(double rounding)
	{
		this.rounding = rounding;
	}

	public double getRounding()
	{
		return this.rounding;
	}
	
	public void setGrandTotal(double grandTotal)
	{
		this.grandTotal = grandTotal;
	}

	public double getGrandTotal()
	{
		return this.grandTotal;
	}
	
	public void setTenderedCash(double tenderedCash)
	{
		this.tenderedCash = tenderedCash;
	}

	public double setTenderedCash()
	{
		return this.tenderedCash;
	}
	
	public void setChange(double change)
	{
		this.change = change;
	}

	public double getChange()
	{
		return this.change;
	}
}
