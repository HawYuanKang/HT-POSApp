package Order_Counter_Package;
import Database_Package.Database;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;

//Author of this class : TAY GUI XIAN , B032110019

// This class is for order counter.
public class Order_Counter_Interface extends JFrame 
{
	// Initials values that will be used later in the code.
	private JPanel contentPane;
	private JTable table;
	JLabel label_total = new JLabel("Grand Total");
	ItemProduct itemproduct = new ItemProduct();
	Calculation calculate = new Calculation();

	// temporary Array to store order item details from database
	ArrayList<Integer> array_id = new ArrayList<Integer>();
	ArrayList<String> array_name = new ArrayList<String>();
	ArrayList<Integer> array_quantity = new ArrayList<Integer>();
	ArrayList<Double> array_price = new ArrayList<Double>();
	
	int int_temp;
	String string_temp;
	Double double_temp;
	Double raw_total;
	Double grand_total;
	Double round;
	
	private JTextField textField;
	private static String test="";
	private static ServerSocket socket;
	private static Socket s;
	private static DataOutputStream OutStream;

	// Launch Application
	public static void main(String[] args) throws IOException 
	{
		// Setup server socket with the same port and connect the correct server.
		socket= new ServerSocket (9999);
		s=socket.accept();		
		OutStream=new DataOutputStream(s.getOutputStream());
		
		// Send connection message to server if it successfully connected to the server
		test = "Order counter connected to server";
		OutStream.writeUTF(test);		
	
		// Call interface
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					Order_Counter_Interface frame = new Order_Counter_Interface();
					frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}


	// Create Interface for order counter
	public Order_Counter_Interface() 
	{
		// Initial value to variable, will use it later.
		raw_total=0.0;
		
		// Set frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 515, 384);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Payment frame that will pop up for cashier to make payment
		JInternalFrame internalFrame = new JInternalFrame("New JInternalFrame");
		internalFrame.setBounds(10, 10, 471, 327);
		contentPane.add(internalFrame);
		internalFrame.getContentPane().setLayout(null);
		
		//label for payment frame amount
		JLabel lblAmountRm = new JLabel("Amount : RM");
		lblAmountRm.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblAmountRm.setBounds(58, 51, 146, 47);
		internalFrame.getContentPane().add(lblAmountRm);
		
		//text field for cashier to key-in tendered cash
		textField = new JTextField();
		textField.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		textField.setBounds(58, 108, 276, 47);
		internalFrame.getContentPane().add(textField);
		textField.setColumns(10);
		
		//button to confirm amount paid
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Times New Roman", Font.PLAIN, 24));
		btnCancel.setBounds(58, 191, 119, 53);
		internalFrame.getContentPane().add(btnCancel);
		
		//button to cancel pay
		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.setFont(new Font("Times New Roman", Font.PLAIN, 24));
		btnConfirm.setBounds(215, 191, 119, 53);
		internalFrame.getContentPane().add(btnConfirm);
		
		//label to display the amount of grand total
		JLabel label_total_amount = new JLabel("Grand Total: RM");
		label_total_amount.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		label_total_amount.setBounds(58, 10, 361, 47);
		internalFrame.getContentPane().add(label_total_amount);
		
		//label for text "menu"
		JLabel label_menu = new JLabel("Drinks Menu:");
		label_menu.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		label_menu.setBounds(35, 28, 124, 38);
		contentPane.add(label_menu);
		
		// combobox that includes all the available drinks 
		JComboBox comboBox = new JComboBox();
		comboBox.setFocusable(false);
		comboBox.setBounds(35, 76, 321, 36);
		contentPane.add(comboBox);

		// Get all item product name from database table itemproduct.
		try 
		{
			// Open connection.
			Database dbCtrl = new Database();
			Connection connection = dbCtrl.getConnection();
			
			// Set query to get all data.
			String sql = "Select * from itemproduct;";
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next())
			{
				// Load data into variables
				String tempp = rs.getString("Name");
				itemproduct.SetName(tempp);
				// Add all the drinks's name into combo box
				comboBox.addItem(itemproduct.GetName());
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		// Button to add data into order list
		JButton button_add = new JButton("Add");
		button_add.setFocusable(false);
		button_add.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		button_add.setBounds(366, 76, 89, 36);
		contentPane.add(button_add);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(35, 155, 421, 120);
		contentPane.add(scrollPane);
		
		// Table will display all the needed information of order list.
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] {"Sequence", "ID", "Name", "Quantity", "Sub-Total"}
		));
		scrollPane.setViewportView(table);
		
		// Label for total price.
		label_total.setHorizontalAlignment(SwingConstants.RIGHT);
		label_total.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		label_total.setBounds(248, 285, 207, 30);
		contentPane.add(label_total);
		
		// Button cancel to clear order list.
		JButton button_cancel = new JButton("Cancel");
		button_cancel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		button_cancel.setBounds(35, 279, 89, 36);
		contentPane.add(button_cancel);
		
		// Button paycash to proceed to payment.
		JButton button_paycash = new JButton("Pay Cash");
		button_paycash.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		button_paycash.setBounds(134, 279, 116, 36);
		contentPane.add(button_paycash);
		
		//events
		//Click add button
		button_add.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				String name;
				name = comboBox.getSelectedItem().toString();

				try 
				{
					Database dbCtrl = new Database();
					Connection connection = dbCtrl.getConnection();
					
					// Query to get all the information of certain item product with its name.
					String sql = "Select * from itemproduct where Name = '"+ name +"'";
					Statement st = connection.createStatement();
					ResultSet rs = st.executeQuery(sql);
					
					while(rs.next())
					{
						// Load data into temporary variables
						int tempp = rs.getInt("ItemProductID");
						String temp_name = rs.getString("Name");
						Double temp_price = rs.getDouble("Price");
						
						// If the item already inside the list, then increase its quantity.
						if(array_id.contains(tempp))
						{
							int_temp = array_id.indexOf(tempp);
							int current_quantity = array_quantity.get(int_temp);
							int new_quantity = current_quantity + 1;
							array_quantity.set(int_temp, new_quantity);
							test = "Order counter added a new item (id: "+tempp+") into order list.";
							OutStream.writeUTF(test);
						}
						// If item name cannot be found inside the list, which means it is the first new drink in the order list.
						else
						{
							array_id.add(tempp);
							array_name.add(temp_name);
							array_quantity.add(1);
							array_price.add(temp_price);
							test = "Order counter added a new item (id: "+tempp+") into order list.";
							OutStream.writeUTF(test);
						}
						//Display function / refresh Order List Table
						DisplayTable();
					}
				} 
				catch (Exception ee) 
				{
					JOptionPane.showMessageDialog(null, ee, ee + "", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		// Event click table cell to reduce quantity according to row
		table.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mousePressed(MouseEvent e) 
			{
				JTable target = (JTable)e.getSource();
	            int roww = target.getSelectedRow();
	            // Reduce the order item quantity by 1 if it is more than a single
	            if (array_quantity.get(roww) > 1)
	            {
	            	int new_quantity = array_quantity.get(roww) - 1;
	            	array_quantity.set(roww, new_quantity);
	            	
	            	// TCP: send operation message to server.
	            	test = "Order counter removed a item (row: "+roww+") from order list.";
					try 
					{
						OutStream.writeUTF(test);
					} 
					catch (IOException e1) 
					{
						e1.printStackTrace();
					}
	            }
	            // Remove drink from order list table because its quantity is 1.
	            else
	            {
	            	array_id.remove(roww);
	            	array_name.remove(roww);
	            	array_quantity.remove(roww);
	            	array_price.remove(roww);
	            	
	            	// TCP: operation message.
	            	test = "Order counter removed a item (row: "+roww+") from order list.";
					try 
					{
						OutStream.writeUTF(test);
					} 
					catch (IOException e1) 
					{
						e1.printStackTrace();
					}
	            }
	            // Display / refresh order list table.
	            DisplayTable();
			}
		});
		
		// Event click cancel button to clear list
		button_cancel.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mousePressed(MouseEvent e) 
			{
				array_id.clear();
            	array_name.clear();
            	array_quantity.clear();
            	array_price.clear();
            	DisplayTable();
            	label_total.setText( "Grand Total");	
            	raw_total=0.0;
            	
            	// TCP: operation message
            	test = "Order counter removed all item of the order list.";
				try 
				{
					OutStream.writeUTF(test);
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
		});
		
		// Event click pay cash button to proceed payment
		button_paycash.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mousePressed(MouseEvent e) 
			{
				// Validates if there is no item in the order list.
				if(raw_total<=0.0)
				{
					JOptionPane.showMessageDialog(null, "Dont have any Item");
					//TCP: operation message
	            	test = "Order counter unable to process payment due to reason don't have any item in list.";
					try 
					{
						OutStream.writeUTF(test);
					} 
					catch (IOException e1) 
					{
						e1.printStackTrace();
					}
				}
				// Display payment panel for cashier to use.
				else
				{
					button_paycash.setVisible(false);
					button_cancel.setVisible(false);
					label_total_amount.setText("Grand Total: RM "+grand_total.toString());
					internalFrame.setVisible(true);
					//TCP: operation message.
	            	test = "Order counter processing payment.";
					try 
					{
						OutStream.writeUTF(test);
					} 
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		});
		
		// Event click to cancel payment (close payment panel)
		btnCancel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e) 
			{
				button_paycash.setVisible(true);
				button_cancel.setVisible(true);
				internalFrame.setVisible(false);
				textField.setText(null);
				//TCP: operation message
            	test = "Order counter cancel payment.";
				try 
				{
					OutStream.writeUTF(test);
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
		});
		
		// Event click to confirm payment
		btnConfirm.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e) 
			{
				try
				{
					double amount=Double.parseDouble(textField.getText());
					// Validate not enough tendered cash for payment.
					if(amount<raw_total)
					{
						JOptionPane.showMessageDialog(null, "Not enough amount");
						
		            	test = "Order counter receive not enough amount for payment.";
						OutStream.writeUTF(test);
					}
					// Make payment.
					else 
					{
							grand_total = Math.floor(grand_total * 1e2) / 1e2;
							double change_amount=amount-grand_total;
							DecimalFormat df = new DecimalFormat("#.#");
							df.setRoundingMode(RoundingMode.CEILING);
							JOptionPane.showMessageDialog(null, "Amount change: RM "+df.format(change_amount));
							
							// Insert order details into database
							try 
							{
								// Check latest order number from the data.
								int ordernum=0;
								try
								{
									Database dbCtrl = new Database();
									Connection connection = dbCtrl.getConnection();
									
									String sql = "SELECT * FROM `order`";
									Statement st = connection.createStatement();
									ResultSet rs = st.executeQuery(sql);
									
									while(rs.next())
									{
										ordernum=rs.getInt("OrderNumber");
									}
							
								}
								catch(Exception b)
								{
									JOptionPane.showMessageDialog(null, "Yataa"+b);
								}
								// Create new order number.
								ordernum=ordernum+1;
								
								// Get transaction date / current time.
								java.util.Date date = new java.util.Date();
								Object param = new java.sql.Timestamp(date.getTime());
								
								// Tendered cash amount.
								double receiveamount=Double.parseDouble(textField.getText());
								
								// Total order item quantity.
								int totalquantity=0;
								for(int i=0;i<array_id.size();i++)
								{
									totalquantity = totalquantity+array_quantity.get(i);
								}

								// Service tax 6% for order subtotal
								double servicetax=double_temp*0.06;
								
								// Insert new order into database order table
								Database dbCtrl1 = new Database();
								Connection conn = dbCtrl1.getConnection();
								String sql="INSERT INTO `order` (`OrderId`, `OrderNumber`, `TransactionDate`, `GrandTotal`, `TenderedCash`, `Change`, `TotalOrderItem`, `SubTotal`, `Rounding`, `ServiceTax`) VALUES (NULL, '"+ordernum+"', current_timestamp(), '"+grand_total+"', '"+receiveamount+"', '"+change_amount+"', '"+totalquantity+"', '"+double_temp+"', '"+round+"', '"+servicetax+"')";
								PreparedStatement statement = conn.prepareStatement(sql);
								statement.execute();
								
								// Get foreign key order id
								int orderid=0;
								Database dbCtrl = new Database();
								Connection connection = dbCtrl.getConnection();
								
								String sql3 = "SELECT * FROM `order`";
								Statement st = connection.createStatement();
								ResultSet rs = st.executeQuery(sql3);
								
								while(rs.next())
								{
									orderid=rs.getInt("OrderId");
								}
								
								// Insert items into database orderitem table
								Database dbCtrl2 = new Database();
								Connection conne = dbCtrl2.getConnection();
								
								for (int i=0; i<array_id.size();i++)
								{
									int itemid=array_id.get(i);
									int subquantity=array_quantity.get(i);
									double subtotal= array_price.get(i) * subquantity;
									
									String sql2="INSERT INTO `orderitem` (`OrderItem`, `ItemProduct`, `Order`, `Quantity`, `SubTotalAmount`, `OrderStatus`, `ReadyTime`) VALUES (NULL, '"+itemid+"', '"+orderid+"', '"+subquantity+"', '"+subtotal+"', 'Processing', NULL)";
									PreparedStatement statement1 = conne.prepareStatement(sql2);
									statement1.execute();
								}
							} 
							catch (Exception i) 
							{
								JOptionPane.showMessageDialog(null, i);
							}
							
							// Back to default frame.
							button_paycash.setVisible(true);
							button_cancel.setVisible(true);
							internalFrame.setVisible(false);
							textField.setText(null);
							array_id.clear();
			            	array_name.clear();
			            	array_quantity.clear();
			            	array_price.clear();
			            	DisplayTable();
			            	label_total.setText( "Grand Total");	
			            	raw_total=0.0;
			            	//TCP: operation message
							test = "Order counter transaction payment made successfully.";
							OutStream.writeUTF(test);
							// Display receipt for cashier to print.
							PrintReceipt();
							//TCP: operation message
							test = "Order counter generate receipt for cashier to print.";
							OutStream.writeUTF(test);	  
					}          	
				}
				catch(Exception a)
				{
					JOptionPane.showMessageDialog(null, "Invalid Input Of Amount");
					//TCP: operation message
					test = "Order counter receive invalid input for payment.";
					try 
					{
						OutStream.writeUTF(test);
					} 
					catch (IOException e1) 
					{
						e1.printStackTrace();
					}
				}
			}
		});
	}
	
	// Display order table list
	public void DisplayTable()
	{
			// Set price format.
			DecimalFormat df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.CEILING);
			double_temp = 0.0;
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			model.setRowCount(0);
			Object[] row = new Object[5];
			// Set values of order details to each column.
			for (int i=0; i<array_id.size();i++)
			{
				row[0] = i+1;
				row[1] = array_id.get(i);
				row[2] = array_name.get(i);
				row[3] = array_quantity.get(i);
				
				// Call function to calculate subtotal.
				double subtotal=calculate.calculate_subtotal(array_quantity.get(i), array_price.get(i));
				row[4] = df.format(subtotal);
				model.addRow(row);
				double_temp = double_temp + subtotal;
			}
			raw_total= double_temp * 1.06;
			raw_total = Double.parseDouble(df.format(raw_total)) ;
			
			// Call rounding function.
			round = calculate.rounding_total(raw_total);
			round = Math.floor(round * 1e2) / 1e2;
			grand_total = raw_total + round;
			grand_total = Math.floor(grand_total * 1e2) / 1e2;
			label_total.setText( "Grand Total: RM " + grand_total);
	}
	
	// Print receipt of current order
	public void PrintReceipt()
	{
		//Initialize variable
		// Variables to store data from database order table
		int order_id = 0;
		int order_number = 0;
		String date="";
		double grand_total=0;
		double tender_cash=0;
		double change=0;
		int total_quantity=0;
		double sub_total=0;
		double rounding =0;
		double service_tax=0;
		
		// Variables to store data from database orderitem table
		int item_id;
		int item_quantity;
		double item_subtotal;
		
		// Variables to store data from database itemproduct table
		String item_name;
		String item_details = "";
		
		// Retrieve data from database
		try
		{
			// Get details from order table
			Database dbCtrl = new Database();
			Connection connection = dbCtrl.getConnection();		
			String sql = "SELECT * FROM `order`";
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next())
			{
				order_id =rs.getInt("OrderId");
				order_number =rs.getInt("OrderNumber");
				date =rs.getString("TransactionDate");
				grand_total =rs.getDouble("GrandTotal");
				tender_cash =rs.getDouble("TenderedCash");
				change =rs.getDouble("Change");
				total_quantity =rs.getInt("TotalOrderItem");
				sub_total =rs.getDouble("SubTotal");
				rounding =rs.getDouble("Rounding");
				service_tax =rs.getDouble("ServiceTax");
			}
			try
			{
				Database dbCtrl1 = new Database();
				Connection connection1 = dbCtrl1.getConnection();
				// Query to select all the orderitem data according to the order id.
				String sql1 = "SELECT * FROM `orderitem` where `Order` = '"+order_id+"'";
				Statement st1 = connection1.createStatement();
				ResultSet rs1 = st1.executeQuery(sql1);
				
				while(rs1.next())
				{
					item_id =rs1.getInt("ItemProduct");
					item_quantity =rs1.getInt("Quantity");
					item_subtotal =rs1.getDouble("SubTotalAmount");
					
					try
					{
						Database dbCtrl2 = new Database();
						Connection connection2 = dbCtrl2.getConnection();
						// Query to select all the itemproduct data according to the order id.
						String sql2 = "SELECT * FROM `itemproduct` where `ItemProductId` = '"+item_id+"'";
						Statement st2 = connection2.createStatement();
						ResultSet rs2 = st2.executeQuery(sql2);
						
						while(rs2.next())
						{
							item_name =rs2.getString("Name");
							// Set string for item details (certain part of receipt)
							item_details = item_details + item_name + "  x" + item_quantity + "   RM " + item_subtotal + "\n";
						}
					}
					catch(Exception x)
					{
						JOptionPane.showMessageDialog(null, x);
					}
				}
			}
			catch(Exception y)
			{
				JOptionPane.showMessageDialog(null, y);
			}
		}
		catch(Exception z)
		{
			JOptionPane.showMessageDialog(null, z);
		}
		
		
		// Set receipt display format.
		UIManager.put("OptionPane.okButtonText", "PRINT");
		JOptionPane.showMessageDialog(null, 
		"-----------------------------------------------------------------\n" +
		"Your order number is: "+ order_number + "\n" +
		"-----------------------------------------------------------------\n" +
		"HornetTea                                 \n" +
		"FICTS                                     \n" +
		"Fakulti Teknologi Maklumat dan Komunikasi \n" +
		"Universiti Teknikal Malaysia Melaka       \n" +
		"Hang Tuah Jaya, 76100 Durian Tunggal      \n" +
		"Melaka, Malaysia                          \n" +
		"-----------------------------------------------------------------\n" +
		"Invoice                                   \n" +
		"                                          \n" +
		"Bill No: " + order_id + "\n" +
		"Date: " + date + "\n" +
		"                                          \n" +
		"Details                                   \n" +
		"-----------------------------------------------------------------\n" +
		"Item Name Qty Price(RM)                   \n" +
		"-----------------------------------------------------------------\n" +
		item_details +
		"-----------------------------------------------------------------\n" +
		"Total Item                                           " + total_quantity + "\n" +
		"                                          \n" +
		"                     Sub total                       " + sub_total + "\n" +
		"                     Service Tax (6%)         " + service_tax + "\n" +
		"                     Rounding                      " + rounding + "\n" +
		"-----------------------------------------------------------------\n" +
		"                     Grand Total                  " + grand_total+ "\n" +
		"                                          \n" +
		"                     Tendered Cash            " + tender_cash + "\n" +
		"                     Change                          " + change + "\n" +
		"-----------------------------------------------------------------\n" +
		"            Thank you and have a good day       \n",
	    "RECEIPT", JOptionPane.PLAIN_MESSAGE);
	}
}
