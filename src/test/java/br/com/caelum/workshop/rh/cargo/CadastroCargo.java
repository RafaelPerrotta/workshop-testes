package br.com.caelum.workshop.rh.cargo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CadastroCargo {
	
	private String urlSistema = "https://workshop-testes.herokuapp.com";
	private WebDriver firefox;
	
	//metodo before e executado antes de cada um dos testes
	@Before
	public void before() {
		//indica o local do driver do firefox:
		System.setProperty("webdriver.gecko.driver", "src/main/resources/drivers/firefox/macos/geckodriver");
		
		//Abre o Firefox:
		firefox = new FirefoxDriver();
	}
	
	//metodo after e executado apos cada um dos testes
	@After
	public void after() {
		firefox.close();
	}
	
	//metodo pra isolar o codigo de logar no sistema
	private void logarNoSistema() {
		//navega pra pagina do login do sistema:
		firefox.navigate().to(urlSistema + "/login");
		
		//encontra o campo de login e preenche com rh:
		firefox.findElement(By.name("username")).sendKeys("rh");
		
		//encontra o campo de senha e preenche com 123456:
		firefox.findElement(By.name("password")).sendKeys("123456");
		
		//encontra o botao de login e clica nele:
		firefox.findElement(By.cssSelector(".btn-primary")).click();
	}
	
	@Test
	public void naoDevePermitirCadastrarCargoComSalarioInferiorAoSalarioMinimoNacional() {
		//chama o metodo para logar no sistema
		logarNoSistema();
		
		//navega pra pagina de cadastro de cargos:
		firefox.navigate().to(urlSistema + "/cargos/form");
		
		//encontra o campo 'Nome' e preenche com 'Cargo teste':
		firefox.findElement(By.name("nome")).sendKeys("Cargo teste");
		
		//encontra o campo 'Salario Minimo' e preenche com '500,00':
		firefox.findElement(By.name("faixaSalarial.salarioMinimo")).sendKeys("500,00");
		
		//encontra o campo 'Salario Maximo' e preenche com '1000,00':
		firefox.findElement(By.name("faixaSalarial.salarioMaximo")).sendKeys("1000,00");
		
		//encontra o botao de gravar e clica nele
		firefox.findElement(By.cssSelector(".btn-primary")).click();
		
		//encontra a mensagem de erro do campo 'Salario Minimo':
		WebElement msgErro = firefox.findElement(By.id("faixaSalarial.salarioMinimo.errors"));
		
		//verifica o texto da mensagem de erro
		Assert.assertTrue(msgErro.getText().equals("Salário Mínimo não pode ser menor do que R$954,00"));
	}
	
	@Test
	public void naoDevePermitirCadastrarCargoComSalarioMinimoMaiorQueSalarioMaximo() {
		//chama o metodo para logar no sistema
		logarNoSistema();
		
		//navega pra pagina de cadastro de cargos:
		firefox.navigate().to(urlSistema + "/cargos/form");
		
		//encontra o campo 'Nome' e preenche com 'Cargo teste':
		firefox.findElement(By.name("nome")).sendKeys("Cargo teste");
		
		//encontra o campo 'Salario Minimo' e preenche com '3000,00':
		firefox.findElement(By.name("faixaSalarial.salarioMinimo")).sendKeys("3000,00");
		
		//encontra o campo 'Salario Maximo' e preenche com '2000,00':
		firefox.findElement(By.name("faixaSalarial.salarioMaximo")).sendKeys("2000,00");
		
		//encontra o botao de gravar e clica nele
		firefox.findElement(By.cssSelector(".btn-primary")).click();
		
		//encontra a mensagem de erro:
		WebElement msgErro = firefox.findElement(By.cssSelector(".alert-danger"));
		
		//verifica o texto da mensagem de erro
		Assert.assertTrue(msgErro.getText().equals("Salário mínimo não pode ser maior que salário máximo!"));
	}
	
	@Test
	public void devePermitirCadastrarCargoComInformacoesCorretas() {
		//chama o metodo para logar no sistema
		logarNoSistema();
		
		//navega pra pagina de cadastro de cargos:
		firefox.navigate().to(urlSistema + "/cargos/form");
		
		//encontra o campo 'Nome' e preenche com 'Cargo teste':
		firefox.findElement(By.name("nome")).sendKeys("Cargo teste");
		
		//encontra o campo 'Salario Minimo' e preenche com '3000,00':
		firefox.findElement(By.name("faixaSalarial.salarioMinimo")).sendKeys("2000,00");
		
		//encontra o campo 'Salario Maximo' e preenche com '2000,00':
		firefox.findElement(By.name("faixaSalarial.salarioMaximo")).sendKeys("3000,00");
		
		//encontra o botao de gravar e clica nele
		firefox.findElement(By.cssSelector(".btn-primary")).click();
		
		//encontra a ultima linha da tabela:
		WebElement ultimaLinhaDaTabela = firefox.findElement(By.cssSelector(".table tbody tr:last-child"));
		
		//encontra a coluna do nome do cargo:
		WebElement colunaNome = ultimaLinhaDaTabela.findElement(By.cssSelector("td:nth-child(2)"));
		
		//verifica o nome do ultimo cargo cadastrado
		Assert.assertTrue(colunaNome.getText().equals("Cargo teste"));
		
		//apaga o cargo, para nao interferir na proxima execucao dos testes
		ultimaLinhaDaTabela.findElement(By.cssSelector(".btn-excluir")).click();
		
		//muda o foco do browser para a popup de confirmacao de exclusao
		Alert popupExclusao = firefox.switchTo().alert();
		
		//clica no botao ok da popup de confirmacao de exclusao
		popupExclusao.accept();
	}

}
