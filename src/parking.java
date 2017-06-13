
import java.util.*;
import java.awt.*;
import java.io.*;

import javax.swing.*;

public class parking extends JFrame {
	private int numberOfPosition = 30,positionCounter=0;
	private ArrayList parkingList = new ArrayList();
	private ImageIcon image1 = new ImageIcon(parking.class.getResource("/images/a.png"));
    private ImageIcon image2 = new ImageIcon(parking.class.getResource("/images/b.png"));
    private String parkingID=null;
    private String[] userIDList;

	
	public void showLayout() throws FileNotFoundException {
		int[] state = new int[numberOfPosition];
		File file = new File("parkingInfo.txt");
		Scanner input = new Scanner(file);
		int count = 1;
		String inputString;
		while(input.hasNext()){
			for(int i = 1;i <= 5; i++){
				inputString=input.next();
				if(i == 3){
					state[count-1] = Integer.parseInt(inputString);
				}
			}
		count++;
		}
		input.close();
	
		for(int i = 1;i <= numberOfPosition; i++){
			if(state[i-1] == 0){			}
			else{
				JPanel temp = new JPanel(); 
				((JPanel) parkingList.get(i-1)).removeAll();
				((JPanel) parkingList.get(i-1)).add(new JLabel(image2),BorderLayout.CENTER);
				((JPanel) parkingList.get(i-1)).add(temp,BorderLayout.SOUTH);
				temp.add(new JLabel(Integer.toString(i)));		
				temp.updateUI();
			}
		}
		this.setVisible(true);
	}
	
	public parking() throws FileNotFoundException{
		Init();
	}
	public void Init() throws FileNotFoundException{
		File file = new File("parkingInfo.txt");
		Scanner input = new Scanner(file);
		this.setLayout(new GridLayout(0,8,5,3));
		for(int i = 1;i <= numberOfPosition; i++){
			JPanel p1 = new JPanel();
			JPanel p2 = new JPanel();
			p1.setLayout(new BorderLayout());
			p1.add(new JLabel(image1),BorderLayout.CENTER);
			p1.add(p2,BorderLayout.SOUTH);
			p2.add(new JLabel(Integer.toString(i)));			
			parkingList.add(p1);
		}
		for(int i = 1;i <= numberOfPosition; i++){
			this.getContentPane().add((Component) parkingList.get(i-1));
		}
		this.pack();
		this.setTitle("parking lot");
		this.setSize(600,450);
		this.setLocationRelativeTo(null);
		this.setBackground(SystemColor.activeCaption);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(false);
	}
	
	public String enter(String userID,entrance entrance1)throws IOException{
		String parkingID = allocate();
		if(parkingID==null)
			return null;
		if(userID.subSequence(0,1).equals("A"))
			entrance1.writeStaffRecord();
		String arrivalTime=entrance1.getEnterTime(userID);
		int parking_place = Integer.parseInt(parkingID.substring(3,5)); //ranging from 1-30
		ArrayList currentInfo = new ArrayList();
		File file = new File("parkingInfo.txt");
		Scanner input = new Scanner(file);
		while(input.hasNext()){
			currentInfo.add(input.next());
		}
		input.close();
		currentInfo.set((parking_place - 1) * 5,parkingID);
		currentInfo.set((parking_place - 1) * 5 + 1, userID);
		currentInfo.set((parking_place - 1)* 5 + 2, new String("1"));
		currentInfo.set((parking_place - 1)* 5 + 3, arrivalTime);
		currentInfo.set((parking_place - 1)* 5 + 4, "null");
		
		PrintWriter output = new PrintWriter(file);
		for(int i = 1; i <= numberOfPosition; i++){
			output.print(currentInfo.get((i-1)*5) + "  ");
			output.print(currentInfo.get((i-1)*5+1)+ "  ");
			output.print(currentInfo.get((i-1)*5+2)+ "  ");
			output.print(currentInfo.get((i-1)*5+3)+ "  ");
			output.println(currentInfo.get((i-1)*5+4)+ "  ");
		}
		output.close();	
		return parkingID;
	}
	
	public void leave(String userID)throws FileNotFoundException{
		int parking_place = 0;
		int count = 0;
		ArrayList<String> currentInfo = new ArrayList<String>();
		File file = new File("parkingInfo.txt");
		Scanner input = new Scanner(file);
		while(input.hasNext()){
			String temp = input.next();
			currentInfo.add(temp);
			if(userID.equals(temp))
				parking_place = count / 5 + 1;
			count++;
		}
		input.close();
		
		currentInfo.set((parking_place - 1) * 5 + 1, null);
		currentInfo.set((parking_place - 1)* 5 + 2, new String("0"));
		currentInfo.set((parking_place - 1)* 5 + 3, null);
		currentInfo.set((parking_place - 1)* 5 + 4, null);
		
		PrintWriter output = new PrintWriter(file);
		for(int i = 1; i <= numberOfPosition; i++){
			output.print(currentInfo.get((i-1)*5) + "  ");
			output.print(currentInfo.get((i-1)*5+1)+ "  ");
			output.print(currentInfo.get((i-1)*5+2)+ "  ");
			output.print(currentInfo.get((i-1)*5+3)+ "  ");
			output.println(currentInfo.get((i-1)*5+4)+ "  ");
		}
		output.flush();
		output.close();
	}
	
	public String allocate()throws FileNotFoundException{
		File file = new File("parkingInfo.txt");
		Scanner input = new Scanner(file);
		int flag = 1;
		int count = 0;
		String inputString;
		while(input.hasNext() && flag == 1 && count <= numberOfPosition){
			count++;
			for(int i = 1;i <= 5; i++){
				inputString=input.next();
				
				if(i == 3){
					if(Integer.parseInt(inputString) == 0)
						flag = 0;
				}
			}
		}
		input.close();
		if(flag == 1)
			return null;
		else if(count<10)
			return "P000" + Integer.toString(count);
		else
			return "P00" + Integer.toString(count);
	}
	
	public boolean userIDCheck(String userID) throws FileNotFoundException{
		userIDList=new String[numberOfPosition*4];
		File file = new File("parkingInfo.txt");
		Scanner input = new Scanner(file);
		int count = 1;
		while(input.hasNext()){
			for(int i = 1;i <= 5; i++){
				if(i==2)
					userIDList[count-1]=input.next();
			}
			count++;
			if(count>numberOfPosition*4)
				break;
		}
		input.close();
		for(positionCounter=0;positionCounter<numberOfPosition*4;positionCounter++){
			if(userID.equals(userIDList[positionCounter])){
				return true;
			}
		}
		return false;
	}
	
	public String getter(String userID) throws FileNotFoundException{
		File file = new File("parkingInfo.txt");
		Scanner input = new Scanner(file);
		String time = null;
		while(input.hasNext()){
			if(userID.equals(input.next())){
				input.next();
				time = input.next();
			}
		}
		input.close();
		return time;
	}
	
	public int counter() throws FileNotFoundException{
		int count=0,count2=0;
		File file = new File("parkingInfo.txt");
		Scanner input = new Scanner(file);
		while(input.hasNext() && count < numberOfPosition){
			count++;
			for(int i = 1;i <= 5; i++){
				String input2=input.next();
				if(i == 3){
					if(Integer.parseInt(input2) == 0)
						count2++;
				}
			}
		}
		return count2;
	}
}