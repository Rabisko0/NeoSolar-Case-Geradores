import java.util.List;

class Gerador {
    private int id;
    private List<Painel> paineis;
    private Inversor inversor;
    private Controlador controlador;
    private int potenciaTotal;

    public Gerador(int id, List<Painel> paineis, Inversor inversor, Controlador controlador) {
        this.id = id;
        this.paineis = paineis;
        this.inversor = inversor;
        this.controlador = controlador;
        this.potenciaTotal = calcularPotenciaTotal();
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Painel> getPaineis() {
		return paineis;
	}

	public void setPaineis(List<Painel> paineis) {
		this.paineis = paineis;
	}

	public Inversor getInversor() {
		return inversor;
	}

	public void setInversor(Inversor inversor) {
		this.inversor = inversor;
	}

	public Controlador getControlador() {
		return controlador;
	}

	public void setControlador(Controlador controlador) {
		this.controlador = controlador;
	}

	public int getPotenciaTotal() {
		return potenciaTotal;
	}

	public void setPotenciaTotal(int potenciaTotal) {
		this.potenciaTotal = potenciaTotal;
	}

	private int calcularPotenciaTotal() {
        int potenciaPaineis = paineis.stream().mapToInt(Painel::getPotenciaEmW).sum();
        return Math.min(potenciaPaineis, Math.min(inversor.getPotenciaEmW(), controlador.getPotenciaEmW()));
    }
    
    
    @Override
    public String toString() {
        return "Gerador{" +
                "Id=" + id +
                ", Paineis Solares=" + paineis +
                ", Inversor=" + inversor +
                ", Controlador De Carga=" + controlador +
                ", Potencia Total=" + potenciaTotal +
                '}';
    }
}