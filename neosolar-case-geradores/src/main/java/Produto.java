import com.google.gson.annotations.SerializedName;

public abstract class Produto {

	private String Categoria;
    private int Id;
    @SerializedName("Potencia em W")
    private int PotenciaEmW;
    private String Produto;

    // Getters e Setters
    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPotenciaEmW() {
        return PotenciaEmW;
    }

    public void setPotenciaEmW(int potenciaEmW) {
        PotenciaEmW = potenciaEmW;
    }

    public String getProduto() {
        return Produto;
    }

    public void setProduto(String produto) {
        Produto = produto;
    }

    @Override
    public String toString() {
        return "{" +
                "Categoria='" + Categoria + '\'' +
                ", Id=" + Id +
                ", PotenciaEmW=" + PotenciaEmW +
                ", Produto='" + Produto + '\'' +
                '}';
    }
}