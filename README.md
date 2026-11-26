# MatrixCalculator

Calculadora de matrizes para terminal e biblioteca Java de precisão dupla. O projeto foi criado para praticar arquitetura de software e cálculo matricial.

## Usar a calculadora

Você precisa de Java 17 ou superior. Use `java -version` para conferir a versão instalada. Abra um terminal na pasta do arquivo `MatrixCalculator-1.0-SNAPSHOT.jar` e rode o comando de ajuda.

```sh
java -jar MatrixCalculator-1.0-SNAPSHOT.jar --help
```

No Windows, se o terminal usar uma versão mais antiga, execute o `java.exe` da pasta `bin` do JDK 17 ou priorize essa pasta no `PATH`.

Coloque cada matriz entre aspas, separe os elementos de uma linha por espaços e use ponto e vírgula para começar outra linha. Os números decimais aceitam ponto ou vírgula.

```sh
java -jar MatrixCalculator-1.0-SNAPSHOT.jar determinante "4 1; 2 3"
java -jar MatrixCalculator-1.0-SNAPSHOT.jar resolver "4 1; 2 3" "6; 8"
java -jar MatrixCalculator-1.0-SNAPSHOT.jar escalar "1 2; 3 4" 0,5
```

O primeiro comando imprime `10.0`. O segundo resolve `AX = B` e imprime `1.0` e `2.0`, em linhas separadas. A ajuda lista todas as operações. Os erros aparecem no fluxo de erros do terminal e retornam o código de saída 2.

## Compilar a partir do código-fonte

- JDK 17
- Apache Maven com o JDK 17 configurado em `JAVA_HOME`

```sh
mvn verify
java -jar target/MatrixCalculator-1.0-SNAPSHOT.jar --help
```

O comando `mvn verify` executa os testes JUnit e gera o arquivo `target/MatrixCalculator-1.0-SNAPSHOT.jar`. A mesma verificação está configurada no GitHub Actions para pushes e pull requests.

O código da [calculadora de terminal](src/main/java/MatrixCalculator/cli/MatrixCli.java) chama a API de [operações com matrizes](src/main/java/MatrixCalculator/Matrix.java). Os [testes](src/test/java) incluem exemplos de uso da biblioteca e do terminal.

## Exemplo em Java

```java
import MatrixCalculator.Matrix;
import MatrixCalculator.analyzer.MatrixAnalyzer;

Matrix a = new Matrix(new double[][] {{4, 1}, {2, 3}});
Matrix b = new Matrix(new double[][] {{6}, {8}});
Matrix x = a.solve(b);

System.out.println(x.get(0, 0)); // 1.0
System.out.println(x.get(1, 0)); // 2.0
System.out.println(a.determinant()); // 10.0
System.out.println(MatrixAnalyzer.analyze(a).isSquare()); // true
```

## API

- `Matrix` oferece soma, subtração, multiplicação matricial e escalar, transposição e normas de Frobenius, 1 e infinito. Os índices começam em zero. O construtor e `getData()` fazem cópias defensivas do array.
- Matrizes quadradas também permitem calcular traço, determinante, inversa e potências inteiras não negativas, além de resolver sistemas `AX = B`.
- Soma, subtração e multiplicação matricial lançam `MatrixDimensionException` para dimensões incompatíveis.
- A fatoração LU usa pivotamento parcial. Um pivô zero durante a fatoração produz determinante zero e lança `SingularMatrixException` em `solve()` ou `inverse()`. Matrizes mal condicionadas podem produzir resultados imprecisos em ponto flutuante.
- `MatrixAnalyzer` identifica propriedades como simetria, diagonalidade e triangularidade usando tolerância absoluta de `1e-11`.
