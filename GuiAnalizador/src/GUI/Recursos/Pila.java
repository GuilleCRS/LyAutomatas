package GUI.Recursos;

public class Pila<T> //Crea la pila de tipo T
{
	private int Max;
	private T [] P; //Crea un arreglo de tipo T
	private int Tope; 
	public T Dr;        // Apuntador al dato retirado de la pila
	
	
	public Pila(){       //Constructor del valor de la pila
		this(10);		 //Crea la pila con 10 valores
	}
	
	public Pila(int NE){              //Inicializa la pila ,Recibe el valor de 10
		P=(T[]) new Object[NE];       //Inicializa el arreglo en 10
		Tope=-1;                      //Pone el tope en -1
		Max=NE;                       //Y el maximo en 10
	}
	public boolean Inserta(T Dato){       //Metodo para agregar un dato a la pila
		if(Llena())                   //Verifica si esta llena 
			return false;         //No se agrego el dato regresa falso
		Tope++;                       //Si no esta llena aumenta el tope en 1
		P[Tope]=Dato;                 //Inserta el dato en el tope de la pila
		return true;                  //Se agrego el dato y regresa el verdadero
	}
	public boolean Retira(){              //Metodo para retirar
		if(Vacia())                   //Verifica si la pila esta vacia
	       return false;       //La pila al estar vacia regresa el falso ya que no se puede retirar nada
		Dr=P[Tope];                   //El dato retirado se inicializa como el tope de la pila
		P[Tope]=null;                 //Se elimina el dato dejando el valor en null
		Tope--;                       //Se le resta uno al tope de la pila
		return true;                  //Regresa verdadero al haberse retirado el valor
	}
	public boolean Llena(){           //Metodo para verificar si la pila esta llena
		return Tope==Max-1;	      //Si esta llena compara el valor del top con el maximo, menos uno
	}
	public boolean Vacia(){          //Metodo para verificar si la pila esta vacia
		return Tope==-1;             //Si esta vacia regresa el valor del tope original
	}
	public T MostrarT()
	{

		if (Vacia())
		{
			return null;
		}else {
			return P[Tope];

		}
		
	}
    public T RecorrerE(int i){
		return P[i];
	}
}
