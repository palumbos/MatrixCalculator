# MatrixCalculator

Biblioteca Java para operações com matrizes de precisão dupla. O projeto foi criado para praticar arquitetura de software e cálculo matricial.

## Requisitos

- JDK 17
- Apache Maven com o JDK 17 configurado em `JAVA_HOME`

## Compilar e testar

```sh
mvn verify
```

Esse comando executa os testes JUnit e gera o arquivo `target/MatrixCalculator-1.0-SNAPSHOT.jar`. A mesma verificação está configurada no GitHub Actions para pushes e pull requests.

## Exemplo

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
