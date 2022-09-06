import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException {
		// File output = new
		// File("C:/Users/Pichau/eclipse-workspace/LerArquivo/japaTest.txt");
		// FileWriter writer = new FileWriter(output);
		// BufferedWriter buff = new BufferedWriter(writer);

		OutputStream os = null;
		FileInputStream inputStream = null;
		Scanner sc = null;
		String line;
		try {
			os = new FileOutputStream(new File("C:/Users/Pichau/eclipse-workspace/LerArquivo/arquivo.txt"));

			inputStream = new FileInputStream("C:/Users/Pichau/eclipse-workspace/LerArquivo/logLu.log");
			sc = new Scanner(inputStream, "UTF-8");

			System.out.println("PADRÂO DE FORMATAÇÂO - Exemplo (25-09-2001 12:32)");

			System.out.println("");

			Scanner sc1 = new Scanner(System.in);
			System.out.println("Digite a data de início: ");
			String dataInicio = sc1.nextLine();

			System.out.println("Digite a data final: ");
			String dataFinal = sc1.nextLine();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			DateTimeFormatter formatterLog = DateTimeFormatter.ofPattern("MMM dd HH:mm:ss 'BRT' yyyy", Locale.ENGLISH)
					.withZone(ZoneId.systemDefault());

			LocalDateTime date1 = LocalDateTime.parse(dataInicio, formatter); // string para data
			LocalDateTime date2 = LocalDateTime.parse(dataFinal, formatter);

			Long dInicial = date1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(); // datas em milisegundos do
																								// tipo long (para fazer
																								// a comparação na linha
																								// 67, se fosse
																								// localDateTime, não
																								// daria)
			Long dFinal = date2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

			TemporalAccessor prevLineDate = null;
			TemporalAccessor lineDate = null;

			while (sc.hasNextLine()) {
				line = sc.nextLine();
				
				if (line.length() < 1) {
					continue;
				}
				
				prevLineDate = lineDate;
				lineDate = TryParseDate(line, formatterLog);
				if (lineDate == null) {
					lineDate = prevLineDate;
				}

				if (lineDate != null) {
					Instant instant = Instant.from(lineDate);
					Long lineTime = Instant.EPOCH.until(instant, ChronoUnit.MILLIS); // pega os milisegundos do Long
																						// para fazer a comparação...

					if (dInicial <= lineTime && dFinal >= lineTime) {
						System.out.println(line);

						os.write(line.getBytes(), 0, line.length());
						os.write("\n".getBytes());
//		        System.out.println(line);
//		        buff.write(line);
//		        buff.newLine();
					}
				}
			}

			// note that Scanner suppresses exceptions
			if (sc.ioException() != null) {
				throw sc.ioException();
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (sc != null) {
				sc.close();
				os.close();
			}
		}
	}

	public static TemporalAccessor TryParseDate(String conteudoLinha, DateTimeFormatter date) {
		try {
			return date.parse(conteudoLinha.substring(4, 28)); // converter para um objeto de data
		} catch (DateTimeParseException ex) {
			return null;
		}
	}
	
	
}
