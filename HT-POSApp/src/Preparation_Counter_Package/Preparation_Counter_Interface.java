package Preparation_Counter_Package;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import Database_Package.Database;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Robot;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

//Author of this class : HAW YUAN KANG , B032110034

// This class is to create an interface and functions for preparation counter.
public class Preparation_Counter_Interface extends JFrame 
{
	private JPanel contentPane;
	private static JButton Label_OrderList;
	private static ServerSocket socket;
	private static Socket s;
	private static DataInputStream InStream;
	private static DataOutputStream OutStream;
	private static String servernotify="";
	private static String operationrecord="";
	
	// Build Application interface
	public static void main(String[] args) throws IOException
	{
		// Set up socket and connect to server to receive and send data.
		socket= new ServerSocket (9999);
		s=socket.accept();		
		InStream=new DataInputStream(s.getInputStream());
		OutStream=new DataOutputStream(s.getOutputStream());

		// Send data to server to check connection.
		operationrecord = "Preparation counter connected to server.";
		OutStream.writeUTF(operationrecord);		
		
		// Display preparation counter interface.
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					Preparation_Counter_Interface frame = new Preparation_Counter_Interface();
					frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
		
		// Get notify from server to notice there is new order coming in.
		while(true)
		{
			try
			{
				servernotify=InStream.readUTF();
				// Robot to perform auto click to refresh the display order list.
				Point point = new Point();
				point = Label_OrderList.getLocationOnScreen();
				Robot robot=new Robot();
				robot.mouseMove(point.x,point.y);
				robot.mousePress(16);
			}
			catch(Exception ab)
			{
				// Waiting for signal from the server
			}		
		}
	}

	// Create frame for preparation counter
	public Preparation_Counter_Interface() 
	{
		// Create background panel
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 575, 397);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Create label order list
		Label_OrderList=new JButton("ORDER LIST");
		Label_OrderList.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		Label_OrderList.setBounds(22, 10, 149, 34);
		contentPane.add(Label_OrderList);
	
		// Create scroll panel
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(22, 54, 529, 238);
		contentPane.add(scrollPane);
		
		// Set grid panel into scroll panel
		JPanel gridpanel = new JPanel();
		gridpanel.setBackground(Color.WHITE);
		scrollPane.setViewportView(gridpanel);
		gridpanel.setLayout(new GridLayout(0,1,30,30));
		
		// Event to click button order list to refresh page
		Label_OrderList.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mousePressed(MouseEvent event) 
			{
				// Clear exist panel to refresh the frame
				gridpanel.removeAll();
				gridpanel.setVisible(false);
				gridpanel.setVisible(true);
				
				try
				{
					// Declare order id and item name to store id and name into them correspondingly.
					int OrderId=0;
					String ItemName="";
					
					// Get order id from table order.
					Database dbCtrl = new Database();
					Connection connection = dbCtrl.getConnection();					
					String sql = "SELECT * FROM `order`";
					Statement st = connection.createStatement();
					ResultSet rs = st.executeQuery(sql);
					while(rs.next())
					{
						// Store order id and convert it into string.
						OrderId = rs.getInt("OrderId");
						String str_temp = Integer.toString(OrderId);
						
						// Create many panel
						JPanel panel1 = new JPanel();	
						panel1.setLayout(new GridLayout(0,1));
						gridpanel.add(panel1);
						panel1.setBounds(0, 0, 50, 50);
						panel1.setBackground(Color.LIGHT_GRAY);
						
						// Create label id into panel1
						JLabel Label_Id = new JLabel();
						Label_Id.setFont(new Font("Times New Roman", Font.PLAIN, 20));
						panel1.add(Label_Id);
						Label_Id.setText("Order Id - "+str_temp);
						Label_Id.setHorizontalAlignment(JLabel.CENTER);
						
						try
						{
							// Get order item details from table orderitem
							Database dbCtrl1 = new Database();
							Connection connection1 = dbCtrl1.getConnection();					
							String sql1 = "SELECT * FROM `orderitem` WHERE `Order`='"+OrderId+"'";
							Statement st1 = connection1.createStatement();
							ResultSet rs1 = st1.executeQuery(sql1);						
							while(rs1.next())
							{
								// Store item id and item quantity
								String ItemId=rs1.getString("ItemProduct");
								int ItemQuantity=rs1.getInt("Quantity");
								
								// Refer to table item product
								try
								{
									// Get item details from table item product
									Database dbCtrl2 = new Database();
									Connection connection2 = dbCtrl2.getConnection();									
									String sql2 = "SELECT * FROM `itemproduct` WHERE "
											+ "`ItemProductId`='"+ItemId+"'";
									Statement st2 = connection2.createStatement();
									ResultSet rs2 = st2.executeQuery(sql2);									
									while(rs2.next())
									{
										ItemName=rs2.getString("Name");
									}
								}
								catch(Exception c)
								{
									JOptionPane.showMessageDialog(null, c);
								}
								
								// Create label item name and display
								JLabel Label_Name = new JLabel();
								Label_Name.setFont(new Font("Times New Roman", Font.PLAIN, 20));
								panel1.add(Label_Name);
								Label_Name.setText(ItemName + " x"+ItemQuantity);
								Label_Name.setHorizontalAlignment(JLabel.CENTER);
							}
						}
						catch(Exception b)
						{
							JOptionPane.showMessageDialog(null, b);
						}
						
						// Create button temporary ready
						JButton Button_Temp = new JButton();
						Button_Temp.setFont(new Font("Times New Roman", Font.PLAIN, 20));
						panel1.add(Button_Temp);
						Button_Temp.setText("Ready");
						Button_Temp.setHorizontalAlignment(JButton.CENTER);
						
						// Button click to change order item status
						Button_Temp.addMouseListener(new MouseAdapter()
						{
							@Override
							public void mousePressed(MouseEvent e) 
							{
								// When the order item is ready to be collected					
								if(Button_Temp.getText()=="Ready")
								{
									// Update order status and ready time
									UpdateOrderStatus(str_temp);
									
									// Change button text from ready to collected
									Button_Temp.setText("Collected");		
									
									// Send operation record to server
									operationrecord = "Preparation counter change status of a order"
											+ " to 'Collected'.";
									try {
										OutStream.writeUTF(operationrecord);
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
																
									// Print sticker
									PrintSticker(str_temp);
									
									// Send operation record to server
									operationrecord = "Preparation counter show sticker of an order"
											+ " for print purpose.";
									try {
										OutStream.writeUTF(operationrecord);
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
								else
								{
									try
									{
										// Remove collected item from table orderitem 
										Database dbCtrl2 = new Database();
										Connection connection2 = dbCtrl2.getConnection();										
										String sql2 = "DELETE FROM `orderitem` WHERE `Order`='"+str_temp+"'";
										PreparedStatement statement2 = connection2.prepareStatement(sql2);
										statement2.execute();
										
										// Remove collected item from table order 
										String sql3 = "DELETE FROM `order` WHERE `OrderId`='"+str_temp+"'";
										PreparedStatement statement3 = connection2.prepareStatement(sql3);
										statement3.execute();
										
										// Auto refresh the order list
										Point point = new Point();
										point = Label_OrderList.getLocationOnScreen();
										Robot robot=new Robot();
										robot.mouseMove(point.x,point.y);
										robot.mousePress(16);
										
										// Send operation record to server
										operationrecord = "Preparation counter removed an order.";
										OutStream.writeUTF(operationrecord);
									}
									catch(Exception d)
									{
										JOptionPane.showMessageDialog(null, d);
									}
								}
							}
						});
					}
				}
				catch(Exception a)
				{
					JOptionPane.showMessageDialog(null, a);
				}
			}
		});		
	}
	
	// Create and print receipt of a ready order
	public void PrintSticker(String collectedorderid)
	{
		// Set variable to store value that get from database
		// Variables that store value from table order
		int OrderId = 0;
		int OrderNum = 0;
		String DateTimeNow="";
		int OrderQuantity=0;
		
		// Variables that store value from table orderitem
		int ItemId;
		int ItemQuantity;
		
		// Variables that store value from table itemproduct
		int Sequence=0;
		String ItemName;
		String ItemDetails = "";
		
		try
		{
			// Get order id and order number from table order
			Database dbCtrl4 = new Database();
			Connection connection4 = dbCtrl4.getConnection();					
			String sql4 = "SELECT * FROM `order` WHERE `OrderId` = '"+collectedorderid+"'";
			Statement st4 = connection4.createStatement();
			ResultSet rs4 = st4.executeQuery(sql4);					
			while(rs4.next())
			{
				// Store order id, order number, transaction date and order quantity
				OrderId=rs4.getInt("OrderId");
				OrderNum=rs4.getInt("OrderNumber");
				DateTimeNow=rs4.getString("TransactionDate");
				OrderQuantity=rs4.getInt("TotalOrderItem");
			}
			
			try
			{
				// Get order item details from table otderitem
				Database dbCtrl5 = new Database();
				Connection connection5 = dbCtrl5.getConnection();					
				String sql5 = "SELECT * FROM `orderitem` WHERE `Order` ='"+OrderId+"'";
				Statement st5 = connection4.createStatement();
				ResultSet rs5 = st5.executeQuery(sql5);					
				while(rs5.next())
				{
					// Store item id and item quantity
					ItemId=rs5.getInt("ItemProduct");
					ItemQuantity=rs5.getInt("Quantity");
					
					try
					{
						// Get item product name from table itemproduct
						Database dbCtrl6 = new Database();
						Connection connection6 = dbCtrl6.getConnection();					
						String sql6 = "SELECT * FROM `itemproduct` WHERE `ItemProductId` ='"+ItemId+"'";
						Statement st6 = connection4.createStatement();
						ResultSet rs6 = st6.executeQuery(sql6);					
						while(rs6.next())
						{
							// Loop to get each item quantity and display them
							for(int i=0;i<OrderQuantity;i++)
							{
								// Store item name
								ItemName=rs6.getString("Name");			
								
								// Set sticker content
								ItemDetails=ItemDetails + 
										"------------------------------------------------------------\n"+
										"HornetTea FTMK UTem\n"+
										"Order Number: "+OrderNum+"\n"+
										"Date: "+DateTimeNow+"\n\n"+
										"Name: \n"+
										ItemName+"\n\n"+
										"Sequence: "+(Sequence=Sequence+1)+" / "+ OrderQuantity+"\n"+
										"------------------------------------------------------------\n\n";
							}
						}
					}
					catch(Exception g)
					{
						JOptionPane.showMessageDialog(null, g);
					}
				}
			}
			catch(Exception f)
			{
				JOptionPane.showMessageDialog(null, f);
			}
			
			// Show order sticker and print it
			UIManager.put("OptionPane.okButtonText", "PRINT");
			JOptionPane.showMessageDialog(null, ItemDetails, "ORDER STICKER", JOptionPane.PLAIN_MESSAGE);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	// Function that use to update the order status and ready time of the table orderitem
	public void UpdateOrderStatus(String readyorderid)
	{
		try
		{
			// Set the status to ready of an order when barista finish making that order item list
			String NewStatus="Ready";
			
			// Set the date and time when barista click button ready to 
			// update the ready time of the table orderitem
			Date nowdatetime = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
			String CurrentDateTime = format.format(nowdatetime);
			
			// Update order status and ready time of the table orderitem
			Database dbCtrl7 = new Database();
			Connection connection7 = dbCtrl7.getConnection();					
			String sql7 = "UPDATE `orderitem` SET `OrderStatus`= '"+NewStatus+"',`ReadyTime`= '"+CurrentDateTime+"' "
					+ "WHERE `Order` = '"+readyorderid+"'";
			Statement st7 = connection7.createStatement();
			st7.execute(sql7);
		}
		catch(Exception g)
		{
			JOptionPane.showMessageDialog(null, g);
		}
	}
}
