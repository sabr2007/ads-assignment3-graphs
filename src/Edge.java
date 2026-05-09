public class Edge{

    private Vertex source;
    private Vertex destination;

    public Edge(Vertex source, Vertex destination){
        this.destination = destination;
        this.source = source;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getDestination() {
        return destination;
    }

    @Override
    public String toString(){
        return source + " -> " + destination;
    }
}