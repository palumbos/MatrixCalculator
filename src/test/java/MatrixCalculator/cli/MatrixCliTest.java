package MatrixCalculator.cli;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class MatrixCliTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errors = new ByteArrayOutputStream();

    @Test
    void showsHelpWhenRunWithoutArguments() {
        assertEquals(0, run());
        assertTrue(output.toString(StandardCharsets.UTF_8).contains("resolver"));
        assertEquals(0, errors.size());
    }

    @Test
    void solvesSystemWithTwoQuotedMatrixArgumentsAndDecimalComma() {
        assertEquals(0, run("resolver", "4 1; 2 3", "6,0; 8.0"));
        assertEquals("1.0\n2.0", output.toString(StandardCharsets.UTF_8).strip().replace("\r\n", "\n"));
        assertEquals(0, errors.size());
    }

    @ParameterizedTest
    @MethodSource("invalidCommands")
    void invalidCommandsWriteAnErrorWithoutPrintingAResult(String[] args, String expectedError) {
        assertEquals(2, run(args));
        assertEquals(0, output.size());
        String errorText = errors.toString(StandardCharsets.UTF_8);
        assertTrue(errorText.contains(expectedError), errorText);
        assertTrue(errorText.contains("--help"), errorText);
    }

    private static Stream<Arguments> invalidCommands() {
        return Stream.of(
                arguments(new String[]{"dividir", "1"}, "Operação desconhecida"),
                arguments(new String[]{"resolver", "4 1; 2 3"}, "espera 2 argumento(s)"),
                arguments(new String[]{"transpor", "1 2;"}, "linha 2 da matriz está vazia"),
                arguments(new String[]{"transpor", "1 2; 3"}, "mesmo número de colunas"),
                arguments(new String[]{"escalar", "1 2", "NaN"}, "número finito"),
                arguments(new String[]{"potencia", "1", "-1"}, "maior ou igual a zero"),
                arguments(new String[]{"somar", "1 2", "1; 2"}, "dimensões das matrizes são incompatíveis"),
                arguments(new String[]{"inversa", "1 2; 2 4"}, "matriz é singular")
        );
    }

    private int run(String... args) {
        return MatrixCli.run(args, new PrintStream(output, true, StandardCharsets.UTF_8),
                new PrintStream(errors, true, StandardCharsets.UTF_8));
    }
}
