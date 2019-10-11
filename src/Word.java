/*Toumanidou Andromachi 3040185 - Texnhth Nohmosynh - Ergasia 2 - Etos 2018-2019
 * Class gia dhmioyrgia antikeimenwn pou paristanoun mia leksh kai to plh8os twn emfanisewn ths se mhnymata ekpaideyshs
 */

public class Word 
{
	private String chars;	//to periexomeno ths lekshs
	private int inSpam;		//oi emfaniseis ths lekshs se spam mhnymata ekpaideyshs
	private int inHam;		//oi emfaniseis ths lekshs se ham mhnymata ekpaideyshs
	
	public Word(String word)//kataskeyasths me orisma mia leksh
	{
		this.chars = word;
		this.inSpam = 0;
		this.inHam = 0;
	}
	
	public String getWord()
	{
		return this.chars;
	}
	
	public int getInSpam()
	{
		return this.inSpam;
	}

	public int getInHam()
	{
		return this.inHam;
	}
	
	public void increaseInSpam()
	{
		this.inSpam++;
	}
	
	public void increaseInHam()
	{
		this.inHam++;
	}
	
	public float getProbWord_Spam() 	//P(Xi=xi|C=1) me ektimhtria Laplace (dialeksh 16-diafaneia 7)
	{									//H pi8anothta h leksh na periexetai sto mhnyma, dedomenou oti to mhnyma einai spam
		return ((float)(this.inSpam + 1.0f) / (float)(Message.totalSpam) + 2.0f); //plh8os emfanisewn ths lekshs se mhnymata spam(syn 1) dia to plh8os olwn to mhnymatwn ekpaideyshs spam(syn 2, epeidh h metablhth C exei 2 dynates times)
	}
	
	public float getProbWord_Ham()		//P(Xi=xi|C=0) me ektimhtria Laplace (dialeksh 16-diafaneia 6)
	{									//H pi8anothta h leksh na periexetai sto mhnyma, dedomenou oti to mhnyma einai ham
		float Ham=(float) (this.inHam + 1.0f);
		float tot = (float)(Message.total - Message.totalSpam + 2.0f);
		return (float) Ham/(float)tot;//plh8os emfanisewn ths lekshs se mhnymata ham(syn 1) dia to plh8os olwn to mhnymatwn ekpaideyshs ham(syn 2, epeidh h metablhth C exei 2 dynates times)
	}	

}
