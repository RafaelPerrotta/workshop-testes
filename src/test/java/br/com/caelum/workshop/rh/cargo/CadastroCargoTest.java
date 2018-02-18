package br.com.caelum.workshop.rh.cargo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class CadastroCargoTest {
	
	private String urlSistema = "https://workshop-testes.herokuapp.com";
	private WebDriver chrome;
	
	//metodo com @Before é executado antes de cada um dos testes
	@Before
	public void before() {
		//indica o local do driver do chrome:
		System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chrome/windows/chromedriver.exe");
		
		//Abre o Chrome:
		chrome = new ChromeDriver();
	}
	
	//metodo com @After é executado apos cada um dos testes
	@After
	public void after() {
		chrome.close();
	}
	
	//metodo pra isolar o codigo de logar no sistema e evitar que esse trecho de codigo se repita em todos os metodos de teste
	private void logarNoSistema() {
		//navega pra pagina do login do sistema:
		chrome.navigate().to(urlSistema + "/login");
		
		//encontra o campo de login e preenche com rh:
		chrome.findElement(By.name("username")).sendKeys("rh");
		
		//encontra o campo de senha e preenche com 123456:
		chrome.findElement(By.name("password")).sendKeys("123456");
		
		//encontra o botao de login e clica nele:
		chrome.findElement(By.cssSelector(".btn-primary")).click();
	}
	
	@Test
	public void naoDevePermitirCadastrarCargoComSalarioInferiorAoSalarioMinimoNacional() {
		//chama o metodo para logar no sistema
		logarNoSistema();
		
		//navega pra pagina de cadastro de cargos:
		chrome.navigate().to(urlSistema + "/cargos/form");
		
		//encontra o campo 'Nome' e preenche com 'Cargo teste':
		chrome.findElement(By.name("nome")).sendKeys("Cargo teste");
		
		//encontra o campo 'Salario Minimo' e preenche com '500,00':
		chrome.findElement(By.name("faixaSalarial.salarioMinimo")).sendKeys("500,00");
		
		//encontra o campo 'Salario Maximo' e preenche com '1000,00':
		chrome.findElement(By.name("faixaSalarial.salarioMaximo")).sendKeys("1000,00");
		
		//encontra o botao de gravar e clica nele
		chrome.findElement(By.cssSelector(".btn-primary")).click();
		
		//encontra a mensagem de erro do campo 'Salario Minimo':
		WebElement msgErro = chrome.findElement(By.id("faixaSalarial.salarioMinimo.errors"));
		
		//verifica o texto da mensagem de erro
		Assert.assertTrue(msgErro.getText().equals("Salário Mínimo não pode ser menor do que R$954,00"));
	}
	
	@Test
	public void naoDevePermitirCadastrarCargoComSalarioMinimoMaiorQueSalarioMaximo() {
		//chama o metodo para logar no sistema
		logarNoSistema();
		
		//navega pra pagina de cadastro de cargos:
		chrome.navigate().to(urlSistema + "/cargos/form");
		
		//encontra o campo 'Nome' e preenche com 'Cargo teste':
		chrome.findElement(By.name("nome")).sendKeys("Cargo teste");
		
		//encontra o campo 'Salario Minimo' e preenche com '3000,00':
		chrome.findElement(By.name("faixaSalarial.salarioMinimo")).sendKeys("3000,00");
		
		//encontra o campo 'Salario Maximo' e preenche com '2000,00':
		chrome.findElement(By.name("faixaSalarial.salarioMaximo")).sendKeys("2000,00");
		
		//encontra o botao de gravar e clica nele
		chrome.findElement(By.cssSelector(".btn-primary")).click();
		
		//encontra a mensagem de erro:
		WebElement msgErro = chrome.findElement(By.cssSelector(".alert-danger"));
		
		//verifica o texto da mensagem de erro
		Assert.assertTrue(msgErro.getText().equals("Salário mínimo não pode ser maior que salário máximo!"));
	}
	
	@Test
	public void devePermitirCadastrarCargoComInformacoesCorretas() {
		//chama o metodo para logar no sistema
		logarNoSistema();
		
		//navega pra pagina de cadastro de cargos:
		chrome.navigate().to(urlSistema + "/cargos/form");
		
		//encontra o campo 'Nome' e preenche com 'Cargo teste':
		chrome.findElement(By.name("nome")).sendKeys("Cargo teste");
		
		//encontra o campo 'Salario Minimo' e preenche com '3000,00':
		chrome.findElement(By.name("faixaSalarial.salarioMinimo")).sendKeys("2000,00");
		
		//encontra o campo 'Salario Maximo' e preenche com '2000,00':
		chrome.findElement(By.name("faixaSalarial.salarioMaximo")).sendKeys("3000,00");
		
		//encontra o botao de gravar e clica nele
		chrome.findElement(By.cssSelector(".btn-primary")).click();
		
		//encontra a ultima linha da tabela:
		WebElement ultimaLinhaDaTabela = chrome.findElement(By.cssSelector(".table tbody tr:last-child"));
		
		//encontra a coluna do nome do cargo:
		WebElement colunaNome = ultimaLinhaDaTabela.findElement(By.cssSelector("td:nth-child(2)"));
		
		//verifica o nome do ultimo cargo cadastrado
		Assert.assertTrue(colunaNome.getText().equals("Cargo teste"));
		
		//apaga o cargo, para nao interferir na proxima execucao dos testes
		ultimaLinhaDaTabela.findElement(By.cssSelector(".btn-excluir")).click();
		
		//muda o foco do browser para a popup de confirmacao de exclusao
		Alert popupExclusao = chrome.switchTo().alert();
		
		//clica no botao ok da popup de confirmacao de exclusao
		popupExclusao.accept();
	}

}
