/*Toumanidou Andromachi 3040185 - Texnhth Nohmosynh - Ergasia 2 - Etos 2018-2019
 * Class gia dhmioyrgia antikeimenwn pou paristanoun mhnymata ekpaideyshs me tis periexomenes lekseis tous kai thn kathgoria tous (spam / ham)
 */

import java.util.ArrayList;


public class Message 
{
	public static int totalSpam;		//metablhth static, krataei to synoliko ari8mo mhnymatwn ekpaideyshs spam
	public static int total;		//metablhth static, krataei to synoliko ari8mo olwn twn mhnymatwn ekpaideyshs
	
	private int id;						//id mhnymatos
	private ArrayList<Word> content;	//lista me tis periexomenes lekseis tou mhnymatos
	private boolean spam;				//boolean metablhth pou deixnei an einai 
	
	//kataskeyastes
	public Message()
	{
		this.id = ++total; //me ka8e dhmiourgia antikeimenou message ayksanetai kai to synolo twn mhnymatwn			
	}
	
	public Message(boolean spam)
	{
		this.id = ++total;	
		this.spam = spam;
	}
	
	public Message(ArrayList<Word> content)
	{
		this.id = ++total;
		this.content = content;
		this.spam = false;		
	}
	
	public Message(ArrayList<Word> content, boolean spam)
	{
		this.id = ++total;
		this.content = content;
		this.spam = spam;		
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public ArrayList<Word> getContent()
	{
		return this.content;
	}
	
	public void setContent(ArrayList<Word> content)
	{
		this.content = content;
	}
	
	public boolean isSpam()
	{
		return this.spam;
	}
	
	public void setSpam(boolean spam)
	{
		this.spam = spam;
	}
	
}
