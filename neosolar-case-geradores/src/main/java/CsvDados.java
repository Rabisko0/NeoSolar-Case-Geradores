import com.opencsv.bean.CsvBindByName;

public class CsvDados {

	@CsvBindByName(column = "Id do Gerador")
    private int idGerador;

    @CsvBindByName(column = "Potencia do Gerador")
    private int potenciaTotal;

    @CsvBindByName(column = "Id do Produto")
    private int idProduto;

    @CsvBindByName(column = "Nome do Produto")
    private String nomeProduto;

    @CsvBindByName(column = "Quantidade")
    private int quantidade;

	public CsvDados(int id, int potenciaTotal, int id2, String produto, int numComponente) {
		this.setIdGerador(id);
		this.setPotenciaTotal(potenciaTotal);
		this.setIdProduto(id2);
		this.setNomeProduto(produto);
		this.setQuantidade(numComponente);
	}

	public int getIdGerador() {
		return idGerador;
	}

	public void setIdGerador(int idGerador) {
		this.idGerador = idGerador;
	}

	public int getPotenciaTotal() {
		return potenciaTotal;
	}

	public void setPotenciaTotal(int potenciaTotal) {
		this.potenciaTotal = potenciaTotal;
	}

	public int getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(int idProduto) {
		this.idProduto = idProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
    
    
}
