/*Toumanidou Andromachi 3040185 - Texnhth Nohmosynh - Ergasia 2 - Etos 2018-2019
 * Class pou periexei th main() kai me8odous pou ylopoioyn ton algori8mo ma8hshs Naive Bayes 
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class SpamFilter 
{
	static HashMap<String, Word> dictionary = new HashMap<String,Word>();	//HashMap gia thn dhmiourgia leksikou me tis lekseis pou exoun emfanistei
	static BufferedWriter predictionResults;								//stream gia thn apo8hkeysh twn problepsewn se arxeio
	
	
	public static void main(String args[]) throws IOException
	{		
		ArrayList<Message> trainingMessages = trainingFilter();	//klhsh ths training filter() - ekpaideysh tou montelou kai epistrofh twn mhnymatwn ekpaideyshs se ArrayList (den aksiopoiw thn epistrefomenh lista sta plaisia ths ergasias, alla 8a mporouse na xrhsimopoih8ei se parapanw ypologismous, xwris na xreiastei na ksanafortw8oun ta mhnymata ekpaideyshs apo ta arxeia

//		predictionResults = new BufferedWriter(new FileWriter("results_train_enron1_test_enron2.csv", true));						
//		predictionResults = new BufferedWriter(new FileWriter("results_train_enron1_test_enron1.csv", true));
		predictionResults = new BufferedWriter(new FileWriter("results_train_enron1_enron2_enron3_enron4_test_enron5.csv", true));	//ta apotelesmata apo8hkeyontai se arxeio .csv kai to diaxwristiko einai to " "
		predictionResults.write("MessageFile Spam Prediction");	//epikefalides sthlwn sto arxeio
		predictionResults.newLine();	//allagh grammhs sto arxeio
		
		int[] results = predict();	//problepseis gia ta mhnymata elegxou kai epistrofh tou plh8ous twn TruePositives, FalsePositives, FalseNegatives, TrueNegatives se array 4 akeraiwn

		predictionResults.close();	//kleisimo tou arxeiou twn apotelesmatwn
		
		//ektypwseis sthn konsola
		System.out.println("Dictionary content: "+dictionary.keySet());
		System.out.println();
		System.out.println("Dictionary has "+dictionary.size()+" distincts words.");
		System.out.println();
		System.out.println("Total training messages: "+Message.total);
		System.out.println("Total spam training messages: "+Message.totalSpam);
		System.out.println();
		System.out.println("TruePositives: "+(results[0]));
		System.out.println("FalsePositives: "+(results[1]));
		System.out.println("FalseNegatives: "+(results[2]));
		System.out.println("TrueNegatives: "+(results[3]));
		
		//ypologismoi akribeias, anaklhshs, sfalamatos kai F1 score						//typoi ypologismou
		double precision = (double)results[0]/(double)(results[0]+results[1]); 			//Precision = TruePositives/(TruePositives+FalsePositives)
		double recall = (double)results[0]/(double)(results[0]+results[2]); 			//Recall = TruePositives/(TruePositives+FalseNegatives)
		double accuracy =(double)(results[0]+results[3])/(double)(results[0]+results[1]+results[2]+results[3]); 		//Accuracy = (TruePositives+TrueNegatives)/(TruePositives+FalsePositives+FalseNegatives+TrueNegatives)
		double f1 =(double) 2*precision*recall/(double)(precision+recall);				//F1 Score = 2*(Recall * Precision) / (Recall + Precision)
		
		//ektypwseis sthn konsola
		System.out.println("");
		System.out.println();
		System.out.println("Precision: "+precision);
		System.out.println("Recall: "+recall);
		System.out.println("Accuracy: "+accuracy);
		System.out.println("F1 Score: "+f1);
		
	}
	
	static ArrayList<Message> trainingFilter() throws IOException
	{																								//klhsh ths getTrainFolderMessages() me orismata to path tou fakelou pou periexei ta mhnymata kai an prokeitai gia spam(true) h ham (false) mhnymata
		ArrayList<Message> hamTrainMessages = getTrainFolderMessages("./enron1/ham/", false);		//eisagwgh dedomenwn ekpaideyshs. Xrhsimopoih8hkan yposynola tou synolou dedomenwn Enron-Spam
		ArrayList<Message> spamTrainMessages = getTrainFolderMessages("./enron1/spam/", true);		
		ArrayList<Message> hamTrainMessages2 = getTrainFolderMessages("./enron2/ham/", false);
		ArrayList<Message> spamTrainMessages2 = getTrainFolderMessages("./enron2/spam/", true);
		ArrayList<Message> hamTrainMessages3 = getTrainFolderMessages("./enron3/ham/", false);
		ArrayList<Message> spamTrainMessages3 = getTrainFolderMessages("./enron3/spam/", true);
		ArrayList<Message> hamTrainMessages4 = getTrainFolderMessages("./enron4/ham/", false);
		ArrayList<Message> spamTrainMessages4 = getTrainFolderMessages("./enron4/spam/", true);
		
		ArrayList<Message> allTrainMessages = new ArrayList<Message>();		//ArrayList mhnymatwn sthn opoia sygxwneyontai ola ta mhnymata ekpaideyshs apo opoion fakelo ki an fortw8hkan
		allTrainMessages.addAll(hamTrainMessages);
		allTrainMessages.addAll(spamTrainMessages);
		allTrainMessages.addAll(hamTrainMessages2);
		allTrainMessages.addAll(spamTrainMessages2);
		allTrainMessages.addAll(hamTrainMessages3);
		allTrainMessages.addAll(spamTrainMessages3);
		allTrainMessages.addAll(hamTrainMessages4);
		allTrainMessages.addAll(spamTrainMessages4);
		
		return allTrainMessages;		//epistrefei thn ArrayList me ola ta mhnymata ekpaideyshs
	}
		
	static ArrayList<Message> getTrainFolderMessages(String folderName, boolean spam)  throws IOException		
	{														//epistrefei ArrayList apo objects typou Message me ola ta mhnymata tou fakelou "folderName"
		ArrayList<Message> folderMessages = new ArrayList<Message>();
		File folder = new File(folderName);
		File[] listOfFiles = folder.listFiles();	//apo8hkeysh se array apo objects typou File twn onomatwn twn periexomenwn sto fakelo arxeiwn 
			
		for (File file : listOfFiles) 		//gia ka8e arxeio tou fakelou ginetai klhsh ths readTrainFile() gia na diabastei to periexomeno tou kai na epistrafei se morfh Message
		{
		    if (file.isFile()) 
		    {
        		Message tempMessage = readTrainFile(folderName+file.getName(), spam);
        		folderMessages.add(tempMessage);
		    }
		}					
		
		return folderMessages;
	}
	
	static Message readTrainFile(String fileName, boolean spam)  throws IOException
	{																//diabazei to periexomeno tou arxeiou "fileName" kai to epistrefei se morfh Message
		Message message= new Message(spam);			//dhmiourgw neo Message pou 8a parei to periexomeno tou arxeiou
		ArrayList<Word> messageContent= new ArrayList<Word>();		//ArrayList sthn opoia pros8etw ka8e leksh tou mhnymatos afou th diabasw kai thn epeksergastw
		File msgfile = new File(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(msgfile)));	//anoigma tou arxeiou gia anagnwsh
		
		String line = reader.readLine();	//diabasma grammhs kai apo8hkeysh se metablhth String
		
		while (line !=null)//diabazei grammh grammh, thn kanei split se lekseis kai elegxei ka8e leksh
		{	
			String splitLine[] = line.split(" ");
			
			for (String s: splitLine)	//elegxos ka8e 
			{
				
				Word word = null;
				
				if(dictionary.containsKey(s))		//an h leksh exei ksanasynanth8ei se mhnyma, anaktw to antistoixo Word apo to leksiko kai krataw mia anafora se ayto
				{
					word = (Word) dictionary.get(s);
				}									
				else 
				{				//an den thn exw ksanasynanthsei, dhmiourgw neo antikeimeno Word kai kanw kataxwrhsh sto leksiko
					word = new Word(s);
					dictionary.put(s,word);
				}
				messageContent.add(word);	//pros8etw th leksh sto periexomeno tou mhnymatos
				
				if(spam == true)		//ayksanw ton antistoixo metrhth ths lekshs,analoga me to 
				{
					word.increaseInSpam();
				}
				else
				{
					word.increaseInHam();
				}
				
			}
			
			line = reader.readLine();
		}
	
        reader.close();		//kleinw to arxeio
	    message.setContent(messageContent);		//apo8hkeyw sto message tis periexomenes lekseis tou
	    
	    if(spam==true)
	    	(Message.totalSpam)++;		//ayksanw to metrhth twn spam mhnymatwn ekpaideyshs an to mhnyma ayto einai spam
		
		return message;		//epistrofh tou mhnymatos ekpaideyshs
	}	
	
	static int[] predict() throws IOException
	{
		int[] results1,results2,results;
		
//		results1 = getTestFolderMessages("./enron2/ham/", false);
//		results2 = getTestFolderMessages("./enron2/spam/", true);	
//		results1 = getTestFolderMessages("./enron1/ham/", false);
//		results2 = getTestFolderMessages("./enron1/spam/", true);
		results1 = getTestFolderMessages("./enron5/ham/", false);		//klhsh ths getTestFolderMessages() gia ta mhnymata elegxou pou briskontai se dyo fakelous
		results2 = getTestFolderMessages("./enron5/spam/", true);
		
		results = new int[4];
		
		for(int i=0;i<4;i++)
			results[i] = results1[i] + results2[i];		//a8roizw ta TruePositives, FalsePositives, FalseNegatives, TrueNegatives twn mhnymatwn twn dyo fakelwn gia tous opoious egine h problepsh
		return results;
	}
	

	
	static int[] getTestFolderMessages(String folderName, boolean spam)  throws IOException
	{
		File folder = new File(folderName);
		File[] listOfFiles = folder.listFiles();		//apo8hkeysh se array apo objects typou File twn onomatwn twn periexomenwn sto fakelo arxeiwn 
		int[] results = new int[4];
		int TruePositives=0;		//Positive = Spam, Negative = Ham
		int TrueNegatives=0;
		int FalsePositives=0;
		int FalseNegatives=0;
		
		
		
		for (File file : listOfFiles) 	
		{
		    if (file.isFile()) 
		    {
		      
        		ArrayList<Word> tempMessage = readTestFile(folderName+file.getName());		//gia ka8e arxeio tou fakelou ginetai klhsh ths readTestFile() gia na diabastei to periexomeno tou kai na epistrafei se morfh ArrayList apo antikeimena Word
        		boolean predictionSpam = naiveBayesClassifier(tempMessage);					//klhsh tou algori8mou taksinomhshs Naive Bayes gia to periexomeno tou arxeiou pou diabasthke kai epistrofh problepshs gia to an einai spama h ham
        		//folderMessages.add(tempMessage);
        		predictionResults.write(file.getName()+" "+spam+" "+predictionSpam);	//eggrafh sto arxeio eksodou mias grammhs me to onoma tou arxeiou to an einai spam pragmatika kai thn problepsh tou algori8mou, xwrismena me kena
        		if (predictionSpam)				//elegxos problepshs kai pragmatikhs timhs kai aykshsh tou katallhlou metrhth
        		{
        			if (spam)
        				TruePositives++;
        			else
        				FalsePositives++;
        		}
        		else
        		{
        			if (spam)
        				FalseNegatives++;
        			else
        				TrueNegatives++;
        		}
        		
		    }
		    predictionResults.newLine();
		}	
		
		results[0] = TruePositives;
		
		results[1] = FalsePositives;
		
		results[2] = FalseNegatives;
		
		results[3] = TrueNegatives;
	
		
		return results;			//epistrefei to plh8os twn TruePositives, FalsePositives, FalseNegatives, TrueNegatives se array 4 akeraiwn
	}

	static ArrayList<Word> readTestFile(String fileName)  throws IOException		//diabazei to periexomeno tou arxeiou "fileName" kai to epistrefei se morfh ArrayList<Word>. Den epistrefei antikeimeno typou Message gia na mhn mplextoun ta mhnymata ekpaideyshs me ta mhnymata elegxou kai ephrreastoun oi metrhtes
	{
		ArrayList<Word> messageContent= new ArrayList<Word>();
		File msgfile = new File(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(msgfile)));
		String line = reader.readLine();
		
		while (line !=null)//diabazei grammh grammh, thn kanei split kai elegxei ka8e timh an einai egkyrh
		{	
			String splitLine[] = line.split(" ");
			
			for (String s: splitLine)
			{
				Word word = null;
				
				if(dictionary.containsKey(s))
				{
					word = (Word) dictionary.get(s);
				}
				else 
				{
					word = new Word(s);
					dictionary.put(s,word);
				}
				messageContent.add(word);
								
			}
			line = reader.readLine();
		}
	
        reader.close();
	   
		return messageContent;
	}	
	
	static boolean naiveBayesClassifier(ArrayList<Word> message) throws IOException		//algori8mos Naive Bayes gia thn problepsh ths kathgorias tou mhnymatos
	{
		double productProbSpam = 1.0d;		//arxikh timh 1 gia ta ginomena, epeidh to 1 einai to oudetero stoixeio tou pollaplasiasmou
		double productProbHam = 1.0d;
		
		for (int i = 0; i < message.size(); i++) 			//gia oles tis lekseis ths ArrayList
		{
			Word word;
			if(dictionary.containsKey(message.get(i).getWord()))		//elegxos an yparxei h leksh hdh sto leksiko
			{
				word = (Word) dictionary.get(message.get(i).getWord());
			}
			else 
			{
				word = new Word(message.get(i).getWord());
				dictionary.put(message.get(i).getWord(),word);
			}
			productProbSpam *= (double) word.getProbWord_Spam(); //ypologismos tou ginomenou Ð P(Xi=xi| C=1) gia ola ta i
			productProbHam *= (double) (1.0f - word.getProbWord_Spam());	//ypologismos tou ginomenou Ð P(Xi=xi| C=0) gia ola ta i
		}
		float pSpam = (float)Message.totalSpam/(float)Message.total;		//P(C=1)
		float pHam = (float)(Message.total - Message.totalSpam)/(float)Message.total;		//P(C=0)
		
		if((float)(pSpam * productProbSpam) > (float)(pHam * productProbHam)) //P(C=1|<X1, X2, X3, ..., Xn>) > P(C=0|<X1, X2, X3, ..., Xn>)
			return true;														//sygkrish twn pi8anothtwn, agnowntas tou paronomastes, ka8ws einai idioi (dialeksh 16 - diafaneia 6) 
		else 
			return false;
		
	}
}
