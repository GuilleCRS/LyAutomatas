package GUI.Recorrido;
/**
 * Created by guill on 25/04/2017.
 */
public class NodoABB<T> {
    private NodoABB <T>   SubIzq;
    public T Info;
    private NodoABB<T>   SubDer;

    public T getInfo() {
        return Info;
    }

    public void setInfo(T info) {
        Info = info;
    }

    public NodoABB(T d)
    {  Info=d;
        SubIzq=null; SubDer=null;
    }
    public NodoABB<T> DameSubIzq(){
        return SubIzq;
    }
    public NodoABB<T> DameSubDer(){
        return SubDer;
    }
    public void setSubIzq(NodoABB<T> Ap){
        SubIzq=Ap;
    }
    public void setSubDer(NodoABB<T> Ap){
        SubDer=Ap;
    }

}