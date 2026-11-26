package MatrixCalculator.cli;

import MatrixCalculator.Matrix;
import MatrixCalculator.analyzer.MatrixAnalyzer;
import MatrixCalculator.analyzer.MatrixProperty;
import MatrixCalculator.exception.MatrixDimensionException;
import MatrixCalculator.exception.SingularMatrixException;

import java.io.PrintStream;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public final class MatrixCli {
    private static final String HELP = """
            MatrixCalculator
            Uso: java -jar MatrixCalculator-1.0-SNAPSHOT.jar <operação> <matriz> [argumento]

            Separe os valores por espaço e as linhas por ponto e vírgula.
            Coloque cada matriz entre aspas. Use ponto ou vírgula nos decimais.

            Operações com uma matriz:
              transpor, determinante, inversa, traco, analisar
              norma-frobenius, norma-1, norma-infinito

            Operações com duas matrizes:
              somar, subtrair, multiplicar, resolver
              Em resolver, informe A e B para calcular X em AX = B.

            Operações com uma matriz e um número:
              escalar   (fator)
              potencia (expoente inteiro não negativo)

            Exemplos:
              java -jar MatrixCalculator-1.0-SNAPSHOT.jar determinante "4 1; 2 3"
              java -jar MatrixCalculator-1.0-SNAPSHOT.jar resolver "4 1; 2 3" "6; 8"
              java -jar MatrixCalculator-1.0-SNAPSHOT.jar escalar "1 2; 3 4" 0,5
            """;

    private MatrixCli() {
    }

    public static void main(String[] args) {
        int exitCode = run(args, System.out, System.err);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    static int run(String[] args, PrintStream output, PrintStream errors) {
        if (args.length == 0 || (args.length == 1 && (args[0].equals("--help") || args[0].equals("-h")))) {
            output.print(HELP);
            return 0;
        }

        try {
            Operation operation = Operation.parse(args[0]);
            if (args.length != operation.operandCount + 1) {
                throw new IllegalArgumentException("A operação " + args[0]
                        + " espera " + operation.operandCount + " argumento(s).");
            }
            Matrix matrix = parseMatrix(args[1]);
            String result = switch (operation) {
                case SOMAR -> formatMatrix(matrix.add(parseMatrix(args[2])));
                case SUBTRAIR -> formatMatrix(matrix.subtract(parseMatrix(args[2])));
                case MULTIPLICAR -> formatMatrix(matrix.multiply(parseMatrix(args[2])));
                case RESOLVER -> formatMatrix(matrix.solve(parseMatrix(args[2])));
                case ESCALAR -> formatMatrix(matrix.multiply(parseNumber(args[2])));
                case POTENCIA -> formatMatrix(matrix.pow(parseExponent(args[2])));
                case TRANSPOR -> formatMatrix(matrix.transpose());
                case INVERSA -> formatMatrix(matrix.inverse());
                case DETERMINANTE -> Double.toString(matrix.determinant());
                case TRACO -> Double.toString(matrix.trace());
                case NORMA_FROBENIUS -> Double.toString(matrix.normFrobenius());
                case NORMA_1 -> Double.toString(matrix.normL1());
                case NORMA_INFINITO -> Double.toString(matrix.normInfinity());
                case ANALISAR -> MatrixAnalyzer.analyze(matrix).getProperties().stream()
                        .map(MatrixCli::propertyLabel)
                        .collect(Collectors.joining(", "));
            };
            output.println(result);
            return 0;
        } catch (SingularMatrixException exception) {
            errors.println("Erro: a matriz é singular. A operação exige uma matriz invertível.");
        } catch (MatrixDimensionException exception) {
            errors.println("Erro: as dimensões das matrizes são incompatíveis com essa operação.");
        } catch (IllegalArgumentException exception) {
            errors.println("Erro: " + exception.getMessage());
        }
        errors.println("Use --help para ver os comandos e exemplos.");
        return 2;
    }

    private static Matrix parseMatrix(String input) {
        String[] rows = input.split(";", -1);
        double[][] values = new double[rows.length][];
        for (int row = 0; row < rows.length; row++) {
            String line = rows[row].strip();
            if (line.isEmpty()) {
                throw new IllegalArgumentException("A linha " + (row + 1) + " da matriz está vazia.");
            }
            String[] columns = line.split("\\s+");
            if (row > 0 && columns.length != values[0].length) {
                throw new IllegalArgumentException("Todas as linhas da matriz devem ter o mesmo número de colunas.");
            }
            values[row] = new double[columns.length];
            for (int column = 0; column < columns.length; column++) {
                values[row][column] = parseNumber(columns[column]);
            }
        }
        return new Matrix(values);
    }

    private static double parseNumber(String input) {
        double value;
        try {
            value = Double.parseDouble(input.replace(',', '.'));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Número inválido: " + input);
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Informe um número finito: " + input);
        }
        return value;
    }

    private static int parseExponent(String input) {
        try {
            int exponent = Integer.parseInt(input);
            if (exponent >= 0) {
                return exponent;
            }
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Expoente inválido. Informe um inteiro entre 0 e " + Integer.MAX_VALUE + ".");
        }
        throw new IllegalArgumentException("O expoente deve ser maior ou igual a zero.");
    }

    private static String formatMatrix(Matrix matrix) {
        StringJoiner rows = new StringJoiner(System.lineSeparator());
        for (int row = 0; row < matrix.getRows(); row++) {
            StringJoiner columns = new StringJoiner(" ");
            for (int column = 0; column < matrix.getCols(); column++) {
                columns.add(Double.toString(matrix.get(row, column)));
            }
            rows.add(columns.toString());
        }
        return rows.toString();
    }

    private static String propertyLabel(MatrixProperty property) {
        return switch (property) {
            case ZERO -> "nula";
            case IDENTITY -> "identidade";
            case DIAGONAL -> "diagonal";
            case SCALAR -> "escalar";
            case UPPER_TRIANGULAR -> "triangular superior";
            case LOWER_TRIANGULAR -> "triangular inferior";
            case ROW -> "linha";
            case COLUMN -> "coluna";
            case SQUARE -> "quadrada";
            case NON_SQUARE -> "retangular não quadrada";
            case SYMMETRIC -> "simétrica";
            case ANTISYMMETRIC -> "antissimétrica";
        };
    }

    private enum Operation {
        SOMAR(2),
        SUBTRAIR(2),
        MULTIPLICAR(2),
        RESOLVER(2),
        ESCALAR(2),
        POTENCIA(2),
        TRANSPOR(1),
        INVERSA(1),
        DETERMINANTE(1),
        TRACO(1),
        NORMA_FROBENIUS(1),
        NORMA_1(1),
        NORMA_INFINITO(1),
        ANALISAR(1);

        private final int operandCount;

        Operation(int operandCount) {
            this.operandCount = operandCount;
        }

        private static Operation parse(String input) {
            try {
                return valueOf(input.toUpperCase(Locale.ROOT).replace('-', '_'));
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException("Operação desconhecida: " + input);
            }
        }
    }
}
