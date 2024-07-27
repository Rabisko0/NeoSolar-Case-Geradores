
public class Painel extends Produto {

	public boolean compararPotencia(Painel a) {
		
		if (this.getPotenciaEmW() == a.getPotenciaEmW() && this.getProduto() == a.getProduto()){
			return true;
		} else 
			return false;
	}
	
}
