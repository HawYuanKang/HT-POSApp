package Order_Counter_Package;

import java.util.Date;

//Author of this class : ARIEF MUIZZUDDIN BIN KHALID , B032110508

public class OrderItem 
{
	private int orderItemId;
	private ItemProduct itemProduct;
	private int quantity;
	private double subTotalAmount;
	private int sequenceNumber;
	private String orderStatus;
	private Date readTime;
	
	public void SetOrderItemId(int id)
	{
		this.orderItemId = id;
	}

	public int GetOrderItemId()
	{
		return this.orderItemId;
	}
	
	public void SetItemProduct(ItemProduct product)
	{
		this.itemProduct = product;
	}

	public ItemProduct GetItemProduct()
	{
		return this.itemProduct;
	}
	
	public void SetQuantity(int quantity)
	{
		this.quantity = quantity;
	}

	public int GetQuantity()
	{
		return this.quantity;
	}
	
	public void SetSubTotalAmount(double subTotalAmount)
	{
		this.subTotalAmount = subTotalAmount;
	}

	public double GetsubTotalAmount()
	{
		return this.subTotalAmount;
	}
	
	public void SetSequenceNumber(int sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	public double GetSequenceNumber()
	{
		return this.sequenceNumber;
	}
	
	public void SetOrderStatus(String orderStatus)
	{
		this.orderStatus = orderStatus;
	}

	public String orderStatus()
	{
		return this.orderStatus;
	}
	
	public void SetReadTime(Date readTime)
	{
		this.readTime = readTime;
	}

	public Date GetreadTime()
	{
		return this.readTime;
	}
}
