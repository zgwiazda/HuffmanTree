import java.io.File;
import java.util.Scanner;

public class ASD4 {
    public static void main(String[] args) {

       //polski alfabet ma 36 liter stąd tylko na 36 jest gotowy kod
        KolejkaPriorytetowa kolejkaPriorytetowa = new KolejkaPriorytetowa(36);
      String home = args[0];

        Scanner scanner= null;
        HuffmanTree huffmanTree = new HuffmanTree();
        int licz=0;
        try {
            scanner = new Scanner(new File(home));

            String znak="";
            int liczba= 0;


            while((scanner.hasNextLine())){
                String line = scanner.nextLine();

                Scanner lineScanner = new Scanner(line);
                if(lineScanner.hasNext()){
                    znak = String.valueOf(lineScanner.next().charAt(0));

                }
                if(lineScanner.hasNext()){
                    lineScanner.skip(" ");
                    liczba = Integer.parseInt(lineScanner.nextLine());

                }
                Node node = new Node(null, null, liczba, znak);
                kolejkaPriorytetowa.wstaw(node);
                licz++;


            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        kolejkaPriorytetowa.kopcuj();
//dla jednego przypadku gdy jest tylko jedna litera- mozna ją zakodować de dacto dowolnie jako 0 lub 1, wiec kodujemy jako zero
        if(licz == 1){

            System.out.println(kolejkaPriorytetowa.wyjmijPierwszy().znak+" 0");
        }

        for(int i=licz-1;i>0;i--){
            Node lewy = kolejkaPriorytetowa.wyjmijPierwszy();
            Node prawy = kolejkaPriorytetowa.wyjmijPierwszy();
            Node nowy = new Node(lewy,prawy,lewy.wartosc+prawy.wartosc,lewy.znak+prawy.znak);
            kolejkaPriorytetowa.wstaw(nowy);
            kolejkaPriorytetowa.kopcuj();
            if(i==1){
                huffmanTree.root=nowy;
            }
        }

        huffmanTree.traverseTree(huffmanTree.root,"");



    }

    public static class KolejkaPriorytetowa{
        private Node[] Kopiec;
        private int rozmiar;
        private int maxRozmiar;

        private static final int front = 1;

        public KolejkaPriorytetowa(int maxRozmiar){
           this.maxRozmiar = maxRozmiar;
           this.rozmiar = 0;
           Kopiec = new Node[this.maxRozmiar +1];
           Kopiec[0] = new Node(null,null,-1,"X");

        }
        private int rodzic (int index){
            return index / 2;
        }

        private int leweDziecko(int index){
            return (index * 2);
        }
        private int praweDziecko(int index){
            return (index * 2)+1;
        }
        private boolean czyLisc(int index){
            if(index>= (rozmiar/2) && index <= rozmiar){
                return true;
            }
            else
                return false;
        }
        private void zamien(int pierszyIndex, int drugiIndex){
            Node tmp;
            tmp= Kopiec[pierszyIndex];
            Kopiec[pierszyIndex]=Kopiec[drugiIndex];
            Kopiec[drugiIndex] = tmp;
        }


     private void sortujKopiec(int index) {
         int lewe = leweDziecko(index);
         int prawe = praweDziecko(index);
         int najmniejszy = index;

         if (lewe <= rozmiar && Kopiec[lewe].wartosc < Kopiec[najmniejszy].wartosc) {
             najmniejszy = lewe;
         }

         if (prawe <= rozmiar && Kopiec[prawe].wartosc < Kopiec[najmniejszy].wartosc) {
             najmniejszy = prawe;
         }

         if (najmniejszy != index) {
             zamien(index, najmniejszy);
             sortujKopiec(najmniejszy);
         }
     }



        public void wstaw(Node node){
            Kopiec[++rozmiar] = node;
            int aktualny = rozmiar;
            while(Kopiec[aktualny].wartosc < Kopiec[rodzic(aktualny)].wartosc){
                zamien(aktualny,rodzic(aktualny));
                aktualny = rodzic(aktualny);
            }
            if(Kopiec[aktualny].wartosc == Kopiec[rodzic(aktualny)].wartosc){
                int porownaj = Kopiec[aktualny].znak.compareTo(Kopiec[rodzic(aktualny)].znak);
                if(porownaj<0){
                    zamien(aktualny,rodzic(aktualny));
                    aktualny = rodzic(aktualny);
                }
            }
        }

        public void kopcuj(){
            for(int i=(rozmiar/2);i>=1;i--){
                sortujKopiec(i);
            }
        }

        public Node wyjmijPierwszy(){
            Node wyjmij = Kopiec[front];
            Kopiec[front] = Kopiec[rozmiar--];
            sortujKopiec(front);
            return wyjmij;
        }

    /*    public void print(){
            for(int i = 1; i <= rozmiar / 2; i++){
                System.out.print(" RODZIC: " + Kopiec[i].znak + "-" + Kopiec[i].wartosc);

                if (Kopiec[2 * i] != null) {
                    System.out.print(", LEWE DZIECKO: " + Kopiec[2 * i].znak + "-" + Kopiec[2 * i].wartosc);
                }

                if (Kopiec[2 * i + 1] != null) {
                    System.out.print(", PRAWE DZIECKO: " + Kopiec[2 * i + 1].znak + "-" + Kopiec[2 * i + 1].wartosc);
                }

                System.out.println();
            }
        }*/

    }
   public static class Node{
       Node lewy;
        Node prawy;
        int wartosc;

        String znak;

        Node(Node lewy, Node prawy, int wartosc, String znak){
            this.lewy=lewy;
            this.prawy=prawy;
            this.wartosc=wartosc;
            this.znak = znak;
        }
    }
    public static class HuffmanTree{

        public Node root;
        public void SetRoot(Node root){
            this.root=root;
        }
        public  void traverseTree(Node node, String kod) {
            if (node != null) {
                traverseTree(node.lewy, kod + "0");  // Dodaj "0" dla przechodzenia w lewo
                traverseTree(node.prawy, kod + "1");  // Dodaj "1" dla przechodzenia w prawo

                if (node.lewy == null && node.prawy == null) {
                    System.out.println(node.znak +" "+ kod);

                }
            }
        }

    }
}