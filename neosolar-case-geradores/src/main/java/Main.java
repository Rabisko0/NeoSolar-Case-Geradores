import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;


public class Main {

	public static void main(String[] args) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		// TODO Auto-generated method stub
		
		getEstoque();

	}
	
	
	
	
	/**
	 * Método para obter e processar informações de estoque de componentes de energia solar a partir de uma URL que retorna um JSON.
	 * Os componentes processados incluem painéis solares, controladores de carga e inversores.
	 * Após processar os dados, também são criados geradores de energia solar com base nos componentes disponíveis.
	 * As informações são exibidas no console.
	 * 
	 * Requisitos:
	 * - A URL fornecida deve retornar um JSON contendo informações categorizadas de componentes.
	 * - É esperado que o JSON contenha informações sobre Painel Solar, Controlador de Carga e Inversor.
	 * 
	 * Funcionamento:
	 * - A função utiliza Gson para desserializar o JSON recebido em objetos Java correspondentes (Painel, Controlador, Inversor).
	 * - Os objetos são armazenados em listas separadas conforme sua categoria.
	 * - Em seguida, são exibidos no console os componentes agrupados por categoria.
	 * - Utilizando um método auxiliar criarGeradores(), são criados geradores de energia solar combinando painéis solares,
	 *   controladores de carga e inversores de acordo com as regras específicas de compatibilidade de potência.
	 * - Os geradores criados também são exibidos no console.
	 * 
	 * Observações:
	 * - Caso ocorra algum problema ao acessar a URL ou ao processar o JSON, uma exceção é tratada e seu stack trace é impresso.
	 * - Categorias desconhecidas encontradas no JSON são informadas no console.
	 * 
	 * Exemplo de Uso:
	 * - getEstoque();
	 *   Este método é chamado para inicializar e processar os dados de estoque de componentes de energia solar.
	 *   Os resultados são impressos diretamente no console para visualização.
	 * @throws CsvRequiredFieldEmptyException 
	 * @throws CsvDataTypeMismatchException 
	 * 
	 * @throws IOException Se houver um problema ao acessar a URL ou ao ler o JSON.
	 */
	
	public static void getEstoque() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		
		try {
			
			// URL do JSON
            URL enderecoEstoque = new URL("https://case-1sbzivi17-henriques-projects-2cf452dc.vercel.app/");

            // Ler o JSON da URL
            InputStreamReader reader = new InputStreamReader(enderecoEstoque.openStream());
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            // Instanciar Gson
            Gson gson = new Gson();

            // Listas para armazenar objetos em suas respectivas classes
            List<Painel> paineisSolares = new ArrayList<>();
            List<Controlador> controladoresDeCarga = new ArrayList<>();
            List<Inversor> inversores = new ArrayList<>();

            // Iterar sobre os objetos no array JSON
            for (JsonElement element : jsonArray) {
                String categoria = element.getAsJsonObject().get("Categoria").getAsString();
                switch (categoria) {
                    case "Painel Solar":
                        Painel painel = gson.fromJson(element, Painel.class);
                        paineisSolares.add(painel);
                        break;
                    case "Controlador de carga":
                        Controlador controlador = gson.fromJson(element, Controlador.class);
                        controladoresDeCarga.add(controlador);
                        break;
                    case "Inversor":
                        Inversor inversor = gson.fromJson(element, Inversor.class);
                        inversores.add(inversor);
                        break;
                    default:
                        System.out.println("Categoria desconhecida: " + categoria);
                        break;
                }
            }

            // Exibir os objetos criados
            System.out.println("Paineis Solares:");
            for (Painel painel : paineisSolares) {
                System.out.println(painel);
            }

            System.out.println("Controladores de Carga:");
            for (Controlador controlador : controladoresDeCarga) {
                System.out.println(controlador);
            }

            System.out.println("Inversores:");
            for (Inversor inversor : inversores) {
                System.out.println(inversor);
            }
            
            List<Gerador> geradores = criarGeradores(paineisSolares, controladoresDeCarga, inversores);

            // Exibir os geradores criados
            System.out.println("Geradores:");
            for (Gerador gerador : geradores) {
                System.out.println(gerador);
            }
			
            criarCsv(geradores);
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	/**
	 * Cria geradores de energia solar utilizando painéis solares, controladores de carga e inversores.
	 * Cada gerador criado consiste de painéis solares da mesma marca e potência, um único controlador de carga
	 * e um único inversor de corrente, todos com potências compatíveis.
	 *
	 * @param paineis       Lista de painéis solares disponíveis para criar geradores.
	 * @param controladores Lista de controladores de carga disponíveis para criar geradores.
	 * @param inversores    Lista de inversores de corrente disponíveis para criar geradores.
	 * @return Lista de objetos Gerador criados com sucesso.
	 */
	
	public static List<Gerador> criarGeradores(List<Painel> paineis, List<Controlador> controladores, List<Inversor> inversores) {
	    List<Gerador> geradores = new ArrayList<>();
	    Random random = new Random();
	    int geradorId = random.nextInt(99999);

	    // Listas auxiliares para controlar inversores e controladores disponíveis
	    List<Inversor> inversoresDisponiveis = new ArrayList<>(inversores);
	    List<Controlador> controladoresDisponiveis = new ArrayList<>(controladores);

	    // Agrupar painéis solares por marca e potência
	    Map<String, Map<Integer, List<Painel>>> paineisPorMarcaEPotencia = paineis.stream()
	            .collect(Collectors.groupingBy(painel -> painel.getProduto().split(" ")[5],
	                    Collectors.groupingBy(Painel::getPotenciaEmW)));

	    // Iterar sobre inversores disponíveis utilizando Iterator
	    Iterator<Inversor> inversorIterator = inversoresDisponiveis.iterator();
	    while (inversorIterator.hasNext()) {
	        Inversor inversor = inversorIterator.next();
	        int potenciaInversor = inversor.getPotenciaEmW();

	        // Iterar sobre controladores disponíveis utilizando Iterator
	        Iterator<Controlador> controladorIterator = controladoresDisponiveis.iterator();
	        while (controladorIterator.hasNext()) {
	            Controlador controlador = controladorIterator.next();
	            int potenciaControlador = controlador.getPotenciaEmW();

	            if (potenciaInversor == potenciaControlador) {
	                for (Map.Entry<String, Map<Integer, List<Painel>>> marcaEntry : paineisPorMarcaEPotencia.entrySet()) {
	                    Map<Integer, List<Painel>> painelPorPotencia = marcaEntry.getValue();

	                    for (Map.Entry<Integer, List<Painel>> potenciaEntry : painelPorPotencia.entrySet()) {
	                        int potenciaPainel = potenciaEntry.getKey();
	                        List<Painel> paineisDisponiveis = potenciaEntry.getValue();
	                        int potenciaTotalPaineis = potenciaPainel * paineisDisponiveis.size();

	                        if (potenciaTotalPaineis >= potenciaInversor) {
	                            List<Painel> paineisSelecionados = new ArrayList<>();
	                            int potenciaAcumulada = 0;

	                            for (Painel painel : paineisDisponiveis) {
	                                if (potenciaAcumulada + painel.getPotenciaEmW() <= potenciaInversor) {
	                                    paineisSelecionados.add(painel);
	                                    potenciaAcumulada += painel.getPotenciaEmW();
	                                }

	                                if (potenciaAcumulada == potenciaInversor) {
	                                    break;
	                                }
	                            }

	                            if (potenciaAcumulada == potenciaInversor) {
	                                Gerador gerador = new Gerador(geradorId++, paineisSelecionados, inversor, controlador);
	                                geradores.add(gerador);

	                                // Remover inversor e controlador das listas de disponíveis
	                                inversorIterator.remove();
	                                controladorIterator.remove();

	                                break; // Sai do loop de painéis por potência, já que encontrou uma combinação válida
	                            }
	                        }
	                    }

	                    if (!geradores.isEmpty()) {
	                        break; // Sai do loop de marca, se já criou um gerador
	                    }
	                }
	            }

	            if (!geradores.isEmpty()) {
	                break; // Sai do loop de controladores, se já criou um gerador
	            }
	        }
	    }

	    return geradores;
	}
	
	
	
	/**
	 * Cria um arquivo CSV a partir de uma lista de objetos do tipo Gerador.
	 *
	 * @param geradores Uma lista contendo objetos do tipo Gerador. Cada Gerador possui informações
	 * sobre seus componentes (painéis, inversor, controlador) e potência total.
	 * @throws CsvDataTypeMismatchException se ocorrer um problema de tipo de dados durante a escrita no CSV.
	 * @throws CsvRequiredFieldEmptyException se um campo obrigatório estiver vazio durante a escrita no CSV.
	 */
	public static void criarCsv(List<Gerador> geradores) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		
		List<CsvDados> csvDataList = new ArrayList<>();
		
		int numComponente = 1;
		
		for (Gerador gerador : geradores) {
            // Incluir painéis
            for (Produto painel : gerador.getPaineis()) {
                csvDataList.add(new CsvDados(gerador.getId(), gerador.getPotenciaTotal(), painel.getId(), painel.getProduto(), gerador.getPaineis().size()));
            }
            // Incluir inversor
            if (gerador.getInversor() != null) {
                csvDataList.add(new CsvDados(gerador.getId(), gerador.getPotenciaTotal(), gerador.getInversor().getId(), gerador.getInversor().getProduto(), numComponente));
            }
            // Incluir controlador
            if (gerador.getControlador() != null) {
                csvDataList.add(new CsvDados(gerador.getId(), gerador.getPotenciaTotal(), gerador.getControlador().getId(), gerador.getControlador().getProduto(), numComponente));
            }
        }
		
		try (Writer writer = new FileWriter("geradores.csv")) {
            StatefulBeanToCsv<CsvDados> beanToCsv = new StatefulBeanToCsvBuilder<CsvDados>(writer).withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
            beanToCsv.write(csvDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}