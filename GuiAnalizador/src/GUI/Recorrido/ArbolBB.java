package GUI.Recorrido;

/**
 * Created by guill on 25/04/2017.
 **/
public class ArbolBB<T> {
    private NodoABB<T> Root;
    public T Dr;

    ArbolBB() {
        Root = null;
        Dr = null;
    }

    NodoABB<T> DameRaiz() {
        return Root;
    }

    public boolean Inserta(T Dato) {
        return Inserta(Root, Dato);
    }

    private boolean Inserta(NodoABB<T> Raiz, T Dato) {
        if (Root == null) {
            System.out.println("Se inserto raiz");
            Root = new NodoABB<>(Dato);
            return true;
        }
        if (Dato.toString().compareTo(Raiz.Info.toString()) < 0) {
            if (Raiz.DameSubIzq() != null) {
                return Inserta(Raiz.DameSubIzq(), Dato);
            } else {
                System.out.println("Se inserto en el nodo izquerdo de la raiz: " + Raiz.getInfo());
                NodoABB<T> Nuevo = new NodoABB<>(Dato);
                Raiz.setSubIzq(Nuevo);
                return true;
            }
        } else {
            if (Raiz.DameSubDer() != null) {
                return Inserta(Raiz.DameSubDer(), Dato);
            } else {
                System.out.println("Se inserto en el hijo derecho de la raiz: " + Raiz.getInfo());
                NodoABB<T> Nuevo = new NodoABB<>(Dato);
                Raiz.setSubDer(Nuevo);
                return true;
            }

        }
    }

    void inorder(NodoABB<T> Raiz) {
        if (Raiz == null)
            return;
        inorder(Raiz.DameSubIzq());
        System.out.print(Raiz.Info + " ");
        inorder(Raiz.DameSubDer());
    }

    void preorder(NodoABB<T> Raiz) {
        if (Raiz == null)
            return;
        System.out.print(Raiz.Info + " ");
        preorder(Raiz.DameSubIzq());
        preorder(Raiz.DameSubDer());
    }

    void posorder(NodoABB<T> Raiz) {
        if (Raiz == null)
            return;
        posorder(Raiz.DameSubIzq());
        posorder(Raiz.DameSubDer());
        System.out.print(Raiz.Info + " ");
    }

    boolean Busca(NodoABB<T> Raiz, T Dato) {
        if (Raiz == null) {
            Dr = null;
            return false;
        }
        if (Dato.toString().compareTo(Raiz.Info.toString()) == 0) {
            Dr = Raiz.Info;
            return true;
        }
        if (Dato.toString().compareTo(Raiz.Info.toString()) > 0) {
            return Busca(Raiz.DameSubDer(), Dato);

        }
        return Busca(Raiz.DameSubIzq(), Dato);
    }

    boolean buscar(T Dato) {
        NodoABB<T> n = Root;
        while (n != null) {
            if (Dato.toString().compareTo(n.Info.toString()) == 0)
                return true;
            if (Dato.toString().compareTo(n.Info.toString()) > 0)
                n = n.DameSubDer();
            else
                n = n.DameSubIzq();
        }

        return false;
    }

    int cuenta(NodoABB<T> Raiz) {
        if (Raiz == null) {
            return 0;
        }
        return cuenta(Raiz.DameSubIzq()) + 1 + cuenta(Raiz.DameSubDer());

    }

    int Altura(NodoABB<T> Raiz, int Nivel) {
        if (Raiz == null) {
            return 0;
        }
        int NSIzq = Altura(Raiz.DameSubIzq(), Nivel + 1);
        if (Nivel > NSIzq) {
            NSIzq = Nivel;
        }
        int NSDer = Altura(Raiz.DameSubDer(), Nivel + 1);
        if (Nivel > NSDer) {
            return NSIzq;
        }
        return NSDer;
    }

    public boolean Retira(NodoABB<T> R, T Infor) {//captura un nodo Raiz y la informacion a buscar
        boolean b = true;//variable bandera
        NodoABB<T> Ant = null;//nodo para señalizar el anterior al encontrado
        String Llave = Infor.toString();//convierte el valor infor a una variable tipo string
        while (R != null) {//mientras la raiz sea distinto a nulo
            if (Llave.compareTo(R.Info.toString()) < 0)//compara si el valor de la llave es menor al de la raiz
            {
                Ant = R; //al nodo anterior le asigna el valor de la raiz
                R = R.DameSubIzq();//la raiz avanza uno en el subarbol izquierdo
                b = false;//la bandera continua falso
            } else if (Llave.compareTo(R.Info.toString()) > 0)//compara si el valor de la llave es mayor a el valor de la raiz
            {
                Ant = R;//el nodo anterior se le asigna el valor de la raiz
                R = R.DameSubDer();//la raiz continua uno en el subarbol derecho
                b = true;//la bandera se hace verdadero
            } else
                break; //si no entra ningun if rompe el ciclo
        }
        if (R == null)//si la raiz es nulo retorna falso
            return false;
        if (R.DameSubIzq() != null && R.DameSubDer() != null) {//si los dos subarboles son distintos a nulos entran en el if
            //
            NodoABB<T> Temp = R.DameSubDer(); //declara un nodo temporal para asignar el subarbol derecho de la raiz
            NodoABB<T> Aux = R;//asigna la raiz a un valor auxiliar
            boolean RamaIzq = false; //La rama izquierda sigue en falso hasta que entre en el while
            while (Temp.DameSubIzq() != null)//mientras la rama de la izquiereda de el nodo temporal sea distinto a nulo
            {
                Aux = Temp;//EL auxiliar se vuelve el temporal
                Temp = Temp.DameSubIzq();//Temporal avanza uno por el subarbol izquierdo
                RamaIzq = true;//la variable rama izquierda se hace true
            }
            //
            Dr = R.Info;//El valro de la raiz actual se le asigna al dato a retirar
            R.Info = Temp.Info;//al valor de la raiz se le asigna el valor temporal actual
            //
            if (RamaIzq)//si la variable ramaizq es verdadera entra en el if
            {
                if (Temp.DameSubIzq() == null)//si temporal ya no tiene valores en su subarbol izquierdo
                    Aux.setSubIzq(Temp.DameSubDer());//Hace que el nodo subizquierdo del auxiliar apunte al nodo subderecho del temporal
                else
                    Aux.setSubIzq(Temp.DameSubIzq());//de lo contrario hace que el nodo subizquierdo del auxiliar apunte al nodo  subderecho del temporal
            } else //si ramaizq continua falso hace que el nodo subderecho del auxiliar apunte al nodo subderecho del temporal
            {
                Aux.setSubDer(Temp.DameSubDer());
            }
            return true;//retorna verdadero
        } else //si R no es nulo y r subizq y subder no son distintos a null entra en este else
        {
            if (R == Root)//si R es igual a la raiz
            {
                Dr = R.Info;//el dato a retirar se convierte en R
                if (R.DameSubIzq() == null)//si la R no tiene subarbol izquierdo
                    Root = R.DameSubDer(); //El valor de la raiz se convierte en el subderecho de R
                else
                    Root = R.DameSubIzq();//de lo contrario en el subizquierdo
            } else //Si R no es igual a la raiz
            {
                Dr = R.Info;//el dato a reitrar se convierte en R
                if (R.DameSubIzq() == null) //Si R no tiene subarbol izquierdo
                    if (b) //si la bandera es igual a true
                    {
                        assert Ant != null;
                        Ant.setSubDer(R.DameSubDer());//El subderecho del Anterior apunta al subderecho de R
                    }
                    else
                        Ant.setSubIzq(R.DameSubDer()); //De lo contrario el subiquierdo del anterior apunta al subderecho de R
                else //Si R tiene subarbol izquierdo
                    if (b) //y si la bandera es igual a true
                    {
                        assert Ant != null;
                        Ant.setSubDer(R.DameSubIzq());//El subderecho del anterior apunta al subizquierdo de R
                    }
                    else
                        Ant.setSubIzq(R.DameSubIzq());//De lo contrario el subizquierdo del anterior apunta al subizquierdo de R
            }
        }
        return true;//retorna falso
    }//fin del metodo


}