import java.io.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Scanner;

public class UndirectedWeightedGraph{
    //Graph Data Structure as Adjacency List .
    class Graph{
        private int num_nodes;
        private int num_edges;
        private final HashMap<String,Vertex> AdjList;
        Graph(int num_nodes){
            this.num_nodes=num_nodes;
            this.AdjList=new HashMap<String,Vertex>(num_nodes);
        }
        Graph(){
            this.AdjList=new HashMap<String,Vertex>();
        }
        void setNum_edges(int e){
            this.num_edges=e;
        }
        void addEdge(Vertex v, Vertex u, int w){
            Edge e=new Edge (v,u,w);
            if(!AdjList.get(v.data).isAdjacent(u, w)){
                AdjList.get(v.data).Neighbours.add(e);
            }
            Edge e_dash=new Edge (u,v,w);
            if(!AdjList.get(u.data).isAdjacent(v, w)){
                AdjList.get(u.data).Neighbours.add(e_dash);
            }
        }
    }
    class Vertex implements Comparable<Vertex>{
        private String data;
        private Integer sum_weights;
        private Integer DFS_no=0;
        private ArrayList<Edge> Neighbours;
        Vertex(String data){
            this.data=data;
            Neighbours=new ArrayList<Edge>();
        }
        @Override
        public int compareTo(UndirectedWeightedGraph.Vertex o) {
            if(this.sum_weights.compareTo(o.sum_weights)!=0){
                return this.sum_weights.compareTo(o.sum_weights);
            }
            else{
                return this.data.compareTo(o.data);
            }
        }
        String getData(){
            return this.data;
        }
        boolean isAdjacent(Vertex u, int w){
            Edge e=new Edge(this, u, w);
            return this.Neighbours.contains(e);
        }
        void setCo_occurence(){
            int sum=0;
            for(int i=0;i<this.Neighbours.size();i++){
                sum+=this.Neighbours.get(i).w;
            }
            this.sum_weights=sum;
            return;
        }
    }
    class Edge{
        private Vertex v;
        private Vertex u;
        private int w;
        Edge(Vertex v, Vertex u, int weight){
            this.v=v;
            this.u=u;
            this.w=weight;
        }
    }

    //Helper Functions
    private Graph csv2graph(String nodes_path, String edges_path){
        String line_n="";
        String line_e="";
        int i=0,j=0;
        File node_file=new File(nodes_path);
        File edge_file=new File(edges_path);
        ArrayList<String> node_names=new ArrayList<String>();
        try{
            BufferedReader br1= new BufferedReader(new FileReader(node_file));
            while((line_n=br1.readLine())!=null){
                if(i!=0){
                    String[] a=line_n.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                    if(a[1].charAt(0)=='"' && a[1].charAt(a[1].length()-1)=='"'){
                        String n_final="";
                        for (int idx=1;idx<a[1].length()-1;idx++){
                            n_final+=a[1].charAt(idx);
                        }
                        node_names.add(n_final);
                    }
                    else{node_names.add(a[1]);}
                }
                i++;
            } 
        }
        catch(FileNotFoundException e){e.printStackTrace();}
        catch(IOException e){e.printStackTrace();}
        Graph G=new Graph(node_names.size());
        for(int k=0;k<node_names.size();k++){
            G.AdjList.put(node_names.get(k), new Vertex(node_names.get(k)));
        }
        try{
            BufferedReader br2= new BufferedReader(new FileReader(edge_file));
            while((line_e=br2.readLine())!=null){
                if(j!=0){
                    String[] a=line_e.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                    Vertex source,dest;
                    
                    //Source Node
                    if(a[0].charAt(0)=='"' && a[0].charAt(a[0].length()-1)=='"'){
                        String src_final="";
                        for (int idx=1;idx<a[0].length()-1;idx++){
                            src_final+=a[0].charAt(idx);
                        }
                        source= G.AdjList.get(src_final);
                    }
                    else{source= G.AdjList.get(a[0]);}
                    
                    //Target Node
                    if(a[1].charAt(0)=='"' && a[1].charAt(a[1].length()-1)=='"'){
                        String d_final="";
                        for (int idx1=1;idx1<a[1].length()-1;idx1++){
                            d_final+=a[1].charAt(idx1);
                        }
                        dest= G.AdjList.get(d_final);
                    }
                    else{dest= G.AdjList.get(a[1]);}
                    
                    // Weight of the Edge.
                    int weight=Integer.parseInt(a[2]);
                    G.addEdge(source, dest, weight);
                }
                j++;
            }
        }
        catch(FileNotFoundException e){e.printStackTrace();}
        catch(IOException e){e.printStackTrace();}
        G.setNum_edges(j-1);
        ArrayList<Vertex> arr =new ArrayList<Vertex>(G.AdjList.values());
        for (int p=0;p<arr.size();p++){
            arr.get(p).setCo_occurence();
        }
        return G;
    }
    private static void DFS(Vertex v,int comp_no, ArrayList<Vertex> store){
        v.DFS_no=comp_no;
        store.add(v);
        for (int i=0;i<v.Neighbours.size();i++){
            Edge edge=v.Neighbours.get(i);
            if (edge.u.DFS_no==0){
                DFS(edge.u, comp_no,store);
            }
        }
    }
 

    //Assignment 4 functions
    public static void average(Graph G){
        float v=G.num_nodes;
        float e=G.num_edges;
        float ans=2*e/v;
	if(v==0){System.out.printf("%.2f", v);return;}
        System.out.printf("%.2f", ans);
        System.out.println();
    }
    public static void rank(Graph G){
        ArrayList<Vertex> a=new ArrayList<Vertex>(G.AdjList.values());
        MergeSort.Sort(a, 0, a.size() -1,1);
        for(int q=0;q<a.size();q++){
            if(q!=a.size()-1){System.out.print(a.get(q).data+",");}
            else{System.out.print(a.get(q).data);}
        }
        System.out.println();
    }

    public static void independent_storylines_dfs(Graph G){
        ArrayList<Vertex> arr=new ArrayList<Vertex>(G.AdjList.values());
        HashMap<Integer,ArrayList<Vertex>> HashStore=new HashMap<Integer,ArrayList<Vertex>>();
        int i=0;
        int comp_no=1;
        while(i<arr.size()){
            Vertex v=arr.get(i);
            if(HashStore.get(comp_no)==null){
                HashStore.put(comp_no, new ArrayList<Vertex>());
            }
            ArrayList<Vertex> store=HashStore.get(comp_no);
            if( arr.get(i).DFS_no==0){
                DFS(v, comp_no, store);
                comp_no++;
            }
            i++;
        }
        // System.out.println(comp_no-1);
        ArrayList<ArrayList<Vertex>> Connected_Comps = new ArrayList<ArrayList<Vertex>>(HashStore.values());
        // ArrayList<ArrayList<Vertex>> SortedNodeVectors = new ArrayList<ArrayList<Vertex>>(Connected_Comps.size());
        for (int idx=0;idx<Connected_Comps.size();idx++){
            ArrayList<Vertex> ith_comp=Connected_Comps.get(idx);
            MergeSort.Sort(ith_comp, 0, ith_comp.size()-1, 2);
            Connected_Comps.set(idx, ith_comp);
        }
        MergeSort.SortLists(Connected_Comps, 0, Connected_Comps.size()-1);
        for (ArrayList<Vertex> comp:Connected_Comps){
            for(int p=0;p<comp.size();p++){
                if(p!=comp.size()-1){System.out.print(comp.get(p).data+",");}
                else{System.out.println(comp.get(p).data);}
            }
        }
    }

    //Merge Sort Class 
    static class MergeSort{

        //Merge Sort of Vertices in Decreasing Order.
        public static void Merge(ArrayList<Vertex> arr, int l, int m, int r, int type){
            int n1 = m-l+1;
            int n2 = r-m;
            if(type!=3){
                ArrayList<Vertex> Left = new ArrayList<>(n1);
                ArrayList<Vertex> Right = new ArrayList<>(n2);
                for (int i=0;i<n1;i++){
                    Left.add(i, arr.get(l+i));
                }
                for (int j=0;j<n2;j++){
                    Right.add(j, arr.get(m+j+1));
                }    
                int a=0,b=0;
                int k=l;
                //Type 1: Vertex Sorting (consisting of sum of weights and string data)
                if(type==1){
                    while (a<n1 && b<n2) {
                        if (Left.get(a).compareTo(Right.get(b)) <= 0)  {
                            arr.set(k, Right.get(b));
                            b++;
                        } else {
                            arr.set(k, Left.get(a));
                            a++;
                        }
                        k++;
                    }
                }
                //Type 2: String data sorting for every vertices.
                if(type==2){
                    while (a<n1 && b<n2) {
                        if (Left.get(a).data.compareTo(Right.get(b).data) <= 0) {
                            arr.set(k, Right.get(b));
                            b++;
                        } else {
                            arr.set(k, Left.get(a));
                            a++;
                        }
                        k++;
                    }
                }
                
                while(a<n1){
                    arr.set(k, Left.get(a));
                    a++;
                    k++;
                }
                while(b<n2){
                    arr.set(k, Right.get(b));
                    b++;
                    k++;
                }
            }

        }
        public static void MergeLists(ArrayList<ArrayList<Vertex>> arr,int l,int m, int r){
            int n1 = m-l+1;
            int n2 = r-m;
            ArrayList<ArrayList<Vertex>> Left = new ArrayList<ArrayList<Vertex>>(n1);
            ArrayList<ArrayList<Vertex>> Right = new ArrayList<ArrayList<Vertex>>(n2);
            for (int i=0;i<n1;i++){
                Left.add(i, arr.get(l+i));
            }
            for (int j=0;j<n2;j++){
                Right.add(j, arr.get(m+j+1));
            }
            int a=0,b=0;
            int k=l;
            
            while (a<n1 && b<n2) {
                if (compareLists(Left.get(a),Right.get(b)) <= 0)  {
                    arr.set(k, Right.get(b));
                    b++;
                } else {
                    arr.set(k, Left.get(a));
                    a++;
                }
                k++;
            }
            while(a<n1){
                arr.set(k, Left.get(a));
                a++;
                k++;
            }
            while(b<n2){
                arr.set(k, Right.get(b));
                b++;
                k++;
            }
        }
        public static void SortLists(ArrayList<ArrayList<Vertex>> arr,int l, int r){
            if(l<r){
                int m=(l+r)/2;
                SortLists(arr,l,m);
                SortLists(arr,m+1,r);
                MergeLists(arr,l,m,r);
            }
            return;
        }
        public static void Sort(ArrayList<Vertex> arr, int l, int r, int type){
            if(l<r){
                int m=(l+r)/2;
                Sort(arr,l,m,type);
                Sort(arr,m+1,r,type);
                Merge(arr,l,m,r,type);
            }
            return;
        }
        public static int compareLists(ArrayList<Vertex> V1,ArrayList<Vertex> V2){
            if(V1.size()>V2.size()){
                return 1;
            }
            else if(V1.size()<V2.size()){
                return  -1;
            }
            else{return V1.get(0).data.compareTo(V2.get(0).data);}
        }

    }

     



    public static void main(String[] args) {
        String nodes_path=args[0];
        String edges_path=args[1];
        UndirectedWeightedGraph A4=new UndirectedWeightedGraph();
        UndirectedWeightedGraph.Graph G= A4.csv2graph(nodes_path, edges_path);

        String func=args[2];
        switch (func) {
            case "average":
                UndirectedWeightedGraph.average(G);
                break;
            case "rank":
                UndirectedWeightedGraph.rank(G);
                break;
            case "independent_storylines_dfs":
                UndirectedWeightedGraph.independent_storylines_dfs(G);
                break;
            default:
                break;
        }
        

        // UndirectedWeightedGraph test=new UndirectedWeightedGraph();
        // String nodes_path="nodes.csv";
        // String edges_path="edges.csv";
        // UndirectedWeightedGraph.Graph G1=test.csv2graph(nodes_path, edges_path);
        // UndirectedWeightedGraph.average(G1);
        // ArrayList<Vertex> arr =new ArrayList<Vertex>(G1.AdjList.values());
        // for (int i=0;i<arr.size();i++){
        //     System.out.println("("+arr.get(i).data+","+arr.get(i).sum_weights+")");
        // }
        // UndirectedWeightedGraph.rank(G1);
        
        // Vertex v1= test.new Vertex("b");
        // v1.sum_weights=6;
        // Vertex v2= test.new Vertex("a"); 
        // v2.sum_weights=2;
        // Vertex v3= test.new Vertex("m");
        // v3.sum_weights=3;
        // Vertex v4= test.new Vertex("q");
        // v4.sum_weights=7;
        // Vertex v5= test.new Vertex("i");
        // v5.sum_weights=9;
        // Vertex v6= test.new Vertex("d");
        // v6.sum_weights=6;
        // Vertex[] a1=new Vertex[6];
        // a1[0]=(v1);
        // a1[1]=(v2);
        // a1[2]=(v3);
        // a1[3]=(v4);
        // a1[4]=(v5);
        // a1[5]=(v6);
        // UndirectedWeightedGraph.MergeSort.Sort(a1, 0, 5,1);
        // for(int j=0;j<a1.length;j++){
        //     System.out.print(a1[j].data);
        // }
    }
}